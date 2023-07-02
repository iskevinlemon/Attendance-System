                                                                                                                                                                                                                                                  
import javafx.event.ActionEvent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.event.EventHandler;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.scene.control.Alert.AlertType;
public class Attendance_System extends Application {

	
	private Button viewAll = new Button();
	private Button btAdd = new Button("Add Attendee");
	private Button markAtd = new Button("Manage Attendees");                                      
	private Button btDelete = new Button("Delete Attendee");
	private Label total = new Label ("");
	
	private Label pwd = new Label("Enter password");
	private PasswordField pf = new PasswordField();
	private Button loginB = new Button("Login");
	private Label errorM = new Label ("");
	private HBox hbPanee2 = new HBox(100);
	
	private Label lTitle = new Label();
	private MenuBar menuBar = new MenuBar(); 
	
	private TextArea taResults = new TextArea(); //display results

	
	//quick search
	private Label qse = new Label("Quick search");
	private TextField qtf = new TextField();
	private Button qsearch = new Button("Search");
	private Button qclear = new Button("Clear");
	
	private ArrayList<Participant >pList = new ArrayList <Participant>();
	
	public static void main(String[] args) {
		
		launch(args);
	}

	public void start(Stage primaryStage) {
		
	     Image img = new Image("rb.jpg");
	     ImageView view = new ImageView(img);
	     viewAll.setGraphic(view);
	     
	     viewAll.setStyle("-fx-background-color: #ffffff; ");
	     
		
		//database connection
		String jdbcURL = "jdbc:mysql://localhost/attendancesystem?serverTimezone=UTC ";
		String dbUsername = "root";
		String dbPassword = "";
		DBUtil.init(jdbcURL, dbUsername, dbPassword);
		
		doView2();
		
		Alert a = new Alert(AlertType.NONE);
		a.setTitle("App Information");
		a.setHeaderText("ATTENDANCE SYSTEM Version 3.1.3");

			// adding item to menu
			Menu menu = new Menu("More");
			
	    	MenuItem logout = new MenuItem("Logout");
	    	//MenuItem info = new MenuItem("App Information");
	    	MenuItem viewer = new MenuItem("Quick View");

			menu.getItems().addAll(logout,viewer);

			menuBar.getMenus().addAll(menu);
			
			menuBar.setDisable(true);
			
		lTitle.setText("Login \n\n\n\n\n\n\n\n\n");
		lTitle.setFont(Font.font("Arial", 22));
		
		HBox hbPane0 = new HBox(100);
		hbPane0.setSpacing(10);
		hbPane0.getChildren().add(lTitle);
		hbPane0.setAlignment(Pos.CENTER);
		
		///////////////////////////////////////////////////////////////////////////////////////
		// Login
		///////////////////////////////////////////////////////////////////////////////////////
		HBox hbPanee = new HBox(100);
		hbPanee.setSpacing(10);
		hbPanee.getChildren().addAll(pwd);
		hbPanee.getChildren().addAll(pf,loginB);
		hbPanee.setAlignment(Pos.CENTER);
		

		hbPanee2.setSpacing(10);
		hbPanee2.getChildren().addAll(errorM);
		hbPanee2.setAlignment(Pos.CENTER);
		
	
		HBox hbPane = new HBox(100);
		hbPane.setSpacing(10);
		hbPane.getChildren().addAll(viewAll,btAdd, markAtd);
		hbPane.setAlignment(Pos.CENTER);
	//	hbPane.setStyle("-fx-background-color:#ffffff;");
		

		
		///////////////////////////////////////////////////////////////////////////////////////
		// VIEW ALL ATTENDEE
		///////////////////////////////////////////////////////////////////////////////////////
		
		//quick search
		HBox hbPane22 = new HBox(300);
		hbPane22.setSpacing(10);
		hbPane22.getChildren().addAll(qse,qtf,qsearch,qclear);
		hbPane22.setAlignment(Pos.CENTER);
		
		qse.setVisible(false);
		qtf.setVisible(false);
		qsearch.setVisible(false);
		qclear.setVisible(false);
		
		HBox hbPane2 = new HBox(300);
		hbPane2.setSpacing(10);
		hbPane2.getChildren().addAll(taResults);
		hbPane2.setAlignment(Pos.CENTER);
		
		HBox hbPane3 = new HBox(300);
		total.setFont(Font.font("Arial", 20));
		hbPane3.setSpacing(10);
		hbPane3.getChildren().addAll(total);
		hbPane3.setAlignment(Pos.CENTER);
		//hbPane3.setVisible(false);
		
		
		taResults.setPrefHeight(350);
		taResults.setPrefWidth(600);
		
	    VBox vbPane = new VBox(10);
	    
        vbPane.getChildren().addAll(menuBar,hbPane0,hbPanee,hbPanee2,hbPane,hbPane22,hbPane2,hbPane3);
        vbPane.setStyle("-fx-background-color:#CBE6E6;");
        Scene scene = new Scene(vbPane, 600, 500);
        
        //menu.setDisable(true);
		//Scene mainScene = new Scene(vbPane);
		primaryStage.setScene(scene);
		
		primaryStage.setTitle("Attendance System");
		primaryStage.setWidth(600);
		primaryStage.setHeight(595);
	//	primaryStage.setResizable(false);
		
		primaryStage.show();	
		taResults.setEditable(false);//unable to edit

		//prevent adding,delete attendee
	//	btAdd.setDisable(true);
		btDelete.setDisable(true);
	//	markAtd.setDisable(true);
		
		taResults.setVisible(false);
		viewAll.setVisible(false);
		btAdd.setVisible(false);
		markAtd.setVisible(false);
		btDelete.setVisible(false);
		total.setVisible(false);
		
		menuBar.setDisable(true);
		
		EventHandler<ActionEvent> handleView = (ActionEvent e) ->doView2();
		viewAll.setOnAction(handleView);//view all
		
		EventHandler<ActionEvent> lg = (ActionEvent e) ->validateLogin();
		loginB.setOnAction(lg);//login
		
		EventHandler<ActionEvent> lgo = (ActionEvent e) ->doLogOut();
		logout.setOnAction(lgo);//logout
		
		EventHandler<ActionEvent> hm = (ActionEvent e) -> (new Manage_Attendees2()).start(new Stage());
		markAtd.setOnAction(hm);//mark attendance
		
		EventHandler<ActionEvent> add = (ActionEvent e) -> (new Add_Attendees()).start(new Stage());
		btAdd.setOnAction(add);//add
		
		EventHandler<ActionEvent> cr = (ActionEvent e) -> doClear();
		qclear.setOnAction(cr);//clear

		EventHandler<ActionEvent> sr = (ActionEvent e) -> doQuickSearch(pList);
		qsearch.setOnAction(sr);//quick search
		
		EventHandler<ActionEvent> v = (ActionEvent e) -> (new QuickView()).start(new Stage());
		viewer.setOnAction(v);//mark attendance
		
		
        EventHandler<ActionEvent> event2 = new
                EventHandler<ActionEvent>() {
  public void handle(ActionEvent e)
  {
      // set alert type
      a.setAlertType(AlertType.INFORMATION);

      // set content text
      a.setContentText("Last updated on: 15/05/2021\n \nUpdate Details:"
      		+ "\n1. Enable editing of mobile number \n2. UI changes \n3. Enhanced refresh button");

      // show the dialog
      a.show();
  }
};
	//info.setOnAction(event2);

	}
	
