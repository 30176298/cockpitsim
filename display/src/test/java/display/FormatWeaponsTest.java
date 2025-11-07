package test.java.display;

import display.FormatWeapons;
import display.KeyPage;
import display.CockpitSim;
import display.CNST;
import display.MHDD;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;
import static org.testfx.assertions.api.Assertions.assertThat;
import javafx.geometry.Point2D;
import org.testfx.util.WaitForAsyncUtils;

public class FormatWeaponsTest extends ApplicationTest {

    private CockpitSim sim;

    @Override
    public void start(Stage stage) {
        sim = new CockpitSim();
        sim.start(stage); // Launch full cockpit UI
    }

    @Test
    public void testGunButtonUpdatesText() throws Exception {
        Thread.sleep(1000); // Wait to see FORM MENU
        clickOn(new Point2D(700, 950)); // FORM MENU

        Thread.sleep(1000); // Wait to see WEAP
        clickOn(new Point2D(950, 950)); // WEAP

        Thread.sleep(1000); // Wait to see GUN
        clickOn(new Point2D(800, 950)); // GUN

        Thread.sleep(1000); // Observe weaponText
        FormatWeapons fw = (FormatWeapons) sim.rightMHDD.getFormat();
        assertThat(fw.weaponText.getText()).contains("Gun");
    }

    @Test
    public void testMissileButtonUpdatesText() throws Exception {
        Thread.sleep(1000); // Wait to see FORM MENU
        clickOn(new Point2D(700, 950)); // FORM MENU

        Thread.sleep(1000); // Wait to see WEAP
        clickOn(new Point2D(950, 950)); // WEAP

        Thread.sleep(1000); // Wait to see MISSILE
        clickOn(new Point2D(900, 950)); // MISSILE

        Thread.sleep(1000); // Observe weaponText
        FormatWeapons fw = (FormatWeapons) sim.rightMHDD.getFormat();
        assertThat(fw.weaponText.getText()).contains("Missile");
    }

    @Test
    public void testBombButtonUpdatesText() throws Exception {
        Thread.sleep(1000); // Wait to see FORM MENU
        clickOn(new Point2D(700, 950)); // FORM MENU

        Thread.sleep(1000); // Wait to see WEAP
        clickOn(new Point2D(950, 950)); // WEAP

        Thread.sleep(1000); // Wait to see BOMB
        clickOn(new Point2D(950, 950)); // BOMB

        Thread.sleep(1000); // Observe weaponText
        FormatWeapons fw = (FormatWeapons) sim.rightMHDD.getFormat();
        assertThat(fw.weaponText.getText()).contains("Bomb");
    }

    @Test
    public void testWeaponSwitching() throws Exception {
        Thread.sleep(1000); // Wait to see FORM MENU
        clickOn(new Point2D(700, 950)); // FORM MENU

        Thread.sleep(1000); // Wait to see WEAP
        clickOn(new Point2D(950, 950)); // WEAP

        Thread.sleep(1000); // Wait to see GUN
        clickOn(new Point2D(800, 950)); // GUN
        FormatWeapons fw = (FormatWeapons) sim.rightMHDD.getFormat();
        assertThat(fw.weaponText.getText()).contains("Gun");

        Thread.sleep(500);
        clickOn(new Point2D(900, 950)); // MISSILE
        assertThat(fw.weaponText.getText()).contains("Missile");

        Thread.sleep(500);
        clickOn(new Point2D(950, 950)); // BOMB
        assertThat(fw.weaponText.getText()).contains("Bomb");
    }

    @Test
    public void testGunReactionTimeUnder1s() throws Exception {
        clickOn(new Point2D(700, 950)); // FORM MENU
        clickOn(new Point2D(950, 950)); // WEAP
        Thread.sleep(500); // Let WEAP page load

        FormatWeapons fw = (FormatWeapons) sim.rightMHDD.getFormat();

        long start = System.currentTimeMillis();
        clickOn(new Point2D(800, 950)); // GUN
        
        WaitForAsyncUtils.waitForFxEvents();

        long end = System.currentTimeMillis();
        long duration = end - start;

        System.out.println("Reaction time: " + duration + " ms");

        assertThat(duration).isLessThan(1000);
        assertThat(fw.weaponText.getText()).contains("Gun");
    }

    @Test
    public void testReadabilityOfText() throws Exception {
        Thread.sleep(1000); // Wait to see FORM MENU
        clickOn(new Point2D(700, 950)); // FORM MENU

        Thread.sleep(1000); // Wait to see WEAP
        clickOn(new Point2D(950, 950)); // WEAP

        Thread.sleep(1000); // Wait to see MISSILE
        clickOn(new Point2D(900, 950)); // MISSILE

        Thread.sleep(1000); // Observe weaponText
        FormatWeapons fw = (FormatWeapons) sim.rightMHDD.getFormat(); 
        Text weaponText = fw.weaponText;
   
        assertThat(weaponText.getFill()).isEqualTo(Color.WHITE);
    }
}
