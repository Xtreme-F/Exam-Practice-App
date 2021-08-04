package ex.controllers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

import ex.components.EditDialog;
import ex.components.Exam;
import ex.components.Exercise;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;

public class MEdit_Controller extends Controller{
	//public Controllers_Manager manager;
	@FXML ListView<Exercise> listview;
	
	@FXML Button button_up;
	@FXML Button button_down;
	@FXML Button button_all;
	
	@FXML Button button_score;
	@FXML Button button_diff;
	
	@FXML Button button_add;
	@FXML Button button_edit;
	@FXML Button button_del;
	
	@FXML Button button_ok;
	@FXML Button button_quit;
	@FXML Button button_cancel;
	
	public ObservableList<Exercise> obsList;
	
	public Exam exam;
	public int count;
	
	public void start(File file) {
		exam = new Exam(file);
		obsList = FXCollections.observableArrayList();
		obsList.addAll(exam.questions);
		
		listview.setItems(obsList);
		listview.getSelectionModel().selectionModeProperty().setValue(SelectionMode.MULTIPLE);
		manager.stage.setScene(manager.scene_menu_edit);
		
		listview.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal)->disablerEnabler());
		
		button_all.setText("Select All");
		count = 0;
		//label_count.setText("Count: " + count);
	}
	
	public void disablerEnabler() {
		if(listview.getSelectionModel().getSelectedItems().size()>1) {
			button_up.setVisible(false);
			button_down.setVisible(false);
			//button_add.setVisible(false);
			button_edit.setVisible(false);
		}
		else {
			button_up.setVisible(true);
			button_down.setVisible(true);
			//button_add.setVisible(false);
			button_edit.setVisible(true);
		}
	}
	
	public void actionAdd(ActionEvent e) {
		EditDialog dialog = new EditDialog(exam, null, true, manager);
		dialog.showAndWait();
		Exercise result = dialog.getResult();
		if(result==null) {
			return;
		}
		exam.questions.add(result);
		obsList.add(result);
		//obsList.add(exam.questions.get(exam.questions.size()-1));
		Exercise temp = new Exercise();
		obsList.add(temp);
		obsList.remove(temp);
		
	}
	
	public void actionEdit(ActionEvent e) {
		if(listview.getSelectionModel().getSelectedItem() == null) {
			Alert errorDialog = new Alert(AlertType.ERROR, "No exercise selected, please select an exercise.");
			errorDialog.showAndWait();
			return;
		}
		int i = listview.getSelectionModel().getSelectedIndex();
		Exercise selected = listview.getSelectionModel().getSelectedItem();
		EditDialog dialog = new EditDialog(exam, selected, false, manager);
		dialog.showAndWait();
		Exercise result = dialog.getResult();
		if(result==null)
			return;
		
		obsList.remove(selected);
		obsList.add(i, selected);
		Exercise temp = new Exercise();
		obsList.add(temp);
		obsList.remove(temp);
	}
	
	public void actionDel(ActionEvent e) {
		ObservableList<Exercise> allselected = listview.getSelectionModel().getSelectedItems();
		if(allselected.size() <= 0)
			return;
		else if(allselected.size() == 1) {
			int i = listview.getSelectionModel().getSelectedIndex();
			if(i<0)
				return;
			exam.questions.remove(i);
			obsList.remove(i);
		}
		else {
			exam.questions.removeAll(allselected);
			obsList.removeAll(allselected);
		}
	}
	
	public void actionScore(ActionEvent e) {
		ObservableList<Exercise> allselected = listview.getSelectionModel().getSelectedItems();
		if(allselected.size() <= 0)
			return;
		
		TextInputDialog dialog = new TextInputDialog();
		dialog.setTitle("Set the new score for the selected exercise(s)");
		dialog.setHeaderText("Score: ");

		dialog.getDialogPane().setMinWidth(750);
		
		Button okButton = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
		TextField inputField = dialog.getEditor();
		dialog.showAndWait();
		String result = dialog.getResult();
		int newscore = Integer.parseInt(result);
		
		for(Exercise ex: allselected) {
			ex.score = newscore;
		}
	}
	
	public void actionDiff(ActionEvent e) {
		ObservableList<Exercise> allselected = listview.getSelectionModel().getSelectedItems();
		if(allselected.size() <= 0)
			return;
		
		TextInputDialog dialog = new TextInputDialog();
		dialog.setTitle("Set the new difficulty for the selected exercise(s)");
		dialog.setHeaderText("Difficulty: ");

		dialog.getDialogPane().setMinWidth(750);
		
		Button okButton = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
		TextField inputField = dialog.getEditor();
		dialog.showAndWait();
		String result = dialog.getResult();
		int newdiff = Integer.parseInt(result);
		
		for(Exercise ex: allselected) {
			ex.difficulty = newdiff;
		}
	}
	
	public void actionUp(ActionEvent e) {
		System.out.println("UP");
		int i = listview.getSelectionModel().getSelectedIndex();
		if(i<=0)
			return;
		Exercise a = exam.questions.get(i);
		Exercise b = exam.questions.get(i-1);
		exam.questions.add(i-1, a); obsList.add(i-1, a);
		//exam.questions.add(i, b); obsList.add(i, b);
		exam.questions.remove(i+1); //exam.questions.remove(i+1);
		obsList.remove(i+1); //obsList.remove(i+1);
		listview.getSelectionModel().clearSelection();
		listview.getSelectionModel().select(a);
	}
	public void actionDown(ActionEvent e) {
		System.out.println("DOWN");
		int i = listview.getSelectionModel().getSelectedIndex();
		if(i<0 || i >= obsList.size()-1)
			return;
		Exercise a = exam.questions.get(i);
		Exercise b = exam.questions.get(i+1);
		exam.questions.add(i, b); obsList.add(i, b);
		//exam.questions.add(i+1, a); obsList.add(i+1, a);
		exam.questions.remove(i+2); //exam.questions.remove(i+2);
		obsList.remove(i+2); //obsList.remove(i+2);
	}
	
	public void actionAll(ActionEvent e) {
		listview.getSelectionModel().selectAll();
		//button_all.setText("Unselect All");
	}
	
	public void actionOK(ActionEvent e) {
		for(int i = 0; i < exam.questions.size(); i++) {
			exam.questions.get(i).number = i+1;
		}
		try {
			exam.printToText();
		}
		catch(IOException except) {
			System.out.println("ACTION FINISH FAILED");
			except.printStackTrace();
		}
		count = 0;
		//empty listview
		manager.afterState();
		//listview.getSelectionModel().get.selectAll();
		//listview.getSelectionModel().selectionModeProperty().setValue(SelectionMode.MULTIPLE);
	}
	
	public void actionCancel(ActionEvent e) {
		count = 0;
		//empty listview
		manager.afterState();
	}
}
