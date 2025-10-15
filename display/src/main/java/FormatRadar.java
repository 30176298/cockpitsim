package display;

import java.lang.Math;
import java.util.ArrayList;
import javafx.util.Duration;
import javafx.collections.ObservableList; 
import javafx.scene.Scene;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.shape.Circle;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.animation.AnimationTimer;
import javafx.animation.FadeTransition;

import display.AircraftData;
import display.CNST;
import display.KeyPage;
import display.DisplayObject;

//add bogey identifier when they are within certain range
//x - 512.0, y - 442.0 | circleCentre

public class FormatRadar extends Format{
  private double currentAngle = 0;
  private long lastNow = 0;
  private long deltaTime = 0;
  Text blankText = new Text();
  private ArrayList<Point3D> bogies;
  private ArrayList<Point3D> screenBogies = new ArrayList<Point3D>();
  private AircraftData aircraftData;
  private Point3D previousAircraftPos;
  
  //radar scaling consts
  private final double RADAR_RANGE_METERS = 5000.0;
  private final double RADAR_RADIUS_PIXELS = 115.0;
  private final double SCALE_FACTOR = RADAR_RADIUS_PIXELS / RADAR_RANGE_METERS;
  
  public FormatRadar(MHDD parent) {
    this.parent = parent;
    this.bogies = parent.parent.bogies;
    home = parent.home;
    this.aircraftData = parent.parent.aircraftData;
    this.previousAircraftPos = aircraftData.getPos();
    
    //create radar display
    Point3D circleCentre = new Point3D((home.getX() + CNST.SCREEN_SIZE / 2), (home.getY() + CNST.SCREEN_SIZE / 2), 0);

    for (int i = 0; i < bogies.size(); i++) {
      Point3D tempBogey = new Point3D(bogies.get(i).getX() + 512.0, bogies.get(i).getY() + 442.0, 0.0);
      screenBogies.add(i, tempBogey);
    }
    
    setUpKeys();
    
    //set Page 0 as active page
    keyPages[0].select();
    
    //create radar display
    Circle radarCircle = new Circle(circleCentre.getX(), circleCentre.getY(), 120);
    radarCircle.setFill(Color.TRANSPARENT);
    radarCircle.setStroke(Color.GREEN);
    radarCircle.setStrokeWidth(2);
    groupChildren.add(radarCircle);     
    
    double radius = 120;
    double radarRotationSpeed = Math.toRadians(180);

    //create radar line
    Line radarLine = new Line();
    radarLine.setStartX(circleCentre.getX());
    radarLine.setStartY(circleCentre.getY());
    radarLine.setStroke(Color.WHITE);
    radarLine.setStrokeWidth(2);
    groupChildren.add(radarLine);

    //rotate radarLine about centre
    AnimationTimer rotationTimer = new AnimationTimer() {
        @Override
        public void handle(long now) {
            deltaTime = now - lastNow;
            
            if (deltaTime > 0) {
                deltaTime = Math.min(deltaTime, CNST.DELTA_TIME_CAP);
              
                double deltaSeconds = deltaTime / 1_000_000_000.0;
                currentAngle += radarRotationSpeed * deltaSeconds;
                
                if (currentAngle > Math.PI * 2) {
                    currentAngle -= Math.PI * 2;
                }
                
                double x = circleCentre.getX() + radius * Math.cos(currentAngle);
                double y = circleCentre.getY() + radius * Math.sin(currentAngle);
                
                radarLine.setEndX(x);
                radarLine.setEndY(y);

                updateRelCoords(bogies); 
                scanForBogies(radarLine, circleCentre, bogies);
            }            
            lastNow = now;
        }
    };
    rotationTimer.start();
  }
  
  private void setUpKeys() {
    setUpFormatMenu();
  }
  
  //blip bogies on radar
  private void scanForBogies(Line radarLine, Point3D centrePoint, ArrayList<Point3D> bogies) {
    Point3D bogey;

    for (int i = 0; i < bogies.size(); i++) {
      bogey = screenBogies.get(i);
      double bogeyX = bogey.getX();
      double bogeyY = bogey.getY();
      
      double radarVectorX = radarLine.getEndX() - centrePoint.getX();
      double radarVectorY = radarLine.getEndY() - centrePoint.getY();

      double bogeyVectorX = bogeyX - centrePoint.getX();
      double bogeyVectorY = bogeyY - centrePoint.getY();

      double radarLength = Math.sqrt(radarVectorX * radarVectorX + radarVectorY * radarVectorY);
      double bogeyLength = Math.sqrt(bogeyVectorX * bogeyVectorX + bogeyVectorY * bogeyVectorY);

      if (bogeyLength > RADAR_RADIUS_PIXELS) {
        continue; //out of radar range
      }

      radarVectorX = radarVectorX / radarLength;
      radarVectorY = radarVectorY / radarLength;
      bogeyVectorX = bogeyVectorX / bogeyLength;
      bogeyVectorY = bogeyVectorY / bogeyLength;

      double dotProduct = radarVectorX * bogeyVectorX + radarVectorY * bogeyVectorY;

      if (dotProduct > 0.995) {
        final Circle scannedBogey = new Circle(bogeyX, bogeyY, 5);
        scannedBogey.setFill(Color.RED);
        groupChildren.add(scannedBogey);
        
        //fade out dots for a realistic "radar" effect
        FadeTransition fade = new FadeTransition(Duration.seconds(1), scannedBogey);
        fade.setFromValue(1.0);
        fade.setToValue(0.0);
        fade.setOnFinished(e -> groupChildren.remove(scannedBogey));
        fade.play();
      }
    }
  }

  private void updateRelCoords(ArrayList<Point3D> bogies){
    Point3D currentPos = aircraftData.getPos();
    Point3D posChange = currentPos.subtract(previousAircraftPos);
    
    //scale coords to radar
    Point3D scaledDelta = new Point3D(
      posChange.getX() * SCALE_FACTOR,
      posChange.getY() * SCALE_FACTOR,
      posChange.getZ() * SCALE_FACTOR
    );
    
    //moves bogies relative to aircraft
    for (int i = 0; i < bogies.size(); i++) {
      Point3D updatedBogey = screenBogies.get(i).add(scaledDelta);
      screenBogies.set(i, updatedBogey);
    }
    
    previousAircraftPos = currentPos;
  }
}
