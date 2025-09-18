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

import display.CNST;

public class Key extends DisplayObject{
  
  public Key(MHDD parent, int pos) {
    
    //Set Key Coordinates based on MHDD pos and key number
    home = new Point2D((parent.home.getX() + (pos * CNST.KEY_X)), (parent.home.getY() + CNST.KEY_Y));

    //Generate base graphic components
    Rectangle keyBackground = new Rectangle(home.getX(), home.getY(), CNST.KEY_WIDTH, CNST.KEY_WIDTH);
    keyBackground.setFill(Color.BLACK);
    groupChildren.add(keyBackground);
    
    //KeyLegend class will add key legends to scene
    KeyLegend legend = new KeyLegend(this, "        ");
    
    //Set up mouse events      
    sceneGroup.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
      public void handle(MouseEvent event) {
        keyBackground.setFill(Color.LIGHTGREY);
      }
    });
    sceneGroup.addEventFilter(MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>() {
      public void handle(MouseEvent event) {
        keyBackground.setFill(Color.BLACK);
      }
    });
    
    //Commit new nodes to scene tree
    parent.groupChildren.add(sceneGroup);
  }
}