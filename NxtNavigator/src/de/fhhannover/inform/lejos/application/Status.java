/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.fhhannover.inform.lejos.application;

/**
 *
 * @author Michael Antemann <antemann.michael at gmail.com>
 */
public enum Status {

    INIT(0, "Init"),
    CALIBRATE(1, "Calibrate"),
    LOCALIZE(3, "Localize"),
    IDLE(2, "Idle"),
    GLOBAL(4, "Global"),
    LOCAL(5, "Local"),
    EXIT(6, "Exit");
    public final int id;
    public final String name;

    Status(final int id, final String desc) {
        this.id = id;
        this.name = desc;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public static Status getRefByName(String name) {
        for (Status status : Status.values()) {
            if (status.getName().equals(name)) {
                return status;
            }
        }
        return null;
    }

    public static Status getRefById(int id) {
        for (Status status : Status.values()) {
            if (status.getId() == id) {
                return status;
            }
        }
        return null;
    }
}
