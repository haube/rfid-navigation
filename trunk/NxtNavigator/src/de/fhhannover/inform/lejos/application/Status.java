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

    INIT        (0),
    CALIBRATE   (1),
    LOCALIZE    (3),
    IDLE        (2),
    GLOBAL      (4),
    LOCAL       (5),
    EXIT        (6);

    public int id;

    Status(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    
}
