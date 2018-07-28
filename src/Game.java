import java.awt.Color;
import java.awt.Shape;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CopyOnWriteArrayList;

public class Game {

	// draw top deck card
	// give cards from back
	// bonus scoring
	// num of cards = 40, 50, 60
	// num of players = 2, 3, 4
	// prox = 5, 7, 9
	
	private int numPlayers = 2;
	private int numPlayersAi = 0;
	private int numCards = 40;
	private int handSize = 10;
	
	private float tranInc = .0015f;
	
	private int playerTurn;
	private int playerStartInc = 0;
	private int inc;
	private int timerInc;
	private int timerIncX;
	private int numHands;
	private int discardX, discardY;
	
	private Card selectedCard;
	private Card highlightedCard;
	
	private byte endGameStage = 0;
	private boolean bonusMode = false;
	public boolean sound = true;
	
	Random rnd = new Random();
	
	GameStatus gameStatus = null;
	
	Menu menu = new Menu(0, 55, 1, 11);
	public Button Continue = new Button();
	
	ArrayList<Player> player = new ArrayList<Player>();
	//CopyOnWriteArrayList<TextEffect> textEffect = new CopyOnWriteArrayList<TextEffect>();
	Deck deck = new Deck();

	Timer timerAi;
	Timer timerDealHand;
	Timer timerEndHand;
	Timer timerEndGame;
	Timer timerMenu;
	int dealHandDelay = 1500, dealHandPeriod = 100; // 1500, 100
	int endHandDelay = 250, endHandPeriod = 150; // 250, 150
	int endGameDelay = 1000, endGamePeriod = 200; // 1000, 200
	int aiDelay = 200, aiPeriod = 100; // 200, 100
	int menuDelay = 1, menuPeriod = 1;

	public Game() {

	}
	
	public void init(){
		gameStatus = GameStatus.PRE_GAME;
		createPlayers(numPlayers, numPlayersAi);
		createDeck(numCards);
		setTable();
		
		Continue.setDefaultColor(menu.getColorHeader());
		Continue.setHighlightColor(Color.ORANGE);
		Continue.setBorderColor(Color.WHITE);
		Continue.setX(menu.getX()+10);
		Continue.setY(menu.getY()+menu.getHeight()-1);
		Continue.setW(8);
		Continue.setH(6);
		Continue.setMessage("C o n t i n u e");
		Continue.setTextColor(Color.WHITE);
		Continue.setHighlighted(false);
		Continue.setVisible(false);
		
		menu.setVisible(true);
		timerMenu = new Timer();
		TimerMenu();
		
		while (gameStatus != null)
			drawGame();
	}

	public void reInit(){
		createPlayers(numPlayers, numPlayersAi);
		createDeck(numCards);
		setTable();
		deck.shuffleDeck();
		
		if (Racko_Main.game.sound)
			SoundEffect.Place.play();
	}
	
	private void createPlayers(int numPlayers, int numPlayersAi) {
		player.clear();
		for (int i = 0; i < numPlayers; i++){
			if (i == 0){
				if (numPlayersAi >= 1){
					Ai temp = new AiOne(150, 150, 200, " AI P1", true);
					player.add(temp);
				}else{
					Player temp = new Player(150, 150, 200, "   P1", false);
					player.add(temp);
				}
			}
			else if (i == 1){
				if (numPlayersAi >= 2){
					Ai temp = new AiOne(150, 150, 200, " AI P2", true);
					player.add(temp);
				}else{
					Player temp = new Player(150, 150, 200, "   P2", false);
					player.add(temp);
				}
			}
			else if (i == 2){
				if (numPlayersAi >= 3){
					Ai temp = new AiOne(150, 150, 200, " AI P3", true);
					player.add(temp);
				}else{
					Player temp = new Player(150, 150, 200, "   P3", false);
					player.add(temp);
				}
			}
			else if (i == 3){
				if (numPlayersAi >= 4){
					Ai temp = new AiOne(150, 150, 200, " AI P4", true);
					player.add(temp);
				}else{
					Player temp = new Player(150, 150, 200, "   P4", false);
					player.add(temp);
				}
			}
		}
		
		 for (Player p: player){
			 p.hand.setInc(numCards / 10);
			 p.hand.setValue(4);
		 }
	}

