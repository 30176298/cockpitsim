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

public class Format{
  
    protected static Point2D home;
    
    //Create new scene tree group to house elements
    protected Group formatGroup = new Group();
    ObservableList formatGroupList = formatGroup.getChildren();
  
    public Format(MHDD parent) {
      
      
    }
}