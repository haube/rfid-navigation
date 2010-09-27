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

    NORTH(0, -2, 2),
    EAST(90, 88, 92),
    WEST(270, 268, 272),
    SOUTH(180, 178, 182);
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

    public boolean inSector(float i) {
        i = i % 360;
        if (i+360 > leftIntervall+360 && i+360 < rightIntervall+360) {
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
