package gui;

import javafx.fxml.FXML;

import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;
import java.util.function.Predicate;

import javax.persistence.RollbackException;

import controllers.SupportManagerController;
import domein.IContracttype;
import domein.enums.ManierTicketAanmaak;
import domein.enums.StatusContracttype;
import domein.enums.TijdstippenAanmaakTicket;
import exceptions.EntityBestaalAlException;
import exceptions.FouteInvoerException;
import exceptions.VerplichtVeldenException;
import javafx.event.ActionEvent;


import javafx.scene.control.TableView;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;

public class ContracttypeBeheerController extends AnchorPane 
{
	@FXML
	private TableView<IContracttype> tableView = new TableView<IContracttype>();
	@FXML
	private TableColumn<IContracttype,String> naamColumn =new TableColumn<IContracttype, String>("naam");;
	@FXML
	private TableColumn<IContracttype,String> tijdstipColumn =new TableColumn<IContracttype, String>("tijdStippenMogelijkheid");;
	@FXML
	private TableColumn<IContracttype,Integer> prijsColumn =new TableColumn<IContracttype, Integer>("prijsContract");;
	@FXML
	private TableColumn<IContracttype,String> isactiefColumn =new TableColumn<IContracttype,String>("isActief");;
	@FXML
	private TableColumn<IContracttype, Long> contracttypeidColumn = new TableColumn<IContracttype, Long>("id");
	@FXML
	private Button btnContracttypeVerwijderen = new Button();
	@FXML
	private TextField txtNaam;
	@FXML
	private Label lblError = new Label();
	@FXML
	private Label lblLopendeContracten;
	@FXML
	private Label lblBehandeldeTickets;
	@FXML
	private TextField txtLopendeContracten;
	@FXML
	private TextField txtBehandeldeTickets;
	@FXML
	private TextField txtMaxAfh;
	@FXML
	private TextField txtMinAfh;
	@FXML
	private TextField txtPrijs;
	@FXML
	private ChoiceBox<TijdstippenAanmaakTicket> choiceTijdsttip = new ChoiceBox<>();
	@FXML
	private ChoiceBox<ManierTicketAanmaak> choiceManier  = new ChoiceBox<>();
	@FXML
	private Label lblContracttypeWijzigen = new Label();
	@FXML
	private Label labelContracttypeToevoegen = new Label();
	@FXML
	private Label lblStatusContracttype = new Label();
	@FXML
	private AnchorPane anchorPane;
	@FXML
	private Button btnContracttypeToevoegen = new Button();
	@FXML
	private Button btnContracttypeWijzigen =new Button();
	@FXML
	private ChoiceBox<StatusContracttype> isActiefInput = new ChoiceBox<>();
	@FXML
	private ChoiceBox<StatusContracttype> choiceStatusContracttype = new ChoiceBox<>();
	private IContracttype contractType = null;
	private SupportManagerController controller;

	public ContracttypeBeheerController(SupportManagerController controller) 
	{
		this.controller = controller;
	}
	
	@FXML
	private void initialize() 
	{
		Predicate<IContracttype> filterIsactief = c -> c.getIsActief().equals(StatusContracttype.Actief);
		controller.filterContract(filterIsactief);
		
		tableView.setItems( controller.geefFilteredContract());
		naamColumn.setCellValueFactory(e -> e.getValue().naam());
		tijdstipColumn.setCellValueFactory(e -> e.getValue().tijdStippenMogelijkheid());
		prijsColumn.setCellValueFactory(new PropertyValueFactory<IContracttype,Integer>("prijsContract"));
		contracttypeidColumn.setCellValueFactory(new PropertyValueFactory<IContracttype, Long>("id"));
		isactiefColumn.setCellValueFactory(e -> e.getValue().isActief());
		tableView.setPlaceholder(new Label("Geen contracttypes voor de gegeven filter"));
		choiceManier.setItems(controller.geefManierTicketAanamak());
		choiceTijdsttip.setItems(controller.geefTijdstipMogelijkheid());
		choiceManier.setValue(ManierTicketAanmaak.Applicatie);
		choiceTijdsttip.setValue(TijdstippenAanmaakTicket.Altijd24op7);
		choiceStatusContracttype.setItems(controller.geefStatussenContracttype());
		isActiefInput.setItems(controller.geefStatussenContracttype());
		isActiefInput.setValue(StatusContracttype.Actief);
		
		isActiefInput.setOnAction(e -> filtreer(e));
		
		lblError.setWrapText(true);
	}
	
	@FXML
	public void btnContracttypeToevoegenAfhandeling(ActionEvent event) throws  EntityBestaalAlException 
	{
		try {
			lblError.setText("");
			controller.voegContracttypeToe(txtNaam.getText(),choiceManier.getValue(),choiceTijdsttip.getValue(),txtMaxAfh.getText(),txtMinAfh.getText(),txtPrijs.getText());
			lblError.setText("");									
		}
		catch(VerplichtVeldenException e) {
			lblError.setText(String.format("%s %s", e.getMessage(),e.getInformatieVanNietIngevuldeElementen()));
		}
		catch (FouteInvoerException e) {
			lblError.setText(String.format("%s %s", e.getMessage(),e.getInformatieVanNietIngevuldeElementen()));
		}
		catch (RollbackException r ) {
			lblError.setText("Probleem met sql transactie");
		}
		catch (EntityBestaalAlException e) {
			lblError.setText(e.getMessage());
		}
	}
	
