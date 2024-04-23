import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Comparator;
import java.text.DecimalFormat;
import java.util.Map;

public class Booking {
    private static final Scanner sc = new Scanner(System.in);
    private static FileIO file = new FileIO();
    private static final DateTimeFormatter format = DateTimeFormatter.ofPattern("MM-dd-yyyy");
    private static final Room[] hotelRooms = populateRooms();
    private static Map<Long, CustomerRecord> transactionRecord =
            new LinkedHashMap<>(file.readFile());
    private static final String CUSTOMER_TITLE_FORMAT =
            "| %-3s | %-10s | %-10s | %-17s | %-11s | %-33s | %-14s | %-10s | %-10s | %-4s | %-9s |%n";
    private static final String ROOM_TITLE_FORMAT = "| %-11s | %-13s | %-12s | %-3s | %-10s |%n";
    private static final String REGEX =
            "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
    private static final DecimalFormat df = new DecimalFormat("#,##0.00");
    private static final String[] ROOM_TITLE_COLUMNS =
            {"Room Number", "Status", "Tier", "Pax", "Price"};
    private static final String[] CUSTOMER_TITLE_COLUMNS = {"ID", "First Name", "Last Name",
            "Address", "MOP", "Email", "Contact", "Check-in", "Check-out", "Room", "Status"};
    private static final String ROOM_TITLE_BORDER =
            "=================================================================";
    private static final String NOT_AVAILABLE = "NOT AVAILABLE";
    private static final String BOOKED = "BOOKED";
    private static final String SPACE = "|            \t\t\t\t\t\t\t|";
    private boolean isValid;
    private String input;

    // ---------------------------------------------------------- STATIC METHODS
    // ---------------------------------------------------------

    /**
     * The function "populateRooms" creates an array of Room objects, with different types of rooms
     * based on their index.
     * 
     * @return The method `populateRooms()` returns an array of `Room` objects.
     */
    private static Room[] populateRooms() {
        final Room[] hotelRooms = new Room[30];
        for (int i = 0; i < hotelRooms.length; i++) {
            if (i >= 0 && i < 10)
                hotelRooms[i] = new DeluxeRoom(i + 1);
            else if (i >= 10 && i < 20)
                hotelRooms[i] = new ExecutiveRoom(i + 1);
            else if (i >= 20 && i <= 29)
                hotelRooms[i] = new PresidentialRoom(i + 1);
        }
        return hotelRooms;
    }

    // ---------------------------------------------------------- PUBLIC METHODS
    // ---------------------------------------------------------

    /**
     * The function "makeReservation" handles the process of making a reservation by getting room
     * tier, selecting a room, checking in and out, getting user information, and displaying the
     * customer record.
     */
    public void makeReservation() {
        clearScreen();
        String roomTier = getRoomTier();
        Room room = getRoom(roomTier);
        String checkIn = checkIn(room);
        String checkOut = checkOut(checkIn);
        CustomerRecord customer = getUserInfo(room, checkIn, checkOut);
        display(customer);
    }

    /**
     * The function "displayAllCustomers" prints a formatted table of all customer records.
     */
    public void displayAllCustomers() {
        clearScreen();
        if (transactionRecord.size() <= 0) {
            System.out.println("| NOTIFICATION: No booking has been made. Please book first\t|\n");
        } else {
            System.out.println("| NOTIFICATION: Displaying all transaction records\t\t|\n");
            System.out.println(
                    "=====================================================================================================================================================================");
            System.out.printf(CUSTOMER_TITLE_FORMAT, CUSTOMER_TITLE_COLUMNS[0], // "ID",
                    CUSTOMER_TITLE_COLUMNS[1], // "First Name",
                    CUSTOMER_TITLE_COLUMNS[2], // "Last Name",
                    CUSTOMER_TITLE_COLUMNS[3], // "Address",
                    CUSTOMER_TITLE_COLUMNS[4], // "MOP",
                    CUSTOMER_TITLE_COLUMNS[5], // "Email",
                    CUSTOMER_TITLE_COLUMNS[6], // "Contact",
                    CUSTOMER_TITLE_COLUMNS[7], // "Check-in",
                    CUSTOMER_TITLE_COLUMNS[8], // "Check-out",
                    CUSTOMER_TITLE_COLUMNS[9], // "Room",
                    CUSTOMER_TITLE_COLUMNS[10]); // "Status"
            System.out.println(
                    "=====================================================================================================================================================================");

            transactionRecord.values().stream()
                    .forEach(customer -> System.out.printf(CUSTOMER_TITLE_FORMAT,
                            customer.customerID(), customer.firstName(), customer.lastName(),
                            customer.address(), customer.modeOfPayment(), customer.email(),
                            customer.contactNum(), customer.getCheckIn(), customer.getCheckOut(),
                            customer.getBookedRoomNum(), customer.status()));
            System.out.println(
                    "=====================================================================================================================================================================\n");
        }
    }

