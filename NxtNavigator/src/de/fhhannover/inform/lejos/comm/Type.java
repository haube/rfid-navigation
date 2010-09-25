/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.fhhannover.inform.lejos.comm;

/**
 *
 * @author Michael Antemann <antemann.michael at gmail.com>
 */
public enum Type {

    QUIT(0),
    PAUSE(1),
    UNPAUSE(2),
    MESSAGE(3),
    TARGET(4);
    int code;

     Type(int code) {
         this.code =code;
    }
}
