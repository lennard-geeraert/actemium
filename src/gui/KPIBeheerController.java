package gui;

import controllers.SupportManagerController;
import javafx.fxml.FXML;

import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

public class KPIBeheerController 
{	
	@FXML
	private CategoryAxis caMaanden = new CategoryAxis();
	@FXML
	private NumberAxis naAantalTickets = new NumberAxis();
	@FXML
	private LineChart<String,Number> linechart;
		
	public KPIBeheerController(SupportManagerController controller)
	{			
		XYChart.Series<String, Number> series = new XYChart.Series<String,Number>();
		series.setName("Maanden");
		series.getData().add(new XYChart.Data<String,Number>("Januari",1));
		series.getData().add(new XYChart.Data<>("Februari",2));
		series.getData().add(new XYChart.Data<>("Maart",3));
		series.getData().add(new XYChart.Data<>("April",4));
		series.getData().add(new XYChart.Data<>("Mei",2));
		series.getData().add(new XYChart.Data<>("Juni",5));
		series.getData().add(new XYChart.Data<>("Juli",6));
		series.getData().add(new XYChart.Data<>("Augustus",6));
		series.getData().add(new XYChart.Data<>("September",8));
		series.getData().add(new XYChart.Data<>("Oktober",2)); 
		series.getData().add(new XYChart.Data<>("November",1));
		series.getData().add(new XYChart.Data<>("December",0));
		
		linechart.getData().add(series);
	}
}
