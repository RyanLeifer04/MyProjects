package com.ufund.api.ufundapi.persistence;

import java.io.IOException;
import com.ufund.api.ufundapi.model.Need;

public interface CupboardDAO {

	/**
	 * Retrieves all {@linkplain Need needs}
	 *
	 * @return An array of {@link Need need} objects, may be empty
	 *
	 * @throws IOException if an issue with underlying storage
	 */
	Need[] getNeeds() throws IOException;

	/**
	 * Retrieves all {@linkplain Need needs} that have not been met
	 *
	 * @return An array of {@link Need need} objects, may be empty
	 *
	 * @throws IOException if an issue with underlying storage
	 */
	Need[] getUnfulfilledNeeds() throws IOException;

	/**
	 * Finds all {@linkplain Need needs} whose name contains the given text
	 *
	 * @param containsText The text to match against
	 *
	 * @return An array of {@link Need needs} whose names contain the given text;
	 *         may be empty
	 *
	 * @throws IOException if an issue with underlying storage
	 */
	Need[] findNeedsByName(String containsText) throws IOException;

	/**
	 * Finds all {@linkplain Need needs} who have a tag that matches the given text
	 *
	 * @param containsText The text to match against
	 *
	 * @return An array of {@link Need needs} whose tags contain the given text; may
	 *         be empty
	 *
	 * @throws IOException if an issue with underlying storage
	 */
	Need[] findNeedsByTag(String containsTag) throws IOException;

	/**
	 * Retrieves a {@linkplain Need need} with the given ID
	 *
	 * @param id the id of the {@link Need need} to get
	 *
	 * @return a {@link Need need} object with the matching id;
	 *         <br>
	 *         null if no {@link Need need} with a matching ID is found
	 *
	 * @throws IOException if an issue with underlying storage
	 */
	Need getNeed(int id) throws IOException;

	/**
	 * Creates and saves a {@linkplain Need need}
	 *
	 * @param need {@link Need need} object to be created and saved
	 *             <br>
	 *             The id of the new need object is ignored and a new unique id is
	 *             assigned
	 *
	 * @return new {@link Need need} if successful; null otherwise
	 *
	 * @throws IOException if an issue with underlying storage
	 */
	Need createNeed(Need need) throws IOException;

	/**
	 * Update and saves a {@linkplain Need need}
	 *
	 * @param need {@link Need need} object to be updated and saved
	 *
	 * @return updated {@link Need need} if successful; null if could not be found
	 *
	 * @throws IOException if an issue with underlying storage
	 */
	Need updateNeed(Need need) throws IOException;

	/**
	 * Deletes a {@linkplain Need need}
	 *
	 * @param id The id of the {@link Need need}
	 *
	 * @return true if the {@link Need need} was deleted
	 *         <br>
	 *         false if {@link Need} with given ID does not exist
	 *
	 * @throws IOException if an issue with underlying storage
	 */
	boolean deleteNeed(int id) throws IOException;
}
