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

public class FormatNav extends Format {
  private Image mapImage;
  private ImageView mapView;
  private Group mapContainer;     // contains mapView and is clipped to square
  private Rectangle clipRect;     // clip for the minimap square
  private Polygon aircraftMarker; // fixed in the centre of the minimap

  private final double MAP_SCALE = 4.0; // world space -> image space (pixels)
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
    Point3D pos = aircraftData.getPos(); // world coords (metres)
    double heading = -aircraftData.getHeading(); // degrees (dlipped)

    //Convert coordinates from world space to pixel space
    double imgCenterX = pos.getX() * MAP_SCALE;
    double imgCenterY = -(pos.getY() * MAP_SCALE);
    // Translate ImageView to reflect aicraft pos
    double halfScreen = CNST.SCREEN_SIZE / 2.0;
    double translateX = -imgCenterX + halfScreen;
    double translateY = -imgCenterY + halfScreen;
    mapView.setTranslateX(translateX);
    mapView.setTranslateY(translateY);

    // Remove any previous Rotate transforms and apply rotation
    mapView.getTransforms().removeIf(t -> t instanceof javafx.scene.transform.Rotate);
    javafx.scene.transform.Rotate mapRotate = new javafx.scene.transform.Rotate(heading, imgCenterX, imgCenterY);
    mapView.getTransforms().add(mapRotate);
  }
  
  private void setUpKeys() {
    setUpFormatMenu();
  }
}
