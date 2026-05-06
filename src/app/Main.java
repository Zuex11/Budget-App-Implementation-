package app;

/**
 * Application entry point for the Mizan budget management desktop app.
 *
 * <p>Instantiates {@link App}, which sets up the Swing UI and routes
 * to the correct startup screen based on application state.</p>
 */
public class Main {

    /** Utility class — do not instantiate. */
    private Main() {}

    /**
     * Launches the application.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        new App();
    }
}
