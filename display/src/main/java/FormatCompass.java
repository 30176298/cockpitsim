package display;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.geometry.Point3D;
import javafx.animation.AnimationTimer;
import javafx.collections.ObservableList;

public class FormatCompass {
  private AircraftData aircraftData;
  private Line needle;
  private ObservableList groupChildren;
  
  public FormatCompass(CockpitSim parent) {
    this.aircraftData = parent.aircraftData;
    this.groupChildren = parent.groupChildren;
    
    Point3D compassCircleCentre = new Point3D(900, 500, 0);
    Circle compassCircle = new Circle(
        compassCircleCentre.getX(),
        compassCircleCentre.getY(),
        50
    );

    compassCircle.setFill(Color.WHITE);
    compassCircle.setStroke(Color.BLACK);
    groupChildren.add(compassCircle);
    
    needle = new Line(
        compassCircleCentre.getX(),
        compassCircleCentre.getY(),
        compassCircleCentre.getX(),
        compassCircleCentre.getY() - 40
    );
    needle.setStroke(Color.RED);
    groupChildren.add(needle);
    
    AnimationTimer updater = new AnimationTimer() {
      public void handle(long now) {
        double heading = aircraftData.getHeading();
        
        double radians = Math.toRadians(-heading);
        double x = compassCircleCentre.getX() + 40 * Math.sin(radians);
        double y = compassCircleCentre.getY() - 40 * Math.cos(radians);
        needle.setEndX(x);
        needle.setEndY(y);

      }
    };
    updater.start();
  }
}