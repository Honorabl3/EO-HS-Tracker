import javax.swing.*;
import java.awt.*;

public class GraphPanel extends JPanel
{

    public double[] data; // Your data for the graph
    private int padding = 1; // Padding around the graph
    private int pointSize = 2; // Size of data points
    private Color graphColor = Color.RED; // Color of the graph

    public GraphPanel(double[] data) {
        this.data = data;
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        int width = getWidth();
        int height = getHeight();

        // Draw x and y axis
        //g2d.setColor(Color.GREEN);
        //g2d.drawLine(padding, padding, padding, height - padding);
        //g2d.drawLine(padding, height - padding, width - padding, height - padding);

        // Calculate the scale and position of the graph
        double xScale = (double) (width - 2 * padding) / (data.length - 1);
        double yScale = (double) (height - 2 * padding) / (getMaxY() - getMinY());

        // Draw the graph
        g2d.setColor(graphColor);
        for (int i = 0; i < data.length - 1; i++)
        {
            int x1 = (int) (i * xScale + padding);
            int y1 = (int) ((getMaxY() - data[i]) * yScale + padding);
            int x2 = (int) ((i + 1) * xScale + padding);
            int y2 = (int) ((getMaxY() - data[i + 1]) * yScale + padding);
            
         // Calculate the angle between two points
            double angle = Math.atan2(y2 - y1, x2 - x1);
            
            // Convert the angle to degrees
            angle = Math.toDegrees(angle);
            
            // Determine the color based on the angle
            Color lineColor;
            if (angle > 0) {
                // Angle is positive, line is going up
                lineColor = Color.GREEN;
            } else if (angle < 0) {
                // Angle is negative, line is going down
                lineColor = Color.RED;
            } else {
                // Angle is zero, line is horizontal
                lineColor = Color.GREEN;
            }
            
            // Set the color and draw the line
            g2d.setColor(lineColor);
            g2d.drawLine(x1, y1, x2, y2);
            
            // Draw the oval with the color green
            g2d.setColor(Color.WHITE);
            g2d.fillOval(x1 - pointSize / 2, y1 - pointSize / 2, pointSize, pointSize);
        }
    }

    private double getMaxY() {
        //double max = Double.MIN_VALUE;
        //for (double value : data) {
        //    if (value > max) {
        //        max = value;
        //    }
        //}
        //return max;
    	return 1.0;
    }

    private double getMinY() {
        //double min = Double.MAX_VALUE;
        //for (double value : data) {
        //    if (value < min) {
        //        min = value;
        //    }
        //}
        //return min;
    	return 0.0;
    }

    /*public static void main(String[] args) {
        JFrame frame = new JFrame("Graph Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        double[] data = {0.5, 0.6, 0.8, 0.7, 0.9, 0.65, 0.7}; // Example data
        GraphPanel graphPanel = new GraphPanel(data);

        frame.add(graphPanel);
        frame.setSize(400, 300);
        frame.setVisible(true);
    }*/
}