package com.ufund.api.ufundapi.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import com.ufund.api.ufundapi.persistence.CupboardDAO;
import com.ufund.api.ufundapi.model.Need;

import org.junit.jupiter.api.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Test the Need Controller class
 *
 * @author Blizzard Finnegan
 */

@Tag("Controller-tier")
public class CupboardControllerTest {
	private CupboardController needController;
	private CupboardDAO mockNeedDao;

	/**
	 * Before each test, create a new blank CupboardController object and inject
	 * a mock CupboardDAO
	 */
	@BeforeEach
	public void setupNeedController() {
		mockNeedDao = mock(CupboardDAO.class);
		needController = new CupboardController(mockNeedDao);
	}

	@Test
	public void testCreateNeed() throws IOException {
		// Setup
		Need need = new Need(99, "Blankets", 0, 5, "");
		when(mockNeedDao.createNeed(need)).thenReturn(need);

		// Invoke
		ResponseEntity<Need> response = needController.createNeed(need);

		// Analyse
		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		assertEquals(need, response.getBody());
	}

	@Test
	public void testCreateNeedFail() throws IOException {
		// Setup
		Need need = new Need(99, "Towels", 3, 10, "");
		when(mockNeedDao.createNeed(need)).thenReturn(null);

		// Invoke
		ResponseEntity<Need> response = needController.createNeed(need);

		// Analyse
		assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
	}

	@Test
	public void testCreateNeedHandleException() throws IOException {
		// Setup
		Need need = new Need(99, "Light bulbs", 9, 45, "");
		doThrow(new IOException()).when(mockNeedDao).createNeed(need);

		// Invoke
		ResponseEntity<Need> response = needController.createNeed(need);

		// Analyse
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
	}

	@Test
	public void testGetNeed() throws IOException {
		// Setup
		int need_id = 99;
		Need need = new Need(need_id, "Need", 2, 5, "Unit");
		when(mockNeedDao.getNeed(need_id)).thenReturn(need);

		// Invoke
		ResponseEntity<Need> response = needController.getNeed(need_id);

		// Analyse
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(need, response.getBody());
	}

	@Test
	public void testGetNeedFail() throws IOException {
		// Setup
		int need_id = 99;
		when(mockNeedDao.getNeed(need_id)).thenReturn(null);

		// Invoke
		ResponseEntity<Need> response = needController.getNeed(need_id);

		// Analyse
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
	}

	@Test
	public void testGetNeedHandleException() throws IOException {
		// Setup
		int need_id = 99;
		doThrow(new IOException()).when(mockNeedDao).getNeed(need_id);

		// Invoke
		ResponseEntity<Need> response = needController.getNeed(need_id);

		// Analyse
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
	}

	@Test
	public void testUpdateNeed() throws IOException {
		// Setup
		int need_id = 99;
		Need need = new Need(need_id, "Need", 2, 5, "Unit");
		when(mockNeedDao.updateNeed(need)).thenReturn(need);

		// Invoke
		ResponseEntity<Need> response = needController.updateNeed(need_id, need);

		// Analyse
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(need, response.getBody());
	}

	@Test
	public void testUpdateNeedCreated() throws IOException {
		// Setup
		int need_id = 99;
		Need need = new Need(need_id, "Need", 2, 5, "Unit");
		when(mockNeedDao.updateNeed(need)).thenReturn(null);
		when(mockNeedDao.createNeed(need)).thenReturn(need);

		// Invoke
		ResponseEntity<Need> response = needController.updateNeed(need_id, need);

		// Analyse
		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		assertEquals(need, response.getBody());
	}

	@Test
	public void testUpdateNeedHandleException() throws IOException {
		// Setup
		Need need = new Need(99, "Need", 2, 5, "Unit");
		doThrow(new IOException()).when(mockNeedDao).updateNeed(need);

		// Invoke
		ResponseEntity<Need> response = needController.updateNeed(99, need);

		// Analyse
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
	}

	/*
	 * Beginning of Unit tests created by Kenny Casey
	 */
	@Test
	public void testGetEmptyNeeds() throws IOException {
		// Setup
		Need[] expected = null;
		// Invoke
		ResponseEntity<Need[]> response = needController.getNeeds();
		// Analyse
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(expected, response.getBody());
	}

	@Test
	public void testGetFilledNeeds() throws IOException {
		// Setup
		Need[] needs = new Need[2];
		needs[0] = new Need(99, "Need", 2, 5, "Unit");
		needs[1] = new Need(100, "Need2", 2, 5, "Unit");
		when(mockNeedDao.getNeeds()).thenReturn(needs);
		// Invoke
		ResponseEntity<Need[]> response = needController.getNeeds();
		// Analyze
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(needs, response.getBody());
	}

	@Test
	public void testGetNeedsException() throws IOException {
		// Set up
		doThrow(new IOException()).when(mockNeedDao).getNeeds();
		// Invoke
		ResponseEntity<Need[]> response = needController.getNeeds();
		// Analyze
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
	}

	/*
	 * End of Unit tests created by Kenny Casey
	 */
	// RYAN LEFIER made searchNeeds tests
	@Test
	public void testSearchNeeds() throws IOException {
		// Setup
		String search = "Need";
		Need[] needs = {
				new Need(99, "Need", 2, 5, "Unit"),
				new Need(100, "Need", 2, 5, "Unit")
		};
		when(mockNeedDao.findNeedsByName(search)).thenReturn(needs);

		// Invoke
		ResponseEntity<Need[]> response = needController.searchNeeds(search);

		// Analyse
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(needs, response.getBody());
	}

	@Test
	public void testSearchNeedsEmpty() throws IOException {
		// Setup
		String search = "Need";
		Need[] needs = {};
		when(mockNeedDao.findNeedsByName(search)).thenReturn(needs);

		// Invoke
		ResponseEntity<Need[]> response = needController.searchNeeds(search);

		// Analyse
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(needs, response.getBody());
	}

	@Test
	public void testSearchNeedsHandleException() throws IOException {
		// Setup
		String search = "Need";
		doThrow(new IOException()).when(mockNeedDao).findNeedsByName(search);

		// Invoke
		ResponseEntity<Need[]> response = needController.searchNeeds(search);

		// Analyse
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
	}
	// END OF WORK FOR RYAN LEIFER

	// START OF WORK FOR NEAV ZIV
	public void testDeleteNeed() throws IOException {
		// Setup
		int needId = 30;
		// when deleteNeed is called return true, simulating successful deletion
		when(mockNeedDao.deleteNeed(needId)).thenReturn(true);

		// Invoke
		ResponseEntity<Need> response = needController.deleteNeed(needId);

		// Analyze
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

	@Test
	public void testDeleteNeedNotFound() throws IOException {
		// Setup
		int needId = 30;
		// when deleteNeed is called return false, simulating failed deletion
		when(mockNeedDao.deleteNeed(needId)).thenReturn(false);

		// Invoke
		ResponseEntity<Need> response = needController.deleteNeed(needId);

		// Analyze
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
	}

	@Test
	public void testDeleteNeedHandleException() throws IOException {
		// Setup
		int needId = 30;
		// When deleteNeed is called on the Mock Need Dao, throw an IOException
		doThrow(new IOException()).when(mockNeedDao).deleteNeed(needId);

		// Invoke
		ResponseEntity<Need> response = needController.deleteNeed(needId);

		// Analyze
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
	}

	// END OF WORK FOR NEAV ZIV
}
