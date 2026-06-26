package minhcreator.functional.fontRender;

import raven.toast.Notifications;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * FontManager class is used to load custom fonts and set them as the default font for the application.
 *
 * @author MinhCreatorVN
 */
public class FontManager extends Component {

    private static final java.util.Map<String, Font> fontCache = new java.util.HashMap<>();

    public static void LoadFont(String FontSetting, String FlatLaf_Font, int style, int size) {
        UIManager.put(FontSetting, new Font(FlatLaf_Font, style, size));
    }

    public static Font load(String path, float size) {
        String key = path + "@" + size;
        Font cached = fontCache.get(key);
        if (cached != null) return cached;
        try {
            File fontFile = new File(path);
            Font font = Font.createFont(Font.TRUETYPE_FONT, fontFile).deriveFont(size);
            fontCache.put(key, font);
            return font;
        } catch (FontFormatException | IOException e) {
            System.err.println("Error: " + e.getMessage());
            Notifications.getInstance().show(Notifications.Type.ERROR, "Error: " + e.getMessage());
            return null;
        }
    }
}