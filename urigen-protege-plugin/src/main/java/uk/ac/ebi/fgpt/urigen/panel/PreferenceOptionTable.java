package uk.ac.ebi.fgpt.urigen.panel;

import java.util.List;
import javax.swing.*;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class PreferenceOptionTable extends JTable {
    public PreferenceOptionTable() {
        super(new PreferenceTableModel());
        setGridColor(Color.LIGHT_GRAY);
        setRowHeight(getRowHeight() + 4);
        getColumnModel().getColumn(0).setMaxWidth(400);

        JTableHeader tableHeader = getTableHeader();
        tableHeader.addMouseListener(new MouseAdapter() {
            public void mouseReleased(MouseEvent mouseEvent) {
            }
        });
        tableHeader.setReorderingAllowed(true);
    }

    public void addRow(UrigenPreferenceOption option) {
        ((PreferenceTableModel) getModel()).getOptions().add(option);
        ((PreferenceTableModel) getModel()).fireTableDataChanged();
    }

    public void fireTableDataChanged() {
        ((PreferenceTableModel) getModel()).fireTableDataChanged();
    }

    public List getOptions() {
        return ((PreferenceTableModel) getModel()).getOptions();
    }

    public UrigenPreferenceOption getRow(int row) {
        return (UrigenPreferenceOption) ((PreferenceTableModel) getModel()).getOptions().get(row);
    }

    public void removeRow(int row) {
        ((PreferenceTableModel) getModel()).removeRow(row);
    }
}

/* Location:           /Users/jupp/Downloads/org.protege.urigen.jar
 * Qualified Name:     uk.ac.ebi.fgpt.urigen.panel.PreferenceOptionTable
 * JD-Core Version:    0.6.1
 */