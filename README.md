# Turtle Drawing Algorithms

**Unit 2 — Graphics, GUI & Applied Class Design** · Pairs with lecture 2.7 (Turtle + Loops: Drawing Algorithms) · builds on 2.6

Yesterday a square cost eight lines and you hated it. Today every repeated
forward/turn pair becomes a `for` loop, and one method —
`drawPolygon(Turtle t, int sides, int length)` — draws every regular shape
there is. The loop is not new; it is Unit 1. What is new is the **angle
arithmetic**, and that is where this assignment will take your time: the
360 rule, and the integer-division landmine that makes a heptagon fail to
close.

A drawing algorithm is three decisions: **how far, how much to turn, and how
many times.** Every method here is a variation on those three numbers.

The autograder cannot look at a picture. It counts the **line segments** your
turtle draws, measures them, and checks where the turtle ends up and which way
it faces — to half a pixel and half a degree.

---

## The world you are drawing in

Same as 2.6. `(0, 0)` is the **top-left**, y grows **downward**, heading 0
is up, 90 is right, positive turns are clockwise. A fresh turtle in a
`World(600, 400)` sits at `(300, 200)` facing up with the pen down.

### Hand-trace the square, loop edition

```java
for (int i = 0; i < 4; i++) {
    t.forward(100);
    t.turn(90.0);
}
```

| pass `i` | `forward(100)` moves to | `turn(90.0)` → heading |
|---|---|---|
| 0 | (300, 100) | 90 |
| 1 | (400, 100) | 180 |
| 2 | (400, 200) | 270 |
| 3 | (300, 200) | 0 |

```
 (300,100) ┌──────────┐ (400,100)
           │          │       4 passes, 4 segments of 100,
           │          │       4 × 90 = 360: closed, facing up again
 (300,200) └──────────┘ (400,200)
     ▲ start, facing up
```

`i` is never used inside the body. That is fine — `i` is counting, not
measuring. Change the 4 to a 5 and the 90 to a 72 and you have a pentagon.
That is the whole idea of `drawPolygon`.

### The 360 rule

Walk all the way around a closed shape and you end up facing the way you
started: you have turned one full circle, 360 degrees. Spread that over `n`
equal corners and each turn is **360 / n**.

| shape | sides | turtle turns | interior angle (NOT the turn) |
|---|---|---|---|
| triangle | 3 | 120 | 60 |
| square | 4 | 90 | 90 |
| pentagon | 5 | 72 | 108 |
| hexagon | 6 | 60 | 120 |
| heptagon | 7 | 51.43 | 128.57 |
| octagon | 8 | 45 | 135 |

The turtle turns the **outside** angle. Interior + turn = 180, every time. Do
not memorize the table; memorize `360 / n`. If your shape does not close,
multiply your turn by your loop count **before** you look at anything else.
If the product is not 360 (or a multiple of 360, for stars), nothing else
about your code matters.

### The integer-division landmine

```java
int angle = 360 / 7;        // 51, not 51.43 — Java throws away the .43
```

Seven turns of 51 add up to 357. The heptagon ends 3 degrees short and about
6 pixels from where it started. The tests check closure to half a pixel, so
this **fails**. The fix is one character:

```java
double angle = 360.0 / sides;   // 51.428571...
t.turn(angle);                  // Turtle.turn accepts a double
```

`360.0 / 7` is real division because one operand is a `double`. This is the
2.4 lesson with something to look at.

---

## What you are given

| File | Status | Purpose |
|---|---|---|
| `src/main/java/Drawings.java` | **you complete this** | six static drawing methods |
| `src/main/java/Turtle.java` | provided | the turtle; `turn(double)` is the method you need today |
| `src/main/java/World.java` | provided | the canvas; records every segment for the autograder |
| `src/main/java/Main.java` | provided | draws every method once and opens a window — run it to see your work |
| `src/test/java/*Test.java` | provided | the autograder's tests — read them |

`Main` opens a window when your machine has a screen and does nothing on a
machine without one. The autograder never runs `Main`; it only runs the
tests.

## What to write

Every method is `public static void`, takes the `Turtle` first, and:

* starts where the turtle is, facing the way it faces — never `moveTo` a
  hard-coded spot;
* draws its shape and **nothing else** — the tests count segments;
* finishes with the pen **down**, even if it lifted the pen in between;
* never changes the pen color or width.

