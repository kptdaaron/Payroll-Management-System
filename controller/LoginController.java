package controller;

import java.awt.event.ActionEvent; import java.awt.event.ActionListener; 
import java.awt.event.KeyEvent; import java.awt.event.KeyListener; 
import java.io.FileNotFoundException; import java.io.IOException;
import javax.swing.JOptionPane;

import model.*;
import view.*;

public class LoginController {
	private LoginView loginView;
	private Employee employee;
	public LoginController(LoginView loginView, MainView mainView, Employee employee) {
		this.loginView = loginView;
		this.loginView.addActionListener(new Action());
		this.loginView.addKeyListener(new Action());
	}
	class Action implements ActionListener, KeyListener {
		public void  actionPerformed(ActionEvent e) {
				isUserValid();	
		}
		@Override
		public void keyReleased(KeyEvent e) {
		}
		@Override
		public void keyTyped(KeyEvent e) {	
		}
		@Override
		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				loginView.login().doClick();
			}
		}	
	}
	public boolean isUserValid() {
		String user, pass;
		user = loginView.txt().getText();
		pass = String.valueOf(loginView.pwd().getPassword());

		if(user.equals("admin") && pass.equals("admin")){
			JOptionPane.showMessageDialog(null, "Username and password correct");
			try {
				showMainFrame();
				loginView.dispose();
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {				
				e1.printStackTrace();
			}				
		}
		else{
			JOptionPane.showMessageDialog(null, "Invalid username or password");
		}
		return false;
	}		
	public void showMainFrame() throws IOException
	{
		MainView mainView = new MainView();
		@SuppressWarnings("unused")
		MainController controller = new MainController(mainView, employee, loginView);
		mainView.setVisible(true);
		
	}
}