	private void createDeck(int numCards) {
		this.deck = new Deck(60, 88, numCards);
		deck.setupCards(numCards);
		discardX = (int) (deck.getX() / 2);
		discardY = (int) deck.getY();
	}

	private void setTable() {
		for (int p = 0; p < numPlayers; p++){
			if (p == 0) {
				player.get(p).createHand(14, 28, handSize, player.get(p).hand.getInc(), 9);
				for (int i = player.get(p).hand.getSize() - 1; i > -1; i--) {
					player.get(p).hand.locX[i] = player.get(p).hand.getX();
					player.get(p).hand.locY[i] = player.get(p).hand.getY() + (i * 4);
				}
			}
			if (p == 1) {
				player.get(p).createHand(player.get(p-1).hand.getX() + 25, 28, handSize, player.get(p).hand.getInc(), 9);
				for (int i = player.get(p).hand.getSize() - 1; i > -1; i--) {
					player.get(p).hand.locX[i] = player.get(p).hand.getX();
					player.get(p).hand.locY[i] = player.get(p).hand.getY() + (i * 4);
				}
			}
			if (p == 2) {
				player.get(p).createHand(player.get(p-1).hand.getX() + 25, 28, handSize, player.get(p).hand.getInc(), 9);
				for (int i = player.get(p).hand.getSize() - 1; i > -1; i--) {
					player.get(p).hand.locX[i] = player.get(p).hand.getX();
					player.get(p).hand.locY[i] = player.get(p).hand.getY() + (i * 4);
				}
			}
			if (p == 3) {
				player.get(p).createHand(player.get(p-1).hand.getX() + 25, 28, handSize, player.get(p).hand.getInc(), 9);
				for (int i = player.get(p).hand.getSize() - 1; i > -1; i--) {
					player.get(p).hand.locX[i] = player.get(p).hand.getX();
					player.get(p).hand.locY[i] = player.get(p).hand.getY() + (i * 4);
				}
			}
			
			player.get(p).createText();
		}

	}
	
	public void startGame(){
		timerMenu = new Timer();
		TimerMenu();
		
		setPlayerTurn(0);
		
		deck.shuffleDeck();
		timerDealHand = new Timer();
		TimerDealHand();
		
		if (Racko_Main.game.sound)
			SoundEffect.EndTurn.play();
	}
	
	private void drawGame(){
			
		Racko_Main.gameBoard.clear();
		Racko_Main.gameBoard.setPenRadius(.01);

		Racko_Main.gameBoard.drawDiscardPile(discardX, discardY, 10, 8);
		
		deck.draw();
		
		if (!menu.isVisible()){
			for (int p = 0; p < numPlayers; p++){
				player.get(p).calculateText();
				player.get(p).drawText();
				player.get(p).hand.draw();
				//if ((gameStatus == GameStatus.END_HAND) || (gameStatus == GameStatus.END_GAME)){
					//if (!player.get(p).isAi())
						player.get(p).hand.drawOrder(p);
				//}
			}
		}

		updateCardLocation();
		
		if ((gameStatus == GameStatus.END_HAND) || (gameStatus == GameStatus.END_GAME)){
			for (int ii = 0; ii < Racko_Main.numParticles; ii++) {
				if (!Racko_Main.gameBoard.particle[ii].isAlive())
					continue;
				
				if (Racko_Main.gameBoard.particle[ii].isHasTarget())
					Racko_Main.gameBoard.particle[ii].moveToTarget();
				Racko_Main.gameBoard.particle[ii].draw();
			}
			
			Continue.draw();
		}
		
		if ((gameStatus == GameStatus.PRE_GAME) || (gameStatus == GameStatus.END_GAME))
			menu.draw();
		
		Racko_Main.gameBoard.show(1); // 60 frames per second
	}

	private void updateCardLocation(){
		for (Card c: deck.deck)
			if (c.isHasTarget())
				c.moveToTarget();
		
		for (Card c: deck.discards)
			if (c.isHasTarget())
				c.moveToTarget();
		
		for (Player p: player){
			for (Card c: p.hand.cards)
				if (c.isHasTarget())
					c.moveToTarget();
		}
	}
	
