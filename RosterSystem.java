import javafx.application.Application;
import javafx.event.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.Alert.*;								
import javafx.scene.layout.*;
import javafx.scene.shape.*;
import javafx.scene.shape.Line;
import javafx.scene.text.*;
import javafx.stage.*;
import javafx.geometry.*;

import java.io.*;
import java.util.*;

/**
 * Name: Luke Doherty
 * Course/Section: ISTE-121.02
 * Roster Assignment
 */
 
public class RosterSystem extends Application implements EventHandler<ActionEvent> {
   // Main window items
   private Stage stage;
   private Scene scene;
   private VBox root = new VBox(8);
   
   // GUI components
   private TextArea taRoster = new TextArea();
   private TextField tfRosterCount = new TextField();
   private TextField tfSearchFor = new TextField();
   private Button btnSearch = new Button("Search");
   private TextField tfUniform = new TextField();
   private TextField tfName = new TextField();
   private TextField tfPos = new TextField();
   private TextField tfHeight = new TextField();
   private TextField tfWeight = new TextField();
   private TextField tfAge = new TextField();
   private TextField tfExp = new TextField();
   private TextField tfCollege = new TextField();
   private Button btnUpdate = new Button("Update");
   private Button btnDelete = new Button("Delete");
   private Button btnSaveExit = new Button("Save/Exit");
   
   // Data Structures
   private TreeMap<Integer, Roster> map = new TreeMap<Integer, Roster>(new SortData());
   
   // Constants
   public static final String ROSTER_DATA = "FBRosterBIN.dat";
   
   // Misc Attributes
   int counter = 0;
   int trackingConstant = 0;
   
   /** main */
   public static void main(String[] args) {
      launch(args);
   }
   
   /** constructor */
   public void start(Stage _stage) {
      stage = _stage;
      stage.setTitle("Roster System");
      
      // row1 - TextArea
      Font newFont = Font.font("MONOSPACED");
      taRoster.setFont(newFont);
      root.getChildren().add(taRoster);
      
      // row2 - roster count
      FlowPane fpRow2 = new FlowPane(8, 8);
      tfRosterCount.setPrefColumnCount(3);
      tfRosterCount.setEditable(false);
      fpRow2.getChildren().addAll(new Label("          Players: "), tfRosterCount);
      root.getChildren().add(fpRow2);

      // row3 - line between roster count and search
      Line lineRow3 = new Line(0, 0, 650, 0);
      root.getChildren().add(lineRow3);
      
      // row4 - search
      FlowPane fpRow4 = new FlowPane(8, 8);
      tfSearchFor.setPrefColumnCount(3);
      fpRow4.getChildren().addAll(new Label("     Uniform #: "), tfSearchFor, btnSearch);
      root.getChildren().add(fpRow4);
      
      // row5 - line between search and data
      Line lineRow5 = new Line(0, 0, 650, 0); 
      root.getChildren().add(lineRow5);
      
      // row6 - current record 1 of 3
      FlowPane fpRow6 = new FlowPane(8, 8);
      tfUniform.setPrefColumnCount(3);
      tfName.setPrefColumnCount(20);
      tfPos.setPrefColumnCount(4);
      fpRow6.getChildren().addAll(new Label("     Uniform #: "), tfUniform, 
         new Label("Name: "), tfName, new Label("Position: "), tfPos);
      root.getChildren().add(fpRow6);     

      // row7 - current record 2 of 3
      FlowPane fpRow7 = new FlowPane(8, 8);
      tfHeight.setPrefColumnCount(3);
      tfWeight.setPrefColumnCount(7);
      tfAge.setPrefColumnCount(3);
      fpRow7.getChildren().addAll(new Label("            Height: "), tfHeight,
         new Label("Weight: "), tfWeight, new Label("Age: "), tfAge);
      root.getChildren().add(fpRow7);
     
      // row8 - current record 3 of 3
      FlowPane fpRow8 = new FlowPane(8, 8);
      tfExp.setPrefColumnCount(3);
      tfCollege.setPrefColumnCount(20);
      fpRow8.getChildren().addAll(new Label("   Experience: "), tfExp,
         new Label("College: "), tfCollege);
      root.getChildren().add(fpRow8);
     
      // row9 - line between data and controls
      Line lineRow9 = new Line(0, 0, 650, 0); 
      root.getChildren().add(lineRow9);
      
      // row10
      FlowPane fpRow10 = new FlowPane(8, 8);
      fpRow10.setAlignment(Pos.CENTER);
      fpRow10.getChildren().addAll(btnUpdate, btnDelete, btnSaveExit);
      root.getChildren().add(fpRow10);

      // button handlers
      btnSearch.setOnAction(this);
      btnUpdate.setOnAction(this);
      btnDelete.setOnAction(this);
      btnSaveExit.setOnAction(this);

      scene = new Scene(root, 600, 420);
      stage.setX(100);
      stage.setY(100);
      stage.setScene(scene);
      stage.show(); 
   
      doProgram();
   }
   
