import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;

public class DrawConcentricSquaresTest {
    private static final double TOL = 0.5;      // pixels and degrees

    private World world;
    private Turtle t;

    @BeforeEach
    void setUp() {
        world = new World(600, 400);
        t = new Turtle(world);                  // (300, 200) facing up, pen down
    }

    private static double headingDiff(double a, double b) {
        double d = Math.abs(((a - b) % 360 + 360) % 360);
        return Math.min(d, 360 - d);
    }

    private static double[] bbox(ArrayList<World.Segment> segs) {
        double minX = Double.MAX_VALUE, minY = Double.MAX_VALUE;
        double maxX = -Double.MAX_VALUE, maxY = -Double.MAX_VALUE;
        for (World.Segment s : segs) {
            minX = Math.min(minX, Math.min(s.x1, s.x2));
            maxX = Math.max(maxX, Math.max(s.x1, s.x2));
            minY = Math.min(minY, Math.min(s.y1, s.y2));
            maxY = Math.max(maxY, Math.max(s.y1, s.y2));
        }
        return new double[] {minX, minY, maxX, maxY};
    }

    @DisplayName("drawConcentricSquares(t, 3, 40, 30): 3 squares x 4 sides = 12 segments")
    @Test
    void drawConcentricSquares_Test01() {
        Drawings.drawConcentricSquares(t, 3, 40, 30);
        assertEquals(12, world.getSegments().size(), "count = 3 means 12 segments");
    }

    @DisplayName("drawConcentricSquares(t, 3, 40, 30): sides are 40,40,40,40, 70,70,70,70, 100,100,100,100 - smallest first")
    @Test
    void drawConcentricSquares_Test02() {
        Drawings.drawConcentricSquares(t, 3, 40, 30);
        ArrayList<World.Segment> segs = world.getSegments();
        assertEquals(12, segs.size(), "expected 12 segments");
        for (int i = 0; i < segs.size(); i++) {
            int square = i / 4;
            assertEquals(40.0 + 30.0 * square, segs.get(i).length(), TOL, "segment " + i + " belongs to square " + square + " and should be " + (40 + 30 * square) + " long");
        }
    }

    @DisplayName("drawConcentricSquares(t, 3, 40, 30): every square starts at the shared corner (300, 200)")
    @Test
    void drawConcentricSquares_Test03() {
        Drawings.drawConcentricSquares(t, 3, 40, 30);
        ArrayList<World.Segment> segs = world.getSegments();
        assertEquals(12, segs.size(), "expected 12 segments");
        for (int k = 0; k < 3; k++) {
            assertEquals(300.0, segs.get(k * 4).x1, TOL, "square " + k + " should start at the start corner (x)");
            assertEquals(200.0, segs.get(k * 4).y1, TOL, "square " + k + " should start at the start corner (y)");
        }
    }

    @DisplayName("drawConcentricSquares(t, 3, 40, 30): turtle ends at the start facing up")
    @Test
    void drawConcentricSquares_Test04() {
        Drawings.drawConcentricSquares(t, 3, 40, 30);
        assertEquals(12, world.getSegments().size(), "expected 12 segments");
        assertEquals(300.0, t.getXPos(), TOL, "x should be back at the start");
        assertEquals(200.0, t.getYPos(), TOL, "y should be back at the start");
        assertEquals(0.0, headingDiff(0, t.getHeading()), TOL, "each square turns a full 360; heading was " + t.getHeading());
    }

    @DisplayName("drawConcentricSquares(t, 3, 40, 30): the whole drawing is the largest square - 100 x 100")
    @Test
    void drawConcentricSquares_Test05() {
        Drawings.drawConcentricSquares(t, 3, 40, 30);
        assertEquals(12, world.getSegments().size(), "expected 12 segments");
        double[] b = bbox(world.getSegments());
        assertEquals(100.0, b[2] - b[0], TOL, "largest side = 40 + 2 x 30 = 100");
        assertEquals(100.0, b[3] - b[1], TOL, "largest side = 40 + 2 x 30 = 100");
    }

    @DisplayName("drawConcentricSquares(t, 1, 55, 10): one square of 55 - 4 segments")
    @Test
    void drawConcentricSquares_Test06() {
        Drawings.drawConcentricSquares(t, 1, 55, 10);
        ArrayList<World.Segment> segs = world.getSegments();
        assertEquals(4, segs.size(), "count = 1 means one square");
        for (World.Segment s : segs) {
            assertEquals(55.0, s.length(), TOL, "the only square has side smallest = 55; step is never added");
        }
    }

    @DisplayName("drawConcentricSquares(t, 3, 40, 0): step 0 draws the same 40 square three times - 12 segments, 40 x 40")
    @Test
    void drawConcentricSquares_Test07() {
        Drawings.drawConcentricSquares(t, 3, 40, 0);
        ArrayList<World.Segment> segs = world.getSegments();
        assertEquals(12, segs.size(), "expected 12 segments");
        for (World.Segment s : segs) {
            assertEquals(40.0, s.length(), TOL, "with step 0 every side stays 40");
        }
        double[] b = bbox(segs);
        assertEquals(40.0, b[2] - b[0], TOL, "width");
        assertEquals(40.0, b[3] - b[1], TOL, "height");
    }

    @DisplayName("drawConcentricSquares(t, 5, 20, 20): 20 segments, square i has side 20 + 20i and closes, pen down afterwards")
    @Test
    void drawConcentricSquares_Test08() {
        Drawings.drawConcentricSquares(t, 5, 20, 20);
        ArrayList<World.Segment> segs = world.getSegments();
        assertEquals(20, segs.size(), "5 squares = 20 segments");
        for (int k = 0; k < 5; k++) {
            assertEquals(20.0 + 20.0 * k, segs.get(k * 4).length(), TOL, "square " + k + " should have side " + (20 + 20 * k));
            assertEquals(300.0, segs.get(k * 4 + 3).x2, TOL, "square " + k + " should close at the start (x)");
            assertEquals(200.0, segs.get(k * 4 + 3).y2, TOL, "square " + k + " should close at the start (y)");
        }
        assertTrue(t.isPenDown(), "the pen must be down when the method returns");
        double[] b = bbox(segs);
        assertEquals(100.0, b[2] - b[0], TOL, "largest square is 100");
    }
}
