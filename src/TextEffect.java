import java.awt.Color;
import java.awt.Font;
import java.util.Random;

public class TextEffect {
	private Color actualColor = new Color(255, 255, 255);
	private Color setColor = new Color(100, 100, 100);
	private double x, y;
	private double xv, yv;
	private boolean visible = true;
	private String message;
	private int r = 255, g = 255, b = 255;
	private int rr = 100, gg = 100, bb = 100;
	private float tran = 1.0f;
	private int countDown = 0;
	private int fontSize;
	Font font;
	
	Random rnd = new Random();
	
	public TextEffect(){

	}
	
	public TextEffect(double x, double y, String message){
		this.x = x;
		this.y = y;
		this.message = message;
		
		xv = .2; //(0.01 + (0.05 - 0.01) * rnd.nextDouble());
		yv = .1; //(0.05 + (0.15 - 0.05) * rnd.nextDouble());
		fontSize = 40;
		countDown = 10;
	}
	
	public void draw(){
		//if (tran >= 1.0f)
			font = new Font("Square721 BT", Font.BOLD, fontSize);
		//else
			//font = new Font("Square721 BT", Font.PLAIN, fontSize);
		Racko_Main.gameBoard.TextEffect(x, y, message, actualColor, tran, font);
	}

	public void calculate(){
		
		if (((r + 5) > rr) && ((r - 5) < rr))
			r = rr;
		if (r > rr)
			r-=5;
		else if (r < rr)
			r+=5;
		
		if (((g + 5) > gg) && ((g - 5) < gg))
			g = gg;
		if (g > gg)
			g-=5;
		else if (g < gg)
			g+=5;
		
		if (((b + 5) > bb) && ((b - 5) < bb))
			b = bb;
		if (b > bb)
			b-=5;
		else if (b < bb)
			b+=5;
		
		setActualColor(r, g, b);
		
		//if (countDown == 0)
			//tran = .5f;
		
		//if ((tran <= .5f) && (tran > 0.0f))
			//tran -= .01;
		
		//tran -= .01;
		
		//if (tran < 0.0f)
			//tran = 0.0f;
		
		//if (tran < 0.7f)
			//y += yv;
		//x -= xv;

		//if (countDown >= 0)
			//countDown--;
		
		if (fontSize > 40)
			fontSize-=3;
	}
	
	public Color getActualColor() {
		return actualColor;
	}

	public void setActualColor(int r, int g, int b) {
		actualColor = new Color(r, g, b);
	}

	public void setActualColor(Color color) {
		r = color.getRed();
		g = color.getGreen();
		b = color.getBlue();
		actualColor = color;
		
	}
	
	public Color getSetColor() {
		return setColor;
	}

	public void setSetColor(int r, int g, int b) {
		setColor = new Color(r, g, b);
	}
	
	public void setSetColor(Color color) {
		rr = color.getRed();
		gg = color.getGreen();
		bb = color.getBlue();
		this.setColor = new Color(rr, gg, bb);
	}
	
	public int getFontSize() {
		return fontSize;
	}

	public void setFontSize(int fontSize) {
		this.fontSize = fontSize;
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

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
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

	public float getTran() {
		return tran;
	}

	public void setTran(float tran) {
		this.tran = tran;
	}
	
	
}