   /** Read in the data file */
   private void doProgram() {
      // Read in the data file and display it
      readData();
      dispData();
   }
   
   /** readData - read in the data from the file */
   private void readData() {       
      Roster newRoster = null;   
      try {
         DataInputStream dis = new DataInputStream(new FileInputStream(new File(ROSTER_DATA)));
   
         while(true) {
            int i = 0;
            int uniformNumber = new Integer(dis.readInt());
            String playerName = dis.readUTF();
            String position = dis.readUTF();
            int heightInInches = new Integer(dis.readInt());
            double weightInPounds = new Double(dis.readDouble());
            int age = new Integer(dis.readInt());
            String experience = dis.readUTF();
            String college = dis.readUTF();
            
            newRoster = new Roster(uniformNumber, playerName, position, heightInInches, weightInPounds,
               age, experience, college);
            map.put(uniformNumber, newRoster);
         } // while
      }
      catch(EOFException eofe) {
         return;
      }
      catch(Exception e) {
         System.out.println("Exception: " + e);
      }
   }

   /** dispData 
   /** dispData - display all roster entries (ala toString()) in the TextArea */
   private void dispData() {
      counter = 0;
      // Iterates through the set
      taRoster.setText("Current Roster:\n");
      for(Integer uniformNumber : map.keySet()) {
         taRoster.appendText("     " + map.get(uniformNumber).toString() + "\n");
         counter++;
      }
      
      // Sets total player count, highlights the uniform # search box
      tfRosterCount.setText(""+counter);
      tfSearchFor.requestFocus();
   
   }
   
   /** SortData
   /** SortData - sorts by roster # */
   class SortData implements Comparator<Integer> {
      public int compare(Integer uniform1, Integer uniform2) {
         return uniform1 - uniform2;
      }
   }

   /** 
    * handle
    * button dispatcher
    */
   public void handle(ActionEvent ae) {
      // get the button that was clicked
      Button btn = (Button)ae.getSource();
      
      // switch on button label
      switch(btn.getText()) {
         case "Search":
            doSearch();
            break;
         case "Update":
            doUpdate();
            break;
         case "Delete":
            doDelete();
            break;
         case "Save/Exit":
            doSaveExit();
            break;
      }
   }
   
   /** doSearch */
   private void doSearch() {
      Integer player = Integer.parseInt(tfSearchFor.getText());
      String alert = "";
      trackingConstant = 1;
      Set<Integer> keys = map.keySet();
      
      // Sets text fields to uniform #s data
      if(keys.contains(player)) {
         tfUniform.setText(""+map.get(player).getUniformNumber());
         tfName.setText(map.get(player).getPlayerName());
         tfPos.setText(map.get(player).getPosition());
         tfHeight.setText(""+map.get(player).getHeightInInches());
         tfWeight.setText(""+map.get(player).getWeightInPounds());
         tfAge.setText(""+map.get(player).getAge());
         tfExp.setText(map.get(player).getExperience());
         tfCollege.setText(map.get(player).getCollege());      
      }
      
      // Input a uniform # that does not exist, alert
      else {
         alert = "Roster entry not found";       
         Alert theAlert = new Alert(AlertType.WARNING, alert);
         theAlert.setHeaderText("Roster Search");
         theAlert.showAndWait();
      }
      tfSearchFor.requestFocus();
   }
   
