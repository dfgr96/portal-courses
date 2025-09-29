package com.portal.course.infraestructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProgressRepository extends JpaRepository<ProgressEntity, Long> {
    List<ProgressEntity> findByUserIdAndModuleId(Long userId, Long moduleId);
    List<ProgressEntity> findByUserId(Long userId);
    List<ProgressEntity> findByUserIdAndModuleIdIn(Long userId, List<Long> moduleIds);
    Optional<ProgressEntity> findFirstByUserIdAndModuleId(Long userId, Long moduleId);
}
