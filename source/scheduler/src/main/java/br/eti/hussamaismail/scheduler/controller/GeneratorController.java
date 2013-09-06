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
import javafx.scene.control.CheckBox;
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

import javax.swing.JOptionPane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.eti.hussamaismail.scheduler.MainApp;
import br.eti.hussamaismail.scheduler.domain.DeadlineMonotonicScheduler;
import br.eti.hussamaismail.scheduler.domain.LeastLaxityScheduler;
import br.eti.hussamaismail.scheduler.domain.PeriodicTask;
import br.eti.hussamaismail.scheduler.domain.RateMonotonicScheduler;
import br.eti.hussamaismail.scheduler.domain.RoundRobinScheduler;
import br.eti.hussamaismail.scheduler.domain.Task;
import br.eti.hussamaismail.scheduler.exception.DeadlineNotSatisfiedException;
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
	@FXML private TableColumn taskActivationTimeColumn;
	
	@FXML private ChoiceBox techniqueChoiceBox;
	@FXML private GridPane simulateControl;
	@FXML private CheckBox preemptiveCheckBox;

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
		taskComputationTimeColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Task,Integer>>() {
			@Override
			public void handle(CellEditEvent<Task, Integer> t) {
				Task task = t.getTableView().getItems().get(t.getTablePosition().getRow());
				if (task instanceof PeriodicTask){
					PeriodicTask pTask = (PeriodicTask) task;
					pTask.setComputationTime(t.getNewValue());
				}
				
			}
		 });
		
		taskPeriodColumn.setCellFactory(cellFactory);
		taskPeriodColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Task,Integer>>() {
			@Override
			public void handle(CellEditEvent<Task, Integer> t) {
				Task task = t.getTableView().getItems().get(t.getTablePosition().getRow());
				if (task instanceof PeriodicTask){
					PeriodicTask pTask = (PeriodicTask) task;
					pTask.setPeriod(t.getNewValue());
				}
				
			}
		 });
		
		taskDeadlineColumn.setCellFactory(cellFactory);
		taskDeadlineColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Task,Integer>>() {
			@Override
			public void handle(CellEditEvent<Task, Integer> t) {
				Task task = t.getTableView().getItems().get(t.getTablePosition().getRow());
				if (task instanceof PeriodicTask){
					PeriodicTask pTask = (PeriodicTask) task;
					pTask.setDeadline(t.getNewValue());
				}
				
			}
		 });
		
		taskActivationTimeColumn.setCellFactory(cellFactory);
		taskActivationTimeColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Task,Integer>>() {
			@Override
			public void handle(CellEditEvent<Task, Integer> t) {
				Task task = t.getTableView().getItems().get(t.getTablePosition().getRow());
				if (task instanceof PeriodicTask){
					PeriodicTask pTask = (PeriodicTask) task;
					pTask.setActivationTime(t.getNewValue());
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

	public void simulate() throws DeadlineNotSatisfiedException{

		Chart simulatedChart = null;
		boolean preemptive = preemptiveCheckBox.isSelected();
		chartPanel.getChildren().clear();
		tasksUtil.resetAllTasks(tasks);
		
		Object techniqueSelected = techniqueChoiceBox.getSelectionModel().getSelectedItem();
		techniqueSelected = techniqueSelected != null ? techniqueSelected.toString().toUpperCase().replaceAll(" ", "") : "";
		
		switch ((String)techniqueSelected){
		
		case "RATEMONOTONIC" : 
			
			RateMonotonicScheduler rateMonotonicScheduler = new RateMonotonicScheduler();
			rateMonotonicScheduler.setTasks(this.tasksUtil.getOnlyPeriodicTasksFromTaskList(tasks));
			rateMonotonicScheduler.setPreemptive(preemptive);
			simulatedChart = rateMonotonicScheduler.simulate();
			
		break;
		
		case "DEADLINEMONOTONIC" : 
			
			DeadlineMonotonicScheduler deadlineMonotonicScheduler = new DeadlineMonotonicScheduler();
			deadlineMonotonicScheduler.setTasks(this.tasksUtil.getOnlyPeriodicTasksFromTaskList(tasks));
			simulatedChart = deadlineMonotonicScheduler.simulate();
			
		break;
		
		case "ROUNDROBIN" : 
			Integer partTimeRB = Integer.valueOf(JOptionPane.showInputDialog("Informe o tamanho de particionamento:"));
			RoundRobinScheduler roundRobinScheduler = new RoundRobinScheduler();
			roundRobinScheduler.setTasks(this.tasksUtil.getOnlyPeriodicTasksFromTaskList(tasks));
			roundRobinScheduler.setSlotSize(partTimeRB);
			simulatedChart = roundRobinScheduler.simulate();
			
		break;
		
		case "LEASTLAXITY" : 
			Integer partTimeLL = Integer.valueOf(JOptionPane.showInputDialog("Informe o tamanho de particionamento:"));
			LeastLaxityScheduler leastLaxityScheduler = new LeastLaxityScheduler();
			leastLaxityScheduler.setTasks(this.tasksUtil.getOnlyPeriodicTasksFromTaskList(tasks));
			leastLaxityScheduler.setSlotSize(partTimeLL);
			simulatedChart = leastLaxityScheduler.simulate();
			
		break;
		
		default : 
			JOptionPane.showMessageDialog(null, "É necessário escolher uma técnica para simular");
			log.error("É necessário escolher uma técnica para simular");
			return; 
			
		}
		
		if (simulatedChart == null){
			JOptionPane.showMessageDialog(null, "Não foi possível escalonar com a técnica desejada");
			log.error("Não foi possível escalonar com a técnica desejada");
			return;
		}
		
		simulatedChart.setMaxWidth(chartPanel.getWidth());
		simulatedChart.setPrefWidth(chartPanel.getWidth());
		simulatedChart.setMinWidth(chartPanel.getWidth());
		
		simulatedChart.setPrefHeight(chartPanel.getHeight() - simulateControl.getHeight());
		simulatedChart.setMaxHeight(chartPanel.getHeight() - simulateControl.getHeight());
		simulatedChart.setMinHeight(chartPanel.getHeight() - simulateControl.getHeight());
		
		chartPanel.getChildren().add(simulatedChart);
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		techniqueChoiceBox.setItems(FXCollections.observableArrayList(
				"RateMonotonic", "Deadline Monotonic",
				"Earliest Deadline First", "Round Robin", "Least Laxity"));

		PeriodicTask t1 = new PeriodicTask();
		t1.setName("T1");
		t1.setComputationTime(3);
		t1.setPeriod(20);
		t1.setDeadline(20);

		PeriodicTask t2 = new PeriodicTask();
		t2.setName("T2");
		t2.setComputationTime(2);
		t2.setPeriod(5);
		t2.setDeadline(5);

		PeriodicTask t3 = new PeriodicTask();
		t3.setName("T3");
		t3.setComputationTime(2);
		t3.setPeriod(10);
		t3.setDeadline(10);
		
// ROUND ROBIN 		
//		PeriodicTask t1 = new PeriodicTask();
//		t1.setName("A");
//		t1.setComputationTime(25);
//		t1.setPeriod(100);
//		t1.setDeadline(100);
//
//		PeriodicTask t2 = new PeriodicTask();
//		t2.setName("B");
//		t2.setComputationTime(20);
//		t2.setPeriod(80);
//		t2.setDeadline(80);
//
//		PeriodicTask t3 = new PeriodicTask();
//		t3.setName("C");
//		t3.setComputationTime(30);
//		t3.setPeriod(100);
//		t3.setDeadline(100);
//		
//		PeriodicTask t4 = new PeriodicTask();
//		t4.setName("D");
//		t4.setComputationTime(20);
//		t4.setPeriod(80);
//		t4.setDeadline(80);


//		PeriodicTask t1 = new PeriodicTask();
//		t1.setName("A");
//		t1.setComputationTime(30);
//		t1.setPeriod(0);
//		t1.setDeadline(40);
//		t1.setActivationTime(0);
//
//		PeriodicTask t2 = new PeriodicTask();
//		t2.setName("B");
//		t2.setComputationTime(10);
//		t2.setPeriod(0);
//		t2.setDeadline(30);
//		t2.setActivationTime(0);
//
//		PeriodicTask t3 = new PeriodicTask();
//		t3.setName("C");
//		t3.setComputationTime(30);
//		t3.setPeriod(0);
//		t3.setDeadline(100);
//		t3.setActivationTime(30);
//		
//		PeriodicTask t4 = new PeriodicTask();
//		t4.setName("D");
//		t4.setComputationTime(40);
//		t4.setPeriod(0);
//		t4.setDeadline(200);
//		t4.setActivationTime(50);
//		
//		PeriodicTask t5 = new PeriodicTask();
//		t5.setName("E");
//		t5.setComputationTime(10);
//		t5.setPeriod(0);
//		t5.setDeadline(90);
//		t5.setActivationTime(70);
		
		

//		PeriodicTask t1 = new PeriodicTask();
//		t1.setName("A");
//		t1.setComputationTime(1);
//		t1.setPeriod(2);
//		t1.setDeadline(2);
//		t1.setActivationTime(0);
//
//		PeriodicTask t2 = new PeriodicTask();
//		t2.setName("B");
//		t2.setComputationTime(4);
//		t2.setPeriod(6);
//		t2.setDeadline(6);
//		t2.setActivationTime(1);
//
//		PeriodicTask t3 = new PeriodicTask();
//		t3.setName("C");
//		t3.setComputationTime(2);
//		t3.setPeriod(3);
//		t3.setDeadline(10);
//		t3.setActivationTime(3);
		

//		PeriodicTask t1 = new PeriodicTask();
//		t1.setName("A");
//		t1.setComputationTime(2);
//		t1.setPeriod(0);
//		t1.setDeadline(7);
//		t1.setActivationTime(0);
//
//		PeriodicTask t2 = new PeriodicTask();
//		t2.setName("B");
//		t2.setComputationTime(4);
//		t2.setPeriod(0);
//		t2.setDeadline(9);
//		t2.setActivationTime(2);
//
//		PeriodicTask t3 = new PeriodicTask();
//		t3.setName("C");
//		t3.setComputationTime(5);
//		t3.setPeriod(0);
//		t3.setDeadline(13);
//		t3.setActivationTime(3);
		
//		PeriodicTask t1 = new PeriodicTask();
//		t1.setName("A");
//		t1.setComputationTime(10);
//		t1.setPeriod(0);
//		t1.setDeadline(33);
//		t1.setActivationTime(0);
//
//		PeriodicTask t2 = new PeriodicTask();
//		t2.setName("B");
//		t2.setComputationTime(3);
//		t2.setPeriod(0);
//		t2.setDeadline(28);
//		t2.setActivationTime(4);
//
//		PeriodicTask t3 = new PeriodicTask();
//		t3.setName("C");
//		t3.setComputationTime(10);
//		t3.setPeriod(0);
//		t3.setDeadline(29);
//		t3.setActivationTime(5);
		
		
//		PeriodicTask t1 = new PeriodicTask();
//		t1.setName("A");
//		t1.setComputationTime(6);
//		t1.setPeriod(2);
//		t1.setDeadline(2);
//		t1.setActivationTime(0);
//
//		PeriodicTask t2 = new PeriodicTask();
//		t2.setName("B");
//		t2.setComputationTime(5);
//		t2.setPeriod(6);
//		t2.setDeadline(6);
//		t2.setActivationTime(1);
//
//		PeriodicTask t3 = new PeriodicTask();
//		t3.setName("C");
//		t3.setComputationTime(2);
//		t3.setPeriod(3);
//		t3.setDeadline(10);
//		t3.setActivationTime(3);
//		
//		PeriodicTask t4 = new PeriodicTask();
//		t4.setName("D");
//		t4.setComputationTime(3);
//		t4.setPeriod(3);
//		t4.setDeadline(10);
//		t4.setActivationTime(3);
//		
//		PeriodicTask t5 = new PeriodicTask();
//		t5.setName("E");
//		t5.setComputationTime(7);
//		t5.setPeriod(3);
//		t5.setDeadline(10);
//		t5.setActivationTime(3);
		
		tasks.add(t1);
		tasks.add(t2);
		tasks.add(t3);
//		tasks.add(t4);
//		tasks.add(t5);

		openNewTaskDialog();
	}
}
