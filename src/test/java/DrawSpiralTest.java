import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;

public class DrawSpiralTest {
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

    @DisplayName("drawSpiral(t, 8, 10, 5): exactly 8 strokes")
    @Test
    void drawSpiral_Test01() {
        Drawings.drawSpiral(t, 8, 10, 5);
        assertEquals(8, world.getSegments().size(), "segments = 8 means 8 strokes");
    }

    @DisplayName("drawSpiral(t, 8, 10, 5): stroke i is 10 + 5*i long - 10, 15, 20, 25, 30, 35, 40, 45")
    @Test
    void drawSpiral_Test02() {
        Drawings.drawSpiral(t, 8, 10, 5);
        ArrayList<World.Segment> segs = world.getSegments();
        assertEquals(8, segs.size(), "expected 8 segments");
        for (int i = 0; i < segs.size(); i++) {
            assertEquals(10.0 + 5.0 * i, segs.get(i).length(), TOL, "stroke " + i + " should be " + (10 + 5 * i) + " long");
        }
    }

    @DisplayName("drawSpiral(t, 8, 10, 5): every stroke is at a right angle to the one before, and starts where it ended")
    @Test
    void drawSpiral_Test03() {
        Drawings.drawSpiral(t, 8, 10, 5);
        ArrayList<World.Segment> segs = world.getSegments();
        assertEquals(8, segs.size(), "expected 8 segments");
        for (int i = 1; i < segs.size(); i++) {
            World.Segment a = segs.get(i - 1), b = segs.get(i);
            assertEquals(a.x2, b.x1, TOL, "stroke " + i + " should start where stroke " + (i - 1) + " ended (x)");
            assertEquals(a.y2, b.y1, TOL, "stroke " + i + " should start where stroke " + (i - 1) + " ended (y)");
            double dot = (a.x2 - a.x1) * (b.x2 - b.x1) + (a.y2 - a.y1) * (b.y2 - b.y1);
            assertEquals(0.0, dot, 40.0, "strokes " + (i - 1) + " and " + i + " should be perpendicular (turn exactly 90)");
        }
    }

    @DisplayName("drawSpiral(t, 8, 10, 5): heading returns to 0 after 8 right turns (720 degrees)")
    @Test
    void drawSpiral_Test04() {
        Drawings.drawSpiral(t, 8, 10, 5);
        assertEquals(8, world.getSegments().size(), "expected 8 segments");
        assertEquals(0.0, headingDiff(0, t.getHeading()), TOL, "8 x 90 = 720 = 2 x 360; heading was " + t.getHeading());
    }

    @DisplayName("drawSpiral(t, 5, 10, 5): heading is 90 after 5 right turns (450 = 360 + 90)")
    @Test
    void drawSpiral_Test05() {
        Drawings.drawSpiral(t, 5, 10, 5);
        assertEquals(5, world.getSegments().size(), "expected 5 segments");
        assertEquals(0.0, headingDiff(90, t.getHeading()), TOL, "5 x 90 = 450, so the turtle faces right (90); heading was " + t.getHeading());
    }

    @DisplayName("drawSpiral(t, 8, 10, 5): turns are clockwise - the 2nd stroke goes to the RIGHT after the 1st goes up")
    @Test
    void drawSpiral_Test06() {
        Drawings.drawSpiral(t, 8, 10, 5);
        ArrayList<World.Segment> segs = world.getSegments();
        assertEquals(8, segs.size(), "expected 8 segments");
        assertEquals(190.0, segs.get(0).y2, TOL, "1st stroke goes straight up 10 from (300, 200)");
        assertEquals(315.0, segs.get(1).x2, TOL, "2nd stroke goes right 15: turnRight, not turnLeft");
        assertEquals(190.0, segs.get(1).y2, TOL, "2nd stroke is horizontal");
    }

    @DisplayName("drawSpiral(t, 4, 50, 0): step 0 draws a plain square - 4 strokes of 50 that close")
    @Test
    void drawSpiral_Test07() {
        Drawings.drawSpiral(t, 4, 50, 0);
        ArrayList<World.Segment> segs = world.getSegments();
        assertEquals(4, segs.size(), "expected 4 segments");
        for (World.Segment s : segs) {
            assertEquals(50.0, s.length(), TOL, "with step 0 every stroke stays 50");
        }
        assertEquals(300.0, t.getXPos(), TOL, "a square closes: x back at start");
        assertEquals(200.0, t.getYPos(), TOL, "a square closes: y back at start");
    }

    @DisplayName("drawSpiral(t, 1, 30, 7): one stroke of 30, then one right turn")
    @Test
    void drawSpiral_Test08() {
        Drawings.drawSpiral(t, 1, 30, 7);
        assertEquals(1, world.getSegments().size(), "segments = 1 means one stroke");
        assertEquals(30.0, world.getSegments().get(0).length(), TOL, "the only stroke is start long");
        assertEquals(0.0, headingDiff(90, t.getHeading()), TOL, "one right turn: heading 90");
    }

    @DisplayName("drawSpiral(t, 6, 20, 10): the LAST stroke is 70 - declare the length OUTSIDE the loop or it resets to 20 every pass")
    @Test
    void drawSpiral_Test09() {
        Drawings.drawSpiral(t, 6, 20, 10);
        ArrayList<World.Segment> segs = world.getSegments();
        assertEquals(6, segs.size(), "expected 6 segments");
        assertEquals(70.0, segs.get(5).length(), TOL, "stroke 5 should be 20 + 5 x 10 = 70; 20 means the length variable was re-declared inside the loop");
        assertTrue(segs.get(5).length() > segs.get(0).length() + 1, "strokes must grow");
    }

    @DisplayName("drawSpiral(t, 40, 10, 5): the lecture's spiral - pass 40 is 205 long, 4300 pixels traveled")
    @Test
    void drawSpiral_Test10() {
        Drawings.drawSpiral(t, 40, 10, 5);
        ArrayList<World.Segment> segs = world.getSegments();
        assertEquals(40, segs.size(), "expected 40 segments");
        assertEquals(10.0, segs.get(0).length(), TOL, "pass 1: forward 10");
        assertEquals(15.0, segs.get(1).length(), TOL, "pass 2: forward 15");
        assertEquals(205.0, segs.get(39).length(), TOL, "pass 40: forward 205");
        assertEquals(4300.0, t.getDistanceTraveled(), TOL, "10 + 15 + ... + 205 = 4300");
    }
}