    /**
     * The function displays all the rooms in a hotel, including their room number, status, tier,
     * maximum occupancy, and price.
     */
    public void displayAllRooms() {
        clearScreen();
        System.out.println("| NOTIFICATION: Displaying all rooms\t\t\t\t|\n");

        System.out.println(ROOM_TITLE_BORDER);

        System.out.printf(ROOM_TITLE_FORMAT, ROOM_TITLE_COLUMNS[0], ROOM_TITLE_COLUMNS[1],
                ROOM_TITLE_COLUMNS[2], ROOM_TITLE_COLUMNS[3], ROOM_TITLE_COLUMNS[4]);

        System.out.println(ROOM_TITLE_BORDER);

        setBookedToNotAvailable();

        Arrays.stream(hotelRooms)
                .forEach(room -> System.out.printf(ROOM_TITLE_FORMAT, room.getRoomNum(),
                        room.getRoomStatus(), room.getRoomTier(), room.getRoomPax(),
                        df.format(room.calculatePrice())));
        System.out.println(ROOM_TITLE_BORDER);
        System.out.println();
    }

    /**
     * The `cancelReservation` function allows a customer to cancel their reservation by entering
     * their room number and updating the room status to "AVAILABLE".
     */
    public void cancelReservation() {
        clearScreen();
        boolean isRoomEqual = true;
        long customerID = cancelCustomerID();
        do {
            if (transactionRecord.size() <= 0) {
                break;
            }
            try {
                System.out.print("| Please enter your room number\t\t:\t");
                input = sc.nextLine().strip();

                checkIfEmpty(input);

                int roomNum = Integer.parseInt(input);

                isRoomEqual = transactionRecord.get(customerID).getBookedRoomNum() == roomNum;

                if (!isRoomEqual) {
                    System.out.println(
                            "\n| ERROR: the room number does not match the booked room number. Please try again. |\n");
                } else {
                    hotelRooms[roomNum - 1].setRoomStatus("AVAILABLE");
                    System.out.println();
                    System.out.println(ROOM_TITLE_BORDER);
                    System.out.println(SPACE);
                    System.out.println("| \tYour reservation has been successfully cancelled.\t|");
                    System.out.println("| \tThank you for using our services!\t\t\t|");
                    System.out.println(SPACE);
                    System.out.println(ROOM_TITLE_BORDER);
                    System.out.println();
                }
            } catch (NumberFormatException e) {
                System.out.println(DataExceptions.errorMessages[1]);
                isRoomEqual = false;
            } catch (EmptyInputException e) {
                System.out.println(e.getMessage());
                isRoomEqual = false;
            }
        } while (!isRoomEqual);
        file.updateFile(transactionRecord);
    }

    /**
     * The function filters hotel rooms by their status and displays the filtered rooms'
     * information.
     * 
     * @param status The "status" parameter is a string that represents the status of the rooms. It
     *        is used to filter the rooms based on their status.
     */
    public void filterByStatus(String status) {
        clearScreen();
        System.out.printf("| NOTIFICATION: Displaying rooms that are %s\t\t|%n%n", status);
        setBookedToNotAvailable();
        System.out.println(ROOM_TITLE_BORDER);
        System.out.printf(ROOM_TITLE_FORMAT, ROOM_TITLE_COLUMNS[0], ROOM_TITLE_COLUMNS[1],
                ROOM_TITLE_COLUMNS[2], ROOM_TITLE_COLUMNS[3], ROOM_TITLE_COLUMNS[4]);
        System.out.println(ROOM_TITLE_BORDER);

        Arrays.stream(hotelRooms).filter(room -> room.getRoomStatus().equals(status))
                .forEach(room -> System.out.printf(ROOM_TITLE_FORMAT, room.getRoomNum(),
                        room.getRoomStatus(), room.getRoomTier(), room.getRoomPax(),
                        df.format(room.calculatePrice())));
        System.out.println(ROOM_TITLE_BORDER);
        System.out.println();
    }

