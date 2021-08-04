package ex.controllers;

import ex.controllers.Controllers_Manager;
import javafx.event.ActionEvent;

public abstract class Controller {
	public Controllers_Manager manager;
	
	public void start() {
		
	}
	
	public void setManager(Controllers_Manager man) {
		manager = man;
	}
	
	/**
	 * Terminates program.
	 * @param e Push of the button that will terminate the program when triggered.
	 */
	public void actionQuit(ActionEvent e) {
		System.exit(0);
	}
}
