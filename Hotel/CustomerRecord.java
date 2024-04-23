public class CustomerRecord {
    private long customerID;
    private final String firstName;
    private final String lastName;
    private final String address;
    private final String modeOfPayment;
    private final String email;
    private final String contactNum;
    private final String checkIn;
    private final String checkOut;
    private final int bookedRoomNum;
    private String status;

    public CustomerRecord(long customerID, String firstName, String lastName, String address, String modeOfPayment,
            String email, String contactNum, String checkIn, String checkOut, int bookedRoomNum) {
        this.customerID = customerID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.modeOfPayment = modeOfPayment;
        this.email = email;
        this.contactNum = contactNum;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.bookedRoomNum = bookedRoomNum;
        this.status = "BOOKED";
    }

    public CustomerRecord(long customerID, String firstName, String lastName, String address, String modeOfPayment,
            String email, String contactNum, String checkIn, String checkOut, int bookedRoomNum, String status) {
        this.customerID = customerID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.modeOfPayment = modeOfPayment;
        this.email = email;
        this.contactNum = contactNum;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.bookedRoomNum = bookedRoomNum;
        this.status = status;
    }

    // GETTERS
    public long customerID() {
        return customerID;
    }

    public String firstName() {
        return firstName;
    }

    public String lastName() {
        return lastName;
    }

    public String address() {
        return address;
    }

    public String modeOfPayment() {
        return modeOfPayment;
    }

    public String email() {
        return email;
    }

    public String contactNum() {
        return contactNum;
    }

    public String getCheckIn() {
        return checkIn;
    }

    public String getCheckOut() {
        return checkOut;
    }

    public int getBookedRoomNum() {
        return bookedRoomNum;
    }

    public String status() {
        return status;
    }

    // SETTERS
    public void setBookStatus(String status) {
        this.status = status;
    }
}