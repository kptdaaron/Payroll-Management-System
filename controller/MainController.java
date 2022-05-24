package controller;

import java.awt.event.ActionEvent; import java.awt.event.ActionListener;
import java.awt.event.MouseEvent; import java.awt.event.MouseListener;
import java.io.FileNotFoundException; import java.io.FileOutputStream;
import java.util.logging.Level; import java.util.logging.Logger;
import javax.swing.*; import javax.swing.table.DefaultTableModel; import java.awt.*;
import com.lowagie.text.Chunk; import com.lowagie.text.Document;
import com.lowagie.text.DocumentException; import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPTable; import com.lowagie.text.pdf.PdfWriter;
import model.Employee; import net.miginfocom.swing.MigLayout; 
import view.LoginView;
import view.MainView;

public class MainController {
	private MainView mainView;
	public double undertime, totalHours, overtimeHours ,regularHours, tempRate, basicPay, overtimePay, x;
	private int absences;
	private JButton save;
	public MainController(MainView mainView, Employee employee, LoginView loginView) {
		this.mainView = mainView;
		this.mainView.addActionListener(new Action());
		this.mainView.addMouseListener(new Action());
	} 
	class Action implements ActionListener, MouseListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if(e.getSource().equals(mainView.proc())) {
				getTotalHours();
				double ut = 0;
				if (validCheck()) {
				} else {
					return;
				}
				totalHours = getTotalHours();	
				if(totalHours > 96)
				{
					overtimeHours = (totalHours - 96);
				} else if (overtimeHours < 1) {
					overtimeHours = 0;
				}
				// Compute for overtime
				x = absences * 8;
				if (ut > 1) {
					undertime = Math.round(112 - totalHours - x);
				} else undertime = 0;
				regularHours = totalHours - overtimeHours;			
				basicPay = regularHours * tempRate;
				overtimePay = overtimeHours * tempRate;	
				ut = (int) 96 - totalHours;
				mainView.txtRegHour().setText(Double.toString(regularHours));	
				mainView.txtOverHour().setText(Double.toString(overtimeHours));
				mainView.txtTotal().setText(Double.toString(totalHours));
				JOptionPane.showMessageDialog(null, "Processing Successful");
			} else if(e.getSource().equals(mainView.reset())){
				reset();
			} else if(e.getSource().equals(mainView.createPayslip())){
				if(validCheck()) {
					payslipDialog();
				} else {
					JOptionPane.showMessageDialog(null, "All fields must be filled in");
				}
			}					
		}
		@Override
		public void mouseClicked(java.awt.event.MouseEvent evt) {
			setToText(evt);			
		}
		@Override
		public void mouseEntered(MouseEvent e) {}
		@Override
		public void mouseExited(MouseEvent e) {}
		@Override
		public void mousePressed(MouseEvent e) {}
		@Override
		public void mouseReleased(MouseEvent e) {}
	}
	public double getTotalHours(){
		double totalHours = 0;
		if (validCheck()) {
			tempRate = Double.parseDouble(mainView.txtRate().getText().toString());	
			for (int rows = 0; rows < mainView.getTimeTable().getRowCount(); rows ++) {				
				for (int cols = 0; cols < mainView.getTimeTable().getColumnCount(); cols++) {					
					totalHours = totalHours+Double.parseDouble(mainView.getTimeTable().getValueAt(rows, cols).toString());
				}			
			}		
		} else {
			JOptionPane.showMessageDialog(null,"All fields must be filled in."); 
		}
		return totalHours;
	}
	public boolean validCheck() {
		if(mainView.getTimeTable().getCellEditor()!= null){
			mainView.getTimeTable().getCellEditor().stopCellEditing();
		} 
		else if(mainView.txtId().getText().trim().isEmpty() | mainView.txtLname().getText().trim().isEmpty() | 
				mainView.txtFname().getText().trim().isEmpty() | mainView.txtRate().getText().trim().isEmpty()) {
			return false; 
		}
		for(int i=0;i< mainView.getTimeTable().getRowCount();i++) {
			for (int j=0;j<mainView.getTimeTable().getColumnCount();j++) {
				String om=mainView.getTimeTable().getValueAt(i,j).toString();
				if(om.trim().length()==0) {
					return false; 
				}
			}
		}
		return true;
	}
	private void reset() {
		mainView.txtId().setText("");
		mainView.txtLname().setText("");
		mainView.txtFname().setText("");
		mainView.txtRate().setText("");
		mainView.txtRegHour().setText("");
		mainView.txtOverHour().setText("");
		mainView.txtTotal().setText("");
		mainView.timeTable();
		JOptionPane.showMessageDialog(null, "All fields cleared successfully");
	}
	public int countAbsences() {
		int occur1 = 0;
		int occur2 = 0;
		for (int i = 0, rows = mainView.timeTable.getColumnCount(); i < rows; i++) {
			if(mainView.timeTable.getValueAt(0, i).equals("0")) {
				occur1++;
			}
		}
		for (int i = 0, rows = mainView.timeTable.getColumnCount(); i < rows; i++) {
			if(mainView.timeTable.getValueAt(1, i).equals("0")){
				occur2++;
			}
		}
		return absences = occur1 + occur2;
	}
	public double getMonthlyRate() {	
		double monthlyRate = (tempRate * 8 * 261) / 12;
		return monthlyRate;
	}
	public double getAbsentDeduction() {
		double absentDeduction = countAbsences() * tempRate;	
		return absentDeduction;
	}
	public void print() throws FileNotFoundException, DocumentException {
		try {
			Document doc = new Document();

			String file = "C:/Users/kptdaaron/Documents/Payslip/" + mainView.getCurDate();
			PdfWriter.getInstance(doc, new FileOutputStream(file + "_" + mainView.txtId().getText() + "_" + mainView.txtLname().getText() + "_" + mainView.txtFname().getText() + ".pdf"));
			doc.open();
			doc.add(new Paragraph("Magical Mystery Tour Foundations, INC"));
			doc.add(new Paragraph("PAYSLIP : " + mainView.getCurDate() + " to " + mainView.getDateTo()));
			doc.add(new Paragraph("Name : " + mainView.txtLname().getText() + "," + " " + mainView.txtFname().getText()));
			doc.add(Chunk.NEWLINE);
			PdfPTable pdfTable = new PdfPTable(mainView.getPayslipTable().getColumnCount());
			//adding table headers
			for (int i = 0; i < mainView.getPayslipTable().getColumnCount(); i++) {
				pdfTable.addCell(mainView.getPayslipTable().getColumnName(i));
			}
			//extracting data from the JTable and inserting it to PdfPTable
			for (int rows = 0; rows < mainView.getPayslipTable().getRowCount(); rows++) {
				for (int cols = 0; cols < mainView.getPayslipTable().getColumnCount(); cols++) {
					pdfTable.addCell(mainView.getPayslipTable().getModel().getValueAt(rows, cols).toString());
				}
			}
			doc.add(pdfTable);
			doc.close();
			JOptionPane.showMessageDialog(null, "The document was saved successfully");
		} catch (DocumentException ex) {
			Logger.getLogger(MainView.class.getName()).log(Level.SEVERE, null, ex);
		} catch (FileNotFoundException ex) {
			Logger.getLogger(MainView.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	private void payslipDialog() {
		JDialog jd = new JDialog();
		jd.setLayout(new BorderLayout());
		jd.setSize(530,400);
		jd.setVisible(true);
		jd.setModal(true);
		jd.setLocationRelativeTo(null);
		jd.setResizable(false);

		JPanel p = new JPanel();
		p.setLayout(new MigLayout());

		mainView.psTable = new JTable();	
		mainView.psTable.setLayout(new BorderLayout());
		JScrollPane jsp = new JScrollPane();
		jsp.setViewportView(mainView.psTable);

		JLabel earnings = new JLabel("EARNINGS");
		save = new JButton("Save");

		double te = (getMonthlyRate() / 2) + overtimePay,tax = te * 0.01, sssp = te * 0.02, pgbg = te * 0.01, sssl = te * 0.03;
		double ph = te * 0.02;
		double td = tax+sssp+pgbg+sssl+ph+getAbsentDeduction();
		double netPay = te-td;

		Object [] column = {"", ""};
		Object [][] data = {		
				{earnings.getText(), ""},
				{"", ""},
				{"Basic Pay : ", Math.round(getMonthlyRate()/2)},
				{"Overtime : ", Math.round(overtimePay)},
				{"Absences :", Math.round(countAbsences())},
				{"TOTAL EARNINGS", Math.round(te)},
				{"", ""},
				{"DEDUCTIONS", ""},
				{"", ""},
				{"Absences Deduction : ", Math.round(getAbsentDeduction())},
				{"Tax Withheld : ", Math.round(tax)},
				{"Tax Withheld : ", Math.round(tax)},
				{"SSS Premium :  ", Math.round(sssp)},
				{"Philhealth : ", Math.round(ph)},
				{"PAG-IBIG :", Math.round(pgbg)},
				{"SSS LOAN : ", Math.round(sssl)},	
				{"TOTAL DEDUCTIONS", Math.round(td)},
				{"", ""},
				{"NET PAY : " , Math.round(netPay)}
		};
		DefaultTableModel dtm = new DefaultTableModel(data, column);
		mainView.psTable.setModel(dtm);
		p.add(jsp, "span, growx, pushx, gap 10 10 10 10");
		p.add(save,"right, gapright 10");
		jd.add(p, BorderLayout.CENTER);

		save.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				mainView.setVisible(false);
				try {
					print();
				} catch (FileNotFoundException | DocumentException e1) {
					e1.printStackTrace();
				}
				mainView.setVisible(true);			
			}
		});
	}
	public void setToText(java.awt.event.MouseEvent evt) {
		DefaultTableModel model = (DefaultTableModel)mainView.empTable().getModel();
		int selectedRowIndex = mainView.empTable().getSelectedRow();

		mainView.txtId().setText(model.getValueAt(selectedRowIndex, 0).toString());
		mainView.txtLname().setText(model.getValueAt(selectedRowIndex, 1).toString());
		mainView.txtFname().setText(model.getValueAt(selectedRowIndex, 2).toString());
		mainView.txtRate().setText(model.getValueAt(selectedRowIndex, 5).toString());
		JOptionPane.showMessageDialog(null, "Employee Selected");
	}
}