	//masking of nric
	public static String mask(String nric) {
		String masked = nric.substring(0,1)+ "****"+nric.substring(5,9);
			return masked;
		}
	
	private void doView2() { //nric not masked
		total.setVisible(true);
		loadParticipants();
		total.setVisible(true);
		String output = String.format("%-10s %-15s %-20s %-10s\n\n", "ID", "NAME", "MOBILE NUMBER", "STATUS");
		
		for(Participant p: pList) {
			output += String.format("%-10s %-15s %-20d %-10s\n",p.getId(), p.getName(), p.getMobileNum(),p.getAttendanceStatus());
		}
		//output+= "\nTotal Number of attendees: "+ pList.size();
				
		taResults.setFont(Font.font("Consolas", 16));
		taResults.setText(output);
		total.setText("\nTotal Number of attendees: "+ pList.size()+"\n");
		}
	
	public void doQuickSearch(ArrayList<Participant >pList) {
		doView2();
		
		String output = String.format("%-10s %-15s %-20s %-10s\n\n", "ID", "NAME", "MOBILE NUMBER", "STATUS");
		
		String toSearch = qtf.getText();
		
		if(qtf.getText().equals("")) { //if empty
		//output = "";
		doView2();
		}
		
		//partial search by id
		if(qtf.getText().equals("")==false) { //is not empty
			for (Participant p : pList) {
				
				if (p.getId().contains(toSearch)) {
					output += String.format("%-10s %-15s %-20s %-10s\n\n",p.getId(), p.getName(), p.getMobileNum(), p.getAttendanceStatus());
				}

			}

		}
		
		//partial search by name
		if(qtf.getText().equals("")==false) { //is not empty
			for (Participant p : pList) {
				
				if (p.getName().contains(toSearch)) {
					output += String.format("%-10s %-15s %-20s %-10s\n\n",p.getId(), p.getName(), p.getMobileNum(), p.getAttendanceStatus());
				}

			}

		}
		
			taResults.setText(output);
			total.setVisible(false);
		}
	private void doView3() { //nric masked

		total.setVisible(true);
		loadParticipants();
		total.setVisible(true);
		String output = String.format("%-10s %-15s %-20s %-10s\n\n", "ID", "NAME", "MOBILE NUMBER", "STATUS");
		
		for(Participant p: pList) {
			output += String.format("%-10s %-15s %-20d %-10s\n",mask(p.getId()), p.getName(), p.getMobileNum(),p.getAttendanceStatus());
		}
		//output+= "\nTotal Number of attendees: "+ pList.size();
				
		taResults.setFont(Font.font("Consolas", 16));
		taResults.setText(output);
		total.setText("\nTotal Number of attendees: "+ pList.size()+"\n");
		}
		
