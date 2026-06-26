package minhcreator.component.stock;

import com.formdev.flatlaf.extras.FlatSVGIcon;

import javax.swing.*;
import java.awt.*;

/**
 * StockStatus enum is used to create a custom stock status indicator.
 *
 * @author Raven
 * @author Modified by MinhCreatorVN
 */
public enum StockStatus implements TableBadgeCellRenderer.Info {
    AVAIlABLE("Available ", new Color(59, 155, 60), "stock.svg"),
    OUT("Out stock ", new Color(255, 69, 0), "cross.svg"),
    LOW("Low stock ", new Color(242, 117, 7), "warning.svg");

    StockStatus(String text, java.awt.Color color, String svg) {
        this.text = text;
        this.color = color;
        this.icon = null;
    }

    public synchronized FlatSVGIcon getOrCreateIcon() {
        if (icon == null) {
            String svg = switch (this) {
                case AVAIlABLE -> "stock.svg";
                case OUT -> "cross.svg";
                case LOW -> "warning.svg";
            };
            icon = new FlatSVGIcon("minhcreator/assets/functional_icon/stock/" + svg, 1f)
                    .setColorFilter(new FlatSVGIcon.ColorFilter((component, color1) -> color));
        }
        return icon;
    }

    @Override
    public Icon getIcon() {
        return getOrCreateIcon();
    }

    StockStatus(String text, java.awt.Color color) {
        this.text = text;
        this.color = color;
    }

    private final String text;
    private final java.awt.Color color;
    private volatile FlatSVGIcon icon;

    @Override
    public String getText() {
        return text;
    }

    @Override
    public Color getColor() {
        return color;
    }

    public static StockStatus getStatusBage(int Storage, int lower) {

        if (Storage > lower) {
            return AVAIlABLE;
        } else if (Storage > 0 && Storage < lower || Storage == lower) {
            return LOW;
        } else {
            return OUT;
        }
    }
}