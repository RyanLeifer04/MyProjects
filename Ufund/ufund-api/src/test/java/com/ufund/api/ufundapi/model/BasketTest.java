package com.ufund.api.ufundapi.model;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * The unit test suite for the Need class
 */

@Tag("Model-tier")
public class BasketTest {
	@Test
	public void testCreate() {
		// Setup
		Integer expected_user = 10;
		Integer[] expected_needs = { 1, 2, 3 };

		// Invoke
		Basket basket = new Basket(expected_user, expected_needs);

		// Analyze
		assertEquals(expected_user, basket.getUserID());
		assertArrayEquals(expected_needs, basket.getNeeds().toArray(new Integer[0]));
	}

	@Test
	public void testAddNeed() {
		// Setup
		Integer user = 10;
		Integer[] needs = { 1, 2, 3 };
		int need_to_add = 4;

		// Invoke
		Basket basket = new Basket(user, needs);
		basket.addNeed(need_to_add);

		// Analyze
		assertTrue(basket.getNeeds().contains(need_to_add));
	}

	@Test
	public void testRemoveNeed() {
		// Setup
		Integer user = 10;
		Integer[] needs = { 1, 2, 3 };
		int need_to_remove = 2;

		// Invoke
		Basket basket = new Basket(user, needs);
		basket.removeNeed(need_to_remove);

		// Analyze
		assertFalse(basket.getNeeds().contains(need_to_remove));
	}
}
