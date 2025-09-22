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
import display.KeyPage;
import display.DisplayObject;

public abstract class Format extends DisplayObject{
  
  //Allocate space for 5 menus
  protected KeyPage keyPages[] = new KeyPage[5]; 
  protected int selectedKeyPage = 0;
  protected MHDD parent;
  
  public void selectPage(int newPageNumber) {
    if(newPageNumber >= 0 && newPageNumber <= 4) {
      //Deselect current KeyPage
      keyPages[selectedKeyPage].deselect();
      selectedKeyPage = newPageNumber;
      //Select new KeyPage
      keyPages[selectedKeyPage].select();
    }
  }
  
  public void select() {
    //Commit new nodes to scene tree
    parent.groupChildren.add(sceneGroup);
  }
  
  public void deselect() {
    //Remove nodes from scene tree
    parent.groupChildren.remove(sceneGroup);
  }
  
  protected void setUpFormatMenu() {
    keyPages[0] = new KeyPage(this, "FORMMENU", CNST.BLANK_LEGEND, CNST.BLANK_LEGEND, CNST.BLANK_LEGEND);
    keyPages[1] = new KeyPage(this, "FORMMENU", "RDAR    ", "NAV     ", "WEAP    ");
    
    //Create logic for select/deselect format menu
    EventHandler selectFormatMenuReleased = new EventHandler<MouseEvent>() {
      public void handle(MouseEvent event) {
        selectPage(1);
      }
    };
    keyPages[0].keys[0].setAction(selectFormatMenuReleased);
    EventHandler deselectFormatMenuReleased = new EventHandler<MouseEvent>() {
      public void handle(MouseEvent event) {
        selectPage(0);
      }
    };
    keyPages[1].keys[0].setAction(deselectFormatMenuReleased);
    
    //Create logic for RADAR format selection
    EventHandler selectRadarFormatReleased = new EventHandler<MouseEvent>() {
      public void handle(MouseEvent event) {
        parent.selectFormat(CNST.FORMAT.RADAR);
      }
    };
    keyPages[1].keys[1].setAction(selectRadarFormatReleased);
    
    //Create logic for NAV format selection
    EventHandler selectNavFormatReleased = new EventHandler<MouseEvent>() {
      public void handle(MouseEvent event) {
        parent.selectFormat(CNST.FORMAT.NAV);
      }
    };
    keyPages[1].keys[2].setAction(selectNavFormatReleased);
    
    //Create logic for WEAPONS format selection
    EventHandler selectWeaponsFormatReleased = new EventHandler<MouseEvent>() {
      public void handle(MouseEvent event) {
        parent.selectFormat(CNST.FORMAT.WEAPONS);
      }
    };
    keyPages[1].keys[3].setAction(selectWeaponsFormatReleased);
    
  }
  
}