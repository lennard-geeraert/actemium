package gui;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;
import java.util.function.Predicate;

import javax.persistence.RollbackException;

import controllers.AdministratorController;
import domein.IWerknemer;
import domein.enums.RolWerknemer;
import domein.enums.StatusGebruiker;
import exceptions.FouteInvoerException;
import exceptions.VerplichtVeldenException;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;

import javafx.scene.control.TableView;

import javafx.scene.control.TableColumn;

public class WerknemersBeheerController extends AnchorPane 
{
	@FXML
	private GridPane gradefilterPane = new GridPane();
	@FXML
	private TextField firstnameInput = new TextField();
	@FXML
	private TextField lastnameInput = new TextField();
	@FXML
	private TextField usernameInput = new TextField();
	@FXML
	private ChoiceBox<StatusGebruiker> statusInput = new ChoiceBox<StatusGebruiker>();
	@FXML
	private CheckMenuItem techniekerItem = new CheckMenuItem();
	@FXML
	private CheckMenuItem administratorItem = new CheckMenuItem();
	@FXML
	private CheckMenuItem supportmanagerItem = new CheckMenuItem();
	@FXML
	private TableView<IWerknemer> tableView = new TableView<>();
	@FXML
	private TableColumn<IWerknemer, String> firstNameColumn = new TableColumn<IWerknemer, String>("firstName");
	@FXML
	private TableColumn<IWerknemer, String> lastNameColumn = new TableColumn<IWerknemer, String>("lastName");
	@FXML
	private TableColumn<IWerknemer, LocalDate> registratieColumn = new TableColumn<IWerknemer, LocalDate>("datumRegistratie");
	@FXML
	private TableColumn<IWerknemer, String> userNameColumn = new TableColumn<IWerknemer, String>("userName");
	@FXML
	private TableColumn<IWerknemer, Long> personeelsnrColumn = new TableColumn<IWerknemer, Long>("id");
	@FXML
	private TableColumn<IWerknemer, String> functieColumn = new TableColumn<IWerknemer, String>("rol");
	@FXML
	private TableColumn<IWerknemer, String> statusColumn = new TableColumn<IWerknemer, String>("status");
	@FXML
	private TableColumn<IWerknemer, String> emailColumn = new TableColumn<IWerknemer, String>("email");
	@FXML
	private TableColumn<IWerknemer, String> adresColumn = new TableColumn<IWerknemer, String>("adres");
	@FXML
	private TableColumn<IWerknemer, String> telefoonColumn= new TableColumn<IWerknemer, String>("telefoonummer");	
	@FXML
	private TextField txtFirstname;
	@FXML
	private TextField txtLastname;
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
	private ChoiceBox<RolWerknemer> choiceRole = new ChoiceBox<>();	
	@FXML
	private Label labelWerknemerToevoegen = new Label();
	@FXML
	private Label labelWerknemerWijzigen = new Label();
	@FXML
	private Label labelState = new Label();
	@FXML
	private Button btnWerknemerToevoegen = new Button();
	@FXML
	private Button btnWerknemerAanpassen = new Button();
	@FXML
	private Button btnWerknemerVerwijderen = new Button();
	@FXML
	private ChoiceBox<StatusGebruiker> choiceState = new ChoiceBox<>();
	@FXML
	private AnchorPane anchorPane;
	@FXML
	private GridPane grid;
	@FXML
	private Label lblError;
	
	private AdministratorController adminController;
	private IWerknemer w = null;

	public WerknemersBeheerController(AdministratorController adminController)
	{
		this.adminController = adminController;
	}
	
	@FXML
	private void initialize()
	{
		tableView.setItems(adminController.getFilteredWerknemerList());
		firstNameColumn.setCellValueFactory(e -> e.getValue().firstName());
		lastNameColumn.setCellValueFactory(e -> e.getValue().lastName());
		registratieColumn.setCellValueFactory(new PropertyValueFactory<IWerknemer, LocalDate>("datumRegistratie"));
		userNameColumn.setCellValueFactory(e -> e.getValue().userName());
		personeelsnrColumn.setCellValueFactory(new PropertyValueFactory<IWerknemer, Long>("id"));
		functieColumn.setCellValueFactory(e -> e.getValue().rol());
		statusColumn.setCellValueFactory(e -> e.getValue().status());
		emailColumn.setCellValueFactory(e -> e.getValue().email());
		adresColumn.setCellValueFactory(e -> e.getValue().adres());
		telefoonColumn.setCellValueFactory(e -> e.getValue().telefoonummer());
		
		choiceRole.setItems(adminController.geefRollenWerknemer());
		choiceRole.setValue(RolWerknemer.Administrator);	
		
		choiceState.setItems(adminController.geefStatussenGebruiker());
		choiceState.setValue(StatusGebruiker.Actief);
		
		statusInput.setItems(adminController.geefStatussenGebruiker());;
		statusInput.setValue(StatusGebruiker.Actief);
		filteerBijStart();
		
		tableView.setPlaceholder(new Label("Geen werknemers voor de gegeven filter"));
		
		lblError.setWrapText(true);
		
		firstnameInput.setOnKeyTyped(e-> filtreer(e));
		lastnameInput.setOnKeyTyped(e-> filtreer(e));
		usernameInput.setOnKeyTyped(e-> filtreer(e));
		statusInput.setOnAction(e -> filtreer(e));
		techniekerItem.setOnAction(e -> filtreer(e));
		administratorItem.setOnAction(e -> filtreer(e));
		supportmanagerItem.setOnAction(e -> filtreer(e));
	}
	
