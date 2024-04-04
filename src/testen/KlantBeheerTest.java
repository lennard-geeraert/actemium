package testen;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.time.Month;
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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import domein.ContactPersoon;
import domein.IKlant;
import domein.Klant;

import domein.KlantBeheerder;
import domein.enums.StatusGebruiker;
import exceptions.FouteInvoerException;
import exceptions.VerplichtVeldenException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import repository.KlantDao;
import repository.KlantDaoJpa;

@ExtendWith(MockitoExtension.class)
public class KlantBeheerTest 
{	
	@Mock
	private KlantDao klantDaoDummy;
	@Mock
	private KlantDaoJpa klantDaoJpa;
	@InjectMocks
	private KlantBeheerder klantbeheerder;
	
	private List<Klant> klanten;
	private ObservableList<IKlant> klantList;
	private ObservableList<ContactPersoon> contactpersonen;
	private FilteredList<IKlant> filterklantList;
	
	@BeforeEach
	public void before() 
	{
		Klant[] klant = new Klant[] {new Klant("Coca-Cola", "Cola", "password", "Coca-cola@coce.com", "Hasselt", LocalDate.of(1966, Month.FEBRUARY, 12),"0481/041472"),
				new Klant("Pepsico", "Pepsi", "password", "Pepsico@pepsi.com", "Brussel", LocalDate.of(1966, Month.FEBRUARY, 12),"0481/556677")
		};		
		List<Klant> klantList = Arrays.asList(klant); 
		ContactPersoon c1 = new ContactPersoon("Naam","Voornaam","email@email.be");
		ContactPersoon c2 = new ContactPersoon("Naam2","Voornaam2","email2@email.be");
		
		klantDaoDummy = klantDaoJpa;
		this.klanten = klantList;
		this.klantList = FXCollections.observableArrayList(this.klanten);
		this.filterklantList = new FilteredList<IKlant>(this.klantList, k -> true);
		Mockito.lenient().when(klantDaoDummy.findAll()).thenReturn(this.klanten);
		klantbeheerder = new KlantBeheerder(klantDaoJpa);
		klantbeheerder.initialiseerContactpersonen(klantList.get(0));
		klantbeheerder.voegContactPersoonToe(c1,klantList.get(0));
		klantbeheerder.voegContactPersoonToe( c1,klantList.get(0));
		klantbeheerder.voegContactPersoonToe(c2,klantList.get(1));
		klantbeheerder.voegContactPersoonToe( c2,klantList.get(1));
		klantbeheerder.initialiseerContactpersonen(klantList.get(1));
	}
	
	@Test
	public void ctorKlantBeheerderTest() 
	{
		ObservableList<IKlant> oblist = klantbeheerder.geefAlleKlanten();
		assertEquals(2,oblist.size());
		Mockito.verify(klantDaoDummy,Mockito.times(2)).findAll();
	}
	
	@ParameterizedTest
	@MethodSource("filterstatusEnAantal")
	public void testOpFilterKlant(StatusGebruiker sg, int aantalKlant) 
	{
		Predicate<IKlant> predicateIKlant = k -> k.getStatus().equals(sg);
		klantbeheerder.filterKlant(predicateIKlant);
		assertEquals(aantalKlant, klantbeheerder.getFilteredList().size());
	}
	
	private static Stream<Arguments> filterstatusEnAantal(){
		
		return Stream.of(				
				Arguments.of(StatusGebruiker.Actief,2)				
				);
	}
	
	@ParameterizedTest
	@MethodSource("voegKlantenToe")
	public void testopKlantToevoegen_SuccesvolToegevoegd(Klant klant) {		
		this.klantbeheerder.voegKlantToe(klant);
		assertEquals(this.klantbeheerder.geefAlleKlanten().size(), 3);
		Mockito.verify(klantDaoDummy,Mockito.times(1)).insert(klant);
	}
	
	private static Stream<Arguments> voegKlantenToe(){
		
		return Stream.of(				
				Arguments.of(new Klant("ICE-Cola", "mmmm", "password", "cola@coce.com", "Hasselt", LocalDate.of(1966, Month.FEBRUARY, 12),"0481/041472")),
				Arguments.of(new Klant("Sprite-Cola", "cccc", "password", "cola@coce.com", "Hasselt", LocalDate.of(1966, Month.FEBRUARY, 12),"0481/041472"))
				);
	}
	
	@ParameterizedTest
	@MethodSource("klantenToeMetWijzigParamters")
	public void testOpWijzigKlant_geldigeGegevens(Klant klant, String name,String userName, String password, String email, String adres, StatusGebruiker status,String telefoonnummer) throws VerplichtVeldenException, FouteInvoerException {
		klant.addContactPersoon(new ContactPersoon("Jan","Jansens","Jan@Jan.be"));
		klantbeheerder.editKlant(klant,name, userName, password, email, adres, status, telefoonnummer);
		assertEquals(name, klant.getName());
	}
	
