package lt.ehu.student.moneytrackerbeta.model.dto;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.UUID;

public class TransactionDto {
    private UUID id;
    private int userId;
    private String transactionType;
    private Timestamp timestamp;
    private String sourceName;
    private String destinationName;
    private BigDecimal amount;
    private String currencySymbol;
    private String comment;

    public TransactionDto(UUID id, int userId, String transactionType, 
                         Timestamp timestamp, String sourceName, 
                         String destinationName, BigDecimal amount, 
                         String currencySymbol, String comment) {
        this.id = id;
        this.userId = userId;
        this.transactionType = transactionType;
        this.timestamp = timestamp;
        this.sourceName = sourceName;
        this.destinationName = destinationName;
        this.amount = amount;
        this.currencySymbol = currencySymbol;
        this.comment = comment;
    }

    // Getters and setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public String getTransactionType() { return transactionType; }
    public void setTransactionType(String transactionType) { this.transactionType = transactionType; }
    public Timestamp getTimestamp() { return timestamp; }
    public void setTimestamp(Timestamp timestamp) { this.timestamp = timestamp; }
    public String getSourceName() { return sourceName; }
    public void setSourceName(String sourceName) { this.sourceName = sourceName; }
    public String getDestinationName() { return destinationName; }
    public void setDestinationName(String destinationName) { this.destinationName = destinationName; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public String getCurrencySymbol() { return currencySymbol; }
    public void setCurrencySymbol(String currencySymbol) { this.currencySymbol = currencySymbol; }
    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
} 