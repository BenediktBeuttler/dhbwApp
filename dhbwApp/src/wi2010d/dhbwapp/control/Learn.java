package wi2010d.dhbwapp.control;

import java.util.AbstractQueue;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import android.util.Log;
import android.widget.Toast;

import wi2010d.dhbwapp.LearningCard;
import wi2010d.dhbwapp.errorhandler.ErrorHandler;
import wi2010d.dhbwapp.model.Card;
import wi2010d.dhbwapp.model.Runthrough;
import wi2010d.dhbwapp.model.Stack;

public class Learn {
	private CardQueue sure;
	private CardQueue dontKnow;
	private CardQueue notSure;
	private Runthrough runthrough;
	private int[] statusBefore = { 0, 0, 0 };
	private int[] statusAfter = { 0, 0, 0 };
	private List<Card> cards;
	private int actualCard = 1;
	private int run = 1;
	private Card card;
	private Stack stack;
	private static Learn learn;
	private int cardsInQueues;
	private int totalCards;

	/**
	 * Use this method to start the Learning session. It resets the variables
	 * and initializes the queues for the composition of the Learning session
	 * 
	 * @param pStack
	 * @return the first card, for the Learning session
	 */
	public Card startLearning(Stack pStack) {

		// reset the variables
		actualCard = 1;
		run = 1;
		statusAfter[0] = 0;
		statusAfter[1] = 0;
		statusAfter[2] = 0;

		// initialize the Queues
		sure = new CardQueue();
		dontKnow = new CardQueue();
		notSure = new CardQueue();

		// set the parameters stack and cards
		stack = pStack;
		cards = stack.getCards();
		totalCards = cards.size();

		// add the cards from the stack to the queues
		for (Card cd : cards) {
			switch (cd.getDrawer()) {
			case 0:
				dontKnow.add(cd);
				break;
			case 1:
				notSure.add(cd);
				break;
			case 2:
				sure.add(cd);
				break;
			}
		}

		// initialize statusBefore Array
		statusBefore[0] = dontKnow.getSize();
		statusBefore[1] = notSure.getSize();
		statusBefore[2] = sure.getSize();

		// create a random generator
		Random generator = new Random();

		/*
		 * if there are more notSure-cards than 1 take only 50% of the cards
		 * from the notSure drawer for the learning session, else take all (1)
		 */
		if (notSure.getSize() > 1) {
			int valueFor = notSure.getSize() / 2;
			for (int i = 1; i <= valueFor; i++) {
				int rnd = generator.nextInt(notSure.getSize());
				notSure.remove(rnd);
			}
		}

		/*
		 * if there are more sure-cards than 2 take only 33% of the cards from
		 * the sure drawer for the learning session (round up), else if there
		 * are 2 cards take 1, else take all (1)
		 */
		if (sure.getSize() > 2) {
			int valueFor = sure.getSize() / 3 * 2;
			if (sure.getSize() % 3 >= 1) {
				valueFor++;
			}
			for (int i = 1; i <= valueFor; i++) {
				int rnd = generator.nextInt(sure.getSize());
				sure.remove(rnd);
			}
		} else {
			if (sure.getSize() == 2) {
				sure.remove(generator.nextInt(2));
			}
		}

		// sum up all the cards in the queues in one variable
		cardsInQueues = dontKnow.getSize() + notSure.getSize() + sure.getSize();

		// get the first card to start with
		if (!dontKnow.isEmpty()) {
			card = (Card) dontKnow.remove();
		} else {
			if (!notSure.isEmpty()) {
				card = (Card) notSure.remove();
				run = 2;
			} else {
				if (!sure.isEmpty()) {
					card = (Card) sure.remove();
					run = 3;
				} else {
					ErrorHandler.getInstance().handleError(
							ErrorHandler.getInstance().GENERAL_ERROR);
					card = null;
				}
			}
		}

		// create a new runthrough for the actual stack
		runthrough = new Runthrough(stack.getStackID(), false, statusBefore);

		// return the first card
		return card;
	}

	/**
	 * @return a string with the actual progress in the learning session
	 */
	public String getActualProgressAsString() {
		String progress = "";
		progress = "Karte " + actualCard + " von " + cardsInQueues;
		return progress;
	}

