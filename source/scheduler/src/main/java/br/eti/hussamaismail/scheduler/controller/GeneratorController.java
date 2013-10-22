package br.eti.hussamaismail.scheduler.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.Chart;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialogs;
import javafx.scene.control.Separator;
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
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
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
import br.eti.hussamaismail.scheduler.domain.SporadicPolicy;
import br.eti.hussamaismail.scheduler.domain.SporadicTask;
import br.eti.hussamaismail.scheduler.domain.Task;
import br.eti.hussamaismail.scheduler.exception.DeadlineNotSatisfiedException;
import br.eti.hussamaismail.scheduler.exception.SchedulabilityConditionNotSatisfiedException;
import br.eti.hussamaismail.scheduler.util.EditingCell;
import br.eti.hussamaismail.scheduler.util.TasksUtil;

import com.sun.prism.impl.Disposer.Record;

@SuppressWarnings({ "unchecked", "rawtypes", "restriction" })
public class GeneratorController implements Initializable {

	private static final Logger log = LoggerFactory.getLogger(MainApp.class);

	public static List<Task> TASKS =  new ArrayList<Task>();
	public static int TIME_SLOT = 1;
	public static Task TASK;
	
	private TasksUtil tasksUtil;
	private AreaChart<Number, Number> serverCapacity;
	private Stage stageServerCapacity;
	
	@FXML private static TableView<Task> tasksTable;
	@FXML private Pane chartPanel;

	@FXML private TableColumn taskNameColumn;
	@FXML private TableColumn taskComputationTimeColumn;
	@FXML private TableColumn taskPeriodColumn;
	@FXML private TableColumn taskDeadlineColumn;
	@FXML private TableColumn taskTypeColumn;
	@FXML private TableColumn taskActivationTimeColumn;
	
	@FXML private ChoiceBox techniqueChoiceBox;
	@FXML private ChoiceBox sporadicPolicy;
	@FXML private GridPane simulateControl;
	@FXML private CheckBox preemptiveCheckBox;	

	@FXML private RadioButton radioRateMonotonic;
	@FXML private RadioButton radioDeadlineMonotonic;
	@FXML private RadioButton radioEDF;

	public GeneratorController() {
		this.tasksUtil = TasksUtil.getInstance();
	}

