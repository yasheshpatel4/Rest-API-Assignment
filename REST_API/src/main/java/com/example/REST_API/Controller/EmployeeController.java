package com.example.REST_API.Controller;

import com.example.REST_API.DTO.EmployeeDto;
import com.example.REST_API.Service.EmployeeService;
import com.example.REST_API.validate.OnUpdate;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/v1/employees")
public class EmployeeController {
    @Autowired
    private EmployeeService service;

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteEmployee(@PathVariable @Min(1) Long id){
        return new ResponseEntity<>(service.deleteEmployee(id),HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDto> getEmployeeById(@PathVariable @Min(1) Long id) {
        return new ResponseEntity<>(service.getEmployeeByID(id),HttpStatus.CREATED);
    }

    @PostMapping
    public ResponseEntity<EmployeeDto> create(@Validated(OnUpdate.class) @RequestBody EmployeeDto dto) {
        return new ResponseEntity<>(service.createEmployee(dto), HttpStatus.CREATED);
    }

    @PostMapping("/{id}/image")
    public ResponseEntity<String> upload(@PathVariable Long id, @RequestParam("file") MultipartFile file) throws IOException {
        service.uploadImage(id, file);
        return ResponseEntity.ok("Image uploaded successfully");
    }

    @GetMapping("/{id}/image")
    public ResponseEntity<Resource> getImage(@PathVariable Long id) throws IOException {
        Resource file = service.fetchImage(id);

        String contentType = "application/octet-stream";
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + file.getFilename() + "\"")
                .contentType(MediaType.IMAGE_JPEG)
                .body(file);
    }

    @GetMapping("/cookie")
    public ResponseEntity<String> setCookie(HttpServletResponse response) {
        ResponseCookie cookie = ResponseCookie.from("token", "token")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(3600)
                .build();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).body("Cookie set!");
    }
    @GetMapping("/all")
    public  ResponseEntity<List<EmployeeDto>> getEmployee(){
        return new ResponseEntity<List<EmployeeDto>>((List<EmployeeDto>) service.getEmployee(),HttpStatus.CREATED);
    }
}
