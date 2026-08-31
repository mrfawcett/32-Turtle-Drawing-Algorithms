/** READ FIRST
 * Turtle + Loops: Drawing Algorithms - lecture 2.7.
 *
 * Yesterday a square cost eight lines. Today every repeated forward/turn pair
 * becomes a for loop, and a drawing algorithm is three decisions: how far,
 * how much to turn, and how many times.
 *
 * THE 360 RULE. Walk all the way around a closed shape and you have turned
 * one full circle. Spread that 360 over n equal corners: the turtle turns
 * 360 / n at each one - the OUTSIDE angle, never the interior angle. A
 * pentagon turns 72, not 108. A triangle turns 120, not 60.
 *
 * THE INTEGER-DIVISION LANDMINE. 360 / 7 is 51 in Java, not 51.43, and seven
 * turns of 51 add up to 357: the heptagon never closes. Write 360.0 / sides
 * and store it in a double. Turtle.turn accepts a double.
 *
 * The rules every method follows (the tests check all of them):
 *   - Start where the turtle is, facing the way it faces. Never moveTo a
 *     hard-coded spot: the caller decides where the picture goes.
 *   - Draw the shape and nothing else. The autograder counts every segment.
 *   - Leave the pen DOWN when you finish, even if you lifted it in between.
 *   - Do not change the pen color or width.
 *
 * Coordinates: (0, 0) is TOP-LEFT and y grows DOWNWARD. Heading 0 is up,
 * 90 is right, positive turns are clockwise.
 *
 * A hand trace of drawPolygon(t, 4, 100) for a turtle at (300, 200) facing
 * up - the same square as yesterday, now three lines long:
 *
 *      pass   forward(100) moves to   turn(90.0) -> heading
 *        0    (300, 100)                90
 *        1    (400, 100)               180
 *        2    (400, 200)               270
 *        3    (300, 200)                 0     4 x 90 = 360, closed
 */
public class Drawings {

    /** COMPLETE THIS METHOD
     * Precondition: sides >= 3 and length > 0
     * Draws a regular polygon with the given number of sides, each of length
     * length, turning 360.0 / sides degrees clockwise after every side, and
     * finishes back at the start facing the starting direction. Exactly
     * sides segments. The angle is computed here, not passed in.
     * Example: drawPolygon(t, 5, 100) draws a pentagon, turning 72 each time.
     * Example: drawPolygon(t, 7, 100) turns 51.428... each time and CLOSES.
     * Hint: double angle = 360.0 / sides;  - the .0 is the whole point.
     */
    public static void drawPolygon(Turtle t, int sides, int length) {
        // Insert your code below

    }

    /** COMPLETE THIS METHOD
     * Precondition: size > 0
     * Draws a five-pointed star: 5 strokes of length size, turning 144
     * degrees clockwise after each. 5 x 144 = 720, two full circles, which is
     * why the strokes cross. Finishes back at the start facing the starting
     * direction. Exactly 5 segments.
     * Example: drawStar(t, 150) draws 5 segments of length 150.
     */
    public static void drawStar(Turtle t, int size) {
        // Insert your code below

    }

    /** COMPLETE THIS METHOD
     * Precondition: segments >= 1, start > 0, step >= 0
     * Draws a square spiral: segments strokes, turning right (90 degrees
     * clockwise) after each one. Stroke number i (counting from 0) has length
     * start + i * step, so the first stroke is start long and every stroke is
     * step longer than the one before it.
     * Example: drawSpiral(t, 8, 10, 5) draws strokes 10, 15, 20, 25, 30, 35,
     * 40, 45 and ends facing up again (8 x 90 = 720).
     * Hint: declare the length OUTSIDE the loop, or it resets every pass.
     */
    public static void drawSpiral(Turtle t, int segments, int start, int step) {
        // Insert your code below

    }

    /** COMPLETE THIS METHOD
     * Precondition: petals >= 1, petalSides >= 3, length > 0
     * Draws a flower: petals copies of a regular polygon with petalSides
     * sides of length length, all starting from the same point, each rotated
     * 360.0 / petals degrees clockwise from the one before. For each petal:
     * draw the polygon (it ends where it began), then turn 360.0 / petals.
     * Exactly petals * petalSides segments; finishes back at the start facing
     * the starting direction.
     * Example: drawFlower(t, 12, 4, 80) draws 12 squares of side 80, each
     * rotated 30 degrees from the last: 48 segments.
     * Hint: the inner shape is drawPolygon. Do not write the loop twice.
     */
    public static void drawFlower(Turtle t, int petals, int petalSides, int length) {
        // Insert your code below

    }

    /** COMPLETE THIS METHOD
     * Precondition: rows >= 1, cols >= 1, cell > 0
     * Draws a grid of rows x cols squares, each of side cell, packed edge to
     * edge with no gaps and no overlaps, so the whole picture is cols * cell
     * wide and rows * cell tall. Each square is drawn completely (4 sides in
     * a row) before the turtle moves on; move between cells with the pen UP.
     * Exactly 4 * rows * cols segments, each of length cell. The pen is DOWN
     * when the method finishes. Where the turtle ends is up to you.
     * Example: drawGrid(t, 2, 3, 40) draws 6 squares, 24 segments, and the
     * drawing is 120 wide and 80 tall.
     * Hint: after drawPolygon(t, 4, cell) the turtle is back at that cell's
     * bottom-left corner facing up. Pen up, step one cell to the right, pen
     * down. At the end of a row, go back cols * cell to the left and one
     * cell up.
     */
    public static void drawGrid(Turtle t, int rows, int cols, int cell) {
        // Insert your code below

    }

    /** COMPLETE THIS METHOD
     * Precondition: count >= 1, smallest > 0, step >= 0
     * Draws count squares that all start from the same corner facing the same
     * way, nested one inside the next like the "shape stack" from lecture:
     * square number i (counting from 0) has side smallest + i * step. Draw
     * them smallest first. Exactly 4 * count segments; finishes back at the
     * start facing the starting direction.
     * Example: drawConcentricSquares(t, 3, 40, 30) draws squares of side 40,
     * 70 and 100 from the same corner: 12 segments.
     * Hint: drawPolygon(t, 4, side) inside a loop, growing side each pass.
     */
    public static void drawConcentricSquares(Turtle t, int count, int smallest, int step) {
        // Insert your code below

    }
}
