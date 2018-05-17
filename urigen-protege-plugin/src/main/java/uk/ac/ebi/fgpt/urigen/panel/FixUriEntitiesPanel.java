package uk.ac.ebi.fgpt.urigen.panel;

import org.protege.editor.core.ui.util.CheckTable;
import org.protege.editor.core.ui.util.InputVerificationStatusChangedListener;
import org.protege.editor.core.ui.util.VerifiedInputEditor;
import org.protege.editor.owl.OWLEditorKit;
import org.protege.editor.owl.model.entity.OWLEntityCreationException;
import org.protege.editor.owl.model.entity.OWLEntityCreationSet;
import org.protege.editor.owl.ui.error.ErrorPanel;
import org.protege.editor.owl.ui.renderer.OWLCellRenderer;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.OWLEntityRenamer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.ebi.fgpt.urigen.entity.UrigenEntityFactory;

import javax.swing.*;
import javax.swing.Timer;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.JTextComponent;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.regex.Pattern;

/**
 * @author Simon Jupp
 * @date 06/10/2014
 * Samples, Phenotypes and Ontologies Team, EMBL-EBI
 */
public class FixUriEntitiesPanel extends JPanel implements VerifiedInputEditor {


    private final Logger logger = LoggerFactory.getLogger(FixUriEntitiesPanel.class);
    private static final int SEARCH_PAUSE_MILLIS = 1000;

    private OWLEditorKit eKit;

    private Map<String, Set<OWLEntity>> nsMap = new HashMap<String, Set<OWLEntity>>();

    private JComboBox findCombo;
    private CheckTable<OWLEntity> list;
    private Map<OWLEntity, String> errorMap = new HashMap<OWLEntity, String>();

    private Map<OWLEntity, IRI> entity2IRIMap = new HashMap<OWLEntity, IRI>();

    private UrigenEntityFactory urigenEntityFactory;
    private static String defaultBaseURI = "http://urigen_local_uri/";

    private ItemListener findListener = new ItemListener(){
        public void itemStateChanged(ItemEvent event) {
            if (event.getStateChange() == ItemEvent.SELECTED){
                reloadEntityListThreaded();
            }
        }
    };


    private ListSelectionListener listSelListener = new ListSelectionListener(){
        public void valueChanged(ListSelectionEvent event) {
            handleStateChanged();
        }
    };

    public FixUriEntitiesPanel(OWLEditorKit eKit) {
        setLayout(new BorderLayout(6, 6));
        this.eKit = eKit;

        urigenEntityFactory = new UrigenEntityFactory(eKit.getModelManager());

        buildEntityNamespaceMap();

        JComponent subPanel = new JPanel();
        subPanel.setBorder(new TitledBorder("Assign new URIs from Urigen"));
        subPanel.setLayout(new BorderLayout());

        findCombo = createCombo("Find", findListener, subPanel, BorderLayout.NORTH);

        add(subPanel, BorderLayout.NORTH);

        list = new CheckTable<OWLEntity>("Matching entities");
        list.checkAll(true);
        list.setDefaultRenderer(new ResultCellRenderer(eKit));
        list.addCheckSelectionListener(listSelListener);

        add(new JScrollPane(list), BorderLayout.CENTER);
    }

    private JComboBox createCombo(String title, final ItemListener listener, JComponent parent, String constraints) {
        final JComboBox combo = new JComboBox(nsMap.keySet().toArray());
        combo.addItem("");
        combo.setSelectedItem("");
        combo.setEditable(true);
        combo.addItemListener(listener);

        final JTextComponent editor = (JTextComponent) combo.getEditor().getEditorComponent();

        final ActionListener actionListener = new ActionListener(){
            public void actionPerformed(ActionEvent actionEvent) {
                combo.setSelectedItem(editor.getText());
            }
        };

        final Timer timer = new Timer(SEARCH_PAUSE_MILLIS, actionListener);

        editor.getDocument().addDocumentListener(new DocumentListener(){
            public void insertUpdate(DocumentEvent event) {
                timer.restart();
            }

            public void removeUpdate(DocumentEvent event) {
                timer.restart();
            }

            public void changedUpdate(DocumentEvent event) {
            }
        });
        JComponent panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panel.add(new JLabel(title));
        panel.add(combo);
        parent.add(panel, constraints);
        return combo;
    }


