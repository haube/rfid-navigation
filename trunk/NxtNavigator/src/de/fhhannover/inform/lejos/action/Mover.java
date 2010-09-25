/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.fhhannover.inform.lejos.action;

import ch.aplu.nxt.Gear;
import ch.aplu.nxt.MotorPort;
import ch.aplu.nxt.Tools;

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
                    st.notifyAll();


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
        gear.forward(100);
        gear.stop();




    }

    public class SweeperThread extends Thread {

        public boolean run = false;
        public int initial = 25;
        public int sweep = initial;

        public void run() {

            do {
                try {
                    while (run) {
                        gear.left(sweep);
                        this.yield();
                        sweep = sweep * (-2);
                        gear.right(sweep);
                        this.yield();
                        sweep = sweep * (-2);
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
