package domein;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import java.util.*;

import domein.enums.ManierTicketAanmaak;
import domein.enums.StatusContract;
import domein.enums.StatusContracttype;
import domein.enums.StatusTicket;
import domein.enums.TijdstippenAanmaakTicket;
import exceptions.FouteInvoerException;
import exceptions.VerplichtVeldenException;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

@Entity
@Table(name = "Contracttype")
@NamedQueries({ @NamedQuery(name = "Contracttype.findAll", query = "SELECT c FROM Contracttype c"),
 @NamedQuery(name = "Contracttype.findOneWithStatus", query = "SELECT c FROM Contracttype c WHERE c.isActief = :isActief")
,@NamedQuery(name = "Contracttype.findOneWithNaam", query = "SELECT c FROM Contracttype c WHERE c.naam = :naam")
 })
@Access(AccessType.FIELD)
public class Contracttype implements IContracttype
{	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private int maximaleAfhandeltijd;
	private int minimaleAfhandeltijd;
	private int prijsContract;
	@OneToMany(fetch =  FetchType.EAGER,cascade = CascadeType.PERSIST)
	private Set<Contract> contracten = new HashSet<Contract>(); 
		
	@Transient
	private StringProperty naam;
	@Transient
	private StringProperty manierTicketAanmaak;
	@Transient
	private  StringProperty tijdStippenMogelijkheid;
	@Transient
	private  StringProperty  isActief;
		
	private Contracttype(Builder builder) 
	{
		this.naam = new SimpleStringProperty();
		this.manierTicketAanmaak  = new SimpleStringProperty();
		this.tijdStippenMogelijkheid= new SimpleStringProperty();
		this.isActief= new SimpleStringProperty();
		
		setNaam(builder.naam);
		setManierTicketAanmaak(builder.manierTicketAanmaak);
		setTijdStippenMogelijkheid(builder.tijdStippenMogelijkheid);
		setMaximaleAfhandeltijd(Integer.parseInt(builder.maximaleAfhandeltijd));
		setMinimaleAfhandeltijd(Integer.parseInt(builder.minimaleAfhandeltijd));
		setPrijsContract(Integer.parseInt(builder.prijsContract));
		setIsActief(StatusContracttype.Actief);
		contracten = new HashSet<Contract>();
	}
	
	public Contracttype() 
	{
		this.naam = new SimpleStringProperty();
		this.manierTicketAanmaak  = new SimpleStringProperty();
		this.tijdStippenMogelijkheid= new SimpleStringProperty();
		this.isActief= new SimpleStringProperty();
	}
	
	@Override
	public long getId() 
	{
		return id;
	}
	
	public StringProperty naam() 
	{
		return this.naam;
	}
	
	public StringProperty manierTicketAanmaak() 
	{
		return this.manierTicketAanmaak;
	}
	
	@Override
	public StringProperty tijdStippenMogelijkheid() 
	{
		 return this.tijdStippenMogelijkheid;
	}
	
	public StringProperty isActief() 
	{
		return this.isActief;
	}
	
	@Access(AccessType.PROPERTY)
	public String getNaam() 
	{
		return naam.get();
	}
	
	private void setNaam(String naam) 
	{	
		this.naam.set(naam);
	}

	@Access(AccessType.PROPERTY)
	public StatusContracttype getIsActief() 
	{
		return StatusContracttype.valueOf(this.isActief.get());
	}

	@Access(AccessType.PROPERTY)
	public ManierTicketAanmaak getManierTicketAanmaak() 
	{
		return ManierTicketAanmaak.valueOf(this.manierTicketAanmaak.get());
	}

	private  void setManierTicketAanmaak(ManierTicketAanmaak manierTicketAanmaak) 
	{
		this.manierTicketAanmaak.set(manierTicketAanmaak.toString());
	}

	@Access(AccessType.PROPERTY)
	public TijdstippenAanmaakTicket getTijdStippenMogelijkheid() 
	{
		return TijdstippenAanmaakTicket.valueOf(this.tijdStippenMogelijkheid.get());
	}

	private void setTijdStippenMogelijkheid(TijdstippenAanmaakTicket tijdStippenMogelijkheid) 
	{	
		this.tijdStippenMogelijkheid.set(tijdStippenMogelijkheid.toString());
	}

	public  int getMaximaleAfhandeltijd() 
	{
		return maximaleAfhandeltijd;
	}

