import java.awt.Dimension;
import java.awt.Toolkit;

public class Racko_Main {
	
	static final int scoreWin = 500;
	static final int numParticles = 50;
	
	static int screenWidth, screenHeight;

	static Game game = new Game();
	static StdDraw gameBoard = new StdDraw();

	public static void init() {
		game.init();
	}

	public void test() {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		screenWidth = screenSize.width;
		screenHeight = screenSize.height;
		gameBoard.setCanvasSize(screenSize.width, screenSize.height);
		gameBoard.setXscale(0.0, 100);
		gameBoard.setYscale(0.0, 100);

		gameBoard.setPenRadius(.005);

		init();
	}

	public static void newGame() {
		game.NewGame();
	}

}
