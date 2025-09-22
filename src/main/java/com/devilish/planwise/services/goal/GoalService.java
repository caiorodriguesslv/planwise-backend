package com.devilish.planwise.services.goal;

import com.devilish.planwise.dto.goal.GoalRequest;
import com.devilish.planwise.dto.goal.GoalResponse;
import com.devilish.planwise.entities.Goal;
import com.devilish.planwise.entities.User;
import com.devilish.planwise.repository.goal.GoalRepository;
import com.devilish.planwise.services.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GoalService {

    private final GoalRepository goalRepository;
    private final UserService userService;

    @Transactional
    public GoalResponse createGoal(GoalRequest request) {
        User currentUser = userService.getCurrentUserEntity();

        Goal goal = new Goal();
        goal.setDescription(request.getDescription());
        goal.setTargetValue(request.getTargetValue());
        goal.setCurrentValue(BigDecimal.ZERO);
        goal.setDeadline(request.getDeadline());
        goal.setStatus(Goal.GoalStatus.EM_ANDAMENTO);
        goal.setUser(currentUser);
        goal.setCreatedAt(LocalDateTime.now());
        goal.setActive(true);

        Goal savedGoal = goalRepository.save(goal);
        return GoalResponse.fromGoal(savedGoal);
    }

    public Page<GoalResponse> getAllGoals(Pageable pageable) {
        User currentUser = userService.getCurrentUserEntity();
        Page<Goal> goals = goalRepository.findByUserAndActiveTrueOrderByCreatedAtDesc(currentUser.getId(), pageable);
        return goals.map(GoalResponse::fromGoal);
    }

    public List<GoalResponse> getAllGoals() {
        User currentUser = userService.getCurrentUserEntity();
        List<Goal> goals = goalRepository.findByUserAndActiveTrueOrderByCreatedAtDesc(currentUser.getId());
        return goals.stream()
                .map(GoalResponse::fromGoal)
                .collect(Collectors.toList());
    }

    public List<GoalResponse> getGoalsByStatus(Goal.GoalStatus status) {
        User currentUser = userService.getCurrentUserEntity();
        List<Goal> goals = goalRepository.findByUserAndStatusAndActiveTrueOrderByCreatedAtDesc(currentUser.getId(), status);
        return goals.stream()
                .map(GoalResponse::fromGoal)
                .collect(Collectors.toList());
    }

    public List<GoalResponse> getExpiredGoals() {
        User currentUser = userService.getCurrentUserEntity();
        List<Goal> goals = goalRepository.findByUserAndDeadlineBeforeAndStatusNotAndActiveTrueOrderByDeadlineAsc(
                currentUser.getId(), LocalDate.now(), Goal.GoalStatus.ATINGIDA);
        return goals.stream()
                .map(GoalResponse::fromGoal)
                .collect(Collectors.toList());
    }

    public GoalResponse getGoalById(Long id) {
        User currentUser = userService.getCurrentUserEntity();
        Goal goal = goalRepository.findByIdAndUserAndActiveTrue(id, currentUser.getId())
                .orElseThrow(() -> new RuntimeException("Meta não encontrada"));
        return GoalResponse.fromGoal(goal);
    }

    @Transactional
    public GoalResponse updateGoal(Long id, GoalRequest request) {
        User currentUser = userService.getCurrentUserEntity();
        Goal goal = goalRepository.findByIdAndUserAndActiveTrue(id, currentUser.getId())
                .orElseThrow(() -> new RuntimeException("Meta não encontrada"));

        goal.setDescription(request.getDescription());
        goal.setTargetValue(request.getTargetValue());
        goal.setDeadline(request.getDeadline());

        // Recalcular status se necessário
        updateGoalStatus(goal);

        Goal savedGoal = goalRepository.save(goal);
        return GoalResponse.fromGoal(savedGoal);
    }

    @Transactional
    public GoalResponse updateGoalProgress(Long id, BigDecimal newCurrentValue) {
        User currentUser = userService.getCurrentUserEntity();
        Goal goal = goalRepository.findByIdAndUserAndActiveTrue(id, currentUser.getId())
                .orElseThrow(() -> new RuntimeException("Meta não encontrada"));

        goal.setCurrentValue(newCurrentValue);
        updateGoalStatus(goal);

        Goal savedGoal = goalRepository.save(goal);
        return GoalResponse.fromGoal(savedGoal);
    }

    @Transactional
    public GoalResponse addToGoalProgress(Long id, BigDecimal amount) {
        User currentUser = userService.getCurrentUserEntity();
        Goal goal = goalRepository.findByIdAndUserAndActiveTrue(id, currentUser.getId())
                .orElseThrow(() -> new RuntimeException("Meta não encontrada"));

        BigDecimal newCurrentValue = goal.getCurrentValue().add(amount);
        goal.setCurrentValue(newCurrentValue);
        updateGoalStatus(goal);

        Goal savedGoal = goalRepository.save(goal);
        return GoalResponse.fromGoal(savedGoal);
    }

    @Transactional
    public void deleteGoal(Long id) {
        User currentUser = userService.getCurrentUserEntity();
        Goal goal = goalRepository.findByIdAndUserAndActiveTrue(id, currentUser.getId())
                .orElseThrow(() -> new RuntimeException("Meta não encontrada"));

        // Soft delete
        goal.setActive(false);
        goalRepository.save(goal);
    }

    public List<GoalResponse> searchGoals(String search) {
        User currentUser = userService.getCurrentUserEntity();
        List<Goal> goals = goalRepository.findByUserAndDescriptionContainingIgnoreCaseAndActiveTrue(currentUser.getId(), search);
        return goals.stream()
                .map(GoalResponse::fromGoal)
                .collect(Collectors.toList());
    }

    public Long getGoalsCountByStatus(Goal.GoalStatus status) {
        User currentUser = userService.getCurrentUserEntity();
        return goalRepository.countByUserAndStatusAndActiveTrue(currentUser.getId(), status);
    }

    @Transactional
    public void updateExpiredGoals() {
        User currentUser = userService.getCurrentUserEntity();
        List<Goal> expiredGoals = goalRepository.findByUserAndDeadlineBeforeAndStatusNotAndActiveTrueOrderByDeadlineAsc(
                currentUser.getId(), LocalDate.now(), Goal.GoalStatus.ATINGIDA);

        for (Goal goal : expiredGoals) {
            goal.setStatus(Goal.GoalStatus.VENCIDA);
            goalRepository.save(goal);
        }
    }

    private void updateGoalStatus(Goal goal) {
        if (goal.isAchieved()) {
            goal.setStatus(Goal.GoalStatus.ATINGIDA);
        } else if (goal.isExpired()) {
            goal.setStatus(Goal.GoalStatus.VENCIDA);
        } else {
            goal.setStatus(Goal.GoalStatus.EM_ANDAMENTO);
        }
    }
}
