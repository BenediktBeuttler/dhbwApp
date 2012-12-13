package wi2010d.dhbwapp.model;

import java.util.ArrayList;
import java.util.List;

public class Tag {

	public static List<Tag> allTags = new ArrayList<Tag>();
	public static int lastTagID;
	private int tagID;
	private int totalCards;
	private String tagName;

	public Tag(int tagID, int totalCards, String tagName) {
		this.tagID = tagID;
		this.totalCards = totalCards;
		this.tagName = tagName;

		Tag.allTags.add(this);
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
	}

	public int getTagID() {
		return tagID;
	}

	public int getTotalCards() {
		return totalCards;
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
