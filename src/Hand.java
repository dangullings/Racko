import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;


public class Hand {
	private double x, y;
	private int size;
	private int player;
	boolean[] order;
	boolean[] partialOrder;
	boolean[] proxy;
	double[] slotLower;
	double[] slotHigher;
	double[] locX, locY;
	private int value;
	private int inc;
	private int score;
	private int modScore;
	public Card lastSelect;
	
	static Random rnd = new Random();
	
	private int prox = 0;
	
	CopyOnWriteArrayList<Card> cards = new CopyOnWriteArrayList<Card>();
	ArrayList<Integer> cardValuesNeeded = new ArrayList<Integer>();
	ArrayList<Clump> clumps = new ArrayList<Clump>();
	ArrayList<Run> bonusRun = new ArrayList<Run>();
	
	class Run{
		ArrayList<Card> cards = new ArrayList<Card>();
		private Run(){
			
		}
	}
	
	public Hand(){
		
	}
	
    public Hand(double x, double y, int size, int inc, int prox) {
        this.x = x;
        this.y = y;
        this.size = size;
        this.inc = inc;
        this.prox = prox;
        locX = new double[size];
        locY = new double[size];
        order = new boolean[size];
        partialOrder = new boolean[size];
        proxy = new boolean[size];
        
        order[0] = true;
    }
    
