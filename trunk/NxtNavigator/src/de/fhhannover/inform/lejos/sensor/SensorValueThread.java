/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.fhhannover.inform.lejos.sensor;

import ch.aplu.nxt.CompassSensor;
import ch.aplu.nxt.LightSensor;
import ch.aplu.nxt.Tools;
import lejos.nxt.comm.RConsole;

/**
 *
 * @author Michael Antemann <antemann.michael at gmail.com>
 */
public class SensorValueThread extends Thread {

    private CompassSensor compass = null;
    private LightSensor light = null;
    public boolean running = true;
    int compasArrayIndex = 0;
    int lightArrayIndex = 0;
    float compassArray[] = new float[40];
    int lightArray[] = new int[40];
    public boolean collectLight = true;
    public boolean collectCompass = true;
    public boolean silent = true;
    private int currentLightValue =0 ;
    private float currentDirectionValue =0 ;
    public float threshold;

    public SensorValueThread(CompassSensor c, LightSensor l, int arraysize) {
        compassArray = new float[arraysize];
        lightArray = new int[arraysize];
        this.compass = c;
        this.light = l;
        this.light.activate(true);
        for (int i = 0; i < compassArray.length; i++) {
            compassArray[i] = compass.getLejosSensor().getDegreesCartesian();
            lightArray[i] = light.getLejosSensor().getNormalizedLightValue();
            Tools.delay(100);
        }
        threshold = this.getAvgLight() -30;
    }

    public void run() {
        int compasArrayIndex = 0;
        int lightArrayIndex = 0;


        while (running) {

            try {
                currentDirectionValue = compass.getLejosSensor().getDegreesCartesian();
                currentLightValue = light.getLejosSensor().getNormalizedLightValue();
                if (compass != null && collectCompass) {
                    compassArray[compasArrayIndex] = currentDirectionValue;
                    compasArrayIndex++;
                    compasArrayIndex = compasArrayIndex % compassArray.length;
                }
                if (light != null && collectLight) {
                    lightArray[lightArrayIndex] = currentLightValue;
                    lightArrayIndex++;
                    lightArrayIndex = lightArrayIndex % lightArray.length;
                }
                if (!silent) {
                    RConsole.println("current : " + currentLightValue + "avg light: " + getAvgLight() + "\tavg cmp: " + getAvgCompass());
                }
                this.sleep(100);

            } catch (InterruptedException ex) {
                running = false;
                silent = true;
            }
        }
        ;
    }

    public float getAvgLight() {
        int sum = 0;
        for (int i = 0; i < lightArray.length; i++) {
            sum = sum + lightArray[i];
        }
        return (float) sum / lightArray.length;
    }

    public float getAvgCompass() {
        float sum = 0;
        for (int i = 0; i < compassArray.length; i++) {
            sum = sum + compassArray[i] + 360;
        }
        return (float) (sum / compassArray.length) % 360;
    }

    public CompassSensor getCompass() {
        return compass;
    }

    public void setCompass(CompassSensor compass) {
        this.compass = compass;
    }

    public LightSensor getLight() {
        return light;
    }

    public void setLight(LightSensor light) {
        this.light = light;
    }

    public float getCurrentDirectionValue() {
        return currentDirectionValue;
    }

    public int getCurrentLightValue() {
        return currentLightValue;
    }
    
}
