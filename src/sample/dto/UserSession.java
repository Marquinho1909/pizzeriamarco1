package sample.dto;

/**
 * Singleton-Muster
 */
public class UserSession {
    private static UserSession currentSession = null;
    private User user;
    private UserSession () {
    }

    public static UserSession currentSession() {
        if (currentSession == null) {
            currentSession = new UserSession();
        }
        return currentSession;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void cleanUserSession() {
        user = null;
    }
}
