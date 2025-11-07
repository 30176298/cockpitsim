package display;

import javafx.collections.ObservableList; 
import javafx.scene.Scene;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.geometry.Point2D;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.control.Button;

import display.CNST;
import display.KeyPage;
import display.DisplayObject;

public class FormatWeapons extends Format{
  
  public Text weaponText;
  public double weaponTextY;
  public KeyPage[] weaponKeyPages;

  // Keep instances of your weapons here
   private Gun gun;
   private Missile missile;
   private Bomb bomb;

  public FormatWeapons(MHDD parent) {
    this.parent = parent;
    home = parent.home;

  // Create weapon objects once
   gun = new Gun("Gun", 10, 30, false);
   missile = new Missile("Missile", 5, 10, true);
   bomb = new Bomb("Bomb", 2, 5, 10);
    
    setUpKeys();
    setUpWeaponMenu();
    
    //Set Page 0 as active page
    keyPages[0].select();

    //Create example text on display
    weaponText = new Text(home.getX() + 30, home.getY() + 140, "WEAPONS\nFORMAT");
    weaponTextY = home.getY() + 140;
    weaponText.setFont(new Font(24));
    weaponText.setFill(Color.WHITE);
    groupChildren.add(weaponText);
    
  }
  private void selectWeapon(String weapon) {
    assert true;
  }

  private void setUpKeys() {
    setUpFormatMenu();
  }

  protected void setUpWeaponMenu() {
    //keyPages[0] = new KeyPage(this, "FORMMENU", CNST.BLANK_LEGEND, CNST.BLANK_LEGEND, CNST.BLANK_LEGEND);
    keyPages[0] = new KeyPage(this, "FORMMENU", "GUN     ", "MISSILE ", "BOMB    ");
    
    //Create logic for select/deselect format menu
    EventHandler selectFormatMenuReleased = new EventHandler<MouseEvent>() {
      public void handle(MouseEvent event) {
        selectPage(1);
      }
    };
    keyPages[0].keys[0].setAction(selectFormatMenuReleased);

    //Create logic for GUN format selection
        EventHandler<MouseEvent> selectGunFormatReleased = event -> {
        weaponText.setY(weaponTextY);
        weaponText.setText(gun.getWeaponInfo());
    };
    keyPages[0].keys[1].setAction(selectGunFormatReleased);
    
    //Create logic for MISSILE format selection
    EventHandler<MouseEvent> selectMissileFormatReleased = event -> {
        weaponText.setY(weaponTextY - 90);
        weaponText.setText(missile.getWeaponInfo()); 
    };
    keyPages[0].keys[2].setAction(selectMissileFormatReleased);
    
    //Create logic for BOMB format selection
    EventHandler<MouseEvent> selectBombFormatReleased = event -> {
        weaponText.setY(weaponTextY - 90);
        weaponText.setText(bomb.getWeaponInfo());
    };
    keyPages[0].keys[3].setAction(selectBombFormatReleased);

    weaponKeyPages = keyPages;
    
  }
}
