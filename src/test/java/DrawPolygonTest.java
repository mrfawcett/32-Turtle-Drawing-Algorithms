import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;

public class DrawPolygonTest {
    private static final double TOL = 0.5;      // pixels and degrees

    private World world;
    private Turtle t;

    @BeforeEach
    void setUp() {
        world = new World(600, 400);
        t = new Turtle(world);                  // (300, 200) facing up, pen down
    }

    /** Smallest difference between two headings, allowing for wraparound at 360. */
    private static double headingDiff(double a, double b) {
        double d = Math.abs(((a - b) % 360 + 360) % 360);
        return Math.min(d, 360 - d);
    }

    /** {minX, minY, maxX, maxY} of every segment endpoint. */
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

    private void assertClosed(String what) {
        assertEquals(300.0, t.getXPos(), TOL, what + ": x should be back at the start (the path must close)");
        assertEquals(200.0, t.getYPos(), TOL, what + ": y should be back at the start (the path must close)");
    }

    private void assertFacingUp(String what) {
        assertEquals(0.0, headingDiff(0, t.getHeading()), TOL,
                what + ": total turn must be exactly 360, so the heading returns to 0; it was " + t.getHeading());
    }

    @DisplayName("drawPolygon(t, 4, 100): 4 segments of 100 - the square, now as a loop")
    @Test
    void drawPolygon_Test01() {
        Drawings.drawPolygon(t, 4, 100);
        ArrayList<World.Segment> segs = world.getSegments();
        assertEquals(4, segs.size(), "sides = 4 means 4 segments");
        for (int i = 0; i < segs.size(); i++) {
            assertEquals(100.0, segs.get(i).length(), TOL, "segment " + i + " should be 100 long");
        }
    }

    @DisplayName("drawPolygon(t, 4, 100): forward THEN turn - the first side goes straight ahead from (300,200) to (300,100)")
    @Test
    void drawPolygon_Test02() {
        Drawings.drawPolygon(t, 4, 100);
        ArrayList<World.Segment> segs = world.getSegments();
        assertEquals(4, segs.size(), "expected 4 segments");
        assertEquals(300.0, segs.get(0).x1, TOL, "first side starts at the turtle's x");
        assertEquals(200.0, segs.get(0).y1, TOL, "first side starts at the turtle's y");
        assertEquals(300.0, segs.get(0).x2, TOL, "first side goes straight up: x unchanged");
        assertEquals(100.0, segs.get(0).y2, TOL, "first side goes straight up 100: y = 200 - 100");
        assertClosed("square");
        assertFacingUp("square");
    }

    @DisplayName("drawPolygon(t, 3, 150): a triangle - 3 segments, turn 120, closes, faces up again")
    @Test
    void drawPolygon_Test03() {
        Drawings.drawPolygon(t, 3, 150);
        ArrayList<World.Segment> segs = world.getSegments();
        assertEquals(3, segs.size(), "sides = 3 means 3 segments");
        for (World.Segment s : segs) {
            assertEquals(150.0, s.length(), TOL, "each side should be 150");
        }
        assertClosed("triangle");
        assertFacingUp("triangle");
    }

    @DisplayName("drawPolygon(t, 5, 100): pentagon closes - turn 72 (360/5), NOT the interior angle 108")
    @Test
    void drawPolygon_Test04() {
        Drawings.drawPolygon(t, 5, 100);
        assertEquals(5, world.getSegments().size(), "sides = 5 means 5 segments");
        assertClosed("pentagon (turning 108 wanders off and never closes)");
        assertFacingUp("pentagon");
    }

    @DisplayName("drawPolygon(t, 5, 100): a real pentagon is 153.9 wide and 161.8 tall - not a star (turn 72, not 144)")
    @Test
    void drawPolygon_Test05() {
        Drawings.drawPolygon(t, 5, 100);
        assertEquals(5, world.getSegments().size(), "expected 5 segments");
        double[] b = bbox(world.getSegments());
        assertEquals(153.884, b[2] - b[0], TOL, "pentagon of side 100 with a vertical first side is 153.9 across");
        assertEquals(161.803, b[3] - b[1], TOL, "pentagon of side 100 with a vertical first side is 161.8 tall");
    }

