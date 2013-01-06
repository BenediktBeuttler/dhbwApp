package wi2010d.dhbwapp.control;

import java.util.AbstractQueue;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import android.util.Log;

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

	public Card startLearning(Stack pStack) {

		// reset
		actualCard = 1;
		run = 1;
		statusAfter[0] = 0;
		statusAfter[1] = 0;
		statusAfter[2] = 0;

		sure = new CardQueue();
		dontKnow = new CardQueue();
		notSure = new CardQueue();

		stack = pStack;
		cards = stack.getCards();

		// init Queues
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

		// init statusBefore Array
		statusBefore[0] = dontKnow.size();
		statusBefore[1] = notSure.size();
		statusBefore[2] = sure.size();

		Random generator = new Random();

		if (notSure.size() > 1) {
			int valueFor = notSure.size() / 2;
			for (int i = 1; i <= valueFor; i++) {
				int rnd = generator.nextInt(notSure.size());
				notSure.remove(rnd);
			}
		}

		if (sure.size() > 2) {
			int valueFor = sure.size() / 3 * 2;
			if (sure.size() % 3 >= 1) {
				valueFor++;
			}
			for (int i = 1; i <= valueFor; i++) {
				int rnd = generator.nextInt(sure.size());

				Log.e("TB Schleife",
						"sure.size: " + sure.size() + ", sure.size / 3: "
								+ (sure.size() / 3)
								+ ", generator.nextInt(sure.size() - 1: )"
								+ (generator.nextInt(sure.size() - 1)));

				sure.remove(rnd);
			}
		} else {
			if (sure.size() == 2) {
				sure.remove(generator.nextInt(2));
			}
		}

		cardsInQueues = dontKnow.size() + notSure.size() + sure.size();

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
		runthrough = new Runthrough(stack.getStackID(), false, statusBefore);
		return card;
	}

	public String getActualProgressAsString() {
		String progress = "";
		progress = "Karte " + actualCard + " von " + cardsInQueues;
		return progress;
	}

	public Card learnCard(int drawer) {

		if (drawer <= 2) {
			Edit.getInstance().setDrawer(card, drawer);
		}
		
		if (actualCard >= cardsInQueues) {
			// globale Variable runthroughDone auf true setzen

			// read status after runthrough and store it in the runthrough
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

			runthrough.setStatusAfter(statusAfter[0], statusAfter[1],
					statusAfter[2]);
			runthrough.setEndDate(new Date());
			stack.addLastRunthrough(runthrough);
			return null;
		} else {
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
	}

	public static Learn getInstance() {
		if (learn == null) {
			learn = new Learn();
		}
		return learn;
	}

	private class CardQueue {
		private ArrayList<Card> arrayList;
		private int queueSize = 0;

		public CardQueue() {
			arrayList = new ArrayList<Card>();
		}

		public void remove(int index) {
			queueSize--;
			arrayList.remove(index);
		}

		public void add(Card card) {
			queueSize++;
			arrayList.add(0, card);
		}

		public Card remove() {
			queueSize--;
			return (Card) arrayList.remove(0);
		}

		public int size() {
			return queueSize;
		}

		public boolean isEmpty() {
			return queueSize == 0;
		}

	}
}
