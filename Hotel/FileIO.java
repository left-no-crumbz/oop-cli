import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FileIO {
    private static final String FILE_NAME = "C:Transaction Record.csv";
    private static final String DELIMITER = ",";
    private static final File CUSTOMER_FILE = new File(FILE_NAME);
    private static final Map<Long, CustomerRecord> transactionRecord = new HashMap<>();

    /**
     * The function reads data from a file and stores it in a map of customer
     * records.
     * 
     * @return The method is returning a Map<Long, CustomerRecord> object.
     */
    public Map<Long, CustomerRecord> readFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line = null;
            String[] fields;

            while ((line = reader.readLine()) != null) {
                fields = line.split(DELIMITER);
                long customerID = Long.parseLong(fields[0]);
                String firstName = fields[1];
                String lastName = fields[2];
                String address = fields[3];
                String modeOfPayment = fields[4];
                String email = fields[5];
                String contactNum = fields[6];
                String checkIn = fields[7];
                String checkOut = fields[8];
                int bookedRoomNum = Integer.parseInt(fields[9]);
                String status = fields[10];
                transactionRecord.put(Long.parseLong(fields[0]), new CustomerRecord(customerID, firstName, lastName,
                        address, modeOfPayment, email, contactNum, checkIn, checkOut, bookedRoomNum, status));
            }

        } catch (IOException e) {

        }
        return transactionRecord;
    }

    /**
     * The function writeFile writes customer data to a file in CSV format.
     * 
     * @param customer The parameter "customer" is an object of the class
     *                 CustomerRecord. It contains
     *                 information about a customer, such as their customer ID,
     *                 first name, last name, address, mode of
     *                 payment, email, contact number, check-in date, check-out
     *                 date, booked room number, and status.
     */
    public void writeFile(CustomerRecord customer) {
        String data = String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s",
                customer.customerID(), customer.firstName(), customer.lastName(), customer.address(),
                customer.modeOfPayment(), customer.email(),
                customer.contactNum(), customer.getCheckIn(), customer.getCheckOut(), customer.getBookedRoomNum(),
                customer.status());
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
            writer.write(data);
            writer.newLine();
        } catch (IOException e) {
            System.out.println(DataExceptions.errorMessages[10]);
        }
    }

    /**
     * The function creates a new file and prints a success message if the file is
     * created successfully.
     */
    public void createFile() {
        try {
            if (CUSTOMER_FILE.createNewFile()) {
                System.out.println("| NOTIFICATION: File created successfully.\t\t|");
            }
        } catch (IOException | SecurityException e) {
            
        }
    }

    /**
     * The function updates a file with customer records by writing the data from a
     * map of customer records
     * to the file.
     * 
     * @param customerRecord A map that contains customer records. The keys are of
     *                       type Long and represent
     *                       the customer IDs, and the values are of type
     *                       CustomerRecord, which is a custom class representing
     *                       the details of a customer.
     */
    public void updateFile(Map<Long, CustomerRecord> customerRecord) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, false))) {
            customerRecord.values().stream()
                    .forEach(
                            customer -> {
                                String data = String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s",
                                        customer.customerID(), customer.firstName(), customer.lastName(),
                                        customer.address(),
                                        customer.modeOfPayment(), customer.email(), customer.contactNum(),
                                        customer.getCheckIn(),
                                        customer.getCheckOut(), customer.getBookedRoomNum(), customer.status());
                                try {
                                    writer.write(data);
                                    writer.newLine();
                                } catch (IOException e) {
                                    System.out.println(DataExceptions.errorMessages[10]);
                                }
                            });
        } catch (IOException e) {
            System.out.println(DataExceptions.errorMessages[10]);
        }
    }
}
