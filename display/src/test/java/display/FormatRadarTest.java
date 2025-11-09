package display;

import display.FormatRadar;
import display.CockpitSim;
import javafx.stage.Stage;
import javafx.geometry.Point3D;
import javafx.scene.shape.Circle;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.util.WaitForAsyncUtils;
import javafx.application.Platform;
import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class FormatRadarTest extends ApplicationTest {

    private CockpitSim sim;
    private FormatRadar radar;

    @Override
    public void start(Stage stage) {
        sim = new CockpitSim();
        sim.start(stage);
    }

    @BeforeEach
    public void setUp() throws Exception {
        WaitForAsyncUtils.waitForFxEvents();
        
        Platform.runLater(() -> {
            radar = new FormatRadar(sim.rightMHDD);
        });
        WaitForAsyncUtils.waitForFxEvents();
    }

    @AfterEach
    public void tearDown() {
        WaitForAsyncUtils.waitForFxEvents();
    }
    
    @Test //FR_010: Radar display rendering
    public void testRadarDisplayRendering() {
        assertNotNull(radar);
        assertNotNull(radar.getCircleCentre());
        assertEquals(2500.0, radar.getRadarRange(), 0.01);
    }

    @Test // FR_011: Radar beam rotation
    public void testRadarBeamRotation() throws Exception {
        double initialAngle = radar.getCurrentAngle();
        
        Thread.sleep(100);
        WaitForAsyncUtils.waitForFxEvents();
        
        double newAngle = radar.getCurrentAngle();
        assertNotEquals(initialAngle, newAngle);
    }

    @Test //FR_012: Bogey rendering system exists
    public void testBogeyRendering() throws Exception {
        Thread.sleep(1000);
        WaitForAsyncUtils.waitForFxEvents();
        
        ArrayList<Circle> blips = radar.getActiveBlips();
        assertNotNull(blips);
    }

    @Test //FR_014: Relative positional updates
    public void testRelativePositionUpdates() throws Exception {
        Thread.sleep(500);
        WaitForAsyncUtils.waitForFxEvents();
        
        assertNotNull(radar.getCurrentAngle());
    }

    @Test //FR_015: Bogey fade effect
    public void testBogeyFadeEffect() throws Exception {
        Thread.sleep(1000);
        WaitForAsyncUtils.waitForFxEvents();
        
        int initialCount = countActiveBlips();
        
        Thread.sleep(1500);
        WaitForAsyncUtils.waitForFxEvents();
        
        int finalCount = countActiveBlips();
        
        assertTrue(finalCount <= initialCount);
    }

    @Test //FR_016: Range limit
    public void testBogeyInRange() {
        Point3D centre = radar.getCircleCentre();
        Point3D closeTarget = new Point3D(centre.getX() + 50, centre.getY() + 50, 0);
        
        assertTrue(radar.isTargetInRange(closeTarget));
    }

    @Test //FR_016: Range limit
    public void testBogeyOutOfRange() {
        Point3D centre = radar.getCircleCentre();
        Point3D farTarget = new Point3D(centre.getX() + 200, centre.getY() + 200, 0);
        
        assertFalse(radar.isTargetInRange(farTarget));
    }

    @Test //NFR_009: Radar beam completes rotation in 1.33 seconds
    public void testRadarBeamSpeed() throws Exception {
        double startAngle = radar.getCurrentAngle();
        long startTime = System.currentTimeMillis();
        
        Thread.sleep(500);
        WaitForAsyncUtils.waitForFxEvents();
        
        double endAngle = radar.getCurrentAngle();
        long endTime = System.currentTimeMillis();
        
        double angleDelta = endAngle - startAngle;
        if (angleDelta < 0) angleDelta += Math.PI * 2;
        
        double timeDelta = (endTime - startTime) / 1000.0;
        double rotationSpeed = angleDelta / timeDelta;
        
        double expectedSpeed = Math.toRadians(270);
        double tolerance = Math.toRadians(30);
        
        assertTrue(Math.abs(rotationSpeed - expectedSpeed) < tolerance);
    }

    @Test
    public void testZoomIn() throws Exception {
        double initialRange = radar.getRadarRange();
        
        Platform.runLater(() -> {
            triggerZoomIn();
        });
        WaitForAsyncUtils.waitForFxEvents();
        
        double newRange = radar.getRadarRange();
        assertEquals(initialRange - 500, newRange, 0.01);
    }

    @Test
    public void testZoomOut() throws Exception {
        double initialRange = radar.getRadarRange();
        
        Platform.runLater(() -> {
            triggerZoomOut();
        });
        WaitForAsyncUtils.waitForFxEvents();
        
        double newRange = radar.getRadarRange();
        assertEquals(initialRange + 500, newRange, 0.01);
    }

    @Test
    public void testMinimumRange() throws Exception {
        for (int i = 0; i < 10; i++) {
            Platform.runLater(() -> triggerZoomIn());
            WaitForAsyncUtils.waitForFxEvents();
        }
        
        assertEquals(1500, radar.getRadarRange(), 0.01);
    }

    @Test
    public void testMaximumRange() throws Exception {
        for (int i = 0; i < 10; i++) {
            Platform.runLater(() -> triggerZoomOut());
            WaitForAsyncUtils.waitForFxEvents();
        }
        
        assertEquals(6000, radar.getRadarRange(), 0.01);
    }

    private void triggerZoomIn() {
        try {
            Field field = Key.class.getDeclaredField("eventHandlerReleased");
            field.setAccessible(true);
            javafx.event.EventHandler handler = (javafx.event.EventHandler) field.get(radar.keyPages[0].keys[1]);
            handler.handle(null);
        } catch (Exception e) {
            fail("Zoom in fail");
        }
    }
    
    private void triggerZoomOut() {
        try {
            Field field = Key.class.getDeclaredField("eventHandlerReleased");
            field.setAccessible(true);
            javafx.event.EventHandler handler = (javafx.event.EventHandler) field.get(radar.keyPages[0].keys[3]);
            handler.handle(null);
        } catch (Exception e) {
            fail("Zoom out fail");
        }
    }
    
    private int countActiveBlips() {
        int count = 0;
        for (Circle blip : radar.getActiveBlips()) {
            if (blip != null) count++;
        }
        return count;
    }
}