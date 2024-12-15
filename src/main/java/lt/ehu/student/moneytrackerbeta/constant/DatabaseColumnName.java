package lt.ehu.student.moneytrackerbeta.constant;

public final class DatabaseColumnName {
    // User table
    public static final String USER_DEFAULT_CURRENCY = "default_currency";
    public static final String USER_EMAIL = "email";
    public static final String USER_FIRST_NAME = "first_name";
    public static final String USER_ID = "id";
    public static final String USER_LAST_NAME = "last_name";
    public static final String USER_LOGIN = "login";
    public static final String USER_PASSWORD_HASH = "password_hash";
    public static final String USER_REGISTRATION_DATE = "registration_date";

    // Transaction table
    public static final String TRANSACTION_ID = "id";
    public static final String TRANSACTION_USER_ID = "user_id";
    public static final String TRANSACTION_TYPE = "type";
    public static final String TRANSACTION_SOURCE = "source";
    public static final String TRANSACTION_DESTINATION = "destination";
    public static final String TRANSACTION_AMOUNT = "amount";
    public static final String TRANSACTION_CURRENCY = "currency_id";
    public static final String TRANSACTION_TIMESTAMP = "timestamp";
    public static final String TRANSACTION_COMMENT = "comment";

    // Transaction type table
    public static final String TRANSACTION_TYPE_ID = "id";
    public static final String TRANSACTION_TYPE_DESCRIPTION = "description";
    public static final String TRANSACTION_TYPE_NAME = "name";

    // Asset table
    public static final String ASSET_ID = "id";
    public static final String ASSET_USER_ID = "user_id";
    public static final String ASSET_TYPE_ID = "type_id";
    public static final String ASSET_NAME = "name";
    public static final String ASSET_BALANCE = "balance";
    public static final String ASSET_CURRENCY_ID = "currency_id";
    public static final String ASSET_DESCRIPTION = "description";

    // Currency table
    public static final String CURRENCY_ID = "id";
    public static final String CURRENCY_ISO_CODE = "code";
    public static final String CURRENCY_NAME = "name";
    public static final String CURRENCY_SYMBOL = "symbol";

    private DatabaseColumnName() {
        // Prevent instantiation
    }
} 