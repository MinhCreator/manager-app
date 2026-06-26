package minhcreator.functional.session;

/**
 * Manages the user session after login/signup.
 * Now stores userId instead of per-user table names
 * since Hibernate uses a unified schema with user_id column.
 *
 * @author MinhCreatorVN
 * @version 2.0 Hibernate
 */
public class sessionManager {

    private static sessionManager instance;
    private String id = "";
    private String username = "";
    private String email = "";
    private String password = "";

    /**
     * User identifier used to filter Hibernate queries by user_id.
     */
    private int userId = 0;

    public sessionManager() {}

    public static synchronized sessionManager GetInstance() {
        if (instance == null) {
            instance = new sessionManager();
        }
        return instance;
    }

    public void createSession(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public void login(String id, String username, String email, String password) {
        this.id = id;
        this.userId = Integer.parseInt(id);
        this.username = username;
        this.email = email;
        this.password = password;
        System.out.println("Session has stored user " + username + " (id=" + id + ")");
    }

    public void register(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
        System.out.println("Session registered user " + username);
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; this.userId = Integer.parseInt(id); }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; this.id = String.valueOf(userId); }

    public String getUsername() { return username != null ? username : ""; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    // Legacy getters — kept for backward compatibility
    public String getYour_inventory() { return "inventory"; }
    public String getUser_product() { return "products"; }
    public String get_User_sale_table() { return "sales_orders"; }
    public String getUser_cost_table() { return "purchase_orders"; }
    public String getUser_invoices() { return "invoices"; }
    public String getUser_invoice_details() { return "invoice_details"; }

    public void setYour_inventory(String s) {}
    public void setUser_product(String s) {}
    public void set_User_sale_table(String s) {}
    public void setUser_cost_table(String s) {}
    public void setUser_invoices(String s) {}
    public void setUser_invoice_details(String s) {}

    public void clearSession() {
        username = null;
        email = null;
        password = null;
        userId = 0;
        id = "";
    }
}