	public void endTurn() {
		if (deck.discards.isEmpty())
			deck.discard(deck.deck.get(deck.deck.size()-1));
		
		if (playerTurn > -1){
			player.get(playerTurn).hand.orderPoints();
			player.get(playerTurn).hand.drawOrder(playerTurn);
			if (bonusMode)
				player.get(playerTurn).hand.findRun();
		}
		
		if (playerTurn > -1)
			if ((player.get(playerTurn).isAi()) && (timerAi != null)){
				timerAi.cancel();
				if (deck.deck.isEmpty())
  	            	 deck.shuffleDiscardsIntoDeck();
			}
		
		if (gameStatus == GameStatus.END_HAND){
			player.get(playerTurn).hand.drawOrder(playerTurn);
			endHand(player.get(playerTurn));
			return;
		}
		
		if (playerTurn >= 0){
			for (Card c: player.get(playerTurn).hand.cards)
				c.setFaceUp(false);
			
			player.get(playerTurn).txtPlayerId.setSetColor(Color.GRAY);
			player.get(playerTurn).txtGameScore.setSetColor(Color.GRAY);
			player.get(playerTurn).txtHandScore.setSetColor(Color.GRAY);
		}
		
		playerTurn++;
		if (playerTurn >= numPlayers)
			playerTurn = 0;
		
		if (!player.get(playerTurn).isAi()){
			
			player.get(playerTurn).txtPlayerId.setSetColor(Color.WHITE);
			player.get(playerTurn).txtGameScore.setSetColor(Color.WHITE);
			player.get(playerTurn).txtHandScore.setSetColor(Color.WHITE);
			player.get(playerTurn).hand.orderPoints();
			player.get(playerTurn).setMoveCountHand(1);
			
			Racko_Main.gameBoard.popUp("Player "+ (playerTurn+1));
		}
		
		for (int p = 0; p < numPlayers; p++)
			if ((playerTurn == p) && (!player.get(p).isAi()))
				for (Card c: player.get(p).hand.cards)
					c.setFaceUp(true);
		
		if (!player.get(playerTurn).isAi())
			return;

		timerAi = new Timer();
		TimerAi();
	}
	
	private void endHand(Player winner) {
		int score = 0;

		numHands++;
		
		for (int p = 0; p < numPlayers; p++) {
			
			player.get(p).txtPlayerId.setSetColor(Color.GRAY);
			player.get(p).txtGameScore.setSetColor(Color.GRAY);
			player.get(p).txtHandScore.setSetColor(Color.GRAY);
			
			if (player.indexOf(winner) == p){
				player.get(p).setWins(1);
				player.get(p).setMoveCountGame(player.get(p).getMoveCountHand());
				player.get(p).setMoveCountHand(-player.get(p).getMoveCountHand());
				double avg = player.get(p).getMoveCountGame() / player.get(p).getWins();
				player.get(p).setMoveCountAvg(avg);
				
				//player.get(p).txtPlayerId.setSetColor(Color.WHITE);
				//player.get(p).txtGameScore.setSetColor(Color.WHITE);
				//player.get(p).txtHandScore.setSetColor(Color.WHITE);
			}
			
			player.get(p).setMoveCountHand(-player.get(p).getMoveCountHand());
					
					player.get(p).setScore(-player.get(p).getScore());
					score = 0;
					if (player.indexOf(winner) == p)
						continue;
					int c = 0;
					while (player.get(p).hand.order[c]) {
						score += 5;
						c++;
					}
					player.get(p).setScore(score);
		}
		winner.setScore(75);
		
		if (bonusMode){
			if (!winner.hand.bonusRun.isEmpty()){
				if (winner.hand.bonusRun.get(0).cards.size() == 3)
					winner.setScore(50);
				else if (winner.hand.bonusRun.get(0).cards.size() == 4)
					winner.setScore(100);
				else if (winner.hand.bonusRun.get(0).cards.size() == 5)
					winner.setScore(200);
				else if (winner.hand.bonusRun.get(0).cards.size() >= 6)
					winner.setScore(400);
			}
		}
		
		for (Player p : player)
			p.txtHandScore.setMessage("+["+p.getScore()+"]");
		
		playerTurn = 0;
		endGameStage = 1;
		timerInc = 0;
		
		//player.get(playerTurn).txtPlayerId.setSetColor(Color.LIGHT_GRAY);
		//player.get(playerTurn).txtGameScore.setSetColor(Color.LIGHT_GRAY);
		//player.get(playerTurn).txtHandScore.setSetColor(Color.LIGHT_GRAY);
		
		timerEndHand = new Timer();
		TimerEndHand(winner);
	}
	
