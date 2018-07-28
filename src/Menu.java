import java.awt.Color;
import java.awt.event.MouseEvent;

public class Menu {
	
	private double x;
	private double y;
	private double width;
	private double height;
	private boolean visible;
	
	private float transparency = 0.0f;
	
	private Color colorHeader = new Color(150, 220, 0);
	private Color colorMain = new Color(60, 200, 60);
	
	Button START = new Button();
	
	Button P2 = new Button();
	Button P3 = new Button();
	Button P4 = new Button();
	
	Button AI1 = new Button();
	Button AI2 = new Button();
	Button AI3 = new Button();
	Button AI4 = new Button();
	
	Button CLASSIC = new Button();
	Button BONUS = new Button();
	
	public Menu(int x, int y, int w, int h){
		this.x = x;
		this.y = y;
		this.width = w;
		this.height = h;
		
		setMenu();
	}
	
	public void draw(){
		if (!visible)
			return;
		
		Racko_Main.gameBoard.drawMenu(this);
		P2.draw();
		P3.draw();
		P4.draw();
		
		AI1.draw();
		AI2.draw();
		AI3.draw();
		AI4.draw();
		
		CLASSIC.draw();
		BONUS.draw();
		
		START.draw();
	}
	
	public void selectButton(Button button){
		
		if (button.equals(START)){
			Racko_Main.game.startGame();
		}else if (button.equals(CLASSIC)){
			Racko_Main.game.setBonusMode(false);
			button.select();
			BONUS.deselect();
			Racko_Main.game.setBonusMode(false);
		}else if (button.equals(BONUS)){
     		button.select();
     		CLASSIC.deselect();
			Racko_Main.game.setBonusMode(true);
		}else if (button.equals(P2)){
			button.select();
    		P3.deselect();
    		P4.deselect();
    		AI3.setEnabled(false);
    		AI4.setEnabled(false);
    		AI1.setHighlighted(false);
 			AI2.setHighlighted(false);
			AI3.setHighlighted(false);
			AI4.setHighlighted(false);
			
			Racko_Main.game.setNumPlayers(2);
    		Racko_Main.game.setNumCards(40);
    		Racko_Main.game.setNumPlayersAi(0);
		}else if (button.equals(P3)){
			button.select();
    		P2.deselect();
    		P4.deselect();
    		AI3.setEnabled(true);
    		AI4.setEnabled(false);
    		AI1.setHighlighted(false);
 			AI2.setHighlighted(false);
			AI3.setHighlighted(false);
			AI4.setHighlighted(false);
			
			Racko_Main.game.setNumPlayers(3);
    		Racko_Main.game.setNumCards(50);
    		Racko_Main.game.setNumPlayersAi(0);
		}else if (button.equals(P4)){
			button.select();
    		P3.deselect();
    		P2.deselect();
    		AI3.setEnabled(true);
    		AI4.setEnabled(true);
    		AI1.setHighlighted(false);
 			AI2.setHighlighted(false);
			AI3.setHighlighted(false);
			AI4.setHighlighted(false);
			
			Racko_Main.game.setNumPlayers(4);
    		Racko_Main.game.setNumCards(60);
    		Racko_Main.game.setNumPlayersAi(0);
		}else if (button.equals(AI1)){
			if (button.isHighlighted()){
	 			Racko_Main.game.setNumPlayersAi(0);
	 			button.deselect();
	 		}else{
	 			button.select();
	 			AI2.deselect();
	 			AI3.deselect();
	 			AI4.deselect();
				Racko_Main.game.setNumPlayersAi(1);
	 		}
		}else if (button.equals(AI2)){
			if (button.isHighlighted()){
	 			Racko_Main.game.setNumPlayersAi(0);
	 			button.deselect();
	 		}else{
	 			button.select();
	 			AI1.deselect();
	 			AI3.deselect();
	 			AI4.deselect();
				Racko_Main.game.setNumPlayersAi(2);
	 		}
		}else if (button.equals(AI3)){
			if (button.isHighlighted()){
	 			Racko_Main.game.setNumPlayersAi(0);
	 			button.deselect();
	 		}else{
	 			button.select();
	 			AI2.deselect();
	 			AI1.deselect();
	 			AI4.deselect();
				Racko_Main.game.setNumPlayersAi(3);
	 		}
		}else if (button.equals(AI4)){
			if (button.isHighlighted()){
	 			Racko_Main.game.setNumPlayersAi(0);
	 			button.deselect();
	 		}else{
	 			button.select();
	 			AI2.deselect();
	 			AI3.deselect();
	 			AI1.deselect();
				Racko_Main.game.setNumPlayersAi(4);
	 		}
		}
		
		Racko_Main.game.reInit();
	}
	
