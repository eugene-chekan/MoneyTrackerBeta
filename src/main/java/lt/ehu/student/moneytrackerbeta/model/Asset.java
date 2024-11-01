package lt.ehu.student.moneytrackerbeta.model;

import java.math.BigDecimal;

public class Asset extends AbstractModel {
    private String id;
    private int userId;
    private String name;
    private String description;
    private BigDecimal initBalance;
    private BigDecimal currentBalance;
    private int currency;
    private int type;

    public Asset(String id, int userId, String name, String description, BigDecimal initBalance, BigDecimal currentBalance, int currency, int type) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.description = description;
        this.initBalance = initBalance;
        this.currentBalance = currentBalance;
        this.currency = currency;
        this.type = type;
    }
    public Asset() {
        this.userId = 0;
        this.name = null;
        this.description = null;
        this.initBalance = null;
        this.currentBalance = null;
        this.currency = 0;
        this.type = 0;
    }

    public Asset(int userId, String name, String description, BigDecimal initBalance, BigDecimal currentBalance, int currency, int type) {
        this.userId = userId;
        this.name = name;
        this.description = description;
        this.initBalance = initBalance;
        this.currentBalance = currentBalance;
        this.currency = currency;
        this.type = type;
    }

    public Asset(int userId, String name, BigDecimal initBalance) {
        this.userId = userId;
        this.name = name;
        this.description = null;
        this.initBalance = initBalance;
        this.currentBalance = initBalance;
        this.currency = 1;
        this.type = 3;
    }


    public String getId() {
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
    public BigDecimal getInitBalance() {
        return initBalance;
    }
    public void setInitBalance(BigDecimal initBalance) {
        this.initBalance = initBalance;
    }
    public BigDecimal getCurrentBalance() {
        return currentBalance;
    }
    public void setCurrentBalance(BigDecimal currentBalance) {
        this.currentBalance = currentBalance;
    }
    public int getCurrency() {
        return currency;
    }
    public void setCurrency(int currency) {
        this.currency = currency;
    }

    public int getType() {
        return type;
    }
    public void setType(int type) {
        this.type = type;
    }
    public boolean isAccount() {
        return type == 3;
    }
    public boolean isIncome() {
        return type == 1;
    }
    public boolean isExpense() {
        return type == 2;
    }
}
