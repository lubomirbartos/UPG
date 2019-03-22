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

public class TrafficMain extends JFrame {
	
	private static final long serialVersionUID = 1L;

	public static void main(String[] args) {

		// TODO check arguments
		// create Simulator
		Simulator simulator = new Simulator();
		String[] scenarios = simulator.getScenarios();
//		simulator.runScenario(scenarios[Integer.parseInt(args[1])]);
		simulator.runScenario(scenarios[1]);
		new TrafficMain(simulator);
		return;
	}

	private TrafficMain(Simulator simulator) {
		super("Traffic");
		
		BorderLayout layout = new BorderLayout();
		setLayout(layout);
	
		addWindowListener(new WindowAdapter()
			{public void windowClosing(WindowEvent e)
			     {dispose(); System.exit(0);}
			}
		);
		

		Traffic traffic = new Traffic(simulator.getRoadSegments(), simulator.getCrossroads(), simulator.getCars(), computeModelDimensions(simulator));
		
		traffic.setPreferredSize(new Dimension((int) traffic.view.get_window_width(), (int) traffic.view.get_window_height()));
		setSize((int) traffic.view.get_window_width(), (int) traffic.view.get_window_height());
		setPreferredSize(new Dimension((int) traffic.view.get_window_width(), (int) traffic.view.get_window_height()));
		
		add(traffic, 0);
		pack();
		
		setLocationRelativeTo(null);
		setVisible(true);
		
		Timer timer  = new Timer(100, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				repaint();		
			}
		});
		
		timer.start();
		
		while(true) {
			traffic.update(simulator.getRoadSegments(), simulator.getCrossroads(), simulator.getCars());

			try {
				TimeUnit.MILLISECONDS.sleep(100);
				simulator.nextStep(1);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}

	}

	// Computes minimum and maximum values of given model.
	// Stores model to variable
	private View computeModelDimensions(Simulator simulator) {
		double maximum_x = 0;
		double maximum_y = 0;
		double model_width = 0;
		double model_height = 0;		
				
		maximum_x = View.get_max_car_x(simulator.getCars());
		if (maximum_x > model_width) {
			model_width = maximum_x;
		}
		maximum_x = View.get_max_road_x(simulator.getRoadSegments());
		if (maximum_x > model_width) {
			model_width = maximum_x;
		}
		
		maximum_y = View.get_max_car_y(simulator.getCars());
		if (maximum_y > model_height) {
			model_height = maximum_y;
		}
		maximum_y = View.get_max_road_y(simulator.getRoadSegments());
		if (maximum_y > model_height) {
			model_height = maximum_y;
		}
		
		return new View(model_width, model_height);
	}
	
	
	

	
}
