package tiketkereta.model;

public class Admin extends User {

    private String username;

    public Admin(String username, String password, String fullName) {
        super(fullName, password);
        this.username = username;
    }

    @Override
    public String getRole() {
        return "ADMIN";
    }

    @Override
    public String getIdentifier() {
        return username;
    }

    @Override
    public String getDisplaySubtitle() {
        return "Administrator (@" + username + ")";
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
