package controller;

import java.io.IOException;

import model.Employee;
import view.LoginView;
import view.MainView;

public class AppRunner {

	private static MainView mainView;
	private static Employee employee;

	public static void main(String[] args) throws IOException {	
		LoginView loginView = new LoginView();
		@SuppressWarnings("unused")
		LoginController loginController = new LoginController(loginView, mainView, employee);
	}
}
