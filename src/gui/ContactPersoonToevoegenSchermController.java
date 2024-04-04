package gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import controllers.AdministratorController;
import domein.ContactPersoon;
import domein.IContactPersoon;
import exceptions.FouteInvoerException;
import exceptions.VerplichtVeldenException;
import javafx.event.ActionEvent;

public class ContactPersoonToevoegenSchermController extends AnchorPane
{
	@FXML
	private TextField txtEmail;
	@FXML
	private TextField txtFirstName;
	@FXML
	private TextField txtLastName;
	@FXML
	private Label lblAdd = new Label();
	@FXML
	private Label lblToevoegenOk = new Label();
	@FXML
	private Label lblError= new Label();
	@FXML
	private Button btnToevoegen = new Button();
	@FXML
	private Button btnVerwijder = new Button();
	@FXML
	private Label lblVerwijderen = new Label();
	
	private AdministratorController controller;
	private boolean verwijderen;
	private boolean heeftKlant;
	
	public ContactPersoonToevoegenSchermController(AdministratorController controller, boolean verwijderen, boolean heeftKlant) 
	{
		this.controller = controller;
		this.verwijderen = verwijderen;
		this.heeftKlant = heeftKlant;
		try 
		{
			FXMLLoader loader = new FXMLLoader(getClass().getResource("ContactPersoonToevoegenScherm.fxml"));
			loader.setController(this);
			loader.setRoot(this);
			loader.load();
		} catch (Exception ex) 
		{
			System.out.println(ex.getMessage());
		}
		lblError.setWrapText(true);
		toonJuisteScherm();
	}
	
	@FXML
	public void toonJuisteScherm()
	{
		IContactPersoon contact = controller.getGeselecteerdeCP();
		if(verwijderen)
		{
			btnToevoegen.setVisible(false);
			txtEmail.setText(contact.getEmail());
			txtEmail.setDisable(true);
			txtFirstName.setText(contact.getFirstName());
			txtFirstName.setDisable(true);
			txtLastName.setText(contact.getLastName());
			txtLastName.setDisable(true);
			lblAdd.setVisible(false);
		}
		else
		{
			btnVerwijder.setVisible(false);
			lblVerwijderen.setVisible(false);
		}
	}

	@FXML
	public void btnVoegToeAfhandeling(ActionEvent event) 
	{
		try {
			lblError.setText("");
			ContactPersoon c = new ContactPersoon.Builder().email(txtEmail.getText())
					.firstName(txtFirstName.getText()).lastName(txtLastName.getText()).build();
			if(heeftKlant)
				controller.voegContactPersoonAanGeselecteerdeKlant(c);
			else
				controller.voegContactPersoonAanBijnaToegevoegdeKlant(c);
			setTerugNormaal();
			lblToevoegenOk.setText("Contactpersoon " + txtFirstName.getText() + " succesvol toegevoegd");
		} catch (VerplichtVeldenException e) {
			lblError.setText(String.format("%s %s", e.getMessage(), e.getInformatieVanNietIngevuldeElementen()));
		} catch (FouteInvoerException e) {
			lblError.setText(String.format("%s %s", e.getMessage(),e.getInformatieVanNietIngevuldeElementen()));
		}
	}
	
	@FXML
    void btnVerwijderAfhandeling(ActionEvent event) throws VerplichtVeldenException
	{
		try {
			lblError.setText("");
			controller.verwijderContactPersoon(txtEmail.getText());
			setTerugNormaal();
			lblToevoegenOk.setText("Contactpersoon " + txtFirstName.getText() + " succesvol verwijderd");
			Stage stage = (Stage) this.getScene().getWindow();
		    stage.close();
		} catch (VerplichtVeldenException e) {
			lblError.setText(e.getMessage());
		} 
    }
	
	private void setTerugNormaal()
	{
		txtEmail.setText("");
		txtFirstName.setText("");
		txtLastName.setText("");
		lblError.setText("");
	}
}
