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

    public static void LoadFont(String FontSetting, String FlatLaf_Font, int style, int size) {
        Component put = (Component) UIManager.put(
                FontSetting,
                new Font(FlatLaf_Font, style, size)
        );
//        return put;
    }

    public static Font load(String path, float size) {
        try {
            File fontFile = new File(path);
            Font font = Font.createFont(Font.TRUETYPE_FONT, fontFile).deriveFont(size);
            return font;
        } catch (FontFormatException | IOException e) {
            System.err.println("Error: " + e.getMessage());
            Notifications.getInstance().show(Notifications.Type.ERROR, "Error: " + e.getMessage());

            // Return default font if custom font fails to load
            return null;
            // return FontLoader.load("Fonts/HelveticaNeue.ttf", (int) size);
        }

    }
}