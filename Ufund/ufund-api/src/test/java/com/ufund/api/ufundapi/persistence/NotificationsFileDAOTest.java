package com.ufund.api.ufundapi.persistence;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.io.IOException;

import static org.mockito.ArgumentMatchers.any;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ufund.api.ufundapi.model.Notification;
import com.ufund.api.ufundapi.model.NotificationsCenter;

import org.junit.jupiter.api.*;

public class NotificationsFileDAOTest {
	private NotificationsFileDAO nFileDAO;
	private ObjectMapper mockObjectMapper;
	private NotificationsCenter notifCenter;

	@BeforeEach
	public void initialise() throws IOException {
		mockObjectMapper = mock(ObjectMapper.class);
		notifCenter = new NotificationsCenter();
		notifCenter.addSubscriber(0, 0);
		notifCenter.addSubscriber(1, 2);
		notifCenter.addSubscriber(2, 4);
		notifCenter.addSubscriber(2, 5);

		when(mockObjectMapper.readValue(new File("doesnt_matter.txt"), NotificationsCenter.class))
				.thenReturn(notifCenter);
		nFileDAO = new NotificationsFileDAO("doesnt_matter.txt", mockObjectMapper);
	}

	@Test
	public void testGetNotification() throws IOException {
		Notification testNotification = new Notification(0, 0, "test");
		nFileDAO.notifySubscribers(testNotification);
		Notification returnVal = nFileDAO.getNotification(1);

		assertSame(testNotification, returnVal);
	}

	@Test
	public void testGetSubscriptions() throws IOException {
		Integer[] expected = { 1 };
		Integer[] returnVal = nFileDAO.getSubscriptions(2);
		assertArrayEquals(expected, returnVal);
	}

	@Test
	public void testNotifySubscribers() throws IOException {
		Notification testNotification = new Notification(0, 0, "test");
		nFileDAO.notifySubscribers(testNotification);
		Integer[] returnVal = nFileDAO.getNotifications(0);

		assertEquals(1, returnVal.length);
	}

	@Test
	public void testNotifySubscribersNoSubscribers() throws IOException {
		Notification testNotification = new Notification(0, 0, "test");
		nFileDAO.notifySubscribers(testNotification);
		Integer[] returnVal = nFileDAO.getNotifications(1);

		assertEquals(0, returnVal.length);
	}

	@Test
	public void testNotifySubscribersMultipleSubscribers() throws IOException {
		Notification testNotification = new Notification(0, 2, "test");
		nFileDAO.notifySubscribers(testNotification);
		Integer[] returnVal1 = nFileDAO.getNotifications(4);
		Integer[] returnVal2 = nFileDAO.getNotifications(5);

		assertEquals(1, returnVal1.length);
		assertEquals(1, returnVal2.length);
	}

	@Test
	public void testRemoveNotification() throws IOException {
		Notification testNotification = new Notification(0, 0, "test");
		nFileDAO.notifySubscribers(testNotification);
		nFileDAO.removeNotification(0, 1);
		Integer[] returnVal = nFileDAO.getNotifications(0);

		assertEquals(0, returnVal.length);
	}

	@Test
	public void testSubscribe() throws IOException {
		nFileDAO.subscribe(0, 1);
		Integer[] returnVal = nFileDAO.getSubscriptions(0);

		assertEquals(2, returnVal.length);
	}

	@Test
	public void testUnsubscribe() throws IOException {
		nFileDAO.unsubscribe(0, 0);
		Integer[] returnVal = nFileDAO.getSubscriptions(0);

		assertEquals(0, returnVal.length);
	}
}
