package phrase.towerManaEvent.command;

public class CommandResult {

    public enum ResultStatus {

        SUCCESS,
        UNKNOWN_COMMAND,
        INCORRECT_ARGUMENTS,
        NO_PERMISSION,
        ERROR

    }

    private final String message;
    private final ResultStatus resultType;

    public CommandResult(String message, ResultStatus resultType) {
        this.message = message;
        this.resultType = resultType;
    }

    public String getMessage() {
        return message;
    }

    public ResultStatus getResultType() {
        return resultType;
    }

}
