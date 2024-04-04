package testen;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import domein.ITicket;
import domein.Ticket;
import domein.TicketBeheer;
import domein.enums.StatusTicket;
import domein.enums.TypeTicket;
import exceptions.FouteInvoerException;
import exceptions.VerplichtVeldenException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import repository.TicketDao;
import repository.TicketDaoJpa;

@ExtendWith(MockitoExtension.class)
public class TicketBeheerTest 
{	
	@Mock
	TicketDao ticketDaoDummy;
	@Mock
	private TicketDaoJpa ticketDaoJpa;
	@InjectMocks
	private TicketBeheer ticketbeheer;
	
	private List<Ticket> tickets;
	private ObservableList<ITicket> tickettList;
	private FilteredList<ITicket> filterTicket;
	
	@BeforeEach
	public void before() 
	{
		Ticket tickets[] = new Ticket[] {new Ticket("Ticket titel2","ismail",TypeTicket.Hardware,"Voorbeeldtext","geen opmerking","Coca-cola",StatusTicket.Geannuleerd),
				new Ticket("Ticket titel3","sow",TypeTicket.Hardware,"Voorbeeldtext","geen opmerking","Pepsico",StatusTicket.Aangemaakt),
				new Ticket("Grens_","them",TypeTicket.Hardware,"Voorbeeldtext","Jehebter10_","Ikea",StatusTicket.Aangemaakt)};
		List<Ticket> ticketsList = Arrays.asList(tickets); 
		ticketDaoDummy = ticketDaoJpa;
		this.tickets = ticketsList;
		this.tickettList = FXCollections.observableArrayList(this.tickets);
		this.filterTicket = new FilteredList<ITicket>(this.tickettList, t -> true);
		Mockito.when(ticketDaoDummy.findAll()).thenReturn(this.tickets);
		ticketbeheer = new TicketBeheer(ticketDaoJpa);
	}
	
	@Test
	public void testCtor() {
		ObservableList<ITicket> oblist = ticketbeheer.geefAlleTickets();
		assertEquals(3,oblist.size());
		Mockito.verify(ticketDaoDummy,Mockito.times(2)).findAll();
		
	}
	
	@ParameterizedTest
	@MethodSource("filterTestArguments")
	public void testOpFilter(StatusTicket st, int aantalTicketsVolgensStatus) {
		Predicate<ITicket> predicateITicket = t -> t.getStatus().equals(st);
		ticketbeheer.filterTickets(predicateITicket);
		
		assertEquals(aantalTicketsVolgensStatus, ticketbeheer.getFilteredTickets().size());
		
	}
	
	private static Stream<Arguments> filterTestArguments(){
		return Stream.of(
				Arguments.of(StatusTicket.Aangemaakt,2),
				Arguments.of(StatusTicket.Geannuleerd,1)
				);
	}
	
	@ParameterizedTest
	@NullSource
	public void voegTicketToe_ticketIsNull(Ticket ticket) {
		assertThrows(IllegalArgumentException.class, ()->{
			ticketbeheer.voegTicketToe(ticket);
		});
		
		Mockito.verify(ticketDaoDummy,Mockito.never()).insert(ticket);
	}
	
	@ParameterizedTest
	@MethodSource("geldigeTickets")
	public void voegTicketToe_gelidgeTicket(Ticket ticket) {
		ticketbeheer.voegTicketToe(ticket);
		assertEquals(4, ticketbeheer.geefAlleTickets().size());
	}
	
	private static Stream<Arguments> geldigeTickets(){
		return Stream.of(
				Arguments.of(new Ticket("Ticket titel4","sow",TypeTicket.Software,"Voorbeelderman","geen opmerking","Fanta",StatusTicket.Aangemaakt)),
				Arguments.of(new Ticket("Ticket","ismail",TypeTicket.Software,"IkTestTicket","geen opmerking","Pepsi",StatusTicket.Aangemaakt))
				);
	}
	
	@ParameterizedTest
	@MethodSource("geldigeDataVoorEdit")
	public void wijzigTicket_geldigeGegevens(Ticket t,String titel,String toegewezenTechnieker, String klant, TypeTicket type, String omschrijving, String opmerking) throws VerplichtVeldenException, FouteInvoerException {
		
		ticketbeheer.wijzigTicket(t,titel, toegewezenTechnieker, klant, type, omschrijving, opmerking);
		assertEquals(t.getTitel(), titel);
	}
	
	private static Stream<Arguments> geldigeDataVoorEdit(){
		
		return Stream.of(				
				Arguments.of(new Ticket("Ticket titel4","sow",TypeTicket.Software,"Voorbeelderman","geen opmerking","Fanta",StatusTicket.Aangemaakt),"Ticket editvoorbeeld","sow","Jansens",TypeTicket.Hardware,"Voorbeeldtext","geen opmerking"),
				Arguments.of(new Ticket("Ticket titel5","sow",TypeTicket.Software,"Voorbeelderman","geen opmerking","Fanta",StatusTicket.Aangemaakt),"Grens_","lenn","Jansens",TypeTicket.Hardware,"Voorbeeldtext","Jehebter10_")
				);
	}
	
	@ParameterizedTest
	@MethodSource("geldigeTickets")
	public void verwijder_gelidgeTicket(Ticket ticket) {
		ticketbeheer.verwijderTicket(ticket);
		assertEquals(StatusTicket.Geannuleerd, ticket.getStatus());
	}	
}
