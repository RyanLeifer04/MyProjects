package com.ufund.api.ufundapi.persistence;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import com.ufund.api.ufundapi.model.*;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.*;

@Tag("Persistence-tier")
public class FundingBasketFileDAOTest {
	private FundingBasketFileDAO basketFileDAO;
	private ObjectMapper mockObjectMapper;
	private Basket[] testBaskets;
	private Integer[] zeroNeeds = new Integer[1];
	private Integer[] oneNeeds = new Integer[1];
	private Integer[] twoNeeds = new Integer[1];

	@BeforeEach
	public void initialise() throws IOException {
		mockObjectMapper = mock(ObjectMapper.class);
		testBaskets = new Basket[3];
		zeroNeeds[0] = 1;
		testBaskets[0] = new Basket(1, zeroNeeds);
		oneNeeds[0] = 5;
		testBaskets[1] = new Basket(2, oneNeeds);
		twoNeeds[0] = 3;
		testBaskets[2] = new Basket(9, twoNeeds);

		when(mockObjectMapper.readValue(new File("doesnt_matter.txt"), Basket[].class)).thenReturn(testBaskets);
		basketFileDAO = new FundingBasketFileDAO("doesnt_matter.txt", mockObjectMapper);
	}

	@Test
	public void testGetNeeds() throws IOException {
		Integer[] returnValues = basketFileDAO.getNeeds(1);
		assertEquals(zeroNeeds[0].intValue(), returnValues[0].intValue());
	}

	@Test
	public void testAddNeed() throws IOException {
		assertTrue(basketFileDAO.addNeed(1, 5));
	}

	@Test
	public void testAddNeedCreateNew() throws IOException {
		assertTrue(basketFileDAO.addNeed(999, 5));
	}

	@Test
	public void testAddNeedFail() throws IOException {
		assertFalse(basketFileDAO.addNeed(1, 1));
	}

	@Test
	public void testGetNeed() throws IOException {
		assertTrue(basketFileDAO.getNeed(1, 1));
	}

	@Test
	public void testGetNeedFail() throws IOException {
		assertFalse(basketFileDAO.getNeed(999, 1));
	}

	@Test
	public void testRemoveNeed() throws IOException {
		assertTrue(basketFileDAO.removeNeed(1, 1));
	}

	@Test
	public void testRemoveNeedFail() throws IOException {
		assertFalse(basketFileDAO.removeNeed(999, 1));
	}

	@Test
	public void testRemoveNeedSecondFail() throws IOException {
		assertFalse(basketFileDAO.removeNeed(1, 999));
	}
}