    /**
     * The function filters hotel rooms by tier and displays the room details.
     * 
     * @param tier The "tier" parameter in the filterByTier method is used to filter the hotel rooms
     *        based on their tier. It is a String that represents the tier of the rooms.
     */
    public void filterByTier(String tier) {
        clearScreen();
        System.out.printf("| NOTIFICATION: Displaying rooms that are %s\t\t|%n%n", tier);
        setBookedToNotAvailable();
        displayRoomsTierDesc(tier);
        System.out.println(ROOM_TITLE_BORDER);
        System.out.printf(ROOM_TITLE_FORMAT, ROOM_TITLE_COLUMNS[0], ROOM_TITLE_COLUMNS[1],
                ROOM_TITLE_COLUMNS[2], ROOM_TITLE_COLUMNS[3], ROOM_TITLE_COLUMNS[4]);
        System.out.println(ROOM_TITLE_BORDER);

        Arrays.stream(hotelRooms).filter(room -> room.getRoomTier().equals(tier))
                .forEach(room -> System.out.printf(ROOM_TITLE_FORMAT, room.getRoomNum(),
                        room.getRoomStatus(), room.getRoomTier(), room.getRoomPax(),
                        df.format(room.calculatePrice())));
        System.out.println(ROOM_TITLE_BORDER);
        System.out.println();

    }

    /**
     * The function filters and sorts hotel rooms by price, with an option to reverse the sorting
     * order.
     * 
     * @param reverse The "reverse" parameter is a boolean value that determines the sorting order
     *        of the rooms by price. If "reverse" is set to true, the rooms will be sorted in
     *        descending order of price. If "reverse" is set to false, the rooms will be sorted in
     *        ascending order of price.
     */
    public void filterByPrice(boolean reverse) {
        clearScreen();
        if (reverse)
            System.out.println("| NOTIFICATION: Displaying rooms in descending order\t\t|\n");
        else
            System.out.println("| NOTIFICATION: Displaying rooms in ascending order\t\t|\n");
        setBookedToNotAvailable();
        System.out.println(ROOM_TITLE_BORDER);
        System.out.printf(ROOM_TITLE_FORMAT, ROOM_TITLE_COLUMNS[0], ROOM_TITLE_COLUMNS[1],
                ROOM_TITLE_COLUMNS[2], ROOM_TITLE_COLUMNS[3], ROOM_TITLE_COLUMNS[4]);
        System.out.println(ROOM_TITLE_BORDER);

        if (!reverse) {
            Arrays.stream(hotelRooms).sorted(Comparator.comparing(Room::calculatePrice))
                    .forEachOrdered(room -> System.out.printf(ROOM_TITLE_FORMAT, room.getRoomNum(),
                            room.getRoomStatus(), room.getRoomTier(), room.getRoomPax(),
                            df.format(room.calculatePrice())));
        } else {
            Arrays.stream(hotelRooms).sorted(Comparator.comparing(Room::getRoomNum).reversed())
                    .sorted(Comparator.comparing(Room::calculatePrice).reversed())
                    .forEachOrdered(room -> System.out.printf(ROOM_TITLE_FORMAT, room.getRoomNum(),
                            room.getRoomStatus(), room.getRoomTier(), room.getRoomPax(),
                            df.format(room.calculatePrice())));
        }
        System.out.println(ROOM_TITLE_BORDER);
        System.out.println();

    }

    // ---------------------------------------------------------- PRIVATE METHODS
    // ---------------------------------------------------------

