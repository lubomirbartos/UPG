
public class View {
	
	private final double model_width;
	private final double model_height;	
	private double window_width;
	private double window_height;
	private double transform_x; // timhle budeme nasobit souradnice modelu ze simulatoru
	private double transform_y;
	private double transform;	
	private double offset_x;
	private double offset_y;
	
	
	public int x(double x) {
		return (int) (x * this.get_transform() + this.get_offset_x());
	}
	public int y(double y) {
		return (int) (y * this.get_transform() + this.get_offset_y());
	}	
	public int scale(double number) {
		return (int) (number * this.get_transform());
	}	
	
	public View(double model_width, double model_height) {
		this.model_width = model_width;
		this.model_height = model_height;
		this.transform_x = 1;
		this.transform_y = 1;
		this.transform = 1;
		this.window_width = 640;
		this.window_height = 640;
		this.offset_x = 0;
		this.offset_y = 0;
	}
	public double get_model_height() {
		return model_height;
	}
	
	public double get_model_width() {
		return model_width;
	}
	
	public double get_window_width() {
		return window_width;
	}
	
	public void set_window_width(double window_width) {
		this.window_width = window_width;
	}
	
	public double get_window_height() {
		return window_height;
	}
	
	public void set_window_height(double window_height) {
		this.window_height = window_height;
	}
	
	public double get_transform_x() {
		return transform_x;
	}
	
	public void set_transform_x(double transform_x) {
		this.transform_x = transform_x;
	}
	
	public double get_offset_x() {
		return this.offset_x;
	}
	
	public double get_offset_y() {
		return this.offset_y;
	}
	
	public double get_transform() {
		return transform;
	}
	
	public void set_transform(double transform) {
		this.transform = transform;
	}

	public double get_transform_y() {
		return transform_y;
	}
	
	public void set_transform_y(double transform_y) {
		this.transform_y = transform_y;
	}
	
	public void set_offset_y(double offset_y) {
		this.offset_y = offset_y;
		
	}	
	
	public void set_offset_x(double offset_x) {
		this.offset_x = offset_x;
	}	
}