	@FXML
	public void btnWerknemerToevoegenAfhandeling(ActionEvent event) 
	{
		try {
			lblError.setText("");

			adminController.voegWerknemer(txtFirstname.getText(),txtLastname.getText(),pwdPassword.getText(),
					choiceRole.getValue(),LocalDate.now(),txtUsername.getText(),txtAdres.getText(),txtEmail.getText(),txtTelefoonnummer.getText());
			setToevoegen();
			lblError.setText("");			
		} catch (VerplichtVeldenException e) {
			lblError.setText(String.format("%s %s", e.getMessage(), e.getInformatieVanNietIngevuldeElementen()));
		} catch (FouteInvoerException e) {
			lblError.setText(String.format("%s %s", e.getMessage(),e.getInformatieVanNietIngevuldeElementen()));
		} catch(IllegalArgumentException e) {
			lblError.setText(e.getMessage());
		} catch (RollbackException r ) {
			lblError.setText("Probleem met sql transactie");
		}
	}
	
	@FXML
	public void getSelectedRow(MouseEvent event)
	{
		w = tableView.getSelectionModel().getSelectedItem();
		adminController.setGeselecteerdeW(adminController.geefWerknemerById(w.getId()));
		txtUsername.setText(w.getUserName());
		txtFirstname.setText(w.getFirstName());
		txtLastname.setText(w.getLastName());
		txtAdres.setText(w.getAdres());
		txtEmail.setText(w.getEmail());
		choiceRole.setValue(w.getRol());
		pwdPassword.setText(w.getPassword());
		choiceState.setValue(w.getStatus());
		txtTelefoonnummer.setText(w.getTelefoonummer());
		
		btnWerknemerAanpassen.setVisible(true);
		btnWerknemerToevoegen.setVisible(false);
		btnWerknemerVerwijderen.setVisible(true);
		lblError.setText("");
		labelWerknemerToevoegen.setVisible(false);
		labelWerknemerWijzigen.setVisible(true);
		labelState.setVisible(true);
		choiceState.setVisible(true);
	}
	
	@FXML
	public void btnEditWerknemerAfhandeling(ActionEvent event)
	{
		try {
			lblError.setText("");
			adminController.editWerknemer(txtFirstname.getText(), txtLastname.getText(), pwdPassword.getText(), choiceRole.getValue(),
					txtUsername.getText(), txtAdres.getText(), txtEmail.getText(), choiceState.getValue(),txtTelefoonnummer.getText());			
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
	public void btnVerwijderWerknemerAfhandeling(ActionEvent event)
	{
		adminController.verwijderWerknemer();		
		setToevoegen();
		filtreer(event);
	}
	
	@FXML
	public void setButtonToevoegen(MouseEvent event)
	{
		setToevoegen();
	}
	
	private void setToevoegen()
	{
		lblError.setText("");
		btnWerknemerAanpassen.setVisible(false);
		btnWerknemerToevoegen.setVisible(true);
		btnWerknemerVerwijderen.setVisible(false);
		txtFirstname.setText("");
		txtLastname.setText("");
		txtUsername.setText("");
		txtAdres.setText("");
		txtEmail.setText("");
		txtTelefoonnummer.setText("");
		pwdPassword.setText("");
		choiceRole.setValue(RolWerknemer.Administrator);
		labelWerknemerToevoegen.setVisible(true);
		labelWerknemerWijzigen.setVisible(false);
		labelState.setVisible(false);
		choiceState.setVisible(false);
	}
	
	public void filtreer(EventObject event) 
	{
		List<Predicate<IWerknemer>> predicatesWerknemer = new ArrayList<>();
		List<Predicate<IWerknemer>> predicatesRol = new ArrayList<>();
		
		if(firstnameInput.getText()!=null&&!firstnameInput.getText().isBlank()) {
			Predicate<IWerknemer> filterFirstName = w -> w.getFirstName().toLowerCase().startsWith(firstnameInput.getText().toLowerCase());
			predicatesWerknemer.add(filterFirstName);
		}
		if(lastnameInput.getText()!=null&&!lastnameInput.getText().isBlank()) {
			Predicate<IWerknemer> filterLastName = w -> w.getLastName().toLowerCase().startsWith(lastnameInput.getText().toLowerCase());
			predicatesWerknemer.add(filterLastName);
		}
		if(usernameInput.getText()!=null&&!usernameInput.getText().isBlank()) {
			Predicate<IWerknemer> filterUserName = w -> w.getUserName().toLowerCase().startsWith(usernameInput.getText().toLowerCase());
			predicatesWerknemer.add(filterUserName);
		}		
		if(administratorItem.isSelected()) {
			Predicate<IWerknemer> filterRol = w -> w.getRol().equals(RolWerknemer.Administrator);
			predicatesRol.add(filterRol);
		}
		if(techniekerItem.isSelected()) {
			Predicate<IWerknemer> filterRol = w -> w.getRol().equals(RolWerknemer.Technieker);
			predicatesRol.add(filterRol);
		}
		if(supportmanagerItem.isSelected()) {
			Predicate<IWerknemer> filterRol = w -> w.getRol().equals(RolWerknemer.SupportManager);
			predicatesRol.add(filterRol);
		}
		Predicate<IWerknemer> statusFilter = w -> w.getStatus().equals(statusInput.getValue());					
	    predicatesWerknemer.add(statusFilter);
	    
	    Predicate<IWerknemer> statusAssembler = predicatesRol.stream().reduce(Predicate::or).orElse(x->true);
		
	    Predicate<IWerknemer> assembler = predicatesWerknemer.stream().reduce(Predicate::and).orElse(x->true);
		adminController.filterWerknemer(assembler.and(statusAssembler));
		
	}	
	
	public void filteerBijStart() 
	{
		Predicate<IWerknemer> statusFilter = w -> w.getStatus().equals(StatusGebruiker.Actief);
		adminController.filterWerknemer(statusFilter);
	}
}