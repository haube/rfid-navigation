/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.fhhannover.inform.lejos.application;

import ch.aplu.nxt.Tools;

/**
 *
 * @author Michael Antemann <antemann.michael at gmail.com>
 */
public class EvilTestThread extends Thread {

    public void run() {
        while (true) {
            Thread.yield();
            Controller.INSTANCE.setStatus(Status.INIT);
            Thread.yield();
            Controller.INSTANCE.setStatus(Status.CALIBRATE);
            Thread.yield();
            Controller.INSTANCE.setStatus(Status.EXIT);
            Thread.yield();
            Controller.INSTANCE.setStatus(Status.IDLE);
            Thread.yield();
            Controller.INSTANCE.setStatus(Status.LOCAL);
            Thread.yield();
            Controller.INSTANCE.setStatus(Status.LOCALIZE);
            Thread.yield();
            Controller.INSTANCE.setStatus(Status.GLOBAL);
            Thread.yield();
        }
    }
}
