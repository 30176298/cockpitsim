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
import javafx.geometry.Point3D;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.animation.AnimationTimer;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.KeyCode;

import display.CNST;
import display.KeyPage;
import display.DisplayObject;
import display.DebugView;

public class AircraftData{
  
  protected Point3D pos = new Point3D(512.0, 512.0, 0.0);
  protected Point3D vel;
  protected Point3D rightVect;
  protected Point3D upVect;
  protected long lastNow = 0;
  protected double deltaTime = 0;
  protected CockpitSim parent;
  protected CNST.ROLLING rolling = CNST.ROLLING.NONE;
  protected CNST.PITCHING pitching = CNST.PITCHING.NONE;
  
  private DebugView debugView = new DebugView(this);
  
  public AircraftData(CockpitSim parent) {
    this.parent = parent;
    //Initial velocity
    vel = new Point3D(0.0, 1.0, 0.0);
    rightVect = new Point3D(-vel.getY(), vel.getX(), 0.0); 
    upVect = vel.crossProduct(rightVect).multiply(-1.0);
    
    debugView.setUpScene();
    
    startSimulation();
  }
  
  public Point3D getPos() {
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
          System.out.println("(" + pos.getX() + ", " + pos.getY() + ", " + pos.getZ() + ")" + "  -  " + deltaTime);
          
          //Handle updating aircraft heading
          // rightVect = new Point3D(vel.getY(), -vel.getX(), 0.0); //Right vector
          
          //Find upVector
          //upVect = vel.crossProduct(rightVect).multiply(-1.0);
          
          Point3D rollingVect = upVect.multiply(deltaTime / 100_000_000);
          Point3D pitchingVect = vel.multiply(deltaTime / 100_000_000);
          Point3D pitchingVectVel = upVect.multiply(-deltaTime / 100_000_000);
          
          switch(rolling) {
            case CNST.ROLLING.LEFT:
              rightVect = rightVect.add(rollingVect).normalize();
              upVect = vel.crossProduct(rightVect).multiply(-1.0);
              break;
            case CNST.ROLLING.RIGHT:
              rightVect = rightVect.subtract(rollingVect).normalize();
              upVect = vel.crossProduct(rightVect).multiply(-1.0);
              break;
          }
          switch(pitching) {
            case CNST.PITCHING.UP:
              upVect = upVect.subtract(pitchingVect).normalize();
              vel = vel.subtract(pitchingVectVel).normalize();
              break;
            case CNST.PITCHING.DOWN:
              upVect = upVect.add(pitchingVect).normalize();
              vel = vel.add(pitchingVectVel).normalize();
              break;
          }
        
        }
        //Update time record to allow calculating delta time
        lastNow = now;
        
        debugView.update();
      }
    };
    updater.start();
    
    //Add events to create keyboard control
    EventHandler keyboardEventPressed = new EventHandler<KeyEvent>() {
      public void handle(KeyEvent event) {
        switch(event.getCode()) {
          case KeyCode.LEFT:
            rolling = CNST.ROLLING.LEFT;
            break;
          case KeyCode.RIGHT:
            rolling = CNST.ROLLING.RIGHT;
            break;
          case KeyCode.UP:
            pitching = CNST.PITCHING.DOWN;
            break;
          case KeyCode.DOWN:
            pitching = CNST.PITCHING.UP;
            break;
        }
      }
    };
    EventHandler keyboardEventReleased = new EventHandler<KeyEvent>() {
      public void handle(KeyEvent event) {
        switch(event.getCode()) {
          case KeyCode.LEFT:
            rolling = CNST.ROLLING.NONE;
            break;
          case KeyCode.RIGHT:
            rolling = CNST.ROLLING.NONE;
            break;
          case KeyCode.UP:
            pitching = CNST.PITCHING.NONE;
            break;
          case KeyCode.DOWN:
            pitching = CNST.PITCHING.NONE;
            break;
        }
      }
    };
    parent.scene.addEventFilter(KeyEvent.KEY_PRESSED, keyboardEventPressed);
    parent.scene.addEventFilter(KeyEvent.KEY_RELEASED, keyboardEventReleased);
  }
}