package persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;

import domain.BudgetCycle;
import domain.CycleStatus;
import domain.Expense;
import domain.Category;

/**
 * Singleton data-access object that manages all SQLite database operations
 * for the Mizan budget application.
 *
 * <p>The single shared {@link Connection} is opened on first access and reused
 * for the lifetime of the application. Tables are created automatically on
 * first run via {@link #initializeTables()}.</p>
 *
 * <p>Usage:</p>
 * <pre>
 *     DatabaseHelper db = DatabaseHelper.getInstance();
 *     db.insertExpense(expense);
 * </pre>
 */
public class DatabaseHelper {

    /** The sole instance of this class (Singleton pattern). */
    private static DatabaseHelper instance = null;

    /** Active SQLite connection used for all queries. */
    private Connection connection;

    /** DDL statement that creates the {@code budget_cycle} table if absent. */
    private static final String CREATE_BUDGET_CYCLE_TABLE =
        "CREATE TABLE IF NOT EXISTS budget_cycle (" +
        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
        "allowance REAL NOT NULL, " +
        "start_date TEXT NOT NULL, " +
        "end_date TEXT NOT NULL, " +
        "status TEXT NOT NULL" +
        ")";

    /** DDL statement that creates the {@code category} table if absent. */
    private static final String CREATE_CATEGORY_TABLE =
        "CREATE TABLE IF NOT EXISTS category (" +
        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
        "name TEXT NOT NULL, " +
        "icon INTEGER NOT NULL" +
        ")";

    /** DDL statement that creates the {@code expense} table if absent. */
    private static final String CREATE_EXPENSE_TABLE =
        "CREATE TABLE IF NOT EXISTS expense (" +
        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
        "amount REAL NOT NULL, " +
        "timestamp TEXT NOT NULL, " +
        "category_id INTEGER NOT NULL, " +
        "cycle_id INTEGER NOT NULL, " +
        "FOREIGN KEY (category_id) REFERENCES category(id), " +
        "FOREIGN KEY (cycle_id) REFERENCES budget_cycle(id)" +
        ")";

