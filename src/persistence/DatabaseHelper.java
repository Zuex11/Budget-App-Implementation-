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

public class DatabaseHelper 
{
    private static DatabaseHelper instance = null;

    private Connection connection;

    private void initializeTables()
    {
        try 
        {
            Statement stmt = connection.createStatement();
            stmt.execute(CREATE_CATEGORY_TABLE);
            stmt.execute(CREATE_BUDGET_CYCLE_TABLE);
            stmt.execute(CREATE_EXPENSE_TABLE);
        } 
        catch (SQLException e) 
        {
            e.printStackTrace();
        }
    }

    private DatabaseHelper()
    {
        try 
        {
            connection = DriverManager.getConnection("jdbc:sqlite:masroofy.db");
            initializeTables();
        } 
        catch (SQLException e) 
        {
            e.printStackTrace();
        }
    }

    public static DatabaseHelper getInstance()
    {
        /*this is called the singletong method, it enforces using only one database across the system 
        by making the constructor inaccessable anywhere else outside the class, meaning that other 
        devs working on the projects wont accidentally make a new connection to the database creating 
        conflicts and issues in the work flow, this works as follows : */
        if(instance == null) //check if the current instance is null, if so construct a new connection.
        {
            instance = new DatabaseHelper();
        }

        //then it returns the instance, incase it wasnt null it will skip the check and return a unified instance.
        return instance;
    }

    private static final String CREATE_BUDGET_CYCLE_TABLE = 
    "CREATE TABLE IF NOT EXISTS budget_cycle"+" ("+
    "id INTEGER PRIMARY KEY AUTOINCREMENT, "+
    "allowance REAL NOT NULL, "+
    "start_date TEXT NOT NULL, "+
    "end_date TEXT NOT NULL, "+
    "status TEXT NOT NULL"
    +")";

    private static final String CREATE_CATEGORY_TABLE = 
    "CREATE TABLE IF NOT EXISTS category (" +
    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
    "name TEXT NOT NULL, " +
    "icon INTEGER NOT NULL" +
    ")";

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

    public long insertCycle(BudgetCycle budgetCycle)
    {
        String INSERT_CYCLE = "INSERT INTO budget_cycle(allowance, start_date, end_date, status) Values(?, ?, ?, ?)";
        try
        {
            PreparedStatement statement = connection.prepareStatement(INSERT_CYCLE,Statement.RETURN_GENERATED_KEYS);
            
            statement.setDouble(1, budgetCycle.getTotalAllowance());
            statement.setString(2, budgetCycle.getStartDate().toString());
            statement.setString(3, budgetCycle.getEndDate().toString());
            statement.setString(4, budgetCycle.getStatus().toString());
            statement.executeUpdate();
            ResultSet keys = statement.getGeneratedKeys();
            if (keys.next()) return keys.getLong(1);
        }

        catch(SQLException e)
        {
            e.printStackTrace();
        }
        
        return -1;
    }

