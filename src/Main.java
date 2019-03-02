import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Point2D;
import java.util.concurrent.TimeUnit;

import javax.swing.JFrame;

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
	private double model_width;
	private double model_height;
	private double window_width = 500;
	private double window_height = 500;
	private static Model  model;
	double transform_x; // timhle budeme nasobit souradnice modelu ze simulatoru
	double transform_y;


	public static void main(String[] args) {

		//check arguments
		// create Simulator
		Simulator simulator = new Simulator();
		Scenario scenario = new Scenario(simulator);
		simulator.addScenario(scenario);
		String[] scenarios = simulator.getScenarios();
		System.out.println(scenarios[0]);
		simulator.runScenario(scenarios[0]);
		new Main(simulator);
		return;
	}

	private Main(Simulator simulator) {
		super("Crossroad");
		setSize((int) window_width, (int) window_height);
		BorderLayout layout = new BorderLayout();
		setLayout(layout);
		setLocationRelativeTo(null);
		setVisible(true);
		addWindowListener(new WindowAdapter()
			{public void windowClosing(WindowEvent e)
			     {dispose(); System.exit(0);}
			}
		);
		
		Cars cars = new Cars(simulator.getCars());
		Roads roads = new Roads(simulator.getRoadSegments(), simulator.getCrossroads());
		model = new Model(roads, cars, transform_x, transform_y);
		add(model);

		while(true) {

			computeModelDimensions(simulator);
			
			
	//		computeModel2WindowTransformation(width, height); //height and width from window?
	
			cars = new Cars(simulator.getCars());
			roads = new Roads(simulator.getRoadSegments(), simulator.getCrossroads());
			model.update(roads, cars, transform_x, transform_y);

			pack();
			setSize((int) window_width, (int) window_height);
		
			try {
				TimeUnit.MILLISECONDS.sleep(200);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			simulator.nextStep(1);
			cars = new Cars(simulator.getCars());
			roads = new Roads(simulator.getRoadSegments(), simulator.getCrossroads());

//			model = new Model(roads, cars, transform_x, transform_y);
//			model.update(roads, cars, transform_x, transform_y);
			repaint();
		}

	}

	// Computes minimum and maximum values of given model.
	// Stores model to variable
	private void computeModelDimensions(Simulator simulator) {
		double maximum_x = 0;
		double maximum_y = 0;
		
		// najdi nejvzdalenejsi auto X
		// najdi nejvzdalenejsi auto Y
		for (Car car : simulator.getCars()) {
			if (car.getPosition().getX() > maximum_x) {
				maximum_x = car.getPosition().getX();				
			}
			if (car.getPosition().getY() > maximum_y) {
				maximum_y = car.getPosition().getY();				
			}			
		}
		// najdi nejvzdalenejsi silnici X      => model width, height
		// najdi nejvzdalenejsi silnici Y
		for (RoadSegment road : simulator.getRoadSegments()) {
			if (road.getStartPosition().getX() > maximum_x || road.getEndPosition().getX() > maximum_x) {
				maximum_x = road.getStartPosition().getX();				
			}
			if (road.getStartPosition().getY() > maximum_y || road.getEndPosition().getY() > maximum_y) {
				maximum_y = road.getStartPosition().getY();				
			}
		}
		// najdi nejvzdalenejsi krizovatku X    ... mozna neni potreba
		// najdi nejvzdalenejsi krizovatku Y
		
		
		model_width = maximum_x;
		model_height = maximum_y;
		
		
		// get window width and height
		window_width = getBounds().getSize().getWidth();
		window_height = getBounds().getSize().getHeight();

		// transform model to window
		
		transform_x = (window_width * 0.5) / model_width;
		transform_y = (window_height * 0.5) / model_height;
	}

	private static void computeModel2WindowTransformation(int width, int height) {
		
	}

	private static Point2D model2window(Model model) {
		return new Point2D.Float();
	}
	
	private static void drawCar(Point2D position, double orientation, int lenght, int width, Graphics2D g) {
		
	}
	
	private static void drawLane(Point2D start, Point2D end, int size, Graphics2D g) {
		
	}
	
	private static void drawRoadSegment(RoadSegment road, Graphics2D g) {
		
	}
	
	private static void drawCrossRoad(CrossRoad cr, Graphics2D g) {
		
	}
	


//	private static void drawCars(Cars cars, Graphics2D g) {
//		
//		Cars car;
//		for(Car car : cars){
//			drawCar(position, orientation, lenght, width, g);
//			
//		}
//	}
//
//	private static void drawTrafficState(Simulator sim, Graphics2D g) {
//		drawCars(cars);
//		drawLane(start, end, size, g);
//		drawRoadSegment(road, g);
//		drawCrossRoad(cr, g);
//		
//	}
}