	public void highlightButton(Button button){
		P2.setColor(P2.getDefaultColor());
    	P3.setColor(P3.getDefaultColor());
    	P4.setColor(P4.getDefaultColor());
    	AI1.setColor(AI1.getDefaultColor());
    	AI2.setColor(AI2.getDefaultColor());
    	AI3.setColor(AI3.getDefaultColor());
    	AI4.setColor(AI4.getDefaultColor());
    	CLASSIC.setColor(CLASSIC.getDefaultColor());
    	BONUS.setColor(BONUS.getDefaultColor());
    	START.setColor(START.getDefaultColor());
    	
		button.setColor(button.getHighlightColor());
	}
	
	public void setMenu(){
		START.setColor(Color.DARK_GRAY);
		START.setHighlightColor(Color.ORANGE);
		START.setHighlightColor(colorHeader);
		START.setX(this.getX()+23);
		START.setY(this.getY()-this.getHeight()+2);
		START.setW(7);
		START.setH(3);
		START.setMessage("Start");
		START.setTextColor(Color.WHITE);
		START.setHighlighted(false);
		START.setVisible(true);
        
		P2.setColor(Color.DARK_GRAY);
		P2.setHighlightColor(Color.ORANGE);
		P2.setHighlightColor(colorHeader);
        P2.setX(this.getX()+5);
        P2.setY(this.getY());
        P2.setW(2);
        P2.setH(2);
        P2.setMessage("2");
        P2.setTextColor(Color.WHITE);
        P2.setBorderColor(Color.WHITE);
        P2.setHighlighted(true);
        P2.setVisible(true);
        
        P3.setColor(Color.DARK_GRAY);
        P3.setHighlightColor(Color.ORANGE);
        P3.setHighlightColor(colorHeader);
        P3.setX(P2.getX()+P2.getW()+3);
        P3.setY(this.getY());
        P3.setW(2);
        P3.setH(2);
        P3.setMessage("3");
        P3.setTextColor(Color.WHITE);
        P3.setHighlighted(false);
        P3.setVisible(true);
        
        P4.setColor(Color.DARK_GRAY);
        P4.setHighlightColor(Color.ORANGE);
        P4.setHighlightColor(colorHeader);
        P4.setX(P3.getX()+P3.getW()+3);
        P4.setY(this.getY());
        P4.setW(2);
        P4.setH(2);
        P4.setMessage("4");
        P4.setTextColor(Color.WHITE);
        P4.setHighlighted(false);
        P4.setVisible(true);
        
        AI1.setColor(Color.DARK_GRAY);
        AI1.setHighlightColor(Color.ORANGE);
        AI1.setHighlightColor(colorHeader);
        AI1.setX(P4.getX()+8);
        AI1.setY(this.getY());
        AI1.setW(2);
        AI1.setH(2);
        AI1.setMessage("1");
        AI1.setTextColor(Color.WHITE);
        AI1.setHighlighted(false);
        AI1.setVisible(true);
        
        AI2.setColor(Color.DARK_GRAY);
        AI2.setHighlightColor(Color.ORANGE);
        AI2.setHighlightColor(colorHeader);
        AI2.setX(AI1.getX()+AI1.getW()+3);
        AI2.setY(this.getY());
        AI2.setW(2);
        AI2.setH(2);
        AI2.setMessage("2");
        AI2.setTextColor(Color.WHITE);
        AI2.setHighlighted(false);
        AI2.setVisible(true);
        
        AI3.setColor(Color.DARK_GRAY);
        AI3.setHighlightColor(Color.ORANGE);
        AI3.setHighlightColor(colorHeader);
        AI3.setX(AI2.getX()+AI2.getW()+3);
        AI3.setY(this.getY());
        AI3.setW(2);
        AI3.setH(2);
        AI3.setMessage("3");
        AI3.setTextColor(Color.WHITE);
        AI3.setHighlighted(false);
        AI3.setEnabled(false);
        AI3.setVisible(true);
        
        AI4.setColor(Color.DARK_GRAY);
        AI4.setHighlightColor(Color.ORANGE);
        AI4.setHighlightColor(colorHeader);
        AI4.setX(AI3.getX()+AI3.getW()+3);
        AI4.setY(this.getY());
        AI4.setW(2);
        AI4.setH(2);
        AI4.setMessage("4");
        AI4.setTextColor(Color.WHITE);
        AI4.setHighlighted(false);
        AI4.setEnabled(false);
        AI4.setVisible(true);
        
        CLASSIC.setColor(Color.DARK_GRAY);
        CLASSIC.setHighlightColor(Color.ORANGE);
        CLASSIC.setHighlightColor(colorHeader);
        CLASSIC.setX(AI4.getX()+AI4.getW()+8);
        CLASSIC.setY(this.getY());
        CLASSIC.setW(4);
        CLASSIC.setH(2);
        CLASSIC.setMessage("classic");
        CLASSIC.setTextColor(Color.WHITE);
        CLASSIC.setBorderColor(Color.WHITE);
        CLASSIC.setHighlighted(true);
        CLASSIC.setVisible(true);
        
        BONUS.setColor(Color.DARK_GRAY);
        BONUS.setHighlightColor(Color.ORANGE);
        BONUS.setHighlightColor(colorHeader);
        BONUS.setX(CLASSIC.getX()+CLASSIC.getW()+5);
        BONUS.setY(this.getY());
        BONUS.setW(4);
        BONUS.setH(2);
        BONUS.setMessage("bonus");
        BONUS.setTextColor(Color.WHITE);
        BONUS.setHighlighted(false);
        BONUS.setVisible(true);
	}

