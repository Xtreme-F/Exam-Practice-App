package ex.controllers;

import java.io.File;

import ex.components.ConclusionStats;
import ex.components.Exam;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Popup;
import javafx.stage.Stage;
import lib.PowerShellProcess;

public class Controllers_Manager {
	Stage stage;
	Scene scene_menu;
	Scene scene_menu_create;
	Scene scene_menu_examsettings;
	Scene scene_exam;
	Scene scene_examconclusion;
	Scene scene_menu_edit;
	
	public Menu_Controller menu;
	public MCreate_Controller mcreate;
	public MExSettings_Controller mexsettings;
	public Exam_Controller examcontrol;
	public EConclusion_Controller econclusioncontrol;
	public MEdit_Controller medit;
	
	public Controllers_Manager(Stage s) {stage = s;};
	
	public Controllers_Manager(Stage s, Scene s_m, Scene s_m_c) {
		stage = s;
		scene_menu = s_m;
		scene_menu_create = s_m_c;
	}
	
	public Controllers_Manager(Stage s, Scene s_m, Scene s_m_c, Scene s_m_s) {
		stage = s;
		scene_menu = s_m;
		scene_menu_create = s_m_c;
		scene_menu_examsettings = s_m_s;
	}
	
	public Controllers_Manager(Stage s, Scene s_m, Scene s_m_c, Scene s_m_s, Menu_Controller m) {
		stage = s;
		scene_menu = s_m;
		scene_menu_create = s_m_c;
		scene_menu_examsettings = s_m_s;
		
		menu = m;
	}
	
	public void setSceneMenu(Scene m) {scene_menu = m;};
	public void setSceneMenuCreate(Scene cm) {scene_menu_create = cm;};
	public void setSceneMenuExamSettings(Scene esm) {scene_menu_examsettings = esm;};
	public void setSceneExam(Scene ex) {scene_exam = ex;};
	public void setSceneExamConclusion(Scene exc) {scene_examconclusion = exc;};
	public void setSceneMenuEdit(Scene em) {scene_menu_edit = em;};
	
	public void setMenuControl(Menu_Controller m) {menu = m;}
	public void setMenuCreateControl(MCreate_Controller mc) {mcreate = mc;}
	public void setMenuExamSettingsControl(MExSettings_Controller mex) {mexsettings = mex;}
	public void setExamControl(Exam_Controller ex) {examcontrol = ex;}
	public void setEConclusionControl(EConclusion_Controller exc) {econclusioncontrol = exc;}
	public void setMenuEditControl(MEdit_Controller me) {medit = me;}
	
	public void start() {
		menu.setManager(this);
		mcreate.setManager(this);
		mexsettings.setManager(this);
		examcontrol.setManager(this);
		econclusioncontrol.setManager(this);
		medit.setManager(this);
		
		stage.setScene(scene_menu);
		stage.show();
	}
	
	public void afterState() {
		stage.setScene(scene_menu);
		stage.show();
	}
	
	public void createState() {
		//Popup popup = new Popup();
		//Alert al = Alert.AlertType;
		String name;
		TextInputDialog dialog1 = new TextInputDialog();
		dialog1.setTitle("Exam Creationg Prep ");
		dialog1.setHeaderText("Input the Name for the exam: ");
		dialog1.getDialogPane().setMinWidth(550);
		
		Button okButton1 = (Button) dialog1.getDialogPane().lookupButton(ButtonType.OK);
		TextField inputField1 = dialog1.getEditor();
		
		dialog1.showAndWait();
		String result1 = dialog1.getResult();
		if(result1==null) {
			System.out.println("Create canceled.");
			return;
		}
		
		/*
		TextInputDialog dialog2 = new TextInputDialog();
		dialog2.setTitle("Exam Creationg Prep ");
		dialog2.setHeaderText("Input the number of questions: ");
		dialog2.getDialogPane().setMinWidth(550);
		
		Button okButton2 = (Button) dialog2.getDialogPane().lookupButton(ButtonType.OK);
		TextField inputField = dialog2.getEditor();
		
		dialog2.showAndWait();
		String result2 = dialog2.getResult();
		if(result2==null) {
			System.out.println("Create canceled.");
			return;
		}
		
		
		int num = 0;
		try {
			num = Integer.parseInt(result2);
		}
		catch(Exception e) {
			System.out.println("Create canceled, input was not an integer");
			return;
		}
		*/
		//popup.show(stage);
		
		//Exam exam = new Exam(result1);
		
		stage.setScene(scene_menu_create);
		mcreate.start(result1);
	}
	
	public void editState(File f) {
		//stage.setScene(scene_menu_edit);
		this.medit.start(f);
	}
	
	public void examSettingsState(File f) {
		
		//stage.setScene(scene_menu_examsettings);
		Exam ex = new Exam(f);
		if(ex == null || ex.questions == null) {
			return;
		}
		this.mexsettings.start(ex);
	}
	
	public void examState(Exam ex, PowerShellProcess process) {
		examcontrol.start(ex, process);
	}
	
	public void examConclusionState(Exam exa, ConclusionStats st) {
		econclusioncontrol.start(exa, st);
	}
	
}
