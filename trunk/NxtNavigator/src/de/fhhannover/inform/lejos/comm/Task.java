/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.fhhannover.inform.lejos.comm;

/**
 *
 * @author Michael Antemann <antemann.michael at gmail.com>
 */
public class Task {

    Type type;
    Message message;

    public Task(Type t, Message m) {
        type = t;
        message = m;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }
    

}
