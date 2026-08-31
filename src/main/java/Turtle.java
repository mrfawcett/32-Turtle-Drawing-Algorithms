import java.awt.Color;

/**
 * PROVIDED CLASS - do not edit.
 *
 * A Turtle lives in a World, faces a direction, and drags a pen. Move it
 * with forward()/backward(), rotate it with turnRight()/turnLeft()/turn(),
 * and lift or lower the pen with penUp()/penDown(). While the pen is down,
 * every move draws a line.
 *
 * Headings are in degrees: 0 = up (north), 90 = right (east), 180 = down,
 * 270 = left. turn(d) with a positive d turns clockwise (to the right).
 *
 * A new turtle starts in the center of its world, facing up, pen down,
 * drawing in black with a 1-pixel pen.
 */
public class Turtle {

    private final World world;
    private double x;
    private double y;
    private double heading;      // degrees, 0 = up, clockwise positive
    private boolean penDown = true;
    private Color penColor = Color.BLACK;
    private int penWidth = 1;
    private Color bodyColor = Color.GREEN;
    private boolean visible = true;
    private double distanceTraveled = 0;

    /** Creates a turtle in the center of the world, facing up. */
    public Turtle(World world) {
        this.world = world;
        this.x = world.getWidth() / 2.0;
        this.y = world.getHeight() / 2.0;
        this.heading = 0;
        world.addTurtle(this);
    }

    /** Creates a turtle at (x, y), facing up. */
    public Turtle(World world, int x, int y) {
        this(world);
        this.x = x;
        this.y = y;
    }

    // ---- movement -------------------------------------------------------

    /** Moves forward 100 pixels. */
    public void forward() {
        forward(100);
    }

    /** Moves forward the given number of pixels in the current heading. */
    public void forward(int pixels) {
        double rad = Math.toRadians(heading);
        double nx = x + pixels * Math.sin(rad);
        double ny = y - pixels * Math.cos(rad);
        moveTo(nx, ny);
    }

    /** Moves backward 100 pixels without changing heading. */
    public void backward() {
        backward(100);
    }

    /** Moves backward the given number of pixels without changing heading. */
    public void backward(int pixels) {
        forward(-pixels);
    }

    /**
     * Moves straight to (x, y), drawing a line if the pen is down.
     * Heading does not change.
     */
    public void moveTo(int x, int y) {
        moveTo((double) x, (double) y);
    }

    private void moveTo(double nx, double ny) {
        if (penDown) {
            world.drawSegment(x, y, nx, ny, penColor, penWidth);
        }
        distanceTraveled += Math.sqrt((nx - x) * (nx - x) + (ny - y) * (ny - y));
        x = nx;
        y = ny;
    }

    // ---- turning --------------------------------------------------------

    /** Turns 90 degrees clockwise. */
    public void turnRight() {
        turn(90);
    }

    /** Turns 90 degrees counter-clockwise. */
    public void turnLeft() {
        turn(-90);
    }

    /** Turns the given number of degrees; positive is clockwise (right). */
    public void turn(int degrees) {
        turn((double) degrees);
    }

    /** Turns the given number of degrees; positive is clockwise (right). */
    public void turn(double degrees) {
        heading = ((heading + degrees) % 360 + 360) % 360;
    }

    /** Points the turtle in an absolute direction (0 = up, 90 = right). */
    public void setHeading(double degrees) {
        heading = ((degrees % 360) + 360) % 360;
    }

    // ---- pen ------------------------------------------------------------

    /** Lifts the pen: moves stop drawing. */
    public void penUp() {
        penDown = false;
    }

    /** Lowers the pen: moves draw again. */
    public void penDown() {
        penDown = true;
    }

    public boolean isPenDown() {
        return penDown;
    }

    public void setPenColor(Color color) {
        penColor = color;
    }

    public Color getPenColor() {
        return penColor;
    }

    /** Pen thickness in pixels (at least 1). */
    public void setPenWidth(int width) {
        penWidth = Math.max(1, width);
    }

    public int getPenWidth() {
        return penWidth;
    }

    // ---- the turtle itself ---------------------------------------------

    /** The color of the turtle's body (does not affect drawing). */
    public void setColor(Color color) {
        bodyColor = color;
    }

    public Color getColor() {
        return bodyColor;
    }

    public void hide() {
        visible = false;
    }

    public void show() {
        visible = true;
    }

    public boolean isVisible() {
        return visible;
    }

    // ---- state ----------------------------------------------------------

    /** Current x position (0 is the left edge). */
    public double getXPos() {
        return x;
    }

    /** Current y position (0 is the top edge). */
    public double getYPos() {
        return y;
    }

    /** Current heading in degrees, 0 to 359.999 (0 = up, 90 = right). */
    public double getHeading() {
        return heading;
    }

    /** Total distance moved, pen up or down. */
    public double getDistanceTraveled() {
        return distanceTraveled;
    }

    public World getWorld() {
        return world;
    }

    public String toString() {
        return String.format("Turtle at (%.1f, %.1f) heading %.1f", x, y, heading);
    }
}
