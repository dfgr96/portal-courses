package com.portal.course.infraestructure.repository;

import com.portal.course.domain.model.Section;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ModuleRepository extends JpaRepository<Section, Long> {
}
