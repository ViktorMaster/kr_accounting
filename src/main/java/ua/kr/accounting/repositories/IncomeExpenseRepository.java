package ua.kr.accounting.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.kr.accounting.models.IncomeExpense;

import java.util.List;


public interface IncomeExpenseRepository extends JpaRepository<IncomeExpense, Long> {
    List<IncomeExpense> findByUserId(Long userId);
}
