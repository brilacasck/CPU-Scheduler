/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package cpuscheduler;

import static com.sun.org.apache.xalan.internal.lib.ExsltDatetime.time;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
    private Button reloadFile;
    
    @FXML
    private TextArea input;
    
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
    
    
    @FXML
    private void handleRunButtonAction(ActionEvent event) {
        
        if(validate() == "OK"){
            status.setText("OK");
            status.setTextFill(Color.DARKGREEN);
            CPU cpu;
            String method = schMethod.getValue().toString();
            if(schMethod.getValue().equals("Round Robin")) method += ":" + quantum.getText();
            
            if(input.getText().startsWith("Random")){
                String[] line = input.getText().split("\n");
                String[] split = line[0].split("\\s+");
                cpu = new CPU(Integer.valueOf(split[1]), method);
            }else{
                cpu = new CPU(input.getText(), method);
            }
            cpu.Simulate();
            System.out.println(cpu.getReport());
        }else{
            status.setText(validate());
            status.setTextFill(Color.RED);
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
            double b = 0, d = 0, p = 0;
            try {
                BufferedReader input = new BufferedReader(new FileReader(file));
                while ((s = input.readLine()) != null) {
                    String[] split = s.split("\\s+");
                    b = Double.parseDouble(split[0]);
                    d = Double.parseDouble(split[1]);
                    p = Integer.parseInt(split[2]);
                    res += b + " " + d + " " + p + "\n";
                }
                this.input.setText(res);
            } catch (Exception e) {
                status.setText("Error: Bad Input File");
                status.setTextFill(Color.RED);
            }
        }
        
    }
    
    @FXML
    private void choiceBoxAction(ActionEvent event){
        
        if(schMethod.getValue().equals("Round Robin")){
            quantum.setDisable(false);
        }else{
            quantum.setDisable(true);
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
            try{
                for (String line : lines) {
                    String[] split = line.split("\\s+");
                    Double.parseDouble(split[0]);
                    Double.parseDouble(split[1]);
                    Integer.parseInt(split[2]);
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
        
        simSpeed.addEventFilter(KeyEvent.KEY_TYPED, numericValidation(2));
        cs.addEventFilter(KeyEvent.KEY_TYPED, numericValidation(5));
        quantum.addEventFilter(KeyEvent.KEY_TYPED, numericValidation(5));
        quantum.setDisable(true);
        
        schMethod.getItems().removeAll(schMethod.getItems());
        schMethod.getItems().addAll("FCFS", "PSJF", "SJF", "Primitive Priority", "Priority", "Round Robin", "Lottery");
        schMethod.getSelectionModel().select("FCFS");
        
        levelMethod.getItems().removeAll(levelMethod.getItems());
        levelMethod.getItems().addAll("Single Level", "Multi Level");
        levelMethod.getSelectionModel().select("Single Level");
        
        
    }
    
}
