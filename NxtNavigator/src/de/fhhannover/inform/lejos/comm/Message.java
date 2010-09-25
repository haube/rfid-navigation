/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.fhhannover.inform.lejos.comm;

/**
 *
 * @author Michael Antemann <antemann.michael at gmail.com>
 */
public class Message {
public int value;

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public Message(int value) {
        this.value = value;
    }


}
