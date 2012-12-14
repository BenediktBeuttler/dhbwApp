package wi2010d.dhbwapp.control;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.jdom2.JDOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import wi2010d.dhbwapp.model.Card;
import wi2010d.dhbwapp.model.Stack;
import wi2010d.dhbwapp.model.Tag;
import wi2010d.dhbwapp.errorhandler.ErrorHandler;

public class Exchange {

	private static Exchange exchange;

	/**
	 * Constructor
	 */
	public Exchange() {
		;
	}

	/**
	 * Singleton Method
	 * 
	 * @return
	 */
	public static Exchange getInstance() {
		if (exchange == null) {
			exchange = new Exchange();
		}

		return exchange;
	}

	public boolean exportStack(Stack stack, String outputPath, String exportName)
			throws Exception {
		Document exportDoc = DocumentBuilderFactory.newInstance()
				.newDocumentBuilder().newDocument();

		Element stackElem = exportDoc.createElement("stack");
		stackElem.setAttribute("name", stack.getStackName());
		stackElem.setAttribute("isDynamicGenerated",
				"" + stack.isDynamicGenerated());
		if (stack.isDynamicGenerated()) {
			for (Tag tag : stack.getDynamicStackTags()) {
				Element tagElem = exportDoc.createElement("tag");
				tagElem.setAttribute("name", tag.getTagName());
				tagElem.setAttribute("totalCards", "" + tag.getTotalCards());
				stackElem.appendChild(tagElem);
			}
		}

		exportDoc.appendChild(stackElem);

		for (Card card : stack.getCards()) {
			Element cardElem = exportDoc.createElement("card");
			cardElem.setAttribute("front", card.getCardFront());
			cardElem.setAttribute("back", card.getCardBack());
			cardElem.setAttribute("frontPic", card.getCardFrontPicture());
			cardElem.setAttribute("backPic", card.getCardBackPicture());

			for (Tag tag : card.getTags()) {
				Element tagElem = exportDoc.createElement("tag");
				tagElem.setAttribute("name", tag.getTagName());
				cardElem.appendChild(tagElem);
			}

			stackElem.appendChild(cardElem);
		}

		TransformerFactory
				.newInstance()
				.newTransformer()
				.transform(new DOMSource(exportDoc),
						new StreamResult(outputPath + exportName + ".xml"));
		return true;
	}

	public boolean importStack(String pathToXML) throws JDOMException,
			IOException {
		List<Card> cardlist = new ArrayList<Card>();

		org.jdom2.Document importDoc = new org.jdom2.input.SAXBuilder()
				.build(new File(pathToXML));

		org.jdom2.Element rootElem = importDoc.getRootElement();

		if (rootElem.getName().equals("stack")) {
			boolean isDynamicGenerated;
			String stackName = rootElem.getAttributeValue("name");

			if (rootElem.getAttributeValue("isDynamicGenerated").equals("true")) {
				isDynamicGenerated = true;
			} else {
				isDynamicGenerated = false;
			}
			Stack stack = new Stack(isDynamicGenerated, Stack.getNextStackID(),
					stackName, 0, 0, 0);

			cardlist = this.createCards(rootElem.getChildren());
			for (Card card : cardlist) {
				stack.getCards().add(card);
			}
		} else {
			ErrorHandler handler = ErrorHandler.getInstance();
			handler.handleError(handler.IMPORT_ERROR);
			return false;
		}
		return true;
	}

	/**
	 * Creates new Card Elements and add creates the tags if neccessary
	 * 
	 * @param cards
	 *            The Card Element List
	 * @return List of all created Cards
	 */
	private List<Card> createCards(List<org.jdom2.Element> cards) {
		List<Card> cardList = new ArrayList<Card>();

		for (org.jdom2.Element cardElem : cards) {
			String front = cardElem.getAttributeValue("front");
			String back = cardElem.getAttributeValue("back");
			String frontPic = cardElem.getAttributeValue("frontPic");
			String backPic = cardElem.getAttributeValue("backPic");
			List<Tag> tagList = new ArrayList<Tag>();

			int foundTag = -1;
			for (org.jdom2.Element tagElem : cardElem.getChildren()) {
				// check if the Tag already exists
				for (int i = 0; i < Tag.allTags.size(); i++) {
					if (tagElem.getAttributeValue("name").equals(
							Tag.allTags.get(i).getTagName())) {
						foundTag = i;
					}
				}
				// if it exists, add the found tag, otherwise create a new one
				if (foundTag >= 0) {
					tagList.add(Tag.allTags.get(foundTag));
				} else {
					tagList.add(new Tag(tagElem.getAttributeValue("name")));
				}
			}
			cardList.add(new Card(front, back, frontPic, backPic, tagList));
		}
		return cardList;
	}

}
