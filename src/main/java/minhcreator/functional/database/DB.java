package minhcreator.functional.database;

import minhcreator.component.CustomDialog;
import raven.toast.*;
import minhcreator.main.Application;
import java.sql.*;
import java.util.Arrays;
import java.util.List;

public class DB {
    public Connection conn;
    public Statement stmt;

    public void getDBConnect(String host,String DBType, String DBName, String su, String pass){
        try {
            conn = DriverManager.getConnection("jdbc:" + DBType + "://" + host + "/" + DBName, su, pass);
//

            CustomDialog noice = new CustomDialog(
                    Application.getInstance(),
                    true,
                    "Database",
                    "Database connected successfully",
                    "minhcreator/assets/debug/success.svg",
                    "ok",
                    arr
            );

        } catch (Exception e) {
//
            CustomDialog noice = new CustomDialog(
                    Application.getInstance(),
                    true,
                    "Database",
                    e.getMessage(),
                    "minhcreator/assets/debug/Error.svg",
                    "ok",
                    arr
            );
        }
    }

    public void getDBConnectNoNotification(String host,String DBType, String DBName, String su, String pass){
        try {
            conn = DriverManager.getConnection("jdbc:" + DBType + "://" + host + "/" + DBName, su, pass);
            System.out.println("Database connected successfully");
//            CustomDialog noice = new CustomDialog(
//                    Application.getInstance(),
//                    true,
//                    "Database",
//                    "Database connected successfully",
//                    "minhcreator/assets/debug/success.svg",
//                    "ok",
//                    arr
//            );

        } catch (Exception e) {
            System.out.println(e.getMessage());
//            CustomDialog noice = new CustomDialog(
//                    Application.getInstance(),
//                    true,
//                    "Database",
//                    e.getMessage(),
//                    "minhcreator/assets/debug/Error.svg",
//                    "ok",
//                    arr
//            );
        }
    }

    // Update, insert, delete
    public int executionSQL(String Query_cmd) {
        int result = 0;
        try {
            stmt = conn.createStatement();
            result = stmt.executeUpdate(Query_cmd);
            CustomDialog noice = new CustomDialog(
                    Application.getInstance(),
                    true,
                    "Database",
                    "Query executed successfully" + " " + result,
                    "minhcreator/assets/debug/success.svg",
                    "ok",
                    arr
            );
        } catch (Exception e) {
            CustomDialog noice = new CustomDialog(
                    Application.getInstance(),
                    true,
                    "Database",
                    "Query executed Failure " + e.getMessage(),
                    "minhcreator/assets/debug/Error.svg",
                    "ok",
                    arr
            );
        }
        return result;
    }
    // Select
    public ResultSet selectSQL(String Query_cmd) {
        try {
            if (conn != null){
                stmt = conn.createStatement();
                CustomDialog noice = new CustomDialog(
                        Application.getInstance(),
                        true,
                        "Database",
                        "Query executed successfully" + " " + stmt.executeQuery(Query_cmd).toString(),
                        "minhcreator/assets/debug/success.svg",
                        "ok",
                        arr
                );
                return stmt.executeQuery(Query_cmd);
            }
        } catch (Exception e) {
            CustomDialog noice = new CustomDialog(
                    Application.getInstance(),
                    true,
                    "Database",
                    "Query executed Failure" + "\n" + e.getMessage(),
                    "minhcreator/assets/debug/Error.svg",
                    "ok",
                    arr
            );
        }
        return null;
    }

    public void closeDBConnect(){
        try {
            conn.close();
            Notifications.getInstance().show(
                    Notifications.Type.SUCCESS,
                    Notifications.Location.TOP_CENTER,
                    "Database closed successfully"
            );
        } catch (Exception e) {
            Notifications.getInstance().show(
                    Notifications.Type.ERROR,
                    Notifications.Location.TOP_CENTER,
                    "Database closed failure" + " " + e.getMessage()
            );
        }
    }
    // test
    public static void main(String[] args){
        DB db = new DB();
        db.getDBConnect("localhost", "mysql", "warehouse", "root", "");
    }

    private Integer[] newarr = {50,50};
    private List<Integer> arr = Arrays.asList(newarr);
}
