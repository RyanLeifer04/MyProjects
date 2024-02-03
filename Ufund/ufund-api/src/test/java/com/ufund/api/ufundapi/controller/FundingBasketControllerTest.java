package com.ufund.api.ufundapi.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.HashMap;

import com.ufund.api.ufundapi.persistence.CupboardDAO;
import com.ufund.api.ufundapi.persistence.FundingBasketDAO;
import com.ufund.api.ufundapi.model.Need;

import org.junit.jupiter.api.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Test the Basket Controller class
 */

@Tag("Controller-tier")
public class FundingBasketControllerTest {
    private FundingBasketController basketController;
    private CupboardDAO mockNeedDao;
    private FundingBasketDAO mockBasketDao;
    private Need[] needs;

    @BeforeEach
    public void setupNeedController() {
        mockNeedDao = mock(CupboardDAO.class);
        mockBasketDao = mock(FundingBasketDAO.class);
        basketController = new FundingBasketController(mockBasketDao, mockNeedDao);

        needs = new Need[3];
        needs[0] = new Need(1, "Need", 2, 5, "Unit");
        needs[1] = new Need(2, "Need2", 2, 5, "Unit");
        needs[2] = new Need(3, "Need3", 2, 5, "Unit");

        try {
            when(mockNeedDao.getNeeds()).thenReturn(needs);
            when(mockNeedDao.getNeed(1)).thenReturn(needs[0]);
            when(mockNeedDao.getNeed(2)).thenReturn(needs[1]);
            when(mockNeedDao.getNeed(3)).thenReturn(needs[2]);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testAddNeed() throws IOException {
        // Setup
        when(mockBasketDao.addNeed(10, 1)).thenReturn(true);
        when(mockBasketDao.getNeeds(10)).thenReturn(new Integer[] { 1 });
        // Invoke
        ResponseEntity<Need> response = basketController.addNeed(10, 1);
        // Analyze
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(needs[0], response.getBody());
    }

    @Test
    public void testAddNeedFail() throws IOException {
        // Setup
        when(mockBasketDao.addNeed(10, 1)).thenReturn(false);
        when(mockBasketDao.getNeeds(10)).thenReturn(new Integer[] { 1 });
        // Invoke
        ResponseEntity<Need> response = basketController.addNeed(10, 1);
        // Analyze
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    public void testAddNeedHandleException() throws IOException {
        // Setup
        when(mockBasketDao.addNeed(10, 1)).thenThrow(new IOException());
        when(mockBasketDao.getNeeds(10)).thenReturn(new Integer[] { 1 });
        // Invoke
        ResponseEntity<Need> response = basketController.addNeed(10, 1);
        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testRemoveNeed() throws IOException {
        // Setup
        when(mockBasketDao.removeNeed(10, 1)).thenReturn(true);
        when(mockBasketDao.getNeeds(10)).thenReturn(new Integer[] { 1 });
        // Invoke
        ResponseEntity<Need> response = basketController.removeNeed(10, 1);
        // Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testRemoveNeedFail() throws IOException {
        // Setup
        when(mockBasketDao.removeNeed(10, 1)).thenReturn(false);
        when(mockBasketDao.getNeeds(10)).thenReturn(new Integer[] { 1 });
        // Invoke
        ResponseEntity<Need> response = basketController.removeNeed(10, 1);
        // Analyze
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testRemoveNeedHandleException() throws IOException {
        // Setup
        when(mockBasketDao.removeNeed(10, 1)).thenThrow(new IOException());
        when(mockBasketDao.getNeeds(10)).thenReturn(new Integer[] { 1 });
        // Invoke
        ResponseEntity<Need> response = basketController.removeNeed(10, 1);
        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testGetEmptyBasket() throws IOException {
        // Setup
        when(mockBasketDao.getNeeds(10)).thenReturn(new Integer[0]);
        // Invoke
        ResponseEntity<Need[]> response = basketController.getBasket(10);
        // Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertArrayEquals(new Need[0], response.getBody());
    }

    @Test
    public void testGetFilledBasket() throws IOException {
        // Setup
        when(mockBasketDao.getNeeds(10)).thenReturn(new Integer[] { 1, 2, 3 });
        // Invoke
        ResponseEntity<Need[]> response = basketController.getBasket(10);
        // Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertArrayEquals(needs, response.getBody());
    }

    @Test
    public void testGetBasketException() throws IOException {
        // Set up
        doThrow(new IOException()).when(mockBasketDao).getNeeds(10);
        // Invoke
        ResponseEntity<Need[]> response = basketController.getBasket(10);
        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testCheckoutBasket() throws IOException {
        // Setup
        when(mockBasketDao.removeNeed(10, 1)).thenReturn(true);
        when(mockBasketDao.getNeeds(10)).thenReturn(new Integer[] { });
        when(mockNeedDao.getNeed(1)).thenReturn(needs[0]);
        when(mockNeedDao.updateNeed(needs[0])).thenReturn(needs[0]);
        // Invoke
        ResponseEntity<Float> response = basketController.checkout(10, new HashMap<>() {
            {
                put(1, 1.0f);
            }
        });
        // Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1.0f, response.getBody());
    }

    @Test
    public void testCheckoutNotInBasket() throws IOException {
        // Setup
        when(mockBasketDao.removeNeed(10, 1)).thenReturn(false);
        when(mockBasketDao.getNeeds(10)).thenReturn(new Integer[] { });
        // Invoke
        ResponseEntity<Float> response = basketController.checkout(10, new HashMap<>()
        {
            {
                put(1, 1.0f);
            }
        });
        // Analyze
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(1, response.getBody());
    }

    @Test
    public void testCheckoutNonExistentNeed() throws IOException {
        // Setup
        when(mockBasketDao.removeNeed(10, 1)).thenReturn(true);
        when(mockBasketDao.getNeeds(10)).thenReturn(new Integer[] { });
        when(mockNeedDao.getNeed(1)).thenReturn(null);
        // Invoke
        ResponseEntity<Float> response = basketController.checkout(10, new HashMap<>()
        {
            {
                put(1, 1.0f);
            }
        });
        // Analyze
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(1, response.getBody());
    }

    @Test
    public void testCheckoutException() throws IOException {
        // Setup
        when(mockBasketDao.removeNeed(10, 1)).thenThrow(new IOException());
        when(mockBasketDao.getNeeds(10)).thenReturn(new Integer[] { });
        // Invoke
        ResponseEntity<Float> response = basketController.checkout(10, new HashMap<>()
        {
            {
                put(1, 1.0f);
            }
        });
        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
}
