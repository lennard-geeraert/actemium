package domein;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import domein.enums.RolWerknemer;
import domein.enums.StatusGebruiker;
import domein.enums.StatusTicket;
import exceptions.FouteInvoerException;
import exceptions.VerplichtVeldenException;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

@Entity
@Table(name = "Werknemer")
@NamedQueries({ @NamedQuery(name = "Werknemer.findAll", query = "SELECT w FROM Werknemer w"), 
	@NamedQuery(name = "Werknemer.findOne", query = "SELECT w FROM Werknemer w WHERE w.userName = :userName")})
@Access(AccessType.FIELD)
public class Werknemer extends Gebruiker implements IWerknemer 
{
	@OneToMany(cascade = CascadeType.PERSIST,fetch = FetchType.EAGER)
	private ArrayList<Ticket> tickets; 
	private int aantalPogingen;
	private boolean aanmeldenGelukt;
	private LocalDate aanmeldDatum;
	@Transient
	private StringProperty rol;
	@Transient
	private StringProperty lastName;
	@Transient
	private StringProperty firstName;
	
	public Werknemer(String firstName, String lastName, String password, LocalDate date, String userName, RolWerknemer rol, String adres, String email,String telefoonnummers)
	{
		super(password, date, userName, email, adres,telefoonnummers);
		this.lastName = new SimpleStringProperty();
		this.firstName = new SimpleStringProperty();
		this.rol = new SimpleStringProperty();
		setFirstName(firstName);
		setLastName(lastName);
		setRol(rol);
		this.tickets= new ArrayList<Ticket>();
	}
	
	public Werknemer(Builder builder) 
	{
		super(builder.password, builder.datumRegistratie, builder.userName, builder.email, builder.adres,builder.telefoonnummers);
		this.lastName = new SimpleStringProperty();
		this.firstName = new SimpleStringProperty();
		this.rol = new SimpleStringProperty();
		setFirstName(builder.firstName);
		setLastName(builder.lastName);
		setRol(builder.rol);
		setStatus(StatusGebruiker.Actief);
		this.tickets= new ArrayList<Ticket>();
	}
	
	protected Werknemer() 
	{
		this.lastName = new SimpleStringProperty();
		this.firstName = new SimpleStringProperty();
		this.rol = new SimpleStringProperty();
	}
	
	public void addTicket(Ticket t)
	{
		if(t==null) {
			throw new IllegalArgumentException();
		}
		tickets.add(t);
	}
	
	@Override
	public ObservableList<Ticket> getTickets()
	{
		return FXCollections.observableArrayList(tickets);
	}
	
	@Override
	public int getAantalPogingen() 
	{
		return aantalPogingen;
	}

	public void setAantalPogingen(int aantalPogingen) 
	{
		this.aantalPogingen = aantalPogingen;
	}

	@Override
	public boolean isAanmeldenGelukt() 
	{
		return aanmeldenGelukt;
	}

	public void setAanmeldenGelukt(boolean aanmeldenGelukt) 
	{
		this.aanmeldenGelukt = aanmeldenGelukt;
	}

	@Override
	public LocalDate getAanmeldDatum() 
	{
		return aanmeldDatum;
	}

	public void setAanmeldDatum(LocalDate aanmeldDatum) 
	{
		this.aanmeldDatum = aanmeldDatum;
	}
	
	@Override
	@Access(AccessType.PROPERTY)
	public RolWerknemer getRol() 
	{
		return RolWerknemer.valueOf(this.rol.get());
	}

	public void setRol(RolWerknemer rol) 
	{
		this.rol.set(rol.toString());
	}
	
	public StringProperty rol() 
	{
		return this.rol;
	}
	
	@Override
	@Access(AccessType.PROPERTY)
	public String getFirstName() 
	{
		return firstName.get();
	}
	
	public void setFirstName(String fisrtName) 
	{
		this.firstName.set(fisrtName);
	}
	
	public StringProperty firstName() 
	{
		return this.firstName;
	}
	
	@Override
	@Access(AccessType.PROPERTY)
	public String getLastName() 
	{
		return lastName.get();
	}
	
	public void setLastName(String lastName) 
	{
		this.lastName.set(lastName);
	}
	
	public StringProperty lastName()
	{
		return this.lastName;
	}
	
	public void setTickets(ArrayList<Ticket> tickets) 
	{
		this.tickets = tickets;
	}
	
	public List<Ticket> geefAfgehandeldeTickets()
	{
		return tickets.stream().filter(t -> t.getStatus() == StatusTicket.Afgehandeld || t.getStatus() == StatusTicket.Geannuleerd).collect(Collectors.toList());
	}
	
	public static class Builder 
	{
		private String userName;
		private String password;
		private StatusGebruiker status;
		private String adres;
		private LocalDate datumRegistratie;
		private String email;
		private RolWerknemer rol;
		private String lastName;
		private String firstName;
		private String telefoonnummers;
		public Builder userName(String userName) {
			this.userName = userName;
			return this;
		}
		public Builder telefoonnummers(String telefoonnummers) {
			this.telefoonnummers = telefoonnummers;
			return this;
		}
		public Builder password(String password) {
			this.password = password;
			return this;
		}
		
		public Builder status(StatusGebruiker status) {
			this.status = status;
			return this;
		}
		
		public Builder adres(String adres) {
			this.adres = adres;
			return this;
		}
		public Builder datumReg(LocalDate date) {
			this.datumRegistratie = date;
			return this;
		}
		
		public Builder email(String email) {
			this.email = email;
			return this;
		}
		