    @DisplayName("drawPolygon(t, 6, 80): hexagon - 6 segments, 138.6 wide, 160 tall, closes")
    @Test
    void drawPolygon_Test06() {
        Drawings.drawPolygon(t, 6, 80);
        assertEquals(6, world.getSegments().size(), "sides = 6 means 6 segments");
        double[] b = bbox(world.getSegments());
        assertEquals(138.564, b[2] - b[0], TOL, "hexagon of side 80 is 80 x sqrt(3) = 138.6 across");
        assertEquals(160.0, b[3] - b[1], TOL, "hexagon of side 80 is 2 x 80 = 160 tall");
        assertClosed("hexagon");
    }

    @DisplayName("drawPolygon(t, 7, 100): the heptagon CLOSES within 0.5 px - 360 / 7 (integer division) leaves a 6 px gap")
    @Test
    void drawPolygon_Test07() {
        Drawings.drawPolygon(t, 7, 100);
        assertEquals(7, world.getSegments().size(), "sides = 7 means 7 segments");
        double gap = Math.sqrt(Math.pow(t.getXPos() - 300, 2) + Math.pow(t.getYPos() - 200, 2));
        assertEquals(0.0, gap, TOL, "the heptagon should close; a gap of about 6 px means you used 360 / sides instead of 360.0 / sides");
    }

    @DisplayName("drawPolygon(t, 7, 100): total turn is 360 within 0.5 degrees - 7 x 51 = 357 is not good enough")
    @Test
    void drawPolygon_Test08() {
        Drawings.drawPolygon(t, 7, 100);
        assertEquals(7, world.getSegments().size(), "expected 7 segments");
        assertEquals(0.0, headingDiff(0, t.getHeading()), TOL,
                "heading should return to 0 (within 0.5); it was " + t.getHeading() + " - 357 means 360 / 7 was done in integer arithmetic");
    }

    @DisplayName("drawPolygon(t, 11, 40): 11 sides of 40, closes and faces up (360.0 / 11 = 32.727...)")
    @Test
    void drawPolygon_Test09() {
        Drawings.drawPolygon(t, 11, 40);
        ArrayList<World.Segment> segs = world.getSegments();
        assertEquals(11, segs.size(), "sides = 11 means 11 segments");
        for (World.Segment s : segs) {
            assertEquals(40.0, s.length(), TOL, "each side should be 40");
        }
        assertClosed("11-gon");
        assertFacingUp("11-gon");
    }

    @DisplayName("drawPolygon(t, 8, 60): octagon - 8 connected segments of 60, each starting where the last ended")
    @Test
    void drawPolygon_Test10() {
        Drawings.drawPolygon(t, 8, 60);
        ArrayList<World.Segment> segs = world.getSegments();
        assertEquals(8, segs.size(), "sides = 8 means 8 segments");
        for (int i = 1; i < segs.size(); i++) {
            assertEquals(segs.get(i - 1).x2, segs.get(i).x1, TOL, "segment " + i + " should start where segment " + (i - 1) + " ended (x)");
            assertEquals(segs.get(i - 1).y2, segs.get(i).y1, TOL, "segment " + i + " should start where segment " + (i - 1) + " ended (y)");
            assertEquals(60.0, segs.get(i).length(), TOL, "segment " + i + " should be 60 long");
        }
        assertClosed("octagon");
    }

    @DisplayName("drawPolygon twice: (3, 50) then (4, 50) gives 7 segments and the turtle is still at the start")
    @Test
    void drawPolygon_Test11() {
        Drawings.drawPolygon(t, 3, 50);
        Drawings.drawPolygon(t, 4, 50);
        ArrayList<World.Segment> segs = world.getSegments();
        assertEquals(7, segs.size(), "3 + 4 segments");
        assertEquals(300.0, segs.get(3).x1, TOL, "the square should start at the start point because the triangle returned there");
        assertEquals(200.0, segs.get(3).y1, TOL, "the square should start at the start point because the triangle returned there");
        assertClosed("triangle then square");
        assertFacingUp("triangle then square");
    }
}
