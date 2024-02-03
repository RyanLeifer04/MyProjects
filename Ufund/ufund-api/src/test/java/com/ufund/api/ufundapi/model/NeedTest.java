package com.ufund.api.ufundapi.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * The unit test suite for the Need class
 *
 * @author Blizzard Finnegan
 */

@Tag("Model-tier")
public class NeedTest {
	@Test
	public void testCreate() {
		// Setup
		int expected_id = 99;
		String expected_name = "Blankets";
		double expected_in_stock = 0;
		double expected_needed = 5;
		String expected_units = "blankets";

		// Invoke
		Need need = new Need(expected_id, expected_name, expected_in_stock, expected_needed, expected_units);

		// Analyse
		assertEquals(expected_id, need.getId());
		assertEquals(expected_name, need.getName());
		assertEquals(expected_in_stock, need.checkInventory());
		assertEquals(expected_needed, need.getAmountNeeded());
		assertEquals(expected_units, need.getAmountUnit());
	}

	@Test
	public void testRename() {
		// Setup
		int id = 99;
		String initial_name = "Blankets";
		double in_stock = 0;
		double needed = 5;
		Need need = new Need(id, initial_name, in_stock, needed, "");

		String expected_name = "Towels";

		// Invoke
		need.setName(expected_name);

		// Analyse
		assertEquals(expected_name, need.getName());
	}

	@Test
	public void testAddToInventory() {
		// Setup
		int id = 99;
		String name = "Blankets";
		double in_stock = 0;
		double needed = 5;
		Need need = new Need(id, name, in_stock, needed, "");
		double amount_added = 3;

		// Invoke
		need.addToInventory(amount_added);

		// Analyse
		assertEquals(amount_added, need.checkInventory());
	}

	@Test
	public void testRemoveFromInventory() {
		// Setup
		int id = 99;
		String name = "Blankets";
		double in_stock = 3;
		double needed = 5;
		Need need = new Need(id, name, in_stock, needed, "");
		double amount_removed = 2;
		double amount_left = 1;

		// Invoke
		need.removeFromInventory(amount_removed);

		// Analyse
		assertEquals(amount_left, need.checkInventory());
	}

	@Test
	public void testModifyNeeded() {
		// Setup
		int id = 99;
		String name = "Blankets";
		double in_stock = 3;
		double initial_needed = 5;
		Need need = new Need(id, name, in_stock, initial_needed, "");
		double expected_needed = 27;

		// Invoke
		need.setAmountNeeded(expected_needed);

		// Analyse
		assertEquals(expected_needed, need.getAmountNeeded());
	}

	@Test
	public void testHasBeenMet() {
		// Setup
		int id = 99;
		String name = "Blankets";
		double in_stock = 6;
		double initial_needed = 5;
		Need need = new Need(id, name, in_stock, initial_needed, "");

		// Invoke

		// Analyse
		assertTrue(need.hasBeenMet());
	}

	@Test
	public void testHasNotBeenMet() {
		// Setup
		int id = 99;
		String name = "Blankets";
		double in_stock = 4;
		double initial_needed = 5;
		Need need = new Need(id, name, in_stock, initial_needed, "");

		// Invoke

		// Analyse
		assertFalse(need.hasBeenMet());
	}

	@Test
	public void testHasBeenMetAfterAdd() {
		// Setup
		int id = 99;
		String name = "Blankets";
		double in_stock = 4;
		double initial_needed = 5;
		Need need = new Need(id, name, in_stock, initial_needed, "");
		double add_to_stock = 2;
		need.addToInventory(add_to_stock);

		// Invoke

		// Analyse
		assertTrue(need.hasBeenMet());
	}

	@Test
	public void testHasNotBeenMetAfterAdd() {
		// Setup
		int id = 99;
		String name = "Blankets";
		double in_stock = 1;
		double initial_needed = 5;
		Need need = new Need(id, name, in_stock, initial_needed, "");
		double add_to_stock = 2;
		need.addToInventory(add_to_stock);

		// Invoke

		// Analyse
		assertFalse(need.hasBeenMet());
	}

	@Test
	public void testHasNotBeenMetAfterRemove() {
		// Setup
		int id = 99;
		String name = "Blankets";
		double in_stock = 6;
		double initial_needed = 5;
		Need need = new Need(id, name, in_stock, initial_needed, "");
		double remove_from_stock = 2;
		need.removeFromInventory(remove_from_stock);

		// Invoke

		// Analyse
		assertFalse(need.hasBeenMet());
	}

	@Test
	public void testHasBeenMetAfterRemove() {
		// Setup
		int id = 99;
		String name = "Blankets";
		double in_stock = 6;
		double initial_needed = 5;
		Need need = new Need(id, name, in_stock, initial_needed, "");
		double remove_from_stock = 1;
		need.removeFromInventory(remove_from_stock);

		// Invoke

		// Analyse
		assertTrue(need.hasBeenMet());
	}

