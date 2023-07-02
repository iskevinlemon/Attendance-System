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
public class QuickView extends Application{
	
private Label findAttendance = new Label("Find Attendance");	
private TextField inputA = new TextField();
private Label nric = new Label("NRIC");
private Label error = new Label("Invalid! try again");
private Button search = new Button("Search");
private Button clear = new Button("Clear");
private TextArea result = new TextArea();


private HBox hb1 = new HBox(20);
private HBox hb2 = new HBox(10);
private HBox hb3 = new HBox(10);
private HBox hb4 = new HBox(10);
private HBox hb5 = new HBox(10);

private VBox vb1 = new VBox(20);


	public static void main(String[] args) {
		launch(args);

	}
	public void start(Stage primaryStage) {
		
		String jdbcURL = "jdbc:mysql://localhost/attendancesystem?serverTimezone=UTC ";
		String dbUsername = "root";
		String dbPassword = "";
		DBUtil.init(jdbcURL, dbUsername, dbPassword);
		
		
		
		hb1.setAlignment(Pos.CENTER);
		hb1.getChildren().add(findAttendance);
		
		hb2.setAlignment(Pos.CENTER);
		hb2.getChildren().addAll(nric,inputA,search);
		
		error.setTextFill(Color.RED);
		hb3.getChildren().add(error);
		error.setVisible(false);
		hb3.setPadding(new Insets(-20,0,0,157));
		//hb3.setVisible(false);
		
		hb4.setAlignment(Pos.CENTER);
		result.setPrefHeight(250);
		result.setPrefWidth(250);
		result.setEditable(false);
		//result.setStyle("-fx-control-inner-background:#FFB2B2");
		hb4.getChildren().addAll(result);
		
		hb5.setAlignment(Pos.CENTER);
		hb5.getChildren().addAll(clear);
		
		vb1.getChildren().addAll(hb1,hb2,hb3,hb4,hb5);
		
		Scene s = new Scene(vb1,470,500);
		primaryStage.setScene(s);
	//	primaryStage.setResizable(false); //prevent window resizing
		primaryStage.setTitle("Quick Viewer");

		primaryStage.setWidth(500);
		primaryStage.setHeight(600);
		primaryStage.show();	
		
		EventHandler<ActionEvent> sr= (ActionEvent e) -> doFind();
		search.setOnAction(sr);
		
		EventHandler<ActionEvent> c= (ActionEvent e) -> doClear();
		clear.setOnAction(c);
	}
//public void doSearch() {
//	if (inputA.getText().equalsIgnoreCase("")) {
//		error.setVisible(true);;
//	}
	private void doClear() {
		inputA.setText("");
		result.setText("");
		result.setStyle("-fx-control-inner-background:#FFFFFF"); 
	}
	private void doFind() {
		String id = inputA.getText();	
		String SQLFind = "SELECT name,mobile_number,AttendanceStatus FROM participant WHERE id='" + id +"'";
		
		int rowsAffected = DBUtil.execSQL(SQLFind);

		ResultSet rs = DBUtil.getTable(SQLFind);

		if (rowsAffected != 1) {
			result.setText("     Invalid ID");
			result.setFont(Font.font("Arial", 30));
			result.setStyle("-fx-control-inner-background:#FFAA55"); 
		}else {
			result.setText("");
			result.setFont(Font.font("Arial", 30));
			result.setStyle("-fx-control-inner-background:#FFAA55"); 
		}
		result.setText("     Invalid ID");
		result.setFont(Font.font("Arial", 30));
		result.setStyle("-fx-control-inner-background:#FFAA55"); 
		try {
			
			while (rs.next()) {
				String name = rs.getString("name");
				String AttendanceStatus = rs.getString("AttendanceStatus");
				int mobileNumber = rs.getInt("mobile_number");
				error.setText("");

				String output = "";
				output+="Name: "+ name;
				output+="\nMobile Number: "+ mobileNumber;
				output+="\nAttendance Status: "+ AttendanceStatus;
				result.setText(output);
				result.setFont(Font.font("Arial", 15));
				
				if(AttendanceStatus.equalsIgnoreCase("LATE")) {
					result.setStyle("-fx-control-inner-background:#FFB2B2");
				}
				if(AttendanceStatus.equalsIgnoreCase("PRESENT")) { 
					result.setStyle("-fx-control-inner-background:#C8F6BB");
				}
				if(AttendanceStatus.equalsIgnoreCase("LATE-VALID")) { 
					result.setStyle("-fx-control-inner-background:#84EAFF"); 
				}
				if(AttendanceStatus.equalsIgnoreCase("NOT MARKED")) { 
					result.setStyle("-fx-control-inner-background:#B4B4B4"); 
				}
			}

		}catch(SQLException e) {
		System.out.println("SQL Error: "+ e.getMessage());
		}
		}
	/**
	 * @param string
	 */
	private void setStyle(String string) {
		// TODO Auto-generated method stub
		
	}
}

