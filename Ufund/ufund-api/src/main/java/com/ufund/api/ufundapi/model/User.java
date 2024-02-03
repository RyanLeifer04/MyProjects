package com.ufund.api.ufundapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.logging.Logger;

import javax.crypto.spec.PBEKeySpec;
import javax.crypto.SecretKeyFactory;

public class User {
    private static final Logger LOG = Logger.getLogger(User.class.getName());
    private static final String STRING_FORMAT = "User [id=%d, first=%s, last=%s, username='%s', hash='%s', admin='%s', salt='%s']";
    // Using recommended algorithm by OWASP, which is built directly into Java
    private final String KEY_GEN_ALGORITHM = "PBKDF2WithHmacSHA256";
    private final int KEY_LENGTH = 128;

    // OWASP recommended value >=600,000
    // Value used is 2^20
    // Larger values take longer to calculate, but are more secure
    private final int PBKDF_ITERARTIONS = 1048576;

    private final SecureRandom random = new SecureRandom();
    @JsonProperty("salt")
    private byte[] salt;
    @JsonProperty("id")
    private final int id;
    @JsonProperty("firstName")
    private String firstName;
    @JsonProperty("lastName")
    private String lastName;
    @JsonProperty("username")
    private String username;
    @JsonProperty("pass_hash")
    private byte[] passHash;
    @JsonProperty("admin")
    private boolean isAdmin;

    public User(@JsonProperty("id") int id,
            @JsonProperty("first_name") String first_name,
            @JsonProperty("last_name") String last_name,
            @JsonProperty("username") String username,
            @JsonProperty("password") String clear_pass,
            @JsonProperty("admin") boolean is_admin) {
        this.id = id;
        this.firstName = first_name;
        this.lastName = last_name;
        this.username = username;
        this.isAdmin = is_admin;
        if (clear_pass == null)
            clear_pass = "admin";
        this.salt = new byte[256];
        random.nextBytes(this.salt);
        try {
            KeySpec spec = new PBEKeySpec(clear_pass.toCharArray(), salt, PBKDF_ITERARTIONS, KEY_LENGTH);
            SecretKeyFactory factory = SecretKeyFactory.getInstance(KEY_GEN_ALGORITHM);
            this.passHash = factory.generateSecret(spec).getEncoded();
        } catch (Exception e) {
            this.passHash = new byte[25];
        }
    }

    public User(int id, User user) {
        this.salt = new byte[256];
        this.salt = user.salt;
        this.id = id;
        this.firstName = user.firstName;
        this.lastName = user.lastName;
        this.username = user.username;
        this.passHash = user.passHash;
        this.isAdmin = user.isAdmin;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return this.username;
    }

    public boolean isAdmin() {
        return this.isAdmin;
    }

    public void setAdmin(boolean admin) {
        this.isAdmin = admin;
    }

    public int getID() {
        return this.id;
    }

    public boolean isPassword(String pass_test) {
        try {
            KeySpec spec = new PBEKeySpec(pass_test.toCharArray(), salt, PBKDF_ITERARTIONS, KEY_LENGTH);
            SecretKeyFactory factory = SecretKeyFactory.getInstance(KEY_GEN_ALGORITHM);
            byte[] newHash = factory.generateSecret(spec).getEncoded();
            LOG.info(new String(this.passHash, StandardCharsets.UTF_8));
            LOG.info(new String(newHash, StandardCharsets.UTF_8));
            int diff = this.passHash.length ^ newHash.length;
            for (int i = 0; i < this.passHash.length && i < newHash.length; i++) {
                diff |= this.passHash[i] ^ newHash[i];
            }
            return diff == 0;
        } catch (Exception e) {
            return false;
        }
    }

    public String toString() {
        return String.format(STRING_FORMAT, this.id, this.firstName, this.lastName, this.username,
                new String(this.passHash, StandardCharsets.UTF_8), this.isAdmin,
                new String(this.salt, StandardCharsets.UTF_8));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object otherObject) {
        if (!(otherObject instanceof User))
            return false;
        User castObject = (User) otherObject;
        return this.id == castObject.id &&
                this.firstName.equals(castObject.firstName) &&
                this.lastName.equals(castObject.lastName) &&
                this.username.equals(castObject.username) &&
                this.passHash == castObject.passHash &&
                this.salt == castObject.salt &&
                this.isAdmin == castObject.isAdmin;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return this.id;
    }
}