	@FXML
	public void getSelectedRow(MouseEvent event)
	{
		contractType = tableView.getSelectionModel().getSelectedItem();
		controller.setGeselecteerdeContracttype(controller.getContracttypesWithId(contractType.getId()));
		lblError.setText("");
		txtBehandeldeTickets.setVisible(true);
		lblBehandeldeTickets.setVisible(true);
		txtLopendeContracten.setVisible(true);
		lblLopendeContracten.setVisible(true);
		btnContracttypeWijzigen.setVisible(true);
		btnContracttypeVerwijderen.setVisible(true);
		btnContracttypeToevoegen.setVisible(false);
		labelContracttypeToevoegen.setVisible(false);
		lblContracttypeWijzigen.setVisible(true);
		choiceStatusContracttype.setVisible(true);
		lblStatusContracttype.setVisible(true);

		txtNaam.setText(contractType.getNaam()); 
		txtMaxAfh.setText(String.valueOf(contractType.getMaximaleAfhandeltijd()));
		txtMinAfh.setText(String.valueOf(contractType.getMaximaleAfhandeltijd()));
		txtPrijs.setText(String.valueOf(contractType.getPrijsContract()));
		choiceManier.setValue(contractType.getManierTicketAanmaak());
		choiceTijdsttip.setValue(contractType.getTijdStippenMogelijkheid());
		choiceStatusContracttype.setValue(contractType.getIsActief());
		txtLopendeContracten.setEditable(false);
		txtBehandeldeTickets.setEditable(false);
		txtLopendeContracten.setText(String.valueOf(controller.getAantalLopendeContracten()));
		txtBehandeldeTickets.setText(String.valueOf(controller.getAantalBehandeldeTickets()));
		System.out.println(contractType.getContracten().size());
		System.out.println(String.valueOf(contractType.getLopendeContracten()));
	}
	
	@FXML
	public void btnContracttypeWijzigenAfhandeling(ActionEvent event) throws EntityBestaalAlException  
	{
		try {
		controller.editContracttype(controller.getGeselecteerdeContracttype(), txtNaam.getText(), choiceManier.getValue(), choiceTijdsttip.getValue(),
				txtMaxAfh.getText(), txtMinAfh.getText(),
					txtPrijs.getText(),choiceStatusContracttype.getValue());
			lblError.setText("");
			
		lblError.setText("");
		zetKnoppenZichtbaar() ;
		filtreer(event);
		}
		catch(VerplichtVeldenException e) {
			lblError.setText(String.format("%s %s", e.getMessage(),e.getInformatieVanNietIngevuldeElementen()));
		}
		catch (FouteInvoerException e) {
			lblError.setText(String.format("%s %s", e.getMessage(),e.getInformatieVanNietIngevuldeElementen()));
		}
		catch (RollbackException r ) {
			lblError.setText("Probleem met sql transactie");
		}	
		catch (EntityBestaalAlException e) {
			lblError.setText(e.getMessage());
		}
	}
	
	@FXML
	public void btnContracttypeVerwijderenAfhandeling(ActionEvent event) 
	{
		controller.verwijderContracttype();
		zetKnoppenZichtbaar();
		filtreer(event);
	}

	@FXML
	public void setButtonToevoegen(MouseEvent event)
	{
		zetKnoppenZichtbaar();
	}
	
	public void zetKnoppenZichtbaar() 
	{
		lblError.setText("");
		txtBehandeldeTickets.setVisible(false);
		lblBehandeldeTickets.setVisible(false);
		txtLopendeContracten.setVisible(false);
		lblLopendeContracten.setVisible(false);
		btnContracttypeWijzigen.setVisible(false);
		btnContracttypeVerwijderen.setVisible(false);
		btnContracttypeToevoegen.setVisible(true);
		labelContracttypeToevoegen.setVisible(true);
		lblContracttypeWijzigen.setVisible(false);
		choiceStatusContracttype.setVisible(false);
		lblStatusContracttype.setVisible(false);
		txtNaam.setText(""); 
		txtMaxAfh.setText("");
		txtMinAfh.setText("");
		txtPrijs.setText("");
		choiceManier.setValue(ManierTicketAanmaak.Applicatie);
		choiceTijdsttip.setValue(TijdstippenAanmaakTicket.Altijd24op7);
	}
	
	public void filtreer(EventObject e) 
	{
		List<Predicate<IContracttype>> predicatesContracttype = new ArrayList<>();
		if(isActiefInput.getValue() != null) {
			Predicate<IContracttype> filterIsactief = c -> c.getIsActief().equals(isActiefInput.getValue());
			predicatesContracttype .add(filterIsactief);
		}		
			 
		Predicate<IContracttype> assembler = predicatesContracttype.stream().reduce(Predicate::and).orElse(x->true);
		controller.filterContract(assembler);
	}
}
