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
import javafx.animation.AnimationTimer;

import display.CNST;
import display.KeyPage;
import display.DisplayObject;

public class FormatRadar extends Format{
  
  Text blankText = new Text();
  
  public FormatRadar(MHDD parent) {
    this.parent = parent;
    home = parent.home;
    
    setUpKeys();
    
    //Set Page 0 as active page
    keyPages[0].select();
    
    //Create example text on display
    blankText = new Text(home.getX() + 30, home.getY() + 157, "RADAR FORMAT");
    blankText.setFont(new Font(30));
    blankText.setFill(Color.WHITE);
    groupChildren.add(blankText);    
  }
  
  private void setUpKeys() {
    setUpFormatMenu();
  }
}