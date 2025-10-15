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
import java.util.ArrayList;

import display.CNST;
import display.DisplayObject;

public class EnvData{

  private CockpitSim parent;
  private ArrayList<Point3D> bogies;

  public EnvData(CockpitSim parent) {
    this.parent = parent;
    this.bogies = parent.bogies;
    startSimulation();
  }

  private void startSimulation() {
    //Add updater to handle updating data each frame
    AnimationTimer updater = new AnimationTimer() {
      public void handle(long now) {
        System.out.println("" + bogies.get(0).getX());
      }
    };
    updater.start();
  }


}
