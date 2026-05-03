package control;

import persistence.DatabaseHelper;

import java.security.MessageDigest;
import java.time.LocalDateTime;

import domain.*;

public class AuthManager {
    private DatabaseHelper dbHelper;
    private int failedAttempts;
    private LocalDateTime lockoutTime;
    private static final int MAX_ATTEMPTS = 3;
    private static final long LOCKOUT_DURATION = 60;

    public AuthManager() {
        this.dbHelper = DatabaseHelper.getInstance();
        this.failedAttempts = 0;
        this.lockoutTime = null;
    }

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

    public void setPIN(String PIN) {
        String hashedPin = hash(PIN);
        dbHelper.savePin(hashedPin);
    }

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

    public boolean isLockedOut() {
        if (lockoutTime == null)
            return false;
        return LocalDateTime.now().isBefore(lockoutTime);
    }

    public void recordFailedAttempt() {
        failedAttempts++;
        if (failedAttempts >= MAX_ATTEMPTS) {
            lockoutTime = LocalDateTime.now().plusSeconds(LOCKOUT_DURATION);
        }
    }

    public boolean hasPin() {
        return dbHelper.getPin() != null;
    }

    public long getSecondsRemaining() {
        if (lockoutTime == null)
            return 0;
        return java.time.Duration.between(LocalDateTime.now(), lockoutTime).getSeconds();
    }



}