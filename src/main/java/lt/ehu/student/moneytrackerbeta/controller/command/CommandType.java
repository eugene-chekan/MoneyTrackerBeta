package lt.ehu.student.moneytrackerbeta.controller.command;

import lt.ehu.student.moneytrackerbeta.controller.command.impl.*;

public enum CommandType {
    NEW_TRANSACTION(new NewTransactionCommand()),
    DASHBOARD(new DashboardCommand()),
    LOGIN(new LoginCommand()),
    LOGOUT(new LogoutCommand()),
    SIGNUP(new SignUpCommand()),

    DEFAULT(new DefaultCommand());
    final Command command;

    CommandType(Command command) {
        this.command = command;
    }

    public static Command chooseCommand(String command) {
        return CommandType.valueOf(command.toUpperCase()).command;
    }
}
