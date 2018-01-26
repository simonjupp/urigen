package uk.ac.ebi.fgpt.urigen.panel;

import javax.swing.*;
import java.net.URL;
import java.util.HashMap;

public class UrigenIcons {
    private static final HashMap<String, Icon> iconMap = new HashMap();

    public static Icon getIcon(String name) {
        Icon icon = iconMap.get(name);
        if (icon != null) {
            return icon;
        }

        ClassLoader loader = UrigenIcons.class.getClassLoader();
        URL url = loader.getResource(name);
        if (url != null) {
            Icon loadedIcon = new ImageIcon(url);
            iconMap.put(name, loadedIcon);
            return loadedIcon;
        }

        return null;
    }
}

/* Location:           /Users/jupp/Downloads/org.protege.urigen.jar
 * Qualified Name:     uk.ac.ebi.fgpt.urigen.panel.UrigenIcons
 * JD-Core Version:    0.6.1
 */