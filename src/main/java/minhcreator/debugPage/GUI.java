package minhcreator.debugPage;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

/**
 *
 * @author KS. Hoang Vu Da Quynh
 * @author Modified by MinhCreatorVN
 */
public class GUI extends JFrame {
    private final JTextField studentIdField, firstNameField, lastNameField, majorField, phoneField, gpaField, dobField;
    private final JButton saveButton, clearButton, refreshButton, editButton, deleteButton, cancelButton, searchButton, sortButton;
    private final JTextField searchField;
    private final JComboBox<String> sortByBox;
    private final JTable table;

    private Connection conn;
    private Statement stmt;

    public GUI() {
        super("Student Enrollment Information System");
        JPanel main = new JPanel(new BorderLayout());
        main.setBorder(BorderFactory.createEmptyBorder(15, 15, 0, 15));
        setContentPane(main);

        JLabel titleLabel = new JLabel("Student Enrollment Information System", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(titleLabel, BorderLayout.NORTH);

        JPanel leftForm = new JPanel(new GridBagLayout());
        leftForm.setBorder(BorderFactory.createLineBorder(Color.gray, 1));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 0, 6, 8);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;

        Font labelFont = new Font("Segoe UI", Font.PLAIN, 13);

        JLabel lblFirstName = new JLabel("First Name");
        lblFirstName.setFont(labelFont);
        JLabel lblLastName = new JLabel("Last Name");
        lblLastName.setFont(labelFont);
        JLabel lblStudentId = new JLabel("Student ID");
        lblStudentId.setFont(labelFont);
        JLabel lblMajor = new JLabel("Major");
        lblMajor.setFont(labelFont);
        JLabel lblPhone = new JLabel("Contact No.");
        lblPhone.setFont(labelFont);
        JLabel lblGpa = new JLabel("GPA");
        lblGpa.setFont(labelFont);
        JLabel lblDob = new JLabel("Birth Date");
        lblDob.setFont(labelFont);

        firstNameField = new JTextField(14);
        lastNameField = new JTextField(14);
        studentIdField = new JTextField(14);
        majorField = new JTextField(14);
        phoneField = new JTextField(14);
        gpaField = new JTextField(14);
        dobField = new JTextField(14);

        addFormRow(leftForm, gbc, lblFirstName, firstNameField);
        addFormRow(leftForm, gbc, lblLastName, lastNameField);
        addFormRow(leftForm, gbc, lblStudentId, studentIdField);
        addFormRow(leftForm, gbc, lblMajor, majorField);
        addFormRow(leftForm, gbc, lblPhone, phoneField);
        addFormRow(leftForm, gbc, lblGpa, gpaField);
        addFormRow(leftForm, gbc, lblDob, dobField);

        JPanel rightPanel = new JPanel(new BorderLayout(8, 8));
        rightPanel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Color.gray, 1),
                        BorderFactory.createEmptyBorder(10, 10, 10, 10)
                )
        );

        JPanel controlPanel = new JPanel(new BorderLayout());
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 4));
        JLabel lblSearch = new JLabel("Search");
        lblSearch.setFont(labelFont);
        searchField = new JTextField(14);
        searchButton = new JButton();
        ImageIcon icon = new ImageIcon("src/Images/search_.png");
        Image img = icon.getImage();
        Image newImg = img.getScaledInstance(14, 14, Image.SCALE_SMOOTH);
        searchButton.setIcon(new ImageIcon(newImg));
        searchPanel.add(lblSearch);
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        JPanel sortPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 4));
        JLabel lblSort = new JLabel("Sort by");
        lblSort.setFont(labelFont);
        sortByBox = new JComboBox<>(new String[]{"First Name", "Last Name", "GPA"});
        sortButton = new JButton("Sort");

        sortPanel.add(lblSort);
        sortPanel.add(sortByBox);
        sortPanel.add(sortButton);

        controlPanel.add(searchPanel, BorderLayout.WEST);
        controlPanel.add(sortPanel, BorderLayout.EAST);
        rightPanel.add(controlPanel, BorderLayout.NORTH);

        table = new JTable();
        table.setRowHeight(22);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setFillsViewportHeight(true);

        JScrollPane scrollPane = new JScrollPane(table);
        rightPanel.add(scrollPane, BorderLayout.CENTER);

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int viewRow = table.getSelectedRow();
                if (viewRow != -1) {
                    int modelRow = table.convertRowIndexToModel(viewRow);
                    DefaultTableModel m = (DefaultTableModel) table.getModel();
                    Object v0 = m.getValueAt(modelRow, 0);
                    Object v1 = m.getValueAt(modelRow, 1);
                    Object v2 = m.getValueAt(modelRow, 2);
                    Object v3 = m.getValueAt(modelRow, 3);
                    Object v4 = m.getValueAt(modelRow, 4);
                    Object v5 = m.getValueAt(modelRow, 5);
                    Object v6 = m.getValueAt(modelRow, 6);

                    studentIdField.setText(v0 == null ? "" : v0.toString());
                    lastNameField.setText(v1 == null ? "" : v1.toString());
                    firstNameField.setText(v2 == null ? "" : v2.toString());
                    majorField.setText(v3 == null ? "" : v3.toString());
                    phoneField.setText(v4 == null ? "" : v4.toString());
                    gpaField.setText(v5 == null ? "" : v5.toString());
                    dobField.setText(v6 == null ? "" : v6.toString());
                }
            }
        });

        JPanel centerPanel = new JPanel(new BorderLayout(20, 0));
        leftForm.setPreferredSize(new Dimension(260, 0));
        centerPanel.add(leftForm, BorderLayout.WEST);
        centerPanel.add(rightPanel, BorderLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);

        saveButton = new JButton("Add");
        clearButton = new JButton("Clear");
        refreshButton = new JButton("Refresh");
        editButton = new JButton("Edit");
        deleteButton = new JButton("Delete");
        cancelButton = new JButton("Cancel");

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 8));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        bottomPanel.add(saveButton);
        bottomPanel.add(deleteButton);
        bottomPanel.add(editButton);
        bottomPanel.add(clearButton);
        bottomPanel.add(refreshButton);
        bottomPanel.add(cancelButton);
        add(bottomPanel, BorderLayout.SOUTH);

        setSize(950, 520);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void addFormRow(JPanel panel, GridBagConstraints gbc, JComponent label, JComponent field) {
        gbc.gridx = 0;
        panel.add(label, gbc);
        gbc.gridx = 1;
        panel.add(field, gbc);
        gbc.gridy++;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GUI::new);
    }
}