    public void draw() {
    	try {
    		for (int i = cards.size() - 1; i >= 0; i--) {
        		if (cards.get(i).isHighlighted())
        			if (Racko_Main.game.getSelectedCard() != null)
        				Racko_Main.game.getSelectedCard().draw();
        		cards.get(i).draw();
        	}
            
         } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Array is out of Bounds"+e);
         }
    }

    public void drawOrder(int p){
    	value = inc;
    	
    	try {
    		for (int i = 0; i <= cards.size()-1; i++){  	
        		//if (partialOrder[i])
        			//Racko_Main.gameBoard.setPenColor(Color.LIGHT_GRAY);
        		//else
        			//Racko_Main.gameBoard.setPenColor(Color.DARK_GRAY);
        		
        		//if (proxy[i])
        			//Racko_Main.gameBoard.setPenColor(Color.GRAY);
        		//else
        			//Racko_Main.gameBoard.setPenColor(Color.DARK_GRAY);
        		Racko_Main.gameBoard.setFont(Racko_Main.gameBoard.BIG_FONT);
    			
        		if (((order[i]) && (Racko_Main.game.gameStatus != GameStatus.GAME_ON)) || ((order[i]) && (!Racko_Main.game.player.get(p).isAi())))
        			Racko_Main.gameBoard.setPenColor(Color.WHITE);
        		else
        			Racko_Main.gameBoard.setPenColor(Color.DARK_GRAY);
        		
        		//Racko_Main.gameBoard.text(locX[i]-(12), locY[i]+5, ""+value);
            	value+=inc;
        	}
         } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Array is out of Bounds"+e);
         }
    }
    
	public void findRun() {
		ArrayList<Run> runs = new ArrayList<Run>();
		Run run = new Run();
		
		for (int i = 0; i < cards.size() - 1; i++) {
			if (cards.get(i).getValue() < cards.get(i+1).getValue()){
				if ((cards.get(i+1).getValue() - cards.get(i).getValue()) == 1){
					run.cards.add(cards.get(i));
					
					if ((i + 1) == (cards.size() - 1)){
						run.cards.add(cards.get(i+1));
						
						if (run.cards.size() > 2){
							runs.add(run);
							break;
						}
						else
							break;
					}

					if (i + 2 < cards.size())
						if ((cards.get(i+2).getValue() - cards.get(i+1).getValue()) != 1){
							run.cards.add(cards.get(i+1));
							
							if (run.cards.size() > 2)
								runs.add(run);
							
							run = new Run();
						}
				}
			}else
				break;
		}
		
		int biggestRun = 0;
		
		for (Run r : runs){
			if (r.cards.size() > biggestRun){
				bonusRun.clear();
				bonusRun.add(r);
				biggestRun = r.cards.size();
			}
		}
		
		//for (Run r : runs)
			//for (Card c : r.cards)
			//System.out.println("runs "+c.getValue());
	}
	
	public void orderPoints() {
		
		for (int i = 0; i <= cards.size() - 1; i++){
			order[i] = false;
			partialOrder[i] = false;
		}
		
		order[0] = true;

		for (int i = 0; i < cards.size() - 1; i++) {
			if (cards.get(i).getValue() < cards.get(i + 1).getValue()){
				order[i] = true;
				
				if (i == (cards.size() - 2)) {
					order[i+1] = true;
					Racko_Main.game.gameStatus = GameStatus.END_HAND;
				}
			}else{
				order[i] = true;
				break;
			}
		}

		for (int i = 0; i <= cards.size() - 1; i++) {
			if (i == 0){
				if (cards.get(i).getValue() < cards.get(i + 1).getValue())
					partialOrder[i] = true;
			}
			else if (i == cards.size() - 1){
				if (cards.get(i).getValue() > cards.get(i - 1).getValue())
					partialOrder[i] = true;
			}
			else if ((cards.get(i).getValue() < cards.get(i + 1).getValue()) && (cards.get(i).getValue() > cards.get(i - 1).getValue())){
				partialOrder[i] = true;
			}
		}
		
		setProxy();
	}
	
	public void setProxy() {
		for (int i = 0; i < cards.size() - 1; i++){
			proxy[i] = false;
		}
		
		for (int i = 0; i <= cards.size() - 1; i++) {
			if ((cards.get(i).getValue() <= (((i+1) * inc) + prox)) && (cards.get(i).getValue() >= (((i+1) * inc) - prox))){
				proxy[i] = true;
			}
		}
	}
	
	public boolean isOrder(int i, int value) {
		if (value < cards.get(i + 1).getValue())
			return true;
		
		return false;
	}
	
	public boolean isPartialOrder(int i, int value) {
		if (i == 0){
			if (value < cards.get(i + 1).getValue())
				return true;
		}
		else if (i == cards.size() - 1){
			if (value > cards.get(i - 1).getValue())
				return true;
		}
		else if ((value < cards.get(i + 1).getValue()) && (value > cards.get(i - 1).getValue())){
			return true;
		}
		
		return false;
	}
	
	public boolean isProxy(Card card, int value) {
		int i = cards.indexOf(card);
		
		if ((value <= (((i+1) * inc) + prox)) && (value >= (((i+1) * inc) - prox)))
			return true;
		
		return false;
	}
	
	public int getProxy(int i) {
		return ((i+1) * inc);
	}
	
	public void createClumps() {
		boolean isFirstIndex = true;
		Clump clump = new Clump();
		clumps.clear();
		cardValuesNeeded.clear();
		
		for (int i = 0; i <= cards.size() - 1; i++){
			if ((i + 1) < cards.size()){
				if ((isPartialOrder(i, cards.get(i).getValue())) || (isOrder(i, cards.get(i).getValue()))){
					clump.cards.add(cards.get(i));
					
					if (isFirstIndex){
						clump.setLowIndex(i);
						clump.setLowValue(cards.get(i).getValue());
						isFirstIndex = false;
					}
					
					continue;
				}
			}

			clump.cards.add(cards.get(i));
			clump.setHighIndex(i);
			clump.setHighValue(cards.get(i).getValue());
			
			if (clump.cards.size() > 1){
				for (Card card : clump.cards){
					if (isProxy(card, card.getValue())){
						clump.setTargetValue(this);
						clump.setAdjacentDensity();
						clumps.add(clump);
						break;
					}
				}
			}
			
			clump = new Clump();
			isFirstIndex = true;
		}
		
		/*
		for (int i = 1; i < clumps.size(); i++){
			Clump tempClump = clumps.get(i-1);
			
			if (tempClump.getHighValue() < clumps.get(i).getLowValue()){
				for (int ii = tempClump.getHighValue()+1; ii < clumps.get(i).getLowValue(); ii++)
					cardValuesNeeded.add(ii);
			}else if (tempClump.getHighValue() > clumps.get(i).getLowValue()){
				for (int ii = tempClump.getHighValue()+1; ii < clumps.get(i).getLowValue(); ii++)
					cardValuesNeeded.add(ii);
			}
		}
		
		for (int i = 1; i < clumps.size(); i++){
			Clump tempClump = clumps.get(i-1);
			
			for (int ii = tempClump.getHighValue()+1; ii < clumps.get(i).getLowValue(); ii++){
				cardValuesNeeded.add(ii);
			}
		}
		*/
		
		//String strings = "clumps ";
		//String string = "clump ";
		for (Clump cl: clumps){
			//System.out.println("-----");
			//System.out.println(strings);
			//string = cl.toString()+" targetValue = "+cl.getTargetValue()+" index "+cl.targetIndex+" lowvalue "+cl.getLowValue()+" highvalue "+cl.getHighValue();//" adjacentDensity = "+cl.getAdjacentDensity();
			//System.out.println(string);
		}
		
		//System.out.println("cardValuesNeeded ");
		//for (int i = 0; i < cardValuesNeeded.size(); i++)
		//System.out.print(", "+cardValuesNeeded.get(i));
		
	}

	public void setSlotProbabilities(){
		CopyOnWriteArrayList<Card> memoryCardsLower = new CopyOnWriteArrayList<Card>();
		CopyOnWriteArrayList<Card> memoryCardsHigher = new CopyOnWriteArrayList<Card>();
		
		for (int i = 0; i < cards.size() - 1; i++){
			memoryCardsLower.clear();
			memoryCardsHigher.clear();
			
			for (Player p: Racko_Main.game.player){
				if (p.hand == this)
					continue;
				
				for (Card c: p.hand.cards)
					if (c.getValue() < cards.get(i).getValue())
						memoryCardsLower.add(c);
					else
						memoryCardsHigher.add(c);
			}
			
			for (Card c: Racko_Main.game.deck.discards)
				if (c.getValue() < cards.get(i).getValue())
					memoryCardsLower.add(c);
				else
					memoryCardsHigher.add(c);
			
			slotLower[i] = (cards.get(i).getValue() - memoryCardsLower.size()) / Racko_Main.game.getNumCards();
			slotHigher[i] = (cards.get(i).getValue() - memoryCardsHigher.size()) / Racko_Main.game.getNumCards();
		}
	}
	
	public void setSlotHigher(){
		
	}

	public void swap(Card in, Card out){
		
		out.explodeRadius = .0001;
		out.explodeTran = 1.0f;
		out.explodePenR = .01f;
		out.explodeGraphic = true;
		
		
		if (in == Racko_Main.game.deck.deck.get(Racko_Main.game.deck.deck.size()-1))
    		Racko_Main.game.deck.deck.remove(in);
    	else
    		Racko_Main.game.deck.discards.remove(in);

    	cards.add(cards.indexOf(out), in);
    	cards.remove(out);
    	Racko_Main.game.deck.discards.add(out);
    	
    	in.setX(out.getX()+5);
    	in.setY(out.getY()+15);
    	in.setTargetX(out.getX());
    	in.setTargetY(out.getY());
    	in.reset(in.getTargetX(), in.getTargetY(), 5);
    	in.setHasTarget(true);
    	in.setFaceUp(true);
    	
    	out.setTargetX(Racko_Main.game.getDiscardX() + (0.5 + (2.0 - 0.5) * rnd.nextDouble()) - (0.5 + (2.0 - 0.5) * rnd.nextDouble()));
    	out.setTargetY(Racko_Main.game.getDiscardY() + (0.5 + (2.0 - 0.5) * rnd.nextDouble()) - (0.5 + (2.0 - 0.5) * rnd.nextDouble()));
    	out.reset(out.getTargetX(), out.getTargetY(), 10);
    	out.setHasTarget(true);
    	out.setFaceUp(true);
	}
	
	public void discard(Card discard){
		discard.setTargetX(Racko_Main.game.getDiscardX() + (0.5 + (2.0 - 0.5) * rnd.nextDouble()) - (0.5 + (2.0 - 0.5) * rnd.nextDouble()));
		discard.setTargetY(Racko_Main.game.getDiscardY() + (0.5 + (2.0 - 0.5) * rnd.nextDouble()) - (0.5 + (2.0 - 0.5) * rnd.nextDouble()));
		discard.setHasTarget(true);
		discard.reset(discard.getTargetX(), discard.getTargetY(), 20);

        Racko_Main.game.deck.discards.add(discard);
        Racko_Main.game.deck.deck.remove(discard);
        discard.setFaceUp(true);

        if (Racko_Main.game.deck.deck.isEmpty())
       	 	Racko_Main.game.deck.shuffleDiscardsIntoDeck();
	}
	
	public int getModScore() {
		modScore = 0;
		for (int i = 0; i <= order.length - 1; i++) {
			if (order[i]){
				modScore += 2;
				continue;
			}
			if (partialOrder[i]){
				//modScore += 1;
				//continue;
			}
		}
		
		//System.out.println("modScore"+modScore);
		
		return modScore;
	}

	public int getModScore(int index, Card card) {
		int modScore = 0;
		ArrayList<Card> modCards = new ArrayList<Card>(cards);
		boolean[] order = new boolean[size];
		boolean[] partialOrder = new boolean[size];
        
		order[0] = true;
		
		modCards.set(index, card);
		
		for (int i = 0; i < modCards.size() - 1; i++) {
			if (modCards.get(i).getValue() < modCards.get(i + 1).getValue()){
				order[i] = true;
			}else{
				order[i] = true;
				break;
			}
		}

		for (int i = 0; i <= modCards.size() - 1; i++) {
			if (i == 0){
				if (modCards.get(i).getValue() < modCards.get(i + 1).getValue())
					partialOrder[i] = true;
			}
			else if (i == modCards.size() - 1){
				if (modCards.get(i).getValue() > modCards.get(i - 1).getValue())
					partialOrder[i] = true;
			}
			else if ((modCards.get(i).getValue() < modCards.get(i + 1).getValue()) && (modCards.get(i).getValue() > modCards.get(i - 1).getValue())){
				partialOrder[i] = true;
			}
		}
		
		for (int i = 0; i <= order.length - 1; i++) {
			if (order[i]){
				modScore += 2;	
				continue;
			}
			if (partialOrder[i]){
				//modScore += 1;
				//continue;
			}
		}
		
		//System.out.println("TEMPmodScore"+modScore);
		return modScore;
	}
	
	public String toString(){
		String string = "";
		//for (Card c: cards)
			//string = string+", "+c.getValue();
		
		return string;
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

	public int getPlayer() {
		return player;
	}

	public void setPlayer(int player) {
		this.player = player;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public int getInc() {
		return inc;
	}

	public void setInc(int inc) {
		this.inc = inc;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getProx() {
		return prox;
	}

	public void setProx(int prox) {
		this.prox = prox;
	}

	public ArrayList<Run> getBonusRun() {
		return bonusRun;
	}

	public void setBonusRun(ArrayList<Run> bonusRun) {
		this.bonusRun = bonusRun;
	}
	
}
