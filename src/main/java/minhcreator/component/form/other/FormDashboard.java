package minhcreator.component.form.other;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.raven.swing.RoundPanel;
import minhcreator.component.Card;
import minhcreator.component.model.ModelCard;

import javax.swing.*;
import java.awt.*;

/**
 *
 * @author Raven
 */
public class FormDashboard extends javax.swing.JPanel {

    public FormDashboard() {
//        initComponents();
//        lb.putClientProperty(FlatClientProperties.STYLE, ""
//                + "font:$h1.font");
        initGroupCard();
//        initComponents();
    }

    @SuppressWarnings("unchecked")

    private void initComponents() {
        Card card = new Card();
        card.setDataSvg(new ModelCard("Test Card", 100, new FlatSVGIcon("minhcreator/assets/debug/Info.svg")));
        add(card);
    }

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        WarehouseInventoryForm.OnReadyUpdate();
//        Notifications.getInstance().show(Notifications.Type.INFO, Notifications.Location.TOP_CENTER, WarehouseInventoryForm.exportData().toString());
    }

    private void initGroupCard() {
        GroupCardPanel = new JPanel(new GridLayout(0, 5));
        TotalRevenueCard = new Card();
        TotalProductCard = new Card();
        CurrentStock_in_storage_Card = new Card();
        Current_Credit_Card = new Card();
        ExpenseCard = new Card();

        // Set Data
        FlatSVGIcon ic = new FlatSVGIcon("minhcreator/assets/debug/Info.svg");
        TotalRevenueCard.setDataSvg(new ModelCard("Total Revenue", 100, ic));
        TotalProductCard.setDataSvg(new ModelCard("Total Product", 100, new FlatSVGIcon("minhcreator/assets/debug/Info.svg")));
        Current_Credit_Card.setDataSvg(new ModelCard("Current Credit", 100, new FlatSVGIcon("minhcreator/assets/debug/Info.svg")));
        ExpenseCard.setDataSvg(new ModelCard("Expense", 100, new FlatSVGIcon("minhcreator/assets/debug/Info.svg")));

        TotalRevenueCard.setSize(10, 50);
        TotalProductCard.setSize(10, 50);
        Current_Credit_Card.setSize(10, 55);
        ExpenseCard.setSize(120, 60);

        GroupCardPanel.add(TotalRevenueCard);
        GroupCardPanel.add(TotalProductCard);
        GroupCardPanel.add(Current_Credit_Card);
        GroupCardPanel.add(ExpenseCard);
        add(GroupCardPanel);

    }

    private RoundPanel RCardPanel;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel lb;
    private JPanel mainPanel, GroupCardPanel;
    private Card TotalRevenueCard, TotalProductCard, CurrentStock_in_storage_Card, Current_Credit_Card, ExpenseCard;
}