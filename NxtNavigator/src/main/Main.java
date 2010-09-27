/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import ch.aplu.nxt.MotorPort;
import ch.aplu.nxt.SensorPort;
import ch.aplu.nxt.Tools;
import de.fhhannover.inform.lejos.application.Controller;
import de.fhhannover.inform.lejos.comm.Message;
import de.fhhannover.inform.lejos.comm.Task;
import de.fhhannover.inform.lejos.comm.Type;
import lejos.nxt.Button;

/**
 *
 * @author Michael Antemann <antemann.michael at gmail.com>
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws InterruptedException {
        //initialisieren
        Controller.INSTANCE.init(1, MotorPort.B, MotorPort.C, SensorPort.S2, SensorPort.S3, SensorPort.S1);
        Controller.INSTANCE.calibrate();

        Controller.INSTANCE.start();
        //Controller.INSTANCE.setStatus(Status.LOCAL);
        Tools.delay(10000);
        synchronized (Controller.INSTANCE) {
            Controller.INSTANCE.addTask(new Task(Type.TARGET, new Message(5)));
//            Controller.INSTANCE.addTask(new Task(Type.TARGET, new Message(0)));
//            Controller.INSTANCE.addTask(new Task(Type.TARGET, new Message(17)));
//            Controller.INSTANCE.addTask(new Task(Type.TARGET, new Message(5)));
        }
        Button.ESCAPE.waitForPressAndRelease();
        Controller.INSTANCE.stop();
        System.exit(0);
    }
}
