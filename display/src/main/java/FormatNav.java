package display;

import javafx.animation.AnimationTimer;
import javafx.geometry.Point3D;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.transform.Scale;
import javafx.scene.input.MouseEvent;
import javafx.event.EventHandler;

public class FormatNav extends Format {
  private Image mapImage;
  private ImageView mapView;
  private Group mapContainer;     // contains mapView and is clipped to square
  private Rectangle clipRect;     // clip for the minimap square
  private Polygon aircraftMarker; // fixed in the centre of the minimap

  private double MAP_SCALE = 1.0; // world space -> image space (pixels)
  private final double MAP_SCALE_MIN = 0.25; 
  private final double MAP_SCALE_MAX = 4.0; 
  private final double MAP_SCALE_STEP = 0.25; 

  
  private AircraftData aircraftData;

  public FormatNav(MHDD parent) {
    this.parent = parent;
    this.home = parent.home;
    this.aircraftData = parent.parent.aircraftData;
    
    setUpKeys();
    keyPages[0].select();

    Image tmpImage = new Image("/Usean_Topographical_Map.bmp");

    this.mapImage = tmpImage;
    this.mapView = new ImageView(mapImage);

    mapContainer = new Group(mapView);
    mapContainer.setLayoutX(home.getX());
    mapContainer.setLayoutY(home.getY());

    // Clip to stay inside boundary
    clipRect = new Rectangle(0, 0, CNST.SCREEN_SIZE, CNST.SCREEN_SIZE);
    mapContainer.setClip(clipRect);

    groupChildren.add(mapContainer);

    Point2D centre = new Point2D((home.getX() + CNST.SCREEN_SIZE / 2), (home.getY() + CNST.SCREEN_SIZE / 2));
    Polygon arrow = new Polygon();
    arrow.getPoints().addAll(new Double[]{ centre.getX()      , centre.getY()      ,
                                           centre.getX() + 1.0, centre.getY() + 1.0,
                                           centre.getX()      , centre.getY() - 2.0, 
                                           centre.getX() - 1.0, centre.getY() + 1.0});
    arrow.setScaleX(10);
    arrow.setScaleY(10);
    arrow.setFill(Color.RED);
    groupChildren.add(arrow);

    // Start update loop
    AnimationTimer updater = new AnimationTimer() {
      @Override
      public void handle(long now) {
          update();
      }
    };
    updater.start();
  }

  private void update() {
    mapView.getTransforms().clear();
    
    Point3D pos = aircraftData.getPos(); // world coords (metres)
    double heading = -aircraftData.getHeading(); // degrees (dlipped)
    
    //Convert coordinates from world space to pixel space
    double imgCentreX = pos.getX() * MAP_SCALE;
    double imgCentreY = -(pos.getY() * MAP_SCALE);

    Scale scale = new Scale(MAP_SCALE, MAP_SCALE, imgCentreX, imgCentreY);
    mapView.getTransforms().add(scale);

    // Translate ImageView to reflect aicraft pos
    double halfScreen = CNST.SCREEN_SIZE / 2.0;
    double translateX = -imgCentreX + halfScreen;
    double translateY = -imgCentreY + halfScreen;
    mapView.setTranslateX(translateX);
    mapView.setTranslateY(translateY);

    // Remove any previous Rotate transforms and apply rotation
    javafx.scene.transform.Rotate mapRotate = new javafx.scene.transform.Rotate(heading, imgCentreX, imgCentreY);
    mapView.getTransforms().add(mapRotate);
  }

  private void zoomIn()  { if (MAP_SCALE < MAP_SCALE_MAX) { MAP_SCALE += MAP_SCALE_STEP; }  }
  private void zoomOut() { if (MAP_SCALE > MAP_SCALE_MIN) { MAP_SCALE -= MAP_SCALE_STEP; }  }
  
  private void setUpKeys() {
    setUpFormatMenu();
    
    // Create zoom control key page
    keyPages[2] = new KeyPage(this, "ZOOM    ", "ZOOM IN ", "ZOOM OUT", CNST.BLANK_LEGEND);
    
    // Zoom In button
    EventHandler zoomInAction = new EventHandler<MouseEvent>() {
      public void handle(MouseEvent event) {
        zoomIn();
      }
    };
    keyPages[2].keys[1].setAction(zoomInAction);
    
    // Zoom Out button
    EventHandler zoomOutAction = new EventHandler<MouseEvent>() {
      public void handle(MouseEvent event) {
        zoomOut();
      }
    };
    keyPages[2].keys[2].setAction(zoomOutAction);
    
    // Add ZOOM button to main menu
    keyPages[0].keys[1].setLegend("ZOOM    ");
    EventHandler showZoomMenu = new EventHandler<MouseEvent>() {
      public void handle(MouseEvent event) {
        selectPage(2);
      }
    };
    keyPages[0].keys[1].setAction(showZoomMenu);
    
    // Back button on zoom menu
    EventHandler hideZoomMenu = new EventHandler<MouseEvent>() {
      public void handle(MouseEvent event) {
        selectPage(0);
      }
    };
    keyPages[2].keys[0].setAction(hideZoomMenu);
  }
}