    private void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    /**
     * The function checks if a given input string is empty and throws an exception if it is.
     * 
     * @param input The input parameter is a String that represents the user input that needs to be
     *        checked for emptiness.
     */
    private void checkIfEmpty(String input) throws EmptyInputException {
        if (input.isEmpty())
            throw new EmptyInputException(DataExceptions.errorMessages[0]);
    }

    /**
     * The function sets the status of all booked rooms to "not available" in a hotel.
     */
    private void setBookedToNotAvailable() {
        transactionRecord.values().stream().filter(customer -> customer.status().equals(BOOKED))
                .forEach(customer -> {
                    Room room = hotelRooms[customer.getBookedRoomNum() - 1];
                    room.setRoomStatus(NOT_AVAILABLE);
                });
    }

    /**
     * The function displays the description of hotel rooms of a specific tier, with each line of
     * the description limited to 60 characters.
     * 
     * @param roomTier The parameter `roomTier` is a String that represents the tier of the rooms to
     *        be displayed.
     */
    private void displayRoomsTierDesc(String roomTier) {
        // The above code is printing a formatted list of hotel rooms that match a
        // specific room tier. It first
        // prints a border line, then iterates over the hotelRooms array and filters out
        // the rooms that have
        // the specified room tier. For each matching room, it splits the room tier
        // description into words and
        // formats them into lines that are no longer than 60 characters. Each line is
        // then printed within a
        // border line. The code also ensures that the border line and room tier
        // description are only printed
        // once, even if there are multiple matching rooms.
        System.out.println(ROOM_TITLE_BORDER);
        final boolean[] isPrinted = {true};
        Arrays.stream(hotelRooms).filter(room -> room.getRoomTier().equals(roomTier))
                .forEach(room -> {
                    if (isPrinted[0]) {
                        String[] words = room.getRoomTierDesc().split(" ");
                        StringBuilder line = new StringBuilder(words[0]);
                        for (int i = 1; i < words.length; i++) {
                            if (line.length() + words[i].length() < 60) {
                                line.append(" ").append(words[i]);
                            } else {
                                System.out.printf("| %-61s |%n", line);
                                line = new StringBuilder(words[i]);
                            }
                        }
                        System.out.printf("| %-61s |%n", line);
                        System.out.println(ROOM_TITLE_BORDER);
                        isPrinted[0] = false;
                    }
                });
    }

    /**
     * The function allows the user to select a room tier and displays all the rooms of that tier
     * along with their details.
     * 
     * @return The method is returning a String representing the room tier that the user has
     *         selected.
     */
    private String getRoomTier() {
        clearScreen();
        final String[] roomTiers = {"DELUXE", "EXECUTIVE", "PRESIDENTIAL"};
        int index = 1;
        displayAllRooms();
        do {
            try {
                System.out.println(ROOM_TITLE_BORDER);
                System.out.println(SPACE);
                System.out.println("| ROOM TIERS:\t\t\t\t\t\t\t|");
                System.out.println(SPACE);
                System.out.println(ROOM_TITLE_BORDER);
                System.out.println("|  - [1] DELUXE\t\t\t\t\t\t\t|");
                System.out.println("|  - [2] EXECUTIVE\t\t\t\t\t\t|");
                System.out.println("|  - [3] PRESIDENTIAL\t\t\t\t\t\t|");
                System.out.println(ROOM_TITLE_BORDER);
                System.out.println();
                System.out.print("| Enter the room tier you want to check in\t:\t");
                input = sc.nextLine().strip();

                checkIfEmpty(input);

                index = Integer.parseInt(input);
                isValid = index >= 1 && index <= 3;
                if (!isValid)
                    throw new InvalidChoiceException(DataExceptions.errorMessages[2]);
            } catch (NumberFormatException e) {
                System.out.println(DataExceptions.errorMessages[1]);
                isValid = false;
            } catch (InvalidChoiceException | EmptyInputException e) {
                System.out.println(e.getMessage());
                isValid = false;
            }
        } while (!isValid);

        final int finalIndex = index - 1;

        System.out.printf("%n| Showing the rooms that are %s tier |%n%n", roomTiers[finalIndex]);

        displayRoomsTierDesc(roomTiers[finalIndex]);

        System.out.println(ROOM_TITLE_BORDER);

        System.out.printf(ROOM_TITLE_FORMAT, ROOM_TITLE_COLUMNS[0], ROOM_TITLE_COLUMNS[1],
                ROOM_TITLE_COLUMNS[2], ROOM_TITLE_COLUMNS[3], ROOM_TITLE_COLUMNS[4]);
        System.out.println(ROOM_TITLE_BORDER);

        Arrays.stream(hotelRooms).filter(room -> room.getRoomTier().equals(roomTiers[finalIndex]))
                .forEach(room -> System.out.printf(ROOM_TITLE_FORMAT, room.getRoomNum(),
                        room.getRoomStatus(), room.getRoomTier(), room.getRoomPax(),
                        df.format(room.calculatePrice())));
        System.out.println(ROOM_TITLE_BORDER);
        return roomTiers[finalIndex];
    }

