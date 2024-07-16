package com.example.employeemanagement.employee;

import com.example.employeemanagement.exception.EmailException;
import com.example.employeemanagement.filestorage.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    private final FileStorageService fileStorageService;


    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public Optional<Employee> getEmployeeById(Long id) {
        return employeeRepository.findById(id);
    }

    public Employee createEmployee(Employee employee) {
        if (employeeRepository.findByEmail(employee.getEmail()).isPresent()) {
            throw new RuntimeException("Employee with email " + employee.getEmail() + " already exists");
        }
        return employeeRepository.save(employee);
    }

    public Employee updateEmployee(Long id, Employee employeeDetails) {
        Employee employee = employeeRepository.findById(id).orElseThrow(() -> new RuntimeException("Employee not found"));
        if (!employee.getEmail().equals(employeeDetails.getEmail()) && employeeRepository.findByEmail(employeeDetails.getEmail()).isPresent()) {
            throw new EmailException("Employee with email " + employeeDetails.getEmail() + " already exists");
        }
        employee.setName(employeeDetails.getName());
        employee.setLastName(employeeDetails.getLastName());
        employee.setEmail(employeeDetails.getEmail());
        employee.setDepartment(employeeDetails.getDepartment());
        return employeeRepository.save(employee);
    }

    public void deleteEmployee(Long id) {
        if (!employeeRepository.existsById(id)) {
            throw new RuntimeException("Employee not found");
        }
        employeeRepository.deleteById(id);
    }

    public ResponseEntity<Employee> uploadEmployeePhoto(Long id, MultipartFile file) {
        return (ResponseEntity<Employee>) employeeRepository.findById(id).map(employee -> {
            String fileName = id + "_" + file.getOriginalFilename();
            try {
                fileStorageService.storeFile(file, fileName);
                employee.setPhoto(fileName);
                employeeRepository.save(employee);
                return new ResponseEntity<>(employee, HttpStatus.OK);
            } catch (IOException e) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
