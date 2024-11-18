package lt.ehu.student.moneytrackerbeta.model;

import java.util.Arrays;

public enum TransactionType {
    INCOME(1, "INCOME"),
    EXPENSE(2, "EXPENSE"),
    TRANSFER(3, "TRANSFER"),
    ACCOUNT(4, "ACCOUNT");

    private final int id;
    private final String name;

    TransactionType(int id, String name) {
        this.id = id;
        this.name = name;
    }
    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public static TransactionType[] getDisplayableTypes() {
        return Arrays.stream(values())
                .filter(type -> type != ACCOUNT)
                .toArray(TransactionType[]::new);
    }
}
