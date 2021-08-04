package ex.components;

import java.text.DecimalFormat;

import ex.controllers.Exam_Controller;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.StringPropertyBase;
import javafx.concurrent.Task;
import javafx.scene.control.Label;

public class TimeLabel extends Label{
	TimeLabel a = this;
	Exam_Controller controller;
	public StringProperty time;
	//public IntegerProperty time;
	public int timecount;
	public Thread thread;
	public boolean isRunning;
	
	static char degree = 176;
	
	public void prepareTimer(Exam_Controller e) {
		controller = e;
		timecount = 0;
		time = new SimpleStringProperty("Time: " + timecount);
	}
	public void startTimer() {
		//time.set(0);
		timecount = 0;
		isRunning = true;
		
		//Task task = new Task<Void>() {
		Runnable task = new Runnable() {
			@Override
			//public Void call() {
			public void run() {
				while(isRunning) {
					//System.out.println("A");
					//try {
						
						try {
							Thread.sleep(10);
						}
						catch(Exception e) {
							e.printStackTrace();
						}
						//time.add(1);
						if(!isRunning)
							break;
						timecount++;
						Platform.runLater(()->{
							//a.setText("Time: " + timecount);
							//time.set("Time: " + timecount/360000 + "" + degree + "" + timecount % 360000 / 6000 % 100 + "\'" + timecount % 360000 % 6000 / 100+ "\'\'." + timecount % 360000 % 6000 % 100);
							time.set("Time: " + TimeLabel.timeToString(timecount));
							if(controller.exam.boolMaxTime && (timecount - controller.time) >= controller.exam.maxTime) {
								controller.actionBad(null);
							}
						});
						
						
					//}
					//catch(Exception e) {
					//	e.printStackTrace();
					//}	
				}
				//return null;
			}
			
		};
		
		thread = new Thread(task);
		thread.start();
	}
	
	public void stopTimer() {
		isRunning = false;
	}
	
	public static String timeToString(int inputtime) {//input time is in 1sec/100
		int hours = inputtime / 360000;
		int mins = inputtime % 360000 / 6000 % 100;
		int secs = inputtime % 360000 % 6000 / 100;
		int centsecs = inputtime % 360000 % 6000 % 100;
		
		DecimalFormat format = new DecimalFormat("00");
		//IntegerFormat fora;
		return "" + hours + "" + degree + "" + format.format(mins) + "\'" + format.format(secs) + "\'\'." + format.format(centsecs);
	}
	public String timeToString(double inputtime) {
		
		return "";
	}
	
}
