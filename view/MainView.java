package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import net.miginfocom.swing.MigLayout;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainView extends JFrame{

	private static final long serialVersionUID = 1L;

	private JScrollPane empTableWrapper, timeTableWrapper;
	private JTable empTable, timeTable;
	public JTextField txtRegHour, txtOverHour, txtRegAmount, txtOverAmount, txtNet, txtId, txtLname, txtFname, txtRate, txtTotal;
	private JLabel hours, regular, net, amount, total, over;
	private JButton proc, reset, save;
	
	private double undertime;
	private double reg;
	private double totalHours;
	private double overtimeHours;
	private double regularHours;
	private double tempSum;
	private int tempRate;
	private double basicPay;
	private double overtimePay;
	private int absences;
	double x;
	
	protected String curDate;
	protected String dateTo;

	public MainView() throws FileNotFoundException {
		
		setupUI();			
		readFile();
		timeTable();
		countAbsences();
		calendar();
		events();	
		
	}
	
	public void setupUI() throws FileNotFoundException {	
		/**
		 * Setup the Main Frame
		 */		
		setTitle("Payroll Processing System");
		setSize(800,400);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		setLocationRelativeTo(null);
		setResizable(false);

		/**
		 * Setup the Main Panel
		 */
		JPanel mainPanel = new JPanel();
		setContentPane(mainPanel);	
		mainPanel.setLayout(new MigLayout("", "", "[] [] [] []"));

		JPanel empInfoPanel = new JPanel();
		empInfoPanel.setLayout(new MigLayout());
		empInfoPanel.setBorder(BorderFactory.createTitledBorder("Employee Info"));

		JLabel Id = new JLabel("Id Number : ");
		txtId = new JTextField(5);
		txtId.setEditable(false);

		JLabel lName = new JLabel("Last Name : ");
		txtLname = new JTextField(10);
		txtLname.setEditable(false);

		JLabel fName = new JLabel("First Name : ");
		txtFname = new JTextField(10);
		txtFname.setEditable(false);

		JLabel rate = new JLabel("Hourly Rate : ");
		txtRate = new JTextField(10);
		txtRate.setEditable(false);

		empInfoPanel.add(Id);
		empInfoPanel.add(txtId, "gapright 20, growx, pushx");
		empInfoPanel.add(lName);
		empInfoPanel.add(txtLname, "gapright 20, growx, pushx");
		empInfoPanel.add(fName);
		empInfoPanel.add(txtFname, "gapright 20, growx, pushx");
		empInfoPanel.add(rate);
		empInfoPanel.add(txtRate, "gapright 20, growx, pushx");

		/**
		 * Setup the Employee Panel
		 */
		JPanel empPanel = new JPanel();
		empPanel.setLayout(new GridLayout(1, 0, 3, 3));
		empPanel.setBorder(BorderFactory.createTitledBorder("Employee List"));

		/**
		 * Setup the Employee Table
		 */
		empTable = new JTable();
		empTableWrapper = new JScrollPane();
		empTableWrapper.setPreferredSize(new Dimension(0, 100));	
		empTableWrapper.setViewportView(empTable);

		empPanel.add(empTableWrapper);

		/**
		 * Setup the Time Sheet Panel
		 */
		JPanel timePanel = new JPanel();
		timePanel.setLayout(new MigLayout(""));
		timePanel.setBorder(BorderFactory.createTitledBorder("Time Sheet"));

		timeTable = new JTable();
		timeTableWrapper = new JScrollPane();
		timeTableWrapper.setPreferredSize(new Dimension(800,73));
		timeTableWrapper.setViewportView(timeTable);

		JLabel fWeek = new JLabel("Week 1");
		JLabel sWeek = new JLabel("Week 2");

		timePanel.add(fWeek,"cell 0 2,gaptop 20, gapleft 10,wrap");
		timePanel.add(sWeek,"cell 0 3, gapleft 10");
		timePanel.add(timeTableWrapper, "growx, pushx, gapleft 10,east");

		/**
		 * Setup the Processing Panel
		 */	
		JPanel 	processPanel = new JPanel();
		processPanel.setLayout(new MigLayout(""));
		processPanel.setBorder(BorderFactory.createTitledBorder("Payroll Processing"));	

		regular = new JLabel("Regular Hours Worked : ");
		processPanel.add(regular);
		
		txtRegHour = new JTextField(5);
		txtRegHour.setEditable(false);
		processPanel.add(txtRegHour, "gapright 20");

		over = new JLabel("Overtime Hours : ");
		processPanel.add(over);
		
		txtOverHour = new JTextField(5);
		txtOverHour.setEditable(false);
		processPanel.add(txtOverHour, "gapright 20");

		total = new JLabel("Total Hours : ");
		processPanel.add(total);

		txtTotal = new JTextField(5);
		txtTotal.setEditable(false);
		processPanel.add(txtTotal, "gapright 20");

		proc = new JButton("Process");
		processPanel.add(proc);

		reset = new JButton("Reset");
		processPanel.add(reset);

		save = new JButton("Save");
		processPanel.add(save);

		/**
		 * Adding Components to the Main Panel
		 */
		mainPanel.add(empPanel, "pushx, growx, wrap");
		mainPanel.add(empInfoPanel, "pushx, growx, wrap");
		mainPanel.add(timePanel,"pushx, growx, wrap");
		mainPanel.add(processPanel,"pushx, growx, wrap");

	}

	public void calendar() {
		
		Calendar calendar = new GregorianCalendar();				
		DateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		
		curDate = sdf.format(calendar.getTime());		
		calendar.add(Calendar.DAY_OF_MONTH, 15);
		dateTo = sdf.format(calendar.getTime());	
		
	}

	public void setToText(java.awt.event.MouseEvent evt) {

		DefaultTableModel model = (DefaultTableModel)empTable.getModel();
		int selectedRowIndex = empTable.getSelectedRow();

		txtId.setText(model.getValueAt(selectedRowIndex, 0).toString());
		txtLname.setText(model.getValueAt(selectedRowIndex, 1).toString());
		txtFname.setText(model.getValueAt(selectedRowIndex, 2).toString());
		txtRate.setText(model.getValueAt(selectedRowIndex, 5).toString());

	}

	private void readFile() {

		String filePath = "src//Employee.txt";
		File file = new File(filePath);

		try {
			@SuppressWarnings("resource")
			BufferedReader br = new BufferedReader(new FileReader(file));

			String firstLine = br.readLine().trim();
			String[] columnsName = firstLine.split(",");

			@SuppressWarnings("serial")
			DefaultTableModel emt = new DefaultTableModel() 			
			{
				public boolean isCellEditable(int row, int column)
				{
					return false;
				}
			};

			empTable.getTableHeader().setReorderingAllowed(false);	
			empTable.setModel(emt);

			emt.setColumnIdentifiers(columnsName);

			Object[] tableLines = br.lines().toArray();

			for(int i = 0; i < tableLines.length; i++)
			{
				String line = tableLines[i].toString().trim();
				String[] dataRow = line.split(",");
				emt.addRow(dataRow);
			}
		} catch (Exception ex) {
			Logger.getLogger(MainView.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public void timeTable() {

		Object[] columns ={"Monday","Tuesday","Wednesday","Thursday", "Friday", "Saturday", "Sunday"};

		Object[][] rows ={{"","","","","","",""},
						  {"","","","","","",""}
		};  

		DefaultTableModel tsmt = new DefaultTableModel(rows, columns);

		timeTable.getTableHeader().setReorderingAllowed(false);	
		timeTable.setRowHeight(25);
		timeTable.setModel(tsmt);

	}

	public double getTotalHours(){

		double week1 = 0;
		double week2 = 0;
		@SuppressWarnings("unused")
		double sum = 0;


		for (int i = 0, rows = timeTable.getColumnCount(); i < rows; i++)
		{
			week1 = week1+Double.parseDouble(timeTable.getValueAt(0, i).toString());
		}

		for (int i = 0, rows = timeTable.getColumnCount(); i < rows; i++)
		{
			week2 = week2+Double.parseDouble(timeTable.getValueAt(1, i).toString());
		}

		return sum = week1 + week2;    

	}

	private void reset() {

		txtId.setText("");
		txtLname.setText("");
		txtFname.setText("");
		txtRate.setText("");
		txtRegHour.setText("");
		txtOverHour.setText("");
		txtTotal.setText("");
		timeTable();

	}

	public void events() {

		empTable.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				setToText(evt);
			}
		});

		proc.addActionListener(new ActionListener() {
			private double ut;

			public void actionPerformed(ActionEvent e) {

				tempRate = Integer.parseInt(txtRate.getText());								
				totalHours = getTotalHours();			
				regularHours = totalHours - overtimeHours;			
				basicPay = regularHours * tempRate;
				overtimePay = overtimeHours * tempRate;	
				ut = (int) 112 - totalHours;
				// Compute for overtime
				if(totalHours > 112)
				{
					overtimeHours = totalHours - 112;
				} else if (overtimeHours < 1) {
					overtimeHours = 0;
				}
				
				x = absences * 8;
	
				if (ut > 1) {
					undertime = 112 - totalHours - x;
				} else undertime = 0;
				
				txtRegHour.setText(Double.toString(regularHours));	
				txtOverHour.setText(Double.toString(overtimeHours));
				txtTotal.setText(Double.toString(totalHours));

			}
		});

		reset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				reset();

			}
		});

		save.addActionListener(new ActionListener() {	

			public void actionPerformed(ActionEvent e) {
				
				JDialog jd = new JDialog();
				jd.setSize(400,355);
				jd.setVisible(true);
				jd.setModal(true);
				jd.setLocationRelativeTo(null);
				jd.setResizable(false);
				
				JPanel p = new JPanel();
				p.setLayout(new MigLayout());
				
				JTable psTable = new JTable();	
				JScrollPane jsp = new JScrollPane();
				jsp.setViewportView(psTable);
				
				JTable dedTable = new JTable();	
				JScrollPane jsp2 = new JScrollPane();
				jsp2.setViewportView(dedTable);
				
				JLabel companyName = new JLabel("Magical Mystery Tour Foundation, INC");
				JLabel payslip = new JLabel("Payslip : " + curDate + " to " + dateTo);
				JLabel earnings = new JLabel("EARNINGS");
				JLabel deductions = new JLabel("DEDUCTIONS");
				 
				double te = (int) basicPay + overtimePay;
				double tax = te * 0.05;
				double sssp = te * 0.03;
				double pgbg = te * 0.01;
				double sssl = te * 0.08;
				double ph = te * 0.02;
				double td = tax+sssp+pgbg+sssl+ph;
				double netPay = te-td;
				
				JLabel netpay = new JLabel("NETPAY : " + netPay);
	
				Object [] column = {"" , ""};
				Object [][] data = {{"Basic Pay  ", basicPay},
								    {"Overtime   ", overtimePay},
									{"Absences", Integer.toString(countAbsences())},
									{"Undertime", Double.toString(undertime)},
									{"TOTAL EARNINGS", te}
									 };
				
				DefaultTableModel dtm = new DefaultTableModel(data, column);
				psTable.setModel(dtm);

				Object [] column2 = {"" , ""};
				Object [][] data2 = {{"Tax Withheld  ", tax},
								    {"SSS Premium   ", sssp},
								    {"Philhealth", ph},
									{"PAG-IBIG", pgbg},
									{"SSS LOAN", sssl},				
									{"TOTAL DEDUCTIONS", td}
									 };
				
				DefaultTableModel dtm2 = new DefaultTableModel(data2, column2);
				dedTable.setModel(dtm2);		
				
				p.add(companyName,"wrap");
				p.add(payslip, "wrap");
				p.add(earnings,"center, wrap");
				p.add(jsp, "span");
				p.add(deductions,"center, wrap");
				p.add(jsp2, "span");
				p.add(netpay, "center, wrap");
				jd.add(p);
			}
		});

	}

	private int countAbsences() {

		int occur1 = 0;
		int occur2 = 0;

		for (int i = 0, rows = timeTable.getColumnCount(); i < rows; i++)
		{
			if(timeTable.getValueAt(0, i).equals("0")){
				occur1++;
			}
		}

		for (int i = 0, rows = timeTable.getColumnCount(); i < rows; i++)
		{
			if(timeTable.getValueAt(1, i).equals("0")){
				occur2++;
			}
		}

		return absences = occur1 + occur2;
	};
}