	private void TimerMenu() {
		timerMenu.scheduleAtFixedRate(new TimerTask() {
			public void run() {
				
				if (gameStatus == GameStatus.PRE_GAME){
					if (tranInc > 0){
						if ((menu.getWidth() < 70) && (menu.getWidth() > 0)){
							menu.setWidth(menu.getWidth() + (tranInc*100));
						
							return;
						}
					
						if ((menu.getTransparency() <= 1.0f) && (menu.getTransparency() >= 0.0f))
							menu.setTransparency(tranInc);
					
						if (menu.getTransparency() >= 1.0f){
							tranInc *= -1;
							timerMenu.cancel();
						}
					}else if (tranInc < 0){
						if (menu.getTransparency() > 0.0f) {
							menu.setTransparency(tranInc);
						
							return;
						}
			        
						if (menu.getWidth() > 0)
							menu.setWidth(menu.getWidth() + (tranInc*100));

						if (menu.getWidth() <= 0){
							tranInc *= -1;
							menu.setWidth(1);
							menu.setVisible(false);
			        	
							for (Card c: deck.deck){
								c.setColor(c.getDefaultColor());
								c.setFaceColor(Color.WHITE);
							}
							for (Card c: deck.discards){
								c.setColor(c.getDefaultColor());
								c.setFaceColor(Color.WHITE);
							}
							for (Player p: player)
								for (Card c: p.hand.cards){
									c.setColor(c.getDefaultColor());
									c.setFaceColor(Color.WHITE);
								}
							
							timerMenu.cancel();
						}
					}
				} else if ((gameStatus == GameStatus.END_HAND) || (gameStatus == GameStatus.END_GAME) || (gameStatus == GameStatus.GAME_ON)){
					if (Continue.getTransparencyRate() > 0){
						if (Continue.getTransparency() <= 1.0f)
							Continue.setTransparency(Continue.getTransparencyRate());
					
						if (Continue.getTransparency() >= 1.0f){
							Continue.setTransparencyRate(Continue.getTransparencyRate() * -1);
							timerMenu.cancel();
						}
					}else if (Continue.getTransparencyRate() < 0){
						if (Continue.getTransparency() > 0.0f)
							Continue.setTransparency(Continue.getTransparencyRate());

						if (Continue.getTransparency() <= 0.0f){
							Continue.setTransparencyRate(Continue.getTransparencyRate() * -1);
							Racko_Main.game.Continue.setVisible(false);
							
							for (Card c: deck.deck){
								c.setColor(c.getDefaultColor());
								c.setFaceColor(Color.WHITE);
							}
							for (Card c: deck.discards){
								c.setColor(c.getDefaultColor());
								c.setFaceColor(Color.WHITE);
							}
							for (Player p: player)
								for (Card c: p.hand.cards){
									c.setColor(c.getDefaultColor());
									c.setFaceColor(Color.WHITE);
								}
							
							timerMenu.cancel();
						}
					}
				}
				
			}
		}, menuDelay, menuPeriod);
	}
	
