package br.eti.hussamaismail.scheduler.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.Chart;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.util.Callback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.eti.hussamaismail.scheduler.MainApp;
import br.eti.hussamaismail.scheduler.domain.PeriodicTask;
import br.eti.hussamaismail.scheduler.domain.RateMonotonicScheduler;
import br.eti.hussamaismail.scheduler.domain.Task;
import br.eti.hussamaismail.scheduler.util.TasksUtil;

public class MainController implements Initializable {
	
    private static final Logger log = LoggerFactory.getLogger(MainApp.class);
    	
	public List<Task> tasks;
	private TasksUtil tasksUtil;
	private ToggleGroup radioButtonsGroup;
	
	@FXML private TableView<Task> tasksTable;
	@FXML private Pane chartPanel;
	
	@FXML private TableColumn<Task, String> taskNameColumn;
	@FXML private TableColumn<Task, String> taskComputationTimeColumn;
	@FXML private TableColumn<Task, String> taskPeriodColumn;
	@FXML private TableColumn<Task, String> taskDeadlineColumn;
	@FXML private TableColumn<Task, String> taskColorColumn;
	@FXML private TableColumn<Task, String> taskTypeColumn;
	
	@FXML private RadioButton radioRateMonotonic;
	@FXML private RadioButton radioDeadlineMonotonic;
	@FXML private RadioButton radioEDF;
	
	public MainController() {
		this.tasks = new ArrayList<Task>();
		this.tasksUtil = TasksUtil.getInstance();
		this.radioButtonsGroup = new ToggleGroup();		
	}
	
	public void openNewTaskDialog(){
		
		ObservableList<Task> list = FXCollections.observableList(tasks);
		taskNameColumn.setCellValueFactory(new PropertyValueFactory<Task, String>("name"));	
		taskComputationTimeColumn.setCellValueFactory(new PropertyValueFactory<Task, String>("computationTime"));
		taskPeriodColumn.setCellValueFactory(new PropertyValueFactory<Task, String>("period"));
		taskDeadlineColumn.setCellValueFactory(new PropertyValueFactory<Task, String>("deadline"));		
		taskColorColumn.setCellFactory(new Callback<TableColumn<Task,String>, TableCell<Task,String>>() {			
			@SuppressWarnings({ "rawtypes", "unchecked" })
			public TableCell<Task, String> call(TableColumn<Task, String> arg0) {
				TableCell cell = new TableCell<Task, Object>(){
					@Override
					protected void updateItem(Object arg0, boolean arg1) {
						super.updateItem(arg0, arg1);
						
			            TableRow currentRow = getTableRow();
			            Task task = (Task) currentRow.getItem();
			            if(task != null){
				            setStyle("-fx-background-color: " + getHexColor(task.getColor()));	
			            }			           
					}
				};	
				return cell;
			}
		});
		taskTypeColumn.setCellFactory(new Callback<TableColumn<Task,String>, TableCell<Task,String>>() {			
			@SuppressWarnings({ "rawtypes", "unchecked" })
			public TableCell<Task, String> call(TableColumn<Task, String> arg0) {
				TableCell cell = new TableCell<Task, Object>(){
					@Override
					protected void updateItem(Object arg0, boolean arg1) {
						super.updateItem(arg0, arg1);
						
			            TableRow currentRow = getTableRow();
			            Task task = (Task) currentRow.getItem();
			            if(task != null){
			            	if (task instanceof PeriodicTask){
			            		this.setText("Periodic");
			            	}else{
			            		this.setText("Sporadic");
			            	}
			            }			           
					}
				};	
				return cell;
			}
		});
		
		this.tasksTable.setItems(list);
	}
	
	public void simulate(){

		Chart simulatedChart = null;
		
		if (radioButtonsGroup.getSelectedToggle() == null){
			log.error("É necessário escolher uma técnica para simular");
			return;
		}else{
			RadioButton selectedRadio = ((RadioButton) radioButtonsGroup.getSelectedToggle());
			if (selectedRadio.equals(radioRateMonotonic)){
				RateMonotonicScheduler scheduler = new RateMonotonicScheduler();
				scheduler.setTasks(this.tasksUtil.getOnlyPeriodicTasksFromTaskList(tasks));
				simulatedChart = scheduler.simulate();
			}
		}
		
		simulatedChart.setMaxWidth(640);
		simulatedChart.setPrefWidth(640);
		simulatedChart.setMinWidth(640);
		
		simulatedChart.setPrefHeight(270);
		simulatedChart.setMaxHeight(270);
		simulatedChart.setMinHeight(270);

		chartPanel.getChildren().clear();
		chartPanel.getChildren().add(simulatedChart);
	}

	private String getHexColor(Color color){
		
		if (color.equals(Color.RED)){
			return "#b22924";
		}else if (color.equals(Color.BLUE)){
			return "#81a3d0";
		}else if (color.equals(Color.GREEN)){
			return "#8cb990";		
		}else if (color.equals(Color.YELLOW)){
			return "#e1ee6d";
		}else{
			return "#FFFFF";
		}
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		PeriodicTask t1 = new PeriodicTask();
		t1.setColor(Color.RED);
		t1.setName("t1");
		t1.setComputationTime(6.25);
		t1.setPeriod(25);
		t1.setDeadline(25);
		
		PeriodicTask t2 = new PeriodicTask();
		t2.setColor(Color.BLUE);
		t2.setName("t2");
		t2.setComputationTime(6.25);
		t2.setPeriod(50);
		t2.setDeadline(50);
		
		PeriodicTask t3 = new PeriodicTask();
		t3.setColor(Color.YELLOW);
		t3.setName("t3");
		t3.setComputationTime(40);
		t3.setPeriod(100);
		t3.setDeadline(100);
		
		tasks.add(t1);
		tasks.add(t2);
		tasks.add(t3);

		openNewTaskDialog();
		
		radioDeadlineMonotonic.setToggleGroup(radioButtonsGroup);
		radioRateMonotonic.setToggleGroup(radioButtonsGroup);
		radioEDF.setToggleGroup(radioButtonsGroup);
	}	
}
