
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Manage_Attendees2 extends Application {

		private VBox vbPane = new VBox();
		
		private Label lbID= new Label("Enter ID");
		private TextField tfId = new TextField();
		private Button find = new Button("Find");

		private Label lbname= new Label("Name");
		private TextField nametf = new TextField("");
		
		private Label lbstatus= new Label("Status");
		private TextField statustf = new TextField("");
		
		private Label mNum= new Label("Mobile Number");
		private TextField mNumtf = new TextField("");
		
		private Button present = new Button("Present");
		private Button late = new Button("Late");
		private Button lateV = new Button("Late(V)");
		
		private Button saveA = new Button("Save");
		private Button done = new Button("Done!");
		private Button delete = new Button("Delete Attendee");
		
		private Label error= new Label("");
		
		private Label msg= new Label("");
		

		public static void main(String[] args) {
			launch(args);
		}
		
		public void start(Stage primaryStage) {
			
			//delete.setDisable(true);
			
			String jdbcURL = "jdbc:mysql://localhost/attendancesystem?serverTimezone=UTC ";
			String dbUsername = "root";
			String dbPassword = "";
			DBUtil.init(jdbcURL, dbUsername, dbPassword);

			error.setTextFill(Color.RED);
			
			HBox h1 = new HBox(10);
			h1.getChildren().addAll(lbID,tfId,find);
			h1.setPadding(new Insets(45,20,25,60));
			
			HBox h2 = new HBox(10);
			h2.getChildren().addAll(error);
			h2.setPadding(new Insets(-20,20,20,140));
			
			HBox h3 = new HBox(10);

			h3.getChildren().addAll(lbname,nametf,mNum,mNumtf);
			nametf.setEditable(false);
			statustf.setEditable(false);
			h3.setAlignment(Pos.CENTER);
			
			HBox h4 = new HBox(10);
			h4.getChildren().addAll(lbstatus,statustf);
			h4.setPadding(new Insets(10,20,40,68));
			
			HBox h42 = new HBox(10);
			h42.getChildren().addAll(present,late,lateV);
			h42.setPadding(new Insets(-65,20,40,273));
			
			HBox h5 = new HBox(10);
			h5.getChildren().addAll(saveA,done);
			h5.setAlignment(Pos.CENTER);
			
			HBox h6 = new HBox(10);
			h6.getChildren().addAll(delete);
			h6.setPadding(new Insets(10,20,20,238));
			delete.setStyle("-fx-background-color: #FF0000"); 
			delete.setStyle("-fx-font: 13 arial; -fx-base: #ee2211;");
		//	h6.setAlignment(Pos.CENTER);
			
			HBox h7 = new HBox(10);
			h7.getChildren().addAll(msg);
			h7.setAlignment(Pos.CENTER);

			
			vbPane.getChildren().addAll(h1,h2,h3,h4,h42,h5,h6,h7);
			vbPane.setStyle("-fx-background-color:#CBE6E6;");
			
			Scene myScene = new Scene(vbPane);
			primaryStage.setScene(myScene);
			
			primaryStage.setTitle("Manage Attendees");
			primaryStage.setResizable(false);
			primaryStage.setWidth(600);
			primaryStage.setHeight(400);
			primaryStage.show();
			
			EventHandler<ActionEvent> handleFind = (ActionEvent e) -> doFind();
			find.setOnAction(handleFind);
			
			//present, late, valid late
			EventHandler<ActionEvent> mp = (ActionEvent e) -> markPresent();
			present.setOnAction(mp);
			
			EventHandler<ActionEvent> ml = (ActionEvent e) -> markLate();
			late.setOnAction(ml);
			
			EventHandler<ActionEvent> mlv = (ActionEvent e) -> markLateV();
			lateV.setOnAction(mlv);
			
			EventHandler<ActionEvent> ua = (ActionEvent e) -> updateAttendance();
			saveA.setOnAction(ua);
			
			EventHandler<ActionEvent> hd = (ActionEvent e) -> closeStage();
			done.setOnAction(hd);
			
			EventHandler<ActionEvent> dl = (ActionEvent e) -> doDelete();
			delete.setOnAction(dl);

		}
		
		private void closeStage() {
			Stage stg = (Stage )present.getParent().getScene().getWindow();
			stg.close();
		}


		private void doFind() {
			String id = tfId.getText();	
			String SQLFind = "SELECT name,mobile_number,AttendanceStatus FROM participant WHERE id='" + id +"'";
			
			int rowsAffected = DBUtil.execSQL(SQLFind);

			ResultSet rs = DBUtil.getTable(SQLFind);

			if (rowsAffected != 1) {
				error.setText("Invalid ID");
			}else {
				error.setText("");
			}
			error.setText("Invalid ID");
			try {
				
				while (rs.next()) {
					String name = rs.getString("name");
					String AttendanceStatus = rs.getString("AttendanceStatus");
					int mobileNumber = rs.getInt("mobile_number");
					error.setText("");
					nametf.setText(name);
					statustf.setText(AttendanceStatus);
					mNumtf.setText(String.valueOf(mobileNumber));
			
				}
	
			}catch(SQLException e) {
			System.out.println("SQL Error: "+ e.getMessage());
			}
			}
		private void markPresent() {
			statustf.setText("PRESENT");
		}
		private void markLate() {
			statustf.setText("LATE");
		}
		private void markLateV() {
			statustf.setText("LATE-VALID");
		}
		
		private void updateAttendance() {

			String id = tfId.getText();	
			
			
			//update status
			String SQLUpdate= "UPDATE `participant` SET `AttendanceStatus` = '" + statustf.getText() +"' WHERE `participant`.`id` = '"+ id+"'";
			int rowsAdded = DBUtil.execSQL(SQLUpdate);
			
			//update mobile number
			int numberUpdate = Integer.valueOf(mNumtf.getText());//conversion
			
			String SQLUpdate2= "UPDATE `participant` SET `mobile_number` = " + numberUpdate +" WHERE `participant`.`id` = '"+ id+"'";
			int rowsAdded2 = DBUtil.execSQL(SQLUpdate2);
			

			
			if (rowsAdded == 1&& rowsAdded2 == 1) {

			msg.setText("\nSaved sucessfull!");
			msg.setTextFill(Color.GREEN);
			
			nametf.setText("");
			tfId.setText("");
			statustf.setText("");
			mNumtf.setText("");
			
			}else {
				msg.setTextFill(Color.RED);
				statustf.setText("");
				msg.setText("\nError encountered...try again");
			}
			
		}
		private void doDelete() {
			String id = tfId.getText();	

			String SQLDelete = "DELETE FROM participant WHERE id='" + id +"'";
			int rowsAffected = DBUtil.execSQL(SQLDelete);

			if (rowsAffected != 1) {
				msg.setTextFill(Color.RED);
				msg.setText("Delete Failed");
				tfId.setText("");
			}else {
				msg.setTextFill(Color.GREEN);
				msg.setText("Attendee deleted!");
				nametf.setText("");
				tfId.setText("");
				statustf.setText("");
				
			}
			}
		}
	

		
