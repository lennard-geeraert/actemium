package controllers;

import java.time.LocalDate;
import java.util.function.Predicate;

import domein.ContactPersoon;
import domein.GebruikerBeheerder;
import domein.IContactPersoon;
import domein.IKlant;
import domein.IWerknemer;
import domein.Klant;
import domein.KlantBeheerder;
import domein.Werknemer;
import domein.enums.RolWerknemer;
import domein.enums.StatusGebruiker;
import exceptions.FouteInvoerException;
import exceptions.VerplichtVeldenException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;

public class AdministratorController implements Controller
{
	private KlantBeheerder klantberheerder;
	private GebruikerBeheerder gebruikerBeheerder;
	private IWerknemer w;
	private Werknemer geselecteerdeW;
	private Klant geselecteerdeK;
	private IContactPersoon geselecteerdeCP;
	private Klant bijnaToegevoegdeklant;
	
	public AdministratorController(KlantBeheerder klantenBeheer, GebruikerBeheerder gebruikerBeheerder) 
	{
		this.klantberheerder = klantenBeheer;
		this.gebruikerBeheerder = gebruikerBeheerder;
	}
	
	public Klant geefKlantById(long id) 
	{
		return klantberheerder.geefKlantMetId(id);
	}
	
	public IKlant geefKlantMetNaam(String userName) 
	{
		return klantberheerder.geefKlantMetNaam(userName);
	}
	
	public ObservableList<IKlant> geefAlleKlanten()
	{
		return (ObservableList<IKlant>) (Object) klantberheerder.geefAlleKlanten();
	}
	
	public FilteredList<IKlant> getFilteredKlantList(){
		return this.klantberheerder.getFilteredList();
	}
	
	public void filterKlant(Predicate<IKlant> prek) {
		this.klantberheerder.filterKlant(prek);
	}
	
	public void voegKlantToeZonderAanObservableListToeTeVoegen(String name, String userName, String password, String email, String adres, LocalDate date,String tel)throws VerplichtVeldenException, FouteInvoerException
	{
		for (IKlant k : klantberheerder.geefAlleKlanten()) {
			if(k.getUserName().equals(userName))
				throw new IllegalArgumentException("Er bestaat al een klant met deze gebruikersnaam");
		}
		Klant k = new Klant.Builder().naam(name).userName(userName).password(password)
				.email(email).adres(adres).datumReg(date)
				.telefoonnummers(tel).build();
		setBijnaToegevoegdeklant(k);
	}
	
	public void voegKlantToeAanObservableList(String name, String userName, String password, String email, String adres, StatusGebruiker status,String tel) throws VerplichtVeldenException, FouteInvoerException
	{
		if(!bijnaToegevoegdeklant.getUserName().equals(userName)) {
			for (IKlant k : klantberheerder.geefAlleKlanten()) {
				if(k.getUserName().equals(userName))
					throw new IllegalArgumentException("Er bestaat al een klant met deze gebruikersnaam");
			}
		}
		klantberheerder.editKlant(bijnaToegevoegdeklant, name, userName, password, email, adres, status,tel);
		klantberheerder.voegKlantToe(bijnaToegevoegdeklant);
	}
	
	public void kijkOfKlantContactPersoonHeeft() throws VerplichtVeldenException
	{
		if(bijnaToegevoegdeklant == null)
			throw new VerplichtVeldenException("Een klant moet minstens 1 contactpersoon hebben");
		if(bijnaToegevoegdeklant.getContactPersonen().isEmpty() || bijnaToegevoegdeklant.getContactPersonen() == null)
			throw new VerplichtVeldenException("Een klant moet minstens 1 contactpersoon hebben");
	}
	
	public void voegKlantToe(Klant k) 
	{
		klantberheerder.voegKlantToe(k);
	}
	
	public void EditKlant(String name, String userName, String password, String email, String adres, StatusGebruiker status,String tel) throws VerplichtVeldenException, FouteInvoerException
	{
		if(!geselecteerdeK.getUserName().equals(userName)) {
			for (IKlant k : klantberheerder.geefAlleKlanten()) {
				if(k.getUserName().equals(userName))
					throw new IllegalArgumentException("Er bestaat al een klant met deze gebruikersnaam");
			}
		}
		klantberheerder.editKlant(geselecteerdeK, name, userName, password, email, adres, status,tel);
	}
	
	public void verwijderKlant()
	{
		klantberheerder.editStatusKlant(geselecteerdeK, StatusGebruiker.Geblokkeerd);
	}
	
	public Klant getGeselecteerdeK() {
		return geselecteerdeK;
	}

	public void setGeselecteerdeK(Klant geselecteerdeK) {
		this.geselecteerdeK = geselecteerdeK;
	}
	
	public ObservableList<IContactPersoon> geefContactPersonenVanKlant()
	{
		return (ObservableList<IContactPersoon>) (Object) klantberheerder.geefContactPersonenVanKlant(geselecteerdeK);
	}
	
	public void voegContactPersoonToe(String email, String firstName, String lastName)
	{
		ContactPersoon cp = new ContactPersoon(firstName, lastName, email);
		klantberheerder.voegContactPersoonToe(cp, geselecteerdeK);
	}
	