    /**
     * The function `getRoom` in Java prompts the user to enter a room number and checks if the room
     * is available and valid based on its tier.
     * 
     * @param roomTier The parameter "roomTier" is a String that represents the tier or category of
     *        the room that the user wants to check in.
     * @return The method is returning a Room object.
     */
    private Room getRoom(String roomTier) {
        int roomNumber = 0;
        boolean isRoomAvailable = true;
        boolean isRoomValid = true;

        do {
            try {
                System.out.print("| Enter the room number you want to check in\t:\t");
                input = sc.nextLine().strip();

                checkIfEmpty(input);

                roomNumber = Integer.parseInt(input);

                final int finalroomNumber = roomNumber;
                isRoomAvailable = transactionRecord.values().stream()
                        .filter(customer -> customer.getBookedRoomNum() == finalroomNumber)
                        .allMatch(customer -> customer.status().equals("CANCELLED")
                                && hotelRooms[finalroomNumber - 1].getRoomStatus()
                                        .equals("AVAILABLE"));

                isRoomValid = hotelRooms[roomNumber - 1].getRoomTier().equals(roomTier);

                if (!isRoomValid)
                    System.out.printf(
                            "%n| ERROR: Room number should be of %s tier. Please try again. |%n%n",
                            roomTier);
                else if (!isRoomAvailable)
                    System.out.println("\n| ERROR: Room is already booked. Please try again. |\n");
            } catch (NumberFormatException e) {
                System.out.println(DataExceptions.errorMessages[1]);
                isRoomAvailable = false;
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println(DataExceptions.errorMessages[3]);
                isRoomAvailable = false;
            } catch (EmptyInputException e) {
                System.out.println(e.getMessage());
                isRoomAvailable = false;
            }
        } while (!isRoomAvailable || !isRoomValid);
        return hotelRooms[roomNumber - 1];
    }

    /**
     * The function sets the check-in date for a room, either using the current date or prompting
     * the user to enter a valid future date.
     * 
     * @param choice A character representing the user's choice for setting the check-in date. 'Y'
     *        indicates that the current date should be used as the check-in date, else indicates
     *        that the user will enter a specific check-in date.
     * @param room The "room" parameter is an object of the Room class.
     * @return The method is returning a String, which is the formatted check-in date.
     */
    private String setCheckIn(char choice, Room room) {
        LocalDate checkInDate = LocalDate.now();
        boolean isValidDate;
        if (choice == 'Y')
            room.setRoomStatus(NOT_AVAILABLE);
        else {
            do {
                isValidDate = true;
                try {
                    System.out.print(
                            "\n| Format: MM-dd-yyyy |\n| Please enter reservation date\t\t\t:\t");
                    String date = sc.nextLine().strip();

                    checkIfEmpty(date);

                    checkInDate = LocalDate.parse(date, format);

                    if (checkInDate.isBefore(LocalDate.now())) {
                        throw new DateIsInThePastException(DataExceptions.errorMessages[6]);
                    }
                } catch (DateTimeParseException e) {
                    System.out.println(DataExceptions.errorMessages[4]);
                    isValidDate = false;
                } catch (DateIsInThePastException | EmptyInputException e) {
                    System.out.println(e.getMessage());
                    isValidDate = false;
                }
            } while (!isValidDate);
            room.setRoomStatus(NOT_AVAILABLE);
        }
        return checkInDate.format(format);
    }

