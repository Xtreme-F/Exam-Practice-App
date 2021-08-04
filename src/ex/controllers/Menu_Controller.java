package ex.controllers;

import java.io.File;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class Menu_Controller extends Controller{
	//public Controllers_Manager manager;
	
	@FXML Button button_create;
	@FXML Button button_selection;
	
	public void start() {
		
	}
	
	public void actionCreate(ActionEvent e) {
		System.out.println("ACTION CREATE");
		manager.createState();
	}
	
	public void actionEdit() {
		System.out.println("ACTION EDIT");
		FileChooser chooser = new FileChooser();
		chooser.setTitle("Choose an Exam");
		chooser.setInitialDirectory(new File("res/"));
		chooser.getExtensionFilters().add(new ExtensionFilter("Exams", "*.exam"));
		File result = chooser.showOpenDialog(manager.stage);
		if(result==null) {
			System.out.println("CANCELED ACTION");
			return;
		}
		manager.editState(result);
	}
	
	public void actionSelection(ActionEvent e) {
		System.out.println("ACTION SELECT");
		FileChooser chooser = new FileChooser();
		chooser.setTitle("Choose an Exam");
		chooser.setInitialDirectory(new File("res/"));
		chooser.getExtensionFilters().add(new ExtensionFilter("Exams", "*.exam"));
		File result = chooser.showOpenDialog(manager.stage);
		if(result==null) {
			System.out.println("CANCELED ACTION");
			return;
		}
		manager.examSettingsState(result);
	}
}