	private void TimerDealHand() {
		timerDealHand.scheduleAtFixedRate(new TimerTask() {
			public void run() {
				
				if (player.get(playerTurn).hand.cards.size() < player.get(playerTurn).hand.getSize()){
					deck.deck.get(deck.deck.size()-1).setTargetX(player.get(playerTurn).hand.locX[inc]);
					deck.deck.get(deck.deck.size()-1).setTargetY(player.get(playerTurn).hand.locY[inc]);
					deck.deck.get(deck.deck.size()-1).reset(deck.deck.get(deck.deck.size()-1).getTargetX(), deck.deck.get(deck.deck.size()-1).getTargetY(), 12);
					deck.deck.get(deck.deck.size()-1).setHasTarget(true);
					deck.deck.get(deck.deck.size()-1).setFaceUp(false);
					player.get(playerTurn).hand.cards.add(deck.deck.get(deck.deck.size()-1));
					deck.deck.remove(deck.deck.size()-1);
				}
				
				if (playerTurn < numPlayers - 1)
					playerTurn++;
				else
					playerTurn = 0;
				
				if (playerTurn == playerStartInc)
					inc++;
				
				if ((player.get(playerTurn).hand.cards.size() == player.get(playerTurn).hand.getSize()) && (playerTurn == playerStartInc)) {
					gameStatus = GameStatus.GAME_ON;
					inc = -1;

					playerTurn = playerStartInc - 1;
					
					for (int i = 0; i < player.size() - 1; i++)
						player.get(i).hand.orderPoints();
					endTurn();
					
					timerDealHand.cancel();
				}
				
			}
		}, dealHandDelay, dealHandPeriod);
	}
	
	private void TimerAi() {
		timerAi.scheduleAtFixedRate(new TimerTask() {
			public void run() {

				Ai p = (Ai) player.get(playerTurn);
				Card discardCard = null;
				Card deckCard = deck.deck.get(deck.deck.size() - 1);
				Card swapCard;
				
				if (!deck.discards.isEmpty())
					discardCard = deck.discards.get(deck.discards.size() - 1);
			
				p.hand.orderPoints();
				p.hand.createClumps();
				p.cardMemory.clear();
				p.cardMemory.addAll(deck.deck);
				p.setCardsNeeded();
				//p.hand.setSlotProbabilities();

				if (discardCard != null){
					swapCard = p.getSwapCard(discardCard, true);
					
					if (swapCard != null){
						
						p.hand.swap(discardCard, swapCard);
						
						endTurn();
						return;
					}
				}
				
				swapCard = p.getSwapCard(deckCard, false);
					
				if (swapCard != null){
					p.hand.swap(deckCard, swapCard);
					endTurn();
					return;
				}
				
				p.hand.discard(deckCard);
				p.hand.orderPoints();
				endTurn();
			}
		}, aiDelay, aiPeriod);
	}
	
	public void NewHand(){
		if (timerEndHand != null)
			timerEndHand.cancel();
		
		for (int ii = 0; ii < Racko_Main.numParticles; ii++) 
			Racko_Main.gameBoard.particle[ii].setAlive(false);
		
		for (Player p : player)
			p.hand.bonusRun.clear();
		
		for (Card c: deck.deck){
			c.setColor(c.getDefaultColor());
			c.setFaceColor(Color.WHITE);
		}
		for (Card c: deck.discards){
			c.setColor(c.getDefaultColor());
			c.setFaceColor(Color.WHITE);
		}
		for (Player p: player)
			for (Card c: p.hand.cards){
				c.setColor(c.getDefaultColor());
				c.setFaceColor(Color.WHITE);
			}
		
		deck.shuffleCardsIntoDeck();

		if (playerStartInc < numPlayers - 1)
			playerStartInc++;
		else
			playerStartInc = 0;
		
		playerTurn = playerStartInc;
		
		inc = 0;
		timerDealHand = new Timer();
		TimerDealHand();
		gameStatus = GameStatus.GAME_ON;
		
		timerMenu = new Timer();
		TimerMenu();
	}
	
	public void NewGame(){
		if (timerEndHand != null)
			timerEndHand.cancel();
		if (timerMenu != null)
			timerMenu.cancel();
		
		gameStatus = GameStatus.PRE_GAME;
		
		for (int ii = 0; ii < Racko_Main.numParticles; ii++) 
			Racko_Main.gameBoard.particle[ii].setAlive(false);
		
		for (Player p: player){
			p.setTotalScore(-p.getTotalScore());
			p.setWins(-p.getWins());
			p.setMoveCountGame(-p.getMoveCountGame());
			p.setMoveCountHand(-p.getMoveCountHand());
			p.setMoveCountAvg(0);
			for (Card card: p.hand.cards){
				card.setFaceColor(Color.WHITE);
				card.setColor(card.getDefaultColor());
			}
		}
		
		deck.shuffleCardsIntoDeck();
		
		playerStartInc = -1;
		playerTurn = 0;
		
		menu.setVisible(true);
		
		Continue.setTransparencyRate(Continue.getTransparencyRate() * -1);
		Racko_Main.game.Continue.setVisible(false);
		
		timerMenu = new Timer();
		TimerMenu();
	}
	
