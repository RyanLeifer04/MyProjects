package com.ufund.api.ufundapi.persistence;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import com.ufund.api.ufundapi.model.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ufund.api.ufundapi.model.User;

import org.junit.jupiter.api.*;

@Tag("Persistence-tier")
public class CupboardFileDAOTest {
	private CupboardFileDAO cupboardFileDAO;
	private Need[] testNeeds;
	private ObjectMapper mockObjectMapper;

	@BeforeEach
	public void initialise() throws IOException {
		mockObjectMapper = mock(ObjectMapper.class);
		testNeeds = new Need[3];

		testNeeds[0] = new Need(0, "Pencil", 0, 50, "units");
		testNeeds[1] = new Need(2, "Binder", 0, 3, "units");
		testNeeds[2] = new Need(8, "coloured pencils", 49, 25, "boxes");

		when(mockObjectMapper.readValue(new File("doesnt_matter.txt"), Need[].class)).thenReturn(testNeeds);
		cupboardFileDAO = new CupboardFileDAO("doesnt_matter.txt", mockObjectMapper);
	}

	@Test
	public void testGetNeeds() throws IOException {
		Need[] returnVal = cupboardFileDAO.getNeeds();
		for (int i = 0; i < testNeeds.length; i++) {
			assertEquals(returnVal[i], testNeeds[i]);
		}
	}

	@Test
	public void testGetUnfullfilledNeeds() throws IOException {
		Need[] returnArray = cupboardFileDAO.getUnfulfilledNeeds();
		ArrayList<Need> returnArrayList = new ArrayList<>(Arrays.asList(returnArray));
		assertFalse(returnArrayList.contains(testNeeds[2]));
	}

	@Test
	public void testFindNeedsByName() throws IOException {
		Need[] returnArray = cupboardFileDAO.findNeedsByName("cil");
		ArrayList<Need> returnArrayList = new ArrayList<>(Arrays.asList(returnArray));
		assertTrue(returnArrayList.contains(testNeeds[0]));
		assertFalse(returnArrayList.contains(testNeeds[1]));
	}

	@Test
	public void testFindNeedsByTag() throws IOException {
		testNeeds[0].addTag("tag");
		cupboardFileDAO.updateNeed(testNeeds[0]);
		Need[] returnArray = cupboardFileDAO.findNeedsByTag("tag");
		ArrayList<Need> returnArrayList = new ArrayList<>(Arrays.asList(returnArray));
		assertTrue(returnArrayList.contains(testNeeds[0]));
	}

	@Test
	public void testFindRequiredNeeds() throws IOException {
		Need[] returnArray = cupboardFileDAO.getUnfulfilledNeeds();
		ArrayList<Need> returnArrayList = new ArrayList<>(Arrays.asList(returnArray));
		assertFalse(returnArrayList.contains(testNeeds[2]));
	}

	@Test
	public void testFindNeedsByTagNoTag() throws IOException {
		testNeeds[0].addTag("tag");
		cupboardFileDAO.updateNeed(testNeeds[0]);
		Need[] returnArray = cupboardFileDAO.findNeedsByTag(null);
		ArrayList<Need> returnArrayList = new ArrayList<>(Arrays.asList(returnArray));
		assertTrue(returnArrayList.contains(testNeeds[0]));
	}

	@Test
	public void testGetNeed() throws IOException {
		Need returnedVal = cupboardFileDAO.getNeed(0);
		assertEquals(returnedVal, testNeeds[0]);
	}

	@Test
	public void testGetNeedNoNeed() throws IOException {
		Need returnedVal = cupboardFileDAO.getNeed(100);
		assertEquals(returnedVal, null);
	}

	@Test
	public void testCreateNeed() throws IOException {
		Need newNeed = new Need(9, "Computers", 0, 1, "beep-boop");
		cupboardFileDAO.createNeed(newNeed);

		Need[] returnArray = cupboardFileDAO.findNeedsByName("puter");
		assertEquals(newNeed, returnArray[0]);
	}

	@Test
	public void testCreateNeedSameName() throws IOException {
		Need newNeed = new Need(9, "Pencil", 0, 1, "beep-boop");
		cupboardFileDAO.createNeed(newNeed);

		Need[] returnArray = cupboardFileDAO.findNeedsByName("Pencil");
		assertEquals(testNeeds[0], returnArray[0]);
	}

	@Test
	public void testDeleteNeed() throws IOException {
		cupboardFileDAO.deleteNeed(0);
		Need[] returnArray = cupboardFileDAO.getNeeds();
		ArrayList<Need> returnArrayList = new ArrayList<>(Arrays.asList(returnArray));
		assertFalse(returnArrayList.contains(testNeeds[0]));
		assertTrue(returnArrayList.contains(testNeeds[1]));
	}

	@Test
	public void testDeleteNeedNoNeed() throws IOException {
		cupboardFileDAO.deleteNeed(100);
		Need[] returnArray = cupboardFileDAO.getNeeds();
		ArrayList<Need> returnArrayList = new ArrayList<>(Arrays.asList(returnArray));
		assertTrue(returnArrayList.contains(testNeeds[0]));
		assertTrue(returnArrayList.contains(testNeeds[1]));
	}

	@Test
	public void testUpdateNeedNoNeed() throws IOException {
		Need newNeed = new Need(100, "Computers", 0, 1, "beep-boop");
		cupboardFileDAO.updateNeed(newNeed);

		Need[] returnArray = cupboardFileDAO.findNeedsByName("puter");
		assertEquals(0, returnArray.length);
	}
}
