package minhcreator.debugPage;

import minhcreator.component.CustomDialog;
import minhcreator.component.stock.StockStatus;
import minhcreator.functional.database.DB;
import minhcreator.util.Shared;

import javax.swing.*;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;

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

    public void init_components() {
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

    private static Vector<Vector<Object>> loadTableData_SQL(String sql) {

        try (ResultSet rs = new DB().selectSQL(sql)) {
            return Create_DataModel(rs);
        } catch (SQLException ex) {
            ex.printStackTrace();
//            JOptionPane.showMessageDialog(this, "Error loading data: " + ex.getMessage());
            return null;
        }
    }


    public static Vector<Vector<Object>> Create_DataModel(ResultSet rs) throws SQLException {


        ResultSetMetaData load_meta = rs.getMetaData();
        int columnCount = load_meta.getColumnCount();

        Vector<Vector<Object>> data = new Vector<>();
        while (rs.next()) {
            Vector<Object> row = new Vector<>();
            for (int i = 1; i <= columnCount; i++)
                row.add(rs.getObject(i));
            data.add(row);
        }
        return data;
    }

    public static Vector<Object> modelBuilding() throws SQLException {
        Object[] col = {Boolean.FALSE, "ID", "Name", "Price", "Amount", "Status"};
        Vector<Object> columns = new Vector<>();

        columns.addAll(Arrays.asList(col));

        Vector<Vector<Object>> data = loadTableData_SQL("SELECT * FROM users_inventory");
//        System.out.println(data);

        Vector<Vector<Object>> model = new Vector<>();

        for (int row_data = 0; row_data < data.size(); row_data++) {
            System.out.println(data.get(row_data));
            data.get(row_data).add(0, Boolean.FALSE);
            data.get(row_data).add(StockStatus.AVAIlABLE);

            model.add(data.get(row_data));
        }
        System.out.println(model);

        return null;
    }

    public static void trigger() {
        System.out.println("Triggered");
    }

    public static void main(String[] args) {
//        new FrameTest();
        JLabel label = new JLabel();
        JButton butt = new JButton();
//        butt.add()
//        butt.add()
    }
}