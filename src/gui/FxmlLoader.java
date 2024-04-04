package gui;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;

public class FxmlLoader<T>
{
	private Pane view;
	
	public Pane getPage(String fileName, T wc)
	{
		try 
		{
			FXMLLoader loader = new FXMLLoader(getClass().getResource(fileName + ".fxml"));
			loader.setController(wc);
			view = loader.load();
		} catch (Exception ex) 
		{
			System.out.println(ex.getMessage());
		}
		return view;
	}
}
