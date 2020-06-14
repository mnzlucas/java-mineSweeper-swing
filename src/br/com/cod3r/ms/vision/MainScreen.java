package br.com.cod3r.ms.vision;

import javax.swing.JFrame;

import br.com.cod3r.ms.model.Board;

@SuppressWarnings("serial")
public class MainScreen extends JFrame{
	
	public MainScreen() {
		Board board = new Board(16, 30, 50);
		add(new BoardPanel(board));
		
		setTitle("Mine Sweeper");
		setSize(90, 438);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setVisible(true);
	}
	public static void main(String[] args) {
		new MainScreen();
	}

}
