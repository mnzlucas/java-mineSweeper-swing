package br.com.cod3r.ms.vision;

import java.awt.GridLayout;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import br.com.cod3r.ms.model.Board;

@SuppressWarnings("serial")
public class BoardPanel extends JPanel {
	
	public BoardPanel(Board board) {
		
		setLayout(new GridLayout(
				board.getLines(), board.getColumns()));
		
		board.forEach(c -> add(new ButtonField(c)));
		
		board.observersRecord(e -> {
			
			SwingUtilities.invokeLater(() -> {
				
				if(e.isWon()) {
					JOptionPane.showMessageDialog(this, "You Won");
				}else {
					JOptionPane.showMessageDialog(this, "Game Over");
					
				}
				
				board.restart();
			});
			
			
		});
	
	}

}
