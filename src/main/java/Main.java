import java.awt.Color;

/**
 * PROVIDED - you do not need to change this file.
 *
 * Builds a World, puts one Turtle in it, and calls every method in Drawings
 * once at a different spot so you can SEE your work. Run it from your IDE: a
 * window opens if your machine has a screen. The autograder never runs this
 * file; it runs the tests in src/test/java, which count and measure the line
 * segments your methods draw. Until you complete a method, its part of the
 * picture is simply blank.
 */
public class Main {

    public static void main(String[] args) {
        World world = new World(600, 400);      // 600 wide, 400 tall; (0,0) is TOP-LEFT
        Turtle t = new Turtle(world);           // starts at (300, 200) facing up
        t.setPenWidth(2);

        // Top row
        jumpTo(t, 40, 150);
        t.setPenColor(Color.BLACK);
        Drawings.drawPolygon(t, 7, 45);         // the heptagon: 360.0 / 7, and it closes

        jumpTo(t, 200, 150);
        t.setPenColor(Color.RED);
        Drawings.drawStar(t, 100);              // turn 144: two laps

        jumpTo(t, 330, 110);
        t.setPenColor(Color.BLUE);
        Drawings.drawSpiral(t, 24, 4, 4);       // strokes 4, 8, 12, ... 96

        jumpTo(t, 470, 150);
        t.setPenColor(new Color(0, 128, 0));    // dark green
        Drawings.drawConcentricSquares(t, 4, 20, 20);

        // Bottom row
        jumpTo(t, 110, 300);
        t.setPenColor(Color.MAGENTA);
        Drawings.drawFlower(t, 12, 4, 60);      // 12 squares, 30 degrees apart

        jumpTo(t, 250, 370);
        t.setPenColor(Color.GRAY);
        Drawings.drawGrid(t, 3, 4, 30);         // 3 rows x 4 columns of 30-pixel cells

        jumpTo(t, 430, 340);
        t.setPenColor(Color.ORANGE);
        Drawings.drawFlower(t, 8, 3, 50);       // 8 triangles, 45 degrees apart

        t.hide();
        world.show();                           // opens a window; does nothing headless
    }

    /** Moves the turtle to (x, y) without drawing, then puts the pen back down. */
    private static void jumpTo(Turtle t, int x, int y) {
        t.penUp();
        t.moveTo(x, y);
        t.penDown();
    }
}
