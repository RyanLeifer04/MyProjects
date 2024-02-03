package com.ufund.api.ufundapi.model;

import java.lang.annotation.Inherited;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Notification {
    /* The id of the notification. */
    @JsonProperty("id")
    private int id;

    /* The ID of the need with the notification. */
    @JsonProperty("needID")
    private int needID;

    /* The message of the notification. */
    @JsonProperty("message")
    private String message;

    /**
     * Create a notification with the given id and message.
     *
     * @param id      The id of the notification.
     * @param message The message of the notification.
     */
    public Notification(@JsonProperty("id") int id, @JsonProperty("needID") int needID,
            @JsonProperty("message") String message) {
        this.id = id;
        this.needID = needID;
        this.message = message;
    }

    public Notification(int id, Notification notification) {
        this.id = id;
        this.needID = notification.needID;
        this.message = new String(notification.message);
    }

    /**
     * Retrieves the ID of the notification.
     *
     * @return The ID of the notification.
     */
    public int getId() {
        return this.id;
    }

    /**
     * Set the ID of the notification.
     *
     * @return The ID of the notification.
     */
    public void setID(int id) {
        this.id = id;
    }

    /**
     * Retrieves the need id of the notification.
     *
     * @return The need id of the notification.
     */
    public int getNeedID() {
        return this.needID;
    }

    /**
     * Retrieves the message of the notification.
     *
     * @return The message of the notification.
     */
    public String getMessage() {
        return this.message;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object anotherObject) {
        if (!(anotherObject instanceof Notification))
            return false;
        Notification castObject = (Notification) anotherObject;
        return this.id == castObject.id &&
                this.needID == castObject.id &&
                this.message == castObject.message;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return this.id;
    }
}
