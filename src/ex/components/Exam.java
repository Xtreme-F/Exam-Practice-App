package ex.components;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.regex.Pattern;

import lib.PowerShellProcess;

public class Exam {
	public String name;
	public ArrayList<Exercise> questions;//All questions
	public ArrayList<Exercise> shuffle;//Can be shuffled, can be a range.
	
	//Exam environment settings.
	public int code;//0 for complete, 1 for turbo, 2 for endless
	public PowerShellProcess process;//null if no voice is selected, else a corresponding process is stored
	public boolean speechQ;
	public boolean speechA;
	
	public boolean boolMaxTime;
	public int maxTime;
	
	public boolean boolAutoGrade;
	
	public boolean boolMaxLives;
	public int maxLives;
	
	public boolean ptsDeduction;
	
	public Exam(String n) {
		name = n;
		questions = new ArrayList<Exercise>();
		shuffle = new ArrayList<Exercise>();
	}
	
	public Exam(File f) {
		try {
			readFromText(f);
		}
		catch(Exception e) {
			System.out.println("Could not read from textfile.");
			e.printStackTrace();
		}
	}
	
	public void addQuestion(Exercise toAdd) {
		this.questions.add(toAdd);
	}
	
	public void completeDeck() {
		for(int i = 0; i < questions.size(); i++) {
			shuffle.add(questions.get(i));
		}
	}
	
	public boolean rangeDeck(int beginning, int end) {
		if(beginning < 0 || end >= questions.size())
			return false;
		
		for(int i = beginning; i < end; i++) {
			shuffle.add(questions.get(i));
		}
		return true;
	}
	
	public boolean randomNDeck(int num) {
		if(num >= questions.size())
			return false;
		
		completeDeck();
		shuffleDeck();
		
		ArrayList<Exercise> temp = new ArrayList<Exercise>();
		
		for(int i = 0; i < num; i++) {
			temp.add(shuffle.get(i));
		}
		shuffle = temp;
		
		return true;
	}
	
	public void shuffleDeck() {
		Collections.shuffle(shuffle);
	}
	
	public String getMetadata() {//||CODE||NAME||NUMOFQ||
		return "||322EXAM V1.0.0||" + this.name + "||" + this.questions.size() + "||";
	}
	
	public void printToText() throws IOException{
		File outputFile = new File("res/" + name + ".exam");
		if(outputFile.createNewFile()) {
			//File does not exist and need to create it.
			//System.out.println("Create file true");
		}
		else {
			//File does exist, no need to create it.
			//System.out.println("Create file false");
		}
		//File tempFile = File.createTempFile(name, ".exam", outputFile.getParentFile());
		
		//BufferedReader reader = new BufferedReader(new FileReader(outputFile));
		BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));//tempFile
		
		String currentLine = this.getMetadata();
		writer.write(currentLine+System.getProperty("line.separator"));
		
		for(int i = 0; i < this.questions.size(); i++) {
			ArrayList<String> quest = this.questions.get(i).getText();
			for(int j = 0; j < quest.size(); j++) {
				currentLine = quest.get(j);
				writer.write(currentLine+System.getProperty("line.separator"));
			}
		}
		
		writer.close();
		
	}
	
	public boolean isSplitValid(String[] splitted) {
		return (splitted != null && splitted.length > 0);
	}
	
	public void readFromText(File inputFile) throws Exception{
		BufferedReader reader = new BufferedReader(new FileReader(inputFile));
		String currentLine;
		if((currentLine=reader.readLine())==null) {
			System.out.println("Invalid Exam File");
			return;
		}
		System.out.println(currentLine);
		String[] splitted = currentLine.split(Pattern.quote("||"));
		System.out.println("Splitted length: " + splitted.length);
		System.out.println(splitted[1]);
		if(!isSplitValid(splitted) || !splitted[1].equals("322EXAM V1.0.0")) {//Exam Metadata
			System.out.println("Invalid Exam: No Metadata Found");
			return;
		}
		this.name = splitted[2];
		this.questions = new ArrayList<Exercise>();
		this.shuffle = new ArrayList<Exercise>();
		
		int number = Integer.parseInt(splitted[3]);
		
		//Decrypt each problem
		for(int pnum = 0; pnum < number; pnum++) {
			currentLine = reader.readLine();
			if(currentLine==null) {	System.out.println("Invalid Exam File");	return;	}
			splitted = currentLine.split(Pattern.quote("||"));
			System.out.println(currentLine);
			System.out.println("Splitted Length: " + splitted.length);
			//Question Metadata
			if(!isSplitValid(splitted) || !splitted[1].equals("Question V1.0.0")) {	System.out.println("Invalid Exam: No Metadata Found");	return;	}
			
			int exnum = Integer.parseInt(splitted[2]);//QUESTION INFO
			int qs = Integer.parseInt(splitted[3]);//USE TO READ
			int as = Integer.parseInt(splitted[4]);//USE TO READ
			int score = Integer.parseInt(splitted[5]);//QUESTION INFO
			int diff = Integer.parseInt(splitted[6]);//QUESTION INFO
			
			Exercise ex = new Exercise(exnum, score, diff);
			
			//Decrypt each q&a for multiple q's and a's.
			for(int qnum = 0; qnum < qs; qnum++) {
				//currentLine = reader.readLine();
				if((currentLine=reader.readLine())==null) {	System.out.println("Invalid Exam File");	return;	}
				
				ex.addQuestionLine(currentLine);
			}
			for(int anum = 0; anum < as; anum++) {
				//currentLine = reader.readLine();
				if((currentLine=reader.readLine())==null) {	System.out.println("Invalid Exam File");	return;	}
				
				ex.addAnswerLine(currentLine);
			}
			
			this.addQuestion(ex);
		}
		//FINISHED
		reader.close();
	}
	
}