	@Test
	public void testGetTagsEmpty() {
		// Setup
		int id = 99;
		String name = "Blankets";
		double in_stock = 6;
		double needed = 5;
		Need need = new Need(id, name, in_stock, needed, "");
		HashSet<String> expected_tags = new HashSet<>();

		// Invoke
		HashSet<String> actual_tags = need.getTags();

		// Analyse
		assertEquals(actual_tags, expected_tags);
	}

	@Test
	public void testSearchTagFail() {
		// Setup
		int id = 99;
		String name = "Blankets";
		double in_stock = 6;
		double needed = 5;
		Need need = new Need(id, name, in_stock, needed, "");

		// Invoke

		// Analyse
		assertFalse(need.containsTag("Warm"));
	}

	@Test
	public void testTagAdd() {
		// Setup
		int id = 99;
		String name = "Blankets";
		double in_stock = 6;
		double needed = 5;
		Need need = new Need(id, name, in_stock, needed, "");
		HashSet<String> expected_tags = new HashSet<>();

		String tag_to_add = "Fluffy";
		expected_tags.add(tag_to_add);

		need.addTag(tag_to_add);

		// Invoke
		HashSet<String> actual_tags = need.getTags();

		// Analyse
		assertEquals(actual_tags, expected_tags);
	}

	@Test
	public void testSearchTagPass() {
		// Setup
		int id = 99;
		String name = "Blankets";
		double in_stock = 6;
		double needed = 5;
		Need need = new Need(id, name, in_stock, needed, "");
		HashSet<String> expected_tags = new HashSet<>();

		String tag_to_add = "Fluffy";
		expected_tags.add(tag_to_add);

		need.addTag(tag_to_add);

		// Invoke

		// Analyse
		assertTrue(need.containsTag(tag_to_add));
	}

	@Test
	public void testTagSet() {
		// Setup
		int id = 99;
		String name = "Blankets";
		double in_stock = 6;
		double needed = 5;
		Need need = new Need(id, name, in_stock, needed, "");
		HashSet<String> expected_tags = new HashSet<>();

		String tag_to_add = "Fluffy";
		expected_tags.add(tag_to_add);

		need.setTags(expected_tags);

		// Invoke
		HashSet<String> actual_tags = need.getTags();

		// Analyse
		assertEquals(actual_tags, expected_tags);
	}

	@Test
	public void testUnitSet() {
		// Setup
		int id = 99;
		String name = "Blankets";
		double in_stock = 6;
		double needed = 5;
		String initial_unit = "units";
		Need need = new Need(id, name, in_stock, needed, initial_unit);

		String expected_unit = "blankets";

		need.setAmountUnit(expected_unit);

		// Analyse
		assertEquals(expected_unit, need.getAmountUnit());
	}

	@Test
	public void testToString() {
		// Setup
		int id = 99;
		String name = "Blankets";
		double in_stock = 6;
		double needed = 5;
		String unit = "pieces";
		Need need = new Need(id, name, in_stock, needed, unit);

		// Invoke
		String actual_string = need.toString();

		String expected_string = String.format(Need.STRING_FORMAT, id, name, in_stock, needed, unit,
				new HashSet<>().toString());

		// Analyse
		assertEquals(actual_string, expected_string);
	}

	@Test
	public void testClone() {
		// Setup
		int expected_id = 99;
		String expected_name = "Blankets";
		double expected_in_stock = 0;
		double expected_needed = 5;
		String expected_units = "blankets";
		Need oldNeed = new Need(42, expected_name, expected_in_stock, expected_needed, expected_units);

		// Invoke
		Need need = new Need(expected_id, oldNeed);

		// Analyse
		assertEquals(expected_id, need.getId());
		assertEquals(expected_name, need.getName());
		assertEquals(expected_in_stock, need.checkInventory());
		assertEquals(expected_needed, need.getAmountNeeded());
		assertEquals(expected_units, need.getAmountUnit());
	}

	@Test
	public void testRemoveTag() {
		// Setup
		int id = 99;
		String name = "Blankets";
		double in_stock = 6;
		double needed = 5;
		Need need = new Need(id, name, in_stock, needed, "kg");

		// Invoke
		String tag_to_add = "Fluffy";
		need.addTag(tag_to_add);
		need.removeTag(tag_to_add);

		// Analyse
		assertEquals(need.getTags().size(), 0);
	}

	@Test
	public void testClearTags() {
		// Setup
		int id = 99;
		String name = "Blankets";
		double in_stock = 6;
		double needed = 5;
		Need need = new Need(id, name, in_stock, needed, "kg");

		// Invoke
		need.addTag("tag 1");
		need.addTag("tag 2");
		need.clearTags();

		// Analyse
		assertEquals(need.getTags().size(), 0);
	}
}
