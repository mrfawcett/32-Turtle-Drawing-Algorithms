import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.SwingUtilities;

/**
 * PROVIDED CLASS - do not edit.
 *
 * A World is the canvas that Turtles draw on. (0, 0) is the TOP-LEFT corner;
 * x grows to the right and y grows DOWNWARD, like every screen coordinate
 * system you will meet.
 *
 * The world keeps a picture (so you can see your drawing) and a list of every
 * line segment ever drawn (so the autograder can check your drawing without a
 * screen). Calling show() opens a window when one is available and does
 * nothing on a machine without a display, so the same code runs in the
 * grader and on your laptop.
 */
public class World {

    /** One straight stroke drawn by a turtle with the pen down. */
    public static class Segment {
        public final double x1, y1, x2, y2;
        public final Color color;
        public final int width;

        public Segment(double x1, double y1, double x2, double y2, Color color, int width) {
            this.x1 = x1; this.y1 = y1; this.x2 = x2; this.y2 = y2;
            this.color = color; this.width = width;
        }

        /** Length of this stroke. */
        public double length() {
            return Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
        }

        public String toString() {
            return String.format("(%.1f, %.1f) -> (%.1f, %.1f)", x1, y1, x2, y2);
        }
    }

    private final int width;
    private final int height;
    private final BufferedImage image;
    private final ArrayList<Turtle> turtles = new ArrayList<Turtle>();
    private final ArrayList<Segment> segments = new ArrayList<Segment>();
    private JFrame frame;
    private JLabel label;

    /** A 640 x 480 world. */
    public World() {
        this(640, 480);
    }

    /** A world of the given size in pixels. */
    public World(int width, int height) {
        this.width = width;
        this.height = height;
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);
        g.dispose();
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    /** Every turtle that has been created in this world, in creation order. */
    public ArrayList<Turtle> getTurtles() {
        return turtles;
    }

    /** Every line segment drawn so far, in drawing order. Used by the tests. */
    public ArrayList<Segment> getSegments() {
        return segments;
    }

    /** The picture. Used by the tests and by show(). */
    public BufferedImage getImage() {
        return image;
    }

    /** Wipes the picture and forgets every segment. Turtles keep their positions. */
    public void clear() {
        segments.clear();
        Graphics2D g = image.createGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);
        g.dispose();
        repaint();
    }

    /** Opens a window showing the world, if this machine has a screen. */
    public void show() {
        if (java.awt.GraphicsEnvironment.isHeadless()) {
            return;
        }
        if (frame == null) {
            frame = new JFrame("World");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            label = new JLabel(new ImageIcon(image));
            frame.getContentPane().add(label);
            frame.pack();
            frame.setLocationRelativeTo(null);
        }
        frame.setVisible(true);
        repaint();
    }

    void addTurtle(Turtle t) {
        turtles.add(t);
    }

    void drawSegment(double x1, double y1, double x2, double y2, Color color, int penWidth) {
        segments.add(new Segment(x1, y1, x2, y2, color, penWidth));
        Graphics2D g = image.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(color);
        g.setStroke(new java.awt.BasicStroke(penWidth, java.awt.BasicStroke.CAP_ROUND,
                java.awt.BasicStroke.JOIN_ROUND));
        g.drawLine((int) Math.round(x1), (int) Math.round(y1),
                   (int) Math.round(x2), (int) Math.round(y2));
        g.dispose();
        repaint();
    }

    private void repaint() {
        if (label != null) {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    label.repaint();
                }
            });
        }
    }
}
