
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.TimeUnit;

import javax.swing.JFrame;
import javax.swing.Timer;

public class BasicDrawing1 {

	public static void main(String[] args) {


		JFrame frame = new JFrame();
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		clovek panel = new clovek();
		
		panel.setPreferredSize(new Dimension(640, 480));
		
		long startTime = System.currentTimeMillis();
		
		Timer timer  = new Timer(1, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				long now = System.currentTimeMillis();
				panel.setTime((now - startTime)/1000.0);
				panel.repaint();				
			}
			
		});
		
		timer.start();
		
		frame.add(panel);
		
		
		frame.pack();

		
		frame.setVisible(true);
		
		
	}

}
