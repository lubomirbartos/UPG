import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.Timer;

import TrafficSim.Simulator;

public class TrafficDisplay extends JFrame {
	
	private static final long serialVersionUID = 1L;

	/*
	 * Main of the traffic visualization program.
	 * 
	 * @param Simulator simulator simulator
	 */
	public static void main(String[] args) {
		Simulator simulator = new Simulator();
		String[] scenarios = simulator.getScenarios();
		int scenario_number; 
		
	    try {
	    	if (args.length > 0) {
	    		scenario_number = Integer.parseInt(args[0]);
	    	} else {
		    	scenario_number = 0;
	    	}
	    } catch(NumberFormatException e) { 
	    	scenario_number = 0;
	    } catch(NullPointerException e) {
	    	scenario_number = 0;
	    }
	    
		simulator.runScenario(scenarios[scenario_number]);
		new TrafficDisplay(simulator);
		
		return;
	}

	/*
	 * Constructor for continually updating simulator for next step and displaying traffic state with each step.
	 * 
	 * @param Simulator simulator simulator
	 */
	private TrafficDisplay(Simulator simulator) {
		super("Traffic");
		
		BorderLayout layout = new BorderLayout();
		setLayout(layout);
	
		addWindowListener(new WindowAdapter()
			{public void windowClosing(WindowEvent e)
			     {dispose(); System.exit(0);}
			}
		);		

		Traffic traffic = new Traffic(simulator.getRoadSegments(), simulator.getCrossroads(), simulator.getCars(), new View(simulator));
		
		Dimension window_dimension = new Dimension((int) traffic.view.get_window_width(), (int) traffic.view.get_window_height());
		
		traffic.setPreferredSize(window_dimension);		
		add(traffic, 0);
		pack();
		
		setLocationRelativeTo(null);
		setVisible(true);
		
		Timer traffic_updater  = new Timer(100, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				simulator.nextStep(0.5);
				traffic.update(simulator);
				repaint();		
			}
		});
		
		traffic_updater.start();
	}

	
	
	

	
}
