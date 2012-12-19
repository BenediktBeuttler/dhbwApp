package wi2010d.dhbwapp.control;

import java.util.AbstractQueue;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

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

	public Card startLearning(Stack pStack) {

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

	public Card learnCard(int drawer) {
		card.setDrawer(drawer);

		if (actualCard > cards.size()) {
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
			switch (run) {
			case 1:
				if (!notSure.isEmpty()) {
					card = (Card) notSure.remove();
					run = 2;
					break;
				} else {
					run = 2;
				}
			case 2:
				if (!dontKnow.isEmpty()) {
					card = (Card) dontKnow.remove();
					run = 3;
					break;
				} else {
					run = 3;
				}
			case 3:
				if (!sure.isEmpty()) {
					card = (Card) sure.remove();
					run = 4;
					break;
				} else {
					run = 4;
				}
			case 4:
				if (!dontKnow.isEmpty()) {
					card = (Card) dontKnow.remove();
					run = 5;
					break;
				} else {
					run = 5;
				}
			case 5:
				if (!notSure.isEmpty()) {
					card = (Card) notSure.remove();
					run = 6;
					break;
				} else {
					run = 6;
				}
			case 6:
				if (!dontKnow.isEmpty()) {
					card = (Card) dontKnow.remove();
					run = 1;
					break;
				} else {
					run = 1;
				}
			}
			actualCard++;
			return card;
		}
	}

	public static Learn getInstance() {
		if (learn == null) {
			learn = new Learn();
		}
		return learn;
	}

	public class CardQueue extends AbstractQueue<Card> {

		private int queueSize = 0;

		@Override
		public boolean add(Card card) {
			queueSize++;
			return super.add(card);
		}

		@Override
		public Card remove() {
			queueSize--;
			return super.remove();
		}

		@Override
		public Card peek() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Card poll() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Iterator iterator() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public int size() {
			return queueSize;
		}

		@Override
		public boolean isEmpty() {
			return queueSize == 0;
		}

		@Override
		public boolean offer(Card e) {
			// TODO Auto-generated method stub
			return false;
		}

	}
}
