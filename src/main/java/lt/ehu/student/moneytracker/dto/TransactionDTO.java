package lt.ehu.student.moneytracker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lt.ehu.student.moneytracker.model.Currency;
import lt.ehu.student.moneytracker.model.Transaction;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@Getter
@Setter
public class TransactionDTO {
    private UUID id;
    private BigDecimal amount;
    private LocalDateTime timestamp;
    private String comment;
    private UUID sourceId;
    private UUID destinationId;
    private UUID transferTransactionId;
    private String sourceName;
    private String destinationName;
    private Currency currency;

    public TransactionDTO(Transaction transaction, String sourceName, String destinationName) {
        this.id = transaction.getId();
        this.amount = transaction.getAmount();
        this.timestamp = transaction.getTimestamp();
        this.comment = transaction.getComment();
        this.sourceId = transaction.getSourceId();
        this.destinationId = transaction.getDestinationId();
        this.transferTransactionId = transaction.getTransferTransactionId();
        this.sourceName = sourceName;
        this.destinationName = destinationName;
        this.currency = transaction.getCurrency();
    }

    public boolean isTransfer() {
        return transferTransactionId != null;
    }

    public boolean isExpense() {
        return amount.compareTo(BigDecimal.ZERO) < 0;
    }

    public boolean isIncome() {
        return !isTransfer() && !isExpense();
    }
}
