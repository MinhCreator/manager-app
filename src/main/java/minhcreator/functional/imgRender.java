package minhcreator.functional;
import com.formdev.flatlaf.extras.*;
import javax.swing.*;
import java.awt.*;

public class imgRender {

    public ImageIcon renderImg(String path, int width, int height) {
        ImageIcon originalIcon = new ImageIcon(getClass().getResource(path));
        Image GetImg = originalIcon.getImage();
        Image Create_Scaled_img = GetImg.getScaledInstance(width, height, Image.SCALE_DEFAULT);

        // return scaled image
        return new ImageIcon(Create_Scaled_img);
    }

    // Bugging feature
    public FlatSVGIcon svg(String path, float scale) {
        return new FlatSVGIcon(path, scale);
    }
}

