package ex.components;

import java.io.IOException;

import ex.controllers.Controllers_Manager;
import ex.controllers.MEditDialog_Controller;
import ex.controllers.MEdit_Controller;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Dialog;

public class EditDialog extends Dialog<Exercise>{
	public boolean booladd;
	public EditDialog(Exam exam, Exercise exercise, boolean a, Controllers_Manager man) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ex/view/Menu_Edit_Dialog.fxml"));
            Parent root = loader.load();
            MEditDialog_Controller controller = loader.<MEditDialog_Controller>getController();
            controller.dialog = this;
            controller.setManager(man);
            controller.exam = exam;
            controller.exercise = exercise;
            controller.booladd = a;
            booladd = a;
            
            //controller.setModel(new LoginModel(data));
            getDialogPane().setContent(root);
            controller.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}