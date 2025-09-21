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

public class KeyPage extends DisplayObject{
  
  public Key keys[] = new Key[4];
  
  //Blank legend constructor
  public KeyPage(Format parent) {
  
    this.parent = parent;
    home = parent.home;
    
    //Key class will add keys to scene    
    keys[0] = new Key(this, 0, "        ");
    keys[1] = new Key(this, 1, "        ");
    keys[2] = new Key(this, 2, "        ");
    keys[3] = new Key(this, 3, "        ");
    

  }
  
  public KeyPage(Format parent, String legend1, String legend2, String legend3, String legend4) {
    
    this.parent = parent;
    home = parent.home;
    
    //Key class will add keys to scene    
    keys[0] = new Key(this, 0, legend1);
    keys[1] = new Key(this, 1, legend2);
    keys[2] = new Key(this, 2, legend3);
    keys[3] = new Key(this, 3, legend4);
  }
  
  public KeyPage(Format parent, String legend0, String legend1, String legend2, String legend3, EventHandler action0, EventHandler action1, EventHandler action2, EventHandler action3) {
    
    this.parent = parent;
    home = parent.home;
    
    //Key class will add keys to scene    
    keys[0] = new Key(this, 0, legend0);
    keys[1] = new Key(this, 1, legend1);
    keys[2] = new Key(this, 2, legend2);
    keys[3] = new Key(this, 3, legend3);
    
    keys[0].setAction(action0);
    keys[1].setAction(action1);
    keys[2].setAction(action2);
    keys[3].setAction(action3);
  }
  
  public void select() {
    //Commit new nodes to scene tree
    parent.groupChildren.add(sceneGroup);
  }
  
  public void deselect() {
    //Remove nodes from scene tree
    parent.groupChildren.remove(sceneGroup);
  }
}