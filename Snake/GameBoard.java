/********************************************************************
 * This version of snake is brought to you by Ben
*********************************************************************/
package snake.game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Random;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.Ellipse2D;
import javax.swing.JPanel;
import javax.swing.Timer;

@SuppressWarnings("serial")
public class GameBoard extends JPanel 
			implements ActionListener {
	
	private int snakeDim = 10;
	private int foodDim = 10;
	
	private final int height = 500;
	private final int width = 1000;
	private final int total = width * height / snakeDim;
	
	private final int snakeX[] = new int[total];
	private final int snakeY[] = new int[total];
	private int foodX;
	private int foodY;
	private int size;
	
	private boolean left = false;
	private boolean right = true;
	private boolean up = false;
	private boolean down = false;
	private boolean inGame = true;
	private boolean wrap = true;
	
	private final int DELAY = snakeDim*2;
	private Timer timer = new Timer(DELAY, this);

	public GameBoard() {
		addKeyListener(new arrowAdapter());
		setBackground(Color.white);
		setFocusable(true);
		
		setPreferredSize(new Dimension(width, height));
		initGame();
	}
	
	public void initGame() {
		//inGame = true;
		final int initLoc = 5*snakeDim;
		size = 2;
		SnakeMain.setScore(size);
		for(int i = 0; i < size; i++) {
			snakeX[i] = initLoc - i*snakeDim;
			snakeY[i] = initLoc;
		}
		
		placeFood();
		
		timer.start();
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		final Graphics2D g2 = (Graphics2D) g;
		
		if(inGame) {
			for(int i = 0; i < size; i++) {
				g2.setColor(Color.black);
				final Ellipse2D thisPiece = 
						new Ellipse2D.Double(snakeX[i], 
						snakeY[i], snakeDim, snakeDim);
				g2.fill(thisPiece);
				g2.draw(thisPiece);
			}
			g2.setColor(Color.red);
			final Ellipse2D food = new Ellipse2D.Double(
					foodX, foodY, foodDim, foodDim);
			g2.fill(food);
			g2.draw(food);
		} else {
			gameOver(g2);
		}
	}
	
	private void gameOver(Graphics2D g2) {
		String msg = "Game Over";
		Font small = new Font("Comic Sans", Font.BOLD, 32);
		FontMetrics metr = getFontMetrics(small);
		
		g2.setColor(Color.black);
		g2.setFont(small);
		g2.drawString(msg, 
				(width-metr.stringWidth(msg))/2, height/2);
	}
	
	private void checkFood() {
		if((snakeX[0] / snakeDim * snakeDim == foodX) 
			&& (snakeY[0] / snakeDim * snakeDim == foodY)) {
			size++;
			placeFood();
			SnakeMain.setScore(size);
		}
	}
	
	private void move() {
		for(int i = size; i > 0; i--) {
			snakeX[i] = snakeX[i-1];
			snakeY[i] = snakeY[i-1];
		}
		
		if(left) snakeX[0] -= snakeDim;
		if(right) snakeX[0] += snakeDim;
		if(up) snakeY[0] -= snakeDim;
		if(down) snakeY[0] += snakeDim;
		
		if(wrap) {
			if(snakeX[0] > width) {
				snakeX[0] -= (width + snakeDim);
			}
			if(snakeX[0] < -snakeDim) {
				snakeX[0] += (width + snakeDim);
			}
			if(snakeY[0] > height) {
				snakeY[0] -= (height + snakeDim);
			}
			if(snakeY[0] < -snakeDim) {
				snakeY[0] += (height + snakeDim);
			}
		}
	}

	private void checkCollision() {
		for(int i = size; i > 0; i--) {
			if((snakeX[0] == snakeX[i]) &&
					(snakeY[0] == snakeY[i])) {
				inGame = false;
			}
		}
		if(!wrap) {
			if(snakeY[0] >= 500 ||
				snakeY[0] < 0 ||
				snakeX[0] >= 1000 ||
				snakeX[0] < 0) inGame = false;
		}
		if(!inGame) timer.stop();
	}
	
	private void placeFood() {
		final Random generator = new Random(
				System.currentTimeMillis());
		int random = generator.nextInt(width / foodDim);
		foodX = (random * foodDim);
		random = generator.nextInt(height / foodDim);
		foodY = (random * foodDim);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		final String sourceName = e.getActionCommand();
		if(inGame) {
			checkFood();
			checkCollision();
			move();
		} else if(!inGame){
			if("New Game".equals(sourceName)) {
				timer.stop();
				inGame = true;
				initGame();
			}
		}
		repaint();
	}
	
	private class arrowAdapter extends KeyAdapter {
		@Override
		public void keyPressed(KeyEvent e) {
			int key = e.getKeyCode();
			
			if((key == KeyEvent.VK_LEFT) && (!right)) {
				left = true;
				up = false;
				down = false;
			}
			if((key == KeyEvent.VK_RIGHT) && (!left)) {
				right = true;
				up = false;
				down = false;
			}
			if((key == KeyEvent.VK_DOWN) && (!up)) {
				down = true;
				right = false;
				left = false;
			}
			if((key == KeyEvent.VK_UP) && (!down)) {
				up = true;
				right = false;
				left = false;
			}
		}
	}
}
