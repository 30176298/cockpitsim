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
import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.geometry.Pos;
import javafx.animation.AnimationTimer;

import java.io.*;
import java.nio.charset.*;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.nio.file.Files;
import java.io.FileWriter;
import java.util.List;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.time.LocalDateTime;

import java.lang.reflect.InvocationTargetException;
import org.codehaus.commons.compiler.CompileException;
import org.codehaus.janino.ScriptEvaluator;

import display.CNST;
import display.Key;

public class TestSystem extends DisplayObject{
  
  public CockpitSim parent;
  private String filePath;
  private Boolean responseDemand = false;
  private Boolean testingBegun = false;
  private int lastI = 0;  
  private int passCount = 0;  
  protected int failCount = 0;  
  
  private List<String> testInstructions = new ArrayList<String>();
  private List<Text> textObjects = new ArrayList<Text>();
  private FileWriter logWriter;
  
  public TestSystem(CockpitSim parent) {
    this.parent = parent;
    home = new Point2D(0, 0);
    
    setUpWindow();
    
    //Commit new nodes to scene tree
    parent.testGroupChildren.add(sceneGroup);
    
    startUpdater();
  }
  
  private void setUpWindow() {
    //Background
    Rectangle fileBackground = new Rectangle(0, 0, 768, 100);
    fileBackground.setFill(Color.DIMGREY);
    groupChildren.add(fileBackground);
    
    Rectangle buttonBackground = new Rectangle(0, 668, 768, 100);
    buttonBackground.setFill(Color.DIMGREY);
    groupChildren.add(buttonBackground);
    
    //File Chooser
    FileChooser testChooser = new FileChooser();
    //File defaultLocation = new File("/");
    //testChooser.setInitialDirectory(defaultLocation);

    Label label = new Label("No Test file selected");
    label.setLayoutX(150);
    label.setLayoutY(40);

    Button button = new Button("Select Test Script");
    button.setLayoutX(20);
    button.setLayoutY(40);
    
    //Button event
    EventHandler<ActionEvent> chooseEvent = new EventHandler<ActionEvent>() {
      public void handle(ActionEvent e) {
        File file = testChooser.showOpenDialog(parent.testStage);
        if (file != null) {
          groupChildren.clear();
          setUpWindow();
          filePath = file.getAbsolutePath();
          label.setText(filePath + "  selected");
          //Begin Test procedure
          lastI = 0;
          testingBegun = true;
          
          //Load test file
          testInstructions = readFile(filePath, StandardCharsets.UTF_8);
    
          //List of javafx text objects
          textObjects = new ArrayList<Text>();
        }
      }
    };
    button.setOnAction(chooseEvent);
  
    groupChildren.add(label);
    groupChildren.add(button);

    //Pass/Fail Buttons
    Rectangle passButton = new Rectangle(20, 688, 60, 60);
    passButton.setFill(Color.GREEN);
    groupChildren.add(passButton);
    Rectangle failButton = new Rectangle(120, 688, 60, 60);
    failButton.setFill(Color.RED);
    groupChildren.add(failButton);
    
    //Create logic for buttons
    EventHandler passButtonPressed = new EventHandler<MouseEvent>() {
      public void handle(MouseEvent event) {
        if (responseDemand) {
          try {
            logWriter.write(" \t-- PASS --\n\n");
            passCount++;
          }
          catch (IOException e) {
            System.out.println(e.getMessage());
          }
          responseDemand = false;
        }
      }
    };
    passButton.addEventFilter(MouseEvent.MOUSE_RELEASED, passButtonPressed);
    EventHandler failButtonPressed = new EventHandler<MouseEvent>() {
      public void handle(MouseEvent event) {
        if (responseDemand) {
          try {
            logWriter.write(" \t-- FAIL --\n\n");
            failCount++;
          }
          catch (IOException e) {
            System.out.println(e.getMessage());
          }
          responseDemand = false;
        }
      }
    };
    failButton.addEventFilter(MouseEvent.MOUSE_RELEASED, failButtonPressed);
  }
  
  private List<String> readFile(String path, Charset encoding) {
    try {
      return Files.readAllLines(Paths.get(path), encoding);
    }
    catch (IOException e) {
      e.printStackTrace();
      List<String> failed = new ArrayList<String>();
      failed.add(":(");   //Very important code
      return failed;
    }
  }
  