| Method | Points | What it does |
|---|---|---|
| `drawPolygon(Turtle t, int sides, int length)` | **25** | `sides` sides of `length`, turning `360.0 / sides` after each. Closes; heading returns. |
| `drawStar(Turtle t, int size)` | 10 | 5 strokes of `size`, turning 144. Closes; heading returns (720 = 2 × 360). |
| `drawSpiral(Turtle t, int segments, int start, int step)` | 15 | `segments` strokes turning right after each; stroke `i` is `start + i * step` long. |
| `drawFlower(Turtle t, int petals, int petalSides, int length)` | **25** | `petals` polygons of `petalSides` sides from one point, each rotated `360.0 / petals`. `petals * petalSides` segments; ends at start facing start. |
| `drawGrid(Turtle t, int rows, int cols, int cell)` | 15 | `rows × cols` squares of side `cell` packed edge to edge: `4 * rows * cols` segments, `cols * cell` wide, `rows * cell` tall. Pen up between cells. |
| `drawConcentricSquares(Turtle t, int count, int smallest, int step)` | 10 | `count` squares from the same corner, sides `smallest`, `smallest + step`, ... `4 * count` segments. |

### drawPolygon

```java
double angle = 360.0 / sides;
for (int i = 0; i < sides; i++) {
    t.forward(length);
    t.turn(angle);
}
```

Forward, then turn — in that order, so the first side goes straight ahead
from where the turtle is. The angle is computed inside the method; the caller
cannot get it wrong. The tests draw 3, 4, 5, 6, 7, 8 and 11 sides. The
7-sided and 11-sided tests exist for one reason: `360 / 7` and `360 / 11`
are not whole numbers.

**Trap:** `t.turn(108)` for a pentagon. The pentagon test message tells you
it wandered off. **Trap:** `t.turn(144)` for a pentagon closes and faces up
but draws a star — the "153.9 wide and 161.8 tall" test catches it.

### drawStar

```java
for (int i = 0; i < 5; i++) {
    t.forward(size);
    t.turn(144);
}
```

5 × 144 = 720 — two full laps, which is why the strokes cross. The turns must
total a **multiple** of 360, not exactly 360. 144 is just 72 doubled. Ask
yourself where the turtle is after two strokes: with 144 it has folded back to
only 0.618 × `size` from the start; with 72 it would be 1.618 × `size` away.
There is a test for exactly that.

### drawSpiral

```java
int length = start;                 // OUTSIDE the loop
for (int i = 0; i < segments; i++) {
    t.forward(length);
    t.turnRight();
    length = length + step;
}
```

The turn is constant; the distance grows. Declare `length` **outside** the
loop or it resets to `start` every pass and you get one small square drawn
`segments` times — the test named "the LAST stroke is 70" is there to catch
that. `drawSpiral(t, 40, 10, 5)` is the lecture's spiral: pass 1 is 10, pass
2 is 15, pass 40 is 205.

### drawFlower — a loop inside a loop

```java
for (int petal = 0; petal < petals; petal++) {
    drawPolygon(t, petalSides, length);   // inner loop: draws one petal
    t.turn(360.0 / petals);               // outer turn: 360 rule again
}
```

The inner shape is the method you already wrote. `drawPolygon` ends where it
began, facing the way it began, so after it the turtle is back at the center
and you only have to turn a little and go again. Two counters, two names —
never reuse `i` for both if you write the inner loop by hand.

```
drawFlower(t, 12, 4, 80)          drawFlower(t, 8, 3, 100)

  12 squares of 80,                 8 triangles of 100,
  each rotated 30 from              each rotated 45 from
  the one before                    the one before
  (360 / 12 = 30)                   (360 / 8 = 45)
  48 segments                       24 segments
```

**Trap:** turning `360.0 / petalSides` between petals instead of
`360.0 / petals`. For 12 squares that is 90 instead of 30 — the heading still
comes out at 0 (12 × 90 = 1080 = 3 × 360), so the heading test passes, but
only 4 distinct squares get drawn. The test "petal 1 is rotated 30 degrees"
checks where the second petal's first side actually ends: (340, 130.7), not
(380, 200). **Trap:** `360 / petals` in integer arithmetic — the 7-petal
flower of heptagons catches it in both loops.

### drawGrid

After `drawPolygon(t, 4, cell)` the turtle is back at the cell's bottom-left
corner facing up. Lift the pen, step one cell to the right, lower the pen,
draw the next. At the end of a row, come back `cols * cell` to the left and
go up one cell.

```
  ┌──┬──┬──┐        drawGrid(t, 2, 3, 40)
  │  │  │  │        6 squares, 24 segments,
  ├──┼──┼──┤        120 wide, 80 tall
  │  │  │  │        shared edges are drawn twice — that is fine,
  └──┴──┴──┘        each square is complete on its own
  ▲ start
```

The tests do not care where the grid sits relative to the start or where the
turtle ends up. They do care that: pen-up moves draw nothing (exactly
`4 * rows * cols` segments), every corner lands on the `cell` lattice, every
cell is drawn once, and the pen is **down** at the end.

**Trap:** forgetting to come back to the left edge at the end of a row. The
grid becomes a staircase 240 wide instead of 120, and the width test says so.

