package domein;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

import domein.enums.StatusTicket;
import domein.enums.TypeTicket;
import exceptions.FouteInvoerException;
import exceptions.VerplichtVeldenException;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

@Entity
@Table(name = "Ticket")
@NamedQueries({@NamedQuery(name = "Ticket.findAll", query = "SELECT t FROM Ticket t"),
	@NamedQuery(name = "Ticket.findAllFromEmployee", query = "SELECT t FROM Ticket t WHERE t.toegewezenTechnieker = :toegewezenTechnieker")})
@Access(AccessType.FIELD)
public class Ticket implements ITicket 
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long ticketID;
	@Transient
	private StringProperty titel;
	@Transient
	private StringProperty toegewezenTechnieker;
	@Transient
	private StringProperty bedrijfKlant;
	@Transient
	private StringProperty status;
	@Transient
	private StringProperty type;
	@Transient
	private StringProperty omschrijving;
	private LocalDate datumAanmaak;
	@Transient
	private StringProperty opmerking;
	//wegdoen na demo klant
	@ManyToOne
	private Klant k;
	@ManyToOne
	private Contract contract;
	
	public Ticket(String titel, String toegewezenTechnieker, TypeTicket type, String omschrijving, String opmerking, 
			String bedrijfKlant, StatusTicket status) 
	{
		this();
		setTitel(titel);
		setToegewezenTechnieker(toegewezenTechnieker);
		setStatus(status);
		setType(type);
		setOmschrijving(omschrijving);
		setDatumAanmaak(LocalDate.now());
		setOpmerking(opmerking);
		setBedrijfKlant(bedrijfKlant);
	}
	
	public Ticket(Builder builder) 
	{
		this();
		setTitel(builder.titel);
		setToegewezenTechnieker(builder.toegewezenTechnieker);
		setStatus(builder.status);
		setType(builder.type);
		setOmschrijving(builder.omschrijving);
		setOpmerking(builder.opmerking);
		setBedrijfKlant(builder.bedrijfKlant);
		setContract(builder.contract);
		setDatumAanmaak(LocalDate.now());
	}
	
	protected Ticket() 
	{
		this.titel = new SimpleStringProperty();
		this.toegewezenTechnieker = new SimpleStringProperty();
		this.bedrijfKlant = new SimpleStringProperty();
		this.status = new SimpleStringProperty();
		this.type = new SimpleStringProperty();
		this.omschrijving = new SimpleStringProperty();
		this.opmerking = new SimpleStringProperty();
	}
	
	public static class Builder
	{
		private String titel;
		private String toegewezenTechnieker;
		private String bedrijfKlant;
		private StatusTicket status;
		private TypeTicket type;
		private String omschrijving;
		private LocalDate datumAanmaak;
		private String opmerking;
		private Contract contract;
		private Klant k;
		public Builder titel(String titel) {
			this.titel = titel;
			return this;
		}
		
		public Builder toegewezenTechnieker(String toegewezenTechnieker) {
			this.toegewezenTechnieker =toegewezenTechnieker;
			return this;
		}
		
		public Builder bedrijfKlant(String bedrijfKlant) {
			this.bedrijfKlant = bedrijfKlant;
			return this;
		}
		
		public Builder status(StatusTicket status) {
			this.status = status;
			return this;
		}
		
		public Builder datumAanmaak(LocalDate date) {
			this.datumAanmaak = date;
			return this;
		}
		
		public Builder type(TypeTicket type) {
			this.type = type;
			return this;
		}
		public Builder omschrijving(String omschrijving) {
			this.omschrijving = omschrijving;
			return this;
		}
		
		public Builder opmerking(String opmerking) {
			this.opmerking = opmerking;
			return this;
		}
		
		public Builder contract(Contract contract) {
			this.contract = contract;
			return this;
		}
		public Builder klant(Klant k) {
			this.k = k;
			return this;
		}
		
		public Ticket build() throws VerplichtVeldenException, FouteInvoerException {
			List<String> verplichteAttributen = new ArrayList<String>();
			List<String> fouteInvoerAttributen = new ArrayList<String>();
			Ticket t = null;
			if(titel == null || titel.isEmpty() ) {
				verplichteAttributen.add("titel");
			}
			if(titel.length()<=5) {
				fouteInvoerAttributen.add("titel (minsten 5 karakters)");
			}
			if (omschrijving == null || omschrijving.isBlank() ) {
				verplichteAttributen.add("omschrijving");
			}
			if(omschrijving.length()<=10) {
				fouteInvoerAttributen.add("omschrijving (minstens 10 karakters)");
			}
			if(bedrijfKlant == null || bedrijfKlant.isEmpty()) {
				verplichteAttributen.add("klant");
			}
			if(type == null) {
				verplichteAttributen.add("type");
			}
			if(!verplichteAttributen.isEmpty()) {
				throw new VerplichtVeldenException("Ticket kan niet aangemaakt worden omdat volgende velden verplicht zijn: ",verplichteAttributen);
			}
			if(!fouteInvoerAttributen.isEmpty()) {
				throw new FouteInvoerException("Ticket kan niet aangemaakt worden omdat volgende velden fout zijn: ",fouteInvoerAttributen);
			}
			t = new Ticket(this);
			return t;
		}
	}
	
	public void edit(String titel, String toegewezenTechnieker, String klant, TypeTicket type, String omschrijving, String opmerking) throws VerplichtVeldenException, FouteInvoerException
	{
		List<String> verplichteAttributen = new ArrayList<String>();
		List<String> fouteInvoerAttributen = new ArrayList<String>();
		if(titel == null || titel.isEmpty() ) {
			verplichteAttributen.add("titel");
		}
		if(titel.length()<=5) {
			fouteInvoerAttributen.add("titel (minstens 5 karakters)");
		}
		if (omschrijving == null || omschrijving.isBlank() ) {
			verplichteAttributen.add("omschrijving");
		}
		if(omschrijving.length()<=10) {
			fouteInvoerAttributen.add("omschrijving (minstens 10 karakters)");
		}
		if(klant == null || klant.isEmpty()) {
			verplichteAttributen.add("klant");
		}
		if(toegewezenTechnieker== null || toegewezenTechnieker.isEmpty()) {
			verplichteAttributen.add("technieker");
		}
		
		if(type == null) {
			verplichteAttributen.add("type");
		}
		if(!verplichteAttributen.isEmpty()) {
			throw new VerplichtVeldenException("Ticket kan niet gewijzigd worden omdat volgende velden verplicht zijn: ",verplichteAttributen);
		}
		if(!fouteInvoerAttributen.isEmpty()) {
			throw new FouteInvoerException("Ticket kan niet gewijzigd worden omdat volgende velden fout zijn: ",fouteInvoerAttributen);
		}
		setTitel(titel);
		setToegewezenTechnieker(toegewezenTechnieker);
		setBedrijfKlant(klant);
		setType(type);
		setOmschrijving(omschrijving);
		setOpmerking(opmerking);
	}
	
	@Override
	@Access(AccessType.PROPERTY)
	public String getTitel() 
	{
		return titel.get();
	}
	
	@Override
	public StringProperty titel() 
	{
		return titel;
	}

	@Override
	public Long getTicketID() 
	{
		return ticketID;
	}

	public void setTicketID(Long ticketID) 
	{
		this.ticketID = ticketID;
	}

	@Override
	@Access(AccessType.PROPERTY)
	public TypeTicket getType() 
	{
		return TypeTicket.valueOf(this.type.get());
	}
	
	@Override
	public StringProperty type() 
	{
		return this.type;
	}

	@Override
	@Access(AccessType.PROPERTY)
	public String getOmschrijving() 
	{
		return omschrijving.get();
	}
	
	@Override
	public StringProperty omschrijving() 
	{
		return this.omschrijving;
	}

	@Override
	public LocalDate getDatumAanmaak() 
	{
		return datumAanmaak;
	}

	public void setDatumAanmaak(LocalDate datumAanmaak) 
	{
		this.datumAanmaak = datumAanmaak;
	}

	@Override
	@Access(AccessType.PROPERTY)
	public String getOpmerking() 
	{
		return opmerking.get();
	}
	
	@Override
	public StringProperty opmerking()
	{
		return this.omschrijving;
	}
	
	@Override
	@Access(AccessType.PROPERTY)
	public StatusTicket getStatus() 
	{
		return StatusTicket.valueOf(this.status.get());
	}
	
	@Override
	public StringProperty status() 
	{
		return this.status;
	}
	
	@Override
	@Access(AccessType.PROPERTY)
	public String getToegewezenTechnieker()
	{
		return toegewezenTechnieker.get();
	}
	
	@Override
	public StringProperty toegewezenTechnieker() 
	{
		return this.toegewezenTechnieker;
	}

	@Override
	@Access(AccessType.PROPERTY)
	public String getBedrijfKlant() 
	{
		return bedrijfKlant.get();
	}
	
	@Override
	public StringProperty bedrijfKlant() 
	{
		return bedrijfKlant;
	}
	
	public Klant getK() 
	{
		return k;
	}

	public void setK(Klant k) 
	{
		this.k = k;
	}

	public Contract getContract() 
	{
		return contract;
	}

	public void setContract(Contract contract) 
	{
		this.contract = contract;
	}
	
	public void setTitel(String titel) 
	{
		if(titel==null || titel.isBlank()) {
			throw new IllegalArgumentException("Titel mag niet leeg zijn!");
		}
		if(titel.length() < 5) {
			throw new IllegalArgumentException("Titel moet minstens 5 karakters bevatten!");
		}
		this.titel.set(titel);
	}

	public void setToegewezenTechnieker(String toegewezenTechnieker) 
	{
		this.toegewezenTechnieker.set(toegewezenTechnieker);
	}

	public void setBedrijfKlant(String bedrijfKlant) 
	{
		if(bedrijfKlant==null || bedrijfKlant.isBlank()) {
			throw new IllegalArgumentException("BedrijfKlant mag niet leeg zijn!");
		}
		this.bedrijfKlant.set(bedrijfKlant);
	}

	public void setStatus(StatusTicket status) 
	{
		if(status==null) {
			throw new IllegalArgumentException("StatusTicket mag niet leeg zijn");
		}
		this.status.set(status.toString());
	}

	public void setType(TypeTicket type) 
	{
		if(type==null) {
			throw new IllegalArgumentException("TypeTicket mag niet leeg zijn");
		}
		this.type.set(type.toString());
	}

	public void setOmschrijving(String omschrijving)
	{
		if(omschrijving==null || omschrijving.isBlank()) {
			throw new IllegalArgumentException("Omschrijving mag niet leeg zijn!");
		}
		if(omschrijving.length() < 10) {
			throw new IllegalArgumentException("Omschrijving moet minstens 5 karakters bevatten!");
		}
		this.omschrijving.set(omschrijving);
	}

	public void setOpmerking(String opmerking) 
	{
		this.opmerking.set(opmerking);
	}
}
