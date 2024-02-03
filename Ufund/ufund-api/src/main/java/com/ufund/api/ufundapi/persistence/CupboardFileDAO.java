package com.ufund.api.ufundapi.persistence;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

import com.ufund.api.ufundapi.model.Need;

/**
 * Implements the functionality for JSON file-based persistence of Needs
 *
 * {@literal @}Component Spring annotation instantiates a single instance of
 * this class and injects the instance into other classes as needed
 *
 * @author Blizzard Finnegan
 */
@Component
public class CupboardFileDAO implements CupboardDAO {
	private static final Logger LOG = Logger.getLogger(CupboardFileDAO.class.getName());
	Map<Integer, Need> needs; // In-memory cache for hero objects
	// Backed by a HashMap as order doesn't really matter,
	// and hashmaps have faster response times overall
	private ObjectMapper objectMapper; // conversion object between Need object(s) and JSON text
	private static int nextID; // Next ID to assign to a new Need
	private String filename; // Where to store the data on-disk

	/**
	 * Creates a Need File Data Access Object
	 *
	 * @param filename     File that will store the Need data
	 * @param objectMapper Provides JSON Object to/from Java Object serialisation
	 *                     and deserialisation
	 *
	 * @throws IOException when file cannot be accessed
	 */
	public CupboardFileDAO(@Value("${needs.file}") String filename, ObjectMapper objectMapper) throws IOException {
		this.filename = filename;
		this.objectMapper = objectMapper;
		load();
	};

	/**
	 * Generates the next ID for a new {@link Need}
	 *
	 * @return the next ID
	 */
	private synchronized static int nextID() {
		int id = nextID;
		++nextID;
		return id;
	}

	/**
	 * Generates an list of all {@link Need needs} from the Map
	 *
	 * @return The array of {@link Need needs}; may be empty
	 */
	private ArrayList<Need> getAllNeeds() {
		return new ArrayList<>(needs.values());
	}

	/**
	 * Generates an array of {@link Need needs} from the hashmap
	 *
	 * @return The array of {@link Need needs}; may be empty
	 */
	private Need[] getNeedArray(ArrayList<Need> needs) {
		Need[] needArray = new Need[needs.size()];
		needs.toArray(needArray);
		return needArray;
	};

	/**
	 * Generates an array of {@link Need needs} from the hashmap for any {@link Need
	 * needs}
	 * that contains the text specified by containsText as a name
	 * <br>
	 * If the containsText is null, then the array contains all {@link Need needs}
	 * in the Map
	 *
	 * @return The list of {@link Need needs}; may be empty
	 */
	private ArrayList<Need> getNeedArrayByName(ArrayList<Need> needs, String containsText) {
		needs.removeIf(need -> containsText != null && !need.getName().contains(containsText));
		return needs;
	};

	/**
	 * Generates an array of {@link Need needs} from the hashmap for any {@link Need
	 * needs}
	 * that contains the text specified by containsText as a tag
	 * <br>
	 * If the containsText is null, then the array contains all {@link Need needs}
	 * in the Map
	 *
	 * @return The list of {@link Need needs}; may be empty
	 */
	private ArrayList<Need> getNeedArrayByTag(ArrayList<Need> needs, String tag) {
		needs.removeIf(need -> tag != null && !need.containsTag(tag));
		return needs;
	};

	/**
	 * Generates an array of {@link Need needs} from the hashmap for any {@link Need
	 * needs}
	 * that has not been met yet
	 *
	 * @return The list of {@link Need needs}; may be empty
	 */
	private ArrayList<Need> getNeedArrayByRequired(ArrayList<Need> needs) {
		needs.removeIf(need -> need.hasBeenMet());
		return needs;
	}

	/**
	 * Saves the {@link Need needs} from the map into the file as an array of JSON
	 * objects
	 *
	 * @return true if the {@link Need needs} were written successfully
	 *
	 * @throws IOException when file cannot be accessed
	 */
	private boolean save() throws IOException {
		Need[] needArray = getNeedArray(getAllNeeds());
		objectMapper.writeValue(new File(filename), needArray);
		return true;
	};

	/**
	 * Loads {@link Need needs} from the JSON file into the map
	 * <br>
	 * Also sets the next ID to be one more than the greatest ID found in the file
	 *
	 * @return true if the file was read successfully
	 *
	 * @throws IOException when file cannot be accessed
	 */
	private boolean load() throws IOException {
		this.needs = new HashMap<>();
		nextID = 0;

		Need[] needArray = objectMapper.readValue(createFile(filename), Need[].class);
		for (Need need : needArray) {
			this.needs.put(need.getId(), need);
			if (need.getId() > nextID) {
				nextID = need.getId();
			}
		}

		++nextID;
		return true;
	};

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Need[] getNeeds() throws IOException {
		synchronized (needs) {
			return getNeedArray(getAllNeeds());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Need[] getUnfulfilledNeeds() throws IOException {
		synchronized (needs) {
			return getNeedArray(getNeedArrayByRequired(getAllNeeds()));
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Need[] findNeedsByName(String containsText) throws IOException {
		synchronized (needs) {
			return getNeedArray(
					getNeedArrayByName(
							getNeedArrayByRequired(getAllNeeds()),
							containsText));
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Need[] findNeedsByTag(String containsTag) throws IOException {
		synchronized (needs) {
			return getNeedArray(
					getNeedArrayByTag(
							getNeedArrayByRequired(getAllNeeds()),
							containsTag));
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Need getNeed(int id) throws IOException {
		synchronized (needs) {
			if (needs.containsKey(id))
				return needs.get(id);
			else
				return null;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Need createNeed(Need need) throws IOException {
		synchronized (needs) {
			// return null if a need already exists with the same name
			for (Need existingNeed : needs.values()) {
				if (existingNeed.getName().equals(need.getName()))
					return null;
			}

			// New Need with the proper ID, as that field is immutable
			Need newNeed = new Need(nextID(), need);
			needs.put(newNeed.getId(), newNeed);
			save(); // IOException possible
			return newNeed;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Need updateNeed(Need need) throws IOException {
		synchronized (needs) {
			if (!needs.containsKey(need.getId()))
				return null; // need does not yet exist
			needs.put(need.getId(), need);
			save();// IOException possible
			return need;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean deleteNeed(int id) throws IOException {
		synchronized (needs) {
			if (needs.containsKey(id)) {
				needs.remove(id);
				return save(); // IOException possible
			} else
				return false;
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
