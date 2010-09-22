/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import ch.aplu.nxt.MotorPort;
import ch.aplu.nxt.SensorPort;
import de.fhhannover.inform.lejos.application.Controller;
import lejos.nxt.Button;
import lejos.nxt.comm.RConsole;

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
        Controller.INSTANCE.init(20,MotorPort.B,MotorPort.C,SensorPort.S2,SensorPort.S3,SensorPort.S1);
        Controller.INSTANCE.start();
        Button.ESCAPE.waitForPressAndRelease();
        Controller.INSTANCE.stop();

    }
}
