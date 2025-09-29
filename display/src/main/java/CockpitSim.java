package display;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.collections.ObservableList; 
import javafx.animation.AnimationTimer;
import javafx.geometry.Point2D;

import java.util.ArrayList;

import display.CNST;
import display.MHDD;


public class CockpitSim extends Application {
  
  //Create new scene tree group to house elements
  protected Group root = new Group();
  protected ObservableList groupChildren = root.getChildren();
  protected Scene scene;
  protected AircraftData aircraftData;

  @Override
  public void start(Stage stage) {
    //Ground simulation
    Rectangle ground = new Rectangle(0.0,  CNST.AIM_POINT.getY(), 1024.0, 1024.0);
    ground.setFill(Color.DARKGREEN);
    groupChildren.add(ground);
    
    //Circle Crosshair
    Circle crosshair = new Circle(CNST.AIM_POINT.getX(), CNST.AIM_POINT.getY(), 30.0);
    crosshair.setFill(Color.TRANSPARENT);
    crosshair.setStroke(Color.GREEN);
    groupChildren.add(crosshair);
    
    //Dashboard
    Circle dashboard = new Circle(512, 1792, 1536, Color.DIMGREY);
    
    //Add Dashboard to scene
    groupChildren.add(dashboard);
    
    //Create and add elements to scene tree
    MHDD LeftMHDD = new MHDD(this, CNST.POS.LEFT);
    MHDD CentreMHDD = new MHDD(this, CNST.POS.CENTRE);
    MHDD RightMHDD = new MHDD(this, CNST.POS.RIGHT);    

    //Create scene using assembled scene tree
    scene = new Scene(root, 1024, 1024);
    
    //Initialise Aircraft Data
    aircraftData = new AircraftData(this);
    stage.setTitle("Cockpit Sim");
    scene.setFill(Color.LIGHTSKYBLUE);
    stage.setScene(scene);
    stage.show();
    startUpdater();
  }

  public static void main(String[] args) {
    launch();
  }
  
  private void startUpdater() {
    AnimationTimer updater = new AnimationTimer() {
      public void handle(long now) {
        //Update code goes here
      }
    };
    updater.start();
  }
}