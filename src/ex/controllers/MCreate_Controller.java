package ex.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import ex.components.Exam;
import ex.components.Exercise;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class MCreate_Controller extends Controller{
	//public Controllers_Manager manager;
	
	@FXML TextArea box_question;
	@FXML TextArea box_answer;
	@FXML TextField textfield_score;
	@FXML TextField textfield_diff;
	@FXML Button button_add;
	@FXML Button button_finish;
	@FXML Button button_quit;
	@FXML Button button_cancel;
	@FXML Label label_count;
	
	public Exam exam;
	public int count;
	
	public void start(String name) {
		exam = new Exam(name);
		count = 0;
		label_count.setText("Count: " + count);
	}
	
	public void actionAdd(ActionEvent e) {
		ArrayList<String> question = new ArrayList<String>();
		Collections.addAll(question, box_question.getText().split("\n"));
		
		ArrayList<String> answer = new ArrayList<String>();
		Collections.addAll(answer, box_answer.getText().split("\n"));
		
		Exercise ex = new Exercise(exam.questions.size()+1);
		
		ex.setQuestion(question);
		ex.setAnswer(answer);
		ex.setScore(Integer.parseInt(textfield_score.getText()));
		ex.setDiff(Integer.parseInt(textfield_diff.getText()));
		
		exam.addQuestion(ex);
		
		box_question.setText("");
		box_answer.setText("");
		
		count++;
		label_count.setText("Count: " + count);
	}
	
	public void actionFinish(ActionEvent e) {
		try {
			exam.printToText();
		}
		catch(IOException except) {
			System.out.println("ACTION FINISH FAILED");
		}
		count = 0;
		label_count.setText("Count: " + count);
		box_question.setText("");
		box_answer.setText("");
		manager.afterState();
	}
	
	public void actionCancel() {
		count = 0;
		label_count.setText("Count: " + count);
		box_question.setText("");
		box_answer.setText("");
		manager.afterState();
	}
}