	private void TimerEndHand(Player winner) {
		timerEndHand.scheduleAtFixedRate(new TimerTask() {
			public void run() {
				
				if (endGameStage == 1){
					
					if (timerInc < 10){
						winner.hand.cards.get(timerInc).setFaceUp(true);
				
						for (int i = timerInc*5; i < (timerInc+1)*5; i++) {
							Racko_Main.gameBoard.particle[i].setStartX(winner.hand.getX());
							Racko_Main.gameBoard.particle[i].setStartY(winner.hand.locY[timerInc] + 5);
							Racko_Main.gameBoard.particle[i].setAlive(true);
							Racko_Main.gameBoard.particle[i].setHasTarget(true);
							Racko_Main.gameBoard.particle[i].reset(0, Racko_Main.gameBoard.particle[i].getTargetX(), Racko_Main.gameBoard.particle[i].getTargetY());
							inc += .75;
							
							if (sound)
								SoundEffect.BulletFire.play();
						}
					}
					
					timerInc++;
				
					if (timerInc == 20){
						endGameStage = 2;
						playerTurn = 0;
						for (Player p: player)
							for (int i = 0; i < 10; i++)
								p.hand.order[i] = false;
					
						timerInc = -1;
					}
				}
				
				if (endGameStage == 2){
					timerInc++;
					
					for (Card c: player.get(playerTurn).hand.cards)
						c.setFaceUp(true);
				
					if (player.get(playerTurn).getScore() == 0)
						timerInc = 10;
					
						if (timerInc == 9){
							
							player.get(playerTurn).txtHandScore.setMessage("+["+player.get(playerTurn).getScore()+"]");
							player.get(playerTurn).txtGameScore.setActualColor(Color.WHITE);
							player.get(playerTurn).txtGameScore.setFontSize(46);
							
							player.get(playerTurn).setTotalScore(30);
							player.get(playerTurn).setScore(-30);
							player.get(playerTurn).hand.order[timerInc] = true;
							
							player.get(playerTurn).txtHandScore.setMessage("+["+player.get(playerTurn).getScore()+"]");
							player.get(playerTurn).txtGameScore.setMessage(""+player.get(playerTurn).getTotalScore());
							
							if (sound)
								SoundEffect.Alert.play();

						} else if (timerInc < 9){
							
							player.get(playerTurn).txtHandScore.setMessage("+["+player.get(playerTurn).getScore()+"]");
							player.get(playerTurn).txtGameScore.setActualColor(Color.WHITE);
							player.get(playerTurn).txtGameScore.setFontSize(46);
							
							player.get(playerTurn).setTotalScore(5);
							player.get(playerTurn).setScore(-5);
							player.get(playerTurn).hand.order[timerInc] = true;
						
							player.get(playerTurn).txtHandScore.setMessage("+["+player.get(playerTurn).getScore()+"]");
							player.get(playerTurn).txtGameScore.setMessage(""+player.get(playerTurn).getTotalScore()); // +" "+player.get(playerTurn).getMoveCountAvg()+" "+player.get(playerTurn).getWins()
							
							if (sound)
								SoundEffect.particleTwo.play();
						}

					if (timerInc >= 10) {
						endGameStage = 3;
						timerInc = -1;
					}
				}
				
				if (endGameStage == 3){
					timerInc++;
					
					if (player.get(playerTurn) != winner)
						timerInc = 12;	
						
					if ((timerInc == 2 ) && (player.get(playerTurn) == winner)){
						if (!player.get(playerTurn).hand.bonusRun.isEmpty()){
							for (Card card : player.get(playerTurn).hand.bonusRun.get(0).cards){
								card.setColor(Color.ORANGE);
								
								player.get(playerTurn).txtHandScore.setMessage("+["+winner.getScore()+"]");
								player.get(playerTurn).txtGameScore.setSetColor(Color.WHITE);
								
								winner.setTotalScore(winner.getScore());
								winner.setScore(-winner.getScore());
								
								player.get(playerTurn).txtHandScore.setMessage("+["+winner.getScore()+"]");
								player.get(playerTurn).txtGameScore.setMessage(""+player.get(playerTurn).getTotalScore());
								
								if (sound)
									SoundEffect.UpgradeBase.play();

							}
						}
						else
							timerInc = 12;
					}
					
					if ((timerInc == 12) && (playerTurn < numPlayers - 1)){
						player.get(playerTurn).txtPlayerId.setSetColor(Color.GRAY);
						player.get(playerTurn).txtGameScore.setSetColor(Color.GRAY);
						player.get(playerTurn).txtHandScore.setSetColor(Color.GRAY);
						player.get(playerTurn).txtHandScore.setMessage("");
						
						playerTurn++;
						endGameStage = 2;
						timerInc = -1;
						
						//player.get(playerTurn).txtPlayerId.setSetColor(Color.LIGHT_GRAY);
						//player.get(playerTurn).txtGameScore.setSetColor(Color.LIGHT_GRAY);
						//player.get(playerTurn).txtHandScore.setSetColor(Color.LIGHT_GRAY);
					}
					else if ((timerInc == 12) && (playerTurn == numPlayers - 1)){
						endGameStage = 4;
						
						player.get(playerTurn).txtPlayerId.setSetColor(Color.GRAY);
						player.get(playerTurn).txtGameScore.setSetColor(Color.GRAY);
						player.get(playerTurn).txtHandScore.setSetColor(Color.GRAY);
						player.get(playerTurn).txtHandScore.setMessage("");
					}
				}
				
				if (endGameStage == 4){
					Player gameWinner = getHighScore();
					System.out.println("high score "+gameWinner.getTotalScore() + " / "+Racko_Main.scoreWin);
					if (gameWinner.getTotalScore() >= Racko_Main.scoreWin){
						gameStatus = GameStatus.END_GAME;
						
						//System.out.println("p1 "+player.get(0).getMoveCountAvg());
						//System.out.println("p2 "+player.get(1).getMoveCountAvg());
						//System.out.println("p3 "+player.get(2).getMoveCountAvg());
						//System.out.println("p4 "+player.get(3).getMoveCountAvg());
						
						gameWinner.txtGameScore.setActualColor(Color.WHITE);
						gameWinner.txtGameScore.setSetColor(Color.ORANGE);
						
						endGameStage = 5;
						timerInc = 0;
						timerIncX = -1;
						playerTurn = -1;
						
						for (Player p: player)
							for (int i = 0; i < 10; i++)
								p.hand.order[i] = false;
						
						timerEndGame = new Timer();
						TimerEndGame(gameWinner);
						
						timerEndHand.cancel();
						if (numPlayersAi == numPlayers){
							Racko_Main.newGame();
							
							if (sound)
								SoundEffect.GameOver.play();
							return;
						}
						
						Continue.setVisible(true);
						for (Card c: deck.deck){
							c.setColor(Color.DARK_GRAY);
							c.setFaceColor(Color.GRAY);
						}
						for (Card c: deck.discards){
							c.setColor(Color.DARK_GRAY);
							c.setFaceColor(Color.GRAY);
						}
						for (Player pl: player)
							for (Card c: pl.hand.cards){
								c.setColor(Color.DARK_GRAY);
								c.setFaceColor(Color.GRAY);
							}
						timerMenu = new Timer();
						TimerMenu();
						return;
					}

					if (numPlayersAi == numPlayers){
						timerEndHand.cancel();
						NewHand();
						return;
					}
					
					for (Player p: player)
						for (int i = 0; i < 10; i++)
							p.hand.order[i] = false;
					
					playerTurn = -1;
					Continue.setVisible(true);
					for (Card c: deck.deck){
						c.setColor(Color.DARK_GRAY);
						c.setFaceColor(Color.GRAY);
					}
					for (Card c: deck.discards){
						c.setColor(Color.DARK_GRAY);
						c.setFaceColor(Color.GRAY);
					}
					for (Player pl: player)
						for (Card c: pl.hand.cards){
							c.setColor(Color.DARK_GRAY);
							c.setFaceColor(Color.GRAY);
						}
					timerMenu = new Timer();
					TimerMenu();
					
					timerEndHand.cancel();
				}

			}
		}, endHandDelay, endHandPeriod);
	}
	
