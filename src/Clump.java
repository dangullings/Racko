import java.util.ArrayList;

public class Clump {

	ArrayList<Card> cards = new ArrayList<Card>();
	int range;
	int targetValue;
	int targetIndex;
	int lowIndex;
	int highIndex;
	int lowValue;
	int highValue;
	double adjacentDensity;
	
	public Clump(){
		
	}

	public void setAdjacentDensity(){
		double temp = 0;
		double lastTemp = 0;
		double total = 0;
		
		for (Card c: cards){
			temp = c.getValue() - lastTemp;
			total += temp;
			lastTemp = c.getValue();
			
			if (c.getValue() == temp)
				total = 0;
		}
		
		adjacentDensity = total / cards.size();
	}
	
	public void setTargetValue(Hand hand){
		int dist = Racko_Main.game.getNumCards();
		
		for (Card hc: hand.cards)
			for (Card c: cards)
				if (c == hc)
					if (hand.isProxy(hc, hc.getValue())){
						if (dist > Math.abs((hc.getValue() - hand.getProxy(hand.cards.indexOf(hc))))){
							targetValue = hc.getValue();
							targetIndex = hand.cards.indexOf(hc);
							dist = Math.abs((hc.getValue() - hand.getProxy(hand.cards.indexOf(hc))));
						}
						if (hand.cards.indexOf(hc) == 0){
							targetValue = hc.getValue();
							targetIndex = hand.cards.indexOf(hc);
							return;
						}
						if (hand.cards.indexOf(hc) == 9){
							targetValue = hc.getValue();
							targetIndex = hand.cards.indexOf(hc);
							return;
						}
					}
	}
	
	public int getLowValue() {
		return lowValue;
	}

	public void setLowValue(int lowValue) {
		this.lowValue = lowValue;
	}

	public int getHighValue() {
		return highValue;
	}

	public void setHighValue(int highValue) {
		this.highValue = highValue;
	}

	public int getLowIndex() {
		return lowIndex;
	}

	public void setLowIndex(int lowIndex) {
		this.lowIndex = lowIndex;
	}

	public int getHighIndex() {
		return highIndex;
	}

	public void setHighIndex(int highIndex) {
		this.highIndex = highIndex;
	}

	public double getAdjacentDensity() {
		return adjacentDensity;
	}

	public void setAdjacentDensity(double adjacentDensity) {
		this.adjacentDensity = adjacentDensity;
	}

	public int getTargetValue() {
		return targetValue;
	}

	public void setTargetValue(int targetValue) {
		this.targetValue = targetValue;
	}

	public String toString(){
		String string = "clump ";
		for (Card c: cards)
			string = string+", "+c.getValue();
		
		return string;
	}
}
