package com.app.APICode.ctest;

import java.util.List;

import com.app.APICode.employee.EmployeeNotFoundException;
import com.app.APICode.employee.EmployeeRepository;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CtestController {
    private CtestRepository ctests;
    private EmployeeRepository employees;

    public CtestController(CtestRepository ctests, EmployeeRepository employees){
        this.ctests = ctests;
        this.employees = employees;
    }
    
    @GetMapping("/employee/{employee_id}/ctests")
    public List<Ctest> getAllReviewsByEmployeeId(@PathVariable (value = "employee_id") Long id) {
        if(!employees.findByEmployeeId(id).isPresent()) {
            throw new EmployeeNotFoundException(id);
        }
        return ctests.findByEmployee(employees.findByEmployeeId(id).get().getEmployee());
    }

    @PostMapping("/employee/{employee_id}/ctests")
    public Ctest addCtest(@PathVariable (value = "employee_id") Long id, @RequestBody Ctest ctest){
        return employees.findByEmployeeId(id).map(employee ->{
            ctest.setEmployee(employee);
            return ctests.save(ctest);
        }).orElseThrow(() -> new EmployeeNotFoundException(id));
    }

    @PutMapping("/employee//{employee_id}/ctests/{ctestId}")
    public Ctest updateCtest(@PathVariable (value = "employee_id") Long id, @PathVariable(value = "ctestId") Long ctestId, @RequestBody Ctest newCtest){
        if(!employees.findByEmployeeId(id).isPresent()) {
            throw new EmployeeNotFoundException(id);
        }
        return ctests.findByIdAndEmployeeId(id, ctestId).map(ctest -> {
            ctest.setType(newCtest.getType());
            ctest.setDate(newCtest.getDate());
            ctest.setResult(newCtest.getResult());
            return ctests.save(ctest);
        }).orElseThrow(() -> new CtestNotFoundException(id));
    }

    @DeleteMapping("/employee//{employee_id}/ctests/{ctestId}")
    public ResponseEntity<?> deleteCtest(@PathVariable (value = "employee_id") Long employeeId, @PathVariable (value = "ctestId") Long ctestId){
        if(!employees.findByEmployeeId(employeeId).isPresent()) {
            throw new EmployeeNotFoundException(employeeId);
        }

        return ctests.findByIdAndEmployeeId(ctestId, employeeId).map(ctest -> {
            ctests.delete(ctest);
            return ResponseEntity.ok().build();
        }).orElseThrow(() -> new CtestNotFoundException(ctestId));
    }
}
