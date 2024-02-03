package com.ufund.api.ufundapi.persistence;

import java.io.IOException;
import com.ufund.api.ufundapi.model.Need;

public interface FundingBasketDAO {

	/**
	 * Retrieves IDs of all {@linkplain Need needs}
	 *
	 * @param userID The ID of the user to retrieve the {@link Need needs}
	 *
	 * @return An array of {@link Need need} IDs, may be empty
	 *
	 * @throws IOException if an issue with underlying storage
	 */
	Integer[] getNeeds(Integer userID) throws IOException;

	/**
	 * Adds a {@linkplain Need need} by its ID
	 *
	 * @param userID The ID of the user to add the {@link Need need} to
	 * @param id     The id of the {@link Need need}
	 *
	 * @return true if successful; false if it already exists
	 *
	 * @throws IOException if an issue with underlying storage
	 */
	boolean addNeed(Integer userID, int id) throws IOException;

	/**
	 * Retrieves a {@linkplain Need need} by its ID
	 *
	 * @param userID The ID of the user to retrieve the {@link Need need}
	 *               from
	 * @param id     The id of the {@link Need need}
	 *
	 * @return true if the {@link Need need} exists
	 *         <br>
	 *         false if {@link Need} with given ID does not exist
	 *
	 * @throws IOException if an issue with underlying storage
	 */
	boolean getNeed(Integer userID, int id) throws IOException;

	/**
	 * Deletes a {@linkplain Need need}
	 *
	 * @param userID The ID of the user to remove the {@link Need need} from
	 * @param id     The id of the {@link Need need}
	 *
	 * @return true if the {@link Need need} was deleted
	 *         <br>
	 *         false if {@link Need} with given ID does not exist
	 *
	 * @throws IOException if an issue with underlying storage
	 */
	boolean removeNeed(Integer userID, int id) throws IOException;
}
