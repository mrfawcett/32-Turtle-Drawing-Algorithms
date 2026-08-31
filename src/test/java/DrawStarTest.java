import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;

public class DrawStarTest {
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

    @DisplayName("drawStar(t, 150): exactly 5 strokes")
    @Test
    void drawStar_Test01() {
        Drawings.drawStar(t, 150);
        assertEquals(5, world.getSegments().size(), "a five-pointed star is 5 strokes");
    }

    @DisplayName("drawStar(t, 150): every stroke is 150 long")
    @Test
    void drawStar_Test02() {
        Drawings.drawStar(t, 150);
        ArrayList<World.Segment> segs = world.getSegments();
        assertEquals(5, segs.size(), "expected 5 segments");
        for (int i = 0; i < segs.size(); i++) {
            assertEquals(150.0, segs.get(i).length(), TOL, "stroke " + i + " should be 150 long");
        }
    }

    @DisplayName("drawStar(t, 150): the path closes - turtle ends back at (300, 200)")
    @Test
    void drawStar_Test03() {
        Drawings.drawStar(t, 150);
        assertEquals(5, world.getSegments().size(), "expected 5 segments");
        assertEquals(300.0, t.getXPos(), TOL, "x should be back at the start");
        assertEquals(200.0, t.getYPos(), TOL, "y should be back at the start");
    }

    @DisplayName("drawStar(t, 150): heading returns to 0 - 5 x 144 = 720, two full laps")
    @Test
    void drawStar_Test04() {
        Drawings.drawStar(t, 150);
        assertEquals(5, world.getSegments().size(), "expected 5 segments");
        assertEquals(0.0, headingDiff(0, t.getHeading()), TOL, "720 is a multiple of 360, so the turtle faces up again; heading was " + t.getHeading());
    }

    @DisplayName("drawStar(t, 150): after two strokes the turtle is only 92.7 from the start - it turned 144, not 72")
    @Test
    void drawStar_Test05() {
        Drawings.drawStar(t, 150);
        ArrayList<World.Segment> segs = world.getSegments();
        assertEquals(5, segs.size(), "expected 5 segments");
        World.Segment second = segs.get(1);
        double dist = Math.sqrt(Math.pow(second.x2 - 300, 2) + Math.pow(second.y2 - 200, 2));
        assertEquals(92.705, dist, TOL, "with turn 144 the second stroke folds back toward the start (a pentagon's turn of 72 would put it 242.7 away)");
    }

    @DisplayName("drawStar(t, 150): the star is 142.7 wide and 150 tall")
    @Test
    void drawStar_Test06() {
        Drawings.drawStar(t, 150);
        assertEquals(5, world.getSegments().size(), "expected 5 segments");
        double[] b = bbox(world.getSegments());
        assertEquals(142.658, b[2] - b[0], TOL, "bounding box width of a star of stroke 150");
        assertEquals(150.0, b[3] - b[1], TOL, "the first stroke goes straight up 150, and that is the full height");
    }

    @DisplayName("drawStar(t, 100): the size parameter is used - 5 strokes of 100, 95.1 wide, 100 tall")
    @Test
    void drawStar_Test07() {
        Drawings.drawStar(t, 100);
        ArrayList<World.Segment> segs = world.getSegments();
        assertEquals(5, segs.size(), "expected 5 segments");
        for (World.Segment s : segs) {
            assertEquals(100.0, s.length(), TOL, "each stroke should be 100, not a hard-coded 150");
        }
        double[] b = bbox(segs);
        assertEquals(95.106, b[2] - b[0], TOL, "bounding box width");
        assertEquals(100.0, b[3] - b[1], TOL, "bounding box height");
    }

    @DisplayName("drawStar(t, 150): the path is connected - each stroke starts where the last one ended")
    @Test
    void drawStar_Test08() {
        Drawings.drawStar(t, 150);
        ArrayList<World.Segment> segs = world.getSegments();
        assertEquals(5, segs.size(), "expected 5 segments");
        for (int i = 1; i < segs.size(); i++) {
            assertEquals(segs.get(i - 1).x2, segs.get(i).x1, TOL, "stroke " + i + " should start where stroke " + (i - 1) + " ended (x)");
            assertEquals(segs.get(i - 1).y2, segs.get(i).y1, TOL, "stroke " + i + " should start where stroke " + (i - 1) + " ended (y)");
        }
    }
}