    /**
     * The function prompts the user to input whether they would like to check in today and
     * validates the input.
     * 
     * @param room The "room" parameter is an object of the Room class.
     * @return The method is returning a String value.
     */
    private String checkIn(Room room) {
        char choice = 'N';
        do {
            try {
                System.out.print("| Would you like to check in today?[Y/N]\t:\t");
                input = sc.nextLine().toUpperCase().strip();

                checkIfEmpty(input);

                choice = input.charAt(0);

                isValid = choice == 'Y' || choice == 'N';
                if (!isValid)
                    System.out.println(DataExceptions.errorMessages[5]);

            } catch (EmptyInputException e) {
                System.out.println(e.getMessage());
                isValid = false;
            }
        } while (!isValid);
        return setCheckIn(choice, room);
    }

    /**
     * The function `checkOut` calculates the check-out date based on the check-in date and the
     * length of stay provided by the user.
     * 
     * @param checkInDate A string representing the check-in date in the format "yyyy-MM-dd".
     * @return The method is returning a String representing the check-out date.
     */
    private String checkOut(String checkInDate) {

        int length = 0;
        boolean isValidLength;
        String strCheckIn = checkInDate;

        LocalDate checkIn = LocalDate.parse(strCheckIn, format);

        do {
            try {
                System.out.print("| How many days would you like to stay?\t\t:\t");
                input = sc.nextLine().strip();

                checkIfEmpty(input);

                length = Integer.parseInt(input);
                isValidLength = length >= 0;
                if (!isValidLength)
                    throw new InvalidLengthOfStayException(DataExceptions.errorMessages[7]);
            } catch (NumberFormatException e) {
                System.out.println(DataExceptions.errorMessages[1]);
                isValidLength = false;
            } catch (InvalidLengthOfStayException | EmptyInputException e) {
                System.out.println(e.getMessage());
                isValidLength = false;
            }
        } while (!isValidLength);

        LocalDate checkOut = checkIn.plusDays(length);
        return checkOut.format(format);
    }

    /**
     * The function `getFirstName()` prompts the user to enter their first name, validates the
     * input, and returns the input if it is valid.
     * 
     * @return The method is returning a String, which is the user's first name.
     */
    private String getFirstName() {
        do {
            try {
                System.out.print("| Enter your first name\t\t\t\t:\t");
                input = sc.nextLine().strip();

                isValid = input.matches("[a-zA-Z]+$");

                checkIfEmpty(input);

                if (!isValid)
                    throw new InvalidChoiceException(DataExceptions.errorMessages[9]);
                if (input.length() < 3)
                    throw new InvalidInputLengthException(
                            "\n| ERROR: First name should at least be 3 characters long. Please try again.\n");

            } catch (EmptyInputException | InvalidChoiceException | InvalidInputLengthException e) {
                System.out.println(e.getMessage());
                isValid = false;
            }
        } while (!isValid);
        return input;
    }

    /**
     * The function `getLastName()` prompts the user to enter their last name, validates the input,
     * and returns the last name.
     * 
     * @return The method is returning a String, which is the user's last name.
     */
    private String getLastName() {
        do {
            try {
                System.out.print("| Enter your last name\t\t\t\t:\t");
                input = sc.nextLine().strip();

                isValid = input.matches("[a-zA-Z]+$");

                checkIfEmpty(input);

                if (!isValid)
                    throw new InvalidChoiceException(DataExceptions.errorMessages[9]);
                if (input.length() < 2)
                    throw new InvalidInputLengthException(
                            "\n| ERROR: Last name should at least be 2 characters long. Please try again.\n");
            } catch (EmptyInputException | InvalidChoiceException | InvalidInputLengthException e) {
                System.out.println(e.getMessage());
                isValid = false;
            }
        } while (!isValid);
        return input;
    }

