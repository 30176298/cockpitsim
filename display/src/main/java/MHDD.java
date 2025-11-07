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
  
  protected CNST.FORMAT selectedFormat = CNST.FORMAT.BLANK;
  private Format format;
  protected CockpitSim parent;
  
  public MHDD(CockpitSim parent, CNST.POS pos) {
    this.parent = parent;
    
    //Set MHDD Coordinates based on position
    home = CNST.POSCoords(pos);

    //Generate base graphic components
    Rectangle screenBackground = new Rectangle(home.getX(), home.getY(), CNST.SCREEN_SIZE, CNST.SCREEN_SIZE);
    screenBackground.setFill(Color.BLACK);
    groupChildren.add(screenBackground);
    
    format = new FormatBlank(this);
    format.select();
    
    //Commit new nodes to scene tree
    parent.groupChildren.add(sceneGroup);
  }
  
  public void selectFormat(CNST.FORMAT newFormat) {
    //Deselect current Format
    format.deselect();
    selectedFormat = newFormat;
    //Select new Format
    switch(selectedFormat) {
      case BLANK:
        format = new FormatBlank(this);
        break;
      case RADAR:
        format = new FormatRadar(this);
        break;
      case NAV:
        format = new FormatNav(this);
        break;
      case WEAPONS:
        format = new FormatWeapons(this);
        break;
      default:
        format = new FormatBlank(this);
        break;
    }
    format.select();
  }
  
  public Group getGroup() {
    return new Group(groupChildren);
  }

  public Format getFormat() {
      return format;
  }
}