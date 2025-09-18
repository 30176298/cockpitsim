package display;

import javafx.collections.ObservableList; 
import javafx.scene.Scene;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.geometry.Point2D;
import javafx.scene.text.Text;

import display.CNST;
import display.Key;

public class MHDD extends DisplayObject{
  
    public MHDD(CockpitSim parent, CNST.POS pos) {
      
      //Set MHDD Coordinates based on position
      home = CNST.POSCoords(pos);

      //Generate base graphic components
      Rectangle screenBackground = new Rectangle(home.getX(), home.getY(), CNST.SCREEN_SIZE, CNST.SCREEN_SIZE);
      screenBackground.setFill(Color.BLACK);
      groupChildren.add(screenBackground);
      
      //Key class will add keys to scene
      Key keyList[] = {new Key(this, 0), new Key(this, 1), new Key(this, 2), new Key(this, 3)};
      
      //Commit new nodes to scene tree
      parent.groupChildren.add(sceneGroup);
    }
}