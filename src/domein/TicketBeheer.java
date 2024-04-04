package domein;

import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import domein.enums.StatusTicket;
import domein.enums.TypeTicket;
import exceptions.FouteInvoerException;
import exceptions.VerplichtVeldenException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import repository.GenericDaoJpa;
import repository.TicketDaoJpa;

public class TicketBeheer 
{
	private TicketDaoJpa ticketDao;
	private ObservableList<ITicket> tickets;
	private FilteredList<ITicket> filteredTickets;
	
	public TicketBeheer() 
	{
		this(new TicketDaoJpa());
	}
	
	public FilteredList<ITicket> getFilteredTickets() 
	{
		return filteredTickets;
	}

	public TicketBeheer(TicketDaoJpa ticketDao) 
	{
		this.ticketDao = ticketDao;
		tickets = FXCollections.observableArrayList(ticketDao.findAll());
		filteredTickets = new FilteredList<ITicket>(this.tickets);
	}
	
	public void voegTicketToe(Ticket ticket)
	{
		try {
			if(ticket==null) {
				throw new NullPointerException();
			}
			GenericDaoJpa.startTransaction();
			tickets.add(ticket);
			ticketDao.insert(ticket);
			GenericDaoJpa.commitTransaction();
		} catch(Exception e) {
			throw new IllegalArgumentException("Het is niet gelukt om het ticket toe te voegen");
		}
	}

	public Map<Integer,Long> geefTicketGesoorteerdOpMaand() 
	{
		Function<ITicket,Integer> filterPerMaand = t->t.getDatumAanmaak().getMonthValue();
		Map<Integer,Long> result = this.tickets.stream().collect(Collectors.groupingBy(filterPerMaand,Collectors.counting()));
		for(int i = 1; i <=12; i++) {
			if(!result.containsKey(i)) {
				result.put(i, (long) 0);
			}
		};
		return result;
	}
	
	public Map<StatusTicket,Long> geefTicketOpStatus()
	{
		Function<ITicket,StatusTicket> filterPerStatus = t -> t.getStatus();
		return this.tickets.stream().collect(Collectors.groupingBy(filterPerStatus,Collectors.counting()));
	}
	
	public void initializeerTickets()
	{
		tickets = FXCollections.observableArrayList(ticketDao.findAll());
	}
	
	public ObservableList<ITicket> geefAlleOpenstaandeTickets()
	{
		return tickets;
	}
	
	public ObservableList<ITicket> geefAlleAfgeslotenTickets()
	{
		return tickets;
	}
	
	public ObservableList<ITicket> geefAlleTickets()
	{
		return tickets;
	}
	
	public void filterTickets(Predicate<ITicket> filterPredicate){
		 filteredTickets.setPredicate(filterPredicate);
	}
	
	public Ticket geefTicketById(Long id)
	{
		return ticketDao.get(id);
	}
	
	public void verwijderTicket(Ticket t)
	{
		GenericDaoJpa.startTransaction();
		t.setStatus(StatusTicket.Geannuleerd);
		GenericDaoJpa.commitTransaction();
	}
	
	public void wijzigTicket(Ticket t, String titel, String toegewezenTechnieker, String klant, TypeTicket type, String omschrijving, String opmerking) throws VerplichtVeldenException, FouteInvoerException
	{
		try {
			GenericDaoJpa.startTransaction();
			t.edit(titel, toegewezenTechnieker, klant, type, omschrijving, opmerking);
			GenericDaoJpa.commitTransaction();
		} catch (VerplichtVeldenException e) {
			throw e;
		}
	}
}
