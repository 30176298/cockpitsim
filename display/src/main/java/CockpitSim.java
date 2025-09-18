package display;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.collections.ObservableList; 

import display.CNST;
import display.MHDD;


public class CockpitSim extends Application {
  
  //Create new scene tree group to house elements
  protected Group root = new Group();
  ObservableList groupChildren = root.getChildren();

  @Override
  public void start(Stage stage) {

      //Dashboard
      Circle dashboard = new Circle(512, 1792, 1536, Color.DIMGREY);
      
      //Add Dashboard to scene
      groupChildren.add(dashboard);
      
      //Create and add elements to scene tree
      MHDD LeftMHDD = new MHDD(this, CNST.POS.LEFT);
      MHDD CentreMHDD = new MHDD(this, CNST.POS.CENTRE);
      MHDD RightMHDD = new MHDD(this, CNST.POS.RIGHT);        
      
      //Create scene using assembled scene tree
      Scene scene = new Scene(root, 1024, 1024);
      stage.setTitle("Cockpit Sim");
      scene.setFill(Color.BLACK);
      stage.setScene(scene);
      stage.show();
  }

  public static void main(String[] args) {
      launch();
  }

}