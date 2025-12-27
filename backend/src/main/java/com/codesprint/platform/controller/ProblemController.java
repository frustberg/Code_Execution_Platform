package com.codesprint.platform.controller;

import com.codesprint.platform.dto.ProblemDto;
import com.codesprint.platform.model.Problem;
import com.codesprint.platform.repo.ProblemRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/problems")
@CrossOrigin(origins = "http://localhost:5173")
public class ProblemController {

    private final ProblemRepository problemRepository;

    public ProblemController(ProblemRepository problemRepository) {
        this.problemRepository = problemRepository;
    }

    @GetMapping
    public List<ProblemDto> listProblems(@RequestParam(defaultValue = "0") int page,
                                         @RequestParam(defaultValue = "50") int limit) {
        return problemRepository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ProblemDto getProblem(@PathVariable String id,
                                 @RequestParam(defaultValue = "python") String language) {
        Problem problem = problemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Problem not found"));
        return toDto(problem);
    }

    private ProblemDto toDto(Problem p) {
        return ProblemDto.builder()
                .id(p.getId())
                .title(p.getTitle())
                .statement(p.getStatement())
                .level(p.getLevel())
                .tags(p.getTags())
                .codeStubs(p.getCodeStubs())
                .build();
    }
}