package ex.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import ex.components.Exam;
import ex.components.Exercise;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import lib.PowerShellProcess;

public class MExSettings_Controller extends Controller{
	//public Controllers_Manager manager;
	
	//Settings
	@FXML ComboBox<String> combo_voices;//done
	@FXML CheckBox checkbox_speakques;
	@FXML CheckBox checkbox_speakans;
	
	
	@FXML CheckBox checkbox_maxtime;
	@FXML TextField textfield_maxtime;
	
	@FXML CheckBox checkbox_autograde;
	
	@FXML CheckBox checkbox_maxlives;
	@FXML TextField textfield_maxlives;
	
	@FXML CheckBox checkbox_deduction;
	
	//Modes
	@FXML RadioButton radio_complete;
	@FXML RadioButton radio_turbo;
	@FXML RadioButton radio_endless;
	
	@FXML TextField textfield_number;
	
	@FXML CheckBox checkbox_completeshuffle;//done
	
	@FXML CheckBox checkbox_turboshuffle;
	@FXML CheckBox checkbox_turborandom;
	
	@FXML Button button_launch;
	
	public Exam exam;
	public int count;
	
	public PowerShellProcess process;
	public static String command1 = "Add-Type -AssemblyName System.Speech;";
	public static String command2 = "$somevoice = (New-Object System.Speech.Synthesis.SpeechSynthesizer);";
	public static String command3 = "$somevoice.GetInstalledVoices().VoiceInfo | Select-Object -Property Name;";
	public static String command4 = "$somevoice.SelectVoice('";//$somevoice.SelectVoice('Microsoft Zira Desktop');";//Hazel, Zira
	//public static String command5 = "$somevoice.Speak('Hello, Welcome to the speech synthesis world!');";
	//public static String command5a = "$somevoice.Speak(\"";
	//public static String command5b = "\");";
	public ArrayList<String> voices;
	public ObservableList<String> obsList;
	
	public void powershellStart() {
		try {
			process = new PowerShellProcess();
			
			voices = process.retrieveOutput();
			voices.add("None");
			obsList = FXCollections.observableArrayList();
			
			Thread outputCheck = new Thread() {
				@Override
				public void run() {
					int time = 0;
					int listsize = voices.size();
					while(true) {
						if(time >= 20000) 
							break;
						if(listsize != voices.size()) {
							Platform.runLater(()->{
								obsList.clear();
								for(String voice:voices) {
									if(voice.equals("") || voice.contains("Name") || voice.contains("----"))
										continue;
									obsList.add(voice.trim());
								}
							});
							listsize = voices.size();
						}
						try {
							Thread.sleep(500);
						}
						catch(Exception e) {e.printStackTrace();}
					}
				}
			};
			outputCheck.start();
			
			process.writeCommand(command1);//speech mode
			process.writeCommand(command2);//create voice object
			process.writeCommand(command3);//shows available voices (to fix).
			//process.writeCommand(command4);//selects voice
			
			//obsList.addAll(voices);
			combo_voices.setItems(obsList);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	public void powershellEnd() {
		process.endProcess();
	}
	
	public void start(Exam ex) {
		voices = new ArrayList<String>();
		powershellStart();
		
		exam = ex;
		ToggleGroup toggle = new ToggleGroup();
		radio_complete.setToggleGroup(toggle);
		radio_turbo.setToggleGroup(toggle);
		radio_endless.setToggleGroup(toggle);
		manager.stage.setScene(manager.scene_menu_examsettings);
	}
	
	public void actionLaunch() throws Exception{
		//Voice preparation.
		String selectedVoice;
		if(combo_voices.getSelectionModel().getSelectedItem() == null)
			selectedVoice = "";
		else
			selectedVoice = combo_voices.getSelectionModel().getSelectedItem().trim();
		
		if(selectedVoice.equals("") || selectedVoice.equals("None")) {
			process.endProcess();
			process = null;
		}
		else {
			process.writeCommand(command4 + selectedVoice + "');");
		}
		//Settings preparation
		exam.speechQ = checkbox_speakques.isSelected()? true:false;
		exam.speechA = checkbox_speakans.isSelected()? true:false;
		
		exam.boolMaxTime = checkbox_maxtime.isSelected()?true:false;
		exam.maxTime = exam.boolMaxTime?Integer.parseInt(textfield_maxtime.getText()) * 100:0;
		
		exam.boolAutoGrade = checkbox_autograde.isSelected()?true:false;
		
		exam.boolMaxLives = checkbox_maxlives.isSelected()?true:false;
		exam.maxLives = exam.boolMaxLives?Integer.parseInt(textfield_maxlives.getText()):0;
		
		exam.ptsDeduction = checkbox_deduction.isSelected()?true:false;
		
		//Exam mode preparation.
		if(radio_complete.isSelected()) {
			exam.code = 0;
			exam.completeDeck();
			if(checkbox_completeshuffle.isSelected())
				exam.shuffleDeck();
		}
		if(radio_turbo.isSelected()) {
			exam.code = 1;
			int num = Integer.parseInt(textfield_number.getText());
			exam.randomNDeck(num);
		}
		if(radio_endless.isSelected()) {
			exam.code = 2;
			exam.completeDeck();
			exam.shuffleDeck();
		}
		manager.examState(exam, process);
	}
	
	
	/*
	public void actionCancel() {
		count = 0;
		label_count.setText("Count: " + count);
		box_question.setText("");
		box_answer.setText("");
		manager.afterState();
	}
	*/
}
