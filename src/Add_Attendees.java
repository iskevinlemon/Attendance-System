
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.util.regex.Pattern;

public class Add_Attendees extends Application {
	private VBox pane = new VBox();
	private HBox row1 = new HBox();
	private HBox row2 = new HBox();
	private HBox row3 = new HBox();
	
	private Label labelName = new Label("Name: ");
	private Label labelNric = new Label("ID: ");
	private Label labelMobile = new Label("Mobile Number: ");
	
	private TextField textfieldName = new TextField();
	private TextField textfieldNRIC = new TextField();
	private TextField textfieldMobile = new TextField();
	
	private Label errorMsg = new Label();

	private Button buttonAdd = new Button();
	private Button buttonDone = new Button("Done");

	private static final int LABEL_PREFWIDTH = 100;
	private static final int TEXTFIELD_COLUMNCOUNT = 15;
	
	private static final String JDBC_URL = "jdbc:mysql://localhost/attendancesystem?serverTimezone=UTC ";
	private static final String DB_USERNAME = "root";
	private static final String DB_PASSWORD = "";
	
	public static void main(String[] args) {
		launch(args);
	}

	public void start(Stage primaryStage) {

		labelName.setPrefWidth(LABEL_PREFWIDTH);
		labelNric.setPrefWidth(LABEL_PREFWIDTH);
		labelMobile.setPrefWidth(LABEL_PREFWIDTH);
		
		textfieldName.setPrefColumnCount(TEXTFIELD_COLUMNCOUNT);
		textfieldNRIC.setPrefColumnCount(TEXTFIELD_COLUMNCOUNT);
		textfieldMobile.setPrefColumnCount(TEXTFIELD_COLUMNCOUNT);


		buttonAdd.setText("Add");
		
		row1.getChildren().addAll(labelNric,textfieldNRIC);
		row1.setAlignment(Pos.CENTER);
		
		row2.getChildren().addAll(labelName,textfieldName);
		row2.setAlignment(Pos.CENTER);
		
		row3.getChildren().addAll(labelMobile,textfieldMobile);
		row3.setAlignment(Pos.CENTER);
		
		pane.getChildren().addAll(row1,row2,row3,buttonAdd,buttonDone);
		pane.setAlignment(Pos.CENTER);
		
		pane.getChildren().addAll(errorMsg);
		
		 pane.setStyle("-fx-background-color:#CBE6E6;");
		
		
		EventHandler<ActionEvent> handleInsert = (ActionEvent e) -> doInsertParticipant();
		buttonAdd.setOnAction(handleInsert);
		
		EventHandler<ActionEvent> c = (ActionEvent e) -> closeStage();
		buttonDone.setOnAction(c);
		
		pane.setAlignment(Pos.TOP_CENTER);
		pane.setPadding(new Insets(20,10,10,10));
		pane.setSpacing(10);
		Scene mainScene = new Scene(pane);
		
		primaryStage.setTitle("Add Attendees");
		primaryStage.setWidth(600);
		primaryStage.setHeight(300);
		primaryStage.setResizable(false);
		primaryStage.setScene(mainScene);
		primaryStage.show();
		

	}
	private void closeStage() {
		Stage stg = (Stage )buttonAdd.getParent().getScene().getWindow();
		stg.close();
	}

	public void doInsertParticipant() {
		DBUtil.init(JDBC_URL, DB_USERNAME, DB_PASSWORD);
		String nric = textfieldNRIC.getText();
		String name = textfieldName.getText();
	//	int mobile = Integer.valueOf(textfieldMobile.getText());
		
		String error = "";
		errorMsg.setTextFill(Color.RED);
		
		String NRIC_CHECKER = "[STGF][0-9]{7}[A-Z]";
		String NAME_CHECKER = "[a-zA-Z]{3,10}";
		String MOBILE_CHECKER = "[89][0-9]{7}";
		
		if (textfieldNRIC.getText().matches(NRIC_CHECKER) == false) { //invalid nric
			textfieldNRIC.setText("");
		//	textfieldName.setText("");
		//	textfieldMobile.setText("");
			error += String.format("Invalid ID!\n");
			errorMsg.setText(error);
			errorMsg.setTextFill(Color.RED);
		}
		if (textfieldName.getText().matches(NAME_CHECKER) == false) { //invalid name
		//	textfieldNRIC.setText("");
			textfieldName.setText("");
		//	textfieldMobile.setText("");
			error += String.format("Invalid Name!\n");
			errorMsg.setText(error);
			errorMsg.setTextFill(Color.RED);
		}
		if (textfieldMobile.getText().matches(MOBILE_CHECKER) == false) { //invalid mobile
		//	textfieldNRIC.setText("");
		//	textfieldName.setText("");
			textfieldMobile.setText("");
			error += String.format("Invalid Mobile!\n");
			errorMsg.setText(error);
			errorMsg.setTextFill(Color.RED);
		}
		
		if(textfieldNRIC.getText().matches(NRIC_CHECKER) && textfieldName.getText().matches(NAME_CHECKER) &&
				textfieldMobile.getText().matches(MOBILE_CHECKER)==true) { // all input is correct
		String insertSQL = String.format("INSERT INTO PARTICIPANT(id, name, mobile_number) VALUES ('%s', '%s', %s)",
				nric, name, textfieldMobile.getText());
		DBUtil.execSQL(insertSQL);
		
		errorMsg.setTextFill(Color.GREEN);
		errorMsg.setText("Attendee added!");
		textfieldNRIC.setText("");
		textfieldName.setText("");
		textfieldMobile.setText("");
		
		error = "";
		}
//
//		if (rowsAffected == 1) {
//
//			labelOutput.setText("Pariticpant added!");
//
//		} else {
//			labelOutput.setText("Insert failed!");
//		}
		DBUtil.close();
		}
	}