	private void validateLogin() {
		
		String p = pf.getText();
		if (p.equals("user246")) {
			hbPanee2.setVisible(false);
			taResults.setVisible(true);
			viewAll.setVisible(true);
			btAdd.setVisible(true);
			markAtd.setVisible(true);
			btDelete.setVisible(true);
			menuBar.setDisable(false);
			
			lTitle.setText("ATTENDANCE SYSTEM v3.1.3");
			pwd.setVisible(false);
			pf.setVisible(false);
			loginB.setVisible(false);
			
			qse.setVisible(true);
			qtf.setVisible(true);
			qsearch.setVisible(true);
			qclear.setVisible(true);
			
		}
		else
			errorM.setTextFill(Color.RED);
			errorM.setText("Invalid password");

	}
	private void doLogOut() { //perform logout
		taResults.setVisible(false);
		viewAll.setVisible(false);
		btAdd.setVisible(false);
		markAtd.setVisible(false);
		btDelete.setVisible(false);
		menuBar.setDisable(true);
		
		
		lTitle.setText("Login \n\n\n\n\n\n\n\n\n");
		pwd.setVisible(true);
		pf.setVisible(true);
		pf.setText("");
		loginB.setVisible(true);
		menuBar.setDisable(false);
		
		qse.setVisible(false);
		qtf.setVisible(false);
		qsearch.setVisible(false);
		qclear.setVisible(false);
	}
	private void loadParticipants() {
		pList.clear();
		String sql = "SELECT * FROM participant";
		ResultSet rs = DBUtil.getTable(sql);
		try {
			while(rs.next()) {
				String id =rs.getString("id");
				String name = rs.getString("name");
				int mobileNum = rs.getInt("mobile_number");
				String AttendanceStatus = rs.getString("AttendanceStatus");
				
				Participant p = new Participant(id, name, mobileNum,AttendanceStatus);
				pList.add(p);	
			}
		}catch (SQLException e) {
			taResults.setText("SQL Error: " + e.getMessage());
		}
	}

	
	public void doClear() {
		total.setVisible(true);
		qtf.setText("");
		taResults.setText("");
		doView2();

	}
		
	}
