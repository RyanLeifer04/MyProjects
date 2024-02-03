package com.ufund.api.ufundapi.model;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.HashMap;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

@Tag("Model-tier")
public class NotificationsCenterTest {
	private HashMap<Integer, ArrayList<Integer>> subscriptions;
	private HashMap<Integer, ArrayList<Integer>> notifications;

	@BeforeEach
	public void initialise() {
		subscriptions = new HashMap<>();
		ArrayList<Integer> subscriberList = new ArrayList<>();
		subscriberList.add(0);
		subscriberList.add(3);
		subscriberList.add(8);

		subscriptions.put(1, subscriberList);

		notifications = new HashMap<>();
		ArrayList<Integer> userNotifList = new ArrayList<>();
		userNotifList.add(5);
		userNotifList.add(2);
		userNotifList.add(6);

		notifications.put(4, userNotifList);
	}

	@Test
	public void testDeepCopy() {
		// Setup
		NotificationsCenter notifCenter = new NotificationsCenter(subscriptions, notifications);
		notifCenter.setRepository(new Notification[] { new Notification(0, 0, "test") });
		NotificationsCenter notifCenter2 = new NotificationsCenter(notifCenter);

		// Invoke
		notifCenter2.addNotification(0, 1);

		// Analyse
		assertEquals(0, notifCenter.getNotifications(0).size());
	}

	@Test
	public void testGetSubscribers() {
		// Setup
		NotificationsCenter notifCenter = new NotificationsCenter(subscriptions, notifications);
		ArrayList<Integer> returnValue = new ArrayList<>();
		returnValue.add(1);

		// Invoke
		notifCenter.addSubscriber(0, 1);

		// Analyse
		assertEquals(returnValue, notifCenter.getSubscribers(0));
	}

	@Test
	public void testRemoveSubscribers() {
		// Setup
		NotificationsCenter notifCenter = new NotificationsCenter(subscriptions, notifications);
		ArrayList<Integer> returnValue = new ArrayList<>();
		notifCenter.addSubscriber(0, 1);

		// Invoke
		notifCenter.removeSubscriber(0, 1);

		// Analyse
		assertEquals(returnValue, notifCenter.getSubscribers(0));
	}

	@Test
	public void testRemoveSubscribersFail() {
		// Setup
		NotificationsCenter notifCenter = new NotificationsCenter(subscriptions, notifications);

		// Invoke
		notifCenter.removeSubscriber(0, 1);

		// Analyse
		assertEquals(null, notifCenter.getSubscribers(0));
	}

	@Test
	public void testGetSubscriptionsArray() {
		// Setup
		NotificationsCenter notifCenter = new NotificationsCenter(subscriptions, notifications);
		notifCenter.addSubscriber(0, 1);

		// Invoke
		Integer[] subArray = notifCenter.getSubscriptions(0);

		// Analyse
		assertEquals(1, subArray.length);
	}

	@Test
	public void testGetNotifications() {
		// Setup
		NotificationsCenter notifCenter = new NotificationsCenter(subscriptions, notifications);
		ArrayList<Integer> returnValue = new ArrayList<>();
		returnValue.add(1);

		// Invoke
		notifCenter.addNotification(0, 1);

		// Analyse
		assertEquals(returnValue, notifCenter.getNotifications(0));
	}

	@Test
	public void testGetEmptyNotifications() {
		// Setup
		NotificationsCenter notifCenter = new NotificationsCenter(subscriptions, notifications);
		ArrayList<Integer> returnValue = new ArrayList<>();

		// Invoke and Analyse
		assertEquals(returnValue, notifCenter.getNotifications(0));
	}

	@Test
	public void testRemoveNotifications() {
		// Setup
		NotificationsCenter notifCenter = new NotificationsCenter(subscriptions, notifications);
		ArrayList<Integer> returnValue = new ArrayList<>();
		notifCenter.addNotification(0, 1);

		// Invoke
		notifCenter.removeNotification(0, 1);

		// Analyse
		assertEquals(returnValue, notifCenter.getNotifications(0));
	}

	@Test
	public void testRemoveNotificationsFail() {
		// Setup
		NotificationsCenter notifCenter = new NotificationsCenter(subscriptions, notifications);
		ArrayList<Integer> returnValue = new ArrayList<>();

		// Invoke
		notifCenter.removeNotification(0, 1);

		// Analyse
		assertEquals(returnValue, notifCenter.getNotifications(0));
	}

	@Test
	public void testClone() {
		// Setup
		NotificationsCenter notifCenter = new NotificationsCenter(subscriptions, notifications);
		ArrayList<Integer> returnValue = new ArrayList<>();
		returnValue.add(1);
		notifCenter.addSubscriber(0, 1);
		notifCenter.addNotification(0, 1);

		// Invoke
		NotificationsCenter clonedCenter = new NotificationsCenter(notifCenter);

		// Analyse
		assertEquals(returnValue, clonedCenter.getNotifications(0));
		assertEquals(returnValue, clonedCenter.getSubscribers(0));
	}
}
