package uk.ac.ebi.fgpt.urigen.panel;

import org.protege.editor.core.ui.util.JOptionPaneEx;
import org.protege.editor.owl.model.entity.CustomOWLEntityFactory;
import org.protege.editor.owl.ui.preferences.OWLPreferencesPanel;
import org.semanticweb.owlapi.model.IRI;
import uk.ac.ebi.fgpt.urigen.entity.UrigenEntityFactory;
import uk.ac.ebi.fgpt.urigen.entity.UrigenPreference;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

public class UrigenPreferencePanel extends OWLPreferencesPanel {
    private final JCheckBox useUrigenServerCheckBox = new JCheckBox("Use Urigen Server");
    private final ButtonGroup bg = new ButtonGroup();
    private final JRadioButton dynamicRadio = new JRadioButton("Dynamic URIs");
    private final JRadioButton manualRadio = new JRadioButton("Manual URIs");
    private JButton updateNow = new JButton("Update now");
    private JToolBar toolbar = new JToolBar();
    private final PreferenceOptionTable table = new PreferenceOptionTable();

    private final Action addAction = new AbstractAction("Add preference", UrigenIcons.getIcon("preference.add.png")) {
        public void actionPerformed(ActionEvent actionEvent) {
            UrigenPreferencePanel.this.handleAddNewPref();
        }
    };

    private final Action removeAction = new AbstractAction("Remove preference", UrigenIcons.getIcon("preference.remove.png")) {
        public void actionPerformed(ActionEvent actionEvent) {
            UrigenPreferencePanel.this.handleRemovePref();
        }
    };

    public void applyChanges() {
        if (this.useUrigenServerCheckBox.isSelected()) {
            UrigenPreference.setUseUrigen(true);
            getOWLEditorKit().getOWLModelManager().setOWLEntityFactory(new UrigenEntityFactory(getOWLModelManager()));
        }
        else {
            UrigenPreference.setUseUrigen(false);
            getOWLEditorKit().getOWLModelManager().setOWLEntityFactory(new CustomOWLEntityFactory(getOWLModelManager()));
        }
    }

    public void rebuidTable() {
        this.table.getOptions().clear();
        List options = new ArrayList();

        for (String ontologyIriString : UrigenPreference.getAllOntologyIris()) {
            IRI ontoIri = IRI.create(ontologyIriString);
            UrigenPreferenceOption opt = new UrigenPreferenceOption(ontoIri, UrigenPreference.getServerUrlByOntologyIri(ontoIri), UrigenPreference.getNameByOntologyIri(ontoIri), UrigenPreference.getApiKeyByOntologyIRI(ontoIri));

            options.add(opt);
        }

        this.table.getOptions().addAll(options);
        this.table.fireTableDataChanged();
    }

    public void initialise()
            throws Exception {
        setLayout(new BorderLayout());

        this.bg.add(this.dynamicRadio);
        this.bg.add(this.manualRadio);
        add(this.useUrigenServerCheckBox, "North");

        if (UrigenPreference.getUseUrigen()) {
            this.useUrigenServerCheckBox.setSelected(true);
        }

        rebuidTable();

        JScrollPane pane = new JScrollPane(this.table);
        pane.setMinimumSize(new Dimension(800, 800));

        this.toolbar = new JToolBar();
        this.toolbar.setFloatable(false);
        addToolbarAction(this.addAction);
        addToolbarAction(this.removeAction);
        this.toolbar.addSeparator(new Dimension(6, 6));

        JPanel centrePanel = new JPanel(new BorderLayout());

        centrePanel.add(this.toolbar, "North");
        centrePanel.add(pane, "Center");

        add(centrePanel, "Center");
    }

    private void addToolbarAction(Action action) {
        JButton button = new JButton(action);
        button.setToolTipText((String) action.getValue("Name"));
        button.setText(null);
        button.setBorder(new EmptyBorder(4, 4, 4, 4));
        this.toolbar.add(button);
    }

    private void handleAddNewPref() {
        AddPreferenceOptionPanel panel = new AddPreferenceOptionPanel();
        int ret = JOptionPaneEx.showValidatingConfirmDialog(this, "Select URIgen server...", panel, -1, 2, panel);

        if (ret == 0) {
            UrigenPreference.save(panel.getOntologyIRI(), panel.getServerlUrl(), panel.getPrefName(), panel.getApiKey());

            rebuidTable();
        }
    }

    private void handleRemovePref() {
        UrigenPreferenceOption option = this.table.getRow(this.table.getSelectedRow());
        UrigenPreference.remove(option.getOntologyIri());
        rebuidTable();
    }

    public void dispose()
            throws Exception {
    }
}

/* Location:           /Users/jupp/Downloads/org.protege.urigen.jar
 * Qualified Name:     uk.ac.ebi.fgpt.urigen.panel.UrigenPreferencePanel
 * JD-Core Version:    0.6.1
 */