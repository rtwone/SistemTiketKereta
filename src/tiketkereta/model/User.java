package tiketkereta.model;

public abstract class User {

    private String fullName;
    private String password;

    protected User(String fullName, String password) {
        this.fullName = fullName;
        this.password = password;
    }

    public abstract String getRole();

    public abstract String getIdentifier();

    public abstract String getDisplaySubtitle();

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isAdmin() {
        return "ADMIN".equalsIgnoreCase(getRole());
    }
}
