import java.awt.Color;

public class Button {
	
	private Color color = new Color(0,0,0);
	private Color highlightColor = (Color.ORANGE);
	private Color defaultColor = new Color(50,50,50);
	private Color SelectedColor = new Color(150,220,50);
	private Color borderColor = new Color(150,150,150);
	private Color textColor = new Color(200,200,200);
	private double x, y, w, h;
	private float transparency = 0.0f;
	private float transparencyRate = .0015f;
	private boolean visible = false, enabled = true;
	private String message;
	private int r, g, b;
	private int rr, gg, bb;
	private boolean selected;
	private boolean highlighted;
    
	public Button(){
	
	}
	
	public void setupButton(Color color, double x, double y, double w, double h, String message){
		this.color = color;
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.message = message;
	}
	
	public void draw() {
		
		if (!visible)
			return;
		
		Racko_Main.gameBoard.drawButton(this);
    }
	
	public void select(){
		highlighted = true;
		borderColor = Color.WHITE;
	}

	public void deselect(){
		highlighted = false;
		borderColor = Color.GRAY;
	}

	public void highlight(){
		highlighted = true;
	}

	public void unhighlight(){
		highlighted = false;
	}
	
	public void setHighlightColor(Color highlightColor) {
		this.highlightColor = highlightColor;
	}

	public void resetRGB(){
		this.r = 0;
		this.g = 0;
		this.b = 0;
	}
	
	public void resetRRGGBB(){
		this.rr = 0;
		this.gg = 0;
		this.bb = 0;
	}
	
	public boolean isHighlighted() {
		return highlighted;
	}

	public void setHighlighted(boolean highlighted) {
		this.highlighted = highlighted;
	}

	public int getRR() {
		return rr;
	}

	public void setRR(int rr) {
		this.rr += rr;
	}

	public int getGG() {
		return gg;
	}

	public void setGG(int gg) {
		this.gg += gg;
	}

	public int getBB() {
		return bb;
	}

	public void setBB(int bb) {
		this.bb += bb;
	}

	public int getR() {
		return r;
	}

	public void setR(int r) {
		this.r += r;
	}

	public int getG() {
		return g;
	}

	public void setG(int g) {
		this.g += g;
	}

	public int getB() {
		return b;
	}

	public void setB(int b) {
		this.b += b;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public Color getColor() {
		return color;
	}

	public Color getHighlightColor() {
		return highlightColor;
	}
	
	public void setColor(Color color) {
		this.color = color;
	}

	public void setHighlightColor() {
		this.highlightColor = new Color(r, g, b);
	}
	
	public void setTextColor() {
		this.textColor = new Color(rr, gg, bb);
	}
	
	public void setTextColor(Color color) {
		this.textColor = color;
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

	public double getW() {
		return w;
	}

	public void setW(double w) {
		this.w = w;
	}

	public double getH() {
		return h;
	}

	public void setH(double h) {
		this.h = h;
	}

	public Color getTextColor() {
		return textColor;
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
	}

	public float getTransparencyRate() {
		return transparencyRate;
	}

	public void setTransparencyRate(float transparencyRate) {
		this.transparencyRate = transparencyRate;
	}

	public Color getDefaultColor() {
		return defaultColor;
	}

	public void setDefaultColor(Color defaultColor) {
		this.defaultColor = defaultColor;
	}

	public Color getSelectedColor() {
		return SelectedColor;
	}

	public void setSelectedColor(Color selectedColor) {
		SelectedColor = selectedColor;
	}

	public Color getBorderColor() {
		return borderColor;
	}

	public void setBorderColor(Color borderColor) {
		this.borderColor = borderColor;
	}

	public Button mouseMoved() {
		if (!enabled)
			return null;
		
		if ((Racko_Main.gameBoard.mouseX() > getX() - getW()) && (Racko_Main.gameBoard.mouseX() < getX() + getW()))
         	if ((Racko_Main.gameBoard.mouseY() > getY() - getH()) && (Racko_Main.gameBoard.mouseY() < getY() + getH()))
         		return this;
	
		return null;	
	}
	
}
