package com.sample.ExpenseIncome;

import com.sample.ExpenseIncome.model.Expense;
import com.sample.ExpenseIncome.model.Income;
import com.sample.ExpenseIncome.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:8081")  // Allow CORS from the frontend port

public class TransactionController {

    @Autowired
    private TransactionService service;

    @PostMapping("/expense")
    public Expense addExpense(@RequestBody Expense expense) {
        return service.addExpense(expense);
    }

    @PostMapping("/income")
    public Income addIncome(@RequestBody Income income) {
        return service.addIncome(income);
    }

    
    @GetMapping("/profit-loss")
    public String getProfitLoss(@RequestParam("startDate") String startDateStr, 
                                @RequestParam("endDate") String endDateStr) {
        // Convert the startDate and endDate strings to LocalDate
        LocalDate startDate = LocalDate.parse(startDateStr);
        LocalDate endDate = LocalDate.parse(endDateStr);

        // Calculate profit or loss for the given date range
        return service.getProfitOrLoss(startDate, endDate);
    }
}
