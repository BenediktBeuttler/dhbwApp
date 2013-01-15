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

import wi2010d.dhbwapp.R;
import wi2010d.dhbwapp.errorhandler.ErrorHandlerFragment;
import wi2010d.dhbwapp.model.Card;
import wi2010d.dhbwapp.model.Runthrough;
import wi2010d.dhbwapp.model.Stack;
import wi2010d.dhbwapp.model.Tag;
import android.widget.Toast;

public class Exchange {

	private static Exchange exchange;
	private ArrayList<String> imageList = new ArrayList<String>();
	private ArrayList<String> imageListPath = new ArrayList<String>();

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

	/**
	 * Exports the stack, with all referenced cards and tags, but without
	 * runthroughs. Old files with the same name will be overwritten
	 * 
	 * @param stack
	 *            The stack to export
	 * @param outputPath
	 *            The path
	 * @param exportName
	 *            The name of the file. '.xml' will be added
	 * @return true, if it worked, otherwise false and an Error Toast will be
	 *         shown
	 * @throws Exception
	 *             Path not found/Cannot write/JDOM Error
	 */
	public boolean exportStack(Stack stack, String outputPath, String exportName)
			throws Exception {
		imageListPath.clear();

		// create a new JDOM Document
		Document exportDoc = DocumentBuilderFactory.newInstance()
				.newDocumentBuilder().newDocument();

		// attach the stack
		Element stackElem = exportDoc.createElement("stack");
		stackElem.setAttribute("name", stack.getStackName());
		stackElem.setAttribute("isDynamicGenerated",
				"" + stack.isDynamicGenerated());

		// if it's a dynamic stack, attach the tags
		if (stack.isDynamicGenerated()) {
			for (Tag tag : stack.getDynamicStackTags()) {
				Element tagElem = exportDoc.createElement("tag");
				tagElem.setAttribute("name", tag.getTagName());
				tagElem.setAttribute("totalCards", "" + tag.getTotalCards());
				stackElem.appendChild(tagElem);
			}
		}

		// append it to the document
		exportDoc.appendChild(stackElem);

		// write the cards
		for (Card card : stack.getCards()) {
			Element cardElem = exportDoc.createElement("card");
			cardElem.setAttribute("front", card.getCardFront());
			cardElem.setAttribute("back", card.getCardBack());
			cardElem.setAttribute("frontPic", card.getCardFrontPicture());
			cardElem.setAttribute("backPic", card.getCardBackPicture());

			// Add the picture paths to the list, needed for Email Export
			if (!card.getCardFrontPicture().equals("")) {
				imageListPath.add(card.getCardFrontPicture());
			}
			if (!card.getCardBackPicture().equals("")) {
				imageListPath.add(card.getCardBackPicture());
			}

			// write the cards' tags
			for (Tag tag : card.getTags()) {
				Element tagElem = exportDoc.createElement("tag");
				tagElem.setAttribute("name", tag.getTagName());
				cardElem.appendChild(tagElem);
			}

			// attach it to the stack
			stackElem.appendChild(cardElem);
		}

		// Write it to the specified path (only SD-Card works)
		// Old files with the same name will be overwritten
		TransformerFactory
				.newInstance()
				.newTransformer()
				.transform(new DOMSource(exportDoc),
						new StreamResult(outputPath + exportName + ".xml"));
		return true;
	}

