package com.portal.course.infraestructure.repository;

import com.portal.course.domain.model.Section;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ModuleRepository extends JpaRepository<Section, Long> {
    List<Section> findByCourseIdOrderByOrderIndex(Long courseId);
}