    private void buildEntityNamespaceMap() {
        for (OWLOntology ont : getOntologies()){
            for (OWLEntity entity : ont.getSignature()){
                extractNSFromEntity(entity);
            }
        }
    }


    private void extractNSFromEntity(OWLEntity entity) {
        String ns = getBase(entity.getIRI());
        Set<OWLEntity> matchingEntities = nsMap.get(ns);
        if (matchingEntities == null){
            matchingEntities = new HashSet<OWLEntity>();
        }
        matchingEntities.add(entity);
        nsMap.put(ns, matchingEntities);
    }


    public String getFindValue(){
        return (String) findCombo.getSelectedItem();
    }

    public java.util.List<OWLEntity> getSelectedEntities(){
        return list.getFilteredValues();
    }


    public java.util.List<OWLOntologyChange> getChanges() {
        OWLOntologyManager mngr = eKit.getModelManager().getOWLOntologyManager();
        OWLEntityRenamer renamer = new OWLEntityRenamer(mngr, getOntologies());
        Map<OWLEntity, IRI> filteredIRIMap = new HashMap<OWLEntity, IRI>();


        for (OWLEntity e : list.getFilteredValues()) {
            String rendering = eKit.getOWLModelManager().getRendering(((OWLObject) e));
            OWLEntityCreationSet<OWLClass> entitySet = null;
            try {
                entitySet = urigenEntityFactory.createOWLClass(rendering, null);
                filteredIRIMap.put(e, entitySet.getOWLEntity().getIRI());
            } catch (OWLEntityCreationException e1) {
                this.logger.error("Error renaming entity: " + e + ":"+ e1.getMessage());
            }
        }
        return renamer.changeIRI(filteredIRIMap);
    }


    private void reloadEntityList() {
        final ArrayList<OWLEntity> sortedEntities = new ArrayList<OWLEntity>(getEntities());
        Collections.sort(sortedEntities, eKit.getModelManager().getOWLObjectComparator());
        list.getModel().setData(sortedEntities, true);
        updateEntityMap();
        handleStateChanged();
    }

    	/*
    	 * The getEntities() and the updateEntityMap should be consistent with one another.
    	 * Currently both assume that the user is changing a namespace prefix if the findValue
    	 * is found in the namespace map but is using a regular expression otherwise.
    	 *
    	 * The other assumption that we will make is that the entity2IRIMap is unfiltered by the
    	 * changes list.
    	 */

    private Set<OWLEntity> getEntities(){
        Set<OWLEntity> matches = nsMap.get(getFindValue());
        if (matches == null){
            matches = new HashSet<OWLEntity>();
            Set<OWLEntity> ents = new HashSet<OWLEntity>();
            for (OWLOntology ont : getOntologies()){
                ents.addAll(ont.getSignature());
            }
            String matchingVal = ".*" + getFindValue() + ".*";
            Pattern p = Pattern.compile(matchingVal);
            for (OWLEntity ent : ents){
                if (p.matcher(ent.getIRI().toString()).matches()){
                    matches.add(ent);
                }
            }
        }
        return matches;
    }

    private void updateEntityMap() {
        entity2IRIMap.clear();
        errorMap.clear();
        Set<OWLEntity> matches = nsMap.get(getFindValue());
        updateEntityMapUsingRegexp();
        list.repaint();
    }


    private void updateEntityMapUsingRegexp() {
        Set<OWLEntity> entities = new HashSet<OWLEntity>();
        for (OWLOntology includedOntology : getOntologies()) {
            entities.addAll(includedOntology.getSignature());
        }
        for (OWLEntity entity : entities){
            UUID random = UUID.randomUUID();
            addToEntityMap(entity, defaultBaseURI + random.toString());
        }
    }

    private void addToEntityMap(OWLEntity entity, String newURIStr) {
        try {
            URI newURI = new URI(newURIStr);
            if (!newURI.isAbsolute()){
                throw new URISyntaxException(newURIStr, "IRI must be absolute");
            }
            entity2IRIMap.put(entity, IRI.create(newURI));
        }
        catch (URISyntaxException e) {
            errorMap.put(entity, newURIStr);
        }
    }

    private Set<OWLOntology> getOntologies() {
        return eKit.getModelManager().getOntologies();
    }


