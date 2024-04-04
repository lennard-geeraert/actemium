package main;

import java.time.LocalDate;
import java.time.Month;

import controllers.AdministratorController;
import controllers.LoginController;
import controllers.SupportManagerController;
import controllers.TechniekerController;
import domein.ContactPersoon;
import domein.Contract;
import domein.Contracttype;
import domein.ContracttypeBeheerder;
import domein.GebruikerBeheerder;
import domein.ITicket;
import domein.IWerknemer;
import domein.Klant;
import domein.KlantBeheerder;
import domein.Ticket;
import domein.TicketBeheer;
import domein.Werknemer;
import domein.enums.ManierTicketAanmaak;
import domein.enums.RolWerknemer;
import domein.enums.StatusContract;
import domein.enums.StatusContracttype;
import domein.enums.StatusTicket;
import domein.enums.TijdstippenAanmaakTicket;
import domein.enums.TypeTicket;
import exceptions.EntityBestaalAlException;
import exceptions.FouteInvoerException;
import exceptions.VerplichtVeldenException;
import gui.LoginSchermController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import repository.ContractypeDaoJpa;
import repository.KlantDaoJpa;
import repository.TicketDaoJpa;
import repository.WerknemerDaoJpa;

public class StartUp extends Application 
{
	public static final String Style = "style.css";
	private AdministratorController adminController;
	private TechniekerController techniekerController;
	private SupportManagerController supportController;
	