	/**
	 * Import the Stack from the given xml file and attach it to the structure
	 * Fails, if the stack already exists
	 * 
	 * @param pathToXML
	 *            The path to the xml file to import
	 * @return true if it worked, otherwise false and a Toast will be shown
	 * @throws JDOMException
	 *             JDOM Exceptions, like wrong attribute name
	 * @throws IOException
	 *             File does not exist/ Cannot read
	 */
	public boolean importStack(String pathToXML) throws JDOMException,
			IOException {
		// clear the imageList, so it can be filled
		imageList.clear();

		// create a cardlist
		List<Card> cardlist = new ArrayList<Card>();

		// open the given document
		org.jdom2.Document importDoc = new org.jdom2.input.SAXBuilder()
				.build(new File(pathToXML));

		// get the root
		org.jdom2.Element rootElem = importDoc.getRootElement();

		// root must be "stack" otherwise something went wrong during export or
		// the file is invalid
		if (rootElem.getName().equals("stack")) {
			boolean isDynamicGenerated;
			String stackName = rootElem.getAttributeValue("name");

			// check if the stack is already existing
			for (Stack stack : Stack.allStacks) {
				if (stack.getStackName().equals(stackName)) {
					Toast.makeText(ErrorHandlerFragment.applicationContext,
							"Stack already exists", Toast.LENGTH_LONG).show();
					return false;
				}
			}

			// Convert from string to boolean
			if (rootElem.getAttributeValue("isDynamicGenerated").equals("true")) {
				isDynamicGenerated = true;
			} else {
				isDynamicGenerated = false;
			}

			// create the stack
			Stack stack = new Stack(isDynamicGenerated, Stack.getNextStackID(),
					stackName, 0, 0, 0);
			int[] statusbefore = { 0, 0, 0 };
			// create the overall runthrough
			stack.setOverallRunthrough(new Runthrough(stack.getStackID(), true,
					statusbefore));

			if (stack.isDynamicGenerated()) {
				for (org.jdom2.Element tagElem : rootElem.getChildren()) {
					if (tagElem.getName().equals("tag")) {
						String tagName = tagElem.getAttributeValue("name");
						stack.getDynamicStackTags().add(
								Create.getInstance().newTag(tagName));
					}
				}

			}

			// create the cards
			cardlist = this.createCards(rootElem.getChildren());
			for (Card card : cardlist) {
				stack.getCards().add(card);
				stack.setDontKnow(stack.getDontKnow() + 1);
			}

			// write everything to the DB
			Database.getInstance().addNewStack(stack);

		} else {
			Toast.makeText(ErrorHandlerFragment.applicationContext,
					R.string.error_handler_import_error, Toast.LENGTH_LONG)
					.show();
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
			if (cardElem.getName().equals("card")) {
				String front = cardElem.getAttributeValue("front");
				String back = cardElem.getAttributeValue("back");
				String frontPic = cardElem.getAttributeValue("frontPic");
				String backPic = cardElem.getAttributeValue("backPic");
				List<Tag> tagList = new ArrayList<Tag>();
				// Add the PictureNames to the imageList --> needed to copy
				if (!frontPic.equals("")) {
					// Only add FileNames
					imageList
							.add(frontPic.substring(frontPic.lastIndexOf("/") + 1));
				}
				if (!backPic.equals("")) {
					// Only add FileNames
					imageList
							.add(backPic.substring(backPic.lastIndexOf("/") + 1));
				}

				int foundTag = -1;
				for (org.jdom2.Element tagElem : cardElem.getChildren()) {
					// check if the Tag already exists
					for (int i = 0; i < Tag.allTags.size(); i++) {
						if (tagElem.getAttributeValue("name").equals(
								Tag.allTags.get(i).getTagName())) {
							foundTag = i;
						}
					}
					// if it exists, add the found tag, otherwise create a new
					// one
					if (foundTag >= 0) {
						tagList.add(Tag.allTags.get(foundTag));
					} else {
						tagList.add(Create.getInstance().newTag(
								tagElem.getAttributeValue("name")));
					}
				}
				cardList.add(Create.getInstance().newCard(front, back, tagList,
						frontPic, backPic));
			}
		}
		return cardList;
	}

	/**
	 * @return The list filled with the imported image
	 */
	public ArrayList<String> getImageList() {
		return imageList;
	}

	/**
	 * @return the imageListPath
	 */
	public ArrayList<String> getImageListPath() {
		return imageListPath;
	}

}
