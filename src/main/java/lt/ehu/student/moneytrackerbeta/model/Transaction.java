package lt.ehu.student.moneytrackerbeta.model;

import java.math.BigDecimal;

public class Transaction extends AbstractModel {
    private String id;
    private int userId;
    private int type;
    private int source;
    private int destination;
    private String label;
    private int date; // this is the creation timestamp
    private BigDecimal amount;
    private String comment;
    private int currency;

    public Transaction(String id, int userId, int type, int source, int destination, String label, int date, BigDecimal amount, String comment, int currency) {
        this.id = id;
        this.userId = userId;
        this.type = type;
        this.source = source;
        this.destination = destination;
        this.label = label;
        this.date = date;
        this.amount = amount;
        this.comment = comment;
        this.currency = currency;
    }

    public Transaction() {
        this.userId = 0;
        this.type = 0;
        this.source = 0;
        this.destination = 0;
        this.label = null;
        this.date = 0;
        this.amount = null;
        this.comment = null;
        this.currency = 0;
    }
    public String getId() {
        return id;
    }
    public int getUserId() {
        return userId;
    }
    public int setUserId(int userId) {
        return this.userId = userId;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setSource(int source) {
        this.source = source;
    }

    public void setDestination(int destination) {
        this.destination = destination;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setCurrency(int currency) {
        this.currency = currency;
    }

    public int getType() {
        return type;
    }

    public int getSource() {
        return source;
    }

    public int getDestination() {
        return destination;
    }

    public String getLabel() {
        return label;
    }

    public int getDate() {
        return date;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getComment() {
        return comment;
    }

    public int getCurrency() {
        return currency;
    }
}
