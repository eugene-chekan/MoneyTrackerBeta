package lt.ehu.student.moneytrackerbeta.model;

public enum CurrencyEnum {
    USD(1, "USD", "$", "US Dollar"),
    EUR(2, "EUR", "€", "Euro"),
    PLN(3, "PLN", "zł", "Polish złoty");

    private final int id;
    private final String code;
    private final String symbol;
    private final String name;

    CurrencyEnum(int id, String code, String symbol, String name) {
        this.id = id;
        this.code = code;
        this.symbol = symbol;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getName() {
        return name;
    }

    public static CurrencyEnum getById(int id) {
        for (CurrencyEnum currency : values()) {
            if (currency.getId() == id) {
                return currency;
            }
        }
        return USD; // Default fallback
    }

}
