package testen;



import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;

import org.junit.jupiter.params.provider.MethodSource;

import org.junit.jupiter.params.provider.ValueSource;


import domein.Contracttype;

import domein.enums.ManierTicketAanmaak;
import domein.enums.StatusContracttype;
import domein.enums.TijdstippenAanmaakTicket;
import exceptions.FouteInvoerException;
import exceptions.VerplichtVeldenException;

class ContracttypeTest {

	private Contracttype c;
	@BeforeEach
	public void before()
	{
		c = new Contracttype(); 
	}
	
	
	
	@ParameterizedTest
	@MethodSource("contracttype")
	public void builderContracttypeJuisteParameters(String naam, ManierTicketAanmaak manier,TijdstippenAanmaakTicket tijdStippenMogelijkheid, String max, String min, String prijsContract) throws VerplichtVeldenException, FouteInvoerException {
		 c = new Contracttype.Builder().naam(naam).manierTicketAanmaak(manier).tijdstippenMogelijkheid(tijdStippenMogelijkheid).maximaleAfhandeltijd(max).minimaleAfhandeltijd(min).prijsContract(prijsContract).build();
		assertEquals(naam, c.getNaam());
	}
	
	
	
	private static Stream<Arguments> contracttype(){
		return Stream.of(
				Arguments.of("Naam test",ManierTicketAanmaak.Applicatie,TijdstippenAanmaakTicket.Altijd24op7,String.valueOf(1),String.valueOf(1),String.valueOf(50)),
				Arguments.of("Chicken", ManierTicketAanmaak.Email, TijdstippenAanmaakTicket.Van8tot17OpWerkdagen,String.valueOf(2),String.valueOf(4),String.valueOf(40))

		);
	}
	
	@ParameterizedTest
	@MethodSource("builderContracttypeVerplictevelden")
	public void builderContactPersoon_parametersOngeldig_VerplichteveldException(String naam, ManierTicketAanmaak manier,TijdstippenAanmaakTicket tijdStippenMogelijkheid, String max, String min, String prijsContract) {
		assertThrows(VerplichtVeldenException.class, ()->{
			new Contracttype.Builder().naam(naam).manierTicketAanmaak(manier).tijdstippenMogelijkheid(tijdStippenMogelijkheid).maximaleAfhandeltijd(max).minimaleAfhandeltijd(min).prijsContract(prijsContract).build();
	});
	}
	
	@ParameterizedTest
	@MethodSource("builderContracttypeVerkeerdeInvoer")
	public void builderContactPersoon_parametersOngeldig_FouteInvoerException(String naam, ManierTicketAanmaak manier,TijdstippenAanmaakTicket tijdStippenMogelijkheid, String max, String min, String prijsContract) {
		assertThrows(VerplichtVeldenException.class, ()->{
			new Contracttype.Builder().naam(naam).manierTicketAanmaak(manier).tijdstippenMogelijkheid(tijdStippenMogelijkheid).maximaleAfhandeltijd(max).minimaleAfhandeltijd(min).prijsContract(prijsContract).build();
		});
	}
	
	private static Stream<Arguments> builderContracttypeVerplictevelden(){
		return Stream.of(
				Arguments.of("", null, null,"","","","")
		);
	}
	
	private static Stream<Arguments> builderContracttypeVerkeerdeInvoer(){
		return Stream.of(
				Arguments.of("a", null,null,"a","a","a")
//				Arguments.of("a", "a", "a@a.a")
		);
	}
	
	@Test
	public void editContractMetJuisteGegevensA() throws VerplichtVeldenException, FouteInvoerException 
	{
	
		c.editContract("ContracttypeAA", ManierTicketAanmaak.Applicatie, TijdstippenAanmaakTicket.Altijd24op7,String.valueOf(12), String.valueOf(4), String.valueOf(50), StatusContracttype.Actief);
		Assertions.assertEquals("ContracttypeAA",c.getNaam());
		Assertions.assertEquals(ManierTicketAanmaak.Applicatie, c.getManierTicketAanmaak());
		Assertions.assertEquals(TijdstippenAanmaakTicket.Altijd24op7, c.getTijdStippenMogelijkheid());
	}
	
	@Test
	public void editContractMetJuisteGegevensB() throws VerplichtVeldenException, FouteInvoerException 
	{
		
		c.editContract("ContracttypeBB", ManierTicketAanmaak.Email, TijdstippenAanmaakTicket.Van8tot17OpWerkdagen,String.valueOf(10), String.valueOf(6), String.valueOf(40), StatusContracttype.Actief);
		Assertions.assertEquals(10, c.getMaximaleAfhandeltijd());
		Assertions.assertEquals(6, c.getMinimaleAfhandeltijd());

		
	}
	
	@Test
	public void editContractMetJuisteGegevensC() throws VerplichtVeldenException, FouteInvoerException 
	{
	
		c.editContract("ContracttypeCC", ManierTicketAanmaak.Telefonisch, TijdstippenAanmaakTicket.Van8tot17OpWerkdagen,String.valueOf(10), String.valueOf(6), String.valueOf(40), StatusContracttype.Actief);
		Assertions.assertEquals(40, c.getPrijsContract());
		Assertions.assertEquals(StatusContracttype.Actief, c.getIsActief());
	}
	
	
	
	@Test
	public void editContractMetGrensWaarden() throws VerplichtVeldenException, FouteInvoerException
	{
		c.editContract("Contra", ManierTicketAanmaak.Email, TijdstippenAanmaakTicket.Van8tot17OpWerkdagen, String.valueOf(1), String.valueOf(1), String.valueOf(1), StatusContracttype.Actief);
		Assertions.assertEquals("Contra", c.getNaam());
		Assertions.assertEquals(1, c.getMaximaleAfhandeltijd());
	}
	
	
	@Test
	public void editContractMetNull() 
	{
		Assertions.assertThrows(NullPointerException.class, () -> c.editContract(null, null, null, null, null, null, null));
	}
	
	@ParameterizedTest
	@ValueSource(strings = {"     ","con","Contracttype12445","CONTRACTTYPE123456"})
	public void editContractVerkeerdeParameterNaam(String naam)
	{
		
		Assertions.assertThrows(VerplichtVeldenException.class, () -> c.editContract(naam, null, null, String.valueOf(0), String.valueOf(0), String.valueOf(0), null));
	}
	
	
	
	@ParameterizedTest
	@ValueSource(strings = {" ","a"})
	public void isNotValidNumber(String number)
	{
		Assertions.assertFalse(c.isValidNumber(number));
	}
	
	
	@ParameterizedTest
	@ValueSource(strings = {"123"})
	public void isValidNumber(String number)
	{
		Assertions.assertTrue(c.isValidNumber(number));
	}
	
//	@ParameterizedTest	
//	@ValueSource(strings = {"aaa","bbbb","ssss"})
//	public void isValidNumberException(String number)
//	{
//		Assertions.assertThrows(NumberFormatException.class, () -> contractType.isValidNumber(number));
//	}
	

	
	
	
	
	
	
	
	
	
	

	
	
	
	
	
	
	
	
	
	
	

	
	
	
	

}
