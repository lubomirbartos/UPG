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

public class Main extends JFrame {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
		
	// TODO set min and max size of window


	public static void main(String[] args) {

		//check arguments
		// create Simulator
		Simulator simulator = new Simulator();
		Scenario scenario1 = new Scenario(simulator);
		scenario1.create();
		simulator.addScenario(scenario1);
		String[] scenarios = simulator.getScenarios();
		for (String scenario : scenarios) {			
			System.out.println(scenario);
		}
		simulator.runScenario(scenario1.getId());
		new Main(simulator);

		return;
	}

	private Main(Simulator simulator) {
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
				
		maximum_x = get_max_car_x(simulator.getCars());
		if (maximum_x > model_width) {
			model_width = maximum_x;
		}
		maximum_x = get_max_road_x(simulator.getRoadSegments());
		if (maximum_x > model_width) {
			model_width = maximum_x;
		}
		
		maximum_y = get_max_car_y(simulator.getCars());
		if (maximum_y > model_height) {
			model_height = maximum_y;
		}
		maximum_y = get_max_road_y(simulator.getRoadSegments());
		if (maximum_y > model_height) {
			model_height = maximum_y;
		}
		
		return new View(model_width, model_height);
	}
	
	
	

	private double get_max_road_y(RoadSegment[] roadSegments) {
		double max_y = 0;
		for (RoadSegment road : roadSegments) {
			if (road.getStartPosition().getY() > max_y) {
				max_y = road.getStartPosition().getY();				
			}
			if (road.getEndPosition().getY() > max_y) {
				max_y = road.getEndPosition().getY();				
			}
		}
		return max_y;
	}

	private double get_max_car_y(Car[] cars) {
		double max_y = 0;
		for (Car car : cars) {
			if (car.getPosition().getY() > max_y) {
				max_y = car.getPosition().getY();				
			}			
		}
		return max_y;
	}

	private double get_max_road_x(RoadSegment[] roadSegments) {
		double max_x = 0;
		for (RoadSegment road : roadSegments) {
			if (road.getStartPosition().getX() > max_x) {
				max_x = road.getStartPosition().getX();				
			}
			if (road.getEndPosition().getX() > max_x) {
				max_x = road.getEndPosition().getX();				
			}
		}
		return max_x;
	}

	private double get_max_car_x(Car[] cars) {
		double max_x = 0;
		for (Car car : cars) {
			if (car.getPosition().getX() > max_x) {
				max_x = car.getPosition().getX();				
			}
		}
		return max_x;
	}	
	
}
