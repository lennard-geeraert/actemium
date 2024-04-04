package testen;

import java.time.LocalDate;
import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;

import domein.Contract;
import domein.Ticket;
import domein.enums.StatusContract;
import domein.enums.StatusTicket;
import domein.enums.TypeTicket;

class ContractTest 
{
	private Contract contract;
	
	@BeforeEach
	public void before()
	{
		
		contract = new Contract(1,LocalDate.now());
	}
	
	@Test
	public void nieuwContractCorrectIngevuld()
	{
		Assertions.assertEquals(1,contract.getDoorlooptijd());
		Assertions.assertEquals(StatusContract.InAanvrag, contract.getStatusContract());
		Assertions.assertEquals(LocalDate.now(), contract.getStartDatum());
	}
	@ParameterizedTest
	@NullSource
	public void voegTicket_ParameterIsNull(Ticket t) {
		Assertions.assertThrows(IllegalArgumentException.class, ()->{
			this.contract.addTickets(t);
		});
	}
	
	@ParameterizedTest
	@MethodSource("voegJuisteTicketToe")
	public void voegTicket_GeldigeTicket(Ticket t) {
		this.contract.addTickets(t);
		
		Ticket ticket = this.contract.getLijstTickets().get(0);
		
		Assertions.assertEquals(1, contract.getLijstTickets().size());
		Assertions.assertEquals(ticket.getTitel(),t.getTitel());
	}
	
	private static Stream<Arguments> voegJuisteTicketToe(){
		return Stream.of(
				Arguments.of(new Ticket("Ticket title","Borens",TypeTicket.Software,"ExampleTESTTEN","GEEN OPMERKING","FANTA",StatusTicket.Geannuleerd))				
				);
	}
	
	
	@Test
	public void statusInAanvraagVeranderdNaartNietActief()
	{
		contract.setStatusContract(StatusContract.NietActief);
		Assertions.assertEquals(StatusContract.NietActief, contract.getStatusContract());
	}
	@Test
	public void nieuwContractFoutIngevuldException()
	{
		Assertions.assertThrows(NullPointerException.class, () -> new Contract(0,null));
	}
}