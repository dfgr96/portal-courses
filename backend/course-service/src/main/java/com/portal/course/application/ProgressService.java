package com.portal.course.application;

import com.portal.course.domain.model.ModuleProgressDto;
import com.portal.course.domain.model.ProgressDto;
import com.portal.course.domain.model.Section;
import com.portal.course.infraestructure.repository.ModuleRepository;
import com.portal.course.infraestructure.repository.ProgressEntity;
import com.portal.course.infraestructure.repository.ProgressRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ProgressService {

    private final ProgressRepository progressRepository;
    private final ModuleRepository moduleRepository;

    public ProgressService(ProgressRepository repository, ModuleRepository moduleRepository) {
        this.progressRepository = repository;
        this.moduleRepository = moduleRepository;
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
}