	public void voegContactPersoonAanGeselecteerdeKlant(ContactPersoon persoon)
	{
		klantberheerder.voegContactPersoonToe(persoon, geselecteerdeK);
	}
	
	public void voegContactPersoonAanBijnaToegevoegdeKlant(ContactPersoon persoon)
	{
		klantberheerder.voegCPToeAanBijnaToegevoegdeKlant(bijnaToegevoegdeklant, persoon);
	}
	
	public void initialiseerContactpersonen()
	{
		klantberheerder.initialiseerContactpersonen(geselecteerdeK);
	}
	
	public void verwijderContactPersoon(String email) throws VerplichtVeldenException
	{
		klantberheerder.verwijderContactPersoon(geselecteerdeK, email);
	}
	
	public IContactPersoon getGeselecteerdeCP() 
	{
		return geselecteerdeCP;
	}

	public void setGeselecteerdeCP(IContactPersoon geselecteerdeCP) 
	{
		this.geselecteerdeCP = geselecteerdeCP;
	}
	
	public Klant getBijnaToegevoegdeklant() {
		return bijnaToegevoegdeklant;
	}

	public void setBijnaToegevoegdeklant(Klant bijnaToegevoegdeklant) {
		this.bijnaToegevoegdeklant = bijnaToegevoegdeklant;
	}
	
	//-------------------------------------------------------------------------------------
	
	public Werknemer geefWerknemerById(long id) 
	{
		return gebruikerBeheerder.geefWerknemerById(id);
	}
	
	public void voegWerknemerToe(String firstName, String lastName, String password, RolWerknemer rol, LocalDate date, String userName, String adres, String email,String tel)
	{
		Werknemer w = new Werknemer(firstName, lastName, password, date, userName, rol, adres, email,tel);
		gebruikerBeheerder.voegWerknemerToe(w);
	}
	
	public void voegWerknemer(String firstName, String lastName, String password,RolWerknemer rol, LocalDate date, String userName, 
			 String adres, String email,String telefoonnummers) throws VerplichtVeldenException, FouteInvoerException 
	{
		for (IWerknemer w : gebruikerBeheerder.getAlleWerknemers()) {
			if(w.getUserName().equals(userName))
				throw new IllegalArgumentException("Er bestaat al een werknemer met deze gebruikersnaam");
		}
		Werknemer w = new Werknemer.Builder().firstname(firstName).lastname(lastName)
				.password(password).rol(rol).datumReg(date)
				.userName(userName).adres(adres).email(email)
				.telefoonnummers(telefoonnummers).build();
		
		gebruikerBeheerder.voegWerknemerToe(w);
	}
	
	public void verwijderWerknemer() 
	{
		gebruikerBeheerder.verwijderWerknemer(geselecteerdeW);
	}
	
	public void editWerknemer(String firstName, String lastName, String password, RolWerknemer rol, String userName, 
			String adres, String email, StatusGebruiker status,String tel) throws VerplichtVeldenException, FouteInvoerException
	{
		if(!geselecteerdeW.getUserName().equals(userName)) {
			for (IWerknemer w : gebruikerBeheerder.getAlleWerknemers()) {
				if(w.getUserName().equals(userName))
					throw new IllegalArgumentException("Er bestaat al een werknemer met deze gebruikersnaam");
			}
		}
		gebruikerBeheerder.editWerknemer(geselecteerdeW, firstName, lastName, password, rol, userName, adres, email, status,tel);
	}
	
	public StatusGebruiker geefStatusWerknemer(String userName)
	{
		return gebruikerBeheerder.geefStatusWerknemer(userName);
	}
	
	public ObservableList<IWerknemer> geefAlleWerknemers()
	{
		return (ObservableList<IWerknemer>) (Object) gebruikerBeheerder.getAlleWerknemers();
	}
	
	public FilteredList<IWerknemer> getFilteredWerknemerList(){
		return this.gebruikerBeheerder.getFilteredWerknemerList();
	}
	
	public void filterWerknemer(Predicate<IWerknemer> prew) {
		this.gebruikerBeheerder.filterWerknemer(prew);
	}
	
	public ObservableList<RolWerknemer> geefRollenWerknemer()
	{
		return FXCollections.observableArrayList(RolWerknemer.Administrator, RolWerknemer.SupportManager, RolWerknemer.Technieker);
	}
	
	public ObservableList<StatusGebruiker> geefStatussenGebruiker()
	{
		return FXCollections.observableArrayList(StatusGebruiker.Actief, StatusGebruiker.Geblokkeerd);
	}

	@Override
	public void setWerknemer(IWerknemer w) 
	{
		this.w = w;
	}

	@Override
	public IWerknemer getWerknemer() 
	{
		return w;
	}

	public Werknemer getGeselecteerdeW() 
	{
		return geselecteerdeW;
	}

	public void setGeselecteerdeW(Werknemer geselecteerdeW) 
	{
		this.geselecteerdeW = geselecteerdeW;
	}
}
