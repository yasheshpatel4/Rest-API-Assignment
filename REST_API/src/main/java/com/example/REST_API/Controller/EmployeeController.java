package com.example.REST_API.Controller;

import com.example.REST_API.DTO.EmployeeDto;
import com.example.REST_API.Service.EmployeeService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/employees")
public class EmployeeController {
    @Autowired
    private EmployeeService service;

    @PostMapping
    public ResponseEntity<EmployeeDto> create(@Valid @RequestBody EmployeeDto dto) {
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
        ResponseCookie cookie = ResponseCookie.from("api-token", "dummy-value")
                .httpOnly(true).secure(true).path("/").maxAge(3600).build();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).body("Cookie set!");
    }
}
