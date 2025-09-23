package display;

import javafx.collections.ObservableList; 
import javafx.scene.Scene;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Line;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.geometry.Point2D;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.animation.AnimationTimer;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.KeyCode;

import display.CNST;
import display.KeyPage;
import display.DisplayObject;

public class AircraftData{
  
  private Point2D pos = new Point2D(512.0, 512.0);
  private Point2D vel;
  private long lastNow = 0;
  private double deltaTime = 0;
  private CockpitSim parent;
  private CNST.TURNING turning = CNST.TURNING.NONE;
  
  //Testing graphics objects
  private Rectangle simRect;
  private Line simLine;
  
  public AircraftData(CockpitSim parent) {
    this.parent = parent;
    vel = new Point2D(0, 1);
    
    //Testing graphics objects
    simRect = new Rectangle(512, 512, 20, 40);
    simRect.setFill(Color.RED);
    
    simLine = new Line(512.0, 512.0, 0.0, 0.0);
    //simLine.setFill(Color.GREEN);
    simLine.setStroke(Color.GREEN);
    simLine.setStrokeWidth(10.0);
    
    parent.groupChildren.add(simRect);
    parent.groupChildren.add(simLine);
  
    startSimulation();
  }
  
  public Point2D getPos() {
    return pos;
  }
  
  private void startSimulation() {
    //Add updater to handle updating data each frame
    AnimationTimer updater = new AnimationTimer() {
      public void handle(long now) {
        deltaTime = now - lastNow;
        if (deltaTime > 0) {   
          //Calculate delta time
          deltaTime = Math.min(deltaTime, CNST.DELTA_TIME_CAP);
          
          //Handle updating aircraft position
          pos = pos.add(vel.multiply(deltaTime / 10_000_000));
          
          //Debug
          System.out.println("(" + pos.getX() + ", " + pos.getY() + ")" + "  -  " + deltaTime);
          
          //Handle updating aircraft heading
          Point2D rightVect = new Point2D(vel.getY(), -vel.getX()).multiply(deltaTime / 100_000_000); //Small right vector
          switch(turning) {
            case CNST.TURNING.LEFT:
              vel = vel.add(rightVect).normalize();
              break;
            case CNST.TURNING.RIGHT:
              vel = vel.subtract(rightVect).normalize();
              break;
          }
          
          //Move testing rectangle
          simRect.setX(pos.getX());
          simRect.setY(pos.getY());
          //Move testing line
          simLine.setEndX(512.0 + (vel.getX() * 200));
          simLine.setEndY(512.0 + (vel.getY() * 200));
        }
        //Update time record to allow calculating delta time
        lastNow = now;
      }
    };
    updater.start();
    
    //Add events to create keyboard control
    EventHandler keyboardEventPressed = new EventHandler<KeyEvent>() {
      public void handle(KeyEvent event) {
        switch(event.getCode()) {
          case KeyCode.LEFT:
            turning = CNST.TURNING.LEFT;
            break;
          case KeyCode.RIGHT:
            turning = CNST.TURNING.RIGHT;
            break;
        }
      }
    };
    EventHandler keyboardEventReleased = new EventHandler<KeyEvent>() {
      public void handle(KeyEvent event) {
        switch(event.getCode()) {
          case KeyCode.LEFT:
            turning = CNST.TURNING.NONE;
            break;
          case KeyCode.RIGHT:
            turning = CNST.TURNING.NONE;
            break;
        }
      }
    };
    parent.scene.addEventFilter(KeyEvent.KEY_PRESSED, keyboardEventPressed);
    parent.scene.addEventFilter(KeyEvent.KEY_RELEASED, keyboardEventReleased);
  }
}