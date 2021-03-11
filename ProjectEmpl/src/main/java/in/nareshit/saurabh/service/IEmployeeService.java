package in.nareshit.saurabh.service;


import org.springframework.data.domain.Pageable;

import java.util.List;

import org.springframework.data.domain.Page;

import in.nareshit.saurabh.model.Employee;

public interface IEmployeeService {
	Integer saveEmployee(Employee e);
	List<Employee> getAllEmployees();
	void deleteEmployee(Integer id);
	Employee getOneEmployee(Integer id);
	void updateEmployee(Employee e);
	boolean isEmployeeEmailExist(String empMail);
	Page<Employee> getAllEmployees(Pageable pageable);
}
