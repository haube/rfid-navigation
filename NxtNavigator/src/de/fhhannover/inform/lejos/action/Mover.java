/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.fhhannover.inform.lejos.action;

import ch.aplu.nxt.Gear;
import ch.aplu.nxt.MotorPort;

/**
 *
 * @author Michael Antemann <antemann.michael at gmail.com>
 */
public enum Mover {

    INSTANCE;
    private Gear gear = new Gear();
    private boolean running = false;
    private int speed = 30;

    Mover() {
        gear.setSpeed(30);
        gear.setVelocity(1);
    }

    public void init(MotorPort left, MotorPort right, int speed) {
        gear = new Gear(left, right);
        this.speed = speed;
        gear.setSpeed(speed);
        gear.setVelocity(0.1);
    }

    public void configure() {
        
    }

    public Gear getGear() {
        return gear;
    }

    public void setGear(Gear gear) {
        this.gear = gear;
    }

    public void setRunning(boolean b) {
        this.running = b;
    }
}
