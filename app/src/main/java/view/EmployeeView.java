package view;

import model.Employee;
import java.util.*;

public interface EmployeeView {

    void employeeReady(List<Employee> employeeList);

    void showError();
}
