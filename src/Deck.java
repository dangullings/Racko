import java.util.Collections;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

public class Deck {
	private double x, y;
	private int size;
	CopyOnWriteArrayList<Card> deck = new CopyOnWriteArrayList<Card>();
	CopyOnWriteArrayList<Card> discards = new CopyOnWriteArrayList<Card>();
	
	static Random rnd = new Random();
	
	public Deck(){
		
	}
	
	public Deck(double x, double y, int size) {
		this.x = x;
		this.y = y;
		this.size = size;
	}

	public void setupCards(int size) {
		for (int i = 0; i < size; i++) {
			Card temp = new Card(x + (i * .05), y + (i * .05), (double) 8, (double) 10, i + 1, 100, 100, 100);
			deck.add(temp);
		}
	}

	public void draw() {
		for (Card c : discards)
            c.draw();
		
		for (Card c : deck) 
            c.draw();
	}

	public void shuffleCardsIntoDeck(){
		for (Player p : Racko_Main.game.player){
			for (Card c : p.hand.cards){
				c.setFaceUp(true);
				c.setTargetX(x + (0.5 + (2.0 - 0.5) * rnd.nextDouble()) - (0.5 + (2.0 - 0.5) * rnd.nextDouble()));
				c.setTargetY(y + (0.5 + (2.0 - 0.5) * rnd.nextDouble()) - (0.5 + (2.0 - 0.5) * rnd.nextDouble()));
				c.reset(c.getTargetX(), c.getTargetY(), 20);
				c.setHasTarget(true);
				deck.add(0, c);
				p.hand.cards.remove(c);
			}
		}
		
		while (!discards.isEmpty()){
			discards.get(0).setFaceUp(true);
			discards.get(0).setTargetX(x + (0.5 + (2.0 - 0.5) * rnd.nextDouble()) - (0.5 + (2.0 - 0.5) * rnd.nextDouble()));
			discards.get(0).setTargetY(y + (0.5 + (2.0 - 0.5) * rnd.nextDouble()) - (0.5 + (2.0 - 0.5) * rnd.nextDouble()));
			discards.get(0).reset(discards.get(0).getTargetX(), discards.get(0).getTargetY(), 20);
			discards.get(0).setHasTarget(true);
			deck.add(0, discards.get(0));
			discards.remove(0);
		}
		
		shuffleDeck();
	}
	
	public void shuffleDiscardsIntoDeck(){
		while (!discards.isEmpty()){
			discards.get(0).setFaceUp(true);
			discards.get(0).setTargetX(x + (0.5 + (2.0 - 0.5) * rnd.nextDouble()) - (0.5 + (2.0 - 0.5) * rnd.nextDouble()));
			discards.get(0).setTargetY(y + (0.5 + (2.0 - 0.5) * rnd.nextDouble()) - (0.5 + (2.0 - 0.5) * rnd.nextDouble()));
			discards.get(0).reset(discards.get(0).getTargetX(), discards.get(0).getTargetY(), 20);
			discards.get(0).setHasTarget(true);
			deck.add(0, discards.get(0));
			discards.remove(0);
		}
	}
	
	public void shuffleDeck(){
		for (Card c: deck)
			c.setFaceUp(false);
		Collections.shuffle(deck);
	}
	
	public void discard(Card discard){
		discard.setTargetX(Racko_Main.game.getDiscardX() + (0.5 + (2.0 - 0.5) * rnd.nextDouble()) - (0.5 + (2.0 - 0.5) * rnd.nextDouble()));
		discard.setTargetY(Racko_Main.game.getDiscardY() + (0.5 + (2.0 - 0.5) * rnd.nextDouble()) - (0.5 + (2.0 - 0.5) * rnd.nextDouble()));
		discard.setHasTarget(true);
		discard.reset(discard.getTargetX(), discard.getTargetY(), 5);

        Racko_Main.game.deck.discards.add(discard);
        Racko_Main.game.deck.deck.remove(discard);
        discard.setFaceUp(true);
	}
	
	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

}
