package ex.app;
import ex.controllers.*;
import ex.controllers.Controllers_Manager;
import ex.controllers.Menu_Controller;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
//import lib.view.LibController;

public class ExamPractice extends Application{
	public void start(Stage stage) throws Exception{
		//String views[] = {"Menu", "Menu_Create", "Menu_StartExam"};
		String views[] = {"Menu", "Menu_Create", "Menu_Edit", "Menu_ExamSettings", "Exam", "Exam_Conclusion"};
		Scene[] scenes = new Scene[views.length];
		Controller[] controllers = new Controller[views.length];
		
		Controllers_Manager manager = new Controllers_Manager(stage, scenes[0], scenes[1]);
		
		System.out.println("before the for");
		for(int i = 0; i < views.length; i++) {
			System.out.println("i: " + i + " views[i]: " + views[i]);
			FXMLLoader loader = new FXMLLoader();
			if(getClass().getResource("/ex/view/" + views[i] + ".fxml") == null)
				System.out.println("fxml path NULL");
			
			loader.setLocation(getClass().getResource("/ex/view/" + views[i] + ".fxml"));
			
			//Example, layout can be any Layout, not just GridPane or VBox.
			AnchorPane layout = (AnchorPane)loader.load();
			Scene temp_scene = new Scene(layout);
			scenes[i] = temp_scene;
			
			
			//controllers[i] = loader.getController();
			switch(i){
			case 0:
				manager.setSceneMenu(scenes[i]);
				manager.setMenuControl(loader.getController());
				break;
			case 1:
				manager.setSceneMenuCreate(scenes[i]);
				manager.setMenuCreateControl(loader.getController());
				break;
			case 2:
				manager.setSceneMenuEdit(scenes[i]);
				manager.setMenuEditControl(loader.getController());
				break;
			case 3:
				manager.setSceneMenuExamSettings(scenes[i]);
				manager.setMenuExamSettingsControl(loader.getController());
				break;
			case 4:
				manager.setSceneExam(scenes[i]);
				manager.setExamControl(loader.getController());
				break;
			case 5:
				manager.setSceneExamConclusion(scenes[i]);
				manager.setEConclusionControl(loader.getController());
				break;
			default:
				System.out.println("DEFAULT");
				break;
			}
		}
		stage.setResizable(false);
		stage.setTitle("Exam Practice!");
		
		//No Icon, sad.
		
		//stage.setHeight(445);
		//stage.setWidth(725);
		//stage.setOnCloseRequest(eventHandler -> LibController.windowOnClose());
		
		//stage.setScene(menu_scene);
		//stage.show();
		
		//Menu_Controller menu = loader.getController();
		
		//Controllers_Manager manager = new Controllers_Manager(stage, scenes[0], scenes[1], scenes[2], controllers[0]);
		
		System.out.println("Before start");
		
		manager.start();
	}
	public static void main(String args[]) {
		launch();
	}
}
