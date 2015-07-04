/**********************************************************
 * This version of snake brought to you by Ben
**********************************************************/
package snake.game;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class SnakeMain extends JFrame {

	private GameBoard myGame;
	private JPanel myPanel = new JPanel();
	private JButton newGame = new JButton("New Game");
	private static JLabel score = new JLabel("Score: 0");
	
	public SnakeMain() {
		myGame = new GameBoard();
		add(myGame, BorderLayout.NORTH);
		newGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//myGame.stopTimer();
				myGame.initGame();
			}
		});
		myPanel.add(newGame);
		myPanel.add(score);
		add(myPanel);
		
		setResizable(false);
		pack();
		setTitle("Snake Mutha Fucka");
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public static void setScore(int newScore) {
		score.setText("Score: " + newScore);
	}
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				JFrame sf = new SnakeMain();
				sf.setVisible(true);
			}
		});
	}

}
