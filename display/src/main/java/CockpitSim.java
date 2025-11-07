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
import javafx.geometry.Point3D;

import java.util.ArrayList;

import display.CNST;
import display.MHDD;


public class CockpitSim extends Application {
  
  //Create new scene tree group to house elements
  protected Group root = new Group();
  protected ObservableList groupChildren = root.getChildren();
  protected Scene scene;
  public AircraftData aircraftData;
  protected EnvData envData;
  protected FormatCompass compass;
  protected Rectangle ground;
  public MHDD leftMHDD;
  public MHDD centreMHDD;
  public MHDD rightMHDD;
  
  //Test window
  protected Group testRoot = new Group();
  protected ObservableList testGroupChildren = testRoot.getChildren();
  protected Scene testScene;
  protected Stage testStage;

  //Enemy coordinate array
  protected ArrayList<Point3D> bogies = new ArrayList<Point3D>();

  @Override
  public void start(Stage stage) {

    //Create scene with root node
    scene = new Scene(root, 1024, 1024);
    testScene = new Scene(testRoot, 768, 768);

    //temp vals until real bogies given
    Point3D radarCentre = new Point3D(0.0, 0.0, 0.0);
    //now in metres
    bogies.add(new Point3D((radarCentre.getX() + 2400), (radarCentre.getY() - 3300), 0));
    bogies.add(new Point3D((radarCentre.getX() - 1200), (radarCentre.getY() + 3350), 0));
    bogies.add(new Point3D((radarCentre.getX() + 2600), (radarCentre.getY() + 3250), 0));
    bogies.add(new Point3D((radarCentre.getX() - 2300), (radarCentre.getY() + 2100), 0));
    bogies.add(new Point3D((radarCentre.getX() + 4150), (radarCentre.getY() - 3600), 0));
    bogies.add(new Point3D((radarCentre.getX() - 4300), (radarCentre.getY() - 2300), 0));

    //Ground simulation
    ground = new Rectangle(-512.0,  CNST.AIM_POINT.getY(), 2048.0, 2048.0);
    ground.setFill(Color.DARKGREEN);
    groupChildren.add(ground);
    
    //Circle Reticule
    Circle reticule = new Circle(CNST.AIM_POINT.getX(), CNST.AIM_POINT.getY(), 30.0);
    reticule.setFill(Color.TRANSPARENT);
    reticule.setStroke(Color.WHITE);
    groupChildren.add(reticule);
    
    //Initialise Aircraft Data
    aircraftData = new AircraftData(this);

    //Initialise Environment Data
    envData = new EnvData(this);

    //Dashboard
    Circle dashboard = new Circle(512, 1792, 1536, Color.DIMGREY);

    //Add Dashboard to scene
    groupChildren.add(dashboard);

    //Create and add elements to scene tree
    leftMHDD = new MHDD(this, CNST.POS.LEFT);
    centreMHDD = new MHDD(this, CNST.POS.CENTRE);
    rightMHDD = new MHDD(this, CNST.POS.RIGHT); 

    //intialise compass
    compass = new FormatCompass(this);   
    
    //Initialise Test System
    TestSystem testSystem = new TestSystem(this);

    stage.setTitle("Cockpit Sim");
    scene.setFill(Color.LIGHTSKYBLUE);
    stage.setScene(scene);
    stage.show();
    stage.setX(0);
    startUpdater();
    
    testStage = new Stage();
    testStage.setTitle("Cockpit Sim - Testing");
    testScene.setFill(Color.LIGHTGREY);
    testStage.setScene(testScene);
    testStage.show();
    testStage.setX(1024);
    
  }

  public static void main(String[] args) {
    launch();
  }

  public void setBogies(ArrayList<Point3D> newBogies) {
    bogies = newBogies;
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
