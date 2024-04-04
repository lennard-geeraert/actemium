package testen;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;

import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;


import domein.Ticket;
import domein.Werknemer;
import domein.enums.RolWerknemer;
import domein.enums.StatusGebruiker;
import domein.enums.StatusTicket;
import domein.enums.TypeTicket;
import exceptions.FouteInvoerException;
import exceptions.VerplichtVeldenException;

class WerknemerTest 
{
	private Werknemer wk;
	
	@BeforeEach
	public void before()
	{
		wk = new Werknemer("Natanael","Dobie","abcdefg",LocalDate.now(),"them",RolWerknemer.Administrator,"DeSchrijverstraat","natanael.dobie@student.hogent.be","0456/041472");
	}
	
	@ParameterizedTest
	@MethodSource("werknemer")
	public void constructorWerknemerJuisteParameters(String firstName, String lastName, String password, LocalDate date, String userName, RolWerknemer rol, String adres, String email,String telefoonnummers) {
		wk = new Werknemer(firstName,lastName,password,date,userName,rol,adres,email,telefoonnummers);
		assertEquals(firstName, wk.getFirstName());
		assertEquals(adres,wk.getAdres());
	}
	
	@ParameterizedTest
	@MethodSource("werknemer")
	public void builderWerknemerJuisteParameters(String firstName, String lastName, String password, LocalDate date, String userName, RolWerknemer rol, String adres, String email,String telefoonnummers) throws VerplichtVeldenException, FouteInvoerException {
		wk = new Werknemer.Builder().firstname(firstName).lastname(lastName).password(password).datumReg(date).userName(userName).rol(rol).adres(adres).email(email).telefoonnummers(telefoonnummers).build();
		assertEquals(lastName, wk.getLastName());
		assertEquals(password,wk.getPassword());
	}
	
	
	
	private static Stream<Arguments> werknemer(){
		return Stream.of(
				Arguments.of("Mohammed","SALMANI","abcdefg",LocalDate.now(),"them",RolWerknemer.Administrator,"DeVijverstraat","mohammed.salmani@student.hogent.be","0456/041472")
		);
	}
	
	@Test
	public void constructorMetVerkeerdeParametersException() {
		Assertions.assertThrows(IllegalArgumentException.class,()->{
			 new Werknemer("","","",LocalDate.now(),"",RolWerknemer.SupportManager,"","","");
		});
	}
	
	@ParameterizedTest
	@MethodSource("constructorWerknemerOnjuisteParameters")
	public void ctorContactPersoon_parametersOngeldig(String firstName, String lastName, String password, LocalDate date, String userName, RolWerknemer rol, String adres, String email,String telefoonnummers) {
		assertThrows(IllegalArgumentException.class, ()->{
			new Werknemer(firstName,lastName,password,date,userName,rol,adres,email,telefoonnummers);
		});
	}
	
	@ParameterizedTest
	@MethodSource("builderWerknemerVerplictevelden")
	public void builderContactPersoon_parametersOngeldig_VerplichteveldException(String firstName, String lastName, String password, LocalDate date, String userName, RolWerknemer rol, String adres, String email,String telefoonnummers) {
		assertThrows(VerplichtVeldenException.class, ()->{
		 new Werknemer.Builder().firstname(firstName).lastname(lastName).password(password).datumReg(date).userName(userName).rol(rol).adres(adres).email(email).telefoonnummers(telefoonnummers).build();
		});
	}
	
	@ParameterizedTest
	@MethodSource("builderWerkenemerFouteInvoer")
	public void builderContactPersoon_parametersOngeldig_FouteInvoerException(String firstName, String lastName, String password, LocalDate date, String userName, RolWerknemer rol, String adres, String email,String telefoonnummers) {
		assertThrows(VerplichtVeldenException.class, ()->{
			 new Werknemer.Builder().firstname(firstName).lastname(lastName).password(password).datumReg(date).userName(userName).rol(rol).adres(adres).email(email).telefoonnummers(telefoonnummers).build();
		});
	}
	
