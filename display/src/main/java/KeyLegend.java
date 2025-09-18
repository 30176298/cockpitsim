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
import javafx.scene.text.Font;

import display.CNST;

public class KeyLegend extends DisplayObject{
  
  public KeyLegend(Key parent, String text) {
    
    home = new Point2D((parent.home.getX() + 7), (parent.home.getY() + 17));
    //Key Legend should be no more than 8 characters, split 4/4
    String splitText = text.substring(0,4) + "\n" + text.substring(4,8);
    Text legendText = new Text(home.getX(), home.getY(), splitText);
    
    //Set text parameters
    legendText.setFont(new Font(15));
    legendText.setLineSpacing(5);
    legendText.setFill(Color.WHITE);
    
    groupChildren.add(legendText);
    
    //Commit new nodes to scene tree
    parent.groupChildren.add(sceneGroup);
  }
}