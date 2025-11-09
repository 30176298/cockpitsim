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
import javafx.scene.transform.Rotate;

import display.AircraftData;
import display.CNST;
import display.KeyPage;
import display.DisplayObject;

//add bogey identifier when they are within certain range
//x - 512.0, y - 442.0 | circleCentre

public class FormatRadar extends Format{
  private double currentAngle = 0;
  private Point3D circleCentre;
  private long lastNow = 0;
  private long deltaTime = 0;
  private Text blankText = new Text();
  private ArrayList<Point3D> bogies; //actual bogies
  private ArrayList<Point3D> screenBogies = new ArrayList<Point3D>(); //bogies shown on radar
  private AircraftData aircraftData;
  private Point3D previousAircraftPos;
  private ArrayList<Circle> activeBlips = new ArrayList<Circle>();
  
  private double radar_range_metres = 2500.0;

  private final double RADAR_RADIUS_PIXELS = 115.0;
  private double getScaleFactor() {
    return RADAR_RADIUS_PIXELS / radar_range_metres;
    }
  
  public FormatRadar(MHDD parent) {
    this.parent = parent;
    this.bogies = parent.parent.bogies;
    home = parent.home;
    this.aircraftData = parent.parent.aircraftData;
    this.previousAircraftPos = aircraftData.getPos();
    
    //create radar display
    circleCentre = new Point3D(
      (home.getX() + CNST.SCREEN_SIZE / 2),
      (home.getY() + CNST.SCREEN_SIZE / 2),
      0
    );

    //scale bogies to screenbogies
    for (int i = 0; i < bogies.size(); i++) {
      Point3D worldBogey = bogies.get(i);
      Point3D relativePos = worldBogey.subtract(previousAircraftPos);
      Point3D screenPos = new Point3D(
        circleCentre.getX() + relativePos.getX() * getScaleFactor(),
        circleCentre.getY() - relativePos.getY() * getScaleFactor(),
        0.0
      );
      screenBogies.add(screenPos);
    }
    
    setUpKeys();
    
    keyPages[0].select();
    
    //create radar display
    Circle radarCircle = new Circle(circleCentre.getX(), circleCentre.getY(), 120);
    radarCircle.setFill(Color.TRANSPARENT);
    radarCircle.setStroke(Color.GREEN);
    radarCircle.setStrokeWidth(2);
    groupChildren.add(radarCircle);     
    
    double radius = 120;
    double radarRotationSpeed = Math.toRadians(270);

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

                updateRelCoords(screenBogies);

                //find aircraft heading
                Point3D velocity = aircraftData.getVel();
                Point3D xyVel = new Point3D(velocity.getX(), velocity.getY(), 0.0);
                Point3D north = CNST.NORTH;
                double heading = Math.toRadians(xyVel.angle(north));
                Point3D east = CNST.EAST;
                if (xyVel.dotProduct(east) < 0.0) {
                  heading = -heading;
                }

                for (int i = 0; i < screenBogies.size(); i++) {
                  Point3D bogey = screenBogies.get(i);
                  
                  //pos relative to radar centre
                  double relX = bogey.getX() - circleCentre.getX();
                  double relY = bogey.getY() - circleCentre.getY();
                  
                  //rotate about radarcentre depending on heading
                  double rotatedX = relX * Math.cos(-heading) - relY * Math.sin(-heading);
                  double rotatedY = relX * Math.sin(-heading) + relY * Math.cos(-heading);
                  
                  Point3D rotatedBogey = new Point3D(
                    circleCentre.getX() + rotatedX,
                    circleCentre.getY() + rotatedY,
                    0
                  );
                  
                  scanForBogies(radarLine, circleCentre, rotatedBogey, i);
                }
                
            }            
            lastNow = now;
        }
    };
    rotationTimer.start();
  }
  
  protected void setUpKeys() {
    setUpFormatMenu();
  }
  
  //blip bogies on radar
  private void scanForBogies(Line radarLine, Point3D centrePoint, Point3D bogey, int bogeyIndex) {
    double bogeyX = bogey.getX();
    double bogeyY = bogey.getY();
    
    double radarVectorX = radarLine.getEndX() - centrePoint.getX();
    double radarVectorY = radarLine.getEndY() - centrePoint.getY();

    double bogeyVectorX = bogeyX - centrePoint.getX();
    double bogeyVectorY = bogeyY - centrePoint.getY();

    double radarLength = Math.sqrt(radarVectorX * radarVectorX + radarVectorY * radarVectorY);
    double bogeyLength = Math.sqrt(bogeyVectorX * bogeyVectorX + bogeyVectorY * bogeyVectorY);

    if (bogeyLength > RADAR_RADIUS_PIXELS) {
      return; //out of radar range
    }

    radarVectorX = radarVectorX / radarLength;
    radarVectorY = radarVectorY / radarLength;
    bogeyVectorX = bogeyVectorX / bogeyLength;
    bogeyVectorY = bogeyVectorY / bogeyLength;

    double dotProduct = radarVectorX * bogeyVectorX + radarVectorY * bogeyVectorY;

    if (dotProduct > 0.995) {
      if (bogeyIndex < activeBlips.size() && activeBlips.get(bogeyIndex) != null) {
        return; //already on radar, prevents blurring
      }
      final Circle scannedBogey = new Circle(bogeyX, bogeyY, 5);
      scannedBogey.setFill(Color.RED);
      groupChildren.add(scannedBogey);
      
      while (activeBlips.size() <= bogeyIndex) {
        activeBlips.add(null);
      }
      activeBlips.set(bogeyIndex, scannedBogey);

      //fade out dots for a realistic "radar" effect
      FadeTransition fade = new FadeTransition(Duration.seconds(1), scannedBogey);
      fade.setFromValue(1.0);
      fade.setToValue(0.0);
      fade.setOnFinished(new EventHandler() {
        @Override
        public void handle(javafx.event.Event e) {
          groupChildren.remove(scannedBogey);
          activeBlips.set(bogeyIndex, null);
        }
      });
      fade.play();
    }
  }
  //update coords rel to bogies
  private void updateRelCoords(ArrayList<Point3D> screenBogies){
    Point3D currentPos = aircraftData.getPos();
        
    for (int i = 0; i < screenBogies.size(); i++) {
      Point3D worldBogey = bogies.get(i);
      Point3D relativePos = worldBogey.subtract(currentPos); 
      Point3D screenPos = new Point3D( 
        circleCentre.getX() + relativePos.getX() * getScaleFactor(), 
        circleCentre.getY() - relativePos.getY() * getScaleFactor(), 
        0.0 
      ); 
      screenBogies.set(i, screenPos); 
    }
    
    previousAircraftPos = currentPos;
  }
  
  //public getters
  public double getCurrentAngle() {
    return this.currentAngle;
  }
  
  public Point3D getCircleCentre() {
    return this.circleCentre;
  }
  
  public double getRadarRange() {
    return this.radar_range_metres;
  }
  
  protected ArrayList<Circle> getActiveBlips() {
    return this.activeBlips;
  }
  
  public boolean isTargetInRange(Point3D target) {
    if (target == null || circleCentre == null) return false;
    double distance = target.distance(circleCentre);
    return distance <= RADAR_RADIUS_PIXELS;
  }

  protected void setUpFormatMenu() {
    keyPages[0] = new KeyPage(this, "FORMMENU", "ZOOM IN ", "        ", "ZOOM OUT");
    
    EventHandler selectFormatMenuReleased = new EventHandler<MouseEvent>() {
      public void handle(MouseEvent event) {
        selectPage(1);
      }
    };
    keyPages[0].keys[0].setAction(selectFormatMenuReleased);

    EventHandler selectZoomInReleased = new EventHandler<MouseEvent>() {
      public void handle(MouseEvent event) {  
        if (radar_range_metres > 1500) {
          radar_range_metres -= 500;
        }
      }
    };
    keyPages[0].keys[1].setAction(selectZoomInReleased);
    
    EventHandler selectZoomOutReleased = new EventHandler<MouseEvent>() {
      public void handle(MouseEvent event) {
        if (radar_range_metres < 6000) {
          radar_range_metres += 500;
        }
      }
    };
    keyPages[0].keys[3].setAction(selectZoomOutReleased);
    
    // Format selection menu page
    keyPages[1] = new KeyPage(this, "BLANK   ", "NAV     ", "WPN     ", "RADAR   ");
    
    EventHandler<MouseEvent> selectBlankFormatReleased = event -> {
        parent.selectFormat(CNST.FORMAT.BLANK);
    };
    keyPages[1].keys[0].setAction(selectBlankFormatReleased);
    
    EventHandler<MouseEvent> selectNavFormatReleased = event -> {
        parent.selectFormat(CNST.FORMAT.NAV);
    };
    keyPages[1].keys[1].setAction(selectNavFormatReleased);
    
    EventHandler<MouseEvent> selectWpnFormatReleased = event -> {
        parent.selectFormat(CNST.FORMAT.WEAPONS);
    };
    keyPages[1].keys[2].setAction(selectWpnFormatReleased);
    
    EventHandler<MouseEvent> selectRadarFormatReleased = event -> {
        parent.selectFormat(CNST.FORMAT.RADAR);
    };
    keyPages[1].keys[3].setAction(selectRadarFormatReleased);
  }
}