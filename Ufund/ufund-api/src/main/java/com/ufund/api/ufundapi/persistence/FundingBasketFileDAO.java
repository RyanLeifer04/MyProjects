package com.ufund.api.ufundapi.persistence;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

import com.ufund.api.ufundapi.model.Need;
import com.ufund.api.ufundapi.model.Basket;

/**
 * Implements the functionality for JSON file-based persistence of Funding
 * Baskets
 *
 * {@literal @}Component Spring annotation instantiates a single instance of
 * this class and injects the instance into other classes as needed
 */
@Component
public class FundingBasketFileDAO implements FundingBasketDAO {
	private static final Logger LOG = Logger.getLogger(CupboardFileDAO.class.getName());
	private Map<Integer, Basket> baskets; // In-memory cache for basket objects
	private ObjectMapper objectMapper; // Conversion object between Need object(s) and JSON text
	private String filename; // Where to store the data on-disk

	/**
	 * Creates a basket File Data Access Object
	 *
	 * @param filename     File that will store the Need data
	 * @param objectMapper Provides JSON Object to/from Java Object serialization
	 *                     and deserialization
	 *
	 * @throws IOException when file cannot be accessed
	 */
	public FundingBasketFileDAO(@Value("${baskets.file}") String filename, ObjectMapper objectMapper)
			throws IOException {
		this.filename = filename;
		this.objectMapper = objectMapper;
		load();
	};

	/**
	 * Generates an array of all Needs IDs from the list
	 *
	 * @param userID The ID of the basket to get the needs from
	 *
	 * @return The array of {@link Need needs} IDs; may be empty
	 */
	private Integer[] getNeedArray(Integer userID) {
		synchronized (baskets) {
			Basket basket = baskets.get(userID);
			if (basket == null) {
				return new Integer[0];
			}

			return basket.getNeeds().toArray(new Integer[basket.getNeeds().size()]);
		}
	}

	/**
	 * Saves the {@link Need needs} IDs from the list into the file as an array of
	 * JSON
	 * objects
	 *
	 * @return true if the {@link Need needs} IDs were written successfully
	 *
	 * @throws IOException when file cannot be accessed
	 */
	private boolean save() throws IOException {
		Basket[] baskets = new Basket[this.baskets.size()];
		this.baskets.values().toArray(baskets);
		objectMapper.writeValue(new File(filename), baskets);
		return true;
	};

	/**
	 * Loads {@link Need needs} IDs from the JSON file into the list
	 * <br>
	 *
	 * @return true if the file was read successfully
	 *
	 * @throws IOException when file cannot be accessed
	 */
	private boolean load() throws IOException {
		baskets = new HashMap<>();
		// read the needs from the file
		Basket[] basketArray = objectMapper.readValue(createFile(filename), Basket[].class);
		for (Basket basket : basketArray) {
			baskets.put(basket.getUserID(), basket);
		}
		return true;
	};

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Integer[] getNeeds(Integer userID) throws IOException {
		synchronized (baskets) {
			return getNeedArray(userID);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean addNeed(Integer userID, int needID) throws IOException {
		synchronized (baskets) {
			Basket basket = baskets.get(userID);
			if (basket == null) {
				basket = new Basket(userID, new Integer[0]);
				baskets.put(userID, basket);
			}

			if (basket.addNeed(needID)) {
				return save(); // IOException possible
			} else {
				return false;
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean getNeed(Integer userID, int id) throws IOException {
		synchronized (baskets) {
			Basket basket = baskets.get(userID);
			if (basket == null) {
				return false;
			}

			return basket.getNeed(id);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean removeNeed(Integer userID, int id) throws IOException {
		synchronized (baskets) {
			Basket basket = baskets.get(userID);
			if (basket == null) {
				return false;
			}

			if (basket.removeNeed(id)) {
				return save(); // IOException possible
			} else {
				return false;
			}
		}
	}

	/**
	 * Creates the storage File object, creating the file if it does not exist.
	 *
	 * @param filename The file to create
	 *
	 * @return The file object
	 *
	 * @throws IOException when file cannot be accessed/created
	 */
	private File createFile(String filename) throws IOException {
		File file = new File(filename);
		if (!file.exists()) {
			try {
				if (file.getParentFile() != null) {
					file.getParentFile().mkdirs();
				}
				if (!file.createNewFile()) {
					LOG.severe("Could not create file " + filename);
					throw new IOException("Could not create file " + filename);
				}
				// write empty JSON array to file
				objectMapper.writeValue(file, new Need[0]);
				LOG.info(filename + " created");
			} catch (IOException e) {
				LOG.severe("Could not create file " + filename);
				throw e;
			}
		}
		return file;
	}
}
