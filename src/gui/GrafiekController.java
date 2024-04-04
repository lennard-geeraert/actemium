package gui;

import java.util.ArrayList;
import java.util.List;

import controllers.SupportManagerController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

public class GrafiekController 
{	
	private SupportManagerController controller;
	@FXML
	private BarChart<String,Number> barchart;
	@FXML
	private HBox hbox = new HBox();
	@FXML
	private Button knopTicketsWeek = new Button();
	@FXML
	private Button knopTicketsStatus = new Button();
	
	private CategoryAxis xAxis = new CategoryAxis();
	
	private NumberAxis yAxis = new NumberAxis();
	
	public GrafiekController(SupportManagerController controller) 
	{
		this.controller = controller;
		this.barchart = new BarChart<>(xAxis, yAxis);
		this.barchart.setTitle("Aantal text");
		this.xAxis.setLabel("x");
		this.yAxis.setLabel("y");

		xAxis.setCenterShape(true);
		xAxis.setTickLabelGap(100);
	}
	
	@FXML
    public void initialize() 
	{
		aantalTicketsPerMaandInitialez();
    }
	
	public void aantalTicketsPerMaandInitialez() 
	{				
		String[] maanden = {"jan","feb","maart","apr","mei","jun","jul","aug","sep","okt","nov","dec"};
		
		List<XYChart.Series<String, Number>> chartCollection = new ArrayList<>();
		List<String> ticketsPerMaandLijst = this.controller.geefTicketPerMaand();
		
		ticketsPerMaandLijst.stream().forEach(el -> {
			XYChart.Series<String,Number> localserie = new XYChart.Series();
			localserie.setName(maanden[Integer.parseInt(el.split("=")[0])-1]);       
			localserie.getData().add(new XYChart.Data(maanden[Integer.parseInt(el.split("=")[0])-1], Integer.parseInt(el.split("=")[1])));	
			chartCollection.add(localserie);
		});
				
		this.barchart.getData().addAll(chartCollection);
	}
	
	@FXML
	public void filterTicketWeek(ActionEvent e) 
	{
		System.out.println("clicked");
		this.barchart.getData().clear();
		aantalTicketsPerMaandInitialez();
	}
	
	@FXML
	public void filterTicketStatus(ActionEvent e) 
	{
		this.barchart.getData().clear();
		List<XYChart.Series<String, Number>> chartCollection = new ArrayList<>();
		List<String> ticketsPerMaandLijst = this.controller.geefTicketPerStatus();
		
		ticketsPerMaandLijst.stream().forEach(el->{
			XYChart.Series<String,Number> localserie = new XYChart.Series();
			localserie.setName(el.split("=")[0]);       
			localserie.getData().add(new XYChart.Data<String, Number>(el.split("=")[0],Integer.parseInt(el.split("=")[1])));	
			chartCollection.add(localserie);
		});
		
		this.barchart.getData().addAll(chartCollection);		
	}
}
