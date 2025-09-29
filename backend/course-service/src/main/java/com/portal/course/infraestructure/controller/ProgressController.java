package com.portal.course.infraestructure.controller;

import com.portal.course.application.ProgressService;
import com.portal.course.domain.model.ModuleProgressDto;
import com.portal.course.domain.model.ProgressDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/progress")
public class ProgressController {

    private final ProgressService service;

    public ProgressController(ProgressService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ProgressDto> updateProgress(@RequestParam Long userId,
                                                      @RequestParam Long moduleId,
                                                      @RequestParam Integer percent) {
        return ResponseEntity.ok(service.updateProgress(userId, moduleId, percent));
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<ModuleProgressDto>> getProgressByCourse(@RequestParam Long userId,
                                                                       @PathVariable Long courseId) {
        List<ModuleProgressDto> list = service.getProgressByCourse(userId, courseId);
        return ResponseEntity.ok(list);
    }
}
