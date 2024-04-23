import java.util.Scanner;

public class App {
    private static Booking booking = new Booking();
    private static FileIO file = new FileIO();
    private static Scanner sc = new Scanner(System.in);
    private static final String BORDER =
            "=================================================================";
    private static final String SPACE = "|            \t\t\t\t\t\t\t|";

    private static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    /**
     * The primaryMenu function displays a menu for a hotel management system and prompts the user
     * to enter a choice, validating the input and returning the choice.
     * 
     * @return The method is returning an integer value, which represents the user's choice from the
     *         primary menu.
     */
    private static int primaryMenu() {
        clearScreen();
        String input;
        boolean isValid = true;
        int choice = 0;
        do {
            try {
                System.out.println(
                        "===================================================================================================");
                System.out.println(
                        "|                                                                                                 |");
                System.out.println(
                        "|                            WELCOME TO DWAYNE'S HOTEL MANAGEMENT SYSTEM                          |");
                System.out.println(
                        "|                                                                                                 |");
                System.out.println(
                        "===================================================================================================");
                System.out.println(
                        "| What services would you like to use?                                                            |");
                System.out.println(
                        "| - [1]. Make a reservation                                                                       |");
                System.out.println(
                        "| - [2]. Cancel a reservation                                                                     |");
                System.out.println(
                        "| - [3]. Search                                                                                   |");
                System.out.println(
                        "| - [4]. Exit                                                                                     |");
                System.out.println(
                        "===================================================================================================\n");

                System.out.print("| Enter choice\t:\t ");
                input = sc.nextLine();

                if (input.isEmpty())
                    throw new EmptyInputException(DataExceptions.errorMessages[0]);

                choice = Integer.parseInt(input);
                isValid = choice >= 1 && choice <= 4;

                if (!isValid)
                    throw new InvalidChoiceException(DataExceptions.errorMessages[2]);
            } catch (NumberFormatException e) {
                System.out.println(DataExceptions.errorMessages[1]);
            } catch (EmptyInputException | InvalidChoiceException e) {
                System.out.println(e.getMessage());
            }
        } while (!isValid);
        System.out.println();
        return choice;
    }

    /**
     * The function `secondaryMenu()` displays a menu and prompts the user to enter a choice,
     * validating the input and returning the choice.
     * 
     * @return The method is returning an integer value, which is the user's choice from the
     *         secondary menu.
     */
    private static int secondaryMenu() {
        clearScreen();
        String input;
        boolean isValid = true;
        int secondaryChoice = 0;
        do {
            try {
                System.out.println(BORDER);
                System.out.println(SPACE);
                System.out.println("| SEARCH MENU\t\t\t\t\t\t\t|");
                System.out.println(SPACE);
                System.out.println(BORDER);
                System.out.println(
                        "| - [1]. Display all transaction records                        |");
                System.out.println(
                        "| - [2]. Display all room info                                  |");
                System.out.println(
                        "| - [3]. Filter room info                                       |");
                System.out.println(
                        "| - [4]. Back to main menu                                      |");
                System.out.println(BORDER);
                System.out.println();

                System.out.print("| Enter choice\t:\t ");
                input = sc.nextLine();

                if (input.isEmpty())
                    throw new EmptyInputException(DataExceptions.errorMessages[0]);

                secondaryChoice = Integer.parseInt(input);

                isValid = secondaryChoice >= 1 && secondaryChoice <= 4;

                if (!isValid)
                    throw new InvalidChoiceException(DataExceptions.errorMessages[2]);
            } catch (NumberFormatException e) {
                System.out.println(DataExceptions.errorMessages[1]);
            } catch (EmptyInputException | InvalidChoiceException e) {
                System.out.println(e.getMessage());
            }
        } while (!isValid);
        System.out.println();
        return secondaryChoice;
    }

