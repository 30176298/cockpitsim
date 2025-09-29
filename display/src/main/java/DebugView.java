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
import display.AircraftData;

public class DebugView{
  

  private AircraftData parent;
  private Point3D pos = new Point3D(512.0, 512.0, 0.0);
  private Point3D vel;
  private Point3D rightVect;
  private Point3D upVect;
  
  //Testing graphics objects
  private Rectangle simRect;
  // private Line simLine;
  // private Line simRightLine;
  // private Line simUpLine;
  
  private ThreeAxisView topDown = new ThreeAxisView(new Point2D(256.0, 256.0));
  private ThreeAxisView fromRight = new ThreeAxisView(new Point2D(768.0, 256.0));
  private ThreeAxisView fromBehind = new ThreeAxisView(new Point2D(256.0, 768.0));
  
  public DebugView(AircraftData parent) {
    this.parent = parent;
    
    //Testing graphics objects
    simRect = new Rectangle(512, 512, 20, 40);
    simRect.setFill(Color.RED);
    
  }
  
  public class ThreeAxisView{
    public Line forward;
    public Line right;
    public Line up;
    public Point2D centre;
    private CockpitSim sim;
    
    public ThreeAxisView(Point2D centre) {
      this.centre = centre;
      forward = new Line(centre.getX(), centre.getY(), 0.0, 0.0);
      right = new Line(centre.getX(), centre.getY(), 0.0, 0.0);
      up = new Line(centre.getX(), centre.getY(), 0.0, 0.0);
      forward.setStroke(Color.GREEN);
      forward.setStrokeWidth(10.0);
      right.setStroke(Color.RED);
      right.setStrokeWidth(10.0);
      up.setStroke(Color.BLUE);
      up.setStrokeWidth(10.0);
    }
    
    public void addToScene(DebugView parent){
      this.sim = parent.parent.parent;
      sim.groupChildren.add(forward);
      sim.groupChildren.add(right);
      sim.groupChildren.add(up);
    }
  }
  
  public void setUpScene() {
    topDown.addToScene(this);
    fromRight.addToScene(this);
    fromBehind.addToScene(this);
  }
  
  public void update() {
    // Update values
    pos = parent.pos;
    vel = parent.vel;
    rightVect = parent.rightVect;
    upVect = parent.upVect;

    // Move testing rectangle
    simRect.setX(pos.getX());
    simRect.setY(pos.getY());
    // Move testing lines
    topDown.forward.setEndX(topDown.centre.getX() + (vel.getX() * 200));
    topDown.forward.setEndY(topDown.centre.getY() + (-vel.getY() * 200));
    topDown.right.setEndX(topDown.centre.getX() + (rightVect.getX() * 200));
    topDown.right.setEndY(topDown.centre.getY() + (-rightVect.getY() * 200));
    topDown.up.setEndX(topDown.centre.getX() + (upVect.getX() * 200));
    topDown.up.setEndY(topDown.centre.getY() + (-upVect.getY() * 200));
    
    fromRight.forward.setEndX(fromRight.centre.getX() + (vel.getY() * 200));
    fromRight.forward.setEndY(fromRight.centre.getY() + (-vel.getZ() * 200));
    fromRight.right.setEndX(fromRight.centre.getX() + (rightVect.getY() * 200));
    fromRight.right.setEndY(fromRight.centre.getY() + (-rightVect.getZ() * 200));
    fromRight.up.setEndX(fromRight.centre.getX() + (upVect.getY() * 200));
    fromRight.up.setEndY(fromRight.centre.getY() + (-upVect.getZ() * 200));
    
    fromBehind.forward.setEndX(fromBehind.centre.getX() + (vel.getX() * 200));
    fromBehind.forward.setEndY(fromBehind.centre.getY() + (-vel.getZ() * 200));
    fromBehind.right.setEndX(fromBehind.centre.getX() + (rightVect.getX() * 200));
    fromBehind.right.setEndY(fromBehind.centre.getY() + (-rightVect.getZ() * 200));
    fromBehind.up.setEndX(fromBehind.centre.getX() + (upVect.getX() * 200));
    fromBehind.up.setEndY(fromBehind.centre.getY() + (-upVect.getZ() * 200));
  }
  
}