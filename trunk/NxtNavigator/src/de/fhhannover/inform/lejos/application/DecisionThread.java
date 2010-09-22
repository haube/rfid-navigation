/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.fhhannover.inform.lejos.application;

import ch.aplu.nxt.Tools;
import de.fhhannover.inform.lejos.action.Mover;

/**
 *
 * @author Michael Antemann <antemann.michael at gmail.com>
 */
public class DecisionThread extends Thread {
//DurchdrückenvonEscapewird"run"auf"false"gesetzt
//unddasProgrammbeendet.

    boolean run = true;
    boolean silent = true;

    public DecisionThread() {
    }

    public void run() {
        while (run) {
          Mover.INSTANCE.getGear().forward();
          Tools.delay(5000);
        }
    }
}
