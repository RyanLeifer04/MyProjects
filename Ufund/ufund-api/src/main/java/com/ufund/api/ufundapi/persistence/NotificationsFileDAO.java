package com.ufund.api.ufundapi.persistence;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

import com.ufund.api.ufundapi.model.Need;
import com.ufund.api.ufundapi.model.Notification;
import com.ufund.api.ufundapi.model.NotificationsCenter;

/**
 * Implements the functionality for JSON file-based persistence of Notifications
 *
 * {@literal @}Component Spring annotation instantiates a single instance of
 * this class and injects the instance into other classes as needed
 */
@Component
public class NotificationsFileDAO implements NotificationsDAO {
    private static final Logger LOG = Logger.getLogger(CupboardFileDAO.class.getName());
    private Map<Integer, Notification> notifications; // In-memory cache for notifications objects
    private NotificationsCenter notificationsCenter; // notifications manager
    private ObjectMapper objectMapper; // Conversion object between Need object(s) and JSON text
    private static int nextID; // Next ID to assign to a new Notification
    private String filename; // Where to store the data on-disk

    /**
     * Creates a notifications File Data Access Object
     *
     * @param filename     File that will store the Need data
     * @param objectMapper Provides JSON Object to/from Java Object serialization
     *                     and deserialization
     *
     * @throws IOException when file cannot be accessed
     */
    public NotificationsFileDAO(@Value("${notifications.file}") String filename, ObjectMapper objectMapper)
            throws IOException {
        this.filename = filename;
        this.objectMapper = objectMapper;
        load();
    };

    /**
     * {@inheritDoc}
     */
    public Notification getNotification(int notificationID) throws IOException {
        return this.notifications.get(notificationID);
    }

    /**
     * {@inheritDoc}
     */
    public Integer[] getSubscriptions(int userID) throws IOException {
        return this.notificationsCenter.getSubscriptions(userID);
    }

    /**
     * {@inheritDoc}
     */
    public Integer[] getNotifications(int userID) throws IOException {
        return this.notificationsCenter.getNotifications(userID).toArray(new Integer[0]);
    }

    /**
     * {@inheritDoc}
     */
    public boolean notifySubscribers(Notification notification) throws IOException {
        notification.setID(nextID());
        notifications.put(notification.getId(), notification);
        for (int subscriber : this.notificationsCenter.getSubscribers(notification.getNeedID())) {
            this.notificationsCenter.addNotification(subscriber, notification.getId());
        }
        return save();
    }

    /**
     * {@inheritDoc}
     */
    public boolean removeNotification(int userID, int notificationID) throws IOException {
        return this.notificationsCenter.removeNotification(userID, notificationID) && save();
    }

    /**
     * {@inheritDoc}
     */
    public boolean subscribe(int userID, int needID) throws IOException {
        return this.notificationsCenter.addSubscriber(needID, userID) && save();
    }

    /**
     * {@inheritDoc}
     */
    public boolean unsubscribe(int userID, int needID) throws IOException {
        return this.notificationsCenter.removeSubscriber(needID, userID) && save();
    }

    /**
     * Saves the {@link Notification notifications} and the
     * {@link NotificationsCenter notifications center} to the JSON file
     *
     * @return true if the {@link Need needs} IDs were written successfully
     *
     * @throws IOException when file cannot be accessed
     */
    private boolean save() throws IOException {
        Notification[] notificationsArray = new Notification[this.notifications.size()];
        this.notifications.values().toArray(notificationsArray);
        this.notificationsCenter.setRepository(notificationsArray);
        objectMapper.writeValue(new File(filename), this.notificationsCenter);
        return true;
    };

    /**
     * Loads the {@link Notification notifications} and the
     * {@link NotificationsCenter notifications center} from the JSON file
     *
     * @return true if the file was read successfully
     *
     * @throws IOException when file cannot be accessed
     */
    private boolean load() throws IOException {
        this.notificationsCenter = objectMapper.readValue(createFile(filename), NotificationsCenter.class);
        this.notifications = new HashMap<Integer, Notification>();
        nextID = 0;

        for (Notification notification : this.notificationsCenter.getRepository()) {
            this.notifications.put(notification.getId(), notification);
            if (notification.getId() > nextID) {
                nextID = notification.getId();
            }
        }

        ++nextID;
        return true;
    };

    /**
     * Creates the storage File object, creating the file if it does not exist.
     *
     * @param filename The file to create
     *
     * @return The file object
     *
     * @throws IOException when file cannot be accessed/created
     */
    private File createFile(String filename) throws IOException {
        File file = new File(filename);
        if (!file.exists()) {
            try {
                if (file.getParentFile() != null) {
                    file.getParentFile().mkdirs();
                }
                if (!file.createNewFile()) {
                    LOG.severe("Could not create file " + filename);
                    throw new IOException("Could not create file " + filename);
                }
                // write empty JSON array to file
                objectMapper.writeValue(file, new NotificationsCenter());
                LOG.info(filename + " created");
            } catch (IOException e) {
                LOG.severe("Could not create file " + filename);
                throw e;
            }
        }
        return file;
    }

    /**
     * Generates the next ID for a new {@link Notification notification}
     *
     * @return the next ID
     */
    private synchronized static int nextID() {
        int id = nextID;
        ++nextID;
        return id;
    }
}
