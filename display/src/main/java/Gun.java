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

public class Gun extends Weapon {
    private boolean safetyOn;

    public Gun(String typeName, int ammoCount, int maxAmmo, boolean safetyOn) {
        super(typeName, ammoCount, maxAmmo);
        this.safetyOn = safetyOn;
    }

    @Override
    public String getWeaponInfo() {
        return "TYPE : " + typeName + "\n" +
               "AMMO COUNT : " + ammoCount + "\n" +
               "MAX AMMO : " + maxAmmo + "\n" +
               "SAFETY ON : " + safetyOn;
    }
}