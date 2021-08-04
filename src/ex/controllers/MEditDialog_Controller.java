package ex.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import ex.components.EditDialog;
import ex.components.Exam;
import ex.components.Exercise;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class MEditDialog_Controller extends Controller{
	//public Controllers_Manager manager;
	public EditDialog dialog;
	
	@FXML Label title;
	@FXML TextArea box_question;
	@FXML TextArea box_answer;
	@FXML TextField textfield_score;
	@FXML TextField textfield_diff;
	@FXML Button button_set;
	@FXML Button button_quit;
	@FXML Button button_cancel;
	
	public Exam exam;
	public Exercise exercise;
	public boolean booladd;
	public int count;
	
	public void start() {
		if(!booladd) {
			String str1 = "";
			for(String s:exercise.question) 
				str1 += s + "\n";
			box_question.setText(str1);
			
			String str2 = "";
			for(String s:exercise.answer)
				str2 += s + "\n";
			box_answer.setText(str2);
			
			textfield_score.setText(""+exercise.score);
			textfield_diff.setText(""+exercise.difficulty);
			
			title.setText("Edit Question");
		}
		else {
			box_question.setText("");
			box_answer.setText("");
			title.setText("Add Question");
		}
	}
	
	public void actionSet(ActionEvent e) {
		if(booladd) {
			exercise = new Exercise();
			//exam.addQuestion(exercise);
		}
		else {
			exercise.question = new ArrayList<String>();
			exercise.answer= new ArrayList<String>();
		}
		
		Collections.addAll(exercise.question, box_question.getText().split("\n"));
		Collections.addAll(exercise.answer, box_answer.getText().split("\n"));
		
		exercise.setScore(Integer.parseInt(textfield_score.getText()));
		exercise.setDiff(Integer.parseInt(textfield_diff.getText()));
		
		box_question.setText("");
		box_answer.setText("");
		
		count++;
		dialog.setResult(exercise);
		//button_set.getScene().getWindow().hide();
	}
	
	public void actionCancel() {
		count = 0;
		box_question.setText("");
		box_answer.setText("");
		dialog.setResult(null);
		button_set.getScene().getWindow().hide();
		
		//manager.afterState();
	}
}
