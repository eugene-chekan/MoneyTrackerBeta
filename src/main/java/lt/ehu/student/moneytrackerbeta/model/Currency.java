package lt.ehu.student.moneytrackerbeta.model;

public class Currency extends AbstractModel {
    private int id;
    private String code;
    private String symbol;
    private String name;

    public Currency(int id, String code, String symbol, String name) {
        this.id = id;
        this.code = code;
        this.symbol = symbol;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
