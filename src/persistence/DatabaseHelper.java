package persistence;

public class DatabaseHelper 
{
    private static DatabaseHelper instance = null;

    private DatabaseHelper(){}

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
    "icon TEXT NOT NULL" +
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
}