		public Builder rol(RolWerknemer rol) {
			this.rol = rol;
			return this;
		}
		
		public Builder lastname(String lastName) {
			this.lastName = lastName;
			return this;
		}
		
		public Builder firstname(String firstname) {
			this.firstName= firstname;
			return this;
		}
		
		public Werknemer build() throws VerplichtVeldenException, FouteInvoerException 
		{
			List<String> verplichteAttributen = new ArrayList<String>();
			List<String> fouteInvoerAttributen = new ArrayList<String>();
			Werknemer w = null;
			if(firstName == null || firstName.isBlank()) {
				verplichteAttributen.add("voornaam");
			}
			if(!firstName.matches("(?i)[a-z]([- ',.a-z]{1,23}[a-z])?")) {
				fouteInvoerAttributen.add("voornaam (alleen letters)");
			}
			if(lastName == null || lastName.isBlank()) {
				verplichteAttributen.add("achteraam");
			}
			if(!lastName.matches("(?i)[a-z]([- ',.a-z]{1,23}[a-z])?")) {
				fouteInvoerAttributen.add("achternaam (alleen letters)");
			}
			if(adres == null || adres.isBlank()) {
				verplichteAttributen.add("adres");
			}
			if(email == null || email.isBlank()) {
				verplichteAttributen.add("email");
			}
			if(!isValid(email)) {
				fouteInvoerAttributen.add("email (vb. jan@gmail.com)");
			}
			if(rol == null) {
				verplichteAttributen.add("rol");
			}
			if(telefoonnummers== null || telefoonnummers.isBlank()) {
				verplichteAttributen.add("telefoonnummer");
			}
			if(!telefoonnummers.matches("\\d{4}/\\d{6}")) {
				fouteInvoerAttributen.add("telefoonnummer (vb. 0482/041472)");
			}
			if(password == null || password.isBlank()) {
				verplichteAttributen.add("wachtwoord");
			}
			if(password.length() < 6) {
				fouteInvoerAttributen.add("wachtwoord (minstens 6 karakters)");
			}
			if(userName == null || userName.isEmpty()) {
				verplichteAttributen.add("gebruikersnaam ");
			}
			if(!verplichteAttributen.isEmpty()) {
				throw new VerplichtVeldenException("Werknemer kan niet gemaakt worden omdat volgende velden verplicht zijn: ",verplichteAttributen);
			}
			if(!fouteInvoerAttributen.isEmpty()) {
				throw new FouteInvoerException("Werknemer kan niet aangemaakt worden omdat volgende velden fout zijn: ", fouteInvoerAttributen);
			}
			w = new Werknemer(this);
			return w;
		}
	}

	public void editWerknemer(String firstName, String lastName, String password, RolWerknemer rol, String userName, String adres, 
			String email, StatusGebruiker status,String tel) throws VerplichtVeldenException, FouteInvoerException 
	{
		List<String> verplichteAttributen = new ArrayList<String>();
		List<String> fouteInvoerAttributen = new ArrayList<String>();
		
		if(firstName == null || firstName.isBlank()) {
			verplichteAttributen.add("voornaam");
		}
		if(!firstName.matches("(?i)[a-z]([- ',.a-z]{1,23}[a-z])?")) {
			fouteInvoerAttributen.add("voornaam (alleen letters)");
		}
		if(tel == null || tel.isBlank()) {
			verplichteAttributen.add("telefoonnummer");
		}
		if(!tel.matches("\\d{4}/\\d{6}")) {
			fouteInvoerAttributen.add("telefoonnummer (vb. 0482/041472)");
		}
		if(userName == null || userName.isBlank()) {
			verplichteAttributen.add("username");
		}
		if(lastName == null || lastName.isBlank()) {
			verplichteAttributen.add("achteraam");
		}
		if(!lastName.matches("(?i)[a-z]([- ',.a-z]{1,23}[a-z])?")) {
			fouteInvoerAttributen.add("achternaam (alleen letters)");
		}
		if(adres == null || adres.isBlank()) {
			verplichteAttributen.add("adres");
		}
		if(email == null || email.isBlank()) {
			verplichteAttributen.add("email");
		}
		if(!isValid(email)) {
			fouteInvoerAttributen.add("email (vb. jan@gmail.com)");
		}
		if(rol == null) {
			verplichteAttributen.add("rol");
		}
		if(password == null || password.isBlank()) {
			verplichteAttributen.add("wachtwoord");
		}
		if(password.length() < 6) {
			fouteInvoerAttributen.add("wachtwoord (minstens 6 karakters)");
		}
		if(!verplichteAttributen.isEmpty()) {
			throw new VerplichtVeldenException("Werknemer kan niet gewijzigd worden omdat volgende velden verplicht zijn: ",verplichteAttributen);
		}
		if(!fouteInvoerAttributen.isEmpty()) {
			throw new FouteInvoerException("Werknemer kan niet gewijzigd worden omdat volgende velden fout zijn: ", fouteInvoerAttributen);
		}
		setFirstName(firstName);
		setLastName(lastName);
		setPassword(password);
		setRol(rol);
		setUserName(userName);
		setAdres(adres);
		setEmail(email);
		setStatus(status);
		setTelefoonummer(tel);
	}
	
	static boolean isValid(String email) 
	{
		   String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
		   return email.matches(regex);
	}

	@Override
	public void voegTicketToe(Ticket t) 
	{
		this.tickets.add(t);
	}
	
	public void editStatusWerknemer(StatusGebruiker status)
	{
		setStatus(status);
	}
}