	private static Stream<Arguments> klantenToeMetWijzigParamters(){
		Klant k = new Klant("ICE-Cola", "mmmm", "password", "cola@coce.com", "Hasselt", LocalDate.of(1966, Month.FEBRUARY, 12),"0481/041472");
		return Stream.of(				
				Arguments.of(k,"Coca-Cola", "Colalangenaam", "passwoddrd", "Coca-cola@coce.com", "Hasselt", StatusGebruiker.Actief,"0481/041472"),
				Arguments.of(new Klant("Sprite-Cola", "Naamtest", "password", "Coca-cola@coce.com", "Hasselt", LocalDate.of(1966, Month.FEBRUARY, 12),"0481/041472"),"Pepsico", "Pepsiiiddd", "passdddword", "Pepsico@pepsi.com", "Brussel", StatusGebruiker.Actief,"0481/556677")
				);
	}
	
	@ParameterizedTest
	@MethodSource("dataVoorVeranderingStatus")
	public void pasStatusKlantAan_statusIsAangepast(Klant k,StatusGebruiker sg) {
		klantbeheerder.editStatusKlant(k, sg);
		assertEquals(sg,k.getStatus());
	}
	
	private static Stream<Arguments> dataVoorVeranderingStatus(){
		Klant k = new Klant("ICE-Cola", "mmmm", "password", "cola@coce.com", "Hasselt", LocalDate.of(1966, Month.FEBRUARY, 12),"0481/041472");
		Klant k2 = new Klant("Sprite-Cola", "Naamtest", "password", "Coca-cola@coce.com", "Hasselt", LocalDate.of(1966, Month.FEBRUARY, 12),"0481/041472");
		return Stream.of(				
				Arguments.of(k,StatusGebruiker.Geblokkeerd),
				Arguments.of(k2,StatusGebruiker.Actief)
				);
	}
	
	@ParameterizedTest
	@MethodSource("contactPersoonKlantData")
	public void testOpToevoegenContactpersoon_ContactpersoonIsToegevoegd(ContactPersoon cp, Klant kl){
		klantbeheerder.voegContactPersoonToe(cp, kl);
		assertEquals(kl.getContactPersonen().size(), 1);
	}
	
	
	private static Stream<Arguments> contactPersoonKlantData(){
		Klant k = new Klant("ICE-Cola", "mmmm", "password", "cola@coce.com", "Hasselt", LocalDate.of(1966, Month.FEBRUARY, 12),"0481/041472");
		Klant k2 = new Klant("Sprite-Cola", "Naamtest", "password", "Coca-cola@coce.com", "Hasselt", LocalDate.of(1966, Month.FEBRUARY, 12),"0481/041472");
		ContactPersoon c1 = new ContactPersoon("Naam","Voornaam","email@email.be");
		ContactPersoon c2 = new ContactPersoon("Naam2","Voornaam2","email2@email.be");
		return Stream.of(				
				Arguments.of(c1,k),
				Arguments.of(c2,k2)
				);
	}
	
	@ParameterizedTest
	@MethodSource("contactPersoonKlantBijnaToegevoegdeKlant")
	public void testOpToevoegenContactpersoonVoorBijnaToegvoegdeKlant_ContactpersoonIsToegevoegd( Klant kl,ContactPersoon cp){
		klantbeheerder.voegCPToeAanBijnaToegevoegdeKlant(kl, cp);
		assertEquals(1,kl.getContactPersonen().size());
	}
	
	private static Stream<Arguments> contactPersoonKlantBijnaToegevoegdeKlant(){
		Klant k = new Klant("ICE-Cola", "mmmm", "password", "cola@coce.com", "Hasselt", LocalDate.of(1966, Month.FEBRUARY, 12),"0481/041472");
		Klant k2 = new Klant("Sprite-Cola", "Naamtest", "password", "Coca-cola@coce.com", "Hasselt", LocalDate.of(1966, Month.FEBRUARY, 12),"0481/041472");
		ContactPersoon c1 = new ContactPersoon("Naam","Voornaam","email@email.be");
		ContactPersoon c2 = new ContactPersoon("Naam2","Voornaam2","email2@email.be");
		return Stream.of(				
				Arguments.of(k,c1),
				Arguments.of(k2,c2)
				);
	}
	
	@ParameterizedTest
	@MethodSource("verwijderKlantData")
	public void verwijderContactPersoonVanKlantMetMeerDan1CP_verwijderdMetSucces(Klant k,String email) throws VerplichtVeldenException {
		klantbeheerder.verwijderContactPersoon(k, email);
		assertEquals( 1,k.getContactPersonen().size());
	}
	
	private static Stream<Arguments> verwijderKlantData(){
		Klant k = new Klant("ICE-Cola", "mmmm", "password", "cola@coce.com", "Hasselt", LocalDate.of(1966, Month.FEBRUARY, 12),"0481/041472");
		Klant k2 = new Klant("Sprite-Cola", "Naamtest", "password", "Coca-cola@coce.com", "Hasselt", LocalDate.of(1966, Month.FEBRUARY, 12),"0481/041472");
		ContactPersoon c1 = new ContactPersoon("Naam","Voornaam","email@email.be");
		ContactPersoon c2 = new ContactPersoon("Naam2","Voornaam2","email2@email.be");
		k.addContactPersoon(c1);
		k.addContactPersoon(c2);
		k2.addContactPersoon(c1);
		k2.addContactPersoon(c2);
		return Stream.of(				
				Arguments.of(k,c1.getEmail()),
				Arguments.of(k2,c2.getEmail())
				);
	}
	
	
	
	
	
	
	
}
