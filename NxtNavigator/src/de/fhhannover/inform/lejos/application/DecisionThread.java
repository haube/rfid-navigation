/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.fhhannover.inform.lejos.application;

import ch.aplu.nxt.Tools;
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

            if(statusId == Status.INIT.id){
                // nichts zu tun
                
            }else if (statusId == Status.CALIBRATE.id) {
               

            }else if(statusId == Status.IDLE.id){
                // warte auf Aufgaben

            }else if(statusId == Status.LOCALIZE.id){
                //Tag gefunden bestimme seine Position und veranlasse weiteres

            }else if(statusId == Status.GLOBAL.id){
                // position bekannt, bestimme direction zum Ziel

            }else if(statusId == Status.LOCAL.id){
                //Fahre bis zu dem Nächsten RFID Tag
                // -> Follow Line, keep direction
                

            }else if(statusId == Status.EXIT.id){
                // programm wird beendet, nichts zu tun
            }
            Tools.delay(100);
        }
    }
}
