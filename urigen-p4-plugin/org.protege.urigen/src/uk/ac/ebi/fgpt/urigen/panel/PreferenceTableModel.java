/*    */ package uk.ac.ebi.fgpt.urigen.panel;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import javax.swing.table.AbstractTableModel;
/*    */ 
/*    */ public class PreferenceTableModel extends AbstractTableModel
/*    */ {
/*    */   private List<UrigenPreferenceOption> options;
/*    */ 
/*    */   public PreferenceTableModel()
/*    */   {
/* 17 */     this.options = new ArrayList();
/*    */   }
/*    */ 
/*    */   public List<UrigenPreferenceOption> getOptions() {
/* 21 */     return this.options;
/*    */   }
/*    */ 
/*    */   public int getRowCount() {
/* 25 */     return this.options.size();
/*    */   }
/*    */ 
/*    */   public int getColumnCount() {
/* 29 */     return 4;
/*    */   }
/*    */ 
/*    */   public Object getValueAt(int row, int col)
/*    */   {
/* 34 */     UrigenPreferenceOption option = (UrigenPreferenceOption)this.options.get(row);
/* 35 */     if (col == 0) {
/* 36 */       return option.getPreferenceName();
/*    */     }
/* 38 */     if (col == 1) {
/* 39 */       return option.getOntologyIri().toString();
/*    */     }
/* 41 */     if (col == 2) {
/* 42 */       return option.getServerURL();
/*    */     }
/* 44 */     if (col == 3) {
/* 45 */       return option.getRestApiKey();
/*    */     }
/*    */ 
/* 48 */     return "";
/*    */   }
/*    */ 
/*    */   public String getColumnName(int col)
/*    */   {
/* 54 */     if (col == 0) {
/* 55 */       return "Name";
/*    */     }
/* 57 */     if (col == 1) {
/* 58 */       return "Ontology IRI";
/*    */     }
/* 60 */     if (col == 2) {
/* 61 */       return "Server URL";
/*    */     }
/*    */ 
/* 64 */     return "API Key";
/*    */   }
/*    */ 
/*    */   public void removeRow(int row)
/*    */   {
/* 75 */     this.options.remove(row);
/* 76 */     fireTableDataChanged();
/*    */   }
/*    */ }

/* Location:           /Users/jupp/Downloads/org.protege.urigen.jar
 * Qualified Name:     uk.ac.ebi.fgpt.urigen.panel.PreferenceTableModel
 * JD-Core Version:    0.6.1
 */