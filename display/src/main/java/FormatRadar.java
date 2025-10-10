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

import display.CNST;
import display.KeyPage;
import display.DisplayObject;

//add bogey identifier when they are within certain range

public class FormatRadar extends Format{
  private double currentAngle = 0;
  private long lastNow = 0;
  private long deltaTime = 0;
  Text blankText = new Text();
  private ArrayList<Point3D> bogies = new ArrayList<Point3D>();
  
  public FormatRadar(MHDD parent) {
    this.parent = parent;
    home = parent.home;
    bogies.add(new Point3D((512.0 + 40), (442.0 - 30), 0));
    bogies.add(new Point3D((512.0 - 20), (442.0 + 35), 0));
    bogies.add(new Point3D((512.0 + 60), (442.0 + 25), 0));
    bogies.add(new Point3D((512.0 - 30), (442.0 + 40), 0));
    bogies.add(new Point3D((512.0 + 15), (442.0 - 60), 0));
    bogies.add(new Point3D((512.0 - 30), (442.0 - 30), 0));
    setUpKeys();
    
    //set Page 0 as active page
    keyPages[0].select();
    
    //create radar display
    Point3D circleCentre = new Point3D((home.getX() + CNST.SCREEN_SIZE / 2), (home.getY() + CNST.SCREEN_SIZE / 2), 0);
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
                
                //System.out.println(circleCentre.getX()); 512.0
                //System.out.println(circleCentre.getY()); 442.0
                radarLine.setEndX(x);
                radarLine.setEndY(y);
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
      bogey = bogies.get(i);
      double bogeyX = bogey.getX();
      double bogeyY = bogey.getY();
      
      double radarVectorX = radarLine.getEndX() - centrePoint.getX();
      double radarVectorY = radarLine.getEndY() - centrePoint.getY();

      double bogeyVectorX = bogeyX - centrePoint.getX();
      double bogeyVectorY = bogeyY - centrePoint.getY();

      double radarLength = Math.sqrt(radarVectorX * radarVectorX + radarVectorY * radarVectorY);
      double bogeyLength = Math.sqrt(bogeyVectorX * bogeyVectorX + bogeyVectorY * bogeyVectorY);

      radarVectorX = radarVectorX / radarLength;
      radarVectorY = radarVectorY / radarLength;
      bogeyVectorX = bogeyVectorX / bogeyLength;
      bogeyVectorY = bogeyVectorY / bogeyLength;

      double dotProduct = radarVectorX * bogeyVectorX + radarVectorY * bogeyVectorY;

      if (dotProduct > 0.995) {
        final Circle scannedBogey = new Circle(bogeyX, bogeyY, 5);
        scannedBogey.setFill(Color.RED);
        groupChildren.add(scannedBogey);
        
        FadeTransition fade = new FadeTransition(Duration.seconds(1), scannedBogey);
        fade.setFromValue(1.0);
        fade.setToValue(0.0);
        fade.setOnFinished(e -> groupChildren.remove(scannedBogey));
        fade.play();
      }
    }
  }
}