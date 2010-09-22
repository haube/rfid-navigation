/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.fhhannover.inform.lejos.application;

import ch.aplu.nxt.CompassSensor;
import ch.aplu.nxt.Gear;
import ch.aplu.nxt.LightSensor;
import ch.aplu.nxt.MotorPort;
import ch.aplu.nxt.NxtRobot;
import ch.aplu.nxt.RFIDSensor;
import ch.aplu.nxt.SensorPort;
import ch.aplu.nxt.Tools;
import de.fhhannover.inform.lejos.action.Mover;
import de.fhhannover.inform.lejos.comm.Task;
import de.fhhannover.inform.lejos.sensor.RFIDListener;
import de.fhhannover.inform.lejos.sensor.SensorValueThread;
import de.fhhannover.lejos.util.navigation.Action;
import de.fhhannover.lejos.util.navigation.rfid.Map;
import de.fhhannover.lejos.util.navigation.rfid.Tag;
import java.util.ArrayList;
import lejos.nxt.comm.RConsole;

/**
 *
 * @author Michael Antemann <antemann.michael at gmail.com>
 */
public enum Controller {

    INSTANCE;
    public boolean running = false;
    public boolean silent = false;
    public float direction = 0f;
    public Tag targetTag = null;
    public ArrayList<Action> currentActions = new ArrayList<Action>();
    public Task currentTask = null;
    public ArrayList<Task> futureTasks = new ArrayList<Task>();
    public Status status = null;
    public Map map = null;
    private NxtRobot robot = new NxtRobot();
    protected SensorValueThread svt = null;
    protected DecisionThread dt = null;

    public ArrayList<Action> getCurrentActions() {
        return currentActions;
    }

    public void setCurrentActions(ArrayList<Action> currentActions) {
        this.currentActions = currentActions;
    }

    public Task getCurrentTask() {
        return currentTask;
    }

    public void setCurrentTask(Task currentTask) {
        this.currentTask = currentTask;
    }

    public float getDirection() {
        return direction;
    }

    public void setDirection(float direction) {
        this.direction = direction;
    }

    public ArrayList<Task> getFutureTasks() {
        return futureTasks;
    }

    public void setFutureTasks(ArrayList<Task> futureTasks) {
        this.futureTasks = futureTasks;
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public boolean isSilent() {
        return silent;
    }

    public void setSilent(boolean silent) {
        this.silent = silent;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Tag getTargetTag() {
        return targetTag;
    }

    public void setTargetTag(Tag targetTag) {
        this.targetTag = targetTag;
    }

    public void init(int speed, MotorPort left, MotorPort right, SensorPort lport, SensorPort rfidport, SensorPort cport) {
        RConsole.openBluetooth(30000);
        RConsole.println("Initialising");
        this.setStatus(Status.INIT);
        Gear gear = null;
        LightSensor light = null;
        RFIDSensor rfid = null;
        CompassSensor compass = null;


        if (left != null && right != null) {
            Mover.INSTANCE.init(left, right, speed);
            robot.addPart(Mover.INSTANCE.getGear());
            Mover.INSTANCE.configure();
        }
        if (lport != null) {
            light = new LightSensor(lport);
            robot.addPart(light);
        }
        if (rfidport != null) {
            rfid = new RFIDSensor(rfidport);
            robot.addPart(rfid);
        }
        if (cport != null) {
            compass = new CompassSensor(cport);
            robot.addPart(compass);
        }

        if (light != null && compass != null) {
            svt = new SensorValueThread(compass, light, 40);
        }
        if (rfid != null) {
            rfid.addRFIDListener(new RFIDListener());
        }
        initMap(Map.SMALL);
        this.map = Map.SMALL;

        dt = new DecisionThread();

    }

    private void initMap(Map map) {
        RConsole.println("creating map");
        map.addTag(new Tag(0, new Long(103546521654641L), 0, 0));
        map.addTag(new Tag(1, new Long(203546521654641L), 0, 1));
        map.addTag(new Tag(2, new Long(303546521654641L), 0, 2));
        map.addTag(new Tag(3, new Long(403546521654641L), 0, 3));
        map.addTag(new Tag(4, new Long(503546521654641L), 0, 4));
        map.addTag(new Tag(5, new Long(603546521654641L), 1, 0));
        map.addTag(new Tag(6, new Long(703546521654641L), 1, 1));
        map.addTag(new Tag(7, new Long(803546521654641L), 1, 2));
        map.addTag(new Tag(8, new Long(903546521654641L), 1, 3));
        map.addTag(new Tag(9, new Long(1003546521654641L), 1, 4));
        map.addTag(new Tag(10, new Long(113546521654641L), 2, 0));
        map.addTag(new Tag(11, new Long(123546521654641L), 2, 1));
        map.addTag(new Tag(12, new Long(133546521654641L), 2, 2));
        map.addTag(new Tag(13, new Long(143546521654641L), 2, 3));
        map.addTag(new Tag(14, new Long(153546521654641L), 2, 4));
        map.addTag(new Tag(15, new Long(163546521654641L), 3, 0));
        map.addTag(new Tag(16, new Long(173546521654641L), 3, 1));
        map.addTag(new Tag(17, new Long(183546521654641L), 3, 2));
        map.addTag(new Tag(18, new Long(193546521654641L), 3, 3));
        map.addTag(new Tag(19, new Long(203546521654641L), 3, 4));
    }

    public void calibrate(){
        
    }

    public void start() {
        RConsole.println("Starting in IDLE mode");
        this.setStatus(Status.IDLE);
        this.setRunning(true);
        Mover.INSTANCE.setRunning(true);
        svt.running = true;
        dt.run = true;
        svt.start();
        dt.start();

    }

    public void stop() {
        RConsole.println("Shutting down");
        this.setStatus(Status.EXIT);
        this.setRunning(false);
        dt.silent = false;
        svt.silent = false;
        dt.run = false;
        svt.running = false;
        Mover.INSTANCE.setRunning(false);
        Tools.delay(1000);
        dt.interrupt();
        svt.interrupt();

        Tools.delay(1000);
        
        robot.exit();
    }
}
