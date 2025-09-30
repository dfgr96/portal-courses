package com.portal.course.infraestructure.controller;

import com.portal.course.application.EnrollmentService;
import com.portal.course.domain.model.EnrollmentDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/enrollments")
public class EnrollmentController {

    private final EnrollmentService service;

    public EnrollmentController(EnrollmentService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<EnrollmentDto> enroll(@RequestParam Long userId, @RequestParam Long courseId) {
        return ResponseEntity.ok(service.enroll(userId, courseId));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<EnrollmentDto>> getUserEnrollments(@PathVariable Long userId) {
        return ResponseEntity.ok(service.getUserEnrollments(userId));
    }
}
