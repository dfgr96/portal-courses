package com.portal.course.application;

import com.portal.course.domain.enums.EnrollmentStatus;
import com.portal.course.domain.model.EnrollmentDto;
import com.portal.course.infraestructure.repository.EnrollmentEntity;
import com.portal.course.infraestructure.repository.EnrollmentRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class EnrollmentService {

    private final EnrollmentRepository repository;

    public EnrollmentService(EnrollmentRepository repository) {
        this.repository = repository;
    }

    public EnrollmentDto enroll(Long userId, Long courseId) {
        EnrollmentEntity entity = new EnrollmentEntity();
        entity.setUserId(userId);
        entity.setCourseId(courseId);
        entity.setStatus(EnrollmentStatus.ACTIVE);
        EnrollmentEntity saved = repository.save(entity);
        return new EnrollmentDto(saved.getId(), saved.getCourseId(), saved.getUserId(),
                saved.getStatus().name(), saved.getEnrolledAt());
    }

    public List<EnrollmentDto> getUserEnrollments(Long userId) {
        return repository.findByUserId(userId).stream()
                .map(e -> new EnrollmentDto(e.getId(), e.getCourseId(), e.getUserId(),
                        e.getStatus().name(), e.getEnrolledAt()))
                .toList();
    }
}