	private void TimerEndGame(Player gameWinner) {
		timerEndGame.scheduleAtFixedRate(new TimerTask() {
			public void run() {
				Card card;
				Card cardX = null;
				
				if (endGameStage == 5){
					
					//for (int i = timerInc*5; i < (timerInc+1)*5; i++) {
					//	Racko_Main.gameBoard.particle[i].setStartX(gameWinner.hand.getX());
					//	Racko_Main.gameBoard.particle[i].setStartY(gameWinner.hand.locY[timerInc] + 5);
					//	Racko_Main.gameBoard.particle[i].setAlive(true);
					//	Racko_Main.gameBoard.particle[i].setHasTarget(true);
					//	Racko_Main.gameBoard.particle[i].reset(0, Racko_Main.gameBoard.particle[i].getTargetX(), Racko_Main.gameBoard.particle[i].getTargetY());
					//	inc += .75;
					//}
					
					
					//card = gameWinner.hand.cards.get(timerInc);
					//if (timerIncX >= 0)
					//	cardX = gameWinner.hand.cards.get(timerIncX);
					
					//card.setColor(Color.WHITE);
					//card.setFaceColor(Color.ORANGE);
					//card.setTargetX(card.getX());
					//card.setTargetY(card.getY() + 1);
					//card.reset(card.getTargetX(), card.getTargetY(), .5);
					//card.setHasTarget(true);
					
					//if (cardX != null){
						//cardX.setColor(Color.DARK_GRAY);
					//	cardX.setTargetX(cardX.getX());
					//	cardX.setTargetY(cardX.getY() - 1);
					//	cardX.reset(cardX.getTargetX(), cardX.getTargetY(), .5);
					//	cardX.setHasTarget(true);
					//}
					
					timerInc++;
					timerIncX++;
				}
				
				if (timerInc == gameWinner.hand.cards.size())
					timerInc = 0;
				if (timerIncX == gameWinner.hand.cards.size()){
					timerIncX = 0;
					endGameStage = 6;
				}
				
				if (endGameStage == 5)
					return;
				
				timerEndGame.cancel();
				
			}
		}, endGameDelay, endGamePeriod);
	}
	
