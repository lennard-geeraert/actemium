package domein;

import java.time.LocalDate;

import domein.enums.StatusTicket;
import domein.enums.TypeTicket;
import javafx.beans.property.StringProperty;

public interface ITicket 
{
	String getTitel();

	StringProperty titel();

	Long getTicketID();

	TypeTicket getType();

	StringProperty type();

	String getOmschrijving();

	StringProperty omschrijving();

	LocalDate getDatumAanmaak();

	String getOpmerking();

	StringProperty opmerking();

	StatusTicket getStatus();

	StringProperty status();

	String getToegewezenTechnieker();

	StringProperty toegewezenTechnieker();

	String getBedrijfKlant();

	StringProperty bedrijfKlant();

}