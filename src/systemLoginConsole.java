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
public class systemLoginConsole {
	
	private static ArrayList<Credentials>cList = new ArrayList <Credentials>();

	public static void main(String[] args) {
		
		
		startDatabase();
		
		String inputUsername = Helper.readString("Enter username> ");
		String inputPassword= Helper.readString("Enter password> ");
		
		for(Credentials cd: cList) {
			
			if(inputUsername.equals(cd.getUsername()) && inputPassword.equals(cd.getPassword())){
				homepage();
				System.out.println("Welcome "+cd.getUsername()+"! ");
			}

			if(inputUsername.equals(cd.getUsername()) && !inputPassword.equals(cd.getPassword())){

				System.out.println("Incorrect password!");
			}
			
		}
		
		
		
	}
	private static void startDatabase() {
		String jdbcURL = "jdbc:mysql://localhost/userdatabase?serverTimezone=UTC ";
		String dbUsername = "root";
		String dbPassword = "";
		DBUtil.init(jdbcURL, dbUsername, dbPassword);
		
		cList.clear();
		String sql = "SELECT * FROM credentials";
		ResultSet rs = DBUtil.getTable(sql);
		try {
			while(rs.next()) {
				String username =rs.getString("username");
				String password = rs.getString("password");
				Credentials cd = new Credentials(username,password);
				cList.add(cd);	
			}
		}catch (SQLException e) {
			Helper.line(30, "=");
		}
	}
	
	public static void homepage() {
		Helper.line(30, "=");
		System.out.println("HOMEPAGE");
		Helper.line(30, "=");
	}

}
