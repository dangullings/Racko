import java.util.ArrayList;

 public abstract class Ai extends Player{

	ArrayList<Card> cardMemory = new ArrayList<Card>();
	ArrayList<Card> cardsNeeded = new ArrayList<Card>();
	
	public Ai(int r, int g, int b, String name, boolean ai) {
		super(r, g, b, name, ai);
	}

	public abstract Card getSwapCard(Card playCard, boolean isDiscard);
	
	public abstract Card getSwapCardTest(Card playCard, boolean isDiscard);
	
	public void setCardsNeeded(){
		int lastCardValue;
		for (int i = 0; i < hand.cards.size() - 1; i++){
			if (hand.order[i]){
				lastCardValue = hand.cards.get(i).getValue();
			}else{
				
			}
		}
	}
}
