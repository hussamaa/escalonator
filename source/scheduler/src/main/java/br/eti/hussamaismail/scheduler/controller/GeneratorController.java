package br.eti.hussamaismail.scheduler.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.Chart;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.util.Callback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.eti.hussamaismail.scheduler.MainApp;
import br.eti.hussamaismail.scheduler.domain.DeadlineMonotonicScheduler;
import br.eti.hussamaismail.scheduler.domain.PeriodicTask;
import br.eti.hussamaismail.scheduler.domain.RateMonotonicScheduler;
import br.eti.hussamaismail.scheduler.domain.Task;
import br.eti.hussamaismail.scheduler.util.EditingCell;
import br.eti.hussamaismail.scheduler.util.TasksUtil;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class GeneratorController implements Initializable {

	private static final Logger log = LoggerFactory.getLogger(MainApp.class);

	public List<Task> tasks;
	private TasksUtil tasksUtil;

	@FXML private TableView<Task> tasksTable;
	@FXML private Pane chartPanel;

	@FXML private TableColumn taskNameColumn;
	@FXML private TableColumn taskComputationTimeColumn;
	@FXML private TableColumn taskPeriodColumn;
	@FXML private TableColumn taskDeadlineColumn;
	@FXML private TableColumn taskTypeColumn;

	@FXML private ChoiceBox techniqueChoiceBox;
	@FXML private GridPane simulateControl;

	@FXML private RadioButton radioRateMonotonic;
	@FXML private RadioButton radioDeadlineMonotonic;
	@FXML private RadioButton radioEDF;

	public GeneratorController() {
		this.tasks = new ArrayList<Task>();
		this.tasksUtil = TasksUtil.getInstance();
	}

	public void openNewTaskDialog() {
		
		ObservableList<Task> list = FXCollections.observableList(tasks);
		
		 Callback<TableColumn, TableCell> cellFactory = new Callback<TableColumn, TableCell>() {
			 public TableCell call(TableColumn p) {
	              return new EditingCell();
	         }
	     };
		
		taskNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		taskNameColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Task,String>>() {
			@Override
			public void handle(CellEditEvent<Task, String> t) {
				t.getTableView().getItems().get(t.getTablePosition().getRow()).setName(t.getNewValue());
			}
		 });
		
		taskComputationTimeColumn.setCellFactory(cellFactory);
		taskComputationTimeColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Task,Double>>() {
			@Override
			public void handle(CellEditEvent<Task, Double> t) {
				Task task = t.getTableView().getItems().get(t.getTablePosition().getRow());
				if (task instanceof PeriodicTask){
					PeriodicTask pTask = (PeriodicTask) task;
					pTask.setComputationTime(t.getNewValue());
				}
				
			}
		 });
		
		taskPeriodColumn.setCellFactory(cellFactory);
		taskPeriodColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Task,Double>>() {
			@Override
			public void handle(CellEditEvent<Task, Double> t) {
				Task task = t.getTableView().getItems().get(t.getTablePosition().getRow());
				if (task instanceof PeriodicTask){
					PeriodicTask pTask = (PeriodicTask) task;
					pTask.setPeriod(t.getNewValue());
				}
				
			}
		 });
		
		taskDeadlineColumn.setCellFactory(cellFactory);
		taskDeadlineColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Task,Double>>() {
			@Override
			public void handle(CellEditEvent<Task, Double> t) {
				Task task = t.getTableView().getItems().get(t.getTablePosition().getRow());
				if (task instanceof PeriodicTask){
					PeriodicTask pTask = (PeriodicTask) task;
					pTask.setDeadline(t.getNewValue());
				}
				
			}
		 });
				
		taskTypeColumn.setCellFactory(new Callback<TableColumn<Task, String>, TableCell<Task, String>>() {
			public TableCell<Task, String> call(
					TableColumn<Task, String> arg0) {
				TableCell cell = new TableCell<Task, Object>() {
					@Override
					protected void updateItem(Object arg0, boolean arg1) {
						super.updateItem(arg0, arg1);

						TableRow currentRow = getTableRow();
						Task task = (Task) currentRow.getItem();
						if (task != null) {
							if (task instanceof PeriodicTask) {
								this.setText("Periodic");
							} else {
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
	
		Object techniqueSelected = techniqueChoiceBox.getSelectionModel().getSelectedItem();
		techniqueSelected = techniqueSelected != null ? techniqueSelected.toString().toUpperCase().replaceAll(" ", "") : "";
		
		switch ((String)techniqueSelected){
		
		case "RATEMONOTONIC" : 
			
			RateMonotonicScheduler rateMonotonicScheduler = new RateMonotonicScheduler();
			rateMonotonicScheduler.setTasks(this.tasksUtil.getOnlyPeriodicTasksFromTaskList(tasks));
			simulatedChart = rateMonotonicScheduler.simulate();
			
		break;
		
		case "DEADLINEMONOTONIC" : 
			
			DeadlineMonotonicScheduler deadlineMonotonicScheduler = new DeadlineMonotonicScheduler();
			deadlineMonotonicScheduler.setTasks(this.tasksUtil.getOnlyPeriodicTasksFromTaskList(tasks));
			simulatedChart = deadlineMonotonicScheduler.simulate();
			
		break;
		
		default : 
			log.error("É necessário escolher uma técnica para simular");
			return; 
			
		}
		
		if (simulatedChart == null){
			log.error("Não foi possível escalonar com a técnica desejada");
			return;
		}
		
		simulatedChart.setMaxWidth(chartPanel.getWidth());
		simulatedChart.setPrefWidth(chartPanel.getWidth());
		simulatedChart.setMinWidth(chartPanel.getWidth());
		
		simulatedChart.setPrefHeight(chartPanel.getHeight() - simulateControl.getHeight());
		simulatedChart.setMaxHeight(chartPanel.getHeight() - simulateControl.getHeight());
		simulatedChart.setMinHeight(chartPanel.getHeight() - simulateControl.getHeight());

		System.out.println(chartPanel.getHeight());
		System.out.println(simulateControl.getHeight());
		
		chartPanel.getChildren().clear();
		chartPanel.getChildren().add(simulatedChart);
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		techniqueChoiceBox.setItems(FXCollections.observableArrayList(
				"RateMonotonic", "Deadline Monotonic",
				"Earliest Deadline First"));

		PeriodicTask t1 = new PeriodicTask();
		t1.setName("t1");
		t1.setComputationTime(6.25);
		t1.setPeriod(25);
		t1.setDeadline(25);

		PeriodicTask t2 = new PeriodicTask();
		t2.setName("t2");
		t2.setComputationTime(6.25);
		t2.setPeriod(50);
		t2.setDeadline(50);

		PeriodicTask t3 = new PeriodicTask();
		t3.setName("t3");
		t3.setComputationTime(20);
		t3.setPeriod(100);
		t3.setDeadline(100);

		tasks.add(t1);
		tasks.add(t2);
		tasks.add(t3);

		openNewTaskDialog();
	}
}
