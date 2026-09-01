#!/bin/sh
# Autograder entry point - generated from the assignment template. Do not edit.
#   sh .github/grade.sh SomeTest    integrity check, then run that one test class
#   sh .github/grade.sh             integrity check, then run every test
#   sh .github/grade.sh --check     integrity check only
# The check verifies src/test/java, pom.xml and grading.json are byte-identical
# (line endings ignored) to the assignment template. Any difference scores 0.
cd "$(dirname "$0")/.." || exit 1
EXPECTED="5d4f2e55479e9b2ed1c25de653c89839b478cb1eb26f171b9b1b0cd8bee28e1f"
ACTUAL=$( (for f in $(find src/test/java -type f -name '*.java' | LC_ALL=C sort) pom.xml grading.json; do sed 's/\r$//' "$f"; done) | sha256sum | cut -d' ' -f1)
if [ "$ACTUAL" != "$EXPECTED" ]; then
  echo "INTEGRITY CHECK FAILED: src/test/java, pom.xml or grading.json differ from the assignment template."
  echo "Grading stopped; this rubric line scores 0. Restore the originals and push again:"
  echo "  git checkout origin/main -- src/test pom.xml grading.json"
  exit 1
fi
if [ "$1" = "--check" ]; then echo "Integrity check passed."; exit 0; fi
# GitHub's stock runners can default to a JDK older than the pom's --release 21;
# prefer the runner's preinstalled JDK 21 when it is there.
if [ -n "$JAVA_HOME_21_X64" ] && [ -x "$JAVA_HOME_21_X64/bin/java" ]; then
  JAVA_HOME="$JAVA_HOME_21_X64"; PATH="$JAVA_HOME/bin:$PATH"; export JAVA_HOME PATH
fi
if [ -n "$1" ]; then exec mvn -B -q test -Dtest="$1"; fi
exec mvn -B -q test
