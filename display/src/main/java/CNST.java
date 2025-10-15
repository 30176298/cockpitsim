package display;

import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
//import display.KeyPage;

public class CNST {
    public static enum POS {
      LEFT,
      CENTRE,
      RIGHT
    }
    
    public static enum FORMAT {
      BLANK,
      RADAR,
      NAV,
      WEAPONS
    }
    
     public static enum ROLLING {
      NONE,
      LEFT,
      RIGHT
    }
    
    public static enum PITCHING {
      NONE,
      UP,
      DOWN
    }
    
    public static Point2D POSCoords(POS pos) {
      switch(pos) {
        case LEFT:
          return new Point2D(40, 630);
        case CENTRE:
          return new Point2D(370, 300);
        case RIGHT:
          return new Point2D(700, 630);
        default:
          return null;          
      }
    }
    
    public static final int SCREEN_SIZE = 284;
    public static final int KEY_WIDTH = 50;
    public static final int KEY_GAP = 28;
    public static final int KEY_Y = SCREEN_SIZE + KEY_GAP;
    public static final int KEY_X = KEY_WIDTH + KEY_GAP;
    
    public static final String BLANK_LEGEND = "        ";
    
    public static final long DELTA_TIME_CAP = 50_000_000;
    
    public static final Point2D AIM_POINT = new Point2D(512, 120);

    public static final Point3D WORLD_UP = new Point3D(0.0, 0.0, 1.0);
    public static final Point3D WORLD_DOWN = new Point3D(0.0, 0.0, -1.0);
    public static final Point3D NORTH = new Point3D(0.0, 1.0, 0.0);
    public static final Point3D EAST = new Point3D(1.0, 0.0, 0.0);
    
    //public static final KeyPage FORMAT_MENU = new KeyPage(this, "FORMMENU", "RDAR    ", "NAV     ", "WEAP    ");
}
