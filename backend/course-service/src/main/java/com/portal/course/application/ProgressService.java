package com.portal.course.application;

import com.portal.course.domain.model.CourseProgressDto;
import com.portal.course.domain.model.ModuleProgressDto;
import com.portal.course.domain.model.ProgressDto;
import com.portal.course.domain.model.Section;
import com.portal.course.infraestructure.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ProgressService {

    private final ProgressRepository progressRepository;
    private final ModuleRepository moduleRepository;
    private final EnrollmentRepository enrollmentRepository;

    public ProgressService(ProgressRepository repository, ModuleRepository moduleRepository, EnrollmentRepository enrollmentRepository) {
        this.progressRepository = repository;
        this.moduleRepository = moduleRepository;
        this.enrollmentRepository = enrollmentRepository;
    }

    @Transactional
    public ProgressDto updateProgress(Long userId, Long moduleId, Integer percent) {
        ProgressEntity entity = progressRepository.findByUserIdAndModuleId(userId, moduleId)
                .stream()
                .findFirst()
                .orElseGet(() -> {
                    ProgressEntity p = new ProgressEntity();
                    p.setUserId(userId);
                    p.setModuleId(moduleId);
                    return p;
                });

        entity.setPercent(percent);
        if (percent == 100) {
            entity.setCompletedAt(Timestamp.from(Instant.now()));
        }

        ProgressEntity saved = progressRepository.save(entity);
        return new ProgressDto(saved.getId(), saved.getUserId(), saved.getModuleId(),
                saved.getPercent(), saved.getCompletedAt());
    }

    @Transactional(readOnly = true)
    public List<ModuleProgressDto> getProgressByCourse(Long userId, Long courseId) {
        List<Section> modules = moduleRepository.findByCourseIdOrderByOrderIndex(courseId);

        if (modules.isEmpty()) {
            return List.of(); //lanzar 404
        }

        List<Long> moduleIds = modules.stream().map(Section::getId).toList();
        List<ProgressEntity> progresses = progressRepository.findByUserIdAndModuleIdIn(userId, moduleIds);

        Map<Long, ProgressEntity> progressMap = progresses.stream()
                .collect(Collectors.toMap(ProgressEntity::getModuleId, p -> p));

        return modules.stream()
                .map(m -> {
                    ProgressEntity p = progressMap.get(m.getId());
                    Integer percent = (p != null) ? p.getPercent() : 0;
                    Timestamp completedAt = (p != null) ? p.getCompletedAt() : null;
                    return new ModuleProgressDto(
                            m.getId(),
                            m.getTitle(),
                            m.getContentUrl(),
                            m.getContentType() != null ? m.getContentType().name() : null,
                            percent,
                            completedAt
                    );
                })
                .toList();
    }

    public List<CourseProgressDto> getUserCoursesProgress(Long userId) {
        List<EnrollmentEntity> enrollments =
                enrollmentRepository.findByUserId(userId);

        List<CourseProgressDto> result = new ArrayList<>();

        for (EnrollmentEntity enrollment : enrollments) {
            Long courseId = enrollment.getCourseId();

            List<Section> modules = moduleRepository.findByCourseIdOrderByOrderIndex(courseId);

            if (modules.isEmpty()) {
                result.add(new CourseProgressDto(courseId, "Sin módulos", 0, false));
                continue;
            }

            int totalPercent = 0;
            int completedModules = 0;

            for (Section module : modules) {
                List<ProgressEntity> progresses =
                        progressRepository.findByUserIdAndModuleId(userId, module.getId());

                if (!progresses.isEmpty()) {
                    int percent = progresses.get(0).getPercent();
                    totalPercent += percent;
                    if (percent == 100) {
                        completedModules++;
                    }
                }
            }

            int avgPercent = totalPercent / modules.size();
            boolean completed = completedModules == modules.size();

            result.add(new CourseProgressDto(courseId, "Curso " + courseId, avgPercent, completed));
        }

        return result;
    }
}
