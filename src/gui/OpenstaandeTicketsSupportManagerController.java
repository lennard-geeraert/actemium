package gui;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;
import java.util.function.Predicate;

import controllers.SupportManagerController;
import controllers.TechniekerController;
import domein.ITicket;
import domein.enums.StatusTicket;
import domein.enums.TypeTicket;
import exceptions.EntityBestaalAlException;
import exceptions.FouteInvoerException;
import exceptions.VerplichtVeldenException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.control.TextArea;

import javafx.scene.control.TableView;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;

public class OpenstaandeTicketsSupportManagerController extends AnchorPane
{
	@FXML
	private GridPane gradefilterPane = new GridPane();
	@FXML
	private TextField titelInput = new TextField();
	@FXML
	private TextField techniekerInput = new TextField();
	@FXML
	private TextField bedrijfInput = new TextField();
	@FXML
	private CheckBox aangemaaktCheckbox = new CheckBox();
	@FXML
	private CheckBox inBehandelingCheckbox = new CheckBox();
	@FXML
	private TableView<ITicket> tableView = new TableView<>();
	@FXML
	private TableColumn<ITicket, Long> idColumn = new TableColumn<ITicket, Long>("ticketID");
	@FXML
	private TableColumn<ITicket, String> titelColumn = new TableColumn<ITicket, String>("titel");
	@FXML
	private TableColumn<ITicket, String> techniekerColumn = new TableColumn<ITicket, String>("toegewezenTechnieker");
	@FXML
	private TableColumn<ITicket, String> bedrijfColumn = new TableColumn<ITicket, String>("bedrijfKlant");
	@FXML
	private TableColumn<ITicket, String> statusColumn = new TableColumn<ITicket, String>("status");
	@FXML
	private TableColumn<ITicket, String> typeColumn = new TableColumn<ITicket, String>("type");
	@FXML
	private TableColumn<ITicket, LocalDate> datumColumn = new TableColumn<ITicket, LocalDate>("datumAanmaak");
	@FXML
	private TextField txtTitel;
	@FXML
	private TextArea areaOpmerking;
	@FXML
	private TextArea areaOmchrijving;
	@FXML
	private ChoiceBox<String> choiceTechnieker = new ChoiceBox<>();
	@FXML
	private ChoiceBox<String> choiceKlant = new ChoiceBox<>();
	@FXML
	private ChoiceBox<TypeTicket> typeTicket = new ChoiceBox<>();
	@FXML
	private Button btnMaakTicket = new Button();
	@FXML
	private Button btnVerwijderTicket = new Button();
	@FXML
	private Label labelTicketToevoegen = new Label();
	@FXML
	private Label labelTicketWijzigen = new Label();
	@FXML
	private Label lblError;
	
	private TechniekerController techniekerController;
	private SupportManagerController suportmanagerController;
	private ITicket t = null;
	
	public OpenstaandeTicketsSupportManagerController(TechniekerController controller,SupportManagerController supportmanagercontroller) 
	{
		this.techniekerController = controller;
		this.suportmanagerController = supportmanagercontroller;
	}
	
	@FXML
	private void initialize()
	{
		idColumn.setCellValueFactory(new PropertyValueFactory<ITicket, Long>("ticketID"));
		titelColumn.setCellValueFactory(e -> e.getValue().titel());
		techniekerColumn.setCellValueFactory(e -> e.getValue().toegewezenTechnieker());
		bedrijfColumn.setCellValueFactory(e -> e.getValue().bedrijfKlant());
		statusColumn.setCellValueFactory(e -> e.getValue().status());
		typeColumn.setCellValueFactory(e -> e.getValue().type());
		datumColumn.setCellValueFactory(new PropertyValueFactory<ITicket, LocalDate>("datumAanmaak"));
		
		filtreerBijStart();
		tableView.setItems(suportmanagerController.geefFilteredListVanTickets());
		
		choiceTechnieker.setItems(techniekerController.geefAlleNamenVanTechniekers());
		choiceKlant.setItems(techniekerController.geefAlleNamenVanKlanten());
		typeTicket.setItems(techniekerController.geefAlleTypes());
		typeTicket.setValue(techniekerController.geefAlleTypes().get(0));
		
		aangemaaktCheckbox.setSelected(true);
		inBehandelingCheckbox.setSelected(true);
		
		tableView.setPlaceholder(new Label("Geen ticktets voor de gegeven filter"));
		
		lblError.setWrapText(true);
		
		titelInput.setOnKeyTyped(e-> filtreer(e));
		techniekerInput.setOnKeyTyped(e-> filtreer(e));
		bedrijfInput.setOnKeyTyped(e-> filtreer(e));
		aangemaaktCheckbox.setOnAction(e -> filtreer(e));
		inBehandelingCheckbox.setOnAction(e -> filtreer(e));
	}
	
