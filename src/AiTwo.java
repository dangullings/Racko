
public class AiTwo extends Ai{
	
	public AiTwo(int r, int g, int b, String name, boolean ai){
		super(b, b, b, name, ai);
	}

	@Override
	public Card getSwapCard(Card playCard, boolean isDiscard){
		int modScore = hand.getModScore();
		int high = 0;
		int bestIndex = -1;
		
		if ((playCard.getValue() > hand.getProxy(hand.cards.size() - 3)) && (playCard.getValue() > hand.cards.get(hand.cards.size() - 1).getValue())){
			//System.out.println("swap last card");
			return hand.cards.get(hand.cards.size() - 1);
		}
		
		if ((playCard.getValue() < hand.getProxy(1)) && (playCard.getValue() < hand.cards.get(0).getValue()) && (playCard.getValue() < hand.cards.get(1).getValue())){
			//System.out.println("swap first card");
			return hand.cards.get(0);
		}
		
		for (int i = 0; i < hand.cards.size() - 1; i++){
			int tempModScore = hand.getModScore(i, playCard);
		
			if (((tempModScore - modScore) > high) && (hand.isProxy(hand.cards.get(i), playCard.getValue()))){
				high = (tempModScore - modScore);
				bestIndex = i;
			}
		}

		if (bestIndex != -1){
			//System.out.println("best Index");
			return hand.cards.get(bestIndex);
		}
	
		if (isDiscard)
			return null;
		
		int loww = Racko_Main.game.getNumCards();
		int distt = Racko_Main.game.getNumCards();
		Clump chosenClump = null;
		
		for (Clump clump: hand.clumps){
			distt = Math.abs(playCard.getValue() - clump.getTargetValue());
			
			if (distt < loww){
				loww = distt;
				chosenClump = clump;
			}
		}
		
		if (!hand.clumps.isEmpty()){
			hand.clumps.set(hand.clumps.indexOf(chosenClump), hand.clumps.get(0));
			hand.clumps.set(0, chosenClump);
		}
		
			for (Clump clump: hand.clumps){
				
				for (int i = clump.targetIndex-1; i >= clump.getLowIndex(); i--){
					int maxProxy = (clump.targetIndex - i) * 8;
					int dist = Math.abs(playCard.getValue() - clump.getTargetValue());
					if ((playCard.getValue() > hand.cards.get(i).getValue()) && (playCard.getValue() < clump.getTargetValue())){
						if (dist <= maxProxy){
							//System.out.println("clump up");
							return hand.cards.get(i);
						}
					}
				}

				for (int i = clump.targetIndex + 1; i <= clump.getHighIndex(); i++){
					int maxProxy = (clump.targetIndex + i) * 8;
					int dist = Math.abs(playCard.getValue() - clump.getTargetValue());
					if ((playCard.getValue() < hand.cards.get(i).getValue()) && (playCard.getValue() > clump.getTargetValue())){
						if (dist <= maxProxy){
							//System.out.println("clump down");
							return hand.cards.get(i);
						}
					}
				}
				
				int ii = clump.targetIndex;
				int dist1 = Math.abs(hand.getProxy(ii) - playCard.getValue());
				int dist2 = Math.abs(hand.getProxy(ii) - hand.cards.get(ii).getValue());
					
				if (dist2 > dist1){
					//System.out.println("clump target " + ii);
					return hand.cards.get(ii);
				}
				
			}
	
		int low = 10;
		int dist = 10;
		int index = -1;
		
		for (int i = 0; i <= hand.cards.size() - 1; i++){
			if ((!hand.isProxy(hand.cards.get(i), hand.cards.get(i).getValue())) && (hand.isProxy(hand.cards.get(i), playCard.getValue())))
				dist = Math.abs(hand.getProxy(i) - playCard.getValue());
				
			if (dist < low){
				index = i;
				low = dist;
			}
		}
		
		if (index != -1){
			//System.out.println("proxy swap");
			return hand.cards.get(index);
		}
		
		return null;
	}
	