	private  void setMaximaleAfhandeltijd(int maximaleAfhandeltijd) 
	{	
		this.maximaleAfhandeltijd = maximaleAfhandeltijd;
	}

	public   int getMinimaleAfhandeltijd() 
	{
		return minimaleAfhandeltijd;
	}

	private  void setMinimaleAfhandeltijd(int minimaleAfhandeltijd) 
	{	
		this.minimaleAfhandeltijd = minimaleAfhandeltijd;
	}

	public   int getPrijsContract() 
	{
		return prijsContract;
	}

	private  void setPrijsContract(int prijsContract) 
	{	
		this.prijsContract = prijsContract;
	}
	
	public void addContract(Contract contract) 
	{
		this.contracten.add(contract);
	}
	
	public void setNaam(StringProperty naam) 
	{
		this.naam = naam;
	}

	public void setIsActief(StatusContracttype isActief) 
	{
		this.isActief.set(isActief.toString());
	}

	@Override
	public Set<Contract> getContracten() 
	{
		return contracten;
	}

	public void setContracten(Set<Contract> contracten) 
	{
		this.contracten = contracten;
	}
	
	public void verwijderContracttype() 
	{
		this.isActief.set(StatusContracttype.NietActief.toString());
	}
		
	public static class Builder
	{	
		private String naam;
		private ManierTicketAanmaak manierTicketAanmaak;
		private TijdstippenAanmaakTicket tijdStippenMogelijkheid;
		private String maximaleAfhandeltijd;
		private String minimaleAfhandeltijd;
		private String prijsContract;
		
		public Builder naam(String naam) {
			this.naam = naam;
			return this;
		}
		
		public Builder manierTicketAanmaak(ManierTicketAanmaak manierTicketAanmaak) {
			this.manierTicketAanmaak = manierTicketAanmaak;
			return this;
		}
		
		public Builder tijdstippenMogelijkheid(TijdstippenAanmaakTicket tijdstippenMogelijkheid) {
			this.tijdStippenMogelijkheid = tijdstippenMogelijkheid;
			return this;
		}
		
		public Builder maximaleAfhandeltijd(String string) {
			this.maximaleAfhandeltijd = string;
			return this;
		}
		
		public Builder minimaleAfhandeltijd(String minafhandeltijd) {
			this.minimaleAfhandeltijd = minafhandeltijd;
			return this;
		}
		
		public Builder prijsContract(String prijs) {
			this.prijsContract = prijs;
			return this;
		}
		
		public Contracttype build() throws VerplichtVeldenException, FouteInvoerException {

			List<String> verplichteAttributen = new ArrayList<String>();
			List<String> fouteInvoerAttributen = new ArrayList<String>();
			Contracttype contracttype =null ;
			if(naam == null || naam.isBlank()  ) {
				verplichteAttributen.add("naam");
			}
			if(naam.length()<=5) {
				fouteInvoerAttributen.add("naam (minstens 5 karakters)");
			}
			if(manierTicketAanmaak == null) {
				verplichteAttributen.add("manier aanmaak ticket");
			}
			if(tijdStippenMogelijkheid == null) {
				verplichteAttributen.add("tijdstippenmogelijkheid");
			}
			if (maximaleAfhandeltijd == null || maximaleAfhandeltijd.isBlank()) {
				verplichteAttributen.add("maximale afhandeltijd");
		    }
			if(minimaleAfhandeltijd == null || minimaleAfhandeltijd.isBlank()) {
				verplichteAttributen.add("minimale afhandeltijd");
			}
			if(prijsContract == null || prijsContract.isEmpty()) {
				verplichteAttributen.add("prijs");
			}
			if( !isValidNumber(String.valueOf(minimaleAfhandeltijd))) {
				fouteInvoerAttributen.add("maximale afhandeltijd (groter dan 0)");
			}
			if( !isValidNumber(String.valueOf(minimaleAfhandeltijd))) {
				fouteInvoerAttributen.add("minimale afhandeltijd (groter dan 0)");
			}
			if( !isValidNumber(String.valueOf(prijsContract))) {
				fouteInvoerAttributen.add("prijs contracttype (groter dan 0)");
			}
			if(!verplichteAttributen.isEmpty()) {
				throw new VerplichtVeldenException("Contracttype kan niet aangemaakt worden omdat volgende velden verplicht zijn: ",verplichteAttributen);
			}
			if(!fouteInvoerAttributen.isEmpty()) {
				throw new FouteInvoerException("Contracttype kan niet aangemaakt worden omdat volgende velden fout zijn: ",fouteInvoerAttributen);
			}
			contracttype = new Contracttype(this);
			return contracttype;
		}
	}

