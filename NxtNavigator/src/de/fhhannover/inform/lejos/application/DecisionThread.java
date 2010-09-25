/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.fhhannover.inform.lejos.application;

import ch.aplu.nxt.Tools;
import de.fhhannover.inform.lejos.action.Mover;
import de.fhhannover.lejos.util.navigation.Action;
import java.util.ArrayList;
import java.util.Iterator;
import lejos.nxt.comm.RConsole;

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
            int statusId = Controller.INSTANCE.getStatus().getId();
            ArrayList<Action> actions = Controller.INSTANCE.getCurrentActions();
            for (Iterator<Action> it = actions.iterator(); it.hasNext();) {
                Action action = it.next();
                RConsole.print(action.getIdentifier()+"; ");
            }
                RConsole.print("\n");
            
            if (statusId == Status.INIT.id) {
                // nichts zu tun
            } else if (statusId == Status.CALIBRATE.id) {
            } else if (statusId == Status.IDLE.id) {
            } else if (statusId == Status.LOCALIZE.id) {
                //Tag gefunden bestimme seine Position und veranlasse weiteres
                Mover.INSTANCE.stop();
                Mover.INSTANCE.getOverTag();
                Controller.INSTANCE.setStatus(Status.GLOBAL);
            } else if (statusId == Status.GLOBAL.id) {
                // position bekannt, bestimme direction zum Ziel
            } else if (statusId == Status.LOCAL.id) {
                //Fahre bis zu dem Nächsten RFID Tag
                // -> Follow Line, keep direction

                // bestimme Action
                if (Controller.INSTANCE.svt.getAvgLight() - 40 < Controller.INSTANCE.svt.getCurrentLightValue()) {
                    Controller.INSTANCE.removeAction(Action.OFFTRACK);
                    Controller.INSTANCE.addAction(Action.ONTRACK);
                    Controller.INSTANCE.svt.collectLight = false;
                } else {
                    Controller.INSTANCE.svt.collectLight = true;
                    Controller.INSTANCE.removeAction(Action.ONTRACK);
                    Controller.INSTANCE.addAction(Action.OFFTRACK);
                }
                if (actions.contains(Action.RFIDFOUND)) {
                    Controller.INSTANCE.setStatus(Status.LOCALIZE);
                    Controller.INSTANCE.removeAction(Action.ONTRACK);
                    Controller.INSTANCE.removeAction(Action.OFFTRACK);
                    Controller.INSTANCE.svt.collectLight = false;
                    Mover.INSTANCE.stop();
                }

                if (actions.contains(Action.OFFTRACK)) {
                    Controller.INSTANCE.svt.collectLight = false;
                    Mover.INSTANCE.sweep();
                } else if(actions.contains(Action.ONTRACK)) {
                    Controller.INSTANCE.svt.collectLight = true;
                    Mover.INSTANCE.forward();
                }


            } else if (statusId == Status.EXIT.id) {
                // programm wird beendet, nichts zu tun
            }
            Tools.delay(100);
        }
    }
}