	@Override
	public Card getSwapCardTest(Card playCard, boolean isDiscard){
		int modScore = hand.getModScore();
		int high = 0;
		int bestIndex = -1;
		
		for (int i = 1; i < hand.cards.size() - 1; i++){
			if ((hand.isProxy(hand.cards.get(i-1), hand.cards.get(i-1).getValue())) && (hand.isProxy(hand.cards.get(i+1), hand.cards.get(i+1).getValue()))){
				if ((hand.cards.get(i).getValue() < hand.cards.get(i-1).getValue()) || (hand.cards.get(i).getValue() > hand.cards.get(i+1).getValue())){
					if ((playCard.getValue() > hand.cards.get(i-1).getValue()) && (playCard.getValue() < hand.cards.get(i+1).getValue())){
						//System.out.println("swap middle card");
						return hand.cards.get(i);
					}
				}
				
			}
		}
		
		if ((playCard.getValue() > hand.getProxy(hand.cards.size() - 3)) && (playCard.getValue() > hand.cards.get(hand.cards.size() - 1).getValue())){
			//System.out.println("swap last card");
			return hand.cards.get(hand.cards.size() - 1);
		}
		
		if ((playCard.getValue() < hand.getProxy(1)) && (playCard.getValue() < hand.cards.get(0).getValue()) && (playCard.getValue() < hand.cards.get(1).getValue())){
			//System.out.println("swap first card");
			return hand.cards.get(0);
		}
		
		for (int i = 0; i < hand.cards.size() - 1; i++){
			int tempModScore = hand.getModScore(i, playCard);
		
			if (((tempModScore - modScore) > high) && (hand.isProxy(hand.cards.get(i), playCard.getValue()))){
				high = (tempModScore - modScore);
				bestIndex = i;
			}
		}

		if (bestIndex != -1){
			//System.out.println("best Index");
			return hand.cards.get(bestIndex);
		}
	
		if (isDiscard)
			return null;
		
		int loww = Racko_Main.game.getNumCards();
		int distt = Racko_Main.game.getNumCards();
		Clump chosenClump = null;
		
		for (Clump clump: hand.clumps){
			distt = Math.abs(playCard.getValue() - clump.getTargetValue());
			
			if (distt < loww){
				loww = distt;
				chosenClump = clump;
			}
		}
		
		if (!hand.clumps.isEmpty()){
			hand.clumps.set(hand.clumps.indexOf(chosenClump), hand.clumps.get(0));
			hand.clumps.set(0, chosenClump);
		}
		
			for (Clump clump: hand.clumps){
				
				for (int i = clump.targetIndex-1; i >= clump.getLowIndex(); i--){
					int maxProxy = (clump.targetIndex - i) * 8;
					int dist = Math.abs(playCard.getValue() - clump.getTargetValue());
					if ((playCard.getValue() > hand.cards.get(i).getValue()) && (playCard.getValue() < clump.getTargetValue())){
						if (dist <= maxProxy){
							//System.out.println("clump up");
							return hand.cards.get(i);
						}
					}
				}

				for (int i = clump.targetIndex + 1; i <= clump.getHighIndex(); i++){
					int maxProxy = (clump.targetIndex + i) * 8;
					int dist = Math.abs(playCard.getValue() - clump.getTargetValue());
					if ((playCard.getValue() < hand.cards.get(i).getValue()) && (playCard.getValue() > clump.getTargetValue())){
						if (dist <= maxProxy){
							//System.out.println("clump down");
							return hand.cards.get(i);
						}
					}
				}
				
				int ii = clump.targetIndex;
				int dist1 = Math.abs(hand.getProxy(ii) - playCard.getValue());
				int dist2 = Math.abs(hand.getProxy(ii) - hand.cards.get(ii).getValue());
					
				if (dist2 > dist1){
					//System.out.println("clump target " + ii);
					return hand.cards.get(ii);
				}
				
			}
	
		int low = 10;
		int dist = 10;
		int index = -1;
		
		for (int i = 0; i <= hand.cards.size() - 1; i++){
			if ((!hand.isProxy(hand.cards.get(i), hand.cards.get(i).getValue())) && (hand.isProxy(hand.cards.get(i), playCard.getValue())))
				dist = Math.abs(hand.getProxy(i) - playCard.getValue());
				
			if (dist < low){
				index = i;
				low = dist;
			}
		}
		
		if (index != -1){
			//System.out.println("proxy swap");
			return hand.cards.get(index);
		}
		
		return null;
	}
	
}