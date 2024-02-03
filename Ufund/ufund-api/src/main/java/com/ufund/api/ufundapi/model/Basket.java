package com.ufund.api.ufundapi.model;

import java.util.ArrayList;
// import java.util.logging.Logger;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a single Basket of {@link Need needs}
 */

public class Basket {
    // private static final Logger LOG = Logger.getLogger(Need.class.getName());

    @JsonProperty("userID")
    private Integer userID;
    @JsonProperty("needs")
    private ArrayList<Integer> needs;

    /**
     * Create a need with the given ID and Name
     *
     * @param user The username of the basket's owner
     */
    public Basket(@JsonProperty("userID") Integer userID, @JsonProperty("needs") Integer[] needs) {
        this.userID = userID;
        this.needs = new ArrayList<Integer>();
        for (Integer need : needs) {
            this.needs.add(need);
        }
    };

    /**
     * Retrieves the username of the basket
     *
     * @return The username of the owner
     */
    public Integer getUserID() {
        return this.userID;
    }

    /**
     * Retrieves the IDs of the {@link Need needs} in the basket
     *
     * @return The {@link Need needs} in the basket
     */
    public ArrayList<Integer> getNeeds() {
        return this.needs;
    }

    /**
     * Adds a {@link Need need} by its ID
     *
     * @param id The id of the {@link Need need}
     *
     * @return true if successful; false if it already exists
     */
    public boolean addNeed(int id) {
        if (this.needs.contains(id))
            return false;
        else {
            this.needs.add(id);
            return true;
        }
    }

    /**
     * Retrieves a {@link Need need} by its ID
     *
     * @param id The id of the {@link Need need}
     *
     * @return true if the {@link Need need} exists
     *         <br>
     *         false if {@link Need} with given ID does not exist
     */
    public boolean getNeed(int id) {
        return this.needs.contains(id);
    }

    /**
     * Deletes a {@link Need need}
     *
     * @param id The id of the {@link Need need}
     *
     * @return true if the {@link Need need} was deleted
     *         <br>
     *         false if {@link Need} with given ID does not exist
     */
    public boolean removeNeed(int id) {
        int i = this.needs.indexOf(id);
        if (i != -1) {
            this.needs.remove(i);
            return true;
        } else
            return false;
    }
}
