import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.net.*;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.SliderUI;

import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class StdDraw implements ActionListener, MouseListener, MouseMotionListener, ItemListener {
	
	private static Random rnd = new Random();
	
	Particle[] particle = new Particle[Racko_Main.numParticles];
	
	private boolean mousePressed = false;
    private double mouseX = 0;
    private double mouseY = 0;
    private Object mouseLock = new Object();
    
    public Color BOOK_BLUE = new Color(9,90,166);
    
    // current pen color
    private static Color penColor;

    // default canvas size is DEFAULT_SIZE-by-DEFAULT_SIZE
    private final int DEFAULT_SIZE = 512;
    private int width  = DEFAULT_SIZE;
    private int height = DEFAULT_SIZE;

    // default pen radius
    private final double DEFAULT_PEN_RADIUS = 0.001;

    // current pen radius
    private double penRadius;
    
    // show we draw immediately or wait until next show?
    private static boolean defer = false;

    // boundary of drawing canvas, 5% border
    private final double BORDER = 0.00;
    private final double DEFAULT_XMIN = 0.0;
    private final double DEFAULT_XMAX = 1.0;
    private final double DEFAULT_YMIN = 0.0;
    private final double DEFAULT_YMAX = 1.0;
    private double xmin, ymin, xmax, ymax;

    private final Font DEFAULT_FONT = new Font("Square721 BT", Font.BOLD, 10);
    final Font BIG_FONT = new Font("Square721 BT", Font.BOLD, 30);
    final Font BIGGER_FONT = new Font("Square721 BT", Font.BOLD, 70);
    // current font
    private Font font;

    // double buffered graphics
    private BufferedImage offscreenImage, onscreenImage;
    Graphics2D offscreen;

	private Graphics2D onscreen;
    
    static Button endTurn = new Button();
    
    // the frame for drawing to the screen
    JFrame frame;
    //JFrame menu;
    
    //private Button P1 = new Button();
    
    private JMenuBar menuBar;
    private JMenu Bmenu;
    private JMenuItem menuItem;
    private JCheckBoxMenuItem cbMenuItem1;
    
    private JButton cmdOk = new JButton("start game");
    private JLabel lblInfo = new JLabel("     O P T I O N S");
    private JLabel lblQuestion = new JLabel("how many players?");
    private JLabel lblQuestionAi = new JLabel("how many computer players?");
    private JRadioButton One = new JRadioButton("2");
    private JRadioButton Two = new JRadioButton("3");
    private JRadioButton Three = new JRadioButton("4");
    private JRadioButton ZeroAi = new JRadioButton("0");
    private JRadioButton OneAi = new JRadioButton("1");
    private JRadioButton TwoAi = new JRadioButton("2");
    private JRadioButton ThreeAi = new JRadioButton("3");
    private JRadioButton FourAi = new JRadioButton("4");
    
    // singleton pattern: client can't instantiate
    //StdDraw() { super(); }

    public void setCanvasSize() {
        setCanvasSize(DEFAULT_SIZE, DEFAULT_SIZE);
    }

    public void setCanvasSize(int w, int h) {
        if (w < 1 || h < 1) throw new IllegalArgumentException("width and height must be positive");
        width = w;
        height = h;
        init();
    }

    private void init() {
    	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        if (frame != null) frame.setVisible(false);
        frame = new JFrame();
        frame.setLayout(null);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setSize( (int)(screenSize.width), screenSize.height);
        offscreenImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        onscreenImage  = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        offscreen = offscreenImage.createGraphics();
        onscreen  = onscreenImage.createGraphics();
        setXscale();
        setYscale();
        offscreen.setColor(Color.WHITE);
        offscreen.fillRect(0, 0, width, height);
        setPenColor();
        setPenRadius();
        setFont();
        clear();
        
        for (int i = 0; i < Racko_Main.numParticles; i++) 
			this.particle[i] = new Particle(0, 0, 0, 0, 0, 0, Color.WHITE, 0, 0);
    	
        menuBar = new JMenuBar();
		
		Bmenu = new JMenu("Menu");
		Bmenu.setMnemonic(KeyEvent.VK_A);
		Bmenu.getAccessibleContext().setAccessibleDescription(
		        "The only menu in this program that has menu items");
		menuBar.add(Bmenu);

		menuItem = new JMenuItem("New Game");
		menuItem.setMnemonic(KeyEvent.VK_N);
		menuItem.addActionListener(Racko_Main.gameBoard);
		menuItem.setActionCommand("new game");
		Bmenu.add(menuItem);

		menuItem = new JMenuItem("Exit");
		menuItem.setMnemonic(KeyEvent.VK_E);
		menuItem.addActionListener(Racko_Main.gameBoard);
		menuItem.setActionCommand("exit game");
		Bmenu.add(menuItem);
		
		//a group of check box menu items
		Bmenu.addSeparator();
		cbMenuItem1 = new JCheckBoxMenuItem("Sound");
		cbMenuItem1.setMnemonic(KeyEvent.VK_S);
		cbMenuItem1.setActionCommand("Sound");
		cbMenuItem1.setSelected(true);
		cbMenuItem1.addItemListener(Racko_Main.gameBoard);
		Bmenu.add(cbMenuItem1);
		
		frame.setJMenuBar(menuBar);
		
		//menu = new JFrame();
        //menu.setLayout(null);
        //menu.setExtendedState(JFrame.NORMAL);
        //menu.setResizable(false);
        //menu.setUndecorated(true);
        //menu.setOpacity((float) 0.0);
        //menu.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        //menu.setAlwaysOnTop(true);
        //menu.setBounds((-60), screenSize.height/3, (int) 1000, 300);
        //menu.setTitle("Options");
        
        lblInfo.setFont(new Font("Square721 BT", Font.BOLD, 60));
        //lblInfo.setBounds(0, 0, menu.getWidth(), 60);
        lblInfo.setOpaque(true);
        //lblInfo.setBackground(GREEN_HEADER);
        lblInfo.setForeground(Color.WHITE);
        lblInfo.setVisible(true);
        
        lblQuestion.setFont(new Font("Square721 BT", Font.BOLD, 20));
        //lblQuestion.setBounds(((menu.getWidth()/2) - 150), 90, 300, 30);
        lblQuestion.setOpaque(false);
        lblQuestion.setForeground(Color.WHITE);
        lblQuestion.setVisible(true);
        
        lblQuestionAi.setFont(new Font("Square721 BT", Font.BOLD, 20));
        //lblQuestionAi.setBounds(((menu.getWidth()/2) - 150), lblQuestion.getY()+ 70, 400, 30);
        lblQuestionAi.setOpaque(false);
        lblQuestionAi.setForeground(Color.WHITE);
        lblQuestionAi.setVisible(true);

        One.setFont(new Font("Square721 BT", Font.BOLD, 16));
        One.setActionCommand("one");
        One.setBounds(lblQuestion.getX(), lblQuestion.getY()+30, 40, 20);
        One.setOpaque(false);
        One.setSelected(true);
        One.setForeground(Color.WHITE);

        Two.setFont(new Font("Square721 BT", Font.BOLD, 16));
        Two.setActionCommand("two");
        Two.setBounds(One.getX()+One.getWidth(), lblQuestion.getY()+30, 40, 20);
        Two.setOpaque(false);
        Two.setForeground(Color.WHITE);
        
        Three.setFont(new Font("Square721 BT", Font.BOLD, 16));
        Three.setActionCommand("three");
        Three.setBounds(Two.getX()+Two.getWidth(), lblQuestion.getY()+30, 40, 20);
        Three.setOpaque(false);
        Three.setForeground(Color.WHITE);
        
        ZeroAi.setFont(new Font("Square721 BT", Font.BOLD, 16));
        ZeroAi.setActionCommand("zeroai");
        ZeroAi.setBounds(lblQuestionAi.getX(), lblQuestionAi.getY()+30, 40, 20);
        ZeroAi.setOpaque(false);
        ZeroAi.setSelected(true);
        ZeroAi.setForeground(Color.WHITE);
        
        OneAi.setFont(new Font("Square721 BT", Font.BOLD, 16));
        OneAi.setActionCommand("oneai");
        OneAi.setBounds(ZeroAi.getX()+ZeroAi.getWidth(), lblQuestionAi.getY()+30, 40, 20);
        OneAi.setOpaque(false);
        OneAi.setForeground(Color.WHITE);
        
        TwoAi.setFont(new Font("Square721 BT", Font.BOLD, 16));
        TwoAi.setActionCommand("twoai");
        TwoAi.setBounds(OneAi.getX()+OneAi.getWidth(), lblQuestionAi.getY()+30, 40, 20);
        TwoAi.setOpaque(false);
        TwoAi.setForeground(Color.WHITE);
        
        ThreeAi.setFont(new Font("Square721 BT", Font.BOLD, 16));
        ThreeAi.setActionCommand("threeai");
        ThreeAi.setBounds(TwoAi.getX()+TwoAi.getWidth(), lblQuestionAi.getY()+30, 40, 20);
        ThreeAi.setOpaque(false);
        ThreeAi.setVisible(false);
        ThreeAi.setForeground(Color.WHITE);
        
        FourAi.setFont(new Font("Square721 BT", Font.BOLD, 16));
        FourAi.setActionCommand("fourai");
        FourAi.setBounds(ThreeAi.getX()+ThreeAi.getWidth(), lblQuestionAi.getY()+30, 40, 20);
        FourAi.setOpaque(false);
        FourAi.setVisible(false);
        FourAi.setForeground(Color.WHITE);
        
        ButtonGroup group = new ButtonGroup();
        group.add(One);
        group.add(Two);
        group.add(Three);
        
        ButtonGroup groupAi = new ButtonGroup();
        groupAi.add(ZeroAi);
        groupAi.add(OneAi);
        groupAi.add(TwoAi);
        groupAi.add(ThreeAi);
        groupAi.add(FourAi);
        
        groupAi.clearSelection();
        
        One.addActionListener(Racko_Main.gameBoard);
        Two.addActionListener(Racko_Main.gameBoard);
        Three.addActionListener(Racko_Main.gameBoard);
        ZeroAi.addActionListener(Racko_Main.gameBoard);
        OneAi.addActionListener(Racko_Main.gameBoard);
        TwoAi.addActionListener(Racko_Main.gameBoard);
        ThreeAi.addActionListener(Racko_Main.gameBoard);
        FourAi.addActionListener(Racko_Main.gameBoard);
        
        cmdOk.setFont(new Font("Square721 BT", Font.BOLD, 16));
        //cmdOk.setBounds((menu.getWidth()/2)-(130/2), menu.getHeight()-60, 140, 40);
        cmdOk.setActionCommand("ok");
        cmdOk.addActionListener(Racko_Main.gameBoard);
        cmdOk.setVisible(true);
        
        Color newColor = new Color(60, 200, 60);
		//menu.getContentPane().setBackground(newColor);
        //menu.setVisible(true);
        
        //Shape shape = new RoundRectangle2D.Float(0, 0, menu.getWidth(), menu.getHeight(), 40, 40);
        //menu.setShape(shape);
        
        //menu.add(lblQuestion);
        //menu.add(lblQuestionAi);
        //menu.add(One);
        //menu.add(Two);
        //menu.add(Three);
        //menu.add(ZeroAi);
        //menu.add(OneAi);
        //menu.add(TwoAi);
        //menu.add(ThreeAi);
        //menu.add(FourAi);
        //menu.add(lblInfo);
        //menu.add(cmdOk);
        
        // add antialiasing
        RenderingHints hints = new RenderingHints(RenderingHints.KEY_ANTIALIASING,
                                                  RenderingHints.VALUE_ANTIALIAS_ON);
        hints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        offscreen.addRenderingHints(hints);

        // frame stuff
        ImageIcon icon = new ImageIcon(onscreenImage);
        JLabel draw = new JLabel(icon);

        draw.addMouseListener(Racko_Main.gameBoard);
        draw.addMouseMotionListener(Racko_Main.gameBoard);
        frame.setContentPane(draw);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);            // closes all windows
        // frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);      // closes only current window
        frame.setTitle("Racko by Dan Gullings");
        frame.pack();
        frame.requestFocusInWindow();
        frame.setVisible(true);
        frame.setBackground(Color.BLACK);
    }
	    
    public void resetMenu(){

    }
    
    public void popUp(String message){
    	JOptionPane.showMessageDialog(frame, message);
    }
    
    public void drawBloom(double x, double y, double r, float tran, int red, int g, int b){
    	double xs = scaleX(x);
        double ys = scaleY(y);
        double ws = factorX(2*r);
        double hs = factorY(2*r);
        
        Paint p;
        Composite originalComposite = offscreen.getComposite();
        
        p = new RadialGradientPaint(new Point2D.Double(xs,
                ys), (float) (hs / 2.0f),
                new Point2D.Double(xs, ys),
                new float[] { 0.0f, 0.5f },
                new Color[] { new Color(red, g, b, 200),
                    new Color(red, g, b, 0) },
                RadialGradientPaint.CycleMethod.NO_CYCLE,
                RadialGradientPaint.ColorSpaceType.SRGB,
                AffineTransform.getScaleInstance(1.0, 1.0));
        offscreen.setPaint(p);
        offscreen.setComposite(makeComposite((float) tran));
        offscreen.fill(new Ellipse2D.Double(xs - ws/2, (float) (ys-(hs*0.5)), ws-1, hs-1));
        offscreen.setComposite(originalComposite);
    }
    
    public void drawSelect(double x, double y, double r, float tran, float penR){
    	double xs = scaleX(x);
        double ys = scaleY(y);
        double ws = factorX(2*r);
        double hs = factorY(2*2);
    
    	setPenRadius(penR);
    	setPenColor(Color.WHITE);
    	
    	Composite originalComposite = offscreen.getComposite();
    	offscreen.setComposite(makeComposite(tran));
    	offscreen.drawRoundRect((int)(xs - ws/2), (int)(ys - hs/2), (int)ws, (int)hs, 10, 10);
    	offscreen.setComposite(originalComposite);
    }
    
    private AlphaComposite makeComposite(float alpha) {
    	int type = AlphaComposite.SRC_OVER;
    	return(AlphaComposite.getInstance(type, alpha));
    }
    
    public void TextEffect(double x, double y, String s, Color color, float tran, Font font){
    	double xs = scaleX(x);
        double ys = scaleY(y);
    
    	setPenColor(color);
    	offscreen.setFont(font);
    	
        FontMetrics metrics = offscreen.getFontMetrics();
        int hs = metrics.getDescent();

        offscreen.drawString(s, (float) (xs), (float) (ys + hs));
    }
    
    public void drawGradientCircle(float x, float y, float r){
    	// Retains the previous state
        Paint oldPaint = offscreen.getPaint();

        float xs = (float) scaleX(x);
        float ys = (float) scaleY(y);
        float ws = (float) factorX(2*r);
        float hs = (float) factorY(2*r);

        // Fills the circle with solid blue color
        offscreen.setColor(new Color(0x0153CC));
        offscreen.fill(new Ellipse2D.Double(xs - ws/2, ys - hs/2, ws-1, hs-1));
        
        // Adds shadows at the top
        Paint p;
        p = new GradientPaint(xs, ys-(hs/3), new Color(0.0f, 0.0f, 0.0f, 0.4f),
                xs, ys, new Color(0.0f, 0.0f, 0.0f, 0.0f));
        offscreen.setPaint(p);
        //offscreen.fill(new Ellipse2D.Double(xs - ws/2, ys - hs/2, ws-1, hs-1));
        
        // Adds highlights at the bottom 
        p = new GradientPaint(xs, ys-(hs/8), new Color(1.0f, 1.0f, 1.0f, 0.0f),
                xs, ys+(hs/3), new Color(1.0f, 1.0f, 1.0f, 0.4f));
        offscreen.setPaint(p);
        //offscreen.fill(new Ellipse2D.Double(xs - ws/2, ys - hs/2, ws-1, hs-1));
        
        // Creates dark edges for 3D effect
        p = new RadialGradientPaint(new Point2D.Double(xs,
                ys), hs / 2.0f,
                new float[] { 0.0f, 1.0f },
                new Color[] { new Color(6, 76, 160, 0),
                    new Color(0.0f, 0.0f, 0.0f, 0.8f) });
        offscreen.setPaint(p);
        offscreen.fill(new Ellipse2D.Double(xs - ws/2, ys - hs/2, ws-1, hs-1));
        
        // Adds oval inner highlight at the bottom
        p = new RadialGradientPaint(new Point2D.Double(xs,
                ys+45.0), hs / 2.5f,
                new Point2D.Double(xs, ys + (hs / 2.5)), // (r / 2.0, r * 1.75 + 6)
                new float[] { 0.0f, 1.0f },
                new Color[] { new Color(64, 142, 203, 255),
                    new Color(64, 142, 203, 0) },
                RadialGradientPaint.CycleMethod.NO_CYCLE,
                RadialGradientPaint.ColorSpaceType.SRGB,
                AffineTransform.getScaleInstance(1.0, 1.05));
        offscreen.setPaint(p);
        //offscreen.fill(new Ellipse2D.Double(xs - ws/2, ys-(hs*0.57), ws-1, hs-1));
        
        // Adds oval specular highlight at the top left
        p = new RadialGradientPaint(new Point2D.Double(xs,
                ys), ws / 1.4f,
                new Point2D.Double(xs-(ws/5), ys-(hs/6)),
                new float[] { 0.0f, 0.5f },
                new Color[] { new Color(1.0f, 1.0f, 1.0f, 0.4f),
                    new Color(1.0f, 1.0f, 1.0f, 0.0f) },
                RadialGradientPaint.CycleMethod.NO_CYCLE);
        offscreen.setPaint(p);
        //offscreen.fill(new Ellipse2D.Double(xs - ws/2, ys - hs/2, ws-1, hs-1));
        
        // Restores the previous state
        offscreen.setPaint(oldPaint);
        
        //draw();
    }
    
    public void drawTrail(double x, double y, double w, double h, float trans, Color color) {
    	double xs = scaleX(x);
        double ys = scaleY(y);
        double ws = factorX(2*w);
        double hs = factorY(2*h);
    	
        offscreen.setColor(color);
    	Composite originalComposite = offscreen.getComposite();
    	offscreen.setComposite(makeComposite((float) trans));
    	offscreen.fillRoundRect((int)(xs - ws/2), (int)(ys - hs/2), (int)ws, (int)hs, 10, 10);
    	offscreen.setComposite(originalComposite);
    }
    
    public void clear() { clear(Color.BLACK); }

    public void clear(Color color) {
        offscreen.setColor(color);
        offscreen.fillRect(0, 0, width, height);
        offscreen.setColor(penColor);
        //draw();
    }
    
    public void dashLine(double x0, double y0, double x1, double y1) {
        offscreen.draw(new Line2D.Double(scaleX(x0), scaleY(y0), scaleX(x1), scaleY(y1)));
        //draw();
    }

    private void pixel(double x, double y) {
        offscreen.fillRect((int) Math.round(scaleX(x)), (int) Math.round(scaleY(y)), 1, 1);
    }

    public void point(double x, double y) {
        double xs = scaleX(x);
        double ys = scaleY(y);
        double r = penRadius;
        float scaledPenRadius = (float) (r * DEFAULT_SIZE);

        // double ws = factorX(2*r);
        // double hs = factorY(2*r);
        // if (ws <= 1 && hs <= 1) pixel(x, y);
        if (scaledPenRadius <= 1) pixel(x, y);
        else offscreen.fill(new Ellipse2D.Double(xs - scaledPenRadius/2, ys - scaledPenRadius/2,
                                                 scaledPenRadius, scaledPenRadius));
        //draw();
    }

    public void circle(double x, double y, double r) {
        if (r < 0) throw new IllegalArgumentException("circle radius must be nonnegative");
        double xs = scaleX(x);
        double ys = scaleY(y);
        double ws = factorX(2*r);
        double hs = factorY(2*r);
        if (ws <= 1 && hs <= 1) pixel(x, y);
        else offscreen.draw(new Ellipse2D.Double(xs - ws/2, ys - hs/2, ws, hs));
        //draw();
    }

    public void filledCircle(double x, double y, double r, float trans, Color color) {
        if (r < 0) throw new IllegalArgumentException("circle radius must be nonnegative");
        double xs = scaleX(x);
        double ys = scaleY(y);
        double ws = factorX(2*r);
        double hs = factorY(2*r);
        
        if (ws <= 1 && hs <= 1){ pixel(x, y); return; }
        //NewGame.player[NewGame.playerTurn].getPieceColor()
        offscreen.setColor(color);
    	Composite originalComposite = offscreen.getComposite();
    	offscreen.setComposite(makeComposite((float) trans));
    	offscreen.fill(new Ellipse2D.Double(xs - ws/2, ys - hs/2, ws, hs));
    	offscreen.setComposite(originalComposite);
    }
    
    public void arc(double x, double y, double r, double angle1, double angle2) {
        if (r < 0) throw new IllegalArgumentException("arc radius must be nonnegative");
        while (angle2 < angle1) angle2 += 360;
        double xs = scaleX(x);
        double ys = scaleY(y);
        double ws = factorX(2*r);
        double hs = factorY(2*r);
        if (ws <= 1 && hs <= 1) pixel(x, y);
        else offscreen.fill(new Arc2D.Double(xs - ws/2, ys - hs/2, ws, hs, angle1, angle2 - angle1, Arc2D.OPEN));
    }
    
    public void drawDiscardPile(double x, double y, double halfWidth, double halfHeight){
    	double xs = scaleX(x);
        double ys = scaleY(y);
        
        setFont(BIGGER_FONT);
        setPenColor(Color.DARK_GRAY);
        
    	rectangle(x, y, halfWidth, halfHeight);
    	Racko_Main.gameBoard.text(x, y, "DISCARD");
    }
    
    public void drawCard(double x, double y, double w, double h, boolean faceUp, Color colorOne, Color colorTwo, int value, boolean highlighted){

        setPenRadius(.005);
        
        if (faceUp){
        	setFont(BIG_FONT);
    		setPenColor(colorTwo);
    		
    		 if (highlighted){
    	        offscreen.setColor(colorOne);
    	        Composite originalComposite = offscreen.getComposite();
    	        offscreen.setComposite(makeComposite((float) .25));
    	        filledRectangle(x, y, w, h);
    	        offscreen.setComposite(originalComposite);
    	     }else{
    	    	 filledRectangle(x, y, w, h);
    	     }
    		 
        	setPenColor(colorOne);
        	rectangle(x, y, w, h);
        	double i = (double)value / (double)Racko_Main.game.getNumCards();
        	textLeft((x-w) + (i * (w*1.7)), y + (h-2), ""+value);
        	
        	setFont(BIG_FONT);
        	Racko_Main.gameBoard.text(x, y, "RACKO");
    	}else{
    		Racko_Main.gameBoard.setPenColor(colorOne);
    		Racko_Main.gameBoard.filledRectangle(x, y, w, h);
    		Racko_Main.gameBoard.setPenColor(colorTwo);
    		Racko_Main.gameBoard.rectangle(x, y, w, h);
        	setFont(BIGGER_FONT);
        	Racko_Main.gameBoard.text(x, y, "RACKO");
    	}

        setFont(BIG_FONT);
        setPenRadius();
    }
    
    public void rectangle(double x, double y, double halfWidth, double halfHeight) {
        if (halfWidth  < 0) throw new IllegalArgumentException("half width must be nonnegative");
        if (halfHeight < 0) throw new IllegalArgumentException("half height must be nonnegative");
        double xs = scaleX(x);
        double ys = scaleY(y);
        double ws = factorX(2*halfWidth);
        double hs = factorY(2*halfHeight);
        if (ws <= 1 && hs <= 1) pixel(x, y);
        else offscreen.drawRoundRect((int)(xs - ws/2), (int)(ys - hs/2), (int)ws, (int)hs, 20, 20);
        //draw();
    }

    public void filledRectangle(double x, double y, double halfWidth, double halfHeight) {
        if (halfWidth  < 0) throw new IllegalArgumentException("half width must be nonnegative");
        if (halfHeight < 0) throw new IllegalArgumentException("half height must be nonnegative");
        double xs = scaleX(x);
        double ys = scaleY(y);
        double ws = factorX(2*halfWidth);
        double hs = factorY(2*halfHeight);
        if (ws <= 1 && hs <= 1) pixel(x, y);
        else offscreen.fillRoundRect((int)(xs - ws/2), (int)(ys - hs/2), (int)ws, (int)hs, 20, 20);
        //draw();
    }
    
    public void drawMenu(Menu menu){

        setPenRadius(.005);
        setFont(BIGGER_FONT);
    	//setPenColor(menu.getColorMain());
    		
        setPenColor(menu.getColorHeader());
    	filledRectangle(menu.getX(), menu.getY()+menu.getHeight(), menu.getWidth(), menu.getHeight()*.35);
    	
        Composite originalComposite = offscreen.getComposite();
        offscreen.setComposite(makeComposite(menu.getTransparency()));
        
        //filledRectangle(menu.getX(), menu.getY(), menu.getWidth(), menu.getHeight());
    	
    	setPenColor(Color.WHITE);
        textLeft(menu.getX()+2, menu.getY()+menu.getHeight()-1, "O  P  T  I  O  N  S");
    	
        setFont(BIG_FONT);
        
        textLeft(menu.getX()+2, menu.getY()+menu.getHeight()-7, "How many players?");
        textLeft(menu.getX()+21, menu.getY()+menu.getHeight()-7, "How many AI players?");
        textLeft(menu.getX()+47, menu.getY()+menu.getHeight()-7, "Game mode?");
        
        offscreen.setComposite(originalComposite);
        
        setFont(BIG_FONT);
        setPenRadius();
        setPenColor();
    }
    
    public void drawButton(Button button){

        setPenRadius(.007);
        setFont(BIG_FONT);
    	setPenColor(button.getColor());
    		
        Composite originalComposite = offscreen.getComposite();
        offscreen.setComposite(makeComposite(button.getTransparency()));
        
        setPenRadius(.01);
		
		if (button.isHighlighted()){
			setPenColor(button.getSelectedColor());
			filledRectangle(button.getX(), button.getY()-1, button.getW(), button.getH());
		}else{
			setPenColor(button.getColor());
			filledRectangle(button.getX(), button.getY()-1, button.getW(), button.getH());
		}
		
		setPenColor(button.getBorderColor());
		rectangle(button.getX(), button.getY()-1, button.getW(), button.getH());
		
		if (button.isEnabled())
			setPenColor(Color.WHITE);
		else
			setPenColor(Color.GRAY);
		
		//Racko_Main.gameBoard.setPenColor(textColor);
		text(button.getX(), button.getY()-1, button.getMessage());
        
        offscreen.setComposite(originalComposite);
        
        setFont(BIG_FONT);
        setPenRadius();
        setPenColor();
    }
    
    public Polygon hex (int x0, int y0) {
    	 
		int y = y0;
		int x = x0;
				
		int[] cx,cy;

		cx = new int[] {x};
 
		cy = new int[] {y};
		return new Polygon(cx,cy,6);
	}
	
	public void drawHex(int i, int j, int num) {
		int x = 0;
		int y = 0;
		Polygon poly = hex(x,y);
		
		offscreen.drawPolygon(poly);
	}
	
	public void fillHex(int i, int j, int num, String state, boolean link) {
		int x = 0;
		int y = 0;
		
		offscreen.fillPolygon(hex(x,y));
	}
	
	 // get an image from the given filename
    private Image getImage(String filename) {

        // to read from file
        ImageIcon icon = new ImageIcon(filename);

        // try to read from URL
        if ((icon == null) || (icon.getImageLoadStatus() != MediaTracker.COMPLETE)) {
            try {
                URL url = new URL(filename);
                icon = new ImageIcon(url);
            } catch (Exception e) { /* not a url */ }
        }

        // in case file is inside a .jar
        if ((icon == null) || (icon.getImageLoadStatus() != MediaTracker.COMPLETE)) {
            URL url = StdDraw.class.getResource(filename);
            if (url == null) throw new IllegalArgumentException("image " + filename + " not found");
            icon = new ImageIcon(url);
        }

        return icon.getImage();
    }
    
	/**
     * Draw picture (gif, jpg, or png) centered on (x, y), rescaled to w-by-h.
     * @param x the center x coordinate of the image
     * @param y the center y coordinate of the image
     * @param s the name of the image/picture, e.g., "ball.gif"
     * @param w the width of the image
     * @param h the height of the image
     * @throws IllegalArgumentException if the width height are negative
     * @throws IllegalArgumentException if the image is corrupt
     */
    public void picture(double x, double y, String s, double w, double h) {
        Image image = getImage(s);
        double xs = scaleX(x);
        double ys = scaleY(y);
        if (w < 0) throw new IllegalArgumentException("width is negative: " + w);
        if (h < 0) throw new IllegalArgumentException("height is negative: " + h);
        double ws = factorX(w);
        double hs = factorY(h);
        if (ws < 0 || hs < 0) throw new IllegalArgumentException("image " + s + " is corrupt");
        if (ws <= 1 && hs <= 1) pixel(x, y);
        else {
            offscreen.drawImage(image, (int) Math.round(xs - ws/2.0),
                                       (int) Math.round(ys - hs/2.0),
                                       (int) Math.round(ws),
                                       (int) Math.round(hs), null);
        }
        draw();
    }
    
    public void picture(double x, double y, String s, double w, double h, double degrees) {
        Image image = getImage(s);
        double xs = scaleX(x);
        double ys = scaleY(y);
        double ws = factorX(w);
        double hs = factorY(h);
        if (ws < 0 || hs < 0) throw new IllegalArgumentException("image " + s + " is corrupt");
        if (ws <= 1 && hs <= 1) pixel(x, y);

        offscreen.rotate(Math.toRadians(-degrees), xs, ys);
        offscreen.drawImage(image, (int) Math.round(xs - ws/2.0),
                                   (int) Math.round(ys - hs/2.0),
                                   (int) Math.round(ws),
                                   (int) Math.round(hs), null);
        offscreen.rotate(Math.toRadians(+degrees), xs, ys);

        draw();
    }
    
    public void text(double x, double y, String s) {
        offscreen.setFont(font);
        FontMetrics metrics = offscreen.getFontMetrics();
        double xs = scaleX(x);
        double ys = scaleY(y);
        int ws = metrics.stringWidth(s);
        int hs = metrics.getDescent();
        
        if ((s != null) && (metrics != null))
        	offscreen.drawString(s, (float) (xs - ws/2.0), (float) (ys + hs));
        //draw();
    }
    
    public void text(double x, double y, String s, double degrees) {
        double xs = scaleX(x);
        double ys = scaleY(y);
        offscreen.rotate(Math.toRadians(-degrees), xs, ys);
        text(x, y, s);
        offscreen.rotate(Math.toRadians(+degrees), xs, ys);
    }

    public void textLeft(double x, double y, String s) {
        offscreen.setFont(font);
        FontMetrics metrics = offscreen.getFontMetrics();
        double xs = scaleX(x);
        double ys = scaleY(y);
        int hs = metrics.getDescent();
        offscreen.drawString(s, (float) (xs), (float) (ys + hs));
        //draw(); 
    }

    public void textRight(double x, double y, String s) {
        offscreen.setFont(font);
        FontMetrics metrics = offscreen.getFontMetrics();
        double xs = scaleX(x);
        double ys = scaleY(y);
        int ws = metrics.stringWidth(s);
        int hs = metrics.getDescent();
        offscreen.drawString(s, (float) (xs - ws), (float) (ys + hs));
        //draw();
    }

    public void show(int t) {
        defer = false;
        draw();
        try { Thread.sleep(t); }
        catch (InterruptedException e) { System.out.println("Error sleeping"); }
        defer = true;
    }

    public void show() {
        defer = false;
        draw();
    }

    // draw onscreen if defer is false
    private void draw() {
        if (defer) return;
        onscreen.drawImage(offscreenImage, 0, 16, null);
        frame.repaint();
    }

    public double getPenRadius() { return penRadius; }

    public void setPenRadius() { setPenRadius(DEFAULT_PEN_RADIUS); }

    public void setPenRadius(double r) {
        if (r < 0) throw new IllegalArgumentException("pen radius must be nonnegative");
        penRadius = r;
        float scaledPenRadius = (float) (r * DEFAULT_SIZE);
        BasicStroke stroke = new BasicStroke(scaledPenRadius, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
        // BasicStroke stroke = new BasicStroke(scaledPenRadius);
        offscreen.setStroke(stroke);
    }

    public void setDashPenRadius(double r) {
        if (r < 0) throw new IllegalArgumentException("pen radius must be nonnegative");
        float scaledPenRadius = (float) (r * DEFAULT_SIZE);
        Stroke dotted = new BasicStroke(scaledPenRadius, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 8, new float[] {1,2}, 0);
        // BasicStroke stroke = new BasicStroke(scaledPenRadius);
        offscreen.setStroke(dotted);
    }

    public Color getPenColor() { return penColor; }

    public void setPenColor() { setPenColor(Color.DARK_GRAY); }

    public void setPenColor(Color color) {
        penColor = color;
        offscreen.setColor(penColor);
    }

    public void setPenColor(int red, int green, int blue) {
        if (red   < 0 || red   >= 256) throw new IllegalArgumentException("amount of red must be between 0 and 255");
        if (green < 0 || green >= 256) throw new IllegalArgumentException("amount of red must be between 0 and 255");
        if (blue  < 0 || blue  >= 256) throw new IllegalArgumentException("amount of red must be between 0 and 255");
        setPenColor(new Color(red, green, blue));
    }

    public Font getFont() { return font; }

    public void setFont() { setFont(DEFAULT_FONT); }
    
    public void setFont(Font f) { font = f; }
    
    public void setXscale() { setXscale(DEFAULT_XMIN, DEFAULT_XMAX); }

    public void setYscale() { setYscale(DEFAULT_YMIN, DEFAULT_YMAX); }

    public void setXscale(double min, double max) {
        double size = max - min;
        synchronized (mouseLock) {
            xmin = min - BORDER * size;
            xmax = max + BORDER * size;
        }
    }

    public void setYscale(double min, double max) {
        double size = max - min;
        synchronized (mouseLock) {
            ymin = min - BORDER * size;
            ymax = max + BORDER * size;
        }
    }

    public void setScale(double min, double max) {
        double size = max - min;
        synchronized (mouseLock) {
            xmin = min - BORDER * size;
            xmax = max + BORDER * size;
            ymin = min - BORDER * size;
            ymax = max + BORDER * size;
        }
    }
	
    // helper functions that scale from user coordinates to screen coordinates and back
    private double  scaleX(double x) { return width  * (x - xmin) / (xmax - xmin); }
    private double  scaleY(double y) { return height * (ymax - y) / (ymax - ymin); }
    private double factorX(double w) { return w * width  / Math.abs(xmax - xmin);  }
    private double factorY(double h) { return h * height / Math.abs(ymax - ymin);  }
    private double   userX(double x) { return xmin + x * (xmax - xmin) / width;    }
    private double   userY(double y) { return ymax - y * (ymax - ymin) / height;   }
	
	    public boolean mousePressed() {
	        synchronized (mouseLock) {
	            return mousePressed;
	        }
	    }

	    public double mouseX() {
	        synchronized (mouseLock) {
	            return mouseX;
	        }
	    }

	    public double mouseY() {
	        synchronized (mouseLock) {
	            return mouseY;
	        }
	    }

	    public void mouseClicked(MouseEvent e) { }

	    public void mouseEntered(MouseEvent e) { }

	    public void mouseExited(MouseEvent e) { }

	    public void mousePressed(MouseEvent e) {
	        synchronized (mouseLock) {
	        	
	            mouseX = userX(e.getX());
	            mouseY = userY(e.getY());
	            
	            mousePressed = true;

	            if (Racko_Main.game.gameStatus != GameStatus.GAME_ON)
	            	return;
	            
	            if (Racko_Main.game.getSelectedCard() != null)
	        		return;
	            
	            if (Racko_Main.game.player.get(Racko_Main.game.getPlayerTurn()).isAi())
	            	return;
	            
	            Card deckCard = null;
	            Card discardCard = null;
	            
	            if (Racko_Main.game.deck.deck.size() > 0)
	            	deckCard = Racko_Main.game.deck.deck.get(Racko_Main.game.deck.deck.size()-1);
	            if (Racko_Main.game.deck.discards.size() > 0)
	            	discardCard = Racko_Main.game.deck.discards.get(Racko_Main.game.deck.discards.size()-1);
	            
	            Card c  = Racko_Main.game.deck.deck.get(Racko_Main.game.deck.deck.size()-1);
	            if ((mouseX > c.getX() - c.getW()) && (mouseX < c.getX() + c.getW())){
            		if ((mouseY < c.getY() + c.getH()) && (mouseY > c.getY() - c.getH())){
            			deckCard.setFaceUp(true);
						Racko_Main.game.setSelectedCard(deckCard);
						return;
	            	}
            	}
	            
	            if (!Racko_Main.game.deck.deck.get(Racko_Main.game.deck.deck.size()-1).isFaceUp()){
	            	if (Racko_Main.game.deck.discards.size() > 0){
	            		Card dc  = Racko_Main.game.deck.discards.get(Racko_Main.game.deck.discards.size()-1);
	            		dc.setHighlighted(false);
	            		if ((mouseX > dc.getX() - dc.getW()) && (mouseX < dc.getX() + dc.getW())){
	            			if ((mouseY < dc.getY() + dc.getH()) && (mouseY > dc.getY() - dc.getH())){
	            				Racko_Main.game.setSelectedCard(discardCard);
	    						return;
	            			}
	            		}
	            	}
	            }
	             
	           return;

	        }
	    }
	    
	    public void mouseReleased(MouseEvent e) {
	        synchronized (mouseLock) {
	        	
	        	if ((Racko_Main.gameBoard.mouseX() > Racko_Main.game.Continue.getX() - Racko_Main.game.Continue.getW()) && (Racko_Main.gameBoard.mouseX() < Racko_Main.game.Continue.getX() + Racko_Main.game.Continue.getW())){
	    	         if ((Racko_Main.gameBoard.mouseY() > Racko_Main.game.Continue.getY() - Racko_Main.game.Continue.getH()) && (Racko_Main.gameBoard.mouseY() < Racko_Main.game.Continue.getY() + Racko_Main.game.Continue.getH())){
	    	            mousePressed = false;

	    	            if (Racko_Main.game.Continue.isVisible()){
	    	            	if (Racko_Main.game.gameStatus == GameStatus.END_GAME)
	    	        		 	Racko_Main.newGame();
	    	        	 	else
	    	        		 	Racko_Main.game.NewHand();
	    					return;
	    	            }
	    			}
	    		}
	        	
	        	if (Racko_Main.game.gameStatus == GameStatus.PRE_GAME){
	        		
	        		Button button = Racko_Main.game.menu.getButton();
	        		
	        		if (button != null){
	        			Racko_Main.game.menu.selectButton(button);
	        			return;
	        		}
	            }
	        	
	        	if (Racko_Main.game.gameStatus != GameStatus.GAME_ON)
	            	return;
	        	
	        	if (Racko_Main.game.getSelectedCard() == null)
	        		return;
	        	
	        	if (Racko_Main.game.player.get(Racko_Main.game.getPlayerTurn()).isAi())
	            	return;
	        	
	    	    if ((Racko_Main.gameBoard.mouseX() > Racko_Main.game.getDiscardX() - Racko_Main.game.deck.deck.get(0).getW()) && (Racko_Main.gameBoard.mouseX() < Racko_Main.game.getDiscardX() + Racko_Main.game.deck.deck.get(0).getW())){
	    	         if ((Racko_Main.gameBoard.mouseY() > Racko_Main.game.getDiscardY() - Racko_Main.game.deck.deck.get(0).getH()) && (Racko_Main.gameBoard.mouseY() < Racko_Main.game.getDiscardY() + Racko_Main.game.deck.deck.get(0).getH())){
	    	        	 if (Racko_Main.game.getSelectedCard() != Racko_Main.game.deck.discards.get(Racko_Main.game.deck.discards.size()-1)){
	    		            	 
	    		            	Racko_Main.game.deck.discards.add(Racko_Main.game.getSelectedCard());
	    	             
	    	             	if (Racko_Main.game.getSelectedCard() == Racko_Main.game.deck.deck.get(Racko_Main.game.deck.deck.size()-1))
	    	            	 	Racko_Main.game.deck.deck.remove(Racko_Main.game.getSelectedCard());
	    	             	else
	    	            	 	Racko_Main.game.player.get(Racko_Main.game.getPlayerTurn()).hand.cards.remove(Racko_Main.game.getSelectedCard());
	    	             
	    	             	Racko_Main.game.getSelectedCard().setHighlighted(false);
	    	             	Racko_Main.game.getSelectedCard().setSelected(false);
	    	             	Racko_Main.game.setSelectedCard(null);
	    	             
	    	             	if (Racko_Main.game.deck.deck.isEmpty())
	    	            	 	Racko_Main.game.deck.shuffleDiscardsIntoDeck();

	    	             	mousePressed = false;

	    	            	Racko_Main.game.endTurn();
	    					return;
	    	        	 }
	    			}
	    		}
	    	    
	    	    	for (Card c: Racko_Main.game.player.get(Racko_Main.game.getPlayerTurn()).hand.cards){
	            		if (c.isHighlighted()){    
	    	            	if (Racko_Main.game.getSelectedCard() == Racko_Main.game.deck.deck.get(Racko_Main.game.deck.deck.size()-1))
	    	            		Racko_Main.game.deck.deck.remove(Racko_Main.game.getSelectedCard());
	    	            	else
	    	            		Racko_Main.game.deck.discards.remove(Racko_Main.game.getSelectedCard());
	    	             
	    	            	Racko_Main.game.player.get(Racko_Main.game.getPlayerTurn()).hand.cards.add(Racko_Main.game.player.get(Racko_Main.game.getPlayerTurn()).hand.cards.indexOf(c), Racko_Main.game.getSelectedCard());
	    	            	Racko_Main.game.player.get(Racko_Main.game.getPlayerTurn()).hand.cards.remove(c);
	    	            	Racko_Main.game.deck.discards.add(c);
	    	            	
	    	            	Racko_Main.game.getSelectedCard().setX(c.getX()+5);
	    	            	Racko_Main.game.getSelectedCard().setY(c.getY()+15);
	    	            	Racko_Main.game.getSelectedCard().setTargetX(c.getX());
	    	            	Racko_Main.game.getSelectedCard().setTargetY(c.getY());
	    	            	Racko_Main.game.getSelectedCard().reset(Racko_Main.game.getSelectedCard().getTargetX(), Racko_Main.game.getSelectedCard().getTargetY(), 5);
	    	            	Racko_Main.game.getSelectedCard().setHasTarget(true);
	    	            	
	    	            	c.setTargetX(Racko_Main.game.getDiscardX() + (0.5 + (2.0 - 0.5) * rnd.nextDouble()) - (0.5 + (2.0 - 0.5) * rnd.nextDouble()));
	    	            	c.setTargetY(Racko_Main.game.getDiscardY() + (0.5 + (2.0 - 0.5) * rnd.nextDouble()) - (0.5 + (2.0 - 0.5) * rnd.nextDouble()));
	    	            	c.reset(c.getTargetX(), c.getTargetY(), 5);
	    	            	c.setHasTarget(true);

	    	            	c.setHighlighted(false);
	    	            	Racko_Main.game.getSelectedCard().setHighlighted(false);
	    	            	Racko_Main.game.getSelectedCard().setSelected(false);
        					Racko_Main.game.setSelectedCard(null);
        					
        					if (Racko_Main.game.deck.deck.isEmpty())
   	    	            	 	Racko_Main.game.deck.shuffleDiscardsIntoDeck();
        					
        					mousePressed = false;

        					Racko_Main.game.player.get(Racko_Main.game.getPlayerTurn()).hand.orderPoints();
        					Racko_Main.game.endTurn();
        					
        					return;
	            		}
	            	}

	    	    if (Racko_Main.game.getSelectedCard() == Racko_Main.game.deck.deck.get(Racko_Main.game.deck.deck.size()-1)){
	    	    	Racko_Main.game.getSelectedCard().setTargetX(Racko_Main.game.deck.getX());
	            	Racko_Main.game.getSelectedCard().setTargetY(Racko_Main.game.deck.getY());
	    	    }else{
	    	    	Racko_Main.game.getSelectedCard().setTargetX(Racko_Main.game.getDiscardX());
	            	Racko_Main.game.getSelectedCard().setTargetY(Racko_Main.game.getDiscardY());
	    	    }
	    	    
            	Racko_Main.game.getSelectedCard().reset(Racko_Main.game.getSelectedCard().getTargetX(), Racko_Main.game.getSelectedCard().getTargetY(), 5);
            	Racko_Main.game.getSelectedCard().setHasTarget(true);
	            	
            	Racko_Main.game.getSelectedCard().setHighlighted(false);
            	Racko_Main.game.getSelectedCard().setSelected(false);
            	Racko_Main.game.setSelectedCard(null);
	        	
	            mousePressed = false;
	            
	        }
	    }
	    
	    /**
	     * This method cannot be called directly.
	     */
	    public void mouseDragged(MouseEvent e)  {
	        synchronized (mouseLock) {
	            mouseX = userX(e.getX());
	            mouseY = userY(e.getY());

	            if (Racko_Main.game.getSelectedCard() == null)
	            	return;
	            
	            Racko_Main.game.getSelectedCard().setX(mouseX);
	            Racko_Main.game.getSelectedCard().setY(mouseY);
	         
	            //Racko_Main.game.getSelectedCard().moveToTarget();
	
	            for (Card c: Racko_Main.game.player.get(Racko_Main.game.getPlayerTurn()).hand.cards){
	            	c.setHighlighted(false);
	            }
	            
	            for (Card c: Racko_Main.game.player.get(Racko_Main.game.getPlayerTurn()).hand.cards){
	            	if (!c.isFaceUp())
	            		continue;
	            	if ((mouseX > c.getX() - c.getW()) && (mouseX < c.getX() + c.getW())){
	            		if ((mouseY < c.getY() + 10) && (mouseY > c.getY() + 4)){
	            			c.setHighlighted(true);
	            			
	            			if (Racko_Main.game.player.get(Racko_Main.game.getPlayerTurn()).hand.lastSelect != c){
	            				c.explodeRadius = 1;
	            				c.explodeTran = 1.0f;
	            				c.explodePenR = .01f;
	            				c.explodeGraphic = true;
	            			}
	            			
	            			Racko_Main.game.player.get(Racko_Main.game.getPlayerTurn()).hand.lastSelect = c;
	            			
	            			Racko_Main.game.getSelectedCard().setX(c.getX());
	            			Racko_Main.game.getSelectedCard().setY(c.getY());
	            			
	            			return;
		            	}
	            	}
	            }
	        }
	    }

	    /**
	     * This method cannot be called directly.
	     */
	    public void mouseMoved(MouseEvent e) {
	        synchronized (mouseLock) {
	            mouseX = userX(e.getX());
	            mouseY = userY(e.getY());

	            if ((Racko_Main.game.gameStatus == GameStatus.END_HAND) || (Racko_Main.game.gameStatus == GameStatus.END_GAME)){
	            	Racko_Main.game.Continue.setColor(Racko_Main.game.Continue.getDefaultColor());
	            
	            	if ((Racko_Main.gameBoard.mouseX() > Racko_Main.game.Continue.getX() - Racko_Main.game.Continue.getW()) && (Racko_Main.gameBoard.mouseX() < Racko_Main.game.Continue.getX() + Racko_Main.game.Continue.getW())){
	    	         	if ((Racko_Main.gameBoard.mouseY() > Racko_Main.game.Continue.getY() - Racko_Main.game.Continue.getH()) && (Racko_Main.gameBoard.mouseY() < Racko_Main.game.Continue.getY() + Racko_Main.game.Continue.getH())){
	    	         		Racko_Main.game.Continue.setColor(Racko_Main.game.Continue.getHighlightColor());
	    					return;
	    	         }
	    			}
	            }

	            if (Racko_Main.game.gameStatus == GameStatus.PRE_GAME){
	            	Button button = Racko_Main.game.menu.getButton();
	        		
	        		if (button != null){
	        			Racko_Main.game.menu.highlightButton(button);
	        			return;
	        		}
	            }
	            
	            if (Racko_Main.game.gameStatus != GameStatus.GAME_ON)
	            	return;
	            
	            if (Racko_Main.game.getSelectedCard() != null)
	        		return;

	            frame.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
	            
	            Card c  = Racko_Main.game.deck.deck.get(Racko_Main.game.deck.deck.size()-1);
	            if ((mouseX > c.getX() - c.getW()) && (mouseX < c.getX() + c.getW())){
            		if ((mouseY < c.getY() + c.getH()) && (mouseY > c.getY() - c.getH())){
            			frame.setCursor(new Cursor(Cursor.HAND_CURSOR));
						return;
	            	}
            	}
	            
	            if (!Racko_Main.game.deck.deck.get(Racko_Main.game.deck.deck.size()-1).isFaceUp()){
	            	if (Racko_Main.game.deck.discards.size() > 0){
	            		Card dc  = Racko_Main.game.deck.discards.get(Racko_Main.game.deck.discards.size()-1);
	            		if ((mouseX > dc.getX() - dc.getW()) && (mouseX < dc.getX() + dc.getW())){
	            			if ((mouseY < dc.getY() + dc.getH()) && (mouseY > dc.getY() - dc.getH())){
	            				frame.setCursor(new Cursor(Cursor.HAND_CURSOR));
	            				return;
	            			}
	            		}
	            	}
	            }
	             
	           return;
	        }
	    }

		@Override
		public void itemStateChanged(ItemEvent e) {
			Object source = e.getItemSelectable();

			if (source == cbMenuItem1){
		    	Racko_Main.game.sound = true;
		    if (e.getStateChange() == ItemEvent.DESELECTED)
		    	Racko_Main.game.sound = false;
			}
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if ("exit game".equals(e.getActionCommand())){
				System.exit(0);
			}else if ("new game".equals(e.getActionCommand())){
				//Racko_Main.game.createGame();
			}
		}
}