  private void startUpdater() {
    AnimationTimer updater = new AnimationTimer() {
      public void handle(long now) {
        if(testingBegun && !responseDemand) runTest();
      }
    };
    updater.start();
  }
  
  private void runTest() {    
    for (int i = lastI; i < testInstructions.size(); i++) {
      if (i == 0) {   //Start of test script
        String[] fPathTok = filePath.split(Pattern.quote(File.separator));
        String tFileName = fPathTok[fPathTok.length - 1];
        String fileName = tFileName.substring(0, tFileName.length() - 5 ) + "_" + LocalDateTime.now() + ".log";
        fileName = fileName.replace(":", "-");
        createNewTestFile(fileName);
        try {
          logWriter = new FileWriter(fileName);
          logWriter.write("----- BEGIN TEST -----\n\n");
        }
        catch (IOException e) {
          System.out.println(e.getMessage());
        }
        passCount = 0;
        failCount = 0;
      }
      if(testInstructions.get(i).length() > 0) {    //Reject blank lines
        switch(testInstructions.get(i).substring(0, 1)) {
          case "P":   //PRINT
            //Move up previous text
            for(int j = 0; j < textObjects.size(); j++) {
              textObjects.get(j).setY(textObjects.get(j).getY() - 30);
            }
            Text temp = new Text(20, 648, testInstructions.get(i).substring(1));  //Discard first character
            temp.setFont(new Font(15));
            temp.setFill(Color.BLACK);
            textObjects.add(temp);
            groupChildren.add(temp);
            //Output to log file
            try {
              logWriter.write(testInstructions.get(i).substring(1) + "\n");
            }
            catch (IOException e) {
              System.out.println(e.getMessage());
            }
            if(testInstructions.get(i).substring(1, 2).equals("R")) {
              responseDemand = true;  //RESPONSE
              lastI = i + 1;
              return;
            }
            break;
        
          case "E":   //EXECUTE
            try{
              ScriptEvaluator se = new ScriptEvaluator();
              se.setParameters(new String[] { "host" }, new Class[] { TestSystem.class });  //Set script to expect 1 parameter of type TestSystem
              se.cook(testInstructions.get(i).substring(1));
              se.evaluate(new Object[] {this});     //Provide reference to this within script
            }
            catch (org.codehaus.commons.compiler.CompileException e) {
              System.out.println(e.getMessage());
            }
            catch (java.lang.reflect.InvocationTargetException e) {
              System.out.println(e.getMessage());
            }
            break;
          default:
        }
      }
      lastI = i + 1;
      if (i == (testInstructions.size() - 1)) { //End of test script
        try {
          //User feedback
          //Move up previous text
          for(int j = 0; j < textObjects.size(); j++) {
            textObjects.get(j).setY(textObjects.get(j).getY() - 30);
          }
          Text temp = new Text(20, 648, "");
          
          //Output to log file
          logWriter.write("----- TEST  COMPLETE -----\n");
          logWriter.write("----- PASS COUNT : " + passCount + " -----\n");
          logWriter.write("----- FAIL COUNT : " + failCount + " -----\n");
          if(failCount > 0) {
            logWriter.write("---!!  TEST  FAILED  !!---\n");
            temp.setFont(new Font(20));
            temp.setFill(Color.RED);
            temp.setText("Test FAILED :(");
          }
          else {
            logWriter.write("----- TEST PASSED :) -----\n");
            temp.setFont(new Font(20));
            temp.setFill(Color.GREEN);
            temp.setText("Test PASSED :)");
          }
          textObjects.add(temp);
          groupChildren.add(temp);
          logWriter.close(); 
        }
        catch (IOException e) {
          System.out.println(e.getMessage());
        }
      }
    }
  }
  
  private void createNewTestFile(String fileName) {
    try {
      File newFile = new File(fileName);
      if (newFile.createNewFile()) {          
        System.out.println("New log file created: " + newFile.getName());
      } else {
        System.out.println("Log file already exists.");
      }
    } catch (IOException e) {
      System.out.println(e.getMessage()); 
    }
  }
}