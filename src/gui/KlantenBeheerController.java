package gui;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;
import java.util.function.Predicate;

import javax.persistence.RollbackException;

import controllers.AdministratorController;
import domein.IContactPersoon;
import domein.IKlant;
import domein.enums.StatusGebruiker;
import exceptions.FouteInvoerException;
import exceptions.VerplichtVeldenException;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;

import javafx.scene.control.TableView;

import javafx.scene.control.TableColumn;

public class KlantenBeheerController extends AnchorPane
{
	@FXML
	private GridPane gradefilterPane = new GridPane();
	@FXML
	private TextField nameInput = new TextField();
	@FXML
	private TextField usernameInput = new TextField();
	@FXML
	private ChoiceBox<StatusGebruiker> statusInput = new ChoiceBox<StatusGebruiker>();
	@FXML
	private TextField bedrijf = new TextField();
	@FXML
	private TableView<IKlant> tableView = new TableView<>();
	@FXML
	private TableColumn<IKlant, String> nameColumn = new TableColumn<IKlant, String>("name");
	@FXML
	private TableColumn<IKlant, String> userNameColumn = new TableColumn<IKlant, String>("userName");
	@FXML
	private TableColumn<IKlant, Long> idColumn = new TableColumn<IKlant, Long>("id");
	@FXML
	private TableColumn<IKlant, String> statusColumn = new TableColumn<IKlant, String>("status");
	@FXML
	private TableColumn<IKlant, String> emailColumn = new TableColumn<IKlant, String>("email");
	@FXML
	private TableColumn<IKlant, String> adresColumn = new TableColumn<IKlant, String>("adres");
	@FXML
	private TableColumn<IKlant, String> actiefColumn = new TableColumn<IKlant, String>("isActief");
	@FXML
	private TableColumn<IKlant, LocalDate> datumColumn= new TableColumn<IKlant, LocalDate>("datumRegistratie");
	@FXML
	private Label labelState = new Label();
	@FXML
	private ChoiceBox<StatusGebruiker> choiceState = new ChoiceBox<>();
	@FXML
	private TextField txtName;
	@FXML
	private TextField txtEmail;
	@FXML
	private TextField txtUsername;
	@FXML
	private TextField 	txtTelefoonnummer;
	@FXML
	private PasswordField pwdPassword;
	@FXML
	private TextField txtAdres;
	@FXML
	private Button btnKlantToevoegen = new Button();
	@FXML
	private Button btnKlantWijzigen = new Button();
	@FXML
	private Button btnKlantVerwijderen = new Button();
	@FXML
	private Label labelKlantToevoegen = new Label();
	@FXML
	private Label labelKlantWijzigen = new Label();
	@FXML
	private TextField  txtVoornaamContactpersoon;
	@FXML
	private TextField txtNaamContactpersoon;
	@FXML
	private TextField txtEmailcontactpersoon;
	@FXML
	private Label lblError = new Label();
	@FXML
    private TableView<IContactPersoon> tableViewContactPersoon = new TableView<IContactPersoon>();
    @FXML
    private TableColumn<IContactPersoon,String> emailColumnCP = new TableColumn<IContactPersoon, String>("email");
    @FXML
    private TableColumn<IContactPersoon,String> voornaamColumnCP = new TableColumn<IContactPersoon, String>("firstName");
    @FXML
    private TableColumn<IContactPersoon,String> familienaamColumnCP = new TableColumn<IContactPersoon, String>("lastName");
    @FXML
    private Button btnVoegCPToe = new Button();
    @FXML
    private Button btnVoegCPToeTerwijlKlantAanmaken = new Button();
	
	private AdministratorController controller;
	private IKlant k = null;
	private IContactPersoon cp = null;
	
	public KlantenBeheerController(AdministratorController controller) 
	{
		this.controller = controller;				
	}
	
