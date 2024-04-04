package gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import repository.ContractypeDaoJpa;
import repository.KlantDaoJpa;
import repository.TicketDaoJpa;
import repository.WerknemerDaoJpa;
import controllers.AdministratorController;
import controllers.LoginController;
import controllers.SupportManagerController;
import controllers.TechniekerController;
import domein.ContracttypeBeheerder;
import domein.GebruikerBeheerder;
import domein.KlantBeheerder;
import domein.TicketBeheer;
import javafx.event.ActionEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

public class SupportManagerSchermController extends AnchorPane
{
	@FXML
	private BorderPane mainPane;
	@FXML
	private HBox hBox = new HBox();
	@FXML
	private Label lblHallo = new Label();
	private ImageView ivImage;
	
	private SupportManagerController controller;
	private TechniekerController techniekerController;

	public SupportManagerSchermController(SupportManagerController controller, TechniekerController tController) 
	{
		super();
		this.controller = controller;
		this.techniekerController = tController;
		try 
		{
			FXMLLoader loader = new FXMLLoader(getClass().getResource("SupportManagerScherm.fxml"));
			loader.setController(this);
			loader.setRoot(this);
			loader.load();
			FxmlLoader object = new FxmlLoader();
			ContracttypeBeheerController cbc = new ContracttypeBeheerController(controller);
			Pane view = object.getPage("ContracttypeBeheer", cbc);
			ivImage = new ImageView	(new Image(getClass().getResourceAsStream("/images/Actemium.png")));
			ivImage.setFitWidth(200);
			ivImage.setFitHeight(52);
			hBox.getChildren().add(0, ivImage);
			hBox.setAlignment(Pos.CENTER);
			hBox.setSpacing(75);
			mainPane.setCenter(view);
		} catch (Exception ex) 
		{
			System.out.println(ex.getMessage());
		}
		lblHallo.setText("Hallo " + controller.getWerknemer().getRol() + " " + controller.getWerknemer().getUserName());
	}
	
	@FXML
	public void btnAfhandelingBeheerContractype(ActionEvent event) 
	{
		FxmlLoader object = new FxmlLoader();
		ContracttypeBeheerController cbc = new ContracttypeBeheerController(controller);
		Pane view = object.getPage("ContracttypeBeheer", cbc);
		mainPane.setCenter(view);
	}
	
	@FXML
	public void btnAfhandelingBeheerKPI(ActionEvent event) 
	{
		FxmlLoader object = new FxmlLoader();
		GrafiekController gf = new GrafiekController(controller);
		Pane view = object.getPage("Grafiek", gf);
		mainPane.setCenter(view);
	}
	
	@FXML
	public void btnOpenstaandeTickets(ActionEvent event) 
	{
		FxmlLoader object = new FxmlLoader();
		OpenstaandeTicketsSupportManagerController dbs = new OpenstaandeTicketsSupportManagerController(techniekerController,controller);
		Pane view = object.getPage("OpenstaandeTicketsSupportManager", dbs);
		mainPane.setCenter(view);
	}
	
	@FXML
	public void btnAfgehandeldeTickets(ActionEvent event) 
	{
		FxmlLoader object = new FxmlLoader();
		AfgeslotenTicketsSupportManagerController dbs = new AfgeslotenTicketsSupportManagerController(techniekerController,controller);
		Pane view = object.getPage("AfgeslotenTicketsSupportManager", dbs);
		mainPane.setCenter(view);
	}
	
	@FXML
	public void btnAfmelden(ActionEvent event)
	{
		GebruikerBeheerder gebruikerBeheerder = new GebruikerBeheerder(new WerknemerDaoJpa());
		KlantBeheerder klantenBeheer = new KlantBeheerder(new KlantDaoJpa());
		TicketBeheer ticketBeheer = new TicketBeheer(new TicketDaoJpa());
		ContracttypeBeheerder contracttypeBeheer = new ContracttypeBeheerder(new ContractypeDaoJpa());
		LoginSchermController root = new LoginSchermController(new LoginController(), new AdministratorController(klantenBeheer, gebruikerBeheerder),
				new TechniekerController(klantenBeheer, gebruikerBeheerder, ticketBeheer), new SupportManagerController(contracttypeBeheer,ticketBeheer));
		Scene scene = new Scene(root, 380, 645);
		Stage stage = (Stage) this.getScene().getWindow();
		stage.setX(500);
		stage.setY(50);
		stage.setMaximized(false);
		stage.setTitle("Actemium");
		stage.setScene(scene);
		stage.show();;
	}
}
