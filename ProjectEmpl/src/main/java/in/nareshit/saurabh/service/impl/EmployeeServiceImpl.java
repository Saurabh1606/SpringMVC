package in.nareshit.saurabh.service.impl;


import org.springframework.data.domain.Pageable;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import in.nareshit.saurabh.repo.EmployeeRepository;
import in.nareshit.saurabh.exception.EmployeeNotFoundException;
import in.nareshit.saurabh.model.Employee;
import in.nareshit.saurabh.service.IEmployeeService;

@Service
public class EmployeeServiceImpl implements IEmployeeService {

	

	@Autowired
	private EmployeeRepository repo;
	
	
	public Integer saveEmployee(Employee e) {
		var sal = e.getEmpSal();
		var hra = sal * 12/100;
		var ta = sal * 3/100;

		//set data to model cls obj
		e.setEmpHra(hra);
		e.setEmpTa(ta);

		//save data in db
		
		e = repo.save(e);

		//PK
		Integer empId = e.getEmpId();

		return empId;
		
	}

	
	public List<Employee> getAllEmployees() {
		List<Employee> list = repo.findAll();
		return list;
	}

	
	public void deleteEmployee(Integer id) {
		repo.delete(getOneEmployee(id));
		
	}

	
	public Employee getOneEmployee(Integer id) {
		return repo.findById(id)
				.orElseThrow(
						()-> new EmployeeNotFoundException(
								"Employee '"+id+"' Not exist")
						);
	}

	
	public void updateEmployee(Employee e) {
		if(repo.existsById(e.getEmpId())) {
			var sal = e.getEmpSal();
			var hra = sal * 12/100;
			var ta = sal * 3/100;

			e.setEmpHra(hra);
			e.setEmpTa(ta);
			
			repo.save(e);
		}
		
	}

	
	public boolean isEmployeeEmailExist(String empMail) {
		return repo.getEmployeeEmailCount(empMail)>0;

	}

	
	public Page<Employee> getAllEmployees(Pageable pageable) {
		return repo.findAll(pageable);
	}

	
	
}
