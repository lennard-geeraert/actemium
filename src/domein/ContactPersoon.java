package domein;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import exceptions.FouteInvoerException;
import exceptions.VerplichtVeldenException;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

@Entity
@Table(name = "Contactpersoon")
@Access(AccessType.FIELD)
public class ContactPersoon implements IContactPersoon 
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	@Transient
	private StringProperty email;
	@Transient
	private StringProperty firstName;
	@Transient
	private StringProperty lastName;
	
	public ContactPersoon(String firstName, String lastName, String email) 
	{
		this();
		if(firstName==null||firstName.isBlank()) {
			throw new IllegalArgumentException();
		}
		if(lastName==null||lastName.isBlank()) {
			throw new IllegalArgumentException();
		}
		if(email==null||email.isBlank()) {
			throw new IllegalArgumentException();
		}
		if(!isValid(email)) {
			throw new IllegalArgumentException();
		}
		setFirstName(firstName);
		setLastName(lastName);
		setEmail(email);
	}
	
	public ContactPersoon(Builder builder) 
	{
		this();
		setFirstName(builder.firstName);
		setLastName(builder.lastName);
		setEmail(builder.email);
	}
	
	protected ContactPersoon() 
	{
		this.email = new SimpleStringProperty();
		this.firstName = new SimpleStringProperty();
		this.lastName = new SimpleStringProperty();
	}
	
	@Override
	@Access(AccessType.PROPERTY)
	public String getFirstName() 
	{
		return firstName.get();
	}
	
	public void setFirstName(String firstName) 
	{
		this.firstName.set(firstName);
	}
	
	@Override
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
	
	@Override
	public StringProperty lastName()
	{
		return this.lastName;
	}
	
	@Override
	@Access(AccessType.PROPERTY)
	public String getEmail() 
	{
		return email.get();
	}
	
	public void setEmail(String email) 
	{
		this.email.set(email);
	}
	
	@Override
	public StringProperty email()
	{
		return this.email;
	}
	
	public static class Builder
	{
		private String firstName;
		private String lastName;
		
		private String email;
		
		public Builder firstName(String firstName) {
			this.firstName= firstName;
			return this;
		}
		
		public Builder lastName(String lastName) {
			this.lastName= lastName;
			return this;
		}
		
		public Builder email (String email) {
			this.email=email;
			return this;
		}
		
		public ContactPersoon build() throws VerplichtVeldenException, FouteInvoerException
		{
			List<String> verplichteAttributen = new ArrayList<String>();
			List<String> fouteInvoerAttributen = new ArrayList<String>();
			ContactPersoon c = null;
			
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
			if(email == null || email.isBlank()) {
				verplichteAttributen.add("email");
			}
			if(!isValid(email)) {
				fouteInvoerAttributen.add("email (bv : john123@gmail.com)");
			}
			c = new ContactPersoon(this);
			if(!verplichteAttributen.isEmpty()) {
				throw new VerplichtVeldenException("Contactpersoon kan niet aangemaakt worden omdat volgende velden verplicht zijn: ",verplichteAttributen);
			}if(!fouteInvoerAttributen.isEmpty()) {
				throw new FouteInvoerException("Contactpersoon kan niet aangemaakt worden omdat volgende velden fout zijn: ", fouteInvoerAttributen);
			}
			return c;
		}
	}
	
	static boolean isValid(String email) 
	{
	   String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
	   return email.matches(regex);
	}
}