	@Override
	public void start(Stage primaryStage) throws VerplichtVeldenException, EntityBestaalAlException, FouteInvoerException 
	{	
		GebruikerBeheerder gebruikerBeheerder = new GebruikerBeheerder(new WerknemerDaoJpa()); 
		KlantBeheerder klantenBeheer = new KlantBeheerder(new KlantDaoJpa());
		TicketBeheer ticketBeheer = new TicketBeheer(new TicketDaoJpa());
		ContracttypeBeheerder contracttypeBeheer = new ContracttypeBeheerder(new ContractypeDaoJpa());
		this.adminController = new AdministratorController(klantenBeheer, gebruikerBeheerder);
		this.techniekerController = new TechniekerController(klantenBeheer, gebruikerBeheerder, ticketBeheer);
		this.supportController = new SupportManagerController(contracttypeBeheer,ticketBeheer);
		LoginSchermController root = new LoginSchermController(new LoginController(), adminController, techniekerController, supportController);
		initialize();
		Scene scene = new Scene(root, 380, 645);
		Image icon = new Image(getClass().getResourceAsStream("/images/Actemium-sidebar.png"));
		primaryStage.getIcons().add(icon);
		
		scene.getStylesheets().add(Style);
		root.getStylesheets().add(Style);		
		primaryStage.setTitle("Actemium");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public static void main(String[] args) 
	{
		launch(args);
	}
	
	private void initialize() throws VerplichtVeldenException, EntityBestaalAlException, FouteInvoerException
	{
		
		//werknemers toevoegen
		IWerknemer w1 = maakWerknemerAanEnVoegToe("Lennard", "Geeraert", "password", LocalDate.of(2000, Month.DECEMBER, 12), "lenn", RolWerknemer.Administrator, "lenn@hogent.be", "Brussel","0481/041472");
		Werknemer w2 = maakWerknemerAanEnVoegToe("Mamadou", "Sow", "password", LocalDate.of(1995, Month.APRIL, 22), "sow", RolWerknemer.Technieker, "sow@hogent.be", "Leuven","0481/041472");
		IWerknemer w3 = maakWerknemerAanEnVoegToe("Natanael", "Dobie", "password", LocalDate.of(1987, Month.MARCH, 1), "them", RolWerknemer.SupportManager, "them@hogent.be", "Aalst","0481/041472");
		Werknemer w4 = maakWerknemerAanEnVoegToe("Ismail", "Assakali", "password", LocalDate.of(1966, Month.FEBRUARY, 12), "ismail", RolWerknemer.Technieker, "ismail@hogent.be", "Gent","0481/041472");		
		
		//klanten toevoegen
		Klant k1 = new Klant("Coca-Cola", "Cola", "password", "Coca-cola@coce.com", "Hasselt", LocalDate.of(1966, Month.FEBRUARY, 12),"0481/041472");
		k1.addContactPersoon(new ContactPersoon("Jan", "De Smedt","jan.desmedt@coce.com"));
		k1.addContactPersoon(new ContactPersoon("Lars", "Hopper","lars.hopper@coce.com"));
		k1.addContactPersoon(new ContactPersoon("Bart", "Jansens","bart.jansens@coce.com"));
		adminController.voegKlantToe(k1);
		Klant k2 = new Klant("Daimler", "Daim", "password", "Daimler@Mercedes.com", "Dendermonde", LocalDate.of(2000, Month.APRIL, 1),"0481/041472");
		k2.addContactPersoon(new ContactPersoon("Lisa", "Fargee","lisa.fargee@mercedes.com"));
		adminController.voegKlantToe(k2);
		Klant k3 = new Klant("Colruyt", "Colr", "password", "Colruyt@col.be", "Luik", LocalDate.of(1968, Month.AUGUST, 2),"0481/041472");
		k3.addContactPersoon(new ContactPersoon("Jana", "Debruyke","jana.debruyke@col.be"));
		adminController.voegKlantToe(k3);
		Klant k4 = new Klant("Unilever", "Uni", "password", "Unilever@gmail.com", "Tienen", LocalDate.of(1999, Month.DECEMBER, 23),"0481/041472");
		k4.addContactPersoon(new ContactPersoon("Geert", "Grassers","geert.grassers@gmail.com"));
		k4.addContactPersoon(new ContactPersoon("Jonas", "Willems","jonas.willems@gamil.com"));
		adminController.voegKlantToe(k4);
		Klant k5 = new Klant("Delhaize", "Del", "password", "Delhaize@hotmail.com", "Hasselt", LocalDate.of(1986, Month.JULY, 25),"0481/041472");
		k5.addContactPersoon(new ContactPersoon("Sarah", "Van leem huizen","sarah.vanleemhuizen@hotmail.com"));
		adminController.voegKlantToe(k5);
		
		//tickets toevoegen

		Contracttype type1 = new Contracttype.Builder().naam("ContracttypeA").manierTicketAanmaak(ManierTicketAanmaak.Applicatie)
				.tijdstippenMogelijkheid(TijdstippenAanmaakTicket.Altijd24op7).maximaleAfhandeltijd("10").minimaleAfhandeltijd("5").prijsContract("200")
				.build();
		
		Contracttype type2 = new Contracttype.Builder().naam("ContracttypeB").manierTicketAanmaak(ManierTicketAanmaak.Telefonisch)
				.tijdstippenMogelijkheid(TijdstippenAanmaakTicket.Van8tot17OpWerkdagen).maximaleAfhandeltijd("12").minimaleAfhandeltijd("4").prijsContract("50")
				.build();
		Contracttype type3 = new Contracttype.Builder().naam("ContracttypeC").manierTicketAanmaak(ManierTicketAanmaak.Telefonisch)
				.tijdstippenMogelijkheid(TijdstippenAanmaakTicket.Van8tot17OpWerkdagen).maximaleAfhandeltijd("12").minimaleAfhandeltijd("4").prijsContract("50")
				.build();
		type3.setIsActief(StatusContracttype.NietActief);
		Contract contract1 = new Contract( 2, LocalDate.now());
		Contract contractc2 = new Contract(3,LocalDate.now(),StatusContract.Lopend);
		Contract contractc3 = new Contract(3,LocalDate.now(),StatusContract.InAanvrag);
		k1.addContract(contract1);
		k1.addContract(contractc2);
		k2.addContract(contractc2);
		k4.addContract(contractc2);
		k5.addContract(contractc2);
		k5.addContract(contractc3);
		type1.addContract(contract1);
		type1.addContract(contractc2);
				
		supportController.voegContracttypeToe(type1.getNaam(),type1.getManierTicketAanmaak(),type1.getTijdStippenMogelijkheid(),
				String.valueOf(type1.getMaximaleAfhandeltijd()),String.valueOf(type1.getMinimaleAfhandeltijd()),String.valueOf(type1.getPrijsContract()));
		supportController.voegContracttypeToe(type2.getNaam(),type2.getManierTicketAanmaak(),type2.getTijdStippenMogelijkheid(),
				String.valueOf(type2.getMaximaleAfhandeltijd()),String.valueOf(type2.getMinimaleAfhandeltijd()),String.valueOf(type2.getPrijsContract()));
		supportController.voegContracttypeToe(type3.getNaam(),type3.getManierTicketAanmaak(),type3.getTijdStippenMogelijkheid(),
				String.valueOf(type3.getMaximaleAfhandeltijd()),String.valueOf(type3.getMinimaleAfhandeltijd()),String.valueOf(type3.getPrijsContract()));
		System.out.println(type1.getAantalBehandeldeTickets());
		System.out.println(type1.getLopendeContracten() + " kjebfkhfzb");

		Ticket t1 = maakTicketEnVoegToe("Probleem printer", w2.getUserName(), TypeTicket.Hardware, "Printer drukt alles dubbel","", k1.getUserName(), StatusTicket.Afgehandeld,w2);
		Ticket t2 = maakTicketEnVoegToe("Probleem met computer", w2.getUserName(), TypeTicket.Hardware, "Ram maakt veel geluid","", k1.getUserName(), StatusTicket.InBehandeling,w2);
		ITicket t3 = maakTicketEnVoegToe("Systeem crasht", w2.getUserName(), TypeTicket.Software, "Heel het systeem ligt plat door een error in de code", "", k2.getUserName(), StatusTicket.InBehandeling,w4);
		ITicket t4 = maakTicketEnVoegToe("Update vereist", w4.getUserName(), TypeTicket.Software, "Een nieuwe update is vereist, zodat het systeem terug werkt", "", k4.getUserName(), StatusTicket.Aangemaakt,w4);
		ITicket t5 = maakTicketEnVoegToe("Scherm kleurt paars", w4.getUserName(), TypeTicket.Hardware, "Telkens wanneer de pc's opgestart worden kleueren de schermen paars","", k4.getUserName(), StatusTicket.Geannuleerd,w4);
		ITicket t6 = maakTicketEnVoegToe("Geluid werkt niet", w4.getUserName(), TypeTicket.Hardware, "Het geluid van de speakers in de winkel werkt niet meer", "", k5.getUserName(), StatusTicket.InBehandeling,w4);
		
		contract1.addTickets(t1);
		contract1.addTickets(t2);
	}
	
	private Werknemer maakWerknemerAanEnVoegToe(String firstName, String lastName, String password, LocalDate date, String userName, 
			RolWerknemer rol, String email, String adres,String tel)
	{
		Werknemer w1 = new Werknemer(firstName, lastName, password, date, userName, rol, adres, email,tel);
		adminController.voegWerknemerToe(w1.getFirstName(), w1.getLastName(), w1.getPassword(), w1.getRol(), w1.getDatumRegistratie(), w1.getUserName(), w1.getAdres(), w1.getEmail(),w1.getTelefoonummer());
		return w1;
	}
	
	private Ticket maakTicketEnVoegToe(String titel, String toegewezenTechnieker, TypeTicket type, String omschrijving, 
			String opmerking, String bedrijfKlant, StatusTicket status,IWerknemer werknemer) throws VerplichtVeldenException, FouteInvoerException, EntityBestaalAlException
	{
		Ticket t = new Ticket(titel, toegewezenTechnieker, type, omschrijving, opmerking, bedrijfKlant, status);
		
		techniekerController.setWerknemer(werknemer);
		techniekerController.setLists();
		techniekerController.voegTicketToe(titel, toegewezenTechnieker, type, omschrijving, opmerking, bedrijfKlant, status);
		return t;
	}
}
