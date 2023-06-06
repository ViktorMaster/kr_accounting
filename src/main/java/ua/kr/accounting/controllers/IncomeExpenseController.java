package ua.kr.accounting.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ua.kr.accounting.models.IncomeExpense;
import ua.kr.accounting.services.IncomeExpenseService;

import java.security.Principal;
import java.util.List;

@Controller
public class IncomeExpenseController {
    private final IncomeExpenseService incomeExpenseService;

    @Autowired
    public IncomeExpenseController(IncomeExpenseService incomeExpenseService) {
        this.incomeExpenseService = incomeExpenseService;
    }

    @GetMapping("/incomeExpense")
    public String incomeExpenses(Principal principal, Model model) {
        List<IncomeExpense> incomeExpenseList = incomeExpenseService.getIncomeExpensesByPrincipal(principal);
        model.addAttribute("incomeExpenseList", incomeExpenseList);
        model.addAttribute("sum", incomeExpenseList != null ? incomeExpenseService.calculateTotalAmount(incomeExpenseList) : 0);
        return "incomeExpenses";
    }

    @PostMapping("/incomeExpense/filter")
    public String filter(@RequestParam("category") String category,
                         @RequestParam("date1") String date1,
                         @RequestParam("date2") String date2,
                         Principal principal, Model model) {
        List<IncomeExpense> incomeExpenseList = incomeExpenseService.getFilteredIncomeExpenseList(principal, category, date1, date2);
        model.addAttribute("incomeExpenseList", incomeExpenseList);
        model.addAttribute("sum", incomeExpenseList != null ? incomeExpenseService.calculateTotalAmount(incomeExpenseList) : 0);
        model.addAttribute("category", category);
        model.addAttribute("date1", date1);
        model.addAttribute("date2", date2);

        return  "filterIncomeExpense";
    }

    @GetMapping("/incomeExpense/create")
    public String incomeExpensesCreation() {
        return "createIncomeExpense";
    }

    @PostMapping("/incomeExpense/create")
    public String createIncomeExpense(IncomeExpense incomeExpense, Principal principal) {
        incomeExpenseService.saveIncomeExpense(principal, incomeExpense);
        return "redirect:/incomeExpense";
    }

    @PostMapping("/incomeExpense/delete/{id}")
    public String deleteIncomeExpense(@PathVariable Long id) {
        incomeExpenseService.deleteIncomeExpense(id);
        return "redirect:/incomeExpense";
    }

    @GetMapping("/incomeExpense/edit/{id}")
    public String editIncomeExpense(@PathVariable Long id, Model model) {
        model.addAttribute("incomeExpense", incomeExpenseService.getIncomeExpenseById(id));
        return "editIncomeExpense";
    }

    @PostMapping("/incomeExpense/edit/{id}")
    public String updateIncomeExpense(IncomeExpense incomeExpense, @PathVariable Long id) {
        incomeExpenseService.updateIncomeExpense(incomeExpense, id);
        return "redirect:/incomeExpense";
    }
}
