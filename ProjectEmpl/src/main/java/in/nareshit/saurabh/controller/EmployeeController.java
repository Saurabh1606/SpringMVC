package in.nareshit.saurabh.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import in.nareshit.saurabh.exception.EmployeeNotFoundException;
import in.nareshit.saurabh.model.Employee;
import in.nareshit.saurabh.service.IEmployeeService;

@Controller
@RequestMapping("/employee")
public class EmployeeController {

	private Logger log = LoggerFactory.getLogger(EmployeeController.class);

	@Autowired
	private IEmployeeService service;//HAS-A

	//1. To Display Register Page
	@GetMapping("/register")
	public String showRegPage() {
		return "EmployeeRegister";
	}

	/**
	 * 2. On Click submit button read form data
	 *    using @ModelAttribute.
	 *    Call service method for saveEmp
	 *    Read PK(ID) Integer and create String msg
	 *    send Message Back to UI using Model.
	 *    Provide Path /save with POST Type.
	 */
	@PostMapping("/save")
	public String saveEmp(
			@ModelAttribute Employee employee,
			Model model
			) 
	{
		log.info("Entered into save method");
		try {
			Integer id = service.saveEmployee(employee);
			String msg = "Employee '"+id+"' saved";
			log.debug("Employee saved with {}",id);
			model.addAttribute("message", msg);
		} catch (Exception e) {
			log.error("Problem in saving {}",e.getMessage());
			e.printStackTrace();
		}
		log.info("About to Leave Save method");
		return "EmployeeRegister";
	}

	/**
	 * 3. On Enter URL /all + GET Type,
	 *    fetch data from service as List<T>
	 *    Use Model memory to send data to UI.
	 *    Display at EmployeeData Page 
	 */
	//.../all?page=__
	@GetMapping("/all")
	public String showAllEmps(
			@PageableDefault(page=0,size=3) Pageable pageable,
			Model model) 
	{
		Page<Employee> page = service.getAllEmployees(pageable);
		model.addAttribute("page", page);
		model.addAttribute("list", page.getContent());
		return "EmployeeData";
	}
	
	
	/*@GetMapping("/all")
	public String showAllEmps(Model model) {
		getUiEmployeeTableData(model);
		return "EmployeeData";
	}*/

	private void getUiEmployeeTableData(Model model) {
		List<Employee> list = service.getAllEmployees();
		model.addAttribute("list", list);
	}

	/***
	 * 4. Read Id using Request Param
	 * 	  call service for delete operation
	 * 	  redirect back to all page(no message)
	 * 
	 */
	@GetMapping("/delete")
	public String deleteEmp(
			@RequestParam("id") Integer empId,
			Model model
			) 
	{
		String page = null;
		try {
			service.deleteEmployee(empId);
			model.addAttribute("msg", "Employee '"+empId+"' Deleted");

		} catch (EmployeeNotFoundException enfe) {
			page = "EmployeeData";
			model.addAttribute("msg", enfe.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
		page="EmployeeData";
		//fetch latest data after delete
		getUiEmployeeTableData(model);


		return page;
	}

	/***
	 * 5. Show Data in Edit Page
	 *    Read ReqParam 'id', 
	 *    call service -> Get optional object.
	 *    If data present-> Goto edit page
	 *    else come back to data page.
	 */
	@GetMapping("/edit")
	public String showEdit(
			@RequestParam("id")Integer id,
			Model model
			) 
	{
		String page="";
		try {
			Employee emp = service.getOneEmployee(id);
			model.addAttribute("employee", emp);
			page ="EmployeeEdit";
		}  catch (EmployeeNotFoundException enfe) {
			page = "EmployeeData";
			model.addAttribute("msg", enfe.getMessage());
			getUiEmployeeTableData(model);
			//throw enfe;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return page;
		/*
		String page = null;
		Optional<Employee> opt = service.getOneEmployee(id);

		if(opt.isPresent()) {
			Employee emp = opt.get();
			model.addAttribute("employee", emp);
			page = "EmployeeEdit";
		} else {
			page = "redirect:all";
		}

		return page;
		 */
	}

	/***
	 * 6. On click FORM Update, Read ModelAttribute
	 *    Perform updateMethod call.
	 *    Send Redirect/ Fetch Latest Data goto DataPage
	 */
	@PostMapping("/update")
	public String updateEmployee(
			@ModelAttribute Employee employee,
			Model model
			) 
	{
		service.updateEmployee(employee);


		//fetch latest data after delete
		getUiEmployeeTableData(model);

		//create message and display at UI
		model.addAttribute("msg", "Employee '"+employee.getEmpId()+"' Updated!!");
		return "EmployeeData";
		//return "redirect:all";
	}

	//7. AJAX Validate
	@GetMapping("/validateMail")
	public @ResponseBody String validateEmail(
			@RequestParam("email")String empMail
			) 
	{
		String message = "";

		if(service.isEmployeeEmailExist(empMail)) {
			message = empMail+", already exist";
		}

		return message;
	}


}