	public Button getButton(){
		if (START.mouseMoved() != null)
			return START;
		else if (CLASSIC.mouseMoved() != null)
			return CLASSIC;
		else if (BONUS.mouseMoved() != null)
			return BONUS;
		else if (P2.mouseMoved() != null)
			return P2;
		else if (P3.mouseMoved() != null)
			return P3;
		else if (P4.mouseMoved() != null)
			return P4;
		else if (AI1.mouseMoved() != null)
			return AI1;
		else if (AI2.mouseMoved() != null)
			return AI2;
		else if (AI3.mouseMoved() != null)
			return AI3;
		else if (AI4.mouseMoved() != null)
			return AI4;	
		
		return null;
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

	public double getWidth() {
		return width;
	}

	public void setWidth(double width) {
		this.width = width;
		
		if (this.width > 70)
			this.width = 70;
		
		if (this.width < 0)
			this.width = 0;
	}

	public double getHeight() {
		return height;
	}

	public void setHeight(double height) {
		this.height = height;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public Color getColorHeader() {
		return colorHeader;
	}

	public void setColorHeader(Color colorHeader) {
		this.colorHeader = colorHeader;
	}

	public Color getColorMain() {
		return colorMain;
	}

	public void setColorMain(Color colorMain) {
		this.colorMain = colorMain;
	}

	public float getTransparency() {
		return transparency;
	}

	public void setTransparency(float transparency) {
		this.transparency += transparency;
		
		if (this.transparency > 1.0f)
			this.transparency = 1.0f;
		
		if (this.transparency < 0.0f)
			this.transparency = 0.0f;
		
		P2.setTransparency(transparency);
		P3.setTransparency(transparency);
		P4.setTransparency(transparency);
		AI1.setTransparency(transparency);
		AI2.setTransparency(transparency);
		AI3.setTransparency(transparency);
		AI4.setTransparency(transparency);
		CLASSIC.setTransparency(transparency);
		BONUS.setTransparency(transparency);
		START.setTransparency(transparency);
	}
	
}
