package minhcreator.functional.session;

/**
 * Class to manage session of user.
 *
 * @author MinhCreatorVN
 * @version 1.0.2 alpha
 */
public class sessionManager {

    /**
     * Singleton instance of sessionManager.
     */
    private static sessionManager instance;
    /**
     * Username of current user.
     */
    private String username = "";
    /**
     * Email of current user.
     */
    private String email = "";
    /**
     * Password of current user.
     */
    private String password = "";
    /**
     * ID of current user.
     */
    private String id = "";
    /**
     * Name of inventory table of current user.
     */
    private String your_inventory = "";
    private String user_product = "";
    /**
     * Name of export table of current user.
     */
    private String user_sale_table = "";
    /**
     * Name of stock add table of current user.
     */
    private String user_cost_table = "";
    private String user_invoices = "";
    private String user_invoice_details = "";

    /**
     * Constructor of sessionManager.
     */
    public sessionManager() {
    }

    /**
     * Getter of your_stock_add.
     *
     * @return your_stock_add of current user.
     */
    public String getUser_cost_table() {
        return user_cost_table;
    }

    /**
     * Setter of your_stock_add.
     *
     * @param user_cost_table new your_stock_add of current user.
     */
    public void setUser_cost_table(String user_cost_table) {
        this.user_cost_table = user_cost_table;
    }

    /**
     * Getter of singleton instance of sessionManager.
     *
     * @return singleton instance of sessionManager.
     */
    public synchronized sessionManager GetInstance() {
        if (instance == null) {
            instance = new sessionManager();
        }
        return instance;
    }

    /**
     * Function to create session of current user.
     *
     * @param username username of current user.
     * @param email    email of current user.
     * @param password password of current user.
     */
    public void createSession(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.your_inventory = this.username + "_inventory";
        this.user_product = this.username + "_products";
        this.user_sale_table = this.username + "_sales_orders";
        this.user_cost_table = this.username + "_purchase_orders";
        this.user_invoices = this.username + "_invoices";
        this.user_invoice_details = this.username + "_invoice_details";

        // Debug line for session manager
//        System.out.println("Session has stored " + username + " " + email + " " + password);
//        System.out.println("table is " + username + " inventory " + your_inventory + " export table " + user_sale_table);

    }

    /**
     * Function to login current user.
     *
     * @param id       id of current user.
     * @param username username of current user.
     * @param email    email of current user.
     * @param password password of current user.
     */
    public void login(String id, String username, String email, String password) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.your_inventory = this.username + "_inventory";
        this.user_product = this.username + "_products";
        this.user_sale_table = this.username + "_sales_orders";
        this.user_cost_table = this.username + "_purchase_orders";
        this.user_invoices = this.username + "_invoices";
        this.user_invoice_details = this.username + "_invoice_details";
        System.out.println("Session has stored " + username + " " + email + " " + password);
        System.out.println("table is " + username + " inventory " + your_inventory + " export table " + user_sale_table);
    }

    /**
     * Function to register current user.
     *
     * @param username username of current user.
     * @param email    email of current user.
     * @param password password of current user.
     */
    public void register(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.your_inventory = this.username + "_inventory";
        this.user_product = this.username + "_products";
        this.user_sale_table = this.username + "_sales_orders";
        this.user_cost_table = this.username + "_purchase_orders";
        this.user_invoices = this.username + "_invoices";
        this.user_invoice_details = this.username + "_invoice_details";
        System.out.println("Session has stored " + username + " " + email + " " + password);
        System.out.println("table is " + username + " inventory " + your_inventory + " export table " + user_sale_table);
    }

    /**
     * Getter of your_inventory.
     *
     * @return your_inventory of current user.
     */
    public String getYour_inventory() {
        return your_inventory;
    }

    /**
     * Setter of your_inventory.
     *
     * @param your_inventory new your_inventory of current user.
     */
    public void setYour_inventory(String your_inventory) {
        this.your_inventory = your_inventory;
    }

    /**
     * Getter of user sale table
     *
     * @return sale table of current user.
     */
    public String get_User_sale_table() {
        return user_sale_table;
    }

    /**
     * Setter of user sale table.
     *
     * @param user_sale_table new user sale table of current user.
     */
    public void set_User_sale_table(String user_sale_table) {
        this.user_sale_table = user_sale_table;
    }

    /**
     * Getter of id.
     *
     * @return id of current user.
     */
    public String getId() {
        return id;
    }

    /**
     * Setter of id.
     *
     * @param id new id of current user.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Getter of username.
     *
     * @return username of current user.
     */
    public String getUsername() {
        return username != null ? username : "";
    }

    /**
     * Setter of username.
     *
     * @param username new username of current user.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Getter of email.
     *
     * @return email of current user.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Setter of email.
     *
     * @param email new email of current user.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Getter of password.
     *
     * @return password of current user.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Setter of password.
     *
     * @param password new password of current user.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    public String getUser_invoices() {
        return user_invoices;
    }

    public void setUser_invoices(String user_invoices) {
        this.user_invoices = user_invoices;
    }

    public String getUser_invoice_details() {
        return user_invoice_details;
    }

    public void setUser_invoice_details(String user_invoice_details) {
        this.user_invoice_details = user_invoice_details;
    }

    public String getUser_product() {
        return user_product;
    }

    public void setUser_product(String user_product) {
        this.user_product = user_product;
    }

    /**
     * Function to clear session of current user.
     */
    public void clearSession() {
        username = null;
        email = null;
        password = null;
        your_inventory = null;
        user_sale_table = null;
    }
}