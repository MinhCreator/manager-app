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

    /**
     * Name of export table of current user.
     */
    private String your_export_table = "";

    /**
     * Name of stock add table of current user.
     */
    private String your_stock_add = "";

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
    public String getYour_stock_add() {
        return your_stock_add;
    }

    /**
     * Setter of your_stock_add.
     *
     * @param your_stock_add new your_stock_add of current user.
     */
    public void setYour_stock_add(String your_stock_add) {
        this.your_stock_add = your_stock_add;
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
        this.your_export_table = this.username + "_export_table";
        this.your_stock_add = this.username + "_stock_add";
        System.out.println("Session has stored " + username + " " + email + " " + password);
        System.out.println("table is " + username + " inventory " + your_inventory + " export table " + your_export_table);
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
        this.your_export_table = this.username + "_export_table";
        this.your_stock_add = this.username + "_stock_add";
        System.out.println("Session has stored " + username + " " + email + " " + password);
        System.out.println("table is " + username + " inventory " + your_inventory + " export table " + your_export_table);
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
        this.your_export_table = this.username + "_export_table";
        this.your_stock_add = this.username + "_stock_add";
        System.out.println("Session has stored " + username + " " + email + " " + password);
        System.out.println("table is " + username + " inventory " + your_inventory + " export table " + your_export_table);
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
     * Getter of your_export_table.
     *
     * @return your_export_table of current user.
     */
    public String getYour_export_table() {
        return your_export_table;
    }

    /**
     * Setter of your_export_table.
     *
     * @param your_export_table new your_export_table of current user.
     */
    public void setYour_export_table(String your_export_table) {
        this.your_export_table = your_export_table;
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

    /**
     * Function to clear session of current user.
     */
    public void clearSession() {
        username = null;
        email = null;
        password = null;
        your_inventory = null;
        your_export_table = null;
    }
}