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
  
  private Text weaponText;
  private double weaponTextY;

  public FormatWeapons(MHDD parent) {
    this.parent = parent;
    home = parent.home;
    
    setUpKeys();
    setUpWeaponMenu();
    
    //Set Page 0 as active page
    keyPages[0].select();

    //Create example text on display
    weaponText = new Text(home.getX() + 30, home.getY() + 140, "WEAPONS\nFORMAT");
    weaponTextY = home.getY() + 140;
    weaponText.setFont(new Font(30));
    weaponText.setFill(Color.WHITE);
    groupChildren.add(weaponText);
    
  }
  private void selectWeapon(String weapon) {
    assert true;
  }

  private void setUpKeys() {
    setUpFormatMenu();
  }


  // private void setupWeaponMenu() {
  //   Text title = new Text(home.getX() + 60, home.getY() + 100, "SELECT WEAPON:");
  //   title.setFont(new Font(20));
  //   title.setFill(Color.WHITE);

  //   Button gunBtn = new Button("Gun");
  //   gunBtn.setLayoutX(home.getX() + 50);
  //   gunBtn.setLayoutY(home.getY() + 140);

  //   Button missileBtn = new Button("Missile");
  //   missileBtn.setLayoutX(home.getX() + 120);
  //   missileBtn.setLayoutY(home.getY() + 140);

  //   Button bombBtn = new Button("Bomb");
  //   bombBtn.setLayoutX(home.getX() + 220);
  //   bombBtn.setLayoutY(home.getY() + 140);

  //   gunBtn.setOnAction(e -> selectWeapon("Gun"));
  //   missileBtn.setOnAction(e -> selectWeapon("Missile"));
  //   bombBtn.setOnAction(e -> selectWeapon("Bomb"));

  //   groupChildren.addAll(title, gunBtn, missileBtn, bombBtn);
  // }

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
    EventHandler selectGunFormatReleased = new EventHandler<MouseEvent>() {
      public void handle(MouseEvent event) {  
        weaponText.setY(weaponTextY);
        weaponText.setText("AMMO COUNT : 10\nMAX AMMO : 30");
      }
    };
    keyPages[0].keys[1].setAction(selectGunFormatReleased);
    
    //Create logic for NAV format selection
    EventHandler selectNavFormatReleased = new EventHandler<MouseEvent>() {
      public void handle(MouseEvent event) {
        weaponText.setY(weaponTextY - 90);
        weaponText.setText("GUIDANCE TYPE : \nUNKNOWN\nLOCK TIME : 30\nTARGET LOCKED : \nFALSE");
      }
    };
    keyPages[0].keys[2].setAction(selectNavFormatReleased);
    
    //Create logic for WEAPONS format selection
    EventHandler selectWeaponsFormatReleased = new EventHandler<MouseEvent>() {
      public void handle(MouseEvent event) {
        weaponText.setY(weaponTextY - 90);
        weaponText.setText("BLAST RADIUS : 10\nDETONATION MODE : \nREMOTE");
      }
    };
    keyPages[0].keys[3].setAction(selectWeaponsFormatReleased);
    
  }
}