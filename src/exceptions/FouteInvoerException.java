package exceptions;

import java.util.ArrayList;
import java.util.List;

public class FouteInvoerException extends Exception 
{
	private List<String> verplichteAttributen = new ArrayList<String>();
	
	public FouteInvoerException() 
	{
		super();
	}

	public FouteInvoerException(String message, List<String> verplichteAttributen) 
	{
		super(message);
		this.verplichteAttributen = verplichteAttributen;
	}

	public FouteInvoerException(String message) 
	{
		super(message);
	}

	public String getInformatieVanNietIngevuldeElementen() 
	{
		StringBuilder string = new StringBuilder();
		for(String s : verplichteAttributen) {
			
			string.append(s).append(", ");
			
		}
		string.delete(string.length() - 2, string.length());
		return string.toString();
	}
}
