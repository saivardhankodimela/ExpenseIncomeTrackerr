package com.sample.ExpenseIncome.service;

import com.sample.ExpenseIncome.ExpenseCategory;
import com.sample.ExpenseIncome.model.Expense;
import com.sample.ExpenseIncome.model.Income;
import com.sample.ExpenseIncome.repository.ExpenseRepository;
import com.sample.ExpenseIncome.repository.IncomeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class TransactionService {

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private IncomeRepository incomeRepository;

    // Add Expense
    public Expense addExpense(Expense expense) {
        if (expense.getCategory() == ExpenseCategory.MONTHLY) {
            // Divide the monthly expense into daily expenses
            return addMonthlyExpenseAsDailyCharges(expense);
        }
        // For non-monthly expenses, save directly
        return expenseRepository.save(expense);
    }

    private Expense addMonthlyExpenseAsDailyCharges(Expense expense) {
        // Get the number of days in the month for the given expense date
        LocalDate expenseDate = expense.getDate();
        int daysInMonth = expenseDate.lengthOfMonth(); // Number of days in the month

        double dailyAmount = expense.getAmount() / daysInMonth; // Divide amount by days in month

        // Create daily expenses for each day in the month
        List<Expense> dailyExpenses = new ArrayList<>();
        for (int i = 1; i <= daysInMonth; i++) {
            LocalDate dailyDate = expenseDate.withDayOfMonth(i); // Set date to each day in the month
            Expense dailyExpense = new Expense();
            dailyExpense.setDescription(expense.getDescription());
            dailyExpense.setAmount(dailyAmount);
            dailyExpense.setDate(dailyDate);
            dailyExpense.setCategory(ExpenseCategory.DAILY_EXPENSE); // Set the category to daily expense
            dailyExpenses.add(dailyExpense);
        }

        // Save each daily expense in the database
        expenseRepository.saveAll(dailyExpenses);

        // Return the first daily expense (optional, can return any one)
        return dailyExpenses.get(0);
    }

    // Add Income
    public Income addIncome(Income income) {
        return incomeRepository.save(income);
    }

    // Get Profit or Loss based on dates
    public String getProfitOrLoss(LocalDate startDate, LocalDate endDate) {
        List<Expense> expenses = expenseRepository.findByDateBetween(startDate, endDate);
        List<Income> incomes = incomeRepository.findByDateBetween(startDate, endDate);

        // Unbox Double to double using doubleValue() method
        double totalExpense = expenses.stream().mapToDouble(expense -> expense.getAmount()).sum();
        double totalIncome = incomes.stream().mapToDouble(income -> income.getAmount()).sum();

        if (totalIncome > totalExpense) {
            double profit = totalIncome - totalExpense;
            return "Profit = " + profit; // Return profit formatted message
        } else if (totalExpense > totalIncome) {
            double loss = totalExpense - totalIncome;
            return "Loss = " + loss; // Return loss formatted message
        } else {
            return "Break-even"; // No profit, no loss
        }
    }
}
