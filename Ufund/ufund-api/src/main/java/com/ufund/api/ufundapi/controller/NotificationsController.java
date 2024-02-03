package com.ufund.api.ufundapi.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ufund.api.ufundapi.model.*;
import com.ufund.api.ufundapi.persistence.NotificationsDAO;

/**
 * Handles the REST API requests for the Notification resource
 * <p>
 * {@literal @}RestController Spring annotation identifies this class as a REST
 * API method handler to the Spring framework
 */

@RestController
@RequestMapping("notifications")
public class NotificationsController {
    private static final Logger LOG = Logger.getLogger(FundingBasketController.class.getName());
    private NotificationsDAO notificationsDao;

    /**
     * Creates a REST API controller to respond to requests
     *
     * @param notificationsDao The {@link NotificationsDao notifications Data Access
     *                         Object} to perform CRUD operations
     *                         <br>
     *                         This dependency is injected by Spring Framework
     */
    public NotificationsController(NotificationsDAO notificationsDao) {
        this.notificationsDao = notificationsDao;
    }

    /**
     * Push a notification to users subscribed to the @{link Need need}
     *
     * @param id           The ID of the {@link Need need} to push the notification
     *                     for
     * @param notification The notification to push
     *
     * @return ResponseEntity with the added {@link Notification notification}
     *         object
     *         and HTTP status of CREATED<br>
     *         ResponseEntity with HTTP status of CONFLICT if {@link Notification
     *         notification} ID already exists<br>
     *         ResponseEntity with HttpStatus of INTERNAL_SERVER_ERROR otherwise
     */
    @PostMapping("/push/{needID}")
    public ResponseEntity<Notification> pushNotification(@PathVariable Integer needID,
            @RequestBody String notificationMessage) {
        LOG.info("POST /notifications/" + needID + " " + notificationMessage);
        try {
            Notification notification = new Notification(0, needID, notificationMessage);
            if (notificationsDao.notifySubscribers(notification))
                return new ResponseEntity<Notification>(notification, HttpStatus.CREATED);
            else
                return new ResponseEntity<>(HttpStatus.CONFLICT);
        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Get the {@link Notification notifications} for a user
     *
     * @param userID The ID of the user to get the {@link Notification
     *               notifications}
     *               for
     *
     * @return ResponseEntity with the {@link Notification notifications} objects
     *         and HTTP status of OK<br>
     *         ResponseEntity with HttpStatus of INTERNAL_SERVER_ERROR otherwise
     */
    @GetMapping("/{userID}")
    public ResponseEntity<Notification[]> getNotifications(@PathVariable Integer userID) {
        LOG.info("GET /notifications/" + userID);
        try {
            Integer[] ids = notificationsDao.getNotifications(userID);
            Notification[] notifications = new Notification[ids.length];
            for (int i = 0; i < ids.length; i++) {
                notifications[i] = notificationsDao.getNotification(ids[i]);
            }
            return new ResponseEntity<Notification[]>(notifications, HttpStatus.OK);
        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Remove a notification from the user's inbox
     *
     * @param userID         The ID of the user to remove the notification from
     * @param notificationID The ID of the notification to remove
     *
     * @return ResponseEntity with HTTP status of OK<br>
     *         ResponseEntity with HTTP status of NOT_FOUND if notification does not
     *         exist<br>
     *         ResponseEntity with HttpStatus of INTERNAL_SERVER_ERROR otherwise
     */
    @DeleteMapping("/{userID}/{notificationID}")
    public ResponseEntity<Void> removeNotification(@PathVariable Integer userID, @PathVariable Integer notificationID) {
        LOG.info("DELETE /notifications/" + userID + "/" + notificationID);
        try {
            if (notificationsDao.removeNotification(userID, notificationID))
                return new ResponseEntity<>(HttpStatus.OK);
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Subscribe a user to a {@link Need need}
     *
     * @param userID The ID of the user to subscribe
     * @param needID The ID of the need to subscribe to
     *
     * @return ResponseEntity with HTTP status of CREATED<br>
     *         ResponseEntity with HTTP status of CONFLICT if subscription already
     *         exists<br>
     *         ResponseEntity with HttpStatus of INTERNAL_SERVER_ERROR otherwise
     */
    @PostMapping("/{userID}/subscriptions/{needID}")
    public ResponseEntity<Void> subscribe(@PathVariable Integer needID, @PathVariable Integer userID) {
        LOG.info("POST /notifications/" + userID + "/subscriptions/" + needID);
        try {
            if (notificationsDao.subscribe(userID, needID))
                return new ResponseEntity<>(HttpStatus.CREATED);
            else
                return new ResponseEntity<>(HttpStatus.CONFLICT);
        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Unsubscribe a user from a {@link Need need}
     *
     * @param userID The ID of the user to unsubscribe
     * @param needID The ID of the need to unsubscribe from
     *
     * @return ResponseEntity with HTTP status of OK<br>
     *         ResponseEntity with HTTP status of NOT_FOUND if subscription does not
     *         exist<br>
     *         ResponseEntity with HttpStatus of INTERNAL_SERVER_ERROR otherwise
     */
    @DeleteMapping("/{userID}/subscriptions/{needID}")
    public ResponseEntity<Void> unsubscribe(@PathVariable Integer needID, @PathVariable Integer userID) {
        LOG.info("DELETE /notifications/" + userID + "/subscriptions/" + needID);
        try {
            if (notificationsDao.unsubscribe(userID, needID))
                return new ResponseEntity<>(HttpStatus.OK);
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Get the {@link Need needs} that a user is subscribed to
     *
     * @param userID The ID of the user to get the {@link Need needs} for
     *
     * @return ResponseEntity with the {@link Need needs} IDs and HTTP status of
     *         OK<br>
     *         ResponseEntity with HttpStatus of INTERNAL_SERVER_ERROR otherwise
     */
    @GetMapping("/{userID}/subscriptions")
    public ResponseEntity<Integer[]> getSubscriptions(@PathVariable Integer userID) {
        LOG.info("GET /notifications/" + userID + "/subscriptions");
        try {
            Integer[] needs = notificationsDao.getSubscriptions(userID);
            return new ResponseEntity<Integer[]>(needs, HttpStatus.OK);
        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
