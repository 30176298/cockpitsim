package display;

import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Circle;
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
import java.util.ArrayList;

import display.CNST;
import display.DisplayObject;

public class EnvData{

  private CockpitSim parent;
  private ArrayList<Point3D> bogies;
  private Point3D pos;
  private Point3D vel;

  public EnvData(CockpitSim parent) {
    this.parent = parent;
    this.bogies = parent.bogies;
    //this.pos = parent.aircraftData.pos;
    //this.vel = parent.aircraftData.vel;


    startSimulation();
  }

  private void startSimulation() {
    //Debug
    Text headingText = new Text(100, 100, "" + parent.aircraftData.getHeading());
    parent.groupChildren.add(headingText);

    Circle[] balloons = new Circle[bogies.size()];
    //Fill with empty circles
    for(int i = 0; i < balloons.length; i++) {
      balloons[i] = new Circle();
    }

    //Add updater to handle updating data each frame
    AnimationTimer updater = new AnimationTimer() {
      public void handle(long now) {

        for(int i = 0; i < bogies.size(); i++) {
          pos = parent.aircraftData.pos;
          vel = parent.aircraftData.vel;
          Point3D thisBogey = getRelativeBogeyCoords(bogies.get(i));
          double range = thisBogey.magnitude();

          //Circular view window boundary
          double viewAngle = projectXYPlane(pos).angle(projectXYPlane(pos.add(vel)), projectXYPlane(thisBogey));
          System.out.println("Range  " + i + " " + range);
          double balloonSize, balloonCentreX, balloonCentreY;
          Color balloonColour;
          System.out.println("Angle " + viewAngle);
          //Reject bogies outside of view
          boolean bogeyInFront = thisBogey.dotProduct(vel) > 0.0;
          if (bogeyInFront) {
            balloonSize = 1600.0 * (1.0 / (range + 0.01)); //Prevent div0
            balloonColour = Color.BEIGE;
          }
          else {
            balloonSize = 0.0;
            balloonColour = Color.TRANSPARENT;
          }
          //Calculate draw position
          boolean bogeyOnRight = thisBogey.dotProduct(parent.aircraftData.rightVect) > 0.0;
          double drawOffset = 4 * range * Math.sin(viewAngle * Math.PI / 180.0);
          balloonCentreX = bogeyOnRight ? 512.0 + drawOffset : 512.0 - drawOffset;

          double heightDifference = thisBogey.getZ();
          balloonCentreY = CNST.AIM_POINT.getY() - (4 * heightDifference);
          System.out.println("X  " + balloonCentreX);
          balloons[i].setRadius(balloonSize);
          balloons[i].setFill(balloonColour);
          balloons[i].setCenterX(balloonCentreX);
          balloons[i].setCenterY(balloonCentreY);

        }
        //Debug
        headingText.setText("" + parent.aircraftData.getHeading());

      }
    };
    updater.start();
    for(int i = 0; i < balloons.length; i++) {
      parent.groupChildren.add(balloons[i]);
      balloons[i].getTransforms().add(parent.aircraftData.groundRotate);
      balloons[i].getTransforms().add(parent.aircraftData.groundTranslate);
    }
  }

  private Point3D getRelativeBogeyCoords(Point3D refBogey) {
    return new Point3D(refBogey.getX() - pos.getX(), refBogey.getY() - pos.getY(), refBogey.getZ() - pos.getZ());
  }

  private Point2D projectXYPlane(Point3D p3) {
    return new Point2D(p3.getX(), p3.getY());
  }
}
