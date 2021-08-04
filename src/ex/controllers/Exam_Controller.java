package ex.controllers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Timer;

import ex.components.ConclusionStats;
import ex.components.Exam;
import ex.components.Exercise;
import ex.components.TimeLabel;
import javafx.beans.property.DoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import lib.PowerShellProcess;

import java.util.Locale;
import java.util.Scanner;

import javax.speech.Central;
import javax.speech.synthesis.Synthesizer;
import javax.speech.synthesis.SynthesizerModeDesc;

//import com.sun.speech.freetts.Voice;
//import com.sun.speech.freetts.VoiceManager;

public class Exam_Controller extends Controller{
	//public Controllers_Manager manager;
	PowerShellProcess process;
	public static String command = "Add-Type -AssemblyName System.Speech; (New-Object System.Speech.Synthesis.SpeechSynthesizer).Speak('Hello');";
	public static String command1 = "Add-Type -AssemblyName System.Speech;";
	public static String command2 = "$somevoice = (New-Object System.Speech.Synthesis.SpeechSynthesizer);";
	public static String command3 = "$somevoice.GetInstalledVoices().VoiceInfo | Select-Object -Property Name;";
	public static String command4 = "$somevoice.SelectVoice('Microsoft Zira Desktop');";//Hazel, Zira
	public static String command5 = "$somevoice.Speak('Hello, Welcome to the speech synthesis world!');";
	public static String command5a = "$somevoice.Speak(\"";
	public static String command5b = "\");";
	
	@FXML Button button_check;
	@FXML Button button_good;
	@FXML Button button_bad;
	@FXML Button button_next;
	@FXML Button button_quit;
	@FXML Button button_quitExam;
	
	@FXML TextArea box_question;
	@FXML TextArea box_answer;
	@FXML TextArea box_correct;
	
	@FXML Label label_questionN;
	@FXML TimeLabel label_time;
	@FXML Label label_score;
	
	public Exam exam;
	public ArrayList<Exercise> deck;
	
	//Current question number
	public int pnumptr;
	//Stats
	public int pscore;
	public int pgoods;
	public int pbads;
	
	public int time;//secs/100
	public Calendar clock;
	public ArrayList<Integer> qtimes;
	
	//Settings
	public boolean boolautograde;
	public boolean boolMaxTime;
	public boolean boolMaxLives;
	public int maxTime;
	public int maxLives;
	public int lives;
	public boolean booldeduction;
	
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
	