### drawConcentricSquares

`count` squares, all starting from the same corner facing the same way,
smallest first: sides `smallest`, `smallest + step`, `smallest + 2*step`, ...
They nest like the "shape stack" from the lecture — everything from one
starting point. (They share a corner rather than a center, which keeps the
method loop-only: no pen lifting, no coordinate math.)

```
  ┌────────────┐      drawConcentricSquares(t, 3, 40, 30)
  │┌────────┐  │      sides 40, 70, 100
  ││┌────┐  │  │      12 segments
  │││    │  │  │
  └┴┴────┴──┴──┘
  ▲ shared corner; turtle ends here facing up
```

---

## Examples

All examples start from a fresh turtle at (300, 200) facing up.

| call | segments | turtle ends at | heading | drawing size |
|---|---|---|---|---|
| `drawPolygon(t, 4, 100)` | 4 | (300, 200) | 0 | 100 × 100 |
| `drawPolygon(t, 5, 100)` | 5 | (300, 200) | 0 | 153.9 × 161.8 |
| `drawPolygon(t, 7, 100)` | 7 | (300, 200) — within 0.5 | 0 — within 0.5 | 219.1 × 224.7 |
| `drawStar(t, 150)` | 5 | (300, 200) | 0 | 142.7 × 150 |
| `drawSpiral(t, 8, 10, 5)` | 8, lengths 10 … 45 | (280, 220) | 0 | 45 × 40 |
| `drawFlower(t, 12, 4, 80)` | 48 | (300, 200) | 0 | 218.6 × 218.6 |
| `drawGrid(t, 2, 3, 40)` | 24 | anywhere | any | 120 × 80 |
| `drawConcentricSquares(t, 3, 40, 30)` | 12 | (300, 200) | 0 | 100 × 100 |

What the integer-division version of `drawPolygon(t, 7, 100)` does instead:
ends at (302.8, 205.4), heading 357. Six pixels and three degrees off.
Failed.

---

## Running the tests

`mvn test` runs everything; `mvn test -Dtest=DrawPolygonTest` runs one rubric
line. Each test's name says what it checks and includes the numbers. When one
fails, read the message before you read your code — it usually names the
segment, the expected value, and the mistake that produces the wrong one.

| Test class | Rubric line | Points |
|---|---|---|
| `DrawPolygonTest` | drawPolygon (the 360.0 / sides rule) | 25 |
| `DrawStarTest` | drawStar | 10 |
| `DrawSpiralTest` | drawSpiral | 15 |
| `DrawFlowerTest` | drawFlower (nested loops) | 25 |
| `DrawGridTest` | drawGrid | 15 |
| `DrawConcentricSquaresTest` | drawConcentricSquares | 10 |

The autograder awards a rubric line only when **every** test in that class
passes. `drawPolygon` is worth 25 on its own and `drawFlower`, `drawGrid` and
`drawConcentricSquares` all call it, so get it right first.

## Suggested order

1. `drawPolygon` — type `360.0`, not `360`. Run `DrawPolygonTest`. Then
   change it to `360 / sides` once, run the 7-sided test, and read the
   failure. Change it back.
2. `drawStar` — same loop, 5 and 144. Predict the picture before you run
   `Main`.
3. `drawSpiral` — one variable outside the loop. Then move the declaration
   inside on purpose, run `Main`, and see one lonely square.
4. `drawConcentricSquares` — `drawPolygon(t, 4, side)` in a loop with a
   growing `side`. Two minutes if `drawPolygon` works.
5. `drawFlower` — `drawPolygon` inside a loop with a turn. Compute
   `petals × turn` before you run anything.
6. `drawGrid` — the only method that lifts the pen. Draw one row first, run
   the 1 × 4 test, then add the row reset.

## Rules of the road

- AP Java subset only: `int`, `double`, `for` loops, method calls, and
  calling your own `drawPolygon`. No `Math` methods are needed.
  Never `moveTo` a hard-coded spot inside `Drawings`; draw relative to where
  the turtle already is. (Reading `getXPos()`/`getYPos()` and returning to
  *that* is allowed.)
- Do not change method headers or provided code. `Turtle.java`,
  `World.java` and `Main.java` are yours to read, not to edit.
- Do not touch `src/test`, `pom.xml`, `grading.json`, or `.github`. The
  autograder checks that they are byte-identical to the template before it
  runs a single test; if they differ it stops and awards nothing, and the
  change shows up in the roster.
- Every method must leave the pen **down** and must not call `setPenColor`,
  `setPenWidth` or `setColor`. The caller chose those.
- `drawFlower`, `drawGrid` and `drawConcentricSquares` should call
  `drawPolygon`. Writing the polygon loop three more times by hand will pass
  the tests and fail the point of the lecture.
