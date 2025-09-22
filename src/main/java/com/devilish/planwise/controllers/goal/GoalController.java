package com.devilish.planwise.controllers.goal;

import com.devilish.planwise.dto.goal.GoalRequest;
import com.devilish.planwise.dto.goal.GoalResponse;
import com.devilish.planwise.entities.Goal;
import com.devilish.planwise.services.goal.GoalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/goals")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class GoalController {

    private final GoalService goalService;

    @PostMapping
    public ResponseEntity<GoalResponse> createGoal(@Valid @RequestBody GoalRequest request) {
        try {
            GoalResponse response = goalService.createGoal(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping
    public ResponseEntity<Page<GoalResponse>> getAllGoals(Pageable pageable) {
        try {
            Page<GoalResponse> goals = goalService.getAllGoals(pageable);
            return ResponseEntity.ok(goals);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<GoalResponse>> getAllGoalsList() {
        try {
            List<GoalResponse> goals = goalService.getAllGoals();
            return ResponseEntity.ok(goals);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<GoalResponse>> getGoalsByStatus(@PathVariable Goal.GoalStatus status) {
        try {
            List<GoalResponse> goals = goalService.getGoalsByStatus(status);
            return ResponseEntity.ok(goals);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/expired")
    public ResponseEntity<List<GoalResponse>> getExpiredGoals() {
        try {
            List<GoalResponse> goals = goalService.getExpiredGoals();
            return ResponseEntity.ok(goals);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<GoalResponse> getGoalById(@PathVariable Long id) {
        try {
            GoalResponse goal = goalService.getGoalById(id);
            return ResponseEntity.ok(goal);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<GoalResponse> updateGoal(@PathVariable Long id, @Valid @RequestBody GoalRequest request) {
        try {
            GoalResponse goal = goalService.updateGoal(id, request);
            return ResponseEntity.ok(goal);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("não encontrada")) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}/progress")
    public ResponseEntity<GoalResponse> updateGoalProgress(@PathVariable Long id, @RequestParam BigDecimal currentValue) {
        try {
            GoalResponse goal = goalService.updateGoalProgress(id, currentValue);
            return ResponseEntity.ok(goal);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("não encontrada")) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}/add-progress")
    public ResponseEntity<GoalResponse> addToGoalProgress(@PathVariable Long id, @RequestParam BigDecimal amount) {
        try {
            GoalResponse goal = goalService.addToGoalProgress(id, amount);
            return ResponseEntity.ok(goal);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("não encontrada")) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteGoal(@PathVariable Long id) {
        try {
            goalService.deleteGoal(id);
            return ResponseEntity.ok("Meta deletada com sucesso");
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<GoalResponse>> searchGoals(@RequestParam String search) {
        try {
            List<GoalResponse> goals = goalService.searchGoals(search);
            return ResponseEntity.ok(goals);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/count/status/{status}")
    public ResponseEntity<Long> getGoalsCountByStatus(@PathVariable Goal.GoalStatus status) {
        try {
            Long count = goalService.getGoalsCountByStatus(status);
            return ResponseEntity.ok(count);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/update-expired")
    public ResponseEntity<String> updateExpiredGoals() {
        try {
            goalService.updateExpiredGoals();
            return ResponseEntity.ok("Metas vencidas atualizadas com sucesso");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