	public void editContract(String naam,ManierTicketAanmaak manier, TijdstippenAanmaakTicket tijdStippenMogelijkheid,
			String maximaleAfh, String minimaleAfh, String prijsContract,StatusContracttype type) throws VerplichtVeldenException, FouteInvoerException {
		List<String> verplichteAttributen = new ArrayList<String>();
		List<String> fouteInvoerAttributen = new ArrayList<String>();
		if(naam == null || naam.isBlank()  ) {
			verplichteAttributen.add("naam");
		}
		if(naam.length()<=5) {
			fouteInvoerAttributen.add("naam (minstens 5 karakters)");
		}
		if(manier == null) {
			verplichteAttributen.add("manier aanmaak ticket");
		}
		if(tijdStippenMogelijkheid == null) {
			verplichteAttributen.add("tijdstippenmogelijkheid");
		}
		if (maximaleAfh == null || maximaleAfh.isBlank()) {
			verplichteAttributen.add("maximale afhandeltijd");
	    }
		if(minimaleAfh == null || minimaleAfh.isEmpty()) {
			verplichteAttributen.add("minimale afhandeltijd");
		}
		if(prijsContract == null || prijsContract.isBlank()) {
			verplichteAttributen.add("prijs");
		}
		if( !isValidNumber(String.valueOf(maximaleAfh))) {
			fouteInvoerAttributen.add("maximale afhandeltijd (groter dan 0)");
		}
		if( !isValidNumber(String.valueOf(minimaleAfh))) {
			fouteInvoerAttributen.add("minimale afhandeltijd (groter dan 0)");
		}
		if( !isValidNumber(String.valueOf(prijsContract))) {
			fouteInvoerAttributen.add("prijs contracttype (groter dan 0)");
		}
		if(!verplichteAttributen.isEmpty()) {
			throw new VerplichtVeldenException("Contracttype kan niet gewijzigd worden omdat volgende velden verplicht zijn: ",verplichteAttributen);
		}
		if(!fouteInvoerAttributen.isEmpty()) {
			throw new FouteInvoerException("Contracttype kan niet gewijzigd worden omdat volgende velden fout zijn: ",fouteInvoerAttributen);
		}
		setNaam(naam);
		setManierTicketAanmaak(manier);
		setMaximaleAfhandeltijd(Integer.parseInt(maximaleAfh));
		setMinimaleAfhandeltijd(Integer.parseInt(minimaleAfh));
		setTijdStippenMogelijkheid(tijdStippenMogelijkheid);
		setIsActief(type);
		setPrijsContract(Integer.parseInt(prijsContract));
	}
	
	public static boolean isValidNumber(String strNum) 
	{
	    try {
	        double d = Double.parseDouble(strNum);
	        if(d<=0) {
	        	return false;
	        }
	    } catch (NumberFormatException nfe) {
	        return false;
	    }
	    return true;
	}

	@Override
	public Long getLopendeContracten() 
	{
			return contracten.stream().filter(c -> c.getStatusContract().equals(StatusContract.Lopend)).count();
	}

	@Override
	public long getAantalBehandeldeTickets() 
	{
		long aantal =  0;
		for(Contract cc : contracten) {
		
			for(ITicket t : cc.getLijstTickets()) {
				if(t.getStatus().equals(StatusTicket.Afgehandeld)) {
					aantal++;
				}
			}}
		return aantal;
		}

	@Override
	public int hashCode() 
	{
		return Objects.hash(naam);
	}

	@Override
	public boolean equals(Object obj) 
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Contracttype other = (Contracttype) obj;
		return Objects.equals(naam, other.naam);
	}

	public boolean heeftContracttypeLopendeContracten() 
	{
		for(Contract c : contracten) {
			if(c.getStatusContract().equals(StatusContract.Lopend)) {
				return true;
			}
		}
		return false;
		
	}

	public void editStatus(StatusContracttype isActief2) 
	{
		setIsActief(isActief2);
	}
}