    private String getBase(IRI uri){
        String frag = getShortForm(uri);
        final String uriStr = uri.toString();
        return uriStr.substring(0, uriStr.lastIndexOf(frag));
    }


    private String getShortForm(IRI uri){
        try {
            String rendering = uri.getFragment();
            if (rendering == null) {
                // Get last bit of path
                String path = uri.toURI().getPath();
                if (path == null) {
                    return uri.toString();
                }
                return uri.toURI().getPath().substring(path.lastIndexOf("/") + 1);
            }
            return rendering;
        }
        catch (Exception e) {
            return "<Error! " + e.getMessage() + ">";
        }
    }

    /////////////////////// validation

    private java.util.List<InputVerificationStatusChangedListener> statusListeners = new ArrayList<InputVerificationStatusChangedListener>();

    private boolean currentStatus = false;

    public void addStatusChangedListener(InputVerificationStatusChangedListener listener) {
        statusListeners.add(listener);
        listener.verifiedStatusChanged(currentStatus);
    }


    public void removeStatusChangedListener(InputVerificationStatusChangedListener listener) {
        statusListeners.remove(listener);
    }


    private void handleStateChanged() {
        boolean valid = getStatus();
        if (currentStatus != valid){
            currentStatus = valid;
            for (InputVerificationStatusChangedListener l : statusListeners){
                l.verifiedStatusChanged(currentStatus);
            }
        }
    }


    private boolean getStatus() {
        return findCombo.getSelectedItem() != null &&!findCombo.getSelectedItem().equals("") &&
                !list.getFilteredValues().isEmpty() && errorMap.isEmpty();
    }


    //////////////////// reload threading

    private Thread reloadThread;

    private Runnable reloadProcess = new Runnable(){
        public void run() {
            reloadEntityList();
            reloadThread = null;
        }
    };


    private void reloadEntityListThreaded(){
        if (reloadThread != null && reloadThread.isAlive()){
            reloadThread.interrupt();
        }

        reloadThread = new Thread(reloadProcess);

        reloadThread.run();
    }


    public JComponent getFocusComponent() {
        return (JComponent)findCombo.getEditor().getEditorComponent();
    }


    /**
     * Should highlight the matching text in the URI
     */
    class ResultCellRenderer extends OWLCellRenderer {

        private boolean inURI = false;

        private Style fadedStyle = null;

        private Style highlightedStyle = null;


        public ResultCellRenderer(OWLEditorKit owlEditorKit) {
            super(owlEditorKit);
        }


        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if (errorMap.containsKey(value)){
                setStrikeThrough(true);
            }
            else{
                setStrikeThrough(false);
            }
            return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        }


        protected String getRendering(Object object) {
            if (object instanceof OWLEntity){
                return super.getRendering(object) + " (" + ((OWLEntity)object).getIRI() + ")";
            }
            return super.getRendering(object);
        }


        protected void renderToken(String curToken, int tokenStartIndex, StyledDocument doc) {
            super.renderToken(curToken, tokenStartIndex, doc, false);
            if (logger.isDebugEnabled()) {
                logger.debug("curToken = " + curToken);
            }
            if (curToken.startsWith("(")){
                inURI = true;
            }
            if (curToken.equals(")")){
                inURI = false;
            }
            else if(inURI){
                if (fadedStyle == null){
                    fadedStyle = doc.addStyle("MY_FADED_STYLE", null);
                    StyleConstants.setForeground(fadedStyle, Color.GRAY);
                }
                doc.setCharacterAttributes(tokenStartIndex, doc.getLength()-tokenStartIndex, fadedStyle, false);

                if (highlightedStyle == null){
                    highlightedStyle = doc.addStyle("MY_HIGHLIGHTED_STYLE", null);
                    StyleConstants.setForeground(highlightedStyle, Color.darkGray);
                    StyleConstants.setBold(highlightedStyle, true);
                }
                final String s = getFindValue().toLowerCase();
                curToken = curToken.toLowerCase();
                if (!s.isEmpty()) {
                    int cur = 0;
                    do {
                        cur = curToken.indexOf(s, cur);
                        if (cur != -1){
                            doc.setCharacterAttributes(tokenStartIndex + cur,  s.length(), highlightedStyle, true);
                            cur++;
                        }
                    }
                    while (cur != -1);
                }

            }
        }
    }
}