	/**
	 * Use this method to store the actual Card in the delivered drawer (0-2)
	 * and get the next card in the learning session. In case it was the last
	 * card it updates the runthrough and returns null. In case the drawer
	 * number is > 2 the drawer won't be changed for the actual card. In case
	 * the drawer number is 4 the learning session will be aborted
	 * 
	 * @param drawer
	 * @return the next card in the learning session, or null
	 */
	public Card learnCard(int drawer) {

		// if drawer 4 is delivered, the learning session will be aborted
		if (drawer == 4) {
			actualCard = cardsInQueues;
		}

		if (drawer == 3) {
			totalCards--;
		}

		// if drawer is 0, 1 or 2, the actual card's drawer will be updated
		if (drawer <= 2) {
			Edit.getInstance().setDrawer(card, drawer);
		}

		/*
		 * if the actual card is the last card in the stack, the runthrough will
		 * be updated and null will be returned
		 */
		if (totalCards > 0) {
			if (actualCard >= cardsInQueues) {

				// read status after runthrough

				cards = stack.getCards();

				for (Card cd : cards) {
					switch (cd.getDrawer()) {
					case 0:
						statusAfter[0]++;
						break;
					case 1:
						statusAfter[1]++;
						break;
					case 2:
						statusAfter[2]++;
						break;
					}
				}

				// store the statusAfter and the EndDate in the runthrough of
				// the stack
				runthrough.setStatusAfter(statusAfter[0], statusAfter[1],
						statusAfter[2]);
				runthrough.setEndDate(new Date());
				stack.addLastRunthrough(runthrough);

				return null;
			} else {
				/*
				 * get the next card for the learning session, the algorithm is
				 * dontKnow->notSure->dontKnow->sure->dontKnow->notSure and new
				 * form the beginning
				 */
				int oldValueActualCard = actualCard;
				while (actualCard == oldValueActualCard) {
					switch (run) {
					case 1:
						if (!notSure.isEmpty()) {
							card = (Card) notSure.remove();
							run = 2;
							actualCard++;
							break;
						} else {
							run = 2;
						}
					case 2:
						if (!dontKnow.isEmpty()) {
							card = (Card) dontKnow.remove();
							run = 3;
							actualCard++;
							break;
						} else {
							run = 3;
						}
					case 3:
						if (!sure.isEmpty()) {
							card = (Card) sure.remove();
							run = 4;
							actualCard++;
							break;
						} else {
							run = 4;
						}
					case 4:
						if (!dontKnow.isEmpty()) {
							card = (Card) dontKnow.remove();
							run = 5;
							actualCard++;
							break;
						} else {
							run = 5;
						}
					case 5:
						if (!notSure.isEmpty()) {
							card = (Card) notSure.remove();
							run = 6;
							actualCard++;
							break;
						} else {
							run = 6;
						}
					case 6:
						if (!dontKnow.isEmpty()) {
							card = (Card) dontKnow.remove();
							run = 1;
							actualCard++;
							break;
						} else {
							run = 1;
						}
					}
				}
				return card;
			}
		} else {
			return null;
		}
	}

	/**
	 * Singleton method
	 * 
	 * @return instance of Learn
	 */
	public static Learn getInstance() {
		if (learn == null) {
			learn = new Learn();
		}
		return learn;
	}

	/**
	 * This is a help class for the management of the queues that store the
	 * cards for the learning session.
	 */
	private class CardQueue {
		private ArrayList<Card> arrayList;
		private int queueSize = 0;

		/**
		 * constructor
		 */
		public CardQueue() {
			arrayList = new ArrayList<Card>();
		}

		/**
		 * Use this method to remove the card at the specified position
		 * 
		 * @param index
		 *            as specified position of the card in the queue
		 */
		public void remove(int index) {
			queueSize--;
			arrayList.remove(index);
		}

		/**
		 * Use this method to add a specified card to the queue. The card is
		 * added to the front of the queue, the remaining cards are moved
		 * backward
		 * 
		 * @param card
		 */
		public void add(Card card) {
			queueSize++;
			arrayList.add(0, card);
		}

		/**
		 * Use this method to get the first card in the CardQueue and remove it
		 * at the same time. The remaining cards are moved forward
		 * 
		 * @return
		 */
		public Card remove() {
			queueSize--;
			return (Card) arrayList.remove(0);
		}

		/**
		 * @return the size of the cardQueue
		 */
		public int getSize() {
			return queueSize;
		}

		/**
		 * @return true if the cardQueue is empty, otherwise returns false
		 */
		public boolean isEmpty() {
			return queueSize == 0;
		}

	}
}
