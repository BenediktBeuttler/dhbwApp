package wi2010d.dhbwapp.model;

import java.util.ArrayList;
import java.util.List;

import wi2010d.dhbwapp.control.Init;

/**
 * Represents a Tag, which can be associated with cards and/or stacks.
 */
public class Tag {

	public static List<Tag> allTags = new ArrayList<Tag>();
	public static int lastTagID;
	private int tagID;
	private int totalCards;
	private String tagName;
	private boolean checked = false;

	/**
	 * Use this constructer when loading from DB
	 * 
	 * @param tagID
	 *            The tags' ID
	 * @param totalCards
	 *            The number of cards
	 * @param tagName
	 *            The tags' name
	 */
	public Tag(int tagID, int totalCards, String tagName) {
		this.tagID = tagID;
		this.totalCards = totalCards;
		this.tagName = tagName;

		Tag.allTags.add(this);

		if (lastTagID <= tagID) {
			lastTagID = tagID;
		}
	}

	/**
	 * Use this constructor when creating a new tag
	 * 
	 * @param tagName
	 *            The tags' name
	 */
	public Tag(String tagName) {
		this.tagName = tagName;
		this.tagID = Tag.getNextTagID();
		this.totalCards = 0;

		Tag.allTags.add(this);
		Init.dataWritten = true;
	}

	/**
	 * Increases the number of total cards.
	 * 
	 * @return the number of total cards +1.
	 */
	public int increaseTotalCards() {
		totalCards = totalCards + 1;
		return totalCards;
	}

	/**
	 * Decreases the number of total cards.
	 * 
	 * @return the number of total cards -1.
	 */
	public int decreaseTotalCards() {

		totalCards = totalCards - 1;
		return totalCards;
	}

	/**
	 * @return The next TagID from the counter
	 */
	public static int getNextTagID() {
		lastTagID = lastTagID + 1;
		return lastTagID;
	}

	/**
	 * @return The tags' name
	 */
	public String getTagName() {
		return tagName;
	}

	/**
	 * Sets the tags' name
	 * 
	 * @param tagName
	 *            the tags' name
	 */
	public void setTagName(String tagName) {
		this.tagName = tagName;
	}

	/**
	 * @return the tags' id
	 */
	public int getTagID() {
		return tagID;
	}

	/**
	 * @return The number of total cards' associated with this tag
	 */
	public int getTotalCards() {
		return totalCards;
	}

	// Next methods needed for Checkbox list
	/**
	 * @return true, if the tag is checked in the list
	 */
	public boolean isChecked() {
		return checked;
	}

	/**
	 * Sets the tags checkbox to true
	 * 
	 * @param checked
	 */
	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	/**
	 * Changes the checkbox from true to false and vise-versa
	 */
	public void toggleChecked() {
		checked = !checked;
	}

	/**
	 * Resets the counter for the tag IDs
	 * @return true, if it worked
	 */
	public static boolean resetLastTagID() {
		lastTagID = 0;
		return true;
	}

	@Override
	public String toString() {
		return tagName;
	}

	@Override
	public boolean equals(Object o) {
		Tag tag = (Tag) o;
		if (tag.getTagID() == this.tagID) {
			return true;
		}
		return false;
	}

}
