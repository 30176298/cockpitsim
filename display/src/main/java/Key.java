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
  
  private KeyLegend legend;
  public Rectangle keyBackground;
  
  //Default empty event handlers
  EventHandler eventHandlerPressed = new EventHandler<MouseEvent>() {
      public void handle(MouseEvent event) {
        keyBackground.setFill(Color.LIGHTGREY);
      }
    };
  EventHandler eventHandlerReleased = new EventHandler<MouseEvent>() {
      public void handle(MouseEvent event) {
        keyBackground.setFill(Color.BLACK);
      }
    };
  
  public Key(DisplayObject parent, int pos, String legendText) {
    
    //Set Key Coordinates based on MHDD pos and key number
    home = new Point2D((parent.home.getX() + (pos * CNST.KEY_X)), (parent.home.getY() + CNST.KEY_Y));

    //Generate base graphic components
    keyBackground = new Rectangle(home.getX(), home.getY(), CNST.KEY_WIDTH, CNST.KEY_WIDTH);
    keyBackground.setFill(Color.BLACK);
    groupChildren.add(keyBackground);
    
    //KeyLegend class will add key legends to scene
    legend = new KeyLegend(this, legendText);
    
    //Set up mouse events      
    sceneGroup.addEventFilter(MouseEvent.MOUSE_PRESSED, eventHandlerPressed);
    sceneGroup.addEventFilter(MouseEvent.MOUSE_RELEASED, eventHandlerReleased);
    
    //Commit new nodes to scene tree
    parent.groupChildren.add(sceneGroup);
  }
  
  public void setLegend(String legendText) {
    legend = new KeyLegend(this, legendText);
  }
  
  public void setAction(EventHandler newEventHandlerPressed) {
    //Remove old action
    sceneGroup.removeEventFilter(MouseEvent.MOUSE_PRESSED, eventHandlerPressed);
    
    eventHandlerPressed = newEventHandlerPressed;
    
    //Add new action
    sceneGroup.addEventFilter(MouseEvent.MOUSE_PRESSED, eventHandlerPressed);
  }
  
  public void setAction(EventHandler newEventHandlerPressed, EventHandler newEventHandlerReleased) {
    //Remove old actions
    sceneGroup.removeEventFilter(MouseEvent.MOUSE_PRESSED, eventHandlerPressed);
    sceneGroup.removeEventFilter(MouseEvent.MOUSE_RELEASED, eventHandlerReleased);
    
    eventHandlerPressed = newEventHandlerPressed;
    eventHandlerReleased = newEventHandlerReleased;
    
    //Add new actions
    sceneGroup.addEventFilter(MouseEvent.MOUSE_PRESSED, eventHandlerPressed);
    sceneGroup.addEventFilter(MouseEvent.MOUSE_RELEASED, eventHandlerReleased);
  }
}