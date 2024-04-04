package controllers;

import java.util.function.Predicate;
import java.util.stream.Collectors;

import domein.GebruikerBeheerder;
import domein.ITicket;
import domein.IWerknemer;
import domein.Klant;
import domein.KlantBeheerder;
import domein.Ticket;
import domein.TicketBeheer;
import domein.Werknemer;
import domein.enums.RolWerknemer;
import domein.enums.StatusTicket;
import domein.enums.TypeTicket;
import exceptions.EntityBestaalAlException;
import exceptions.FouteInvoerException;
import exceptions.VerplichtVeldenException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;

public class TechniekerController implements Controller
{
	private TicketBeheer ticketBeheer;
	private FilteredList<ITicket> filteredList;
	private ObservableList<ITicket> ticketWerknemerList;
	private GebruikerBeheerder gebruikerBeheerder;
	private KlantBeheerder klantBeheerder;
	private IWerknemer werknemer;
	private Ticket geselecteerdeT;
	
	public TechniekerController(KlantBeheerder klantenBeheer, GebruikerBeheerder gebruikerBeheerder, TicketBeheer ticketBeheer) 
	{
		this.ticketBeheer = ticketBeheer;
		this.gebruikerBeheerder = gebruikerBeheerder;
		this.klantBeheerder = klantenBeheer;
	}
	
	public void voegTicketToe(String titel, String toegewezenTechnieker, TypeTicket type, String omschrijving, 
			String opmerking, String bedrijfKlant, StatusTicket status)throws VerplichtVeldenException, FouteInvoerException, EntityBestaalAlException
	{
		Ticket tt = new Ticket.Builder().titel(titel).toegewezenTechnieker(toegewezenTechnieker).type(type).omschrijving(omschrijving)
				.opmerking(opmerking).bedrijfKlant(bedrijfKlant).status(status).build();
		Klant k = klantBeheerder.geefKlantMetNaam(bedrijfKlant);
		
		if(toegewezenTechnieker != null) {
			Werknemer w = gebruikerBeheerder.geefWerknemerMetNaam(toegewezenTechnieker);
			w.addTicket(tt);
		}
		if(!k.heeftKlantActiefContract()) {
			throw new EntityBestaalAlException("Ticket kan niet toegevoegd worden omdat klant geen actief contract heeft");
		}
		
		k.addTicket(tt);
	
		this.ticketWerknemerList.add(tt);
		ticketBeheer.voegTicketToe(tt);
	}
	
	public void voegTicket(Ticket t) throws VerplichtVeldenException
	{
		Klant k = klantBeheerder.geefKlantMetNaam(t.getBedrijfKlant());
		Werknemer w = gebruikerBeheerder.geefWerknemerMetNaam(t.getToegewezenTechnieker());
		
		k.addTicket(t);
		w.addTicket(t);
		ticketBeheer.voegTicketToe(t);
	}
	
	public ObservableList<ITicket> geefOpenstaandeTicketsVanWerknemer()
	{
		return (ObservableList<ITicket>) (Object) werknemer.getTickets();
	}
	
	public void setLists() {

		this.ticketWerknemerList = (ObservableList<ITicket>) (Object) werknemer.getTickets(); 
		this.filteredList = new FilteredList<>(this.ticketWerknemerList,p->true);
	}
	
	public FilteredList<ITicket> geefFilteredListVanTickets(){
		return this.filteredList;
	}
	
	public void filterTickets(Predicate<ITicket> filterPredicate){
		 filteredList.setPredicate(filterPredicate);
	}
	
	
	public ObservableList<ITicket> geefAfgehandeldeTicketsVanWerknemer()
	{
		return (ObservableList<ITicket>) (Object) werknemer.getTickets();
	}
	
	public ObservableList<ITicket> geefAlleOpenstaandeTickets()
	{
		return ticketBeheer.geefAlleOpenstaandeTickets();
	}
	
	public ObservableList<ITicket> geefAlleAfgeslotenTickets()
	{
		return ticketBeheer.geefAlleAfgeslotenTickets();
	}

	public void initializeerTickets()
	{
		ticketBeheer.initializeerTickets();
	}
	
	public ObservableList<StatusTicket> geefAlleStatussen()
	{
		return FXCollections.observableArrayList(StatusTicket.Aangemaakt, StatusTicket.Afgehandeld, StatusTicket.Geannuleerd, StatusTicket.InBehandeling);
	}
	
	public ObservableList<TypeTicket> geefAlleTypes()
	{
		return FXCollections.observableArrayList(TypeTicket.Hardware, TypeTicket.Software, TypeTicket.Algemeen);
	}
	
	public ObservableList<String> geefAlleNamenVanTechniekers()
	{		
		return FXCollections.observableArrayList(gebruikerBeheerder.getAlleWerknemers().filtered(w -> w.getRol().equals(RolWerknemer.Technieker)).stream().map(w -> w.getUserName()).collect(Collectors.toList()));
	}
	
	public ObservableList<String> geefAlleNamenVanKlanten()
	{
		return FXCollections.observableArrayList(klantBeheerder.geefAlleKlanten().stream().map(k -> k.getUserName()).collect(Collectors.toList()));
	}
	
	public Ticket geefTicketById(Long id)
	{
		return ticketBeheer.geefTicketById(id);
	}
	
	public void verwijderTicket(Ticket t)
	{		
		ticketBeheer.verwijderTicket(t);
	}
	
	public void wijzigTicket(Ticket t, String titel, String toegewezenTechnieker, String klant, TypeTicket type, String omschrijving, String opmerking) throws VerplichtVeldenException, FouteInvoerException, EntityBestaalAlException
	{	
		Klant k = klantBeheerder.geefKlantMetNaam(klant);
		if(!k.heeftKlantActiefContract()) {
			throw new EntityBestaalAlException("Ticket kan niet toegevoegd worden omdat klant geen actief contract heeft");
		}
		
		ticketBeheer.wijzigTicket(t, titel, toegewezenTechnieker, klant, type, omschrijving, opmerking);
	}
 
	@Override
	public void setWerknemer(IWerknemer w) 
	{
		this.werknemer = w;
	}

	@Override
	public IWerknemer getWerknemer() 
	{
		return werknemer;
	}

	public Ticket getGeselecteerdeT() 
	{
		return geselecteerdeT;
	}

	public void setGeselecteerdeT(Ticket geselecteerdeT) 
	{
		this.geselecteerdeT = geselecteerdeT;
	}
	
	public Klant geefKlantByUserName(String username) 
	{
		Klant k = klantBeheerder.geefKlantMetNaam(username);
		return k;
	}
}