    /**
     * The tertiaryMenu function displays a menu and prompts the user to enter a choice, validating
     * the input and returning the choice.
     * 
     * @return The method is returning an integer value, which is the user's choice from the
     *         tertiary menu.
     */
    private static int tertiaryMenu() {
        clearScreen();
        String input;
        boolean isValid = true;
        int tertiaryChoice = 0;
        do {
            try {
                System.out.println(BORDER);
                System.out.println(SPACE);
                System.out.println("| FILTER MENU\t\t\t\t\t\t\t|");
                System.out.println(SPACE);
                System.out.println(BORDER);
                System.out.println(
                        "| - [1]. Filter by Status - AVAILABLE                           |");
                System.out.println(
                        "| - [2]. Filter by Status - NOT AVAILABLE                       |");
                System.out.println(
                        "| - [3]. Filter by Tier   - DELUXE                              |");
                System.out.println(
                        "| - [4]. Filter by Tier   - EXECUTIVE                           |");
                System.out.println(
                        "| - [5]. Filter by Tier   - PRESIDENTIAL                        |");
                System.out.println(
                        "| - [6]. Filter by Price  - ASCENDING                           |");
                System.out.println(
                        "| - [7]. Filter by Price  - DESCENDING                          |");
                System.out.println(
                        "| - [8]. Back to search menu                                    |");
                System.out.println(BORDER);
                System.out.println();

                System.out.print("| Enter choice\t:\t ");
                input = sc.nextLine();

                if (input.isEmpty())
                    throw new EmptyInputException(DataExceptions.errorMessages[0]);

                tertiaryChoice = Integer.parseInt(input);

                isValid = tertiaryChoice >= 1 && tertiaryChoice <= 9;

                if (!isValid)
                    throw new InvalidChoiceException(DataExceptions.errorMessages[2]);
            } catch (NumberFormatException e) {
                System.out.println(DataExceptions.errorMessages[1]);
                isValid = false;
            } catch (EmptyInputException | InvalidChoiceException e) {
                System.out.println(e.getMessage());
                isValid = false;
            }
        } while (!isValid);
        System.out.println();
        return tertiaryChoice;
    }

    public static void main(String[] args) {
        boolean isRunning = true;
        file.createFile();
        while (isRunning) {

            int primaryChoice = primaryMenu();
            int secondaryChoice = 0;
            int tertiaryChoice = 0;

            switch (primaryChoice) {
                case 1 -> booking.makeReservation();

                case 2 -> booking.cancelReservation();

                case 3 -> {
                    boolean secondaryMenuIsRunning = true;
                    while (secondaryMenuIsRunning) {
                        secondaryChoice = secondaryMenu();
                        switch (secondaryChoice) {
                            case 1 -> booking.displayAllCustomers();
                            case 2 -> booking.displayAllRooms();
                            case 3 -> {
                                boolean tertiaryMenuIsRunning = true;

                                while (tertiaryMenuIsRunning) {
                                    tertiaryChoice = tertiaryMenu();
                                    switch (tertiaryChoice) {
                                        case 1 -> booking.filterByStatus("AVAILABLE");
                                        case 2 -> booking.filterByStatus("NOT AVAILABLE");
                                        case 3 -> booking.filterByTier("DELUXE");
                                        case 4 -> booking.filterByTier("EXECUTIVE");
                                        case 5 -> booking.filterByTier("PRESIDENTIAL");
                                        case 6 -> booking.filterByPrice(false);
                                        case 7 -> booking.filterByPrice(true);
                                        case 8 -> tertiaryMenuIsRunning = false;
                                    }
                                }
                            }
                            case 4 -> secondaryMenuIsRunning = false;
                        }
                    }
                }
                case 4 -> {
                    System.out.println(BORDER);
                    System.out.println(SPACE);
                    System.out.println("| \t\t Thank you for using our services!\t\t|");
                    System.out.println(SPACE);
                    System.out.println(BORDER);
                    isRunning = false;
                }
            }
        }
    }

}
