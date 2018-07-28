import java.awt.Color;
import java.util.concurrent.CopyOnWriteArrayList;


public class Player {
	private Color color;
	private int r, g, b;
	private boolean Ai;
	private String name;
	private double moveCountHand;
	private double moveCountGame;
	private double moveCountAvg;
	private double wins;
	private int score;
	private int totalScore;
	
	Hand hand = new Hand();
	TextEffect txtPlayerId = new TextEffect();
	TextEffect txtGameScore = new TextEffect();
	TextEffect txtHandScore = new TextEffect();
	
    public Player(int r, int g, int b, String name, boolean ai) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.name = name;
        this.Ai = ai;
    }
	
    public Player() {
		// TODO Auto-generated constructor stub
	}

	public void createHand(double x, double y, int size, int inc, int prox){
    	this.hand = new Hand(x, y, size, inc, prox);
    }
    
    public void createText(){
    	txtPlayerId = new TextEffect(hand.getX()-4, hand.locY[0]-18, name);//"P"+(Racko_Main.game.player.indexOf(this)+1));
    	txtGameScore = new TextEffect(hand.getX()-3, hand.locY[0]-13, ""+getTotalScore());
    	txtHandScore = new TextEffect(hand.getX()+2, hand.locY[0]-13, ""+getScore());
    }
    
    public void calculateText(){
    	txtPlayerId.calculate();
		txtGameScore.calculate();
		txtHandScore.calculate();
    }
    
    public void drawText(){
    	txtPlayerId.draw();
		txtGameScore.draw();
		
		if (Racko_Main.game.gameStatus == GameStatus.END_HAND)
			txtHandScore.draw();
    }
    
	public double getMoveCountAvg() {
		return moveCountAvg;
	}

	public void setMoveCountAvg(double moveCountAvg) {
		this.moveCountAvg = moveCountAvg;
	}

	public Color getColor() {
		return color;
	}

	public void setColor() {
		this.color = new Color (r, g, b);
	}

	public int getR() {
		return r;
	}

	public void setR(int r) {
		this.r = r;
	}

	public int getG() {
		return g;
	}

	public void setG(int g) {
		this.g = g;
	}

	public int getB() {
		return b;
	}

	public void setB(int b) {
		this.b = b;
	}

	public boolean isAi() {
		return Ai;
	}

	public void setAi(boolean ai) {
		Ai = ai;
	}

	public double getMoveCountHand() {
		return moveCountHand;
	}

	public void setMoveCountHand(double moveCountHand) {
		this.moveCountHand += moveCountHand;
	}

	public double getMoveCountGame() {
		return moveCountGame;
	}

	public void setMoveCountGame(double moveCountGame) {
		this.moveCountGame += moveCountGame;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score += score;
	}

	public int getTotalScore() {
		return totalScore;
	}

	public void setTotalScore(int totalScore) {
		this.totalScore += totalScore;
	}

	public double getWins() {
		return wins;
	}

	public void setWins(double wins) {
		this.wins += wins;
	}
	
}
