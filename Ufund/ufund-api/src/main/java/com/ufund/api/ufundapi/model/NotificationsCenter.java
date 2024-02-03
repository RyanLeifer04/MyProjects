package com.ufund.api.ufundapi.model;

import java.util.ArrayList;
import java.util.HashMap;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class NotificationsCenter {
    /* Map of needs and their subscribers. */
    @JsonProperty("subscriptions")
    private HashMap<Integer, ArrayList<Integer>> subscriptions;

    /* Map of users and their notifications. */
    @JsonProperty("notifications")
    private HashMap<Integer, ArrayList<Integer>> notifications;

    /* Repository of @{link Notification notifications} for storage. */
    @JsonProperty("notificationsRepo")
    private Notification[] notificationsRepo;

    /**
     * Create a notifications center with the given subscriptions and notifications.
     *
     * @param subscriptions The subscriptions of the notifications center.
     * @param notifications The notifications of the notifications center.
     */
    public NotificationsCenter(@JsonProperty("subscriptions") HashMap<Integer, ArrayList<Integer>> subscriptions,
            @JsonProperty("notifications") HashMap<Integer, ArrayList<Integer>> notifications) {
        this.notificationsRepo = new Notification[0];
        this.subscriptions = subscriptions;
        this.notifications = notifications;
    }

    public NotificationsCenter(NotificationsCenter notificationsCenter) {
        this.subscriptions = new HashMap<Integer, ArrayList<Integer>>();
        for (Integer need : notificationsCenter.subscriptions.keySet()) {
            this.subscriptions.put(need, new ArrayList<Integer>());
            for (Integer user : notificationsCenter.subscriptions.get(need)) {
                this.subscriptions.get(need).add(user);
            }
        }
        this.notifications = new HashMap<Integer, ArrayList<Integer>>();
        for (Integer user : notificationsCenter.notifications.keySet()) {
            this.notifications.put(user, new ArrayList<Integer>());
            for (Integer notification : notificationsCenter.notifications.get(user)) {
                this.notifications.get(user).add(notification);
            }
        }
        this.notificationsRepo = new Notification[notificationsCenter.notificationsRepo.length];
        for (int i = 0; i < notificationsCenter.notificationsRepo.length; i++) {
            this.notificationsRepo[i] = new Notification(notificationsCenter.notificationsRepo[i].getId(),
                    notificationsCenter.notificationsRepo[i]);
        }
    }

    public NotificationsCenter() {
        this.subscriptions = new HashMap<Integer, ArrayList<Integer>>();
        this.notifications = new HashMap<Integer, ArrayList<Integer>>();
        this.notificationsRepo = new Notification[0];
    }

    /**
     * Retrieves the notifications of the user.
     *
     * @param id The id of the user.
     *
     * @return The notification IDs of the user.
     */
    public ArrayList<Integer> getNotifications(int id) {
        ArrayList<Integer> notifications = this.notifications.get(id);
        if (notifications == null)
            return new ArrayList<Integer>();
        return notifications;
    }

    /**
     * Adds a notification to the user.
     *
     * @param id           The id of the user.
     * @param notification The notification to add.
     *
     * @return true if successful; false if it already exists
     */
    public boolean addNotification(int id, Integer notificationID) {
        if (!this.notifications.containsKey(id))
            this.notifications.put(id, new ArrayList<Integer>());
        return this.notifications.get(id).add(notificationID);
    }

    /**
     * Removes a notification from the user.
     *
     * @param id
     * @return true if successful; false if it does not exist
     */
    public boolean removeNotification(int id, int notificationId) {
        if (!this.notifications.containsKey(id))
            return false;
        return this.notifications.get(id).remove(this.notifications.get(id).indexOf(notificationId)) != null;
    }

    /**
     * Retrieves the subscribers of the need.
     *
     * @param id The id of the need.
     *
     * @return The subscribers to the need.
     */
    public ArrayList<Integer> getSubscribers(int id) {
        return this.subscriptions.get(id);
    }

    /**
     * Adds a user to the subscribers of the need.
     *
     * @param id     The id of the need.
     * @param userId The id of the user.
     *
     * @return true if successful; false if user already exists
     */
    public boolean addSubscriber(int id, int userId) {
        if (!this.subscriptions.containsKey(id))
            this.subscriptions.put(id, new ArrayList<Integer>());
        return this.subscriptions.get(id).add(userId);
    }

    /**
     * Removes a user from the subscribers of the need.
     *
     * @param id     The id of the need.
     * @param userId The id of the user.
     *
     * @return true if successful; false if it does not exist
     */
    public boolean removeSubscriber(int id, int userId) {
        if (!this.subscriptions.containsKey(id))
            return false;
        return this.subscriptions.get(id).remove(this.subscriptions.get(id).indexOf(userId)) != null;
    }

    /**
     * Get the subscriptions of a user.
     *
     * @param id The id of the user.
     *
     * @return The subscriptions of the user.
     */
    public Integer[] getSubscriptions(int id) {
        ArrayList<Integer> subscriptions = new ArrayList<Integer>();
        for (Integer need : this.subscriptions.keySet()) {
            if (this.subscriptions.get(need).contains(id))
                subscriptions.add(need);
        }
        return subscriptions.toArray(new Integer[subscriptions.size()]);
    }

    /**
     * Set the repository of notifications.
     *
     * @param notificationsRepo The repository of notifications.
     */
    @JsonIgnore
    public void setRepository(Notification[] notificationsRepo) {
        this.notificationsRepo = notificationsRepo;
    }

    /**
     * Get the repository of notifications.
     *
     * @return The repository of notifications.
     */
    @JsonIgnore
    public Notification[] getRepository() {
        return this.notificationsRepo;
    }
}
