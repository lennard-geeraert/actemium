package testen;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import domein.Contracttype;
import domein.ContracttypeBeheerder;
import domein.enums.ManierTicketAanmaak;
import domein.enums.StatusContracttype;
import domein.enums.TijdstippenAanmaakTicket;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import repository.ContracttypeDao;
import repository.ContractypeDaoJpa;

@ExtendWith(MockitoExtension.class)
class ContracttypeBeheerderTest 
{
	@Mock
	private ContractypeDaoJpa dJpa;
	@Mock
	private ContracttypeDao contracttypeRepo;
	@InjectMocks
	private ContracttypeBeheerder cT;
	
	private List<Contracttype> contracttypes = new ArrayList<>();
	private ObservableList<Contracttype> contractTypeLijst;
	private FilteredList<Contracttype> filteredLijst;
	private SortedList<Contracttype> sortedLijst;
	
	@BeforeEach()
	public void before()
	{
		contracttypeRepo = dJpa;
		contractTypeLijst = FXCollections.observableArrayList(contracttypes);
		filteredLijst = new FilteredList<Contracttype>(contractTypeLijst, g -> true);
		sortedLijst = new SortedList<>(filteredLijst);
		Mockito.when(contracttypeRepo.findAll()).thenReturn(contracttypes);
		cT = new ContracttypeBeheerder(dJpa);
	}
	
	@ParameterizedTest
	@MethodSource("status")
	public void geefLijstContractTypesVolgensStatus(StatusContracttype status)
	{
		List<Contracttype> expected = new FilteredList<>(contractTypeLijst, c -> c.getIsActief().equals(status));
		Assertions.assertEquals(expected.size(), cT.getAlleContracttypesWithStatus(true).size());
	}
	
	
	private static Stream<Arguments> status(){
		return Stream.of(
				Arguments.of(StatusContracttype.Actief),
				Arguments.of(StatusContracttype.NietActief)
		);
	}

	@ParameterizedTest
	@MethodSource("editContracttypeOnjuisteParameters")
	public void editContracttypeOnjuisteParameters(Contracttype type,String naam,ManierTicketAanmaak manier, TijdstippenAanmaakTicket tijdStippenMogelijkheid,
			String maximaleAfh,String minimaleAfh, String prijsContract,StatusContracttype isActief)
	{
		Assertions.assertThrows(NullPointerException.class, ()->{
			this.cT.editContracttype(type, naam, manier, tijdStippenMogelijkheid, maximaleAfh, minimaleAfh, prijsContract, isActief);
		});
	}
	
	private static Stream<Arguments> editContracttypeOnjuisteParameters(){
		return Stream.of(
				Arguments.of(null,"",ManierTicketAanmaak.Applicatie,TijdstippenAanmaakTicket.Altijd24op7,String.valueOf(1),String.valueOf(1),String.valueOf(1),StatusContracttype.Actief)				
				);
	}
	
	@ParameterizedTest
	@NullSource
	public void editContracttypeParameterIsNullException(Contracttype c) {
		Assertions.assertThrows(NullPointerException.class, ()->{
			this.cT.editContracttype(c, null, null, null, null, null, null, null);
		});
	}
		
	@ParameterizedTest
	@NullSource
	public void verwijderContracttypeParameterIsNullException(Contracttype c) {
		Assertions.assertThrows(NullPointerException.class, ()->{
			this.cT.verwijderContracttype(c);
		});
	}
}
