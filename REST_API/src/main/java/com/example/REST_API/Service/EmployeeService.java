package com.example.REST_API.Service;

import com.example.REST_API.DTO.EmployeeDto;
import com.example.REST_API.Entity.Employee;
import com.example.REST_API.Repository.EmployeeRepo;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class EmployeeService {
    @Autowired private EmployeeRepo repository;
    @Autowired private ModelMapper mapper;

    @Value("${file.upload-dir}")
    private String uploadDir;

    public void uploadImage(Long id, MultipartFile file) throws IOException {
        Employee emp = (Employee) repository.findById(id).orElseThrow();

        Path root = Paths.get(System.getProperty("user.dir"), uploadDir);
        if (!Files.exists(root)) Files.createDirectories(root);

        String fileName = id + "_" + file.getOriginalFilename();
        Files.copy(file.getInputStream(), root.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);

        emp.setProfilePicturePath(fileName);
        repository.save(emp);
    }

    @Cacheable(value = "employeeImages", key = "#id")
    public Resource fetchImage(Long id) throws IOException {
        Employee emp = (Employee) repository.findById(id)
                .orElseThrow();

        Path filePath = Paths.get(System.getProperty("user.dir"), uploadDir)
                .resolve(emp.getProfilePicturePath());

        Resource resource = new UrlResource(filePath.toUri());
        if (resource.exists()) {
            return resource;
        } else {
            throw new RuntimeException("File not found on disk");
        }
    }

    public EmployeeDto createEmployee(@Valid EmployeeDto dto) {

        Employee employee = mapper.map(dto, Employee.class);
        Employee savedEmployee = (Employee) repository.save(employee);
        return mapper.map(savedEmployee, EmployeeDto.class);
    }

    public List<EmployeeDto> getEmployee() {
        List<Employee> employees = repository.findAll();

        List<EmployeeDto> employeeDtos = employees.stream()
                .map(employee -> mapper.map(employee, EmployeeDto.class))
                .collect(Collectors.toList());

        return employeeDtos;
    }

    public EmployeeDto getEmployeeByID(long id) {
        Optional<Employee> emp;
        emp = repository.findById(id);
        return mapper.map(emp,EmployeeDto.class);
    }

    public String deleteEmployee(@Min(1) Long id) {
        repository.deleteById(id);
        return "deleted";
    }
}



