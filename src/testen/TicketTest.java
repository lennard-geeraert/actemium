package testen;

import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import domein.Ticket;
import domein.enums.StatusTicket;
import domein.enums.TypeTicket;
import exceptions.FouteInvoerException;
import exceptions.VerplichtVeldenException;

public class TicketTest 
{
	private Ticket ticket;
	
	@BeforeEach
	public void before() {
		ticket = new Ticket("Ticket titel","Jansens",TypeTicket.Hardware,"Voorbeeldtext","geen opmerking","Coca-cola",StatusTicket.Aangemaakt);
	}
		
	@Test
	public void ctorMetInvalideParameters() {
		Assertions.assertThrows(IllegalArgumentException.class,()->{
			new Ticket("","",null,"","","",null);
		});
	}
	
	@Test
	public void ctorMetGrensParameters() {
		Ticket t = new Ticket("Geval","Jansens",TypeTicket.Hardware,"Net tien C","geen opmerking","Pepsi",StatusTicket.Aangemaakt);
		Assertions.assertEquals("Geval", t.getTitel());
	}
	
	@ParameterizedTest
	@MethodSource("voegInvalideParamaters")
	public void editTicketMetLegeParameters(String titel, String toegewezenTechnieker, String klant, TypeTicket type, String omschrijving, String opmerking) {
		Assertions.assertThrows(VerplichtVeldenException.class, ()->{
			this.ticket.edit(titel, toegewezenTechnieker, klant, type, omschrijving, opmerking);
		});
	}
	
	private static Stream<Arguments> voegInvalideParamaters(){
		return Stream.of(
				Arguments.of("kort", "","kort",TypeTicket.Algemeen,"korterda10","dee","dee",StatusTicket.Geannuleerd),
				Arguments.of("k", "","",TypeTicket.Algemeen,"ko","dee","dee",StatusTicket.Afgehandeld)
				);
	}
	
	@ParameterizedTest
	@MethodSource("voegValideParamaters")
	public void editTicketMetValideParametersEnGrenzwaarden(String titel,String toegewezenTechnieker, String klant, TypeTicket type, String omschrijving, String opmerking) throws VerplichtVeldenException, FouteInvoerException {
		ticket.edit(titel,toegewezenTechnieker,klant,type,omschrijving,opmerking);
		Assertions.assertEquals(titel, ticket.getTitel());
		Assertions.assertEquals(toegewezenTechnieker, ticket.getToegewezenTechnieker());
		Assertions.assertEquals(type, ticket.getType());
		Assertions.assertEquals(omschrijving, ticket.getOmschrijving());
		Assertions.assertEquals(opmerking, ticket.getOpmerking());
	}
	
	private static Stream<Arguments> voegValideParamaters(){
		return Stream.of(
				Arguments.of("Ticket titel2","ismail","Jansens",TypeTicket.Hardware,"Voorbeeldtext","geen opmerking"),
				Arguments.of("Ticket titel3","sow","Jansens",TypeTicket.Hardware,"Voorbeeldtext","geen opmerking"),
				Arguments.of("Grens_","lenn","Jansens",TypeTicket.Hardware,"Voorbeeldtext","Jehebter10_")
				);
	}
	
	
	@ParameterizedTest
	@MethodSource("voegBuilderValideTicketParamaters")
	public void maakTicketMetBuilder(String titel, String toegewezenTechnieker, TypeTicket type, String omschrijving, String opmerking, 
			String bedrijfKlant, StatusTicket status) throws VerplichtVeldenException, FouteInvoerException {		
		Ticket t = new Ticket.Builder().titel(titel).toegewezenTechnieker(toegewezenTechnieker).type(type).omschrijving(omschrijving)
				.bedrijfKlant(bedrijfKlant).status(status).build();
		
		Assertions.assertEquals(titel, t.getTitel());
	}
	
	private static Stream<Arguments> voegBuilderValideTicketParamaters(){
		return Stream.of(
				Arguments.of("Ticket titel2","ismail",TypeTicket.Hardware,"Voorbeeldtext","geen opmerking","Coca-cola",StatusTicket.Aangemaakt),
				Arguments.of("Ticket titel3","sow",TypeTicket.Hardware,"Voorbeeldtext","geen opmerking","Pepsico",StatusTicket.Aangemaakt),
				Arguments.of("Grens_","them",TypeTicket.Hardware,"Voorbeeldtext","Jehebter10_","Ikea",StatusTicket.Aangemaakt)
				);
	}
	
	
	@ParameterizedTest
	@MethodSource("voegBuilderInvalideTicketParamaters")
	public void maakTicketMetBuilderMetInvalideParameters_gooitVerplichteVeldException(String titel, String toegewezenTechnieker, TypeTicket type, String omschrijving, String opmerking, 
			String bedrijfKlant, StatusTicket status) {		
		Assertions.assertThrows(VerplichtVeldenException.class, ()->{
			new Ticket.Builder().titel(titel).toegewezenTechnieker(toegewezenTechnieker).type(type).omschrijving(omschrijving)
			.bedrijfKlant(bedrijfKlant).status(status).build();
		});
		
	}
	
	private static Stream<Arguments> voegBuilderInvalideTicketParamaters(){
		return Stream.of(
				Arguments.of("","",null,"","","",null),
				Arguments.of("   ","   ",null,"   ","  ","   ",null),
				Arguments.of("titel","dee",null,"ddede","kort","Coca-cola",null),
				Arguments.of("kort","them",null,"kort","kort","Ikea",null)
				);
	}	
}
