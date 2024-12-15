package lt.ehu.student.moneytrackerbeta.model;

public class TransactionType {
    // Constants for type names (matching database values)
    public static final String ACCOUNT = "Account";
    public static final String INCOME = "Income";
    public static final String EXPENSE = "Expense";
    public static final String TRANSFER = "Transfer";

    private int id;
    private String name;
    private String description;

    public TransactionType(int id, String description, String name) {
        this.id = id;
        this.description = description;
        this.name = name;
    }

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public boolean isAccount() {
        return ACCOUNT.equals(this.name);
    }
    public boolean isIncome() {
        return INCOME.equals(this.name);
    }
    public boolean isExpense() {
        return EXPENSE.equals(this.name);
    }
    public boolean isTransfer() {
        return TRANSFER.equals(this.name);
    }
}
