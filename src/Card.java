import java.awt.Color;
import java.util.ArrayList;

public class Card {
	private double x, y;
	private double h, w;
	private int value;
	private boolean faceUp;
	private int r, g, b;
    private double vx, vy;    // velocity
    private double targetX, targetY;
    private double tarX, tarY;
	private double speed;
	private double totalDist, dist;
	private double mag;
	private float tran;
	private boolean hasTarget;
	private Color defaultColor = new Color(60, 200, 60); // 0,120,220
	private Color color = new Color(100, 100, 100);
	private Color faceColor = new Color(255, 255, 255); // 0,120,220
	private double speedRate;
	private boolean isSelected;
	private boolean isHighlighted;
	
	public double explodeRadius = .00001;
	public float explodeTran = 1.0f;
	public float explodePenR = .0185f;
	public boolean explodeGraphic = false;
	
	ArrayList<Trail> trail = new ArrayList<Trail>();
	
	public Card(){
		
	}
	
    public Card(double x, double y, double h, double w, int value, int red, int green, int blue) {
        this.x = x;
        this.y = y;
        this.h = h;
        this.w = w;
        this.value = value;
    }
    
    public void draw() {
    	for (Trail t: this.trail){
    		t.setH(t.getH()-.3);
    		t.setW(t.getW()-.3);
    		t.setTrans(t.getTrans()-.1f);
    		if ((t.getH() <= 0) || (t.getW() <= 0) || (t.getTrans() <= 0)){
    			this.trail.remove(t);
    			break;
    		}
    	}
    	
    	for (int i = 0; i < trail.size(); i++){
    		trail.get(i).draw();
    	}
    	
    	Racko_Main.gameBoard.drawCard(x, y, w, h, faceUp, color, faceColor, value, isHighlighted);
    	
    	if (explodeGraphic)
    		drawSelectGraphic();
    }

    public void reset(double targetX, double targetY, double rate){
		tarX = targetX;
		tarY = targetY;
			
		totalDist = Math.sqrt(((targetX-x) * (targetX-x)) + ((targetY-y) * (targetY-y)));
		
		tran = 1.0f;
		speed = rate;
		speedRate = rate;
		
		trail.clear();
	}
    
    public void moveToTarget(){
    	//if (this.trail.size() < 2){
    	//	Trail trail = new Trail();
    	//	trail.setup(color, this.getX(), this.getY(), this.getW(), this.getH(), 1.0f);
    	//	this.trail.add(trail);
    	//}
    	
		if (totalDist <= 0)
			return;

    	dist = Math.sqrt(((tarX-x) * (tarX-x)) + ((tarY-y) * (tarY-y)));
    	
    	if (dist <= 2){
    		totalDist = 0;
    		hasTarget = false;
    		x = tarX;
    		y = tarY;
    		return;
    	}
    		
    	vx = tarX - x;
    	vy = tarY - y;
    	mag = Math.sqrt(vx * vx + vy * vy);
    	
    	speed = ((dist*speedRate) / totalDist);
    	vx = vx * speed / mag;
    	vy = vy * speed / mag;
    	
    	x += vx;
    	y += vy;
	}

    public void drawSelectGraphic(){
		//StdDraw.drawBloom(x, y, explodeRadius*3, .5f);
    	if ((explodeTran >= 0.0f) && (explodeTran <= 1.0f))
    		Racko_Main.gameBoard.drawSelect(x, y+6, explodeRadius, explodeTran, explodePenR);
		explodeRadius += .6;
        explodeTran -= .0575;
        //explodePenR -= .00065;
        if (explodeTran <= 0.0)
        	explodeGraphic = false;
        if (explodePenR <= 0.0)
        	explodeGraphic = false;
	}
    
	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
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

	public double getH() {
		return h;
	}

	public void setH(double h) {
		this.h = h;
	}

	public double getW() {
		return w;
	}

	public void setW(double w) {
		this.w = w;
	}

	public boolean isFaceUp() {
		return faceUp;
	}

	public void setFaceUp(boolean faceUp) {
		this.faceUp = faceUp;
	}

	public double getTargetX() {
		return targetX;
	}

	public void setTargetX(double targetX) {
		this.targetX = targetX;
	}

	public double getTargetY() {
		return targetY;
	}

	public void setTargetY(double targetY) {
		this.targetY = targetY;
	}

	public boolean isHasTarget() {
		return hasTarget;
	}

	public void setHasTarget(boolean hasTarget) {
		this.hasTarget = hasTarget;
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

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}
    
	public boolean isHighlighted() {
		return isHighlighted;
	}

	public void setHighlighted(boolean isHighlighted) {
		this.isHighlighted = isHighlighted;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public Color getDefaultColor() {
		return defaultColor;
	}

	public void setDefaultColor(Color defaultColor) {
		this.defaultColor = defaultColor;
	}

	public Color getFaceColor() {
		return faceColor;
	}

	public void setFaceColor(Color faceColor) {
		this.faceColor = faceColor;
	}
	
}
