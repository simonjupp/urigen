package uk.ac.ebi.fgpt.urigen.panel;

import org.protege.editor.core.ui.util.InputVerificationStatusChangedListener;
import org.protege.editor.core.ui.util.VerifiedInputEditor;
import org.semanticweb.owlapi.model.IRI;
import uk.ac.ebi.fgpt.urigen.entity.ServerURLConnection;
import uk.ac.ebi.fgpt.urigen.exception.UrigenException;
import uk.ac.ebi.fgpt.urigen.web.view.PreferenceBean;
import uk.ac.ebi.fgpt.urigen.web.view.UserBean;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.ListDataListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AddPreferenceOptionPanel extends JPanel
        implements VerifiedInputEditor {

    private final JTextField serverURL = new JTextField(30);

    private boolean serverVerified = false;

    private final JComboBox prefNames = new JComboBox();

    private final JTextField apiKeyField = new JTextField("");

    private final JLabel errorMessage = new JLabel("", UrigenIcons.getIcon("warn.png"), 2);

    private final PreferenceComboBoxModel model = new PreferenceComboBoxModel();

    private final List<InputVerificationStatusChangedListener> listeners = new ArrayList();

    public AddPreferenceOptionPanel() {
        setLayout(new BorderLayout());

        JButton connectButton = new JButton("Connect...");
        connectButton.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent actionEvent) {
                AddPreferenceOptionPanel.this.getPreferenceList();
            }
        });
        CaretListener textBoxListener = caretEvent -> {
            if ((AddPreferenceOptionPanel.this.serverVerified) && (AddPreferenceOptionPanel.this.isValidUser())) {
                for (InputVerificationStatusChangedListener l : AddPreferenceOptionPanel.this.listeners) {
                    l.verifiedStatusChanged(true);
                    AddPreferenceOptionPanel.this.errorMessage.setText("");
                    AddPreferenceOptionPanel.this.errorMessage.setVisible(false);
                }
            }
            else {
                for (InputVerificationStatusChangedListener l : AddPreferenceOptionPanel.this.listeners) {
                    AddPreferenceOptionPanel.this.errorMessage.setText("Not a valid API key");
                    AddPreferenceOptionPanel.this.errorMessage.setVisible(true);
                    l.verifiedStatusChanged(false);
                }
            }
        };
        this.apiKeyField.addCaretListener(textBoxListener);

        this.prefNames.setModel(this.model);
        this.prefNames.setRenderer((jList, o, i, b, b1) -> {
            if (o != null) {
                PreferenceItem item = (PreferenceItem) o;
                JLabel l = new JLabel(item.getName() + " (" + item.getIri() + ")");
                Border paddingBorder = BorderFactory.createEmptyBorder(2, 2, 2, 2);
                Border border = BorderFactory.createLineBorder(Color.LIGHT_GRAY);

                l.setBorder(BorderFactory.createCompoundBorder(border, paddingBorder));
                return l;
            }
            return new JLabel("");
        });
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(Color.WHITE);
        addDivider(titlePanel, 3, true);

        JLabel titleLabel = new JLabel("Select preference server:");
        titleLabel.setFont(titleLabel.getFont().deriveFont(1, 13.5F));

        JLabel titleIcon = new JLabel("");
        JTextArea titleMessage = new JTextArea("First enter the URL of the URI server and select connect.\nThen select an ontology URI preference from the list.\nFinally enter your API key.");
        titleMessage.setMargin(new Insets(5, 10, 10, 10));
        titleMessage.setFont(titleMessage.getFont().deriveFont(11.0F));
        titleMessage.setEditable(false);
        titleMessage.setFocusable(false);
        titleMessage.setBackground(Color.white);

        titlePanel.setBorder(new CompoundBorder(titlePanel.getBorder(), new EmptyBorder(5, 5, 0, 5)));
        add(titlePanel, "North");
        titlePanel.add(titleLabel, "North");
        titlePanel.add(titleIcon, "West");
        titlePanel.add(titleMessage, "Center");

        JPanel body = new JPanel();
        body.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        body.setLayout(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();
        c.fill = 2;
        c.weightx = 0.1D;
        c.insets = new Insets(0, 10, 10, 10);
        c.gridx = 0;
        c.gridy = 0;
        JLabel serverUrlLabel = new JLabel("Server URL:");
        body.add(serverUrlLabel, c);

        c.fill = 2;
        c.weightx = 1.0D;

        c.gridx = 1;
        c.gridy = 0;
        body.add(this.serverURL, c);

        c.fill = 2;
        c.weightx = 0.1D;

        c.gridx = 2;
        c.gridy = 0;
        body.add(connectButton, c);

        c.fill = 2;
        c.ipady = 20;
        c.weightx = 0.1D;
        c.gridwidth = 1;
        c.gridx = 0;
        c.gridy = 1;
        JLabel prefNameLabel = new JLabel("Choose an ontology:");
        body.add(prefNameLabel, c);

        c.fill = 2;
        c.ipady = 20;
        c.weightx = 0.5D;
        c.gridwidth = 2;
        c.gridx = 1;
        c.gridy = 1;
        body.add(this.prefNames, c);

        c.fill = 2;
        c.ipady = 1;
        c.weightx = 0.1D;
        c.gridwidth = 1;
        c.gridx = 0;
        c.gridy = 2;
        JLabel apiKeyLabel = new JLabel("Api Key");
        body.add(apiKeyLabel, c);

        c.fill = 2;
        c.ipady = 1;

        c.weightx = 0.5D;
        c.gridwidth = 2;
        c.gridx = 1;
        c.gridy = 2;
        body.add(this.apiKeyField, c);

        c.fill = 2;
        c.ipady = 1;

        c.weightx = 1.0D;
        c.gridwidth = 1;
        c.gridx = 1;
        c.gridy = 3;
        body.add(this.errorMessage, c);

        this.errorMessage.setVisible(false);
        add(body, "Center");
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Add preference UI");
        frame.setDefaultCloseOperation(3);

        frame.add(new AddPreferenceOptionPanel());

        frame.pack();
        frame.setVisible(true);
    }

    private boolean isValidUser() {
        String apiKey = this.apiKeyField.getText();
        if ((!apiKey.equals("")) && (apiKey.length() == 40)) {
            String url = this.serverURL.getText();
            if (url != null) {
                try {
                    ServerURLConnection connection = new ServerURLConnection(url);
                    UserBean user = connection.getUrigenUserByApiKey(apiKey);
                    return user.getId() > -1;
                } catch (IOException e) {
                } catch (UrigenException e) {
                }
            }
        }
        return false;
    }

    private boolean isValidInput() {
        return (this.serverVerified) && (isValidUser());
    }

    private void getPreferenceList() {
        String url = this.serverURL.getText();
        if ((url != null) &&
                (!url.equals(""))) {
            try {
                ServerURLConnection connection = new ServerURLConnection(url);
                List items = new ArrayList();
                for (PreferenceBean b : connection.getUrigenPreferences()) {
                    PreferenceItem item = new PreferenceItem(b.getOntologyName(), b.getOntologyUri());
                    items.add(item);
                }
                Collections.sort(items);
                if (items.size() > 0) {
                    this.model.setItems(items);
                    this.prefNames.setSelectedIndex(0);
                    this.prefNames.updateUI();
                    this.serverVerified = true;
                    this.errorMessage.setText("");
                    this.errorMessage.setVisible(false);
                }
                else {
                    this.errorMessage.setText("No preferences found on server");
                    this.errorMessage.setVisible(true);
                }
            } catch (IllegalStateException e) {
                JOptionPane.showMessageDialog(this, "Can't connect to URIgen server at: " + url);
                this.serverVerified = false;
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Can't connect to URIgen server at: " + url);
                this.serverVerified = false;
            } catch (UrigenException e) {
                JOptionPane.showMessageDialog(this, "Can't get preferences from URIgen server at: " + url);
                this.serverVerified = false;
            }
        }
    }

    protected void addDivider(JComponent component, final int position, final boolean etched) {
        component.setBorder(new Border() {
            private final Color borderColor = new Color(0.6F, 0.6F, 0.6F);

            public Insets getBorderInsets(Component c) {
                if (position == 1) {
                    return new Insets(5, 0, 0, 0);
                }
                return new Insets(0, 0, 5, 0);
            }

            public boolean isBorderOpaque() {
                return false;
            }

            public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
                if (position == 1) {
                    if (etched) {
                        g.setColor(this.borderColor);
                        g.drawLine(x, y, x + width, y);
                        g.setColor(Color.WHITE);
                        g.drawLine(x, y + 1, x + width, y + 1);
                    }
                    else {
                        g.setColor(Color.LIGHT_GRAY);
                        g.drawLine(x, y, x + width, y);
                    }
                }
                else if (etched) {
                    g.setColor(this.borderColor);
                    g.drawLine(x, y + height - 2, x + width, y + height - 2);
                    g.setColor(Color.WHITE);
                    g.drawLine(x, y + height - 1, x + width, y + height - 1);
                }
                else {
                    g.setColor(Color.LIGHT_GRAY);
                    g.drawLine(x, y + height - 1, x + width, y + height - 1);
                }
            }
        });
    }

    public void addStatusChangedListener(InputVerificationStatusChangedListener listener) {
        this.listeners.add(listener);
        listener.verifiedStatusChanged(isValidInput());
    }

    public void removeStatusChangedListener(InputVerificationStatusChangedListener listener) {
        this.listeners.remove(listener);
    }

    public IRI getOntologyIRI() {
        return IRI.create(((PreferenceItem) this.prefNames.getSelectedItem()).getIri());
    }

    public String getServerlUrl() {
        return this.serverURL.getText();
    }

    public String getPrefName() {
        return ((PreferenceItem) this.prefNames.getSelectedItem()).getName();
    }

    public String getApiKey() {
        return this.apiKeyField.getText();
    }

    //  public static void main(String[] args)
