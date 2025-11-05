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

public abstract class Weapon {
    protected String typeName;
    protected int ammoCount;
    protected int maxAmmo;

    public Weapon(String typeName, int ammoCount, int maxAmmo) {
        this.typeName = typeName;
        this.ammoCount = ammoCount;
        this.maxAmmo = maxAmmo;
    }

    public String getTypeName() { return typeName; }
    public int getAmmoCount() { return ammoCount; }
    public int getMaxAmmo() { return maxAmmo; }

    public abstract String getWeaponInfo();
}


