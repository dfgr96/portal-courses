package com.portal.course.infraestructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EnrollmentRepository extends JpaRepository<EnrollmentEntity, Long> {
    List<EnrollmentEntity> findByUserId(Long userId);
    Optional<EnrollmentEntity> findByUserIdAndCourseId(Long userId, Long courseId);
}
