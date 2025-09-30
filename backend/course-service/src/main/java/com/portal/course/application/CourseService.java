package com.portal.course.application;

import com.portal.course.domain.model.Course;
import com.portal.course.domain.model.Section;
import com.portal.course.infraestructure.repository.CourseRepository;
import com.portal.course.infraestructure.repository.ModuleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseService {

    private final CourseRepository courseRepository;
    private final ModuleRepository moduleRepository;

    public CourseService(CourseRepository courseRepository, ModuleRepository moduleRepository) {
        this.courseRepository = courseRepository;
        this.moduleRepository = moduleRepository;
    }

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public Course getCourse(Long id) {
        return courseRepository.findById(id).orElseThrow();
    }

    public Course createCourse(Course course) {
        for (Section module : course.getModules()) {
            module.setCourse(course);
        }
        return courseRepository.save(course);
    }

    public Section addModule(Long courseId, Section module) {
        Course course = getCourse(courseId);
        module.setCourse(course);
        return moduleRepository.save(module);
    }
}
