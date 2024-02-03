package com.ufund.api.ufundapi.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;

public class NotificationTest {
	@Test
	public void testCreate(){
		//Setup
		String message = "Message";
		int id = 2;
		int needID = 5;
		//Invoke
		Notification notif = new Notification(id,needID,message);
		
		//Analyse
		assertEquals(id,notif.getId());
		assertEquals(needID,notif.getNeedID());
		assertEquals(message,notif.getMessage());
	}

	@Test
	public void testSetID(){
		//Setup
		String message = "Message";
		int oldID = 2;
		int needID = 5;
		int newID = 8;
		Notification notif = new Notification(oldID,needID,message);

		//Invoke
		notif.setID(newID);

		//Analyse
		assertEquals(newID,notif.getId());
	}

	@Test
	public void testClone(){
		//Setup
		String message = "Message";
		int oldID = 2;
		int needID = 5;
		int newID = 8;
		Notification oldNotif = new Notification(oldID,needID,message);

		//Invoke
		Notification newNotif = new Notification(newID,oldNotif);

		//Analyse
		assertEquals(newID,newNotif.getId());
		assertEquals(needID,newNotif.getNeedID());
		assertEquals(message,newNotif.getMessage());
	}
}