	@FXML
	public void initialize()
	{
		//tableView
		nameColumn.setCellValueFactory(e -> e.getValue().name());
		userNameColumn.setCellValueFactory(e -> e.getValue().userName());
		idColumn.setCellValueFactory(new PropertyValueFactory<IKlant, Long>("id"));
		statusColumn.setCellValueFactory(e -> e.getValue().status());
		emailColumn.setCellValueFactory(e -> e.getValue().email());
		datumColumn.setCellValueFactory(new PropertyValueFactory<IKlant, LocalDate>("datumRegistratie"));
		adresColumn.setCellValueFactory(e -> e.getValue().adres());
		actiefColumn.setCellValueFactory(cellData -> {
            boolean isActief = cellData.getValue().isActief();
            String isActiefAsString;
            if(isActief)
            	isActiefAsString = "Actief";
            else
            	isActiefAsString = "Niet-Actief";

         return new ReadOnlyStringWrapper(isActiefAsString);
        });
		tableView.setItems(controller.getFilteredKlantList());
		
		tableView.setPlaceholder(new Label("Geen klanten voor de gegeven filter"));
		
		lblError.setWrapText(true);
		
		//detail paneel
		statusInput.setItems(controller.geefStatussenGebruiker());
		statusInput.setValue(StatusGebruiker.Actief);
		
		choiceState.setItems(controller.geefStatussenGebruiker());
		filteerBijStart();
		choiceState.setValue(StatusGebruiker.Actief);
		
		nameInput.setOnKeyTyped(e-> filtreer(e));				
		usernameInput.setOnKeyTyped(e-> filtreer(e));
		statusInput.setOnAction(e -> filtreer(e));
	}

	@FXML
	public void btnKlantToevoegenAfhandeling(ActionEvent event) 
	{
		try {
			lblError.setText("");
			controller.kijkOfKlantContactPersoonHeeft();
			controller.voegKlantToeAanObservableList(txtName.getText(), txtUsername.getText(), pwdPassword.getText(), txtEmail.getText(), 
					txtAdres.getText(), choiceState.getValue(),txtTelefoonnummer.getText());
			lblError.setText("");
			setToevoegen();
		} catch (VerplichtVeldenException e) {
			lblError.setText(String.format("%s %s", e.getMessage(),e.getInformatieVanNietIngevuldeElementen()));
		} catch (FouteInvoerException e) {
			lblError.setText(String.format("%s %s", e.getMessage(),e.getInformatieVanNietIngevuldeElementen()));
		} catch(IllegalArgumentException e) {
			lblError.setText(e.getMessage());
		}
	}
	
	@FXML
	public void getSelectedRow(MouseEvent event)
	{
		k = tableView.getSelectionModel().getSelectedItem();
		controller.setGeselecteerdeK(controller.geefKlantById(k.getId()));
		txtUsername.setText(k.getUserName());
		txtName.setText(k.getName());
		txtAdres.setText(k.getAdres());
		txtEmail.setText(k.getEmail());
		choiceState.setValue(k.getStatus());
		pwdPassword.setText(k.getPassword());
		txtTelefoonnummer.setText(k.getTelefoonummer());
		
		tableViewContactPersoon.setVisible(true);
		controller.initialiseerContactpersonen();
		tableViewContactPersoon.setItems(controller.geefContactPersonenVanKlant());
		emailColumnCP.setCellValueFactory(e -> e.getValue().email());
		voornaamColumnCP.setCellValueFactory(e -> e.getValue().firstName());
		familienaamColumnCP.setCellValueFactory(e -> e.getValue().lastName());
		
		btnKlantToevoegen.setVisible(false);
		btnKlantWijzigen.setVisible(true);
		btnKlantVerwijderen.setVisible(true);
		lblError.setText("");
		labelState.setVisible(true);
		choiceState.setVisible(true);
		labelKlantToevoegen.setVisible(false);
		labelKlantWijzigen.setVisible(true);
		btnVoegCPToe.setVisible(true);
		btnVoegCPToeTerwijlKlantAanmaken.setVisible(false);
		btnVoegCPToe.setVisible(true);
		
		lblError.setWrapText(true);
	}
	
	@FXML
	public void btnKlantWijzigenAfhandeling(ActionEvent event)
	{
		try {
			lblError.setText("");
			controller.EditKlant(txtName.getText(), txtUsername.getText(), pwdPassword.getText(), txtEmail.getText(), 
						txtAdres.getText(), choiceState.getValue(),txtTelefoonnummer.getText());
			lblError.setText("");
			setToevoegen();
			filtreer(event);
		
		} catch (VerplichtVeldenException e) {
			lblError.setText(String.format("%s %s", e.getMessage(),e.getInformatieVanNietIngevuldeElementen()));
		} catch (FouteInvoerException e) {
			lblError.setText(String.format("%s %s", e.getMessage(),e.getInformatieVanNietIngevuldeElementen()));
		} catch(IllegalArgumentException e) {
			lblError.setText(e.getMessage());
		} catch (RollbackException r ) {
			lblError.setText("Probleem met sql transactie");
		}
	}
	
	@FXML
	public void btnKlantVerwijderenAfhandeling(ActionEvent event)
	{
		controller.verwijderKlant();
		setToevoegen();
		filtreer(event);
	}
	