    /**
     * The getAddress() function prompts the user to enter their address (city) and validates the
     * input to ensure it is not empty, consists of only letters and spaces, and is at least 4
     * characters long.
     * 
     * @return The method is returning a String, which is the user's address (city).
     */
    private String getAddress() {
        do {
            try {
                System.out.print("| Enter your address(City)\t\t\t:\t");
                input = sc.nextLine().strip();

                isValid = input.matches("[a-zA-Z ]+$");

                checkIfEmpty(input);

                if (!isValid)
                    throw new InvalidChoiceException(DataExceptions.errorMessages[9]);
                if (input.length() < 4)
                    throw new InvalidInputLengthException(
                            "\n| ERROR: City name should at least be 4 characters long. Please try again.\n");

            } catch (EmptyInputException | InvalidChoiceException | InvalidInputLengthException e) {
                System.out.println(e.getMessage());
                isValid = false;
            }
        } while (!isValid);
        return input;
    }

    /**
     * The function `getModeOfPayment()` prompts the user to enter their mode of payment and returns
     * the selected mode of payment as a string.
     * 
     * @return The method is returning a String representing the mode of payment chosen by the user.
     */
    private String getModeOfPayment() {
        int choice = 0;
        do {
            try {
                System.out.println("| Enter your mode of payment: ");
                System.out.println("|  - [1]. Credit Card");
                System.out.println("|  - [2]. Cash");
                System.out.println("|  - [3]. Cheque");
                System.out.print("| Enter your choice\t\t\t\t:\t");
                input = sc.nextLine().strip();

                checkIfEmpty(input);

                choice = Integer.parseInt(input);

                isValid = choice >= 1 && choice <= 3;

                if (!isValid)
                    throw new InvalidChoiceException(DataExceptions.errorMessages[2]);
            } catch (NumberFormatException e) {
                System.out.println(DataExceptions.errorMessages[1]);
                isValid = false;
            } catch (InvalidChoiceException | EmptyInputException e) {
                System.out.println(e.getMessage());
                isValid = false;
            }
        } while (!isValid);
        String[] modeOfPaymentsArr = {"CREDIT CARD", "CASH", "CHEQUE"};
        return modeOfPaymentsArr[choice - 1];
    }

    // The above code is a method in Java that prompts the user to enter an email
    // address and validates
    // if the entered email address is in the correct format.
    private String getEmailAddress() {
        String email;
        do {
            System.out.print("| Enter your email\t\t\t\t:\t");
            email = sc.nextLine().strip();

            isValid = email.matches(REGEX);

            if (!isValid) {
                System.out.println("\n| ERROR: Invalid email format. Please try again.\n");
                System.out.println(
                        """
                                ========================================================================================================================================================================
                                |                                                                                                                                                                      |
                                | EMAIL FORMAT:                                                                                                                                                        |
                                | -\tThe email must start with one or more alphanumeric characters (a-z, A-Z, 0-9), underscores (_), plus signs (+), ampersands (&), asterisks (*), or hyphens (-).    |
                                | -\tThe email must contain an at symbol (@) after the initial characters and any optional dot-separated groups.                                                       |
                                | -\tAfter the `@` symbol, the email must contain one or more groups of alphanumeric characters or hyphens, each separated by a dot.                                   |
                                | -\tEach group must not start or end with a hyphen.                                                                                                                   |
                                | -\tThe email must end with a top-level domain that consists of two to seven alphabetic characters (a-z, A-Z).                                                        |
                                |                                                                                                                                                                      |
                                ========================================================================================================================================================================
                                """);
            }
        } while (!isValid);
        return email;
    }

    // The above code is a method in Java that prompts the user to enter their
    // contact number. It
    // ensures that the contact number is a 10-digit number and returns the valid
    // contact number. If
    // the entered contact number is not 10 digits long, it displays an error
    // message and prompts the
    // user to enter the contact number again.
    private String getContactNum() {
        String contactNum;
        boolean isValidContact;
        do {
            System.out.print("| Enter your contact number\t\t\t:\t(+63)");
            contactNum = sc.nextLine().strip();
            isValidContact = contactNum.matches("^\\d{10}$");
            if (!isValidContact)
                System.out.println(
                        "\n| ERROR: Contact number should only be 10-digits long. Please try again.\n");
        } while (!isValidContact);
        return contactNum;
    }

    // The above code is a method in Java that generates a unique customer ID. It
    // does this by retrieving
    // the maximum customer ID from a transaction record, and then incrementing it
    // by 1 to generate a new
    // ID.
    private static long generateCustomerID() {
        return transactionRecord.keySet().stream().max(Long::compare).orElse(0L) + 1;
    }

