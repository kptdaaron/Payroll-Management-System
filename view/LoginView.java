package view;

import java.awt.event.ActionListener; import java.awt.event.KeyListener; import javax.swing.*;
import net.miginfocom.swing.MigLayout;

public class LoginView extends JFrame{

	private JPanel p;
	private JLabel u, pw;
	private JTextField txt;
	private JPasswordField pwd;
	private JButton login;

	public LoginView() {
		initializeComponents();		
	}
	private void initializeComponents() {		
		setTitle("Login Portal");
		setSize(300,130);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);			
		p = new JPanel();	
		setContentPane(p);		
		u = new JLabel("Username : ");
		txt = new JTextField(20);
		pw = new JLabel("Password : ");
		pwd = new JPasswordField(20);
		login = new JButton("Login");		
		p.setLayout(new MigLayout("", "10[][]10", "10[] [] [grow,fill]10"));
		p.add(u);
		p.add(txt, "pushx, growx, wrap");
		p.add(pw);
		p.add(pwd, "pushx, growx, wrap");
		p.add(login, "span, growx,");
		
		setVisible(true);
	}
	public JTextField txt() {
		return this.txt;
	}	
	public JPasswordField pwd() {
		return this.pwd;
	}	
	public JButton login() {
		return this.login;
	}
	public void addActionListener(ActionListener l) {	
		this.login.addActionListener(l);
	}	
	public void addKeyListener(KeyListener l) {
		this.txt.addKeyListener(l);
		this.pwd.addKeyListener(l);
	}
}