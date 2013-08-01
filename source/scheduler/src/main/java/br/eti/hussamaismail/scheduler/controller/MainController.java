package br.eti.hussamaismail.scheduler.controller;

import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import br.eti.hussamaismail.scheduler.domain.PeriodicTask;
import br.eti.hussamaismail.scheduler.domain.Task;

public class MainController{
    
//	private static final Logger log = LoggerFactory.getLogger(MainController.class);
	
	@FXML private TableView<Task> tasksTable;
	
	@FXML private TableColumn<Task, String> taskNameColumn;
	@FXML private TableColumn<Task, String> taskComputationTimeColumn;
	@FXML private TableColumn<Task, String> taskPeriodColumn;
	@FXML private TableColumn<Task, String> taskDeadlineColumn;
	
	
	public void openNewTaskDialog(){
		System.out.println("CLICOU NO ADD + ");
		System.out.println(tasksTable);
		
		List<Task> tasks = new ArrayList<Task>();
		
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
		t3.setComputationTime(40);
		t3.setPeriod(100);
		t3.setDeadline(100);
		
		tasks.add(t1);
		tasks.add(t2);
		tasks.add(t3);

		
		ObservableList<Task> list = FXCollections.observableList(tasks);
		taskNameColumn.setCellValueFactory(new PropertyValueFactory<Task, String>("name"));	
		taskComputationTimeColumn.setCellValueFactory(new PropertyValueFactory<Task, String>("computationTime"));
		taskPeriodColumn.setCellValueFactory(new PropertyValueFactory<Task, String>("period"));
		taskDeadlineColumn.setCellValueFactory(new PropertyValueFactory<Task, String>("deadline"));
		
		this.tasksTable.setItems(list);
	}

}
