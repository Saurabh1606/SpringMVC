package in.nareshit.saurabh.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import in.nareshit.saurabh.model.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

	@Query("SELECT COUNT(empMail) FROM Employee WHERE empMail=:empMail")
	Integer getEmployeeEmailCount(String empMail);
}
