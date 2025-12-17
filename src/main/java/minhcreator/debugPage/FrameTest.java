package minhcreator.debugPage;

import minhcreator.UIManager.UIManager;
import minhcreator.component.CustomDialog;
import minhcreator.component.form.*;
import minhcreator.util.Shared;

import javax.swing.*;
import java.util.ArrayList;

public class FrameTest extends javax.swing.JFrame {

    public FrameTest() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 700);
        setResizable(true);
        setLocationRelativeTo(null);
        setVisible(true);
        init_components();
//        UIManager.getInstance().initApplication(this);
    }

    public void init_components(){
        ArrayList<Integer> arr = new ArrayList<>();
        arr.add(50);
        arr.add(50);
//        CustomDialog dialog = new CustomDialog(this, true,"Header", "message", "minhcreator/assets/debug/Error.svg", "buttonText", arr);
//        dialog.setVisible(true);

        JButton button = new JButton("login");
        button.addActionListener(e -> {
            CustomDialog dia = new CustomDialog(this, true, Shared.casting_value, Shared.casting_value, "ok");
        });

//        JButton buttont = new JButton("login");
//        buttont.addActionListener(e -> {
//            UIManager.getInstance().showForm(new Sign_up());
//        });
        add(button);
//        add(buttont);
    }

    public static void main(String[] args) {
        new FrameTest();
    }
}