    // The above code is defining a method called `getUserInfo` that takes in a
    // `Room` object, a `checkIn`
    // date, and a `checkOut` date as parameters.
    private CustomerRecord getUserInfo(Room room, String checkIn, String checkOut) {
        long customerID = generateCustomerID();
        String firstName = getFirstName();
        String lastName = getLastName();
        String address = getAddress();
        String modeOfPayment = getModeOfPayment();
        String email = getEmailAddress();
        String contactNum = getContactNum();
        int bookedRoomNum = room.getRoomNum();
        CustomerRecord customer = new CustomerRecord(customerID, firstName, lastName, address,
                modeOfPayment, email, contactNum, checkIn, checkOut, bookedRoomNum);
        transactionRecord.put(customerID, customer);
        file.writeFile(customer);
        return customer;
    }

    // The above code is defining a method called "display" that takes a
    // CustomerRecord object as a
    // parameter. This method is responsible for printing out the details of the
    // customer's record, such as
    // their ID, first name, last name, address, mode of payment, email address,
    // contact number, check-in
    // and check-out dates, and room number. It also includes a message for the
    // customer to take note of
    // their ID and room number for future reference.
    private void display(CustomerRecord customer) {
        System.out.println(
                "=========================================== [ RECEIPT ] ===========================================");
        System.out.println(
                "|                                                                                                   ");
        System.out.printf("| Customer's ID                \t\t\t:\t%s%n", customer.customerID());
        System.out.printf("| Customer's First Name        \t\t\t:\t%s%n", customer.firstName());
        System.out.printf("| Customer's Last Name         \t\t\t:\t%s%n", customer.lastName());
        System.out.printf("| Customer's Address           \t\t\t:\t%s%n", customer.address());
        System.out.printf("| Customer's Mode of Payment   \t\t\t:\t%s%n", customer.modeOfPayment());
        System.out.printf("| Customer's Email Address     \t\t\t:\t%s%n", customer.email());
        System.out.printf("| Customer's Contact Number    \t\t\t:\t%s%n", customer.contactNum());
        System.out.printf("| Customer's Check-in Date     \t\t\t:\t%s%n", customer.getCheckIn());
        System.out.printf("| Customer's Check-out Date    \t\t\t:\t%s%n", customer.getCheckOut());
        System.out.printf("| Customer's Room Number       \t\t\t:\t%d%n",
                customer.getBookedRoomNum());
        System.out.println(
                "|                                                                                                   ");
        System.out.println(
                "===================================================================================================");
        System.out.println(
                "|                                                                                                 |");
        System.out.println(
                "|     Please take note of your ID and Room Number. You will use this to cancel your reservation.  |");
        System.out.println(
                "|                                                                                                 |");
        System.out.println(
                "===================================================================================================");
    }

    // Handles the cancellation of booking. Sets the status to CANCELLED.
    private long cancelCustomerID() {
        System.out.println(ROOM_TITLE_BORDER);
        System.out.println("|             \t\t\t\t\t\t\t|");
        System.out.println("| CANCELLATION\t\t\t\t\t\t\t|");
        System.out.println("|             \t\t\t\t\t\t\t|");
        System.out.println(ROOM_TITLE_BORDER);
        System.out.println();

        displayAllCustomers();

        long customerID = 0;
        do {
            if (transactionRecord.size() <= 0) {
                break;
            }
            try {
                System.out.print("| Please enter your Customer ID\t\t:\t");
                input = sc.nextLine().strip();

                if (input.isEmpty())
                    throw new EmptyInputException(input);

                customerID = Long.parseLong(input);

                isValid = transactionRecord.containsKey(customerID);

                if (!isValid) {
                    System.out.println(DataExceptions.errorMessages[8]);
                } else {
                    transactionRecord.get(customerID).setBookStatus("CANCELLED");
                }
            } catch (NumberFormatException e) {
                System.out.println(DataExceptions.errorMessages[1]);
                isValid = false;
            } catch (EmptyInputException e) {
                System.out.println(DataExceptions.errorMessages[0]);
                isValid = false;
            }
        } while (!isValid);
        return customerID;
    }
}
