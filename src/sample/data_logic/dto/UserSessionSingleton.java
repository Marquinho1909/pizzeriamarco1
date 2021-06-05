package sample.data_logic.dto;

/**
 * Singleton Pattern for handling user session
 */
public class UserSessionSingleton {
    private static UserSessionSingleton currentSession = null;
    private User user;
    private UserSessionSingleton() {
    }

    public static UserSessionSingleton currentSession() {
        if (currentSession == null) {
            currentSession = new UserSessionSingleton();
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
