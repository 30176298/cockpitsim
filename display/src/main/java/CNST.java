package display;

import javafx.geometry.Point2D;

public class CNST {
    public static enum POS {
      LEFT,
      CENTRE,
      RIGHT
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
}