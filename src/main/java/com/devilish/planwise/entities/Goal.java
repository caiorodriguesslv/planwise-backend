package com.devilish.planwise.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_goal")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Goal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Descrição é obrigatória")
    @Size(min = 3, max = 100, message = "Descrição deve ter entre 3 e 100 caracteres")
    @Column(nullable = false)
    private String description;

    @NotNull(message = "Valor alvo é obrigatório")
    @DecimalMin(value = "0.01", message = "Valor alvo deve ser maior que zero")
    @Column(name = "target_value", nullable = false, precision = 10, scale = 2)
    private BigDecimal targetValue;

    @NotNull(message = "Valor atual é obrigatório")
    @DecimalMin(value = "0.00", message = "Valor atual não pode ser negativo")
    @Column(name = "current_value", nullable = false, precision = 10, scale = 2)
    private BigDecimal currentValue = BigDecimal.ZERO;

    @NotNull(message = "Data limite é obrigatória")
    @Column(nullable = false)
    private LocalDate deadline;

    @NotNull(message = "Status é obrigatório")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GoalStatus status = GoalStatus.EM_ANDAMENTO;

    @Column(name = "data_criacao")
    private LocalDateTime createdAt;

    @Column(name = "ativo")
    private Boolean active = true;

    // Relacionamento com User
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public enum GoalStatus {
        EM_ANDAMENTO, ATINGIDA, VENCIDA
    }

    // Método para calcular o progresso da meta
    public BigDecimal getProgressPercentage() {
        if (targetValue.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        return currentValue.divide(targetValue, 4, BigDecimal.ROUND_HALF_UP)
                .multiply(BigDecimal.valueOf(100));
    }

    // Método para verificar se a meta foi atingida
    public boolean isAchieved() {
        return currentValue.compareTo(targetValue) >= 0;
    }

    // Método para verificar se a meta está vencida
    public boolean isExpired() {
        return LocalDate.now().isAfter(deadline) && !isAchieved();
    }
}
