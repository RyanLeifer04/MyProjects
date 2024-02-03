package com.ufund.api.ufundapi.model;

import java.util.HashSet;
// import java.util.logging.Logger;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a single Need entity
 *
 * @author Blizzard Finnegan
 */

public class Need {
	// private static final Logger LOG = Logger.getLogger(Need.class.getName());

	static final String STRING_FORMAT = "Need [id=%d, name=%s, amount_in_stock=%,.2f, amount_needed=%,.2f, amount_unit=%s, tags=%s]";

	@JsonProperty("id")
	private int id;
	@JsonProperty("name")
	private String name;
	@JsonProperty("amount_in_stock")
	private double amount_in_stock;
	@JsonProperty("amount_needed")
	private double amount_needed;
	@JsonProperty("amount_unit")
	private String amount_unit;
	@JsonProperty("tags")
	private HashSet<String> tags;

	/**
	 * Create a need with the given ID and Name
	 *
	 * @param id   The id of the Need
	 * @param name the name of the Need
	 *
	 *             {@literal @}JSonProperty is used in serialisation and
	 *             deserialisation of the JSON object to the Java object in mapping
	 *             the fields. If a field is not provided n the JSON object, the
	 *             java field gets the default java value.
	 */
	public Need(
			@JsonProperty("id") int id,
			@JsonProperty("name") String name,
			@JsonProperty("amount_in_stock") double amount_in_stock,
			@JsonProperty("amount_needed") double amount_needed,
			@JsonProperty("amount_unit") String amount_unit) {
		this.id = id;
		this.name = name;
		this.amount_in_stock = amount_in_stock;
		this.amount_needed = amount_needed;
		this.amount_unit = amount_unit;
		this.tags = new HashSet<>();
	};

	public Need(int id, Need need) {
		this.id = id;
		this.name = new String(need.name);
		this.amount_in_stock = need.amount_in_stock;
		this.amount_needed = need.amount_needed;
		this.amount_unit = need.amount_unit;
		this.tags = new HashSet<>(need.tags);
	};

	/**
	 * Retrieves ID of the need
	 *
	 * @return The id of the hero
	 */
	public int getId() {
		return this.id;
	}

	/**
	 * Sets/modify the name of the Need
	 *
	 * @param name The name of the need
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Retrieves the name of the Need
	 *
	 * @return The name of the Need
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Sets/modify the unit of the Need
	 *
	 * @param name The unit of the need
	 */
	public void setAmountUnit(String unit) {
		this.amount_unit = unit;
	}

	/**
	 * Retrieves the unit of the Need
	 *
	 * @return The unit of the Need
	 */
	@JsonIgnore
	public String getAmountUnit() {
		return this.amount_unit;
	}

	/**
	 * Add to the amount currently in stock for this Need
	 *
	 * @param count The amount of this Need being added
	 *
	 * @return The total amount currently in stock
	 */
	public double addToInventory(double count) {
		this.amount_in_stock += count;
		return this.amount_in_stock;
	};

	/**
	 * Add to the amount currently in stock for this Need
	 *
	 * @param count The amount of this Need being added
	 *
	 * @return The total amount currently in stock
	 */
	public double removeFromInventory(double count) {
		this.amount_in_stock -= count;
		return this.amount_in_stock;
	};

	/**
	 * Retrieve the current amount in stock for this Need
	 *
	 * @return the total amount currently in stock
	 */
	public double checkInventory() {
		return this.amount_in_stock;
	}

	/**
	 * Sets the amount needed for this Need
	 *
	 * @param count The count of the amount needed
	 */
	public void setAmountNeeded(double count) {
		this.amount_needed = count;
	}

	/**
	 * Recieves the amount needed for this Need
	 *
	 * @return The count of the amount needed
	 */
	@JsonIgnore
	public double getAmountNeeded() {
		return this.amount_needed;
	}

	/**
	 * Returns whether this need has been met completely
	 *
	 * @return True if the amount in stock is enough; otherwise false
	 */
	public boolean hasBeenMet() {
		return this.amount_in_stock - this.amount_needed >= 0;
	}

	/**
	 * Adds a tag to this Need
	 *
	 * @param tag The tag to be added
	 *
	 * @return whether the tag was successfully added
	 */
	public boolean addTag(String tag) {
		return this.tags.add(tag);
	}

	/**
	 * Removes a tag to this Need
	 *
	 * @param tag The tag to be removes
	 *
	 * @return whether the tag was successfully removed
	 */
	public boolean removeTag(String tag) {
		return this.tags.remove(tag);
	}

	/**
	 * Clears all tags for this Need
	 */
	public void clearTags() {
		this.tags = new HashSet<>();
	}

	/**
	 * Get all tags for this Need
	 *
	 * @return HashSet of all tags
	 */
	public HashSet<String> getTags() {
		return this.tags;
	}

	/**
	 * Set all tags for this Need
	 *
	 * @param tags HashSet of all tags
	 */
	public void setTags(HashSet<String> tags) {
		this.tags = tags;
	}

	/**
	 * Check if this Need has a given tag already
	 *
	 * @param tag The tag to be searched for
	 *
	 * @return whether this need has that tag
	 *
	 */
	public boolean containsTag(String tag) {
		return this.tags.contains(tag);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return String.format(STRING_FORMAT, this.id, this.name, this.amount_in_stock, this.amount_needed,
				this.amount_unit, this.tags.toString());
	};

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object otherObject) {
		if (!(otherObject instanceof Need))
			return false;
		Need castObject = (Need) otherObject;
		return this.id == castObject.id &&
				this.name.equals(castObject.name) &&
				this.amount_unit.equals(castObject.amount_unit) &&
				this.amount_needed == castObject.amount_needed &&
				this.amount_in_stock == castObject.amount_in_stock &&
				this.tags.equals(castObject.tags);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return this.id;
	}
}
