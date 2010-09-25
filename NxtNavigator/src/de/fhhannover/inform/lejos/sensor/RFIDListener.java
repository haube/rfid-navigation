/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.fhhannover.inform.lejos.sensor;

import ch.aplu.nxt.SensorPort;
import de.fhhannover.inform.lejos.application.Controller;
import de.fhhannover.lejos.util.navigation.Action;
import de.fhhannover.lejos.util.navigation.rfid.Map;
import de.fhhannover.lejos.util.navigation.rfid.Tag;
import lejos.nxt.comm.RConsole;

/**
 *
 * @author Michael Antemann <antemann.michael at gmail.com>
 */
public class RFIDListener implements ch.aplu.nxt.RFIDListener {

    static Tag current = null;

    public void detected(SensorPort port, long id) {
        Tag found = Map.SMALL.getById(id);
        if (found == null) {
            RConsole.println("Unbekannten Tag gefunden: " + id);
        } else if (current == found) {
            RConsole.println("Tag bereits erfasst" + found.reference + "\tx:" + found.x + "\ty:" + found.y);
            current = found;
        } else if (current != found) {
            RConsole.println("Neuer Tag" + found.reference + "\tx:" + found.x + "\ty:" + found.y);
            Controller.INSTANCE.currentActions.add(Action.RFIDFOUND);
            current = found;
        }

    }
}
