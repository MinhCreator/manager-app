package minhcreator.component.model;

import com.formdev.flatlaf.extras.FlatSVGIcon;

import javax.swing.*;

public class ModelCard {

    public FlatSVGIcon getSvg() {
        return svg;
    }

    public void setSvg(FlatSVGIcon svg) {
        this.svg = svg;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getValues() {
        return values;
    }

    public void setValues(double values) {
        this.values = values;
    }

    public Icon getIcon() {
        return icon;
    }

    public void setIcon(Icon icon) {
        this.icon = icon;
    }

    public ModelCard(String title, double values, Icon icon) {
        this.title = title;
        this.values = values;
        this.icon = icon;
    }

    public ModelCard(String title, double values, FlatSVGIcon icon) {
        this.title = title;
        this.values = values;
        this.svg = icon;
    }

    public ModelCard() {
    }

    private String title;
    private double values;
    private FlatSVGIcon svg;
    private Icon icon;
}