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

public class Missile extends Weapon {
    private boolean lockedOn;

    public Missile(String typeName, int ammoCount, int maxAmmo, boolean lockedOn) {
        super(typeName, ammoCount, maxAmmo);
        this.lockedOn = lockedOn;
    }

    @Override
    public String getWeaponInfo() {
        return "TYPE : " + typeName + "\n" +
               "AMMO COUNT : " + ammoCount + "\n" +
               "MAX AMMO : " + maxAmmo + "\n" +
               "TARGET LOCKED : " + lockedOn;
    }
}