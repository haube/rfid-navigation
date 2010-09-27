/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.fhhannover.inform.lejos.action;

import ch.aplu.nxt.Gear;
import ch.aplu.nxt.MotorPort;
import ch.aplu.nxt.Tools;
import de.fhhannover.lejos.util.navigation.Direction;
import java.util.logging.Level;
import java.util.logging.Logger;
import lejos.nxt.comm.RConsole;

/**
 *
 * @author Michael Antemann <antemann.michael at gmail.com>
 */
public enum Mover {

    INSTANCE;
    private Gear gear = new Gear();
    private boolean running = false;
    private int speed = 30;
    private SweeperThread st = new SweeperThread();
    private boolean fwd = false;
    private boolean sweep = false;
    private boolean stop = true;

    Mover() {
        gear.setSpeed(30);
        gear.setVelocity(1);
    }

    public void init(MotorPort left, MotorPort right, int speed) {
        gear = new Gear(left, right);
        this.speed = speed;
        gear.setSpeed(speed);
        gear.setVelocity(0.1);
        st.start();
    }

    public void shutdown() {
        st.interrupt();
        gear.stop();

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

    public void forward() {
        if (fwd) {
            if (gear.isMoving()) {
                return;
            } else {
                this.resetMove();
                fwd = true;
                move();
            }
        } else {
            this.resetMove();
            fwd = true;
            move();
        }
    }

    public void sweep() {
        if (sweep) {
            if (st.run == true) {
            } else {
                synchronized (st) {
                    st.notify();
                }
                return;
            }
        } else {
            this.resetMove();
            sweep = true;
            move();



        }
    }

    public void stop() {
        fwd = false;
        sweep = false;
        stop = true;
        move();


    }

    public void resetMove() {
        fwd = false;
        sweep = false;
        stop = true;


    }

    public void move() {
        if (stop && fwd && !sweep) {
            st.run = false;
            gear.stop();
            Tools.delay(200);
            gear.forward();


        } else if (stop && sweep && !fwd) {
            if (!st.run) {
                st.run = true;
                synchronized (st) {
                    st.notifyAll();


                }
            }

        } else if (stop && !sweep && !fwd) {
            st.run = false;
            gear.stop();


        }

    }

    public void getOverTag() {
        gear.forward(300);
        gear.stop();




    }

    public void turnToDirection(Direction direction) {
    }

    public void turnToDirection(Direction target, float direction) {
        int value = (int) ((target.getDirection() - direction));
        if (!gear.isMoving()) {
//            if(value < 15 && value > 0){
//                value = 25;
//            }else if(value > -15 && value <0){
//                value = -25;
//            }
            RConsole.println("rotating: " + value);
            gear.turnTo(value);

        }
    }

    public void sweepInSector(Direction target, float direction) {
        if (!gear.isMoving()) {
            int turn = 30;
            int value = (int) ((target.getDirection() - direction));
            if(value >180 ){
                turn = turn *-1;
            }
            gear.turnTo(turn);
            Thread.yield();
            Tools.delay(200);
            gear.turnTo(turn*-1);
            Thread.yield();
            Tools.delay(200);
        }
    }

    public class TurningThread extends Thread {

        public boolean run = false;
        public int initial = 25;
        public int sweep = initial;

        public void run() {
            while (true) {

                while (run) {
                    gear.turnTo(460, true);
                    Tools.delay(200);
                }

                synchronized (this) {
                    try {
                        wait();
                    } catch (InterruptedException ex) {
                    }
                }
            }
        }
    }

    public class SweeperThread extends Thread {

        public boolean run = false;
        public int initial = 25;
        public int sweep = initial;

        public void run() {

            do {
                try {
                    while (run) {
                        sweep = (int) (sweep * (-1.2));
                        gear.turnTo(sweep);
                        Tools.delay(initial);
                    }
                    synchronized (this) {
                        wait();
                    }
                    sweep = initial;
                } catch (InterruptedException ex) {
                }
            } while (true);
        }
    }
}
