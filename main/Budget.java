package main;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Budget {
    private double totalBudget;
    private List<Expense> expenses;
    private List<Income> incomes;
    private User authorizedUser;
    
    public Budget(double totalBudget, User user) {
        this.totalBudget = totalBudget;
        this.expenses = new ArrayList<>();
        this.incomes = new ArrayList<>();
        this.authorizedUser = user;
    }

    public void addExpense(Expense expense, User user) {
        if (isAuthorized(user)) {
            if (expense.getAmount() <= 0) {
                throw new IllegalArgumentException("main.Expense amount must be positive");
            }
            if (expense.getAmount() > getRemainingBudget(user)) {
                throw new IllegalArgumentException("Insufficient budget for this expense");
            }
            expenses.add(expense);
            totalBudget -= expense.getAmount();
        } else {
            throw new SecurityException("Unauthorized access");
        }
    }


    public void addIncome(Income income, User user) {
        if (isAuthorized(user)) {
            if (income.getAmount() <= 0) {
                throw new IllegalArgumentException("main.Income amount must be positive");
            }
            incomes.add(income);
            totalBudget += income.getAmount();
        } else {
            throw new SecurityException("Unauthorized access");
        }
    }
    
    public double getRemainingBudget(User user) {
        if (isAuthorized(user)) {
            return totalBudget;
        } else {
            throw new SecurityException("Unauthorized access");
        }
    }

    public List<Expense> getExpenses(User user) {
        if (isAuthorized(user)) {
            return new ArrayList<>(expenses);
        } else {
            throw new SecurityException("Unauthorized access");
        }
    }

    public List<Income> getIncomes(User user) {
        if (isAuthorized(user)) {
            return new ArrayList<>(incomes);
        } else {
            throw new SecurityException("Unauthorized access");
        }
    }


    private boolean isAuthorized(User user) {
        return authorizedUser.equals(user);
    }
}

