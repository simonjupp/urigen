/*    */ package uk.ac.ebi.fgpt.urigen.panel;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import java.awt.event.MouseAdapter;
/*    */ import java.awt.event.MouseEvent;
/*    */ import java.util.List;
/*    */ import javax.swing.JTable;
/*    */ import javax.swing.table.JTableHeader;
/*    */ import javax.swing.table.TableColumn;
/*    */ import javax.swing.table.TableColumnModel;
/*    */ 
/*    */ public class PreferenceOptionTable extends JTable
/*    */ {
/*    */   public PreferenceOptionTable()
/*    */   {
/* 19 */     super(new PreferenceTableModel());
/* 20 */     setGridColor(Color.LIGHT_GRAY);
/* 21 */     setRowHeight(getRowHeight() + 4);
/* 22 */     getColumnModel().getColumn(0).setMaxWidth(400);
/*    */ 
/* 24 */     JTableHeader tableHeader = getTableHeader();
/* 25 */     tableHeader.addMouseListener(new MouseAdapter()
/*    */     {
/*    */       public void mouseReleased(MouseEvent mouseEvent)
/*    */       {
/*    */       }
/*    */     });
/* 30 */     tableHeader.setReorderingAllowed(true);
/*    */   }
/*    */ 
/*    */   public void addRow(UrigenPreferenceOption option)
/*    */   {
/* 36 */     ((PreferenceTableModel)getModel()).getOptions().add(option);
/* 37 */     ((PreferenceTableModel)getModel()).fireTableDataChanged();
/*    */   }
/*    */ 
/*    */   public void fireTableDataChanged()
/*    */   {
/* 42 */     ((PreferenceTableModel)getModel()).fireTableDataChanged();
/*    */   }
/*    */ 
/*    */   public List<UrigenPreferenceOption> getOptions() {
/* 46 */     return ((PreferenceTableModel)getModel()).getOptions();
/*    */   }
/*    */ 
/*    */   public UrigenPreferenceOption getRow(int row) {
/* 50 */     return (UrigenPreferenceOption)((PreferenceTableModel)getModel()).getOptions().get(row);
/*    */   }
/*    */ 
/*    */   public void removeRow(int row) {
/* 54 */     ((PreferenceTableModel)getModel()).removeRow(row);
/*    */   }
/*    */ }

/* Location:           /Users/jupp/Downloads/org.protege.urigen.jar
 * Qualified Name:     uk.ac.ebi.fgpt.urigen.panel.PreferenceOptionTable
 * JD-Core Version:    0.6.1
 */