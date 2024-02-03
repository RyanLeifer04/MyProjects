package com.ufund.api.ufundapi.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ufund.api.ufundapi.model.*;
import com.ufund.api.ufundapi.persistence.CupboardDAO;

/**
 * Handles the REST API requests for the Need resource
 * <p>
 * {@literal @}RestController Spring annotation identifies this class as a REST
 * API method handler
 * to the Spring framework
 *
 * @author Blizzard Finnegan
 */

@RestController
@RequestMapping("cupboard/need")
public class CupboardController {
	private static final Logger LOG = Logger.getLogger(CupboardController.class.getName());
	private CupboardDAO needDao;

	/**
	 * Creates a REST API controller to respond to requests
	 *
	 * @param needDao The {@link CupboardDAO Cupboard Data Access Object} to perform
	 *                CRUD operations
	 *                <br>
	 *                This dependency is injected by Spring Framework
	 */
	public CupboardController(CupboardDAO needDao) {
		this.needDao = needDao;
	}

	/**
	 * Creates a {@linkplain Need need} with the provided need object
	 *
	 * @param need - The {@link Need need} to create
	 *
	 * @return ResponseEntity with created {@link Need need} object and HTTP status
	 *         of CREATED<br>
	 *         ResponseEntity with HTTP status of CONFLICT if {@link Need need}
	 *         object already exists<br>
	 *         ResponseEntity with HttpStatus of INTERNAL_SERVER_ERROR otherwise
	 */
	@PostMapping("")
	public ResponseEntity<Need> createNeed(@RequestBody Need need) {
		LOG.info("POST /cupboard/need " + need);
		try {
			Need returnedNeed = needDao.createNeed(need);
			if (returnedNeed != null)
				return new ResponseEntity<Need>(returnedNeed, HttpStatus.CREATED);
			else
				return new ResponseEntity<>(HttpStatus.CONFLICT);
		} catch (IOException e) {
			LOG.log(Level.SEVERE, e.getLocalizedMessage());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * Get a {@linkplain Need need} with the provided need ID
	 *
	 * @param id - The {@link Need need} ID to retrieve
	 *
	 * @return ResponseEntity with retrieved {@link Need need} object and HTTP
	 *         status of OK<br>
	 *         ResponseEntity with HTTP status of NOT_FOUND if {@link Need need}
	 *         object not found<br>
	 *         ResponseEntity with HttpStatus of INTERNAL_SERVER_ERROR otherwise
	 */
	@GetMapping("/{id}")
	public ResponseEntity<Need> getNeed(@PathVariable int id) {
		LOG.info("GET /cupboard/need/" + id);
		try {
			Need returnedNeed = needDao.getNeed(id);
			if (returnedNeed != null)
				return new ResponseEntity<Need>(returnedNeed, HttpStatus.OK);
			else
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} catch (IOException e) {
			LOG.log(Level.SEVERE, e.getLocalizedMessage());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * Update a {@linkplain Need need} with the provided need ID
	 *
	 * @param need - The {@link Need need} with updated information
	 *
	 * @return ResponseEntity with updated {@link Need need} object and HTTP
	 *         status of OK<br>
	 *         ResponseEntity with new {@link Need need} object and HTTP status
	 *         of CREATED if {@link Need need} object not found<br>
	 *         ResponseEntity with HTTP status of CONFLICT if {@link Need need}
	 *         object already exists<br>
	 *         ResponseEntity with HttpStatus of INTERNAL_SERVER_ERROR otherwise
	 */
	@PutMapping("/{id}")
	public ResponseEntity<Need> updateNeed(@PathVariable int id, @RequestBody Need need) {
		LOG.info("PUT /cupboard/need/" + id + " " + need);
		try {
			Need returnedNeed = needDao.updateNeed(need);
			if (returnedNeed != null)
				return new ResponseEntity<Need>(returnedNeed, HttpStatus.OK);
			else {
				returnedNeed = needDao.createNeed(need);
				if (returnedNeed != null)
					return new ResponseEntity<Need>(returnedNeed, HttpStatus.CREATED);
				else // should never happen
					return new ResponseEntity<>(HttpStatus.CONFLICT);
			}
		} catch (IOException e) {
			LOG.log(Level.SEVERE, e.getLocalizedMessage());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * Get the {@linkplain Need[] need} from the cupboard
	 *
	 * @return ResponseEntity with {@linkplain Need[] need} object and HTTP
	 *         status of OK
	 *         ResponseEntity with HttpStatus of INTERNAL_SERVER_ERROR otherwise
	 */
	@GetMapping("")
	public ResponseEntity<Need[]> getNeeds() {
		LOG.info("GET /cupboard/need");
		try {
			Need[] cupboard = needDao.getNeeds();
			return new ResponseEntity<Need[]>(cupboard, HttpStatus.OK);
		} catch (IOException e) {
			LOG.log(Level.SEVERE, e.getLocalizedMessage());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * Get the {@linkplain Need[] need} from the cupboard that are unfulfilled
	 *
	 * @return ResponseEntity with {@linkplain Need[] need} object and HTTP
	 *         status of OK
	 *         ResponseEntity with HttpStatus of INTERNAL_SERVER_ERROR otherwise
	 */
	@GetMapping("/unfulfilled")
	public ResponseEntity<Need[]> getUnfulfilledNeeds() {
		LOG.info("GET /cupboard/need/unfulfilled");
		try {
			Need[] cupboard = needDao.getUnfulfilledNeeds();
			return new ResponseEntity<Need[]>(cupboard, HttpStatus.OK);
		} catch (IOException e) {
			LOG.log(Level.SEVERE, e.getLocalizedMessage());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * Responds to the GET request for all {@linkplain Need needs} whose name
	 * contains the provided text
	 *
	 * @param name The name parameter which contains the text used to find the
	 *             {@link Need needs}
	 *
	 * @return ResponseEntity with array of {@link Need need} objects (may be empty)
	 *         and HTTP status of OK<br>
	 *         ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
	 */
	@GetMapping("/unfulfilled/search")
	public ResponseEntity<Need[]> searchNeeds(@RequestParam String name) {
		LOG.info("GET /cupboard/need/unfulfilled/search?name=" + name);
		try {
			Need[] needs = needDao.findNeedsByName(name);
			return new ResponseEntity<Need[]>(needs, HttpStatus.OK);
		} catch (IOException e) {
			LOG.log(Level.SEVERE, e.getLocalizedMessage());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * Deletes a {@linkplain Need need} with the given id
	 *
	 * @param id The id of the {@link Need need} to be deleted
	 *
	 * @return ResponseEntity HTTP status of OK if deleted<br>
	 *         ResponseEntity with HTTP status of NOT_FOUND if not found<br>
	 *         ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
	 */
	@DeleteMapping("/{id}")
	public ResponseEntity<Need> deleteNeed(@PathVariable int id) {
		LOG.info("DELETE /cupboard/need/" + id);

		try {
			if (needDao.deleteNeed(id)) {
				return new ResponseEntity<>(HttpStatus.OK);
			} else {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
		} catch (IOException e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