	private Player getHighScore(){
		Player gameWinner = null;
		int highScore = 0;
		
		for (Player p: player)
			if (p.getTotalScore() > highScore){
				gameWinner = p;
				highScore = p.getTotalScore();
			}
		
		return gameWinner;
	}
	
	public int getPlayerTurn() {
		return playerTurn;
	}

	private void setPlayerTurn(int i) {
		this.playerTurn = i;
	}

	public Card getSelectedCard() {
		return selectedCard;
	}

	public void setSelectedCard(Card selectedCard) {
		this.selectedCard = selectedCard;
	}

	public Card getHighlightedCard() {
		return highlightedCard;
	}

	public void setHighlightedCard(Card highlightedCard) {
		this.highlightedCard = highlightedCard;
	}
	
	public int getDiscardX() {
		return discardX;
	}

	public void setDiscardX(int discardX) {
		this.discardX = discardX;
	}

	public int getDiscardY() {
		return discardY;
	}

	public void setDiscardY(int discardY) {
		this.discardY = discardY;
	}

	public int getNumCards() {
		return numCards;
	}

	public void setNumCards(int numCards) {
		this.numCards = numCards;
	}

	public int getNumPlayers() {
		return numPlayers;
	}

	public void setNumPlayers(int numPlayers) {
		this.numPlayers = numPlayers;
	}

	public int getNumPlayersAi() {
		return numPlayersAi;
	}

	public void setNumPlayersAi(int numPlayersAi) {
		this.numPlayersAi = numPlayersAi;
	}

	public boolean isBonusMode() {
		return bonusMode;
	}

	public void setBonusMode(boolean bonusMode) {
		this.bonusMode = bonusMode;
		
		if (Racko_Main.game.sound)
			SoundEffect.Place.play();
	}
	
}