	public void configureTable() {
			
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
				task.setComputationTime(t.getNewValue());
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
				task.setActivationTime(t.getNewValue());
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
								this.setText("Periódica");
							} else {
								this.setText("Esporádica");
							}
						}
					}
				};
				return cell;
			}
		});
		
        TableColumn col_action = new TableColumn<>("");
        col_action.setSortable(false);
         
        col_action.setCellValueFactory(
                new Callback<TableColumn.CellDataFeatures<Record, Boolean>, 
                ObservableValue<Boolean>>() {
 
            @Override
            public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<Record, Boolean> p) {
                return new SimpleBooleanProperty(p.getValue() != null);
            }
        });
	 
	        col_action.setCellFactory(
	                new Callback<TableColumn<Record, Boolean>, TableCell<Record, Boolean>>() {
	 
	            @Override
	            public TableCell<Record, Boolean> call(TableColumn<Record, Boolean> p) {
	                return new ButtonCell();
	            }
	         
	        });
        tasksTable.getColumns().add(col_action);
	}

	public void simulate() throws DeadlineNotSatisfiedException{
		
		/* Verifica se existem tarefas a serem escalonadas */
		if (GeneratorController.TASKS.size() == 0){
			Dialogs.showWarningDialog(MainApp.STAGE, "É necessário adicionar tarefas a serem escalonadas", "Não foi possível realizar a operação", "Alerta");
			return;
		}

		Chart simulatedChart = null;
		this.serverCapacity = null;
		boolean preemptive = preemptiveCheckBox.isSelected();
		tasksUtil.resetAllTasks(TASKS);
		
		Object techniqueSelected = techniqueChoiceBox.getSelectionModel().getSelectedItem();
		techniqueSelected = techniqueSelected != null ? techniqueSelected.toString().toUpperCase().replaceAll(" ", "") : "";
		
		boolean existsSporadic = existsSporadicTasks();	
		/* Limita o trabalho com tarefas esporadicas somente ao EDF */
		if ((!"EARLIESTDEADLINEFIRST".equals(techniqueSelected.toString())) && existsSporadic){
			Dialogs.showWarningDialog(MainApp.STAGE, "Apenas a tecnica 'Earliest Deadline First' suporta tarefas esporadicas", "Não foi possível realizar a operação", "Alerta");
			return;
		}
		
		/* Verifica se alguma politica para as tarefas esporadicas foi selecionada */
		if (existsSporadic && (sporadicPolicy.getValue() == null || sporadicPolicy.getValue().toString().length() == 0)){
			Dialogs.showWarningDialog(MainApp.STAGE, "É necessário escolher uma política de escalonamento para as tarefas esporádicas", "Não foi possível realizar a operação", "Alerta");
			return;
		}
		
		try{
			switch ((String)techniqueSelected){
			
				case "RATEMONOTONIC" : 
					
					RateMonotonicScheduler rateMonotonicScheduler = new RateMonotonicScheduler();
					rateMonotonicScheduler.setTasks(TASKS);
					rateMonotonicScheduler.setPreemptive(preemptive);
					simulatedChart = rateMonotonicScheduler.simulate();
					
				break;
				
				case "DEADLINEMONOTONIC" : 
					
					DeadlineMonotonicScheduler deadlineMonotonicScheduler = new DeadlineMonotonicScheduler();
					deadlineMonotonicScheduler.setTasks(TASKS);
					deadlineMonotonicScheduler.setPreemptive(preemptive);
		
					simulatedChart = deadlineMonotonicScheduler.simulate();
					
				break;
				
				case "EARLIESTDEADLINEFIRST" : 
										
					String sporadicPolicyString = sporadicPolicy.getValue().toString().toUpperCase().replaceAll(" ", "");
					EarliestDeadlineFirstScheduler edfScheduler = new EarliestDeadlineFirstScheduler();
					edfScheduler.setTasks(TASKS);
					
					if (sporadicPolicyString.equals("SERVIDORBACKGROUND")){
						edfScheduler.setSporadicPolicy(SporadicPolicy.BACKGROUND_SERVER);
						log.debug("Politica Background: " + edfScheduler.getSporadicPolicy());
					}
					
					if (sporadicPolicyString.equals("SERVIDORPOLLING")){
						if (!existsTaskServer()){
							Dialogs.showWarningDialog(MainApp.STAGE, "Para utilizar essa política para as tarefas esporádicas é necessário criar uma tarefa periódica com o nome de 'TS'.", "Não foi possível realizar a operação", "Alerta");
							return;
						}
						edfScheduler.setSporadicPolicy(SporadicPolicy.POLLING_SERVER);
						log.debug("Politica Background: " + edfScheduler.getSporadicPolicy());
					}
					
					if (sporadicPolicyString.equals("SERVIDORSPORADIC")){
						if (!existsTaskServer()){
							Dialogs.showWarningDialog(MainApp.STAGE, "Para utilizar essa política para as tarefas esporádicas é necessário criar uma tarefa periódica com o nome de 'TS'.", "Não foi possível realizar a operação", "Alerta");
							return;
						}
						edfScheduler.setSporadicPolicy(SporadicPolicy.SPORADIC_SERVER);
						log.debug("Politica Background: " + edfScheduler.getSporadicPolicy());
					}
				
					simulatedChart = edfScheduler.simulate();
					this.serverCapacity = (AreaChart<Number, Number>) edfScheduler.getServerCapacity();
					
				break;
				
				case "ROUNDROBIN" : 
					Integer partTimeRB = getTimeSlotFromUser("Round Robin");
					RoundRobinScheduler roundRobinScheduler = new RoundRobinScheduler();
					roundRobinScheduler.setTasks(TASKS);
					roundRobinScheduler.setSlotSize(partTimeRB);
					simulatedChart = roundRobinScheduler.simulate();
					
				break;
				
				case "LEASTLAXITY" : 
					Integer partTimeLL = getTimeSlotFromUser("Least Laxity");
					LeastLaxityScheduler leastLaxityScheduler = new LeastLaxityScheduler();
					leastLaxityScheduler.setTasks(TASKS);
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
		
		Separator separator = new Separator();
		separator.setVisible(false);
		separator.setOrientation(Orientation.VERTICAL);
		separator.setMinHeight(15);
		
		Label lbDiagramaTemporal = new Label();
		lbDiagramaTemporal.setText("  Diagrama Temporal");
		lbDiagramaTemporal.setStyle("-fx-text-fill: #e1fdff; -fx-font-weight: bold;");

		VBox vBox = new VBox();
		vBox.setPrefHeight(chartPanel.getHeight() - simulateControl.getHeight() - 20);
		vBox.setMaxHeight(chartPanel.getHeight() - simulateControl.getHeight() - 20);
		vBox.setMinHeight(chartPanel.getHeight() - simulateControl.getHeight() - 20);
		vBox.setMaxWidth(chartPanel.getWidth());
		vBox.setPrefWidth(chartPanel.getWidth());
		vBox.setMinWidth(chartPanel.getWidth());

		vBox.getChildren().add(separator);
		vBox.getChildren().add(lbDiagramaTemporal);
		vBox.getChildren().add(simulatedChart);	
		
		if (serverCapacity != null){
			Label lbServerCapacity = new Label();
			lbServerCapacity.setText("  Capacidade do Servidor");
			lbServerCapacity.setStyle("-fx-text-fill: #e1fdff; -fx-font-weight: bold;");
			vBox.getChildren().add(lbServerCapacity);

			this.serverCapacity.legendSideProperty().set(Side.RIGHT);
			simulatedChart.legendSideProperty().set(Side.RIGHT);
			vBox.getChildren().add(this.serverCapacity);			
		}
		
		chartPanel.getChildren().add(vBox);
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		techniqueChoiceBox.setItems(FXCollections.observableArrayList(
				"RateMonotonic", "Deadline Monotonic",
				"Earliest Deadline First", "Round Robin", "Least Laxity"));
		configureTable();
		
		PeriodicTask t1 = new PeriodicTask();
		t1.setName("T1");
		t1.setComputationTime(3);
		t1.setPeriod(20);
		t1.setDeadline(7);
		
		PeriodicTask t2 = new PeriodicTask();
		t2.setName("T2");
		t2.setComputationTime(2);
		t2.setPeriod(5);
		t2.setDeadline(4);
		
		PeriodicTask ts = new PeriodicTask();
		ts.setName("TS");
		ts.setComputationTime(2);
		ts.setPeriod(10);
		ts.setDeadline(8);
		
		SporadicTask s1 = new SporadicTask();
		s1.setName("T3");
		s1.setComputationTime(1);
		s1.setActivationTime(3);
		
		SporadicTask s2 = new SporadicTask();
		s2.setName("T4");
		s2.setComputationTime(1);
		s2.setActivationTime(6);
		
//		SporadicTask s3 = new SporadicTask();
//		s3.setName("T4");
//		s3.setComputationTime(1);
//		s3.setActivationTime(10);
				
		GeneratorController.TASKS.add(t1);
		GeneratorController.TASKS.add(t2);
		GeneratorController.TASKS.add(ts);
		GeneratorController.TASKS.add(s1);
		GeneratorController.TASKS.add(s2);
//		GeneratorController.STASKS.add(s3);
		
		tasksTable.getItems().addAll(GeneratorController.TASKS);
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
	
	/**
	 * Metodo que exibe a tela de cadastro para tarefas
	 * periodicas e esporadicas.
	 * 
	 */
	public void addTask(){
		
		GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(0, 10, 0, 10));
        
        final TextField taskName = new TextField();
        grid.add(new Label("Nome:"), 0, 0);
        grid.add(taskName, 1, 0); 	
        
        ObservableList<String> options = 
		    FXCollections.observableArrayList(
		        "Periódica",
		        "Esporádica"
		    );
        
        final ComboBox<String> taskType = new ComboBox(options);
        taskType.setPromptText("Periódica");
        taskType.setValue("Periódica");
        grid.add(new Label("Tipo:"), 2, 0);
        grid.add(taskType, 3, 0);

        final TextField computationTime = new TextField();
        grid.add(new Label("Tempo de Computação: "), 0, 1);       
        grid.add(computationTime, 1, 1);
        
        final TextField period = new TextField();        
        grid.add(new Label("Período: "), 2, 1);
        grid.add(period, 3, 1);
        
        final TextField deadline = new TextField();        
        grid.add(new Label("Deadline: "), 0, 2);
        grid.add(deadline, 1, 2);
        
        final TextField activationTime = new TextField();        
        grid.add(new Label("Tempo de Ativação: "), 2, 2);
        grid.add(activationTime, 3, 2);
                
        Callback<Void, Void> myCallback = new Callback<Void, Void>() {
        	@Override
        	public Void call(Void arg0) {
        		
        		try{
	        		if ("PERIÓDICA".equals(taskType.getValue().toUpperCase())){
	        			GeneratorController.TASK = new PeriodicTask();
	        		}
	        		if ("ESPORÁDICA".equals(taskType.getValue().toUpperCase())){
	        			GeneratorController.TASK = new SporadicTask();
	        		}
	        		
	        		if (GeneratorController.TASK instanceof PeriodicTask){
	        			PeriodicTask pTask = (PeriodicTask) GeneratorController.TASK;
	        			
	        			pTask.setName(taskName.getText());
	        			if (existsTaskName(pTask.getName()) == false){
	        			
		        			pTask.setComputationTime(Integer.valueOf(computationTime.getText()));
		        			pTask.setPeriod(Integer.valueOf(period.getText()));
		        			pTask.setDeadline(Integer.valueOf(deadline.getText()));
		        			pTask.setActivationTime(Integer.valueOf(activationTime.getText()));
		        			
		        			GeneratorController.TASKS.add(pTask);	        			
		        			ObservableList<Task> observableArrayList = FXCollections.observableArrayList(GeneratorController.TASKS);	        			
		        			GeneratorController.tasksTable.setItems(observableArrayList);
	        			}
	        		}
	        		if (GeneratorController.TASK instanceof SporadicTask){
	        			SporadicTask sTask = (SporadicTask) GeneratorController.TASK;
	        			
	        			sTask.setName(taskName.getText());
	        			if (existsTaskName(sTask.getName()) == false){
	        			
	        				sTask.setComputationTime(Integer.valueOf(computationTime.getText()));
	        				sTask.setActivationTime(Integer.valueOf(activationTime.getText()));
		        			
		        			GeneratorController.TASKS.add(sTask);	        			
		        			ObservableList<Task> observableArrayList = FXCollections.observableArrayList(GeneratorController.TASKS);	        			
		        			GeneratorController.tasksTable.setItems(observableArrayList);
	        			}
	        		}
	        		
        		}catch(NumberFormatException ne){
        			Dialogs.showWarningDialog(MainApp.STAGE,"Apenas números inteiros e maiores que 0 (zero) são permitidos", "Erro de Validação", "Atenção");       
        			addTask();
        		}        		        		
        		return null;
        	}
        };
        
        Dialogs.showCustomDialog(MainApp.STAGE, grid, "Adicionar Tarefa", "Entrada de Dados", DialogOptions.OK, myCallback);
	}
	
	public void showServerCapacity(){
		
		if (this.serverCapacity == null){
			Dialogs.showWarningDialog(MainApp.STAGE, "Não existe nenhum gráfico de capacidade de servidor disponível.", "Não foi possível realizar a operação", "Alerta");
			return;
		}else{
			
			AreaChart<Number, Number> temp = new AreaChart<Number, Number>(
					this.serverCapacity.getXAxis(), 
					this.serverCapacity.getYAxis(), 
					this.serverCapacity.getData());
					
			this.stageServerCapacity = new Stage();					
			this.stageServerCapacity.setTitle("Capacidade do Servidor");
			Scene scene  = new Scene(temp,800,600);
	        this.stageServerCapacity.setScene(scene);			
            this.stageServerCapacity.show();
		}		
	}
	
	private boolean existsTaskName(String taskName){		
		for(Task task : GeneratorController.TASKS){
			if (taskName.equals(task.getName())){
    			Dialogs.showWarningDialog(MainApp.STAGE,"Já existe uma tarefa cadastrada com o nome '"+taskName+"'", "Erro de Validação", "Atenção");
    			return true;
			}
		}
		return false;
	}
	
	private class ButtonCell extends TableCell<Record, Boolean> {
        final Button cellButton = new Button("X");
         
        ButtonCell(){             
            cellButton.setOnAction(new EventHandler<ActionEvent>(){
 
                @Override
                public void handle(ActionEvent t) {
                	  int selectdIndex = getTableRow().getIndex();
                      GeneratorController.tasksTable.getItems().remove(selectdIndex);
                      GeneratorController.TASKS.remove(selectdIndex);
                }
            });
        }
 
        @Override
        protected void updateItem(Boolean t, boolean empty) {
            super.updateItem(t, empty);
            if(!empty){
                setGraphic(cellButton);
            }
        }
    }
	
	private boolean existsSporadicTasks(){
		for (Task task : GeneratorController.TASKS) {
			if (task instanceof SporadicTask){
				return true;
			}			
		}
		return false;
	}
	
	private boolean existsTaskServer(){
		for (Task task : GeneratorController.TASKS) {
			if (task instanceof PeriodicTask && task.getName().equals("TS")){
				return true;				
			}			
		}
		return false;
	}
}
