package ua.kr.accounting.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.kr.accounting.models.IncomeExpense;
import ua.kr.accounting.models.User;
import ua.kr.accounting.repositories.IncomeExpenseRepository;
import ua.kr.accounting.repositories.UserRepository;

import java.security.Principal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class IncomeExpenseService {
    private final IncomeExpenseRepository incomeExpenseRepository;
    private final UserRepository userRepository;

    @Autowired
    public IncomeExpenseService (IncomeExpenseRepository incomeExpenseRepository, UserRepository userRepository) {
        this.incomeExpenseRepository = incomeExpenseRepository;
        this.userRepository = userRepository;
    }

    public List<IncomeExpense> getIncomeExpenseList() {
        return incomeExpenseRepository.findAll();
    }

    public List<IncomeExpense> getIncomeExpensesByPrincipal(Principal principal) {
        return incomeExpenseRepository.findByUserId(getUserByPrincipal(principal).getId());
    }

    public List<IncomeExpense> getFilteredIncomeExpenseList(Principal principal, String category, String startDate, String endDate) {
        List<IncomeExpense> filteredList = new ArrayList<>();
        List<IncomeExpense> byCategoryList = incomeExpenseRepository.findByUserId(getUserByPrincipal(principal).getId());

        if (!"".equals(category) && category != null) {
            byCategoryList = filterByCategory(byCategoryList, category);
        }

        LocalDate start;
        LocalDate end;

        if ("".equals(startDate) || startDate == null) {
            start = LocalDate.MIN;
        } else start = LocalDate.parse(startDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        if ("".equals(endDate) || endDate == null) {
            end = LocalDate.MAX;
        } else end = LocalDate.parse(endDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        for (IncomeExpense incomeExpense : byCategoryList) {
            LocalDate date = LocalDate.parse(incomeExpense.getDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));

            if ((date.isEqual(start) || date.isAfter(start)) && (date.isEqual(end) || date.isBefore(end))) {
                filteredList.add(incomeExpense);
            }
        }

        return filteredList;
    }

    public List<IncomeExpense> filterByCategory(List<IncomeExpense> incomeExpenseList, String category) {
        List<IncomeExpense> filteredList = new ArrayList<>();

        for (IncomeExpense incomeExpense : incomeExpenseList) {
            if (incomeExpense.getCategory().equals(category)) {
                filteredList.add(incomeExpense);
            }
        }

        return filteredList;
    }

    public double calculateTotalAmount(List<IncomeExpense> incomeExpenses) {
        double totalAmount = 0.0;
        for (IncomeExpense incomeExpense : incomeExpenses) {
            totalAmount += incomeExpense.getAmount();
        }
        return totalAmount;
    }

    public void saveIncomeExpense(Principal principal, IncomeExpense incomeExpense) {
        incomeExpense.setUser(getUserByPrincipal(principal));
        incomeExpenseRepository.save(incomeExpense);
    }

    public User getUserByPrincipal(Principal principal) {
        if (principal == null) return new User();
        return userRepository.findByEmail(principal.getName());
    }

    public void deleteIncomeExpense (Long id) {
        incomeExpenseRepository.deleteById(id);
    }

    public IncomeExpense getIncomeExpenseById(Long id) {
        return incomeExpenseRepository.findById(id).orElse(null);
    }

    public void updateIncomeExpense(IncomeExpense updatedIncomeExpense, Long id) {
        IncomeExpense incomeExpense = incomeExpenseRepository.findById(id).orElseThrow();

        incomeExpense.setDescription(updatedIncomeExpense.getDescription());
        incomeExpense.setAmount(updatedIncomeExpense.getAmount());
        incomeExpense.setCategory(updatedIncomeExpense.getCategory());
        incomeExpense.setDate(updatedIncomeExpense.getDate());

        incomeExpenseRepository.save(incomeExpense);
    }
}