	private static Stream<Arguments> constructorWerknemerOnjuisteParameters(){
		return Stream.of(
				Arguments.of("nasf", "dd", "password&ssf",null,"NAAAAAA",null,"DESchiersdtsatsrt","emaislsfsi&&&lssf.com","04567839394988588933")
		);
	}
	
	private static Stream<Arguments> builderWerknemerVerplictevelden(){
		return Stream.of(
				Arguments.of("", "", "",null,"",null,"","","")
		);
	}
	
	private static Stream<Arguments> builderWerkenemerFouteInvoer(){
		return Stream.of(
				Arguments.of("a", "a", "a",null,"a",null,"a","a","a")
		);
	}
	
	
	@ParameterizedTest
	@MethodSource("voegOnjuisteParameters")
	public void editWerknemerMetLegeParameters(String firstName, String lastName, String password, RolWerknemer rol, String userName, String adres, 
			String email, StatusGebruiker status,String tel) {
		Assertions.assertThrows(VerplichtVeldenException.class, ()->{
			this.wk.editWerknemer(firstName,lastName,password,rol,userName,adres,email,status,tel);
		});
	}
	
	private static Stream<Arguments> voegOnjuisteParameters(){
		return Stream.of(
				Arguments.of("leeg","","",RolWerknemer.Administrator,"","","",StatusGebruiker.Actief,"")

				);
	}
	
	
	@ParameterizedTest
	@MethodSource("voegJuisteParameters")
	public void editWerknemerMetJuisteParametersEnGrenswaarden(String firstName, String lastName, String password, RolWerknemer rol, String userName, String adres, 
			String email, StatusGebruiker status,String tel) throws VerplichtVeldenException, FouteInvoerException {
		wk.editWerknemer(firstName,lastName,password,rol,userName,adres,email,status,tel);
		Assertions.assertEquals(firstName, wk.getFirstName());
		Assertions.assertEquals(lastName, wk.getLastName());
		Assertions.assertEquals(rol,wk.getRol());
	}
	
	private static Stream<Arguments> voegJuisteParameters(){
		return Stream.of(
				Arguments.of("Natanael","Dobie","abcdefg",RolWerknemer.Administrator,"them","DeSchrijverstraat","natanael.dobie@student.hogent.be",StatusGebruiker.Actief,"0456/041472"),
				Arguments.of("Mohamed","Salmani","abcdefg",RolWerknemer.Administrator,"them123","DeVijverstraat","mohamed.salmani@student.hogent.be",StatusGebruiker.Actief,"0456/041472")
				);
	}
	
	@ParameterizedTest
	@ValueSource(strings = {" "})
	public void editWerknemerMetNull(String naam)
	{
		Assertions.assertThrows(NullPointerException.class, () -> wk.editWerknemer(naam, null, null, null, null, null, null, null, null));
	}
	
	
	@ParameterizedTest
	@NullSource
	public void voegTicket_ParameterIsNull(Ticket t) {
		Assertions.assertThrows(IllegalArgumentException.class, ()->{
			this.wk.addTicket(t);
		});
	}
	
	@ParameterizedTest
	@MethodSource("voegJuisteTicketToe")
	public void voegTicket_GeldigeTicket(Ticket t) {
		this.wk.addTicket(t);
		
		Ticket ticket = this.wk.getTickets().get(0);
		
		Assertions.assertEquals(1, wk.getTickets().size());
		Assertions.assertEquals(ticket.getTitel(),t.getTitel());
	}
	
	private static Stream<Arguments> voegJuisteTicketToe(){
		return Stream.of(
				Arguments.of(new Ticket("Ticket title","Borens",TypeTicket.Software,"ExampleTESTTEN","GEEN OPMERKING","FANTA",StatusTicket.Geannuleerd))				
				);
	}
}
