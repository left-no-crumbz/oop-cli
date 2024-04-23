public class DataExceptions {
    protected static final String[] errorMessages = {
            "\n| ERROR: Input should not be empty. Please try again. |\n", // 0
            "\n| ERROR: Input should be an `integer`. Please try again. |\n", // 1
            "\n| ERROR: Input should be an `integer` between the given ranges. Please try again. |\n", // 2
            "\n| ERROR: Room number should be between `1-30`. Please try again. |\n", // 3
            "\n| ERROR: Invalid date format. Format is `MM-dd-yyyy`. Please try again. |\n", // 4
            "\n| ERROR: Input should only be between `Y` or `N`. Please try again. |\n", // 5
            "\n| ERROR: The date you entered is in the past. Please try again. |\n", // 6
            "\n| ERROR: Length of stay cannot be less than `0`. Please try again. |\n", // 7
            "\n| ERROR: Customer ID does not exist! Please try again. |\n", // 8
            "\n| ERROR: Input should only consist of letters. Please try again. |\n", // 9
            "\n| ERROR: IOException caught. |\n" // 10
    };
}

class DateIsInThePastException extends Exception {
    public DateIsInThePastException(String message) {
        super(message);
    }
}

class InvalidLengthOfStayException extends Exception {
    public InvalidLengthOfStayException(String message) {
        super(message);
    }
}

class InvalidChoiceException extends Exception {
    public InvalidChoiceException(String message) {
        super(message);
    }
}

class EmptyInputException extends Exception {
    public EmptyInputException(String message) {
        super(message);

    }
}

class InvalidInputLengthException extends Exception {
    public InvalidInputLengthException(String message) {
        super(message);
    }
}