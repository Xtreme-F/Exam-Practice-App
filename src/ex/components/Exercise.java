package ex.components;

import java.util.ArrayList;

public class Exercise {
	public ArrayList<String> question;//Multiple lines of question.
	public ArrayList<String> answer;//Multiple Answers or lines of answer.
	
	public int number;
	public int score;
	public int difficulty;
	
	public Exercise(){
		question = new ArrayList<String>();
		answer = new ArrayList<String>();
	}
	public Exercise(int n) {
		number = n;
		question = new ArrayList<String>();
		answer = new ArrayList<String>();
	}
	public Exercise(int n, int s) {
		number = n;
		score = s;
		question = new ArrayList<String>();
		answer = new ArrayList<String>();
	}
	public Exercise(int n, int s, int diff) {
		number = n;
		score = s;
		difficulty = diff;
		question = new ArrayList<String>();
		answer = new ArrayList<String>();
	}
	
	public void setQuestion(ArrayList<String> q) {		question = q;	}
	public void setAnswer(ArrayList<String> a) {		answer = a;	}
	public void setNumber(int n) {		number = n;	}
	public void setScore(int s) {		score = s;	}
	public void setDiff(int d) {		difficulty = d;	}
	
	public void addQuestionLine(String s) {		question.add(s);	}
	public void addAnswerLine(String s) {		answer.add(s);	}
	
	public String getMetadata() {//||VERSION||NUMBER||QLENGTH||ALENGTG||SCORE||DIFF
		return "||Question V1.0.0||" + number + "||" + question.size() + "||" + answer.size() + "||" + score + "||" + difficulty + "||";
	}
	
	public ArrayList<String> getText() {
		ArrayList<String> result = new ArrayList<String>();
		result.add(this.getMetadata());
		for(int i = 0; i < question.size(); i++) {
			result.add(this.question.get(i));
		}
		for(int i = 0; i < answer.size(); i++) {
			result.add(this.answer.get(i));
		}
		return result;
	}
	
	public String toString() {// Question: <first question> Answer: <first answer>
		if(question.size()==0)
			return "0";
		return "Question: " + question.get(0) + " - Answer: " + answer.get(0);
	}
}
