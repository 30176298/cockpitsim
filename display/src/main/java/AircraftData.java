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
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

import display.CNST;
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
  protected double rollAngle = 0.0;
  protected Rectangle ground;
  protected Rotate groundRotate = new Rotate(0.0, CNST.AIM_POINT.getX(), CNST.AIM_POINT.getY());
  protected Translate groundTranslate = new Translate();
  
  private DebugView debugView = new DebugView(this);
  
  public AircraftData(CockpitSim parent) {
    this.parent = parent;
    this.ground = parent.ground;
    //Initial velocity
    vel = new Point3D(0.0, 1.0, 0.0);
    rightVect = new Point3D(vel.getY(), -vel.getX(), 0.0);
    upVect = vel.crossProduct(rightVect).multiply(-1.0);
    
    //debugView.setUpScene();
    
    startSimulation();
  }
  
  public Point3D getPos() {
    return pos;
  }

  public Point3D getVel() {
    return vel;
  }

  public double getHeading() {
    Point3D xyVel = new Point3D (vel.getX(), vel.getY(), 0.0);
    double heading = xyVel.angle(CNST.NORTH);
    if (xyVel.dotProduct(CNST.EAST) < 0.0) heading = 360 - heading;
    return heading;
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
          
          //Find roll angle
          double rollAngle = getRollAngle();

          //Find pitch angle
          double pitchAngle = -1.0 * (vel.angle(CNST.WORLD_UP) - 90.0);

          //Update horizon graphics
          groundRotate.setAngle(-rollAngle);
          groundTranslate.setY(pitchAngle * 16.0);

          //Create small vector to nudge aircraft by
          Point3D rollingVect = upVect.multiply(deltaTime / 200_000_000);
          Point3D pitchingVect = vel.multiply(deltaTime / 400_000_000);

          switch(rolling) {
            case LEFT:
              rightVect = rightVect.add(rollingVect).normalize();
              upVect = vel.crossProduct(rightVect).multiply(-1.0);
              break;
            case RIGHT:
              rightVect = rightVect.subtract(rollingVect).normalize();
              upVect = vel.crossProduct(rightVect).multiply(-1.0);
              break;
          }
          switch(pitching) {
            case UP:
              upVect = upVect.subtract(pitchingVect).normalize();
              vel = rightVect.crossProduct(upVect).multiply(-1.0);
              break;
            case DOWN:
              upVect = upVect.add(pitchingVect).normalize();
              vel = rightVect.crossProduct(upVect).multiply(-1.0);
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
          case LEFT:
            rolling = CNST.ROLLING.LEFT;
            break;
          case RIGHT:
            rolling = CNST.ROLLING.RIGHT;
            break;
          case UP:
            pitching = CNST.PITCHING.DOWN;
            break;
          case DOWN:
            pitching = CNST.PITCHING.UP;
            break;
        }
      }
    };
    EventHandler keyboardEventReleased = new EventHandler<KeyEvent>() {
      public void handle(KeyEvent event) {
        switch(event.getCode()) {
          case LEFT:
            rolling = CNST.ROLLING.NONE;
            break;
          case RIGHT:
            rolling = CNST.ROLLING.NONE;
            break;
          case UP:
            pitching = CNST.PITCHING.NONE;
            break;
          case DOWN:
            pitching = CNST.PITCHING.NONE;
            break;
        }
      }
    };
    parent.scene.addEventFilter(KeyEvent.KEY_PRESSED, keyboardEventPressed);
    parent.scene.addEventFilter(KeyEvent.KEY_RELEASED, keyboardEventReleased);

    //Control graphics view out window
    ground.getTransforms().add(groundRotate);
    ground.getTransforms().add(groundTranslate);
  }

    private double getRollAngle() {
      //Determine right wing high
      boolean negativeRoll = rightVect.dotProduct(CNST.WORLD_UP) > rightVect.dotProduct(CNST.WORLD_DOWN);
      //upVector traces sine wave path during pitching 360
      double rollAngle = upVect.angle(CNST.WORLD_UP);
      //Correct for roll left vs roll right
      if (negativeRoll) rollAngle = -rollAngle;
      return rollAngle;
  }


}
