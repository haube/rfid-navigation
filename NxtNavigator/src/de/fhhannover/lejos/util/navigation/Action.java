package de.fhhannover.lejos.util.navigation;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Michael Antemann <antemann.michael at gmail.com>
 */
public enum Action {

    SWEEPING("sweeping",0),
    RFIDFOUND("foundrfid",1),
    ONLINE("online",2),
    ONTRACK("ontrack",3),
    OFFLINE("offline",4),
   OFFTRACK("offtrack",4);
    int actionCode;
    String identifier;

    Action(String identifier,int code) {
        this.identifier = identifier;
        this.actionCode = code;
    }

    public int getActionCode() {
        return actionCode;
    }

    public void setActionCode(int actionCode) {
        this.actionCode = actionCode;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String actionIdentifier) {
        this.identifier = actionIdentifier;
    }

    public static void printall() {
        for (Action action : Action.values()) {
            System.out.println("number: " + action.getActionCode() + " action: " + action.getIdentifier());
        }
    }

    @Override
    public String toString() {
        return "Action{" + " " + actionCode + " =" + identifier + '}';
    }
    
}
