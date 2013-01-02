package wi2010d.dhbwapp.model;

import java.util.ArrayList;
import java.util.List;

import wi2010d.dhbwapp.control.Database;

public class Tag {

	public static List<Tag> allTags = new ArrayList<Tag>();
	public static int lastTagID;
	private int tagID;
	private int totalCards;
	private String tagName;
	private boolean checked = false;

	public Tag(int tagID, int totalCards, String tagName) {
		this.tagID = tagID;
		this.totalCards = totalCards;
		this.tagName = tagName;

		Tag.allTags.add(this);

		if (lastTagID <= tagID) {
			lastTagID = tagID;
		}
	}

	public Tag(String tagName) {
		this.tagName = tagName;
		this.tagID = Tag.getNextTagID();
		this.totalCards = 0;

		Tag.allTags.add(this);
	}

	public int increaseTotalCards() {
		totalCards = totalCards + 1;
		return totalCards;
	}

	public int decreaseTotalCards() {

		totalCards = totalCards - 1;
		return totalCards;
	}

	public static int getNextTagID() {
		lastTagID = lastTagID + 1;
		return lastTagID;
	}

	public String getTagName() {
		return tagName;
	}

	public void setTagName(String tagName) {
		this.tagName = tagName;
		Database.getInstance().changeTag(this);
	}

	public int getTagID() {
		return tagID;
	}

	public int getTotalCards() {
		return totalCards;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	@Override
	public String toString() {
		return tagName;
	}

	public void toggleChecked() {
		checked = !checked;
	}

	public static boolean resetLastTagID() {
		lastTagID = 0;
		return true;
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
