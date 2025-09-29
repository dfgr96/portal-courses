package com.portal.course.domain.model;

import java.sql.Timestamp;

public record ModuleProgressDto(
        Long moduleId,
        String moduleTitle,
        String contentUrl,
        String contentType,
        Integer percent,
        Timestamp completedAt
) {}
