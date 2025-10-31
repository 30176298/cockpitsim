package display;

import javafx.collections.ObservableList; 
import javafx.scene.Scene;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Polygon;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.geometry.Rectangle2D;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.animation.AnimationTimer;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

// import javax.lang.model.element.AnnotationMirror; I honestly have no idea when or where this line came from

import display.Format;

import java.io.FileInputStream;

import display.CNST;
import display.KeyPage;
import display.DisplayObject;

public class FormatNav extends Format{
  
  private AircraftData aircraftData;
  private Point3D previousAircraftPos;

  public FormatNav(MHDD parent) {
    this.parent = parent;
    home = parent.home;
    this.aircraftData = parent.parent.aircraftData;
    previousAircraftPos = aircraftData.getPos();
    
    setUpKeys();
    
    //Set Page 0 as active page
    keyPages[0].select();
        
    // Map code
    //try {
      Image mapIcon = new Image("/Usean_Topographical_Map.bmp");
      ImageView mapView = new ImageView();
      mapView.setX(home.getX());
      mapView.setY(home.getY());
      mapView.setViewport(new Rectangle2D(home.getX(), home.getY(), CNST.SCREEN_SIZE, CNST.SCREEN_SIZE));
      mapView.setImage(mapIcon);
      groupChildren.add(mapView);
    /*} catch (FileNotFoundException e) {
      Text errorText = new Text(home.getX() + 30, home.getY() + 157, "MAP NOT FOUND");
      errorText.setFont(new Font(20));
      errorText.setFill(Color.RED);
      groupChildren.add(errorText);
      e.printStackTrace();
    }*/
    // Draw arrow polygon
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

    
  }
  
  private void setUpKeys() {
    setUpFormatMenu();
  }
}