/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.fhhannover.inform.lejos.application;

import ch.aplu.nxt.Tools;
import de.fhhannover.inform.lejos.action.Mover;
import de.fhhannover.inform.lejos.comm.Type;

import de.fhhannover.inform.lejos.sensor.RFIDListener;
import de.fhhannover.lejos.util.navigation.Action;
import de.fhhannover.lejos.util.navigation.Direction;
import de.fhhannover.lejos.util.navigation.rfid.Map;
import de.fhhannover.lejos.util.navigation.rfid.Tag;
import java.util.ArrayList;
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

    boolean ontrack() {
//        return ((Controller.INSTANCE.svt.threshold < Controller.INSTANCE.svt.getCurrentLightValue()));
        return (Controller.INSTANCE.svt.getAvgLight() - 40 < Controller.INSTANCE.svt.getCurrentLightValue()); //withe
//        return (Controller.INSTANCE.svt.getAvgLight() + 40 > Controller.INSTANCE.svt.getCurrentLightValue()); // black
    }

    boolean insectorAvg() {
        return Controller.INSTANCE.getDirection().inSector(Controller.INSTANCE.svt.getAvgCompass());
    }

    boolean inSector() {
        return Controller.INSTANCE.getDirection().inSector(Controller.INSTANCE.svt.getCurrentDirectionValue());
    }

    public void run() {
        ArrayList<Action> actions = new ArrayList<Action>();
        while (run) {
            actions.clear();
            int statusId = Controller.INSTANCE.getStatus().getId();
            actions.addAll(Controller.INSTANCE.getCurrentActions());


            if (statusId == Status.INIT.id) {
                // nichts zu tun
            } else if (statusId == Status.CALIBRATE.id) {
            } else if (statusId == Status.IDLE.id) {

                Controller.INSTANCE.setNextTask();
                if (null == Controller.INSTANCE.getCurrentTask()) {
                    RConsole.println("task null");
                    Thread.yield();
                } else if (null != Controller.INSTANCE.getCurrentTask() && Controller.INSTANCE.getCurrentTask().getType().equals(Type.TARGET)) {
                    Controller.INSTANCE.setTargetTag(Map.SMALL.getByRef(Controller.INSTANCE.getCurrentTask().getMessage().getValue()));
                    RConsole.println("target: " + Controller.INSTANCE.getTargetTag().getReference());
                    Controller.INSTANCE.setStatus(Status.GLOBAL);
                }
            } else if (statusId == Status.LOCALIZE.id) {
                //Tag gefunden bestimme seine Position und veranlasse weiteres
                Mover.INSTANCE.stop();
                Mover.INSTANCE.getOverTag();
                Controller.INSTANCE.setLastTag(RFIDListener.current);
                Controller.INSTANCE.setStatus(Status.GLOBAL);
                Controller.INSTANCE.removeAction(Action.RFIDFOUND);

            } else if (statusId == Status.GLOBAL.id) {
                // position bekannt, bestimme direction zum Ziel
                Controller.INSTANCE.svt.collectLight = false;
                if (Map.SMALL.getDummy().equals(Controller.INSTANCE.getLastTag())) {
                    Controller.INSTANCE.setStatus(Status.LOCAL);
                } else if ((Controller.INSTANCE.getLastTag().equals(Controller.INSTANCE.getTargetTag()))) {
                    Controller.INSTANCE.getRobot().playTone(600, 500);
                    Controller.INSTANCE.getRobot().playTone(800, 500);
                    Controller.INSTANCE.getRobot().playTone(400, 500);
                    Controller.INSTANCE.getRobot().playTone(900, 500);
                    Tools.delay(1000);
                    Controller.INSTANCE.clearActions();
                    Controller.INSTANCE.setCurrentTask(null);
                    Controller.INSTANCE.setStatus(Status.IDLE);
                } else {
                    //bestimme position und richtung und gehe über zu navigation
                    Tag target = Controller.INSTANCE.getTargetTag();
                    Tag current = Controller.INSTANCE.getLastTag();

                    if (current.x < target.x) {
                        Controller.INSTANCE.setDirection(Direction.WEST);
                    } else if (current.x > target.x) {
                        Controller.INSTANCE.setDirection(Direction.EAST);
                    } else {
                        if (current.y < target.y) {
                            Controller.INSTANCE.setDirection(Direction.NORTH);
                        } else if (current.y > target.y) {
                            Controller.INSTANCE.setDirection(Direction.SOUTH);
                        }
                    }

                    if (inSector() && !actions.contains(Action.TURNED)) {
                        RConsole.println("Current Direction:" + Controller.INSTANCE.svt.getCurrentDirectionValue() + "\t" + Controller.INSTANCE.getDirection());
                        Controller.INSTANCE.removeAction(Action.TURNING);
                        Controller.INSTANCE.addAction(Action.TURNED);
                        Mover.INSTANCE.stop();
                    } else if (inSector() && actions.contains(Action.TURNED)) {
                        if (actions.contains(Action.ONTRACK) && actions.contains(Action.TURNED)) {
                            Mover.INSTANCE.stop();
                            Controller.INSTANCE.clearActions();
                            Controller.INSTANCE.setStatus(Status.LOCAL);
                        } else {
                            Mover.INSTANCE.sweepInSector(Controller.INSTANCE.getDirection(), Controller.INSTANCE.svt.getCurrentDirectionValue());
                            Mover.INSTANCE.stop();
                            Controller.INSTANCE.addAction(Action.ONTRACK);
                            Tools.delay(100);
                            if (Controller.INSTANCE.getCurrentActions().contains(Action.ONTRACK)) {
                                Mover.INSTANCE.stop();
                                Controller.INSTANCE.clearActions();
                                Controller.INSTANCE.setStatus(Status.LOCAL);
                            }
                        }
                    } else {
                        RConsole.println("Current Direction:" + Controller.INSTANCE.svt.getCurrentDirectionValue() + "\t" + Controller.INSTANCE.getDirection());
                        Controller.INSTANCE.addAction(Action.TURNING);
                        Mover.INSTANCE.turnToDirection(Controller.INSTANCE.getDirection(), Controller.INSTANCE.svt.getCurrentDirectionValue());
                    }


                    /*
                    if (Controller.INSTANCE.getDirection().inSector(Controller.INSTANCE.svt.getAvgCompass())
                    && !(ontrack())) {
                    Mover.INSTANCE.stop();
                    Controller.INSTANCE.setStatus(Status.LOCAL);
                    } else {
                    RConsole.println("sweepe");
                    Mover.INSTANCE.sweep();
                    }
                     */
                    // wenn richtung stimmt und linie über sensor wechsle status
                    //ansonsten richte aus
                }
            } else if (statusId == Status.LOCAL.id) {
                //Fahre bis zu dem Nächsten RFID Tag
                // -> Follow Line, keep direction

                // bestimme Action
                if (ontrack()) {
                    Controller.INSTANCE.removeAction(Action.OFFTRACK);
                    Controller.INSTANCE.addAction(Action.ONTRACK);
                    Controller.INSTANCE.svt.collectLight = true;
                } else {
                    Controller.INSTANCE.svt.collectLight = false;
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
//                    if (inSector()) {
//                        Mover.INSTANCE.sweepInSector(Controller.INSTANCE.getDirection(), Controller.INSTANCE.svt.getCurrentDirectionValue());
//                    } else {
//                    //    Mover.INSTANCE.turnToDirection(Controller.INSTANCE.getDirection(), Controller.INSTANCE.svt.getCurrentDirectionValue());

//                    }
                } else if (actions.contains(Action.ONTRACK)) {
                    Controller.INSTANCE.svt.collectLight = true;
                    Mover.INSTANCE.forward();
                }


            } else if (statusId == Status.EXIT.id) {
                // programm wird beendet, nichts zu tun
            }
            Tools.delay(30);
        }
    }
}
