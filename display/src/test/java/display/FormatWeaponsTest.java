package display;

import display.FormatWeapons;
import display.CockpitSim;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.util.WaitForAsyncUtils;
import javafx.application.Platform;
import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;

public class FormatWeaponsTest extends ApplicationTest {

    private CockpitSim sim;
    private FormatWeapons weapons;

    @Override
    public void start(Stage stage) {
        sim = new CockpitSim();
        sim.start(stage);
    }

    @BeforeEach
    public void setUp() throws Exception {
        WaitForAsyncUtils.waitForFxEvents();
        
        Platform.runLater(() -> {
            weapons = new FormatWeapons(sim.rightMHDD);
        });
        WaitForAsyncUtils.waitForFxEvents();
    }

    @Test
    public void testWeaponsDisplayExists() {
        assertNotNull(weapons, "Weapons display should exist");
    }

    @Test
    public void testWeaponsKeyPagesExist() {
        assertNotNull(weapons.weaponKeyPages, "Key pages should exist");
    }

    @Test
    public void testSelectGun() {
        boolean success = false;
        try {
            Field field = Key.class.getDeclaredField("eventHandlerReleased");
            field.setAccessible(true);
            javafx.event.EventHandler handler = (javafx.event.EventHandler) field.get(weapons.weaponKeyPages[0].keys[1]);
            handler.handle(null);
            success = true;
        } catch (Exception e) {
            success = false;
        }
        assertTrue(success, "Gun selection should work");
    }

    @Test
    public void testSelectMissile() {
        boolean success = false;
        try {
            Field field = Key.class.getDeclaredField("eventHandlerReleased");
            field.setAccessible(true);
            javafx.event.EventHandler handler = (javafx.event.EventHandler) field.get(weapons.weaponKeyPages[0].keys[2]);
            handler.handle(null);
            success = true;
        } catch (Exception e) {
            success = false;
        }
        assertTrue(success, "Missile selection should work");
    }

    @Test
    public void testSelectBomb() {
        boolean success = false;
        try {
            Field field = Key.class.getDeclaredField("eventHandlerReleased");
            field.setAccessible(true);
            javafx.event.EventHandler handler = (javafx.event.EventHandler) field.get(weapons.weaponKeyPages[0].keys[3]);
            handler.handle(null);
            success = true;
        } catch (Exception e) {
            success = false;
        }
        assertTrue(success, "Bomb selection should work");
    }
}