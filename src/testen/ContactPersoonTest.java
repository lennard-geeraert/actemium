package testen;

import static org.junit.jupiter.api.Assertions.*;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import domein.ContactPersoon;
import exceptions.FouteInvoerException;
import exceptions.VerplichtVeldenException;

public class ContactPersoonTest 
{
	@ParameterizedTest
	@MethodSource("contactPersoonData")
	public void ctorContactPersoon_geldigeParameters(String firstName, String lastName, String email) {
		ContactPersoon cp = new ContactPersoon(firstName, lastName, email);
		assertEquals(firstName, cp.getFirstName());
	}
	
	@ParameterizedTest
	@MethodSource("contactPersoonData")
	public void builderContactPersoon_geldigeParameters(String firstName, String lastName, String email) throws VerplichtVeldenException, FouteInvoerException {
		ContactPersoon cp = new ContactPersoon.Builder().firstName(firstName).lastName(lastName).email(email).build();
		assertEquals(firstName, cp.getFirstName());
	}
	
	private static Stream<Arguments> contactPersoonData(){
		return Stream.of(
				Arguments.of("Jan", "Jansens", "Jan@testemail.com"),
				Arguments.of("Peter", "Jansens", "Peter@testemail.com"),
				Arguments.of("P", "J", "P@g.be")
		);
	}
	
	
	@ParameterizedTest
	@MethodSource("ctor_contactPersoonDataOngeldig")
	public void ctorContactPersoon_parametersOngeldig(String firstName, String lastName, String email) {
		assertThrows(IllegalArgumentException.class, ()->{
			new ContactPersoon(firstName, lastName, email);
		});
	}
	
	@ParameterizedTest
	@MethodSource("builder_contactPersoonDataOngeldig_Verplictevelden")
	public void builderContactPersoon_parametersOngeldig_VerplichteveldException(String firstName, String lastName, String email) {
		assertThrows(VerplichtVeldenException.class, ()->{
			new ContactPersoon.Builder().firstName(firstName).lastName(lastName).email(email).build();
		});
	}
	
	@ParameterizedTest
	@MethodSource("builder_contactPersoonDataOngeldig_FouteInvoer")
	public void builderContactPersoon_parametersOngeldig_FouteInvoerException(String firstName, String lastName, String email) {
		assertThrows(FouteInvoerException.class, ()->{
			new ContactPersoon.Builder().firstName(firstName).lastName(lastName).email(email).build();
		});
	}
	
	private static Stream<Arguments> ctor_contactPersoonDataOngeldig(){
		return Stream.of(
				Arguments.of("", "", ""),
				Arguments.of(null, null, null)
		);
	}
	
	private static Stream<Arguments> builder_contactPersoonDataOngeldig_Verplictevelden(){
		return Stream.of(
				Arguments.of("", "", "")
		);
	}
	
	private static Stream<Arguments> builder_contactPersoonDataOngeldig_FouteInvoer(){
		return Stream.of(
				Arguments.of("a", "a", "a"),
				Arguments.of("a", "a", "a@a.a")
		);
	}
	
}
