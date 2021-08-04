package ex.controllers;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

import ex.components.ConclusionStats;
import ex.components.Exam;
import ex.components.Exercise;
import ex.components.TimeLabel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class EConclusion_Controller extends Controller{
	//public Controllers_Manager manager;
	
	@FXML Button button_ok;
	@FXML Button button_check;
	@FXML Button button_good;
	@FXML Button button_bad;
	@FXML Button button_quit;
	@FXML Button button_quitExam;
	
	@FXML TextArea box_question;
	@FXML TextArea box_answer;
	@FXML TextArea box_correct;
	
	@FXML Label label_score;
	@FXML Label label_correct;
	@FXML Label label_wrong;
	
	@FXML Label label_time;
	@FXML Label label_avg;
	@FXML Label label_longest;
	@FXML Label label_shortest;
	
	public Exam exam;
	public ArrayList<Exercise> deck;
	public ConclusionStats stats;
	
	public String questionAllStrings(int num, int choice) {//choice 0: Questions, choice 1: Answers
		String str = "";
		ArrayList<String> set;
		if(choice == 0) {
			set = deck.get(num).question;
		}
		else if(choice == 1){
			set = deck.get(num).answer;
		}
		else {
			set = new ArrayList<String>();
			set.addAll(deck.get(num).question);
			set.addAll(deck.get(num).answer);
		}
		
		for(int i = 0; i < set.size(); i++) {
			str += set.get(i);
			str += "\n";
		}
		return str;
	}
	
	public void start(Exam ex, ConclusionStats st) {
		manager.stage.setScene(manager.scene_examconclusion);
		exam = ex;
		deck = exam.shuffle;
		stats = st;
		stats.calculateDetails();
		
		char degree = 176;
		
		//STATS GOODS % ON SCORE FOR NOW.
		label_score.setText("" + stats.score + " (" + (stats.goods * 100 / deck.size()) + "%)");
		label_correct.setText("" + stats.goods + " (" + (stats.goods * 100 / deck.size()) + "%)");
		label_wrong.setText("" + stats.bads + " (" + (stats.bads * 100 / deck.size()) + "%)");
		
		label_time.setText(TimeLabel.timeToString(stats.time));
		//label_time.setText("" + stats.time/360000 + "" + degree + "" + stats.time% 360000 / 6000 % 100 + "\'" + stats.time % 360000 % 6000 / 100+ "\'\'." + stats.time % 360000 % 6000 % 100);
		label_avg.setText("" + stats.avgtime*10);
		label_longest.setText(TimeLabel.timeToString(stats.longest));
		label_shortest.setText(TimeLabel.timeToString(stats.shortest));
	}
	
	public void actionOK(ActionEvent e) {
		manager.afterState();
	}
	
	
}
