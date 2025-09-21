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

public abstract class DisplayObject{
  
  //Top left coordinate
  protected Point2D home;
  
  protected DisplayObject parent;
  
  //Create new scene tree group to house elements
  protected Group sceneGroup = new Group();
  ObservableList groupChildren = sceneGroup.getChildren();
  
}