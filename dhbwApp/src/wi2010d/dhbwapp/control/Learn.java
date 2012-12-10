package wi2010d.dhbwapp.control;

import java.util.List;
import java.util.Queue;

import wi2010d.dhbwapp.model.Card;
import wi2010d.dhbwapp.model.Runthrough;
import wi2010d.dhbwapp.model.Stack;

public class Learn {
	private Queue sure;
	private Queue dontKnow;
	private Queue notSure;
	private Runthrough runthrough;
	private int[] statusBefore = {0,0,0};
	private List<Card> cards;
	private int actualCard = 0;
	private Card card;

	private void startLearning(Stack stack){
		cards = stack.getCards();
		for(Card cd :cards){
			switch (cd.getDrawer()){
			case 0:
				notSure.add(cd);
			case 1:
				dontKnow.add(cd);
			case 2:
				sure.add(cd);
			}
		}
		
		card=(Card) notSure.element();
		
		//gloable Variablen auf entsprechende Kartenwerte setzen, card.getFrontText...
		//runthrough.setStatusAfter();
	}
	
	private void learnCard(int drawer){
		if(actualCard>cards.size()){
			//globale Variable runthroughDone auf true setzen
		}
		else{
			//abfragen welche karte als nächstes gezeigt werden soll actualCard%2...
		}
	}
}
