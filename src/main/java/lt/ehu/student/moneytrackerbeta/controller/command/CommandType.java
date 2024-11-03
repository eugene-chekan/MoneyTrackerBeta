package lt.ehu.student.moneytrackerbeta.controller.command;

import lt.ehu.student.moneytrackerbeta.controller.command.impl.*;

public enum CommandType {
    DASHBOARD(new DashboardCommand()),
    LOGIN(new LoginCommand()),
    LOGOUT(new LogoutCommand()),
    NEW_TRANSACTION(new NewTransactionCommand()),
    SIGNUP(new SignUpCommand()),
    SUBMIT_TRANSACTION(new SubmitTransactionCommand()),

    DEFAULT(new DefaultCommand());
    final Command command;

    CommandType(Command command) {
        this.command = command;
    }

    public static Command chooseCommand(String command) {
        return CommandType.valueOf(command.toUpperCase()).command;
    }
}