	@FXML
    void btnVoegCPToeAfhandeling(ActionEvent event) 
	{
		try {
			ContactPersoonToevoegenSchermController cptsc = new ContactPersoonToevoegenSchermController(controller, false, true);
			Stage stage = new Stage();
			stage.setTitle("Contactpersoon toevoegen");
			stage.setScene(new Scene(cptsc, 300, 400));
			stage.show();
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
    }
	
	@FXML
    void btnVoegCPToeTerwijlKlantAanmakenAfhandeling(ActionEvent event) 
	{
		try {
			lblError.setText("");
		
			controller.voegKlantToeZonderAanObservableListToeTeVoegen(txtName.getText(),txtUsername.getText(),pwdPassword.getText(),txtEmail.getText(),txtAdres.getText(),
					LocalDate.now(),txtTelefoonnummer.getText());

			ContactPersoonToevoegenSchermController cptsc = new ContactPersoonToevoegenSchermController(controller, false, false);
			Stage stage = new Stage();
			stage.setTitle("Contactpersoon toevoegen");
			stage.setScene(new Scene(cptsc, 300, 400));
			stage.show();
		} catch (VerplichtVeldenException e) {
			lblError.setText(String.format("%s %s", e.getMessage(),e.getInformatieVanNietIngevuldeElementen()));
		} catch (FouteInvoerException e) {
			lblError.setText(String.format("%s %s", e.getMessage(),e.getInformatieVanNietIngevuldeElementen()));
		} catch(IllegalArgumentException e) {
			lblError.setText(e.getMessage());
		} catch (RollbackException r ) {
			lblError.setText("Probleem met sql transactie");
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
    }
	
	@FXML
	public void setButtonToevoegen(MouseEvent event)
	{
		setToevoegen();
	}
	
	private void setToevoegen()
	{
		lblError.setText("");
		btnKlantWijzigen.setVisible(false);
		btnKlantToevoegen.setVisible(true);
		btnKlantVerwijderen.setVisible(false);
		txtName.setText("");
		txtUsername.setText("");
		txtAdres.setText("");
		txtTelefoonnummer.setText("");
		txtEmail.setText("");
		pwdPassword.setText("");
		labelKlantToevoegen.setVisible(true);
		labelKlantWijzigen.setVisible(false);
		labelState.setVisible(false);
		choiceState.setVisible(false);
		btnVoegCPToe.setVisible(true);		
		tableViewContactPersoon.setVisible(false);
		btnVoegCPToeTerwijlKlantAanmaken.setVisible(true);
		btnVoegCPToe.setVisible(false);
	}
	
	@FXML
	public void getSelectedRowCP(MouseEvent event)
	{
		cp = tableViewContactPersoon.getSelectionModel().getSelectedItem();
	    controller.setGeselecteerdeCP(cp);
		try {
			ContactPersoonToevoegenSchermController cptsc = new ContactPersoonToevoegenSchermController(controller, true, true);
			Stage stage = new Stage();
			stage.setTitle("Contactpersoon verwijderen");
			stage.setScene(new Scene(cptsc, 300, 400));
			stage.show();
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	public void filtreer(EventObject event) 
	{
		List<Predicate<IKlant>> predicatesKlant = new ArrayList<>();
		
		if(nameInput.getText()!=null&&!nameInput.getText().isBlank()) {
			Predicate<IKlant> filterName = k -> k.getName().toLowerCase().startsWith(nameInput.getText().toLowerCase());
			predicatesKlant.add(filterName);
		}
		
		if(usernameInput.getText()!=null&&!usernameInput.getText().isBlank()) {
			Predicate<IKlant> filterUsername = k -> k.getUserName().toLowerCase().startsWith(usernameInput.getText().toLowerCase());
			predicatesKlant.add(filterUsername);
		}
		
		if(statusInput.getValue() != null) {
			Predicate<IKlant> filterStatus = k -> k.getStatus().equals(statusInput.getValue());
			predicatesKlant.add(filterStatus);
		}
		
		Predicate<IKlant> statusFilter = w -> w.getStatus().equals(statusInput.getValue());					
		predicatesKlant.add(statusFilter);
		
	    Predicate<IKlant> assembler = predicatesKlant.stream().reduce(Predicate::and).orElse(x->true);
	    controller.filterKlant(assembler);
	}	
	
	public void filteerBijStart() 
	{
		Predicate<IKlant> statusFilter = w -> w.getStatus().equals(StatusGebruiker.Actief);
		controller.filterKlant(statusFilter);
	}
}
