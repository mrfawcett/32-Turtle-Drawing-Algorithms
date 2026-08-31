import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;

public class DrawGridTest {
    private static final double TOL = 0.5;      // pixels

    private World world;
    private Turtle t;

    @BeforeEach
    void setUp() {
        world = new World(600, 400);
        t = new Turtle(world);                  // (300, 200) facing up, pen down
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

    /** How far v is from the nearest multiple of cell, measured from origin. */
    private static double offGrid(double v, double origin, double cell) {
        double k = (v - origin) / cell;
        return Math.abs(k - Math.round(k)) * cell;
    }

    @DisplayName("drawGrid(t, 2, 3, 40): 6 cells x 4 sides = 24 segments - moves between cells must not draw")
    @Test
    void drawGrid_Test01() {
        Drawings.drawGrid(t, 2, 3, 40);
        assertEquals(24, world.getSegments().size(), "2 x 3 squares = 24 segments; more means a move between cells was made with the pen down");
    }

    @DisplayName("drawGrid(t, 2, 3, 40): every segment is 40 long")
    @Test
    void drawGrid_Test02() {
        Drawings.drawGrid(t, 2, 3, 40);
        ArrayList<World.Segment> segs = world.getSegments();
        assertEquals(24, segs.size(), "expected 24 segments");
        for (int i = 0; i < segs.size(); i++) {
            assertEquals(40.0, segs.get(i).length(), TOL, "segment " + i + " should be one cell (40) long");
        }
    }

    @DisplayName("drawGrid(t, 2, 3, 40): the drawing is cols x cell = 120 wide and rows x cell = 80 tall")
    @Test
    void drawGrid_Test03() {
        Drawings.drawGrid(t, 2, 3, 40);
        assertEquals(24, world.getSegments().size(), "expected 24 segments");
        double[] b = bbox(world.getSegments());
        assertEquals(120.0, b[2] - b[0], TOL, "3 columns of 40 = 120 wide; 240 means you never came back to the left edge for row 2");
        assertEquals(80.0, b[3] - b[1], TOL, "2 rows of 40 = 80 tall");
    }

    @DisplayName("drawGrid(t, 2, 3, 40): every segment is horizontal or vertical")
    @Test
    void drawGrid_Test04() {
        Drawings.drawGrid(t, 2, 3, 40);
        ArrayList<World.Segment> segs = world.getSegments();
        assertEquals(24, segs.size(), "expected 24 segments");
        for (int i = 0; i < segs.size(); i++) {
            World.Segment s = segs.get(i);
            boolean vertical = Math.abs(s.x1 - s.x2) < TOL;
            boolean horizontal = Math.abs(s.y1 - s.y2) < TOL;
            assertTrue(vertical || horizontal, "segment " + i + " " + s + " should be axis-aligned: turn exactly 90");
        }
    }

    @DisplayName("drawGrid(t, 2, 3, 40): every corner lies on the 40-pixel lattice - cells are packed edge to edge")
    @Test
    void drawGrid_Test05() {
        Drawings.drawGrid(t, 2, 3, 40);
        ArrayList<World.Segment> segs = world.getSegments();
        assertEquals(24, segs.size(), "expected 24 segments");
        double[] b = bbox(segs);
        for (int i = 0; i < segs.size(); i++) {
            World.Segment s = segs.get(i);
            assertEquals(0.0, offGrid(s.x1, b[0], 40), TOL, "segment " + i + " start x should be a multiple of 40 from the left edge");
            assertEquals(0.0, offGrid(s.y1, b[1], 40), TOL, "segment " + i + " start y should be a multiple of 40 from the top edge");
            assertEquals(0.0, offGrid(s.x2, b[0], 40), TOL, "segment " + i + " end x should be a multiple of 40 from the left edge");
            assertEquals(0.0, offGrid(s.y2, b[1], 40), TOL, "segment " + i + " end y should be a multiple of 40 from the top edge");
        }
    }

    @DisplayName("drawGrid(t, 2, 3, 40): each square is drawn whole - its 4th side ends where its 1st side began")
    @Test
    void drawGrid_Test06() {
        Drawings.drawGrid(t, 2, 3, 40);
        ArrayList<World.Segment> segs = world.getSegments();
        assertEquals(24, segs.size(), "expected 24 segments");
        for (int k = 0; k < 6; k++) {
            World.Segment first = segs.get(k * 4), last = segs.get(k * 4 + 3);
            assertEquals(first.x1, last.x2, TOL, "square " + k + " should close (x)");
            assertEquals(first.y1, last.y2, TOL, "square " + k + " should close (y)");
        }
    }

    @DisplayName("drawGrid(t, 2, 3, 40): all 6 cells are drawn, each exactly once")
    @Test
    void drawGrid_Test07() {
        Drawings.drawGrid(t, 2, 3, 40);
        ArrayList<World.Segment> segs = world.getSegments();
        assertEquals(24, segs.size(), "expected 24 segments");
        double[] b = bbox(segs);
        boolean[][] seen = new boolean[2][3];
        for (int k = 0; k < 6; k++) {
            ArrayList<World.Segment> square = new ArrayList<World.Segment>();
            for (int i = 0; i < 4; i++) square.add(segs.get(k * 4 + i));
            double[] sb = bbox(square);
            assertEquals(40.0, sb[2] - sb[0], TOL, "square " + k + " should be 40 wide");
            assertEquals(40.0, sb[3] - sb[1], TOL, "square " + k + " should be 40 tall");
            int col = (int) Math.round((sb[0] - b[0]) / 40);
            int row = (int) Math.round((sb[1] - b[1]) / 40);
            assertTrue(row >= 0 && row < 2 && col >= 0 && col < 3, "square " + k + " lies outside the 2 x 3 grid");
            assertFalse(seen[row][col], "cell row " + row + ", col " + col + " was drawn twice");
            seen[row][col] = true;
        }
    }

    @DisplayName("drawGrid(t, 1, 1, 40): one cell is just a square - 4 segments, 40 x 40")
    @Test
    void drawGrid_Test08() {
        Drawings.drawGrid(t, 1, 1, 40);
        assertEquals(4, world.getSegments().size(), "1 x 1 = 4 segments");
        double[] b = bbox(world.getSegments());
        assertEquals(40.0, b[2] - b[0], TOL, "width");
        assertEquals(40.0, b[3] - b[1], TOL, "height");
    }

    @DisplayName("drawGrid(t, 3, 1, 40) is 40 wide and 120 tall; drawGrid(t, 1, 4, 40) is 160 wide and 40 tall - rows and cols not swapped")
    @Test
    void drawGrid_Test09() {
        Drawings.drawGrid(t, 3, 1, 40);
        assertEquals(12, world.getSegments().size(), "3 x 1 = 12 segments");
        double[] b = bbox(world.getSegments());
        assertEquals(40.0, b[2] - b[0], TOL, "3 rows, 1 col: 1 cell wide");
        assertEquals(120.0, b[3] - b[1], TOL, "3 rows, 1 col: 3 cells tall");

        World world2 = new World(600, 400);
        Turtle t2 = new Turtle(world2);
        Drawings.drawGrid(t2, 1, 4, 40);
        assertEquals(16, world2.getSegments().size(), "1 x 4 = 16 segments");
        double[] b2 = bbox(world2.getSegments());
        assertEquals(160.0, b2[2] - b2[0], TOL, "1 row, 4 cols: 4 cells wide");
        assertEquals(40.0, b2[3] - b2[1], TOL, "1 row, 4 cols: 1 cell tall");
    }

    @DisplayName("drawGrid(t, 3, 4, 25): 48 segments, 100 x 75, and the pen is DOWN afterwards")
    @Test
    void drawGrid_Test10() {
        Drawings.drawGrid(t, 3, 4, 25);
        assertEquals(48, world.getSegments().size(), "3 x 4 x 4 = 48 segments");
        double[] b = bbox(world.getSegments());
        assertEquals(100.0, b[2] - b[0], TOL, "4 cols of 25");
        assertEquals(75.0, b[3] - b[1], TOL, "3 rows of 25");
        assertTrue(t.isPenDown(), "every penUp needs a matching penDown - the pen must be down when the method returns");
        t.forward(10);
        assertEquals(49, world.getSegments().size(), "a forward() right after drawGrid should draw");
    }
}
