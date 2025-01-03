package lt.ehu.student.moneytrackerbeta.model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.UUID;

public class Transaction extends AbstractModel {
    private final UUID id;
    private int userId;
    private int typeId;
    private Timestamp timestamp; // this is the creation timestamp
    private UUID sourceId;  // uuid type
    private UUID destinationId; // uuid type
    private BigDecimal amount;
    private int currencyId;
    private String comment;

    public Transaction(UUID id, int userId, int typeId, Timestamp timestamp, UUID sourceId, UUID destinationId, BigDecimal amount, int currencyId, String comment) {
        this.id = id;
        this.userId = userId;
        this.typeId = typeId;
        this.timestamp = timestamp;
        this.sourceId = sourceId;
        this.destinationId = destinationId;
        this.amount = amount;
        this.currencyId = currencyId;
        this.comment = comment;
    }

    public Transaction() {
        this.id = UUID.randomUUID();
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

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public UUID getSourceId() {
        return sourceId;
    }

    public void setSourceId(UUID sourceId) {
        this.sourceId = sourceId;
    }

    public UUID getDestinationId() {
        return destinationId;
    }

    public void setDestinationId(UUID destinationId) {
        this.destinationId = destinationId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public int getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(int currencyId) {
        this.currencyId = currencyId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
