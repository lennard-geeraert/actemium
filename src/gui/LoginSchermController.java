package gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;

import javafx.scene.control.TextField;

import java.util.NoSuchElementException;

import controllers.AdministratorController;
import controllers.LoginController;
import controllers.SupportManagerController;
import controllers.TechniekerController;
import domein.IWerknemer;
import domein.enums.RolWerknemer;
import javafx.event.ActionEvent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import javafx.scene.control.PasswordField;

public class LoginSchermController extends GridPane 
{
	@FXML
	private TextField txtUsername;
	@FXML
	private PasswordField pwdPassword;
	@FXML
	private Button btnSignIn;
	@FXML
	private TextField txtFirstName;
	@FXML
	private ImageView ivImage;
	
	private LoginController loginController;
	private AdministratorController adminController;
	private TechniekerController techniekerController;
	private SupportManagerController supportController;
	
	public LoginSchermController(LoginController loginController, AdministratorController admin, TechniekerController tech, SupportManagerController supp) 
	{
		super();
		this.loginController = loginController;
		this.adminController = admin;
		this.techniekerController = tech;
		this.supportController = supp;
		ivImage = new ImageView	(new Image(getClass().getResourceAsStream("/images/1519912855639.png")));
		ivImage.setTranslateX(15);
		ivImage.setTranslateY(120);
		ivImage.setFitHeight(300);
		ivImage.setFitWidth(300);	
		this.getChildren().addAll(ivImage);
		
		try 
		{
			FXMLLoader loader = new FXMLLoader(getClass().getResource("LoginScherm.fxml"));
			loader.setController(this);
			loader.setRoot(this);
			loader.load();
		} catch (Exception ex) 
		{
			System.out.println(ex.getMessage());
		}
	}

	@FXML
	public void btnSignInAfhandeling(ActionEvent event)
	{
		try {		
			btnSignIn.setDefaultButton(true);
			loginController.meldAan(txtUsername.getText(), pwdPassword.getText());
			IWerknemer w = loginController.getWerknemer();
			if(w.getRol() == RolWerknemer.Administrator) {
				adminController.setWerknemer(w);
				AdministratorSchermController controller = new AdministratorSchermController(adminController);
				Scene scene = new Scene(controller);
				Stage stage = (Stage) this.getScene().getWindow();
				stage.setScene(scene);
				stage.setMaximized(true);
				stage.show();
			}
			else if(w.getRol() == RolWerknemer.Technieker) {
				techniekerController.setWerknemer(w);
				techniekerController.setLists();
				TechniekerSchermController controller = new TechniekerSchermController(techniekerController);
				Scene scene = new Scene(controller);
				Stage stage = (Stage) this.getScene().getWindow();
				stage.setScene(scene);
				stage.setMaximized(true);
				stage.show();
			}
			else if(w.getRol() == RolWerknemer.SupportManager) {
				supportController.setWerknemer(w);
				techniekerController.setWerknemer(w);
				techniekerController.setLists();
				SupportManagerSchermController controller = new SupportManagerSchermController(supportController, techniekerController);
				Scene scene = new Scene(controller);
				Stage stage = (Stage) this.getScene().getWindow();
				stage.setScene(scene);
				stage.setMaximized(true);
				stage.show();
			}
		}catch(IllegalArgumentException e)
		{
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
			alert.setTitle("Login");
			alert.setHeaderText("Foute Login !");
			alert.setContentText(e.getMessage());
			alert.showAndWait();
		}
		catch(NoSuchElementException e)
		{
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
			alert.setTitle("Block");
			alert.setHeaderText("U bent geblokkeerd!");
			alert.setContentText(e.getMessage());
			alert.showAndWait();
		}
	}
}
