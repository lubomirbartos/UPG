import javax.swing.*;
import java.awt.*;

public class BasicDrawing31 {

    public static void main (String[] args) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        DrawingPanel3 panel = new DrawingPanel3();
        panel.setPreferredSize(new Dimension(640, 400));

        frame.add(panel);

        frame.pack();

        frame.setVisible(true);
    }
}