//  {
//    SwingUtilities.invokeLater(new Runnable() {
//      public void run() {
//        AddPreferenceOptionPanel.access$600();
//      }
//    });
//  }
///*     */
    private class PreferenceItem
            implements Comparable<PreferenceItem> {
        private final String name;
        private final String iri;

        private PreferenceItem(String name, String iri) {
            this.name = name;
            this.iri = iri;
        }

        public String getName() {
            return this.name;
        }

        public String getIri() {
            return this.iri;
        }

        public int compareTo(PreferenceItem preferenceItem) {
            return this.name.compareTo(preferenceItem.getName());
        }
    }

    private class PreferenceComboBoxModel
            implements ComboBoxModel {
        private List<AddPreferenceOptionPanel.PreferenceItem> items;
        private AddPreferenceOptionPanel.PreferenceItem selected;

        public PreferenceComboBoxModel() {
            this.items = new ArrayList();
        }

        public void setItems(List<AddPreferenceOptionPanel.PreferenceItem> items) {
            this.items.clear();
            this.items = items;
        }

        public Object getSelectedItem() {
            return this.selected;
        }

        public void setSelectedItem(Object o) {
            this.selected = ((AddPreferenceOptionPanel.PreferenceItem) o);
        }

        public int getSize() {
            return this.items.size();
        }

        public Object getElementAt(int i) {
            return this.items.get(i);
        }

        public void addListDataListener(ListDataListener listDataListener) {
        }

        public void removeListDataListener(ListDataListener listDataListener) {
        }
    }
}

/* Location:           /Users/jupp/Downloads/org.protege.urigen.jar
 * Qualified Name:     uk.ac.ebi.fgpt.urigen.panel.AddPreferenceOptionPanel
 * JD-Core Version:    0.6.1
 */