package lt.ehu.student.moneytrackerbeta.model;

public class Currency extends AbstractModel {
    private final int id;
    private final String longName;
    private final String code;
    private final String symbol;
    private final String description;

    public Currency(int id, String longName, String code, String symbol, String description) {
        this.id = id;
        this.longName = longName;
        this.code = code;
        this.symbol = symbol;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public String getLongName() {
        return longName;
    }

    public String getCode() {
        return code;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getDescription() {
        return description;
    }
}