	public void powershellStart() {
		try {
			process = new PowerShellProcess();
			process.writeCommand(command1);//speech mode
			process.writeCommand(command2);//create voice object
			process.writeCommand(command3);//shows available voices (to fix).
			//process.writeCommand(command4);//selects voice
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void commandStringQuestions(String str) {
		if(process==null)
			return;
		
		String[] questions = str.split("\n");
		//System.out.println(questions.length);
		try {
			for(String question:questions) 
				process.writeCommand(command5a + question + command5b);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void powershellEnd() {
		if(process==null)
			return;
		process.endProcess();
	}
	
	public void start(Exam ex, PowerShellProcess p) {
		manager.stage.setScene(manager.scene_exam);
		
		exam = ex;
		boolMaxLives = exam.boolMaxLives;
		boolMaxTime = exam.boolMaxTime;
		maxTime = exam.boolMaxTime?exam.maxTime:0;
		maxLives = exam.boolMaxLives?exam.maxLives:0;
		lives = maxLives;
		boolautograde = exam.boolAutoGrade;
		booldeduction = exam.ptsDeduction;
		
		//button_next.setVisible(boolautograde);
		button_good.setVisible(!boolautograde);
		button_bad.setVisible(!boolautograde);
		
		deck = exam.shuffle;
		
		String qstr = questionAllStrings(0, 0);
		
		pnumptr = 0;
		pscore = 0;
		pgoods = 0;
		pbads = 0;
		qtimes = new ArrayList<Integer>();
		
		label_questionN.setText("Question " + (pnumptr+1) + ":");
		label_time.setText("Time: " + 0);
		label_score.setText("Score: " + pscore);
		
		box_question.setText(qstr);
		box_answer.setText("");
		box_correct.setText("");
		
		label_time.prepareTimer(this);
		System.out.println("before textproperty");
		label_time.textProperty().bind(label_time.time);
		System.out.println("after textproperty");
		label_time.startTimer();
		System.out.println("after startTimer");
		
		String message = "Hello, this is speech synthesis";
		//String[] command = {"powershell.exe", "Add-Type -AssemblyName System.Speech; (New-Object System.Speech.Synthesis.SpeechSynthesizer).Speak('" + message +"');"};
		//String[] command1 = {"powershell.exe", ""};
		//System.out.println(qstr.substring(0, qstr.length()-1));
		
		//powershellStart();
		process = p;
		
		if(exam.speechQ)
			commandStringQuestions(qstr);
		
		
		/*
		//-Dmbrola.base=/Users/Fer80_000/Desktop/Free-tts/freetts-1.2/lib/mbrola
		//"C:\\Users\\Fer80_000\\Desktop\\Free-tts\\freetts-1.2\\lib\\mbrola
		System.setProperty("mbrola.base", "C:\\Users\\Fer80_000\\Desktop\\Free-tts\\freetts-1.2\\mbrola");
		
		Voice voice;//Creating object of Voice class
		Voice voices[] = VoiceManager.getInstance().getVoices();
		voice = VoiceManager.getInstance().getVoice("kevin16");//Getting voice
        if (voice != null) {
            voice.allocate();//Allocating Voice
        }
        try {
            voice.setRate(190);//Setting the rate of the voice
            voice.setPitch(150);//Setting the Pitch of the voice
            voice.setVolume(3);//Setting the volume of the voice
            voice.speak(qstr);//Calling speak() method
  
        }
        catch(Exception e)
        {
       e.printStackTrace();
        }
        
        for(Voice v: voices) {
        	System.out.println(v.getName());
        	System.out.println(v.getGender().toString());
        	System.out.println(v.getPitchRange());
        	System.out.println(v.getDescription());
        }
        */
		
		/*
		try {
            // Set property as Kevin Dictionary
            System.setProperty(
                "freetts.voices",
                "com.sun.speech.freetts.en.us"
                    + ".cmu_us_kal.KevinVoiceDirectory");
  
            // Register Engine
            Central.registerEngineCentral(
                "com.sun.speech.freetts"
                + ".jsapi.FreeTTSEngineCentral");
  
            // Create a Synthesizer
            Synthesizer synthesizer
                = Central.createSynthesizer(
                    new SynthesizerModeDesc(Locale.US));
  
            // Allocate synthesizer
            synthesizer.allocate();
  
            // Resume Synthesizer
            synthesizer.resume();
  
            // Speaks the given text
            // until the queue is empty.
            synthesizer.speakPlainText(
                qstr, null);
            synthesizer.waitEngineState(
                Synthesizer.QUEUE_EMPTY);
  
            // Deallocate the Synthesizer.
            synthesizer.deallocate();
        }
  
        catch (Exception e) {
            e.printStackTrace();
        }
        */
		
		//ObservableList<String> obsList = FXCollections.observableArrayList();
		//obsList.add("" + clock.getTimeInMillis() / 1000);
		//label_time.textProperty().bind(obsList);;
	}
	
	public void concludeExam() {
		//Conclude
		label_time.stopTimer();
		label_time.textProperty().unbind();
		time = label_time.timecount;
		powershellEnd();
		
		ConclusionStats stats = new ConclusionStats(pscore, pgoods, pbads, time, qtimes);
		manager.examConclusionState(exam, stats);
		return;
	}
	
	public void nextQuestion() {
		pnumptr++;
		qtimes.add(label_time.timecount - time);
		if(pnumptr >= exam.shuffle.size() && exam.code != 2) {
			//Conclude
			concludeExam();
			return;
		}
		if(exam.code == 2 && pnumptr % exam.shuffle.size() == 0) {
			exam.shuffleDeck();
			System.out.println("Shuffled!");
		}
		
		time = label_time.timecount;
		label_questionN.setText("Question " + (pnumptr+1) + ":");
		label_score.setText("Score: " + pscore);
		
		String qstr = questionAllStrings(pnumptr % exam.shuffle.size(), 0);
		box_question.setText(qstr);
		box_answer.setText("");
		box_correct.setText("");
		
		if(exam.speechQ)
			commandStringQuestions(qstr);
	}
	
	public void autoCheck() {
		String box1 = this.box_answer.getText();
		String box2 = this.box_correct.getText();
		
		box1 = box1.trim().toLowerCase();
		box2 = box2.trim().toLowerCase();
		
		ArrayList<String> words1 = new ArrayList<String>(Arrays.asList(box1.split("[, ?.@+-]")));
		ArrayList<String> words2 = new ArrayList<String>(Arrays.asList(box2.split("[, ?.@+-]")));
		System.out.println(words1);
		System.out.println(words2);
	
		Collections.sort(words1); Collections.sort(words2);
		
		if(box1.equals(box2) || (words1.equals(words2) && words2.equals(words1))) {
			pscore += exam.shuffle.get(pnumptr).score;
			pgoods++;
		}
		else {
			System.out.println("bad");
			pbads++;
			if(booldeduction)
				pscore -= exam.shuffle.get(pnumptr).score;
			if(boolMaxLives) {
				lives--;
				if(lives <= 0) {
					//Conclude
					concludeExam();
					return;
				}
			}
		}
		
	}
	
	public void actionCheck(ActionEvent e) {
		String astr = questionAllStrings(pnumptr, 1);
		box_correct.setText(astr);
		
		if(boolautograde) {
			autoCheck();
			this.button_next.setVisible(true);
			this.button_check.setVisible(false);
		}
		
		if(exam.speechA)
			commandStringQuestions(astr);
	}
	public void actionGood(ActionEvent e) {
		pscore += exam.shuffle.get(pnumptr).score;
		pgoods++;
		nextQuestion();
	}
	public void actionBad(ActionEvent e) {
		pbads++;
		if(booldeduction)
			pscore -= exam.shuffle.get(pnumptr).score;
		if(boolMaxLives) {
			lives--;
			if(lives <= 0) {
				//Conclude
				concludeExam();
				return;
			}
		}
		nextQuestion();
	}
	
	public void actionNext(ActionEvent e) {
		this.button_next.setVisible(false);
		this.button_check.setVisible(true);
		nextQuestion();
	}
	
	public void actionQuitExam(ActionEvent e) {
		//Conclude
		concludeExam();
	}
	
	public void actionQuit(ActionEvent e) {
		//Conclude
		label_time.stopTimer();
		label_time.textProperty().unbind();
		time = label_time.timecount;
		powershellEnd();
		System.exit(0);
	}
	
}