	@FXML
    void btnMaakTicketAfhandeling(ActionEvent event) throws FouteInvoerException 
	{
		try {
			lblError.setText("");
		techniekerController.voegTicketToe(txtTitel.getText(), choiceTechnieker.getValue(), typeTicket.getValue(), areaOmchrijving.getText(),
				areaOpmerking.getText(), choiceKlant.getValue(), StatusTicket.Aangemaakt);
		lblError.setText("");
		setToevoegen();
		}catch(VerplichtVeldenException v) {
			lblError.setText(String.format("%s %s", v.getMessage(),v.getInformatieVanNietIngevuldeElementen()));
		}
		catch (FouteInvoerException e) {
			lblError.setText(String.format("%s %s", e.getMessage(),e.getInformatieVanNietIngevuldeElementen()));
		}
		catch(EntityBestaalAlException e) {
			lblError.setText(e.getMessage());
		}
    }
	
	@FXML
	public void getSelectedRow(MouseEvent event)
	{
		t = tableView.getSelectionModel().getSelectedItem();
		techniekerController.setGeselecteerdeT(techniekerController.geefTicketById(t.getTicketID()));
		txtTitel.setText(t.getTitel());
		choiceTechnieker.setValue(t.getToegewezenTechnieker());
		choiceKlant.setValue(t.getBedrijfKlant());
		typeTicket.setValue(t.getType());
		areaOmchrijving.setText(t.getOmschrijving());
		areaOpmerking.setText(t.getOpmerking());
		lblError.setText("");
		btnMaakTicket.setVisible(false);
		btnVerwijderTicket.setVisible(true);
		txtTitel.setEditable(false);
		areaOmchrijving.setEditable(false);
		areaOpmerking.setEditable(false);
		
		labelTicketToevoegen.setVisible(false);
		labelTicketWijzigen.setVisible(true);
	}
	
	@FXML
	public void setButtonToevoegen(MouseEvent event)
	{
		setToevoegen();
	}
	
	public void setToevoegen()
	{
		lblError.setText("");
		btnMaakTicket.setVisible(true);
		btnVerwijderTicket.setVisible(false);
		txtTitel.setText("");
		areaOmchrijving.setText("");
		areaOpmerking.setText("");
		labelTicketToevoegen.setVisible(true);
		labelTicketWijzigen.setVisible(false);
		txtTitel.setEditable(true);
		areaOmchrijving.setEditable(true);
		areaOpmerking.setEditable(true);
		
		choiceKlant.setDisable(false);
		choiceTechnieker.setDisable(false);
		typeTicket.setDisable(false);
	}

    @FXML
    void btnVerwijderTicketAfhandeling(ActionEvent event) 
    {
    	techniekerController.verwijderTicket(techniekerController.getGeselecteerdeT());
    	setToevoegen();
    	tableView.refresh();
    }
    
    public void filtreerBijStart() 
    {
    	Predicate<ITicket> filterStatusBegin = t -> t.getStatus().equals(StatusTicket.Aangemaakt)||t.getStatus().equals(StatusTicket.InBehandeling);		
		suportmanagerController.filterTickets(filterStatusBegin);
    }
    
	public void filtreer(EventObject event) 
	{
		List<Predicate<ITicket>> predicatesTicket = new ArrayList<>();
		List<Predicate<ITicket>> predicatesStatusTicket = new ArrayList<>();
		
		Predicate<ITicket> filterStatusBegin = t -> t.getStatus().equals(StatusTicket.Aangemaakt)||t.getStatus().equals(StatusTicket.InBehandeling);
		
		if(titelInput.getText()!=null&&!titelInput.getText().isBlank()) {
			Predicate<ITicket> filterTitel = t -> t.getTitel().toLowerCase().startsWith(titelInput.getText().toLowerCase());
			predicatesTicket.add(filterTitel);
		}
		
		if(techniekerInput.getText()!=null&&!techniekerInput.getText().isBlank()) {
			Predicate<ITicket> filterTechnieker = t -> t.getToegewezenTechnieker().toLowerCase().startsWith(techniekerInput.getText().toLowerCase());
			predicatesTicket.add(filterTechnieker);
		}
		
		if(bedrijfInput.getText()!=null&&!bedrijfInput.getText().isBlank()) {
			Predicate<ITicket> filterBedrijf = t -> t.getBedrijfKlant().toLowerCase().startsWith(bedrijfInput.getText().toLowerCase());
			predicatesTicket.add(filterBedrijf);
		}	
		
		if(aangemaaktCheckbox.selectedProperty().get()) {
			Predicate<ITicket> filterStatus = t -> t.getStatus().toString().equals(StatusTicket.Aangemaakt.toString());
			predicatesStatusTicket.add(filterStatus);
		}
		
		if(inBehandelingCheckbox.selectedProperty().get()) {
			Predicate<ITicket> filterStatus = t -> t.getStatus().equals(StatusTicket.InBehandeling);
			predicatesStatusTicket.add(filterStatus);
		}
		
		Predicate<ITicket> assemblerStatus = predicatesStatusTicket.stream().reduce(Predicate::or).orElse(x->true);
	    Predicate<ITicket> assembler = predicatesTicket.stream().reduce(Predicate::and).orElse(x->true);
		suportmanagerController.filterTickets(assembler.and(assemblerStatus).and(filterStatusBegin));
	}	
}
