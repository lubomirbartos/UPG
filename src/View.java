
public class View {
	
	public static double model_width = 0;
	public static double model_height = 0;
	
	public static double window_width = 500;
	public static double window_height = 500;

	public static double transform_x = 1; // timhle budeme nasobit souradnice modelu ze simulatoru
	public static double transform_y = 1;
	public static double transform = 1;
	
	public static double offset_x = 0;
	public static double offset_y = 0;
	
	
	public static int x(double x) {
		return (int) (x * transform + offset_x);
	}
	public static int y(double y) {
		return (int) (y * transform + offset_y);
	}	
	
	public View() {
		
	}
	
	
	


	
}
