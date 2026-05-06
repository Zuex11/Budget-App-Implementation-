package control;

import persistence.DatabaseHelper;

import java.security.MessageDigest;
import java.time.LocalDateTime;

import domain.*;

/**
 * Manages PIN-based authentication and lockout logic for the application.
 *
 * <p>PINs are never stored in plain text; they are hashed with SHA-256 before
 * being persisted via {@link DatabaseHelper}. After {@value #MAX_ATTEMPTS}
 * consecutive wrong attempts the user is locked out for
 * {@value #LOCKOUT_DURATION} seconds.</p>
 */
public class AuthManager {

    /** Backing store used to persist and retrieve the hashed PIN. */
    private DatabaseHelper dbHelper;

    /** Number of consecutive failed unlock attempts in the current session. */
    private int failedAttempts;

    /** Time at which the lockout expires; {@code null} when not locked out. */
    private LocalDateTime lockoutTime;

    /** Maximum number of failed attempts before a lockout is triggered. */
    private static final int MAX_ATTEMPTS = 3;

    /** Duration of the lockout period in seconds. */
    private static final long LOCKOUT_DURATION = 60;

    /**
     * Constructs an AuthManager and resets the failed-attempt counter.
     */
    public AuthManager() {
        this.dbHelper = DatabaseHelper.getInstance();
        this.failedAttempts = 0;
        this.lockoutTime = null;
    }

    /**
     * Hashes a plain-text PIN using SHA-256.
     *
     * @param pin plain-text PIN string
     * @return lowercase hex-encoded SHA-256 digest, or {@code null} on error
     */
    private String hash(String pin) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] bytes = md.digest(pin.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes)
                sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Hashes and persists a new PIN, replacing any previously stored one.
     * Pass {@code null} to remove the PIN entirely.
     *
     * @param PIN plain-text 4-digit PIN, or {@code null} to disable PIN lock
     */
    public void setPIN(String PIN) {
        String hashedPin = hash(PIN);
        dbHelper.savePin(hashedPin);
    }

    /**
     * Verifies an entered PIN against the stored hash.
     * Resets the failed-attempt counter on success; increments it on failure.
     * Always returns {@code false} while the user is locked out.
     *
     * @param pin plain-text PIN entered by the user
     * @return {@code true} if the PIN matches and the user is not locked out
     */
    public boolean verifyPIN(String pin) {
        if (isLockedOut())
            return false;
        String stored = dbHelper.getPin();
        if (stored != null && stored.equals(hash(pin))) {
            failedAttempts = 0;
            lockoutTime = null;
            return true;
        }
        recordFailedAttempt();
        return false;
    }

    /**
     * Checks whether the user is currently in a lockout period.
     *
     * @return {@code true} if locked out and the lockout has not yet expired
     */
    public boolean isLockedOut() {
        if (lockoutTime == null)
            return false;
        return LocalDateTime.now().isBefore(lockoutTime);
    }

    /**
     * Increments the failed-attempt counter and starts a lockout if the
     * maximum number of attempts has been reached.
     */
    public void recordFailedAttempt() {
        failedAttempts++;
        if (failedAttempts >= MAX_ATTEMPTS) {
            lockoutTime = LocalDateTime.now().plusSeconds(LOCKOUT_DURATION);
        }
    }

    /**
     * Checks whether a PIN has been set for this application.
     *
     * @return {@code true} if a hashed PIN exists in the database
     */
    public boolean hasPin() {
        return dbHelper.getPin() != null;
    }

    /**
     * Returns the number of seconds remaining in the current lockout.
     *
     * @return seconds until lockout expires, or 0 if not locked out
     */
    public long getSecondsRemaining() {
        if (lockoutTime == null)
            return 0;
        return java.time.Duration.between(LocalDateTime.now(), lockoutTime).getSeconds();
    }
}
