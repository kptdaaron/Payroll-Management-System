package model;
import java.io.FileNotFoundException; import java.io.FileReader; import java.util.ArrayList;
import java.util.List; import java.util.Scanner;

public class EmployeeDA {
	private Scanner scan;
	private List<Employee> empList;
	private String line;
	private String [] lineSpecific;
	
	public EmployeeDA() throws FileNotFoundException {	
		scan = new Scanner(new FileReader("C:\\Users\\kptdaaron\\Documents\\Payslip\\Employee.csv"));
		empList = new ArrayList<Employee>();
		
		while(scan.hasNext()) {
			Employee employee = new Employee();
			line = scan.nextLine();
			lineSpecific = line.split(",");
			
			employee.setEmpNo(lineSpecific[0].trim().toString());
			employee.setlName(lineSpecific[1].trim());
			employee.setfName(lineSpecific[2].trim());
			employee.setDepartment(lineSpecific[3].trim());
			employee.setPosition(lineSpecific[4].trim());
			employee.setHourlyRate(Double.parseDouble(lineSpecific[5].trim().toString()));			
			empList.add(employee);
		}		
	}
	public List<Employee> getEmpList(){		
		return empList;		}	
}
