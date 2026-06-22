package minhcreator.util;

import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.extras.FlatAnimatedLafChange;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import com.formdev.flatlaf.themes.FlatMacLightLaf;

import javax.swing.*;
import java.awt.*;

public class ThemeManager {

    public static final String THEME_MAC_LIGHT = "mac_light";
    public static final String THEME_MAC_DARK = "mac_dark";
    public static final String THEME_MATERIAL_LIGHT = "material_light";
    public static final String THEME_MATERIAL_DARK = "material_dark";

    public static void applyTheme(String theme) {
        EventQueue.invokeLater(() -> {
            FlatAnimatedLafChange.showSnapshot();
            switch (theme) {
                case THEME_MAC_DARK:
                    FlatMacDarkLaf.setup();
                    break;
                case THEME_MATERIAL_LIGHT:
                    setupMaterialLight();
                    break;
                case THEME_MATERIAL_DARK:
                    setupMaterialDark();
                    break;
                default:
                    FlatMacLightLaf.setup();
                    break;
            }
            FlatLaf.updateUI();
            FlatAnimatedLafChange.hideSnapshotWithAnimation();
            AppLogger.info("Theme", "Switched to " + theme);
        });
    }

    public static void applyTheme(String theme, Component root) {
        applyTheme(theme);
        if (root != null) {
            SwingUtilities.updateComponentTreeUI(root);
        }
    }

    private static void setupMaterialLight() {
        FlatLightLafProxy.setup("MaterialYouLightLaf");
    }

    private static void setupMaterialDark() {
        FlatLightLafProxy.setup("MaterialYouDarkLaf");
    }

    public static String getThemeDisplayName(String theme) {
        return switch (theme) {
            case THEME_MAC_LIGHT -> "macOS Light";
            case THEME_MAC_DARK -> "macOS Dark";
            case THEME_MATERIAL_LIGHT -> "Material You Light";
            case THEME_MATERIAL_DARK -> "Material You Dark";
            default -> "Unknown";
        };
    }

    public static String[] getAvailableThemes() {
        return new String[]{THEME_MAC_LIGHT, THEME_MAC_DARK, THEME_MATERIAL_LIGHT, THEME_MATERIAL_DARK};
    }

    /**
     * Internal proxy so we can call FlatLightLaf.setup() with a theme class name
     * without needing the concrete class at compile time.
     */
    private static class FlatLightLafProxy extends com.formdev.flatlaf.FlatLightLaf {
        public static void setup(String themeClassName) {
            System.setProperty("flatlaf.theme", "minhcreator/themes/" + themeClassName + ".properties");
            FlatLightLafProxy.setup();
            System.clearProperty("flatlaf.theme");
        }
    }
}
