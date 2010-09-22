/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.fhhannover.lejos.util.navigation.rfid;

/**
 *
 * @author Michael Antemann <antemann.michael at gmail.com>
 */
public class Tag {

    public long id =-1;
    public int reference = 0;
    public int x;
    public int y;
    public Tag(int reference, long id, int x,int y ){
        this.reference = reference;
        this.id = id;
        this.x  = x;
        this.y = y;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public int getReference() {
        return reference;
    }

    public void setReference(int reference) {
        this.reference = reference;
    }

    public void setY(int y) {
        this.y = y;
    }


    
}
