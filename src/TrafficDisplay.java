import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Point2D;
import java.util.concurrent.TimeUnit;

import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

import TrafficSim.Car;
import TrafficSim.CrossRoad;
import TrafficSim.RoadSegment;
import TrafficSim.Simulator;
import TrafficSim.TrafficLightState;
import TrafficSim.Scenarios.Scenario;

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
		int number_of_simulation ; 
		
	    try { 
	    	number_of_simulation = Integer.parseInt(args[0]);
			simulator.runScenario(scenarios[number_of_simulation]);
	    } catch(NumberFormatException e) { 
			simulator.runScenario(scenarios[0]);
	    } catch(NullPointerException e) {
			simulator.runScenario(scenarios[0]);
	    }
		
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
		simulator.nextStep(1);

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
				simulator.nextStep(1);
				traffic.update(simulator);
				repaint();		
			}
		});
		
		traffic_updater.start();
	}

	
	
	

	
}
