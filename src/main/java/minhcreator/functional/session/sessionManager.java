package minhcreator.functional.session;

public class sessionManager {
    private String username;
    private String email;
    private String password;
    private String id;
    private static sessionManager instance;

    public sessionManager() {
    }

    public sessionManager GetInstance() {
        if (instance == null) {
            instance = new sessionManager();
        }
        return instance;
    }

    public void createSession(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
        System.out.println("Session has stored" + username + " " + email + " " + password);
    }

    public void login(String id, String username, String email, String password) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        System.out.println("Session has stored" + username + " " + email + " " + password);
    }

    public void register(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
        System.out.println("Session has stored" + username + " " + email + " " + password);
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void clearSession() {
        username = null;
        email = null;
        password = null;
    }
}