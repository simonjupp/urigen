/*    */ package uk.ac.ebi.fgpt.urigen.panel;
/*    */ 
/*    */ import java.net.URL;
/*    */ import java.util.HashMap;
/*    */ import javax.swing.Icon;
/*    */ import javax.swing.ImageIcon;
/*    */ 
/*    */ public class UrigenIcons
/*    */ {
/* 18 */   private static HashMap<String, Icon> iconMap = new HashMap();
/*    */ 
/*    */   public static Icon getIcon(String name)
/*    */   {
/* 23 */     Icon icon = (Icon)iconMap.get(name);
/* 24 */     if (icon != null) {
/* 25 */       return icon;
/*    */     }
/*    */ 
/* 28 */     ClassLoader loader = UrigenIcons.class.getClassLoader();
/* 29 */     URL url = loader.getResource(name);
/* 30 */     if (url != null) {
/* 31 */       Icon loadedIcon = new ImageIcon(url);
/* 32 */       iconMap.put(name, loadedIcon);
/* 33 */       return loadedIcon;
/*    */     }
/*    */ 
/* 36 */     return null;
/*    */   }
/*    */ }

/* Location:           /Users/jupp/Downloads/org.protege.urigen.jar
 * Qualified Name:     uk.ac.ebi.fgpt.urigen.panel.UrigenIcons
 * JD-Core Version:    0.6.1
 */