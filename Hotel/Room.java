public abstract class Room {
    protected static final double BASE_PRICE = 5_000.00;
    protected int roomNum;
    protected String roomStatus; // reserved, occupied, available
    protected int roomPax;
    protected String roomTier;
    protected String roomTierDesc;
    protected double additionalFee;

    protected Room(int roomNum) {
        this.roomNum = roomNum;
        this.roomStatus = "AVAILABLE";
    }

    protected void setRoomStatus(String roomStatus) {
        this.roomStatus = roomStatus;
    }

    protected int getRoomNum() {
        return roomNum;
    }

    protected String getRoomStatus() {
        return roomStatus;
    }

    protected abstract String getRoomTier();

    protected abstract String getRoomTierDesc();

    protected abstract int getRoomPax();

    protected abstract double calculatePrice();
}

class DeluxeRoom extends Room {
    public DeluxeRoom(int roomNum) {
        super(roomNum);
        this.roomTier = "DELUXE";
        this.roomTierDesc = "Experience comfort and relaxation in our deluxe rooms. They offer a range of amenities and services to cater to your needs. Whether you're traveling for business or leisure, you'll find everything you need for a memorable stay.";
        this.roomPax = 6;
        this.additionalFee = 5_000;
    }

    public String getRoomTier() {
        return roomTier;
    }

    public String getRoomTierDesc() {
        return roomTierDesc;
    }

    public int getRoomPax() {
        return roomPax;
    }

    public double calculatePrice() {
        return BASE_PRICE + additionalFee;
    }

}

class ExecutiveRoom extends Room {
    public ExecutiveRoom(int roomNum) {
        super(roomNum);
        this.roomTier = "EXECUTIVE";
        this.roomTierDesc = "Experience a higher level of luxury and convenience in our executive rooms, which are designed to meet the expectations of discerning travelers. Our executive rooms feature spacious and elegant interiors, premium bedding, and exclusive access to our executive lounge and benefits.";
        this.roomPax = 10;
        this.additionalFee = 10_000;
    }

    public String getRoomTier() {
        return roomTier;
    }

    public String getRoomTierDesc() {
        return roomTierDesc;
    }

    public int getRoomPax() {
        return roomPax;
    }

    public double calculatePrice() {
        return BASE_PRICE + additionalFee;
    }
}

class PresidentialRoom extends Room {
    public PresidentialRoom(int roomNum) {
        super(roomNum);
        this.roomTier = "PRESIDENTIAL";
        this.roomTierDesc = "Indulge yourself in the ultimate indulgence and sophistication in our presidential rooms, which are the epitome of elegance and style. Our presidential rooms boast stunning views, lavish furnishings, and state-of-the-art technology, as well as personalized service and attention from our dedicated staff.";
        this.roomPax = 15;
        this.additionalFee = 15_000;
    }

    public String getRoomTier() {
        return roomTier;
    }

    public String getRoomTierDesc() {
        return roomTierDesc;
    }

    public int getRoomPax() {
        return roomPax;
    }

    public double calculatePrice() {
        return BASE_PRICE + additionalFee;
    }
}