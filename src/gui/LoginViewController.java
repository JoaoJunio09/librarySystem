package gui;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginViewController implements Initializable {
	
	@FXML
	private TextField txtLogin;
	
	@FXML
	private PasswordField pswTxtSenha;
	
	@FXML
	private Label labelErrorLoginOrSenha;
	
	@FXML
	private Button btLogin;
	
	@FXML
	public void onBtLogin() {
		System.out.println("onBtLogin");
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {		
	}
}
