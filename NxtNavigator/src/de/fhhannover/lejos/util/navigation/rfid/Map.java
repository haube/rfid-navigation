/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.fhhannover.lejos.util.navigation.rfid;

import java.util.ArrayList;

/**
 *
 * @author Michael Antemann <antemann.michael at gmail.com>
 */
public enum Map {

    SMALL(4, 5);

    ArrayList<Tag> tags = new ArrayList<Tag>();
    Tag[][] map = null;

    Map(int x, int y) {
        map = new Tag[x][y];
    }

   public void addTag(Tag newTag){
        map[newTag.getX()][newTag.getY()]= newTag;
        tags.add(newTag);
   }
}