   /** doUpdate */
   private void doUpdate() {
      Integer player = Integer.parseInt(tfSearchFor.getText());
      String alert = "";
      Set<Integer> keys = map.keySet();
      
      // Updates the map based on the text fields
      if(keys.contains(player)&&trackingConstant!=0) {
         
         map.get(player).setUniformNumber(Integer.parseInt(tfUniform.getText()));
         map.get(player).setPlayerName(tfName.getText());
         map.get(player).setPosition(tfPos.getText());
         map.get(player).setHeightInInches(Integer.parseInt(tfHeight.getText()));
         map.get(player).setWeightInPounds(Double.parseDouble(tfWeight.getText()));
         map.get(player).setAge(Integer.parseInt(tfAge.getText()));
         map.get(player).setExperience(tfExp.getText());
         map.get(player).setCollege(tfCollege.getText());      
         
         dispData();
         trackingConstant = 0;
         
         alert = "Roster entry updated";
         Alert theAlert = new Alert(AlertType.INFORMATION, alert);
         theAlert.setHeaderText("Roster Update");
         theAlert.showAndWait();
      }
      
      // Input a uniform # that does not exist, alert
      else {
         alert = "Roster entry not found";       
         Alert theAlert = new Alert(AlertType.WARNING, alert);
         theAlert.setHeaderText("Roster Search");
         theAlert.showAndWait();
      }
      
   }
   
   /** doDelete */
   private void doDelete() {
      Integer player = Integer.parseInt(tfSearchFor.getText());
      String alert = "";
      Set<Integer> keys = map.keySet();
      
      // Deletes the uniform # player from the map
      if(keys.contains(player)&&trackingConstant!=0) {
         map.remove(player);
         
         tfSearchFor.setText("");
         tfUniform.setText("");
         tfName.setText("");
         tfPos.setText("");
         tfHeight.setText("");
         tfWeight.setText("");
         tfAge.setText("");
         tfExp.setText("");
         tfCollege.setText("");  
         
         dispData();
         trackingConstant = 0;
         
         alert = "Roster entry deleted";
         Alert theAlert = new Alert(AlertType.INFORMATION, alert);
         theAlert.setHeaderText("Roster Deletion");
         theAlert.showAndWait();
      }
      
      // Input a uniform # that does not exist, alert
      else {
         alert = "Roster entry not found";       
         Alert theAlert = new Alert(AlertType.WARNING, alert);
         theAlert.setHeaderText("Roster Search");
         theAlert.showAndWait();
      }
   }
   
   /** do SaveExit ... button and window close */
   private void doSaveExit() {
      
      // Writes to the file the changes -- I couldn't get this to work,
      // I think I have the right idea but I'm not sure why fileWriter does not work with the Roster map :(
      
 //     try {
 //        FileWriter fileWriter = new FileWriter("FBRosterBIN.dat", false);   
 //        for(Integer uniformNumber : map.keySet()) {
 //           fileWriter.write(map.get(uniformNumber));   
 //        }      
 //        fileWriter.flush();
 //        fileWriter.close();
 //     } 
 //     catch(IOException ioe) {
 //        Alert alert = new Alert(AlertType.ERROR, String.format("Error reading: %s\n",ioe));
 //        alert.showAndWait();
 //        System.exit(1);
 //     }
   
      // Alerts then closes
      String alert = "File written";
      Alert theAlert = new Alert(AlertType.INFORMATION, alert);
      theAlert.setHeaderText("File Save");
      theAlert.showAndWait();
      System.exit(0);
      
   }
}

/** 
 * The reason we specify a TreeMap compared to a HashMap is that the
 * TreeMap is used primarily for sorting, sorting by keys that are in ascending order.
 * HashMap does not maintain order while iterating.
 */