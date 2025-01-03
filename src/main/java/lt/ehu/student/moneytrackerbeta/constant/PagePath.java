package lt.ehu.student.moneytrackerbeta.constant;

public final class PagePath {
    // Base paths
    public static final String BASE_PAGE_PATH = "/pages/";
    public static final String BASE_ERROR_PATH = "/error/";
    
    // Main pages
    public static final String LOGIN = "/index.jsp";
    public static final String SIGNUP = BASE_PAGE_PATH + "signup.jsp";
    public static final String SIGNUP_SUCCESS = BASE_PAGE_PATH + "signup_success.jsp";
    public static final String DASHBOARD = BASE_PAGE_PATH + "dashboard.jsp";
    
    // Transaction pages
    public static final String NEW_TRANSACTION = BASE_PAGE_PATH + "new_transaction.jsp";
    public static final String VIEW_TRANSACTIONS = BASE_PAGE_PATH + "view_transactions.jsp";
    
    // Error pages
    public static final String ERROR_404 = BASE_ERROR_PATH + "404.jsp";
    public static final String ERROR_500 = BASE_ERROR_PATH + "500.jsp";

    private PagePath() {
        // Prevent instantiation
    }
} 