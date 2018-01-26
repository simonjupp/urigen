package uk.ac.ebi.fgpt.urigen.panel;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class PreferenceTableModel extends AbstractTableModel {
    private final List<UrigenPreferenceOption> options;

    public PreferenceTableModel() {
        this.options = new ArrayList();
    }

    public List getOptions() {
        return this.options;
    }

    public int getRowCount() {
        return this.options.size();
    }

    public int getColumnCount() {
        return 4;
    }

    public Object getValueAt(int row, int col) {
        UrigenPreferenceOption option = this.options.get(row);
        if (col == 0) {
            return option.getPreferenceName();
        }
        if (col == 1) {
            return option.getOntologyIri().toString();
        }
        if (col == 2) {
            return option.getServerURL();
        }
        if (col == 3) {
            return option.getRestApiKey();
        }

        return "";
    }

    public String getColumnName(int col) {
        if (col == 0) {
            return "Name";
        }
        if (col == 1) {
            return "Ontology IRI";
        }
        if (col == 2) {
            return "Server URL";
        }

        return "API Key";
    }

    public void removeRow(int row) {
        this.options.remove(row);
        fireTableDataChanged();
    }
}

/* Location:           /Users/jupp/Downloads/org.protege.urigen.jar
 * Qualified Name:     uk.ac.ebi.fgpt.urigen.panel.PreferenceTableModel
 * JD-Core Version:    0.6.1
 */