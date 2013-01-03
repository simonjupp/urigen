/*     */ package uk.ac.ebi.fgpt.urigen.panel;
/*     */ 
/*     */ import java.awt.BorderLayout;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import javax.swing.AbstractAction;
/*     */ import javax.swing.Action;
/*     */ import javax.swing.ButtonGroup;
/*     */ import javax.swing.Icon;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JCheckBox;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JRadioButton;
/*     */ import javax.swing.JScrollPane;
/*     */ import javax.swing.JToolBar;
/*     */ import javax.swing.border.EmptyBorder;
/*     */ import org.protege.editor.core.ui.util.JOptionPaneEx;
/*     */ import org.protege.editor.owl.OWLEditorKit;
/*     */ import org.protege.editor.owl.model.OWLModelManager;
/*     */ import org.protege.editor.owl.model.entity.CustomOWLEntityFactory;
/*     */ import org.protege.editor.owl.ui.preferences.OWLPreferencesPanel;
/*     */ import org.semanticweb.owlapi.model.IRI;
/*     */ import uk.ac.ebi.fgpt.urigen.entity.UrigenEntityFactory;
/*     */ import uk.ac.ebi.fgpt.urigen.entity.UrigenPreference;
/*     */ 
/*     */ public class UrigenPreferencePanel extends OWLPreferencesPanel
/*     */ {
/*  24 */   private JCheckBox useUrigenServerCheckBox = new JCheckBox("Use Urigen Server");
/*  25 */   private ButtonGroup bg = new ButtonGroup();
/*  26 */   private JRadioButton dynamicRadio = new JRadioButton("Dynamic URIs");
/*  27 */   private JRadioButton manualRadio = new JRadioButton("Manual URIs");
/*  28 */   private JButton updateNow = new JButton("Update now");
/*  29 */   private JToolBar toolbar = new JToolBar();
/*  30 */   private PreferenceOptionTable table = new PreferenceOptionTable();
/*     */ 
/*  34 */   private Action addAction = new AbstractAction("Add preference", UrigenIcons.getIcon("preference.add.png")) {
/*     */     public void actionPerformed(ActionEvent actionEvent) {
/*  36 */       UrigenPreferencePanel.this.handleAddNewPref();
/*     */     }
/*  34 */   };
/*     */ 
/*  40 */   private Action removeAction = new AbstractAction("Remove preference", UrigenIcons.getIcon("preference.remove.png")) {
/*     */     public void actionPerformed(ActionEvent actionEvent) {
/*  42 */       UrigenPreferencePanel.this.handleRemovePref();
/*     */     }
/*  40 */   };
/*     */ 
/*     */   public void applyChanges()
/*     */   {
/*  50 */     if (this.useUrigenServerCheckBox.isSelected()) {
/*  51 */       UrigenPreference.setUseUrigen(true);
/*  52 */       getOWLEditorKit().getOWLModelManager().setOWLEntityFactory(new UrigenEntityFactory(getOWLModelManager()));
/*     */     }
/*     */     else {
/*  55 */       UrigenPreference.setUseUrigen(false);
/*  56 */       getOWLEditorKit().getOWLModelManager().setOWLEntityFactory(new CustomOWLEntityFactory(getOWLModelManager()));
/*     */     }
/*     */   }
/*     */ 
/*     */   public void rebuidTable()
/*     */   {
/*  63 */     this.table.getOptions().clear();
/*  64 */     List options = new ArrayList();
/*     */ 
/*  66 */     for (String ontologyIriString : UrigenPreference.getAllOntologyIris())
/*     */     {
/*  68 */       IRI ontoIri = IRI.create(ontologyIriString);
/*  69 */       UrigenPreferenceOption opt = new UrigenPreferenceOption(ontoIri, UrigenPreference.getServerUrlByOntologyIri(ontoIri), UrigenPreference.getNameByOntologyIri(ontoIri), UrigenPreference.getApiKeyByOntologyIRI(ontoIri));
/*     */ 
/*  74 */       options.add(opt);
/*     */     }
/*     */ 
/*  77 */     this.table.getOptions().addAll(options);
/*  78 */     this.table.fireTableDataChanged();
/*     */   }
/*     */ 
/*     */   public void initialise()
/*     */     throws Exception
/*     */   {
/*  85 */     setLayout(new BorderLayout());
/*     */ 
/*  87 */     this.bg.add(this.dynamicRadio);
/*  88 */     this.bg.add(this.manualRadio);
/*  89 */     add(this.useUrigenServerCheckBox, "North");
/*     */ 
/*  91 */     if (UrigenPreference.getUseUrigen()) {
/*  92 */       this.useUrigenServerCheckBox.setSelected(true);
/*     */     }
/*     */ 
/*  95 */     rebuidTable();
/*     */ 
/*  97 */     JScrollPane pane = new JScrollPane(this.table);
/*  98 */     pane.setMinimumSize(new Dimension(800, 800));
/*     */ 
/* 101 */     this.toolbar = new JToolBar();
/* 102 */     this.toolbar.setFloatable(false);
/* 103 */     addToolbarAction(this.addAction);
/* 104 */     addToolbarAction(this.removeAction);
/* 105 */     this.toolbar.addSeparator(new Dimension(6, 6));
/*     */ 
/* 107 */     JPanel centrePanel = new JPanel(new BorderLayout());
/*     */ 
/* 109 */     centrePanel.add(this.toolbar, "North");
/* 110 */     centrePanel.add(pane, "Center");
/*     */ 
/* 112 */     add(centrePanel, "Center");
/*     */   }
/*     */ 
/*     */   private void addToolbarAction(Action action)
/*     */   {
/* 120 */     JButton button = new JButton(action);
/* 121 */     button.setToolTipText((String)action.getValue("Name"));
/* 122 */     button.setText(null);
/* 123 */     button.setBorder(new EmptyBorder(4, 4, 4, 4));
/* 124 */     this.toolbar.add(button);
/*     */   }
/*     */ 
/*     */   private void handleAddNewPref() {
/* 128 */     AddPreferenceOptionPanel panel = new AddPreferenceOptionPanel();
/* 129 */     int ret = JOptionPaneEx.showValidatingConfirmDialog(this, "Select URIgen server...", panel, -1, 2, panel);
/*     */ 
/* 135 */     if (ret == 0) {
/* 136 */       UrigenPreference.save(panel.getOntologyIRI(), panel.getServerlUrl(), panel.getPrefName(), panel.getApiKey());
/*     */ 
/* 142 */       rebuidTable();
/*     */     }
/*     */   }
/*     */ 
/*     */   private void handleRemovePref()
/*     */   {
/* 148 */     UrigenPreferenceOption option = this.table.getRow(this.table.getSelectedRow());
/* 149 */     UrigenPreference.remove(option.getOntologyIri());
/* 150 */     rebuidTable();
/*     */   }
/*     */ 
/*     */   public void dispose()
/*     */     throws Exception
/*     */   {
/*     */   }
/*     */ }

/* Location:           /Users/jupp/Downloads/org.protege.urigen.jar
 * Qualified Name:     uk.ac.ebi.fgpt.urigen.panel.UrigenPreferencePanel
 * JD-Core Version:    0.6.1
 */