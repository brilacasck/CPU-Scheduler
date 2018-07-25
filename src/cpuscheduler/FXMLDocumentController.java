/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package cpuscheduler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 *
 * @author soheilchangizi
 */
public class FXMLDocumentController implements Initializable {
    
    @FXML
    private Label status;
    
    @FXML
    private Button run;
    
    @FXML
    private Button randomInput;
    
    @FXML
    private Button reloadFile;
    
    @FXML
    private Button addLevel;
    
    @FXML
    private Button removeLevel;
    
    @FXML
    private TextArea input;
    
    @FXML
    private TextArea levels;
    
    @FXML
    private ChoiceBox schMethod;
    
    @FXML
    private ChoiceBox levelMethod;
    
    @FXML
    private TextField simSpeed;
    
    @FXML
    private TextField cs;
    
    @FXML
    private TextField quantum;
    
    private static ArrayList<String> schLevels = new ArrayList<>();
    
    private static String prevInput = "";
    
    private static CPU cpu;
    
    private static double speed;
    
    @FXML
    private void handleRunButtonAction(ActionEvent event) {
        
        if(validate() == "OK"){
            status.setText("OK");
            status.setTextFill(Color.DARKGREEN);
            String method = schMethod.getValue().toString();
            if(schMethod.getValue().equals("Round Robin")) method += ":" + quantum.getText();
            
            if(input.getText().startsWith("Random")){
                status.setText("Error: Randomize First (press Random Input button)");
                status.setTextFill(Color.RED);
            }else{
                if(levelMethod.getValue().equals("Single Level")){
                    cpu = new CPU(input.getText(), method);
                }else if(levelMethod.getValue().equals("Preemptive Multi Level")){
                    cpu = new CPU(input.getText(), schLevels, "Preemptive ");
                }else{
                    cpu = new CPU(input.getText(), schLevels, "");
                }
                prevInput = input.getText();
                cpu.Simulate();
                speed = Double.parseDouble(simSpeed.getText());
                try {
                    Parent root = FXMLLoader.load(getClass().getResource("Simulation.fxml"));
                    Scene scene = new Scene(root);
                    Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                    stage.setScene(scene);
                    stage.show();
                } catch (IOException ex) {
                    Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }else{
            status.setText(validate());
            status.setTextFill(Color.RED);
        }
        
        
    }
    
    @FXML
    private void handleRandomInputButtonAction(ActionEvent event) {
        
        String method = schMethod.getValue().toString();
        if(schMethod.getValue().equals("Round Robin")) method += ":" + quantum.getText();
        
        if(input.getText().startsWith("Random")){
            status.setText("OK");
            status.setTextFill(Color.DARKGREEN);
            String[] line = input.getText().split("\n");
            String[] split = line[0].split("\\s+");
            if(levelMethod.getValue().equals("Single Level")){
                CPU.randProc(Integer.valueOf(split[1]), false, 1);
            }else{
                CPU.randProc(Integer.valueOf(split[1]), true, schLevels.size());
            }
            String res = "";
            for (String string : cpu.getRandomData()) {
                res += string + "\n";
            }
            input.setText(res);
        }else{
            if(levelMethod.getValue().equals("Single Level")){
                CPU.randProc(Integer.valueOf(5), false, 1);
            }else{
                CPU.randProc(Integer.valueOf(5), true, schLevels.size());
            }
            String res = "";
            for (String string : cpu.getRandomData()) {
                res += string + "\n";
            }
            input.setText(res);
        }
        
    }
    
    @FXML
    private void handleReloadFileButtonAction(ActionEvent event) {
        
        FileChooser fileChooser = new FileChooser();
        
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);
        
        Stage primaryStage = new Stage();
        File file = fileChooser.showOpenDialog(primaryStage);
        if(file != null){
            String s = "", res = "";
            double b = 0, d = 0;
            int p = 0, l = 0;
            try {
                BufferedReader input = new BufferedReader(new FileReader(file));
                while ((s = input.readLine()) != null) {
                    String[] split = s.split("\\s+");
                    b = Double.parseDouble(split[0]);
                    d = Double.parseDouble(split[1]);
                    p = Integer.parseInt(split[2]);
                    l = Integer.parseInt(split[3]);
                    res += b + " " + d + " " + p + " " + l + "\n";
                }
                this.input.setText(res);
            } catch (Exception e) {
                status.setText("Error: Bad Input File");
                status.setTextFill(Color.RED);
            }
        }
        
    }
    
    @FXML
    private void handleAddLevelButtonAction(ActionEvent event) {
        
        String method = schMethod.getValue().toString();
        if(schMethod.getValue().equals("Round Robin")) method += ":" + quantum.getText();
        schLevels.add(method);
        levels.setText(levels.getText() + "Level " + schLevels.size() + " : " + method + "\n");
        
    }
    
    @FXML
    private void handleRemoveLevelButtonAction(ActionEvent event) {
        
        if(schLevels.size() > 0){
            schLevels.remove(schLevels.size()-1);
            String res = "";
            int i = 1;
            for (String schLevel : schLevels) {
                res += "Level " + i + " : " + schLevel + "\n";
                i++;
            }
            levels.setText(res);
        }
        
    }
    
    @FXML
    private void choiceBoxAction(ActionEvent event){
        
        if(schMethod.getValue().equals("Round Robin")){
            quantum.setDisable(false);
        }else{
            quantum.setDisable(true);
        }
        
        try{
            if(levelMethod.getValue().equals("Multi Level")
                    || levelMethod.getValue().equals("Preemptive Multi Level")){
                levels.setVisible(true);
                input.setPrefHeight(258);
                removeLevel.setVisible(true);
                addLevel.setVisible(true);
                run.setTranslateY(-5);
                reloadFile.setTranslateY(-5);
                randomInput.setTranslateY(-5);
            }else{
                levels.setVisible(false);
                input.setPrefHeight(650);
                removeLevel.setVisible(false);
                addLevel.setVisible(false);
                run.setTranslateY(5);
                reloadFile.setTranslateY(5);
                randomInput.setTranslateY(5);
            }
        }catch(Exception e){
            
        }
        
    }
    
    public EventHandler<KeyEvent> numericValidation(final Integer max_Lengh) {
        return new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent e) {
                TextField txt_TextField = (TextField) e.getSource();
                if (txt_TextField.getText().length() >= max_Lengh) {
                    e.consume();
                }
                if(e.getCharacter().matches("[0-9.]")){
                    if(txt_TextField.getText().contains(".") && e.getCharacter().matches("[.]")){
                        e.consume();
                    }else if(txt_TextField.getText().length() == 0 && e.getCharacter().matches("[.]")){
                        e.consume();
                    }
                }else{
                    e.consume();
                }
            }
        };
    }
    
    public String validate(){
        
        String inputCheck = input.getText();
        String lines[] = inputCheck.split("\n");
        
        if(lines.length == 0){
            return "Error: No Input";
        }else if(lines[0].startsWith("Random")){
            String split[] = lines[0].split("\\s+");
            try{
                Integer.valueOf(split[1]);
            }catch(Exception e){
                return "Error: Bad Input for Random";
            }
        }else{
            int level = 0;
            try{
                for (String line : lines) {
                    String[] split = line.split("\\s+");
                    Double.parseDouble(split[0]);
                    Double.parseDouble(split[1]);
                    Integer.parseInt(split[2]);
                    level = Integer.parseInt(split[3]);
                    if(level-1 > schLevels.size()){
                        return "Error: Bad Level Input";
                    }
                }
            }catch(Exception e){
                return "Error: Bad Input";
            }
        }
        
        if(Double.parseDouble(quantum.getText()) < 0.2){
            return "Error: minimum value for quantum is 0.2";
        }
        if(Double.parseDouble(cs.getText()) < 0.4){
            return "Error: minimum value for quantum is 0.4";
        }
        
        return "OK";
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        input.setText(prevInput);
        String res = "";
        int i = 1;
        for (String schLevel : schLevels) {
            res += "Level " + i + " : " + schLevel + "\n";
            i++;
        }
        levels.setText(res);
        
        simSpeed.addEventFilter(KeyEvent.KEY_TYPED, numericValidation(2));
        cs.addEventFilter(KeyEvent.KEY_TYPED, numericValidation(5));
        quantum.addEventFilter(KeyEvent.KEY_TYPED, numericValidation(5));
        quantum.setDisable(true);
        
        schMethod.getItems().removeAll(schMethod.getItems());
        schMethod.getItems().addAll("FCFS", "PSJF", "SJF", "Preemptive Priority", "Priority", "Round Robin", "Lottery");
        schMethod.getSelectionModel().select("FCFS");
        
        levelMethod.getItems().removeAll(levelMethod.getItems());
        levelMethod.getItems().addAll("Single Level", "Multi Level", "Preemptive Multi Level");
        levelMethod.getSelectionModel().select("Single Level");
        
        input.setPrefHeight(650);
        levels.setEditable(false);
        levels.setVisible(false);
        
        removeLevel.setVisible(false);
        addLevel.setVisible(false);
        run.setTranslateY(20);
        reloadFile.setTranslateY(20);
        randomInput.setTranslateY(20);
        
    }
    
    public static CPU getCpu() {
        return cpu;
    }
    
    public static double getSpeed(){
        return speed;
    }
    
    
}
