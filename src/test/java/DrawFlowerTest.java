import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;

public class DrawFlowerTest {
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

    @DisplayName("drawFlower(t, 12, 4, 80): 12 squares x 4 sides = 48 segments")
    @Test
    void drawFlower_Test01() {
        Drawings.drawFlower(t, 12, 4, 80);
        assertEquals(48, world.getSegments().size(), "12 outer passes x 4 inner passes = 48 forward calls");
    }

    @DisplayName("drawFlower(t, 12, 4, 80): every segment is 80 long")
    @Test
    void drawFlower_Test02() {
        Drawings.drawFlower(t, 12, 4, 80);
        ArrayList<World.Segment> segs = world.getSegments();
        assertEquals(48, segs.size(), "expected 48 segments");
        for (int i = 0; i < segs.size(); i++) {
            assertEquals(80.0, segs.get(i).length(), TOL, "segment " + i + " should be 80 long");
        }
    }

    @DisplayName("drawFlower(t, 12, 4, 80): turtle ends where it started, facing the way it started")
    @Test
    void drawFlower_Test03() {
        Drawings.drawFlower(t, 12, 4, 80);
        assertEquals(48, world.getSegments().size(), "expected 48 segments");
        assertEquals(300.0, t.getXPos(), TOL, "x should be back at the start");
        assertEquals(200.0, t.getYPos(), TOL, "y should be back at the start");
        assertEquals(0.0, headingDiff(0, t.getHeading()), TOL, "12 turns of 30 = 360, so the heading is 0 again; it was " + t.getHeading());
    }

    @DisplayName("drawFlower(t, 12, 4, 80): every petal starts at the center (300, 200)")
    @Test
    void drawFlower_Test04() {
        Drawings.drawFlower(t, 12, 4, 80);
        ArrayList<World.Segment> segs = world.getSegments();
        assertEquals(48, segs.size(), "expected 48 segments");
        for (int p = 0; p < 12; p++) {
            World.Segment first = segs.get(p * 4);
            assertEquals(300.0, first.x1, TOL, "petal " + p + " should start at the center (x) - each polygon must return to where it began");
            assertEquals(200.0, first.y1, TOL, "petal " + p + " should start at the center (y) - each polygon must return to where it began");
        }
    }

    @DisplayName("drawFlower(t, 12, 4, 80): every petal is a closed square - its 4th side ends at the center")
    @Test
    void drawFlower_Test05() {
        Drawings.drawFlower(t, 12, 4, 80);
        ArrayList<World.Segment> segs = world.getSegments();
        assertEquals(48, segs.size(), "expected 48 segments");
        for (int p = 0; p < 12; p++) {
            World.Segment last = segs.get(p * 4 + 3);
            assertEquals(300.0, last.x2, TOL, "petal " + p + " should close back at the center (x)");
            assertEquals(200.0, last.y2, TOL, "petal " + p + " should close back at the center (y)");
        }
    }

    @DisplayName("drawFlower(t, 12, 4, 80): petal 1 is rotated 30 degrees (360/12) - its first side ends at (340, 130.7), not (380, 200)")
    @Test
    void drawFlower_Test06() {
        Drawings.drawFlower(t, 12, 4, 80);
        ArrayList<World.Segment> segs = world.getSegments();
        assertEquals(48, segs.size(), "expected 48 segments");
        World.Segment s = segs.get(4);          // first side of the second petal
        assertEquals(340.0, s.x2, TOL, "heading 30: x = 300 + 80 sin(30) = 340. If you got 380 you turned 90 (360 / petalSides) instead of 30 (360 / petals)");
        assertEquals(130.718, s.y2, TOL, "heading 30: y = 200 - 80 cos(30) = 130.7");
    }

    @DisplayName("drawFlower(t, 12, 4, 80): the whole flower is 218.6 x 218.6")
    @Test
    void drawFlower_Test07() {
        Drawings.drawFlower(t, 12, 4, 80);
        assertEquals(48, world.getSegments().size(), "expected 48 segments");
        double[] b = bbox(world.getSegments());
        assertEquals(218.564, b[2] - b[0], TOL, "12 squares of 80 rotated 30 apart span 218.6 (160 means the petals were not rotated 30 apart)");
        assertEquals(218.564, b[3] - b[1], TOL, "and the same height");
    }

    @DisplayName("drawFlower(t, 8, 3, 100): 8 triangles 45 degrees apart - 24 segments, faces up at the end, petal 1 heads 45")
    @Test
    void drawFlower_Test08() {
        Drawings.drawFlower(t, 8, 3, 100);
        ArrayList<World.Segment> segs = world.getSegments();
        assertEquals(24, segs.size(), "8 x 3 = 24 segments");
        assertEquals(0.0, headingDiff(0, t.getHeading()), TOL, "8 turns of 45 = 360; a turn of 120 (the triangle's angle) would leave heading 240; it was " + t.getHeading());
        World.Segment s = segs.get(3);          // first side of the second petal
        assertEquals(370.711, s.x2, TOL, "heading 45: x = 300 + 100 sin(45)");
        assertEquals(129.289, s.y2, TOL, "heading 45: y = 200 - 100 cos(45)");
    }

    @DisplayName("drawFlower(t, 7, 7, 40): 49 segments, closes and faces up - both 360.0 / 7 divisions must be in double")
    @Test
    void drawFlower_Test09() {
        Drawings.drawFlower(t, 7, 7, 40);
        assertEquals(49, world.getSegments().size(), "7 x 7 = 49 segments");
        assertEquals(300.0, t.getXPos(), TOL, "x should be back at the start");
        assertEquals(200.0, t.getYPos(), TOL, "y should be back at the start");
        assertEquals(0.0, headingDiff(0, t.getHeading()), TOL, "heading should return to 0; 357 means 360 / petals was integer division; it was " + t.getHeading());
    }

    @DisplayName("drawFlower(t, 1, 5, 60): one petal is just a pentagon - 5 segments, heading 0 after turning 360")
    @Test
    void drawFlower_Test10() {
        Drawings.drawFlower(t, 1, 5, 60);
        ArrayList<World.Segment> segs = world.getSegments();
        assertEquals(5, segs.size(), "1 x 5 = 5 segments");
        for (World.Segment s : segs) {
            assertEquals(60.0, s.length(), TOL, "each side should be 60");
        }
        assertEquals(0.0, headingDiff(0, t.getHeading()), TOL, "turning 360.0 / 1 = 360 leaves the heading at 0");
    }

    @DisplayName("drawFlower(t, 5, 5, 50): 25 segments, each pentagon closed, whole flower 153.9 x 161.8")
    @Test
    void drawFlower_Test11() {
        Drawings.drawFlower(t, 5, 5, 50);
        ArrayList<World.Segment> segs = world.getSegments();
        assertEquals(25, segs.size(), "5 x 5 = 25 segments");
        for (int p = 0; p < 5; p++) {
            assertEquals(300.0, segs.get(p * 5 + 4).x2, TOL, "petal " + p + " should close at the center (x)");
            assertEquals(200.0, segs.get(p * 5 + 4).y2, TOL, "petal " + p + " should close at the center (y)");
        }
        double[] b = bbox(segs);
        assertEquals(153.884, b[2] - b[0], TOL, "bounding box width");
        assertEquals(161.803, b[3] - b[1], TOL, "bounding box height");
    }
}
