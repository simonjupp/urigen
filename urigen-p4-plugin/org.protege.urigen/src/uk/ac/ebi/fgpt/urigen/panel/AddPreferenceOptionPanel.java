/*     */ package uk.ac.ebi.fgpt.urigen.panel;
/*     */ 
/*     */ import java.awt.BorderLayout;
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.ComponentOrientation;
/*     */ import java.awt.Font;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.GridBagConstraints;
/*     */ import java.awt.GridBagLayout;
/*     */ import java.awt.Insets;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import javax.swing.AbstractAction;
/*     */ import javax.swing.BorderFactory;
/*     */ import javax.swing.ComboBoxModel;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JComboBox;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JFrame;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JList;
/*     */ import javax.swing.JOptionPane;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JTextArea;
/*     */ import javax.swing.JTextField;
/*     */ import javax.swing.ListCellRenderer;
/*     */ import javax.swing.SwingUtilities;
/*     */ import javax.swing.border.Border;
/*     */ import javax.swing.border.CompoundBorder;
/*     */ import javax.swing.border.EmptyBorder;
/*     */ import javax.swing.event.CaretEvent;
/*     */ import javax.swing.event.CaretListener;
/*     */ import javax.swing.event.ListDataListener;
/*     */ import org.protege.editor.core.ui.util.InputVerificationStatusChangedListener;
/*     */ import org.protege.editor.core.ui.util.VerifiedInputEditor;
/*     */ import org.semanticweb.owlapi.model.IRI;
/*     */ import uk.ac.ebi.fgpt.urigen.entity.ServerURLConnection;
/*     */ import uk.ac.ebi.fgpt.urigen.exception.UrigenException;
/*     */ import uk.ac.ebi.fgpt.urigen.web.view.PreferenceBean;
/*     */ import uk.ac.ebi.fgpt.urigen.web.view.UserBean;
/*     */ 
/*     */ public class AddPreferenceOptionPanel extends JPanel
/*     */   implements VerifiedInputEditor
/*     */ {
/*  34 */   private JLabel serverUrlLabel = new JLabel("Server URL:");
/*  35 */   private JTextField serverURL = new JTextField(30);
/*  36 */   private boolean serverVerified = false;
/*     */ 
/*  38 */   private JButton connectButton = new JButton("Connect...");
/*     */ 
/*  40 */   private JLabel prefNameLabel = new JLabel("Choose an ontology:");
/*  41 */   private JComboBox prefNames = new JComboBox();
/*     */ 
/*  43 */   private JLabel apiKeyLabel = new JLabel("Api Key");
/*  44 */   private JTextField apiKeyField = new JTextField("");
/*     */ 
/*  46 */   private JLabel errorMessage = new JLabel("", UrigenIcons.getIcon("warn.png"), 2);
/*     */ 
/*  48 */   private PreferenceComboBoxModel model = new PreferenceComboBoxModel();
/*     */ 
/*  50 */   private List<InputVerificationStatusChangedListener> listeners = new ArrayList();
/*     */ 
/*  52 */   private CaretListener textBoxListener = new CaretListener()
/*     */   {
/*     */     public void caretUpdate(CaretEvent caretEvent) {
/*  55 */       if ((AddPreferenceOptionPanel.this.serverVerified) && (AddPreferenceOptionPanel.this.isValidUser())) {
/*  56 */         for (InputVerificationStatusChangedListener l : AddPreferenceOptionPanel.this.listeners) {
/*  57 */           l.verifiedStatusChanged(true);
/*  58 */           AddPreferenceOptionPanel.this.errorMessage.setText("");
/*  59 */           AddPreferenceOptionPanel.this.errorMessage.setVisible(false);
/*     */         }
/*     */       }
/*     */       else
/*  63 */         for (InputVerificationStatusChangedListener l : AddPreferenceOptionPanel.this.listeners) {
/*  64 */           AddPreferenceOptionPanel.this.errorMessage.setText("Not a valid API key");
/*  65 */           AddPreferenceOptionPanel.this.errorMessage.setVisible(true);
/*  66 */           l.verifiedStatusChanged(false);
/*     */         }
/*     */     }
/*  52 */   };
/*     */ 
/*     */   public AddPreferenceOptionPanel()
/*     */   {
/*  75 */     setLayout(new BorderLayout());
/*     */ 
/*  77 */     this.connectButton.addActionListener(new AbstractAction()
/*     */     {
/*     */       public void actionPerformed(ActionEvent actionEvent) {
/*  80 */         AddPreferenceOptionPanel.this.getPreferenceList();
/*     */       }
/*     */     });
/*  85 */     this.apiKeyField.addCaretListener(this.textBoxListener);
/*     */ 
/*  87 */     this.prefNames.setModel(this.model);
/*  88 */     this.prefNames.setRenderer(new ListCellRenderer() {
/*     */       public Component getListCellRendererComponent(JList jList, Object o, int i, boolean b, boolean b1) {
/*  90 */         if (o != null) {
/*  91 */           AddPreferenceOptionPanel.PreferenceItem item = (AddPreferenceOptionPanel.PreferenceItem)o;
/*  92 */           JLabel l = new JLabel(item.getName() + " (" + item.getIri() + ")");
/*  93 */           Border paddingBorder = BorderFactory.createEmptyBorder(2, 2, 2, 2);
/*  94 */           Border border = BorderFactory.createLineBorder(Color.LIGHT_GRAY);
/*     */ 
/*  96 */           l.setBorder(BorderFactory.createCompoundBorder(border, paddingBorder));
/*  97 */           return l;
/*     */         }
/*  99 */         return new JLabel("");
/*     */       }
/*     */     });
/* 111 */     JPanel titlePanel = new JPanel(new BorderLayout());
/* 112 */     titlePanel.setBackground(Color.WHITE);
/* 113 */     addDivider(titlePanel, 3, true);
/*     */ 
/* 116 */     JLabel titleLabel = new JLabel("Select preference server:");
/* 117 */     titleLabel.setFont(titleLabel.getFont().deriveFont(1, 13.5F));
/*     */ 
/* 119 */     JLabel titleIcon = new JLabel("");
/* 120 */     JTextArea titleMessage = new JTextArea("First enter the URL of the URI server and select connect.\nThen select an ontology URI preference from the list.\nFinally enter your API key.");
/* 121 */     titleMessage.setMargin(new Insets(5, 10, 10, 10));
/* 122 */     titleMessage.setFont(titleMessage.getFont().deriveFont(11.0F));
/* 123 */     titleMessage.setEditable(false);
/* 124 */     titleMessage.setFocusable(false);
/* 125 */     titleMessage.setBackground(Color.white);
/*     */ 
/* 127 */     titlePanel.setBorder(new CompoundBorder(titlePanel.getBorder(), new EmptyBorder(5, 5, 0, 5)));
/* 128 */     add(titlePanel, "North");
/* 129 */     titlePanel.add(titleLabel, "North");
/* 130 */     titlePanel.add(titleIcon, "West");
/* 131 */     titlePanel.add(titleMessage, "Center");
/*     */ 
/* 133 */     JPanel body = new JPanel();
/* 134 */     body.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
/* 135 */     body.setLayout(new GridBagLayout());
/*     */ 
/* 137 */     GridBagConstraints c = new GridBagConstraints();
/* 138 */     c.fill = 2;
/* 139 */     c.weightx = 0.1D;
/* 140 */     c.insets = new Insets(0, 10, 10, 10);
/* 141 */     c.gridx = 0;
/* 142 */     c.gridy = 0;
/* 143 */     body.add(this.serverUrlLabel, c);
/*     */ 
/* 145 */     c.fill = 2;
/* 146 */     c.weightx = 1.0D;
/*     */ 
/* 148 */     c.gridx = 1;
/* 149 */     c.gridy = 0;
/* 150 */     body.add(this.serverURL, c);
/*     */ 
/* 152 */     c.fill = 2;
/* 153 */     c.weightx = 0.1D;
/*     */ 
/* 155 */     c.gridx = 2;
/* 156 */     c.gridy = 0;
/* 157 */     body.add(this.connectButton, c);
/*     */ 
/* 159 */     c.fill = 2;
/* 160 */     c.ipady = 20;
/* 161 */     c.weightx = 0.1D;
/* 162 */     c.gridwidth = 1;
/* 163 */     c.gridx = 0;
/* 164 */     c.gridy = 1;
/* 165 */     body.add(this.prefNameLabel, c);
/*     */ 
/* 167 */     c.fill = 2;
/* 168 */     c.ipady = 20;
/* 169 */     c.weightx = 0.5D;
/* 170 */     c.gridwidth = 2;
/* 171 */     c.gridx = 1;
/* 172 */     c.gridy = 1;
/* 173 */     body.add(this.prefNames, c);
/*     */ 
/* 175 */     c.fill = 2;
/* 176 */     c.ipady = 1;
/* 177 */     c.weightx = 0.1D;
/* 178 */     c.gridwidth = 1;
/* 179 */     c.gridx = 0;
/* 180 */     c.gridy = 2;
/* 181 */     body.add(this.apiKeyLabel, c);
/*     */ 
/* 183 */     c.fill = 2;
/* 184 */     c.ipady = 1;
/*     */ 
/* 186 */     c.weightx = 0.5D;
/* 187 */     c.gridwidth = 2;
/* 188 */     c.gridx = 1;
/* 189 */     c.gridy = 2;
/* 190 */     body.add(this.apiKeyField, c);
/*     */ 
/* 192 */     c.fill = 2;
/* 193 */     c.ipady = 1;
/*     */ 
/* 195 */     c.weightx = 1.0D;
/* 196 */     c.gridwidth = 1;
/* 197 */     c.gridx = 1;
/* 198 */     c.gridy = 3;
/* 199 */     body.add(this.errorMessage, c);
/*     */ 
/* 201 */     this.errorMessage.setVisible(false);
/* 202 */     add(body, "Center");
/*     */   }
/*     */ 
/*     */   private boolean isValidUser()
/*     */   {
/* 208 */     String apiKey = this.apiKeyField.getText();
/* 209 */     if ((!apiKey.equals("")) && (apiKey.length() == 40)) {
/* 210 */       String url = this.serverURL.getText();
/* 211 */       if (url != null)
/*     */         try {
/* 213 */           ServerURLConnection connection = new ServerURLConnection(url);
/* 214 */           UserBean user = connection.getUrigenUserByApiKey(apiKey);
/* 215 */           return user.getId() > -1;
/*     */         } catch (IOException e) {
/*     */         }
/*     */         catch (UrigenException e) {
/*     */         }
/*     */     }
/* 221 */     return false;
/*     */   }
/*     */ 
/*     */   private boolean isValidInput() {
/* 225 */     return (this.serverVerified) && (isValidUser());
/*     */   }
/*     */ 
/*     */   private void getPreferenceList()
/*     */   {
/* 231 */     String url = this.serverURL.getText();
/* 232 */     if ((url != null) && 
/* 233 */       (!url.equals("")))
/*     */       try {
/* 235 */         ServerURLConnection connection = new ServerURLConnection(url);
/* 236 */         List items = new ArrayList();
/* 237 */         for (PreferenceBean b : connection.getUrigenPreferences()) {
/* 238 */           PreferenceItem item = new PreferenceItem(b.getOntologyName(), b.getOntologyUri());
/* 239 */           items.add(item);
/*     */         }
/* 241 */         Collections.sort(items);
/* 242 */         if (items.size() > 0) {
/* 243 */           this.model.setItems(items);
/* 244 */           this.prefNames.setSelectedIndex(0);
/* 245 */           this.prefNames.updateUI();
/* 246 */           this.serverVerified = true;
/* 247 */           this.errorMessage.setText("");
/* 248 */           this.errorMessage.setVisible(false);
/*     */         }
/*     */         else {
/* 251 */           this.errorMessage.setText("No preferences found on server");
/* 252 */           this.errorMessage.setVisible(true);
/*     */         }
/*     */       }
/*     */       catch (IllegalStateException e) {
/* 256 */         JOptionPane.showMessageDialog(this, "Can't connect to URIgen server at: " + url);
/* 257 */         this.serverVerified = false;
/*     */       }
/*     */       catch (IOException e) {
/* 260 */         JOptionPane.showMessageDialog(this, "Can't connect to URIgen server at: " + url);
/* 261 */         this.serverVerified = false;
/*     */       } catch (UrigenException e) {
/* 263 */         JOptionPane.showMessageDialog(this, "Can't get preferences from URIgen server at: " + url);
/* 264 */         this.serverVerified = false;
/*     */       }
/*     */   }
/*     */ 
/*     */   protected void addDivider(JComponent component, final int position, final boolean etched)
/*     */   {
/* 273 */     component.setBorder(new Border() {
/* 274 */       private final Color borderColor = new Color(0.6F, 0.6F, 0.6F);
/*     */ 
/*     */       public Insets getBorderInsets(Component c) {
/* 277 */         if (position == 1) {
/* 278 */           return new Insets(5, 0, 0, 0);
/*     */         }
/* 280 */         return new Insets(0, 0, 5, 0);
/*     */       }
/*     */ 
/*     */       public boolean isBorderOpaque()
/*     */       {
/* 285 */         return false;
/*     */       }
/*     */ 
/*     */       public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
/* 289 */         if (position == 1) {
/* 290 */           if (etched) {
/* 291 */             g.setColor(this.borderColor);
/* 292 */             g.drawLine(x, y, x + width, y);
/* 293 */             g.setColor(Color.WHITE);
/* 294 */             g.drawLine(x, y + 1, x + width, y + 1);
/*     */           } else {
/* 296 */             g.setColor(Color.LIGHT_GRAY);
/* 297 */             g.drawLine(x, y, x + width, y);
/*     */           }
/*     */         }
/* 300 */         else if (etched) {
/* 301 */           g.setColor(this.borderColor);
/* 302 */           g.drawLine(x, y + height - 2, x + width, y + height - 2);
/* 303 */           g.setColor(Color.WHITE);
/* 304 */           g.drawLine(x, y + height - 1, x + width, y + height - 1);
/*     */         } else {
/* 306 */           g.setColor(Color.LIGHT_GRAY);
/* 307 */           g.drawLine(x, y + height - 1, x + width, y + height - 1);
/*     */         }
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   public void addStatusChangedListener(InputVerificationStatusChangedListener listener)
/*     */   {
/* 316 */     this.listeners.add(listener);
/* 317 */     listener.verifiedStatusChanged(isValidInput());
/*     */   }
/*     */ 
/*     */   public void removeStatusChangedListener(InputVerificationStatusChangedListener listener)
/*     */   {
/* 323 */     this.listeners.remove(listener);
/*     */   }
/*     */ 
/*     */   public IRI getOntologyIRI()
/*     */   {
/* 328 */     return IRI.create(((PreferenceItem)this.prefNames.getSelectedItem()).getIri());
/*     */   }
/*     */ 
/*     */   public String getServerlUrl() {
/* 332 */     return this.serverURL.getText();
/*     */   }
/*     */ 
/*     */   public String getPrefName()
/*     */   {
/* 340 */     return ((PreferenceItem)this.prefNames.getSelectedItem()).getName();
/*     */   }
/*     */ 
/*     */   public String getApiKey() {
/* 344 */     return this.apiKeyField.getText();
/*     */   }
/*     */ 
/*     */   private static void createAndShowGUI()
/*     */   {
/* 420 */     JFrame frame = new JFrame("Add preference UI");
/* 421 */     frame.setDefaultCloseOperation(3);
/*     */ 
/* 424 */     frame.add(new AddPreferenceOptionPanel());
/*     */ 
/* 427 */     frame.pack();
/* 428 */     frame.setVisible(true);
/*     */   }
/*     */ 
///*     */   public static void main(String[] args)
///*     */   {
///* 434 */     SwingUtilities.invokeLater(new Runnable() {
///*     */       public void run() {
///* 436 */         AddPreferenceOptionPanel.access$600();
///*     */       }
///*     */     });
///*     */   }
///*     */
/*     */   private class PreferenceItem
/*     */     implements Comparable<PreferenceItem>
/*     */   {
/*     */     private String name;
/*     */     private String iri;
/*     */ 
/*     */     public String getName()
/*     */     {
/* 398 */       return this.name;
/*     */     }
/*     */ 
/*     */     public String getIri() {
/* 402 */       return this.iri;
/*     */     }
/*     */ 
/*     */     private PreferenceItem(String name, String iri)
/*     */     {
/* 408 */       this.name = name;
/* 409 */       this.iri = iri;
/*     */     }
/*     */ 
/*     */     public int compareTo(PreferenceItem preferenceItem) {
/* 413 */       return this.name.compareTo(preferenceItem.getName());
/*     */     }
/*     */   }
/*     */ 
/*     */   private class PreferenceComboBoxModel
/*     */     implements ComboBoxModel
/*     */   {
/*     */     private List<AddPreferenceOptionPanel.PreferenceItem> items;
/*     */     private AddPreferenceOptionPanel.PreferenceItem selected;
/*     */ 
/*     */     public PreferenceComboBoxModel()
/*     */     {
/* 353 */       this.items = new ArrayList();
/*     */     }
/*     */ 
/*     */     public void setItems(List<AddPreferenceOptionPanel.PreferenceItem> items) {
/* 357 */       this.items.clear();
/* 358 */       this.items = items;
/*     */     }
/*     */ 
/*     */     public void setSelectedItem(Object o)
/*     */     {
/* 363 */       this.selected = ((AddPreferenceOptionPanel.PreferenceItem)o);
/*     */     }
/*     */ 
/*     */     public Object getSelectedItem()
/*     */     {
/* 368 */       return this.selected;
/*     */     }
/*     */ 
/*     */     public int getSize() {
/* 372 */       return this.items.size();
/*     */     }
/*     */ 
/*     */     public Object getElementAt(int i) {
/* 376 */       return this.items.get(i);
/*     */     }
/*     */ 
/*     */     public void addListDataListener(ListDataListener listDataListener)
/*     */     {
/*     */     }
/*     */ 
/*     */     public void removeListDataListener(ListDataListener listDataListener)
/*     */     {
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/jupp/Downloads/org.protege.urigen.jar
 * Qualified Name:     uk.ac.ebi.fgpt.urigen.panel.AddPreferenceOptionPanel
 * JD-Core Version:    0.6.1
 */