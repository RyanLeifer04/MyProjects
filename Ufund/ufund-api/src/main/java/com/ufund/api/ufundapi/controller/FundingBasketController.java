package com.ufund.api.ufundapi.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ufund.api.ufundapi.model.*;
import com.ufund.api.ufundapi.persistence.CupboardDAO;
import com.ufund.api.ufundapi.persistence.FundingBasketDAO;

/**
 * Handles the REST API requests for the Basket resource
 * <p>
 * {@literal @}RestController Spring annotation identifies this class as a REST
 * API method handler
 * to the Spring framework
 */

@RestController
@RequestMapping("basket")
public class FundingBasketController {
	private static final Logger LOG = Logger.getLogger(FundingBasketController.class.getName());
	private FundingBasketDAO basketDao;
	private CupboardDAO needDao;

	/**
	 * Creates a REST API controller to respond to requests
	 *
	 * @param basketDao The {@link FundingBasketDAO Basket Data Access Object} to
	 *                  perform CRUD operations
	 *                  <br>
	 *                  This dependency is injected by Spring Framework
	 * @param needDao   The {@link CupboardDAO Cupboard Data Access Object} to
	 *                  perform CRUD operations
	 *                  <br>
	 *                  This dependency is injected by Spring Framework
	 */
	public FundingBasketController(FundingBasketDAO basketDao, CupboardDAO needDao) {
		this.basketDao = basketDao;
		this.needDao = needDao;
	}

	/**
	 * Adds a {@link Need need} with the provided need ID
	 *
	 * @param userID - The ID of the user to add the {@link Need need} to
	 * @param id     - The {@link Need need} ID to add
	 *
	 * @return ResponseEntity with the added {@link Need need} object and HTTP
	 *         status of CREATED<br>
	 *         ResponseEntity with HTTP status of CONFLICT if {@link Need need}
	 *         ID already exists<br>
	 *         ResponseEntity with HttpStatus of INTERNAL_SERVER_ERROR otherwise
	 */
	@PostMapping("/{userID}")
	public ResponseEntity<Need> addNeed(@PathVariable Integer userID, @RequestBody Integer id) {
		LOG.info("POST /basket/" + userID + " " + id);
		refreshBasket(userID);

		try {
			if (basketDao.addNeed(userID, id)) {
				Need need = needDao.getNeed(id);
				return new ResponseEntity<Need>(need, HttpStatus.CREATED);
			} else
				return new ResponseEntity<>(HttpStatus.CONFLICT);
		} catch (IOException e) {
			LOG.log(Level.SEVERE, e.getLocalizedMessage());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * Get the {@link Need need} from the basket by its ID
	 *
	 * @param userID - The ID of the user to get the {@link Need need} for
	 * @param id     - The {@link Need need} ID to get
	 *
	 * @return ResponseEntity with the {@link Need need} object and HTTP status of
	 *         OK<br>
	 *         ResponseEntity with HTTP status of NOT_FOUND if {@link Need need} ID
	 *         does not exist<br>
	 *         ResponseEntity with HttpStatus of INTERNAL_SERVER_ERROR otherwise
	 */
	@GetMapping("/{userID}/{id}")
	public ResponseEntity<Need> getNeed(@PathVariable Integer userID, @PathVariable int id) {
		LOG.info("GET /basket/" + userID + "/" + id);
		refreshBasket(userID);

		try {
			if (basketDao.getNeed(userID, id)) {
				Need need = needDao.getNeed(id);
				return new ResponseEntity<Need>(need, HttpStatus.OK);
			} else {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
		} catch (IOException e) {
			LOG.log(Level.SEVERE, e.getLocalizedMessage());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * Get the {@link Need[] needs} IDs from the basket
	 *
	 * @param userID - The ID of the user to get the {@link Need needs} for
	 *
	 * @return ResponseEntity with {@link Need[] needs} objects and HTTP
	 *         status of OK
	 *         ResponseEntity with HttpStatus of INTERNAL_SERVER_ERROR otherwise
	 */
	@GetMapping("{userID}")
	public ResponseEntity<Need[]> getBasket(@PathVariable Integer userID) {
		LOG.info("GET /basket/" + userID);
		refreshBasket(userID);

		try {
			Integer[] basket = basketDao.getNeeds(userID);
			ArrayList<Need> needs = new ArrayList<Need>();
			for (int i = 0; i < basket.length; i++) {
				needs.add(needDao.getNeed(basket[i]));
			}

			return new ResponseEntity<Need[]>(needs.toArray(new Need[needs.size()]), HttpStatus.OK);
		} catch (IOException e) {
			LOG.log(Level.SEVERE, e.getLocalizedMessage());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * Deletes a {@link Need need} with the given id
	 *
	 * @param userID The ID of the user to remove the {@link Need need} from
	 * @param id     The id of the {@link Need need} to be deleted
	 *
	 * @return ResponseEntity HTTP status of OK if deleted<br>
	 *         ResponseEntity with HTTP status of NOT_FOUND if not found<br>
	 *         ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
	 */
	@DeleteMapping("/{userID}/{id}")
	public ResponseEntity<Need> removeNeed(@PathVariable Integer userID, @PathVariable int id) {
		LOG.info("DELETE /basket/" + userID + "/" + id);
		refreshBasket(userID);

		try {
			if (basketDao.removeNeed(userID, id)) {
				Need need = needDao.getNeed(id);
				return new ResponseEntity<>(need, HttpStatus.OK);
			} else {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
		} catch (IOException e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * Checks out the provided basket. Removes the needs from the basket and updates
	 * the need inventory.
	 *
	 * @param userID The ID of the user to checkout
	 * @param basket The basket to checkout, a dictionary of need IDs and the
	 *               amount to checkout
	 *
	 * @return ResponseEntity with total amount funded and HTTP status of OK if
	 *         successful<br>
	 *         ResponseEntity with {@link Need need} ID and HTTP status of NOT_FOUND
	 *         if ID does not exist<br>
	 */
	@PostMapping("/{userID}/checkout")
	public ResponseEntity<Float> checkout(@PathVariable Integer userID,
			@RequestBody HashMap<Integer, Float> basket) {
		LOG.info("POST /basket/" + userID + "/checkout " + basket);
		refreshBasket(userID);

		try {
			float total = 0;
			for (Integer id : basket.keySet()) {
				float amount = basket.get(id);
				total += amount;

				// remove the need from the basket
				if (!basketDao.removeNeed(userID, id)) {
					amount = id;
					return new ResponseEntity<Float>(amount, HttpStatus.NOT_FOUND);
				}

				// retrieve the need
				Need need = needDao.getNeed(id);
				if (need == null) {
					amount = id;
					return new ResponseEntity<Float>(amount, HttpStatus.NOT_FOUND);
				}

				double amount_needed = need.getAmountNeeded() - need.checkInventory();
				if (amount > amount_needed) {
					amount = (float) amount_needed;
				}

				// update the need inventory
				need.addToInventory(amount);
				needDao.updateNeed(need);
			}
			return new ResponseEntity<Float>(total, HttpStatus.OK);
		} catch (IOException e) {
			LOG.log(Level.SEVERE, e.getLocalizedMessage());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	private void refreshBasket(Integer userID) {
		try {
			Integer[] basket = basketDao.getNeeds(userID);
			for (int i = 0; i < basket.length; i++) {
				Need need = needDao.getNeed(basket[i]);
				if (need == null) {
					basketDao.removeNeed(userID, basket[i]);
				}
			}
		} catch (IOException e) {
			LOG.log(Level.SEVERE, e.getLocalizedMessage());
		}
	}
}
