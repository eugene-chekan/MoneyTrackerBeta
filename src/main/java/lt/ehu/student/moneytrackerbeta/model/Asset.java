package lt.ehu.student.moneytrackerbeta.model;

import java.math.BigDecimal;
import java.util.UUID;

public class Asset extends AbstractModel {
    private final UUID id;
    private int userId;
    private int typeId;
    private String name;
    private BigDecimal balance;
    private int currencyId;
    private String description;

    public Asset(UUID id, int userId, int typeId, String name, BigDecimal balance, int currencyId, String description) {
        this.id = id;
        this.userId = userId;
        this.typeId = typeId;
        this.name = name;
        this.balance = balance;
        this.currencyId = currencyId;
        this.description = description;
    }
    public Asset() {
        this.id = UUID.randomUUID();
        this.balance  = new BigDecimal("0.00");
    }

    public UUID getId() {
        return id;
    }
    public int getUserId() {
        return userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public BigDecimal getBalance() {
        return balance;
    }
    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
    public int getCurrencyId() {
        return currencyId;
    }
    public void setCurrencyId(int currencyId) {
        this.currencyId = currencyId;
    }
    public int getTypeId() {
        return typeId;
    }
    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }
    public boolean isAccount() {
        return typeId == TransactionType.ACCOUNT.getId();
    }
    public boolean isIncome() {
        return typeId == TransactionType.INCOME.getId();
    }
    public boolean isExpense() {
        return typeId == TransactionType.EXPENSE.getId();
    }
}
