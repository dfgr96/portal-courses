package com.portal.course.domain.model;

public record CourseProgressDto(
        Long courseId,
        String courseTitle,
        int progressPercent,
        boolean completed
) { }
