package BugTracker.bugket.models;

public class TempAppUserDetails{
    private final String username;
    private String role;

    public TempAppUserDetails(String username, String role) {
        this.username = username;
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
