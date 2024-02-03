package com.ufund.api.ufundapi.persistence;

import java.io.IOException;
import com.ufund.api.ufundapi.model.Need;
import com.ufund.api.ufundapi.model.Notification;

public interface NotificationsDAO {

    /**
     * Get a @{link Notification notification} by its ID
     *
     * @param notificationID The ID of the notification to retrieve
     * @return The notification with the given ID
     * @throws IOException if an issue with underlying storage
     */
    Notification getNotification(int notificationID) throws IOException;

    /**
     * Retrieves IDs of all {@linkplain Need needs} that the user is subscribed to
     *
     * @param userID The ID of the user to retrieve the {@link Need needs}
     *
     * @return An array of {@link Need need} IDs, may be empty
     *
     * @throws IOException if an issue with underlying storage
     */
    Integer[] getSubscriptions(int userID) throws IOException;

    /**
     * Retrieves IDs of all {@linkplain Notification notifications} in the user's
     * inbox
     *
     * @param userID The ID of the user to retrieve the {@link Need needs}
     *
     * @return An array of {@link Need need} IDs, may be empty
     *
     * @throws IOException if an issue with underlying storage
     */
    Integer[] getNotifications(int userID) throws IOException;

    /**
     * Notifies users subscribed to a {@link Need need} with a {@link Notification
     * notification}
     *
     * @param needID       The ID of the {@link Need need} to notify subscribers
     *                     of
     * @param notification The {@link Notification notification} to send
     *
     * @return true if successful; false if notification doesn't exist
     */
    boolean notifySubscribers(Notification notification) throws IOException;

    /**
     * Remove a notification from the user's inbox
     *
     * @param userID         The ID of the user to remove the notification from
     * @param notificationID The ID of the notification to remove
     *
     * @return true if successful; false if it does not exist
     */
    boolean removeNotification(int userID, int notificationID) throws IOException;

    /**
     * Subscribe a @{link User user} to a {@link Need need}
     *
     * @param userID The ID of the user to subscribe
     * @param needID The ID of the need to subscribe to
     *
     * @return true if successful; false if it already exists
     */
    boolean subscribe(int userID, int needID) throws IOException;

    /**
     * Unsubscribe a @{link User user} from a {@link Need need}
     *
     * @param userID The ID of the user to unsubscribe
     * @param needID The ID of the need to unsubscribe from
     *
     * @return true if successful; false if it does not exist
     */
    boolean unsubscribe(int userID, int needID) throws IOException;
}
