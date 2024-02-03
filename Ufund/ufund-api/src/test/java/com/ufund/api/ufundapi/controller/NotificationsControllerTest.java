package com.ufund.api.ufundapi.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import javax.annotation.Resource;

import com.ufund.api.ufundapi.model.Notification;
import com.ufund.api.ufundapi.persistence.NotificationsDAO;

import org.junit.jupiter.api.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Tag("Controller-tier")
public class NotificationsControllerTest {
	private NotificationsController notifController;
	private NotificationsDAO mockNotifDao;

	@BeforeEach
	public void initialise() {
		mockNotifDao = mock(NotificationsDAO.class);
		notifController = new NotificationsController(mockNotifDao);
	}

	@Test
	public void testSubscribe() throws IOException{
		//Setup
		when(mockNotifDao.subscribe(0,0)).thenReturn(true);
		Integer returnVal = 0;
		Integer[] returnArray = new Integer[1];
		returnArray[0] = returnVal;

		//Invoke
		ResponseEntity<Void> response = notifController.subscribe(0,0);

		//Analyse
		assertEquals(HttpStatus.CREATED,response.getStatusCode());
	}

	@Test
	public void testSubscribeFail() throws IOException{
		//Setup
		when(mockNotifDao.subscribe(0,0)).thenReturn(false);
		Integer returnVal = 0;
		Integer[] returnArray = new Integer[1];
		returnArray[0] = returnVal;

		//Invoke
		ResponseEntity<Void> response = notifController.subscribe(0,0);

		//Analyse
		assertEquals(HttpStatus.CONFLICT,response.getStatusCode());
	}

	@Test
	public void testSubscribeInternalError() throws IOException{
		//Setup
		when(mockNotifDao.subscribe(0,0)).thenThrow(new IOException());
		Integer returnVal = 0;
		Integer[] returnArray = new Integer[1];
		returnArray[0] = returnVal;

		//Invoke
		ResponseEntity<Void> response = notifController.subscribe(0,0);

		//Analyse
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
	}

	@Test
	public void testGetNotif() throws IOException {
		// Setup
		Integer returnVal = 0;
		Integer[] returnArray = new Integer[1];
		returnArray[0] = returnVal;
		when(mockNotifDao.getNotifications(0)).thenReturn(returnArray);

		// Invoke
		ResponseEntity<Notification[]> response = notifController.getNotifications(0);

		// Analyse
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

	@Test
	public void testGetNotifInternalError() throws IOException{
		when(mockNotifDao.getNotifications(0)).thenThrow(new IOException());
		ResponseEntity<Notification[]> response = notifController.getNotifications(0);
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
	}

	@Test
	public void testPushNotification() throws IOException{
		when(mockNotifDao.notifySubscribers(new Notification(0,0,"test message"))).thenReturn(true);
		ResponseEntity<Notification> response = notifController.pushNotification(0,"test message");
		assertEquals(HttpStatus.CREATED,response.getStatusCode());
	}

	@Test
	public void testPushNotificationFail() throws IOException{
		when(mockNotifDao.notifySubscribers(new Notification(0,0,"test message"))).thenReturn(false);
		ResponseEntity<Notification> response = notifController.pushNotification(0,"test message");
		assertEquals(HttpStatus.CONFLICT,response.getStatusCode());
	}

	@Test
	public void testPushNotificationInternalError() throws IOException{
		when(mockNotifDao.notifySubscribers(new Notification(0,0,"test message"))).thenThrow(new IOException());
		ResponseEntity<Notification> response = notifController.pushNotification(0,"test message");
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
	}

	@Test
	public void testUnsubscribe() throws IOException{
		when(mockNotifDao.unsubscribe(0,0)).thenReturn(true);
		ResponseEntity<Void> response = notifController.unsubscribe(0,0);
		assertEquals(HttpStatus.OK,response.getStatusCode());
	}

	@Test
	public void testUnsubscribeFail() throws IOException{
		when(mockNotifDao.unsubscribe(0,0)).thenReturn(false);
		ResponseEntity<Void> response = notifController.unsubscribe(0,0);
		assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
	}

	@Test
	public void testUnsubscribeInternalError() throws IOException{
		when(mockNotifDao.unsubscribe(0,0)).thenThrow(new IOException());
		ResponseEntity<Void> response = notifController.unsubscribe(0,0);
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
	}

	@Test
	public void testRemoveNotif() throws IOException{
		when(mockNotifDao.removeNotification(0,0)).thenReturn(true);
		ResponseEntity<Void> response = notifController.removeNotification(0,0);
		assertEquals(HttpStatus.OK,response.getStatusCode());
	}

	@Test
	public void testRemoveNotifFail() throws IOException{
		when(mockNotifDao.removeNotification(0,0)).thenReturn(false);
		ResponseEntity<Void> response = notifController.removeNotification(0,0);
		assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
	}

	@Test
	public void testRemoveNotifInternalError() throws IOException{
		when(mockNotifDao.removeNotification(0,0)).thenThrow(new IOException());
		ResponseEntity<Void> response = notifController.removeNotification(0,0);
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
	}

	@Test
	public void testGetSubscriptions() throws IOException {
		Integer returnVal = 0;
		Integer[] returnArray = new Integer[1];
		returnArray[0] = returnVal;
		when(mockNotifDao.getSubscriptions(0)).thenReturn(returnArray);
		ResponseEntity<Integer[]> response = notifController.getSubscriptions(0);
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

	@Test
	public void testGetSubscriptionsInternalError() throws IOException{
		when(mockNotifDao.getSubscriptions(0)).thenThrow(new IOException());
		ResponseEntity<Integer[]> response = notifController.getSubscriptions(0);
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
	}
}
