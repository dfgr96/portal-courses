package com.portal.course.infraestructure.controller;

import com.portal.course.application.CourseService;
import com.portal.course.domain.model.Course;
import com.portal.course.domain.model.Section;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/courses")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping
    public List<Course> getAllCourses() {
        return courseService.getAllCourses();
    }

    @GetMapping("/{id}")
    public Course getCourse(@PathVariable Long id) {
        return courseService.getCourse(id);
    }

    @PostMapping
    public ResponseEntity<Course> createCourse(@RequestBody Course course) {
        return ResponseEntity.ok(courseService.createCourse(course));
    }

    @PostMapping("/{id}/modules")
    public ResponseEntity<Section> addModule(@PathVariable Long id, @RequestBody Section module) {
        return ResponseEntity.ok(courseService.addModule(id, module));
    }
}