    public BudgetCycle getBudgetCycle()
    {
        String GET_CYCLE = "SELECT * FROM budget_cycle";
        try
        {
            PreparedStatement statement = connection.prepareStatement(GET_CYCLE);
            ResultSet result = statement.executeQuery();

            if(result.next())
            {
                BudgetCycle currentCycle = new BudgetCycle
                (
                    result.getDouble("allowance"),
                    LocalDate.parse(result.getString("start_date")),
                    LocalDate.parse(result.getString("end_date"))
                );

                currentCycle.setId(result.getInt("id"));
                currentCycle.setStatus(CycleStatus.valueOf(result.getString("status")));
                return currentCycle;
            }
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public void updateCycleStatus(int id, CycleStatus status)
    {
        String UPDATE_STATUS = "UPDATE budget_cycle SET status = ? WHERE id = ?" ;
        try
        {
            PreparedStatement statement = connection.prepareStatement(UPDATE_STATUS);

            statement.setString(1,status.name());
            statement.setInt(2, id);

            statement.executeUpdate();
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
    }

    public void deleteCycle(int id)
    {
        String DELETE_CYCLE = "DELETE FROM budget_cycle WHERE id = ?" ;

        try
        {
            PreparedStatement statement = connection.prepareStatement(DELETE_CYCLE);

            statement.setInt(1, id);

            statement.executeUpdate();
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
    }

    public long insertExpense(Expense expense)
    {
        String INSERT_EXPENSE = "INSERT INTO expense(amount, timestamp, category_id, cycle_id) values(?, ?, ?, ?)" ;

        try
        {
            PreparedStatement statement = connection.prepareStatement(INSERT_EXPENSE, Statement.RETURN_GENERATED_KEYS);

            statement.setDouble(1, expense.getAmount());
            statement.setString(2, expense.getTimestamp().toString());
            statement.setInt(3, expense.getCategoryId());
            statement.setInt(4, expense.getCycleId());

            statement.executeUpdate();
            ResultSet keys = statement.getGeneratedKeys();

            if(keys.next())
            {
                return keys.getLong(1) ;
            }

        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
        return -1;
    }

    public void updateExpense(Expense expense)
    {
        String UPDATE_EXPENSE = "UPDATE expense SET amount = ?, category_id = ? WHERE id = ?";

        try
        {
            PreparedStatement statement = connection.prepareStatement(UPDATE_EXPENSE);

            statement.setDouble(1, expense.getAmount());
            statement.setInt(2, expense.getCategoryId());
            statement.setInt(3, expense.getId());

            statement.executeUpdate();
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
    }

    public void deleteExpense(int id)
    {
        String DELETE_EXPENSE = "DELETE FROM expense WHERE id = ?";

        try
        {
            PreparedStatement statement = connection.prepareStatement(DELETE_EXPENSE);

            statement.setInt(1, id);
            statement.executeUpdate();
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
    }

    public List<Expense> getAllExpenses(int cycleId)
    {
        List<Expense> allExpenses = new ArrayList<>() ;

        String GET_ALL_EXPENSES = "SELECT * FROM expense WHERE cycle_id = ?";

        try
        {
            PreparedStatement statement = connection.prepareStatement(GET_ALL_EXPENSES);

            statement.setInt(1, cycleId);

            ResultSet results = statement.executeQuery();

            while(results.next())
            {
                Expense temp = new Expense(results.getDouble("amount"),results.getInt("category_id"),results.getInt("cycle_id"));
                temp.setId(results.getInt("id"));
                allExpenses.add(temp);
            }

            return allExpenses;
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }

        return allExpenses;
    }

    public List<Expense> getExpensesByCategory(int cycleId, int categoryId)
    {
        List<Expense> allExpenses = new ArrayList<>();

        String GET_EXPENSES_BY_CATEGORY = "SELECT * FROM expense WHERE cycle_id = ? AND category_id = ?";

        try
        {
            PreparedStatement statement = connection.prepareStatement(GET_EXPENSES_BY_CATEGORY);

            statement.setInt(1, cycleId);
            statement.setInt(2, categoryId);

            ResultSet results = statement.executeQuery();

            while (results.next()) 
            {
                Expense temp = new Expense(results.getDouble("amount"),results.getInt("category_id"),results.getInt("cycle_id"));
                temp.setId(results.getInt("id"));
                allExpenses.add(temp);
            }
        }

        catch(SQLException e)
        {
            e.printStackTrace();
        }

        return allExpenses;
    }

    public long insertCategory(Category category)
    {
        String INSERT_CATEGORY = "INSERT INTO category(name, icon) VALUES(?, ?)";

        try
        {
            PreparedStatement statement = connection.prepareStatement(INSERT_CATEGORY, Statement.RETURN_GENERATED_KEYS);

            statement.setString(1, category.getName());
            statement.setInt(2, category.getIconResId());

            statement.executeUpdate();
            ResultSet keys = statement.getGeneratedKeys();

            if(keys.next())
            {
                return keys.getLong(1);
            }
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }

        return -1;
    }

    public List<Category> getAllCategories()
    {
        List<Category> allCategories = new ArrayList<>();

        String GET_ALL_CATEGORIES = "SELECT * FROM category";

        try
        {
            PreparedStatement statement = connection.prepareStatement(GET_ALL_CATEGORIES);

            ResultSet results = statement.executeQuery();

            while(results.next())
            {
                Category temp = new Category(results.getInt("id"), results.getString("name"), results.getInt("icon"));
                allCategories.add(temp);
            }
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }

        return allCategories;
    }
    
    public double getTotalSpent(int cycleId)
    {
        String GET_TOTAL_SPENT = "SELECT SUM(amount) FROM expense WHERE cycle_id = ?" ;

        try
        {
            PreparedStatement statement = connection.prepareStatement(GET_TOTAL_SPENT);

            statement.setInt(1, cycleId);

            ResultSet results = statement.executeQuery();

            if(results.next())
            {
                return results.getDouble(1);
            }
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }

        return -1;
    }

    public void savePin(String hashedPin) {
    try {
        Statement stmt = connection.createStatement();
        stmt.execute("CREATE TABLE IF NOT EXISTS pin (hashed_pin TEXT NOT NULL)");
        connection.createStatement().execute("DELETE FROM pin");
        PreparedStatement ps = connection.prepareStatement("INSERT INTO pin VALUES (?)");
        ps.setString(1, hashedPin);
        ps.executeUpdate();
    } catch (SQLException e) { e.printStackTrace(); }
   }

    public String getPin() {
    try {
        Statement stmt = connection.createStatement();
        stmt.execute("CREATE TABLE IF NOT EXISTS pin (hashed_pin TEXT NOT NULL)");
        ResultSet rs = connection.createStatement().executeQuery("SELECT hashed_pin FROM pin");
        if (rs.next()) return rs.getString(1);
    } 
    catch (SQLException e) { e.printStackTrace(); }
    return null;
    }
}