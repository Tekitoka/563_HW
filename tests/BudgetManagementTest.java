package tests;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import java.time.LocalDate;
import main.*;

@ExtendWith(TestResultLogger.class)
public class BudgetManagementTest {
    public Budget budget;
    private User user;

    @BeforeEach
    void setUp() {
        user = new User("testUser", "password123");
        budget = new Budget(1000.0, user);
    }

    @Test
    void testAddValidExpense() {
        Expense expense = new Expense("Food", 200.0, LocalDate.now());
        budget.addExpense(expense, user);
        Assertions.assertEquals(800.0, budget.getRemainingBudget(user));
    }

    @Test
    void testAddInvalidExpense() {
        Expense expense = new Expense("Rent", 1200.0, LocalDate.now());
        assertThrows(IllegalArgumentException.class, () -> budget.addExpense(expense, user));
    }

    @Test
    void testAddZeroExpense() {
        Expense expense = new Expense("Snacks", 0.0, LocalDate.now());
        assertThrows(IllegalArgumentException.class, () -> budget.addExpense(expense, user));
    }

    @Test
    void testAddNegativeExpense() {
        Expense expense = new Expense("Mistake", -50.0, LocalDate.now());
        assertThrows(IllegalArgumentException.class, () -> budget.addExpense(expense, user));
    }

    @Test
    void testMultipleExpenseAddition() {
        budget.addExpense(new Expense("Groceries", 100.0, LocalDate.now()), user);
        budget.addExpense(new Expense("Fuel", 50.0, LocalDate.now()), user);
        Assertions.assertEquals(850.0, budget.getRemainingBudget(user), 0.01);
    }

    @Test
    void testRetrieveExpenses() {
        budget.addExpense(new Expense("Groceries", 100.0, LocalDate.now()), user);
        Assertions.assertEquals(1, budget.getExpenses(user).size());
    }

    @Test
    void testBudgetChangesAfterIncome() {
        double initialBudget = budget.getRemainingBudget(user);
        budget.addIncome(new Income("Bonus", 200.0), user);
        double newBudget = budget.getRemainingBudget(user);
        assertNotEquals(initialBudget, newBudget);
    }

    @Test
    void testAddNegativeIncome() {
        Income income = new Income("Bonus", -100.0);
        assertThrows(IllegalArgumentException.class, () -> budget.addIncome(income, user));
    }

    @Test
    void testAddZeroIncome() {
        Income income = new Income("None", 0.0);
        assertThrows(IllegalArgumentException.class, () -> budget.addIncome(income, user));
    }

    @Test
    void testBudgetUpdateAfterMultipleIncomes() {
        budget.addIncome(new Income("Salary", 500.0), user);
        budget.addIncome(new Income("Gift", 200.0), user);
        Assertions.assertEquals(1700.0, budget.getRemainingBudget(user), 0.01);
    }

    @Test
    void testRetrieveIncomes() {
        budget.addIncome(new Income("Job", 300.0), user);
        Assertions.assertEquals(1, budget.getIncomes(user).size());
    }

    @RepeatedTest(3)
    void testRepeatedAddingIncome() {
        Income income = new Income("Bonus", 100.0);
        budget.addIncome(income, user);
        assertTrue(budget.getRemainingBudget(user) > 1000.0);
    }

    @ParameterizedTest
    @CsvSource({"Food, 100", "Transport, 50", "Entertainment, 75"})
    void testParameterizedAddExpense(String category, double amount) {
        Expense expense = new Expense(category, amount, LocalDate.now());
        budget.addExpense(expense, user);
        assertTrue(budget.getRemainingBudget(user) <= 1000.0);
    }

    @Test
    void testInitialBudgetHasNoExpenses() {
        Assertions.assertEquals(0, budget.getExpenses(user).size());
    }

    @Test
    void testInitialBudgetHasNoIncomes() {
        Assertions.assertEquals(0, budget.getIncomes(user).size());
    }

    @Test
    void testUnauthorizedAccess() {
        User anotherUser = new User("otherUser", "pass123");
        assertThrows(SecurityException.class, () -> budget.getRemainingBudget(anotherUser));
    }

}

