import java.awt.Color;
import java.util.Random;

public class Particle {
    private double x, y;    // position
    private double vx, vy;    // velocity
    private double radius;    // radius
    private double targetX, targetY;
    private double startX, startY, tarX, tarY;
	private double speed;
	private double totalDist, dist;
	private double mag;
	private double distPerc;
	private int targetPlanet;
	private float tran = 1.0f;
	private Color color;      // color
	private boolean alive;
	private boolean hasTarget;
	private int player;
	private double explodeRadius = .00001;
	private float explodeTran = 1.0f;
	private float explodePenR = .0185f;
	private boolean explodeGraphic = false;
	private double bloomR;
	private float bloomT;
	private double distRate;
	
    // create a new particle with given parameters        
    public Particle(double rx, double ry, double vx, double vy, double radius, double mass, Color color, double targetX, double targetY) {
        this.vx = vx;
        this.vy = vy;
        this.x = rx;
        this.y = ry;
        this.radius = radius;
        this.color  = color;
        this.targetX = targetX;
        this.targetY = targetY;
    }
         
    // create a random particle
    public Particle() {
        x     = Math.random();
        y     = Math.random();
        vx     = 0.01 * (Math.random() - 0.5);
        vy     = 0.01 * (Math.random() - 0.5);
        radius = 0.01;
        tran = 1.0f;
        color  = Color.BLACK;
    }

    // draw the particle
    public void draw() {
    	if (!alive)
			return;
    	Racko_Main.gameBoard.setPenRadius(.02);
    	Racko_Main.gameBoard.setPenColor(color);
    	//StdDraw.filledCircle(x, y, radius, tran, color);
    	//if (explodeGraphic)
    		//drawExplodeGraphic();
    	Racko_Main.gameBoard.drawBloom(x, y, radius*bloomR, bloomT, color.getRed(), color.getGreen(), color.getBlue());
    	Racko_Main.gameBoard.setPenColor(Color.WHITE);
    }
    public void reset(int card, double TX, double TY){
		Random rnd = new Random();
		explodeGraphic = true;

		this.color = new Color(rnd.nextInt(254) + 1, rnd.nextInt(254) + 1, rnd.nextInt(254) + 1);
		
		bloomR = 1;
		bloomT = 1.0f;
		distPerc = 1.5;
		x = startX;
		y = startY;

		tarX = startX + (rnd.nextInt(25)+10) - (rnd.nextInt(25)+10);
		tarY = startY + (rnd.nextInt(10)+5) - (rnd.nextInt(10)+5);
		
		if (tarX == startX)
			tarX++;
		if (tarY == startY)
			tarY++;
		
		totalDist = Math.sqrt(((tarX-x) * (tarX-x)) + ((tarY-y) * (tarY-y)));
		
		radius = (0.1 + (0.5 - 0.1) * rnd.nextDouble());
		bloomR = (1.0 + (2.5 - 1.0) * rnd.nextDouble());
		tran = 1.0f;
		distRate = (0.020 + (.060 - 0.020) * rnd.nextDouble());
	}
	
	public void moveToTarget(){
		//if (explodeGraphic)
			//drawExplodeGraphic();
		
		if (!alive)
			return;
		
    	dist = Math.sqrt(((tarX-x) * (tarX-x)) + ((tarY-y) * (tarY-y)));
    	
    	//if (bloomR >= .1)
			//bloomR -= .1;
    	if (bloomT >= .001)
			bloomT -= .001;
    		
    	radius = radius + .012;
    	bloomR = bloomR + .17;
    	bloomT = bloomT - .011f;
    	tran = (tran - .0075f);
    	
    	if (radius <= 0)
    		dist = 0;
    	
    	if (bloomT < 0)
    		bloomT = 0;
    	if (tran < 0)
    		tran = 0;
    	
    	if (bloomT <= 0){
    		
    		//explodeGraphic = true;
    		explodeRadius = .0001;
			explodeTran = 1.0f;
			explodePenR = .01f;
    		//if (bloomR >= 1)
    			//bloomR -= 1;
    		//if (bloomT >= .01)
    			//bloomT -= .01;
			
    		dist = 0;
    		tran = 0f;
    		bloomR = 0;
    		bloomT = 0;
    		radius = 0;
    		alive = false;
    		hasTarget = false;
    	}
    	if (dist == 0)
			return;
    	
    	vx = tarX - x;
    	vy = tarY - y;
    	mag = Math.sqrt(vx * vx + vy * vy);
    	
    	//tran = (float) (distX / totalDistX);
    	speed = (dist / totalDist) + .01;
    	//if (tran > 1.0)
    	//	tran = 1.0f;
    	//if (tran < 0.0)
    		//tran = 0.0f;
    	vx = vx * speed / mag;
    	vy = (vy * speed / mag);

    	vx *= distPerc;
    	vy *= distPerc;
    	
    	if (distPerc > 0){
    		distPerc -= distRate; // .025
    	}
    	
    	x += (vx);
    	y += (vy) - .1;
	}

	public void drawExplodeGraphic(){
		//StdDraw.drawBloom(x, y, explodeRadius*3, .5f, player);
		Racko_Main.gameBoard.drawSelect(x, y, explodeRadius, explodeTran, explodePenR);
		explodeRadius += .015;
        explodeTran -= .02;
        explodePenR -= .0005;
        if (explodeTran <= 0.0)
        	explodeGraphic = false;
        if (explodePenR <= 0.0)
        	explodeGraphic = false;
	}
	
	public int getTargetPlanet() {
		return targetPlanet;
	}

	public void setTargetPlanet(int targetPlanet) {
		this.targetPlanet = targetPlanet;
	}

	public boolean isAlive() {
		return alive;
	}

	public void setAlive(boolean alive) {
		this.alive = alive;
	}

	public boolean isHasTarget() {
		return hasTarget;
	}

	public void setHasTarget(boolean hasTarget) {
		this.hasTarget = hasTarget;
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

	public double getStartX() {
		return startX;
	}

	public void setStartX(double startX) {
		this.startX = startX;
	}

	public double getStartY() {
		return startY;
	}

	public void setStartY(double startY) {
		this.startY = startY;
	}

	public int getPlayer() {
		return player;
	}

	public void setPlayer(int player) {
		this.player = player;
	}

	public boolean isExplodeGraphic() {
		return explodeGraphic;
	}

	public void setExplodeGraphic(boolean explodeGraphic) {
		this.explodeGraphic = explodeGraphic;
	}

	public double getBloomR() {
		return bloomR;
	}

	public void setBloomR(double bloomR) {
		this.bloomR = bloomR;
	}

	public float getBloomT() {
		return bloomT;
	}

	public void setBloomT(float bloomT) {
		this.bloomT = bloomT;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}
	
}