    /**
     * Private constructor — opens the SQLite connection and creates tables.
     * Use {@link #getInstance()} to obtain the shared instance.
     */
    private DatabaseHelper() {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:masroofy.db");
            initializeTables();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates all required tables if they do not already exist.
     * Called once during construction.
     */
    private void initializeTables() {
        try {
            Statement stmt = connection.createStatement();
            stmt.execute(CREATE_CATEGORY_TABLE);
            stmt.execute(CREATE_BUDGET_CYCLE_TABLE);
            stmt.execute(CREATE_EXPENSE_TABLE);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the singleton instance, creating it on first call.
     *
     * @return the shared {@link DatabaseHelper} instance
     */
    public static DatabaseHelper getInstance() {
        if (instance == null) {
            instance = new DatabaseHelper();
        }
        return instance;
    }

    // -------------------------------------------------------------------------
    // Budget Cycle operations
    // -------------------------------------------------------------------------

    /**
     * Inserts a new budget cycle record into the database.
     *
     * @param budgetCycle the cycle to persist (ID field is ignored)
     * @return the generated database ID, or -1 on failure
     */
    public long insertCycle(BudgetCycle budgetCycle) {
        String INSERT_CYCLE =
            "INSERT INTO budget_cycle(allowance, start_date, end_date, status) VALUES(?, ?, ?, ?)";
        try {
            PreparedStatement statement =
                connection.prepareStatement(INSERT_CYCLE, Statement.RETURN_GENERATED_KEYS);
            statement.setDouble(1, budgetCycle.getTotalAllowance());
            statement.setString(2, budgetCycle.getStartDate().toString());
            statement.setString(3, budgetCycle.getEndDate().toString());
            statement.setString(4, budgetCycle.getStatus().toString());
            statement.executeUpdate();
            ResultSet keys = statement.getGeneratedKeys();
            if (keys.next()) return keys.getLong(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * Retrieves the first budget cycle found in the database.
     *
     * @return a populated {@link BudgetCycle}, or {@code null} if none exists
     */
    public BudgetCycle getBudgetCycle() {
        String GET_CYCLE = "SELECT * FROM budget_cycle";
        try {
            PreparedStatement statement = connection.prepareStatement(GET_CYCLE);
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                BudgetCycle currentCycle = new BudgetCycle(
                    result.getDouble("allowance"),
                    LocalDate.parse(result.getString("start_date")),
                    LocalDate.parse(result.getString("end_date"))
                );
                currentCycle.setId(result.getInt("id"));
                currentCycle.setStatus(CycleStatus.valueOf(result.getString("status")));
                return currentCycle;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Updates the status column of a budget cycle row.
     *
     * @param id     database ID of the cycle to update
     * @param status new {@link CycleStatus} value
     */
    public void updateCycleStatus(int id, CycleStatus status) {
        String UPDATE_STATUS = "UPDATE budget_cycle SET status = ? WHERE id = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(UPDATE_STATUS);
            statement.setString(1, status.name());
            statement.setInt(2, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Permanently deletes a budget cycle and its row from the database.
     *
     * @param id database ID of the cycle to delete
     */
    public void deleteCycle(int id) {
        String DELETE_CYCLE = "DELETE FROM budget_cycle WHERE id = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(DELETE_CYCLE);
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // -------------------------------------------------------------------------
    // Expense operations
    // -------------------------------------------------------------------------

    /**
     * Inserts a new expense record into the database.
     *
     * @param expense the expense to persist (ID field is ignored)
     * @return the generated database ID, or -1 on failure
     */
    public long insertExpense(Expense expense) {
        String INSERT_EXPENSE =
            "INSERT INTO expense(amount, timestamp, category_id, cycle_id) VALUES(?, ?, ?, ?)";
        try {
            PreparedStatement statement =
                connection.prepareStatement(INSERT_EXPENSE, Statement.RETURN_GENERATED_KEYS);
            statement.setDouble(1, expense.getAmount());
            statement.setString(2, expense.getTimestamp().toString());
            statement.setInt(3, expense.getCategoryId());
            statement.setInt(4, expense.getCycleId());
            statement.executeUpdate();
            ResultSet keys = statement.getGeneratedKeys();
            if (keys.next()) return keys.getLong(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * Updates the amount and category of an existing expense record.
     *
     * @param expense expense object carrying the new values; its {@code id}
     *                must match an existing database row
     */
    public void updateExpense(Expense expense) {
        String UPDATE_EXPENSE = "UPDATE expense SET amount = ?, category_id = ? WHERE id = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(UPDATE_EXPENSE);
            statement.setDouble(1, expense.getAmount());
            statement.setInt(2, expense.getCategoryId());
            statement.setInt(3, expense.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Permanently deletes an expense record from the database.
     *
     * @param id database ID of the expense to delete
     */
    public void deleteExpense(int id) {
        String DELETE_EXPENSE = "DELETE FROM expense WHERE id = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(DELETE_EXPENSE);
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns all expenses belonging to a specific budget cycle.
     *
     * @param cycleId the cycle to query
     * @return list of {@link Expense} objects; empty list if none found
     */
    public List<Expense> getAllExpenses(int cycleId) {
        List<Expense> allExpenses = new ArrayList<>();
        String GET_ALL_EXPENSES = "SELECT * FROM expense WHERE cycle_id = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(GET_ALL_EXPENSES);
            statement.setInt(1, cycleId);
            ResultSet results = statement.executeQuery();
            while (results.next()) {
                Expense temp = new Expense(
                    results.getDouble("amount"),
                    results.getInt("category_id"),
                    results.getInt("cycle_id")
                );
                temp.setId(results.getInt("id"));
                allExpenses.add(temp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return allExpenses;
    }

    /**
     * Returns all expenses for a specific category within a budget cycle.
     *
     * @param cycleId    the cycle to query
     * @param categoryId the category to filter by
     * @return filtered list of {@link Expense} objects
     */
    public List<Expense> getExpensesByCategory(int cycleId, int categoryId) {
        List<Expense> allExpenses = new ArrayList<>();
        String GET_EXPENSES_BY_CATEGORY =
            "SELECT * FROM expense WHERE cycle_id = ? AND category_id = ?";
        try {
            PreparedStatement statement =
                connection.prepareStatement(GET_EXPENSES_BY_CATEGORY);
            statement.setInt(1, cycleId);
            statement.setInt(2, categoryId);
            ResultSet results = statement.executeQuery();
            while (results.next()) {
                Expense temp = new Expense(
                    results.getDouble("amount"),
                    results.getInt("category_id"),
                    results.getInt("cycle_id")
                );
                temp.setId(results.getInt("id"));
                allExpenses.add(temp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return allExpenses;
    }

    /**
     * Returns the sum of all expense amounts for today within a cycle.
     * Matches rows whose {@code timestamp} starts with today's date string.
     *
     * @param cycleId the cycle to query
     * @return total amount spent today in EGP, or 0 if none
     */
    public double getTodaySpent(int cycleId) {
        String sql =
            "SELECT SUM(amount) FROM expense WHERE cycle_id = ? AND timestamp LIKE ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, cycleId);
            ps.setString(2, java.time.LocalDate.now().toString() + "%");
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getDouble(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Returns the cumulative total of all expenses in a cycle.
     *
     * @param cycleId the cycle to query
     * @return total spent in EGP, or -1 on query failure
     */
    public double getTotalSpent(int cycleId) {
        String GET_TOTAL_SPENT = "SELECT SUM(amount) FROM expense WHERE cycle_id = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(GET_TOTAL_SPENT);
            statement.setInt(1, cycleId);
            ResultSet results = statement.executeQuery();
            if (results.next()) return results.getDouble(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    // -------------------------------------------------------------------------
    // Category operations
    // -------------------------------------------------------------------------

    /**
     * Inserts a new category record into the database.
     *
     * @param category the category to persist (ID field is ignored)
     * @return the generated database ID, or -1 on failure
     */
    public long insertCategory(Category category) {
        String INSERT_CATEGORY = "INSERT INTO category(name, icon) VALUES(?, ?)";
        try {
            PreparedStatement statement =
                connection.prepareStatement(INSERT_CATEGORY, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, category.getName());
            statement.setInt(2, category.getIconResId());
            statement.executeUpdate();
            ResultSet keys = statement.getGeneratedKeys();
            if (keys.next()) return keys.getLong(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * Returns all categories stored in the database.
     *
     * @return list of all {@link Category} objects; empty list if none found
     */
    public List<Category> getAllCategories() {
        List<Category> allCategories = new ArrayList<>();
        String GET_ALL_CATEGORIES = "SELECT * FROM category";
        try {
            PreparedStatement statement = connection.prepareStatement(GET_ALL_CATEGORIES);
            ResultSet results = statement.executeQuery();
            while (results.next()) {
                Category temp = new Category(
                    results.getInt("id"),
                    results.getString("name"),
                    results.getInt("icon")
                );
                allCategories.add(temp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return allCategories;
    }

    /**
     * Permanently deletes a category record from the database.
     *
     * @param id database ID of the category to delete
     */
    public void deleteCategory(int id) {
        String DELETE_CATEGORY = "DELETE FROM category WHERE id = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(DELETE_CATEGORY);
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // -------------------------------------------------------------------------
    // PIN / Auth operations
    // -------------------------------------------------------------------------

    /**
     * Persists a hashed PIN, replacing any previously stored value.
     * Creates the {@code pin} table automatically if it does not exist.
     *
     * @param hashedPin SHA-256 hex digest of the user's PIN
     */
    public void savePin(String hashedPin) {
        try {
            Statement stmt = connection.createStatement();
            stmt.execute("CREATE TABLE IF NOT EXISTS pin (hashed_pin TEXT NOT NULL)");
            connection.createStatement().execute("DELETE FROM pin");
            PreparedStatement ps =
                connection.prepareStatement("INSERT INTO pin VALUES (?)");
            ps.setString(1, hashedPin);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieves the stored hashed PIN from the database.
     * Creates the {@code pin} table automatically if it does not exist.
     *
     * @return the stored SHA-256 hex digest, or {@code null} if no PIN is set
     */
    public String getPin() {
        try {
            Statement stmt = connection.createStatement();
            stmt.execute("CREATE TABLE IF NOT EXISTS pin (hashed_pin TEXT NOT NULL)");
            ResultSet rs =
                connection.createStatement().executeQuery("SELECT hashed_pin FROM pin");
            if (rs.next()) return rs.getString(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
