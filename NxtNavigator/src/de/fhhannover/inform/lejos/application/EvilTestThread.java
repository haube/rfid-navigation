/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.fhhannover.inform.lejos.application;

import ch.aplu.nxt.Tools;
import de.fhhannover.lejos.util.navigation.Action;

/**
 *
 * @author Michael Antemann <antemann.michael at gmail.com>
 */
public class EvilTestThread extends Thread {

    public void run() {
        Action[] actions = Action.values();
        while (true) {


            for (int i = 0; i < actions.length; i++) {
                Action action = actions[i];
                Controller.INSTANCE.addAction(action);
                Thread.yield();
            }
            Tools.delay(60);
            for (int i = 0; i < actions.length; i++) {
                Action action = actions[i];
                Controller.INSTANCE.removeAction(action);
                Thread.yield();
            }
            Tools.delay(90);

        }
    }
}
