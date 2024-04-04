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
import javafx.fxml.FXML;

import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.control.TextArea;

import javafx.scene.control.TableView;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;

public class AfgeslotenTicketsSupportManagerController extends AnchorPane
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
	private CheckBox afgehandeldCheckbox = new CheckBox();
	@FXML
	private CheckBox geannuleerdCheckbox = new CheckBox();
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
	private Label labelTicketDetail = new Label();
	
	private TechniekerController techniekerController;
	private SupportManagerController supportManagerController;
	private ITicket t = null;
	
	public AfgeslotenTicketsSupportManagerController(TechniekerController controller,SupportManagerController supportManagerController) 
	{
		this.techniekerController = controller;
		this.supportManagerController = supportManagerController;
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
		tableView.setItems(supportManagerController.geefFilteredListVanTickets());
		
		afgehandeldCheckbox.setSelected(true);
		geannuleerdCheckbox.setSelected(true);
		
		tableView.setPlaceholder(new Label("Geen gebruikers voor de gegeven filter"));
		
		titelInput.setOnKeyTyped(e-> filtreer(e));
		techniekerInput.setOnKeyTyped(e-> filtreer(e));
		bedrijfInput.setOnKeyTyped(e-> filtreer(e));
		geannuleerdCheckbox.setOnAction(e -> filtreer(e));
		afgehandeldCheckbox.setOnAction(e -> filtreer(e));
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
	}
	
	@FXML
	public void setButtonToevoegen(MouseEvent event)
	{
		setToevoegen();
	}
	
	public void setToevoegen()
	{
		txtTitel.setText("");
		areaOmchrijving.setText("");
		areaOpmerking.setText("");
		txtTitel.setText("");
		areaOmchrijving.setText("");
		areaOpmerking.setText("");
		choiceKlant.setValue("");
		choiceTechnieker.setValue("");
		typeTicket.setValue(null);
	}

    public void filtreerBijStart() 
    {
    	Predicate<ITicket> filterStatusBegin = t -> t.getStatus().equals(StatusTicket.Geannuleerd)||t.getStatus().equals(StatusTicket.Afgehandeld);	
		supportManagerController.filterTickets(filterStatusBegin);
    }
    
	public void filtreer(EventObject event) 
	{
		System.out.println("test");
		List<Predicate<ITicket>> predicatesTicket = new ArrayList<>();
		List<Predicate<ITicket>> predicatesStatusTicket = new ArrayList<>();
		
		Predicate<ITicket> filterStatusBegin = t -> t.getStatus().equals(StatusTicket.Geannuleerd)||t.getStatus().equals(StatusTicket.Afgehandeld);
		
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
		
		if(geannuleerdCheckbox.selectedProperty().get()) {
			Predicate<ITicket> filterStatus = t -> t.getStatus().toString().equals(StatusTicket.Geannuleerd.toString());
			predicatesStatusTicket.add(filterStatus);
		}
		
		if(afgehandeldCheckbox.selectedProperty().get()) {
			Predicate<ITicket> filterStatus = t -> t.getStatus().equals(StatusTicket.Afgehandeld);
			predicatesStatusTicket.add(filterStatus);
		}
		
		
		Predicate<ITicket> assemblerStatus = predicatesStatusTicket.stream().reduce(Predicate::or).orElse(x->true);
	    Predicate<ITicket> assembler = predicatesTicket.stream().reduce(Predicate::and).orElse(x->true);
		supportManagerController.filterTickets(assembler.and(assemblerStatus).and(filterStatusBegin));
	}
}
