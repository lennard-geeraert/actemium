package testen;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.time.Month;
import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import domein.ContactPersoon;
import domein.Contract;
import domein.Klant;
import domein.Ticket;
import domein.enums.StatusContract;
import domein.enums.StatusTicket;
import domein.enums.TypeTicket;

public class KlantTest 
{
		
	private Klant klant;
	
	@BeforeEach
	public void before() {
		this.klant = new Klant("Coca-Cola", "Cola", "password", "Coca-cola@coce.com", "Hasselt", LocalDate.of(1966, Month.FEBRUARY, 12),"0481/041472");
	}
	
	@Test
	public void ctorMetInvalideParameters() {
		Assertions.assertThrows(IllegalArgumentException.class,()->{
			 new Klant("", "", "", "", "", LocalDate.of(1966, Month.FEBRUARY, 12),"0481/041472");
		});
	}
	
	@Test
	public void ctorMetGrensParameters() {
		Klant k = new Klant("C", "C", "passwor", "c@c.c", "B", LocalDate.of(1966, Month.FEBRUARY, 12),"0481/041472");
		Assertions.assertEquals("C", k.getName());
	}
	
	@ParameterizedTest
	@NullSource
	public void voegcontactPersoon_ParamteterIsNull(ContactPersoon c) {
		Assertions.assertThrows(IllegalArgumentException.class, ()->{
			this.klant.addContactPersoon(c);
		});
	}
	
	@ParameterizedTest
	@MethodSource("voegGeldigeContactpersoonToe")
	public void voegcontactPersoon_GeldigeContactPersoon(ContactPersoon c) {
		this.klant.addContactPersoon(c);
		
		ContactPersoon cp = klant.getContactPersonen().get(0);
		
		Assertions.assertEquals(1, klant.getContactPersonen().size());
		Assertions.assertEquals(cp.getFirstName(),c.getFirstName());
	}
	
	private static Stream<Arguments> voegGeldigeContactpersoonToe(){
		return Stream.of(
				Arguments.of(new ContactPersoon("Jan", "Jansens", "Jan@testemail.com")),
				Arguments.of(new ContactPersoon("Peter", "Jansens", "Peter@testemail.com"))
				);
	}
	
	@ParameterizedTest
	@NullAndEmptySource
	@ValueSource(strings = {"   ","test.test.be","test","test.test"})	
	public void verwijderContactPersoon_ongeldigeEmail(String email) {
		Assertions.assertThrows(IllegalArgumentException.class, ()->{
			this.klant.verwijderContactPersoon(email);
		});
	}
	
	@ParameterizedTest	
	@ValueSource(strings= {"jansens@test.be"})	
	public void verwijderContactPersoon_contactPersoonBestaatniet(String email) {
		Assertions.assertThrows(NullPointerException.class, ()->{
			this.klant.verwijderContactPersoon(email);
		});
	}
	
	@ParameterizedTest
	@MethodSource("voegGeldigeContactpersoonToeEnEmail")
	public void verwijderContactPersoon_GeldigeVerwijdering(ContactPersoon c,String email) {		
		this.klant.addContactPersoon(c);
		assertEquals(klant.getContactPersonen().size(), 1);
		this.klant.verwijderContactPersoon(email);
		assertEquals(klant.getContactPersonen().size(), 0);
	}
	
	private static Stream<Arguments> voegGeldigeContactpersoonToeEnEmail(){
		return Stream.of(
				Arguments.of(new ContactPersoon("Jan", "Jansens", "Jan@testemail.com"),"Jan@testemail.com"),
				Arguments.of(new ContactPersoon("Peter", "Jansens", "Peter@testemail.com"),"Peter@testemail.com")
				);
	}
	
	
	@ParameterizedTest
	@NullSource
	public void voegContract_ParameterIsNull(Contract c) {
		Assertions.assertThrows(IllegalArgumentException.class, ()->{
			this.klant.addContract(c);
		});
	}
	
	
	@ParameterizedTest
	@MethodSource("voegGeldigeContract")
	public void voegContract_GeldigeContract(Contract c) {
		this.klant.addContract(c);
		
		Contract contract =  this.klant.getContracten().get(0);
		
		Assertions.assertEquals(1, klant.getContracten().size());
		Assertions.assertEquals(contract.getStartDatum(),c.getStartDatum());
	}
	
	private static Stream<Arguments> voegGeldigeContract(){
		return Stream.of(
				Arguments.of(new Contract(20, LocalDate.now(),StatusContract.Lopend)),
				Arguments.of(new Contract(30, LocalDate.now(),StatusContract.Lopend))
				);
	}
	
	@ParameterizedTest
	@MethodSource("voegGeldigeContract")
	public void heeftKlantActiefContract_TestOpContactIsActief(Contract c) {
		this.klant.addContract(c);
		Assertions.assertTrue(klant.heeftKlantActiefContract());
	}
	
	
	@ParameterizedTest
	@NullSource
	public void voegTicket_ParameterIsNull(Ticket t) {
		Assertions.assertThrows(IllegalArgumentException.class, ()->{
			this.klant.addTicket(t);
		});
	}
	
	@ParameterizedTest
	@MethodSource("voegGeldigeTicket")
	public void voegTicket_GeldigeTicket(Ticket t) {
		this.klant.addTicket(t);
		
		Ticket ticket = this.klant.getTickets().get(0);
		
		Assertions.assertEquals(1, klant.getTickets().size());
		Assertions.assertEquals(ticket.getTitel(),t.getTitel());
	}
	
	private static Stream<Arguments> voegGeldigeTicket(){
		return Stream.of(
				Arguments.of(new Ticket("Ticket titel","Jansens",TypeTicket.Hardware,"Voorbeeldtext","geen opmerking","Coca-cola",StatusTicket.Aangemaakt))				
				);
	}
	

	
}
