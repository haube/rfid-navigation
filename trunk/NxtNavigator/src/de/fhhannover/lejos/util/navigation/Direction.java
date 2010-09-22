/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.fhhannover.lejos.util.navigation;

/**
 *
 * @author Michael Antemann <antemann.michael at gmail.com>
 */
public enum Direction {

    NORTH(0, -29, 30),
    EAST(90, 60, 120),
    WEST(270, 240, 310),
    SOUTH(180, 150, 210);
    int direction;
    int leftIntervall;
    int rightIntervall;

    Direction(int d, int l, int r) {
        this.direction = d;
        this.leftIntervall = l;
        this.rightIntervall = r;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public int getLeftIntervall() {
        return leftIntervall;
    }

    public void setLeftIntervall(int leftIntervall) {
        this.leftIntervall = leftIntervall;
    }

    public int getRightIntervall() {
        return rightIntervall;
    }

    public void setRightIntervall(int rightIntervall) {
        this.rightIntervall = rightIntervall;
    }

    public boolean inSector(int i) {
        i = i % 360;
        if (i > leftIntervall && i < rightIntervall) {
            return true;
        } else {
            return false;
        }
    }

    public static Direction evaluate(int compare) {
        for (Direction direction : Direction.values()) {
            if (direction.inSector(compare)) {
                return direction;
            }
        }
        return null;

    }
}
