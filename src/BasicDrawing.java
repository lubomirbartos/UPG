
import java.awt.Dimension;

import javax.swing.JFrame;

public class BasicDrawing {

	public static void main(String[] args) {

		JFrame okno = new JFrame();
		
		
		okno.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
		DrawingPanel panel = new DrawingPanel();
		
		panel.setPreferredSize(new Dimension(640, 480));
		okno.add(panel);
		okno.pack();
		
		
		
		
		
		
		
		
		
		
		
		
		okno.setVisible(true);
	}

}
