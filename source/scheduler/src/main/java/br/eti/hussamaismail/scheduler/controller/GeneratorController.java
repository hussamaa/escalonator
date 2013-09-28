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
import javafx.geometry.Insets;
import javafx.scene.chart.Chart;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Dialogs;
import javafx.scene.control.Dialogs.DialogOptions;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.util.Callback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.eti.hussamaismail.scheduler.MainApp;
import br.eti.hussamaismail.scheduler.domain.DeadlineMonotonicScheduler;
import br.eti.hussamaismail.scheduler.domain.EarliestDeadlineFirstScheduler;
import br.eti.hussamaismail.scheduler.domain.LeastLaxityScheduler;
import br.eti.hussamaismail.scheduler.domain.PeriodicTask;
import br.eti.hussamaismail.scheduler.domain.RateMonotonicScheduler;
import br.eti.hussamaismail.scheduler.domain.RoundRobinScheduler;
import br.eti.hussamaismail.scheduler.domain.Task;
import br.eti.hussamaismail.scheduler.exception.DeadlineNotSatisfiedException;
import br.eti.hussamaismail.scheduler.exception.SchedulabilityConditionNotSatisfiedException;
import br.eti.hussamaismail.scheduler.util.EditingCell;
import br.eti.hussamaismail.scheduler.util.TasksUtil;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class GeneratorController implements Initializable {

	private static final Logger log = LoggerFactory.getLogger(MainApp.class);

	public List<Task> tasks;
	public static int TIME_SLOT = 1;
	
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
		tasksUtil.resetAllTasks(tasks);
		
		Object techniqueSelected = techniqueChoiceBox.getSelectionModel().getSelectedItem();
		techniqueSelected = techniqueSelected != null ? techniqueSelected.toString().toUpperCase().replaceAll(" ", "") : "";
		try{
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
					deadlineMonotonicScheduler.setPreemptive(preemptive);
		
					simulatedChart = deadlineMonotonicScheduler.simulate();
					
				break;
				
				case "EARLIESTDEADLINEFIRST" : 
					
					EarliestDeadlineFirstScheduler edfScheduler = new EarliestDeadlineFirstScheduler();
					edfScheduler.setTasks(this.tasksUtil.getOnlyPeriodicTasksFromTaskList(tasks));
		
					simulatedChart = edfScheduler.simulate();
					
				break;
				
				case "ROUNDROBIN" : 
					Integer partTimeRB = getTimeSlotFromUser("Round Robin");
					RoundRobinScheduler roundRobinScheduler = new RoundRobinScheduler();
					roundRobinScheduler.setTasks(this.tasksUtil.getOnlyPeriodicTasksFromTaskList(tasks));
					roundRobinScheduler.setSlotSize(partTimeRB);
					simulatedChart = roundRobinScheduler.simulate();
					
				break;
				
				case "LEASTLAXITY" : 
					Integer partTimeLL = getTimeSlotFromUser("Least Laxity");
					LeastLaxityScheduler leastLaxityScheduler = new LeastLaxityScheduler();
					leastLaxityScheduler.setTasks(this.tasksUtil.getOnlyPeriodicTasksFromTaskList(tasks));
					leastLaxityScheduler.setSlotSize(partTimeLL);
					simulatedChart = leastLaxityScheduler.simulate();
					
				break;
				
				default : 
					
					Dialogs.showWarningDialog(MainApp.STAGE, "É necessário escolher uma técnica de escalonamento", "Não foi possível realizar a operação", "Alerta");
					log.error("É necessário escolher uma técnica para simular");
					return; 
					
			}
		}catch(DeadlineNotSatisfiedException e){
			Dialogs.showErrorDialog(MainApp.STAGE, "Ocorreu violação de deadline durante o processamento das tarefas", "Não foi possível escalonar com a técnica desejada",
				    "Erro no escalonamento", e);
			return;
		}catch(SchedulabilityConditionNotSatisfiedException e){
			Dialogs.showErrorDialog(MainApp.STAGE, "O teste de escalonabilidade não foi satifeito para as tarefas informadas pelo usuário", "Não foi possível escalonar com a técnica desejada",
				    "Erro no escalonamento", e);
			return;
		}
		
		if (simulatedChart == null){
			Dialogs.showErrorDialog(MainApp.STAGE, "Ocorreu um erro não esperado durante o processamento", "Não foi possível escalonar com a técnica desejada",null);
			log.error("Não foi possível escalonar com a técnica desejada");
			return;
		}
		
		chartPanel.getChildren().clear();
		
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
		t1.setDeadline(7);
		t1.setActivationTime(0);

		PeriodicTask t2 = new PeriodicTask();
		t2.setName("T2");
		t2.setComputationTime(2);
		t2.setPeriod(5);
		t2.setDeadline(4);
		t2.setActivationTime(0);
		
		PeriodicTask t3 = new PeriodicTask();
		t3.setName("T3");
		t3.setComputationTime(1);
		t3.setPeriod(10);
		t3.setDeadline(8);
		t3.setActivationTime(0);
		
		tasks.add(t1);
		tasks.add(t2);
		tasks.add(t3);

		openNewTaskDialog();
	}
	
	public Integer getTimeSlotFromUser(final String escalonamento){
		
		GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(0, 10, 0, 10));
        final TextField username = new TextField();
        username.setPromptText("timeSlot");

        grid.add(new Label("Informe o 'Time Slot' para o escalonamento:"), 0, 0);
        grid.add(username, 1, 0);
                        
        Callback<Void, Void> myCallback = new Callback<Void, Void>() {
        	@Override
        	public Void call(Void arg0) {
        		
        		int timeSlot = 1;
        		
        		try{
        			timeSlot = Integer.parseInt(username.getText());
        			if (timeSlot <= 0){
        				throw new NumberFormatException();
        			}
        		}catch(NumberFormatException ne){
        			Dialogs.showWarningDialog(MainApp.STAGE,"Apenas números inteiros e maiores que 0 (zero) são permitidos", "Erro de Validação", "Atenção");       
        			getTimeSlotFromUser(escalonamento);
        		}
        		
        		GeneratorController.TIME_SLOT = timeSlot;
        		return null;
        	}
        };
        
        Dialogs.showCustomDialog(MainApp.STAGE, grid, escalonamento, "Entrada de Dados", DialogOptions.OK, myCallback);
        return GeneratorController.TIME_SLOT;
	}
	
	public int getValueFromCallback(String value){
		return Integer.valueOf(value);
	}
}
