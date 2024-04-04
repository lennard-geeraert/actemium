package domein;

import javafx.beans.property.StringProperty;

public interface IContactPersoon 
{
	String getFirstName();
	StringProperty firstName();
	String getLastName();
	StringProperty lastName();
	String getEmail();
	StringProperty email();
}