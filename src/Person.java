/**
 * This is the abstract person class
 * @author Joseph Medina - Sandoval
 */
public abstract class Person {
    private String firstName;
    private String lastName;
    private String dateOfBirth;
    private String address;
    private String phoneNumber;
    private int identificationNumber;

    public Person() {
    }

    /**
     * The Person class will be used as an abstract for a Customer class
     * @param firstNameIn The first name of the user
     * @param lastNameIn The last name of the user
     * @param dateOfBirthIn The date of birth of the user
     * @param addressIn The address of the user
     * @param phoneNumberIn The phone number of the user
     * @param identificationNumberIn The identification number of the user
     */
    public Person(String firstNameIn, String lastNameIn, String dateOfBirthIn, String addressIn, String phoneNumberIn, int identificationNumberIn) {
        this.firstName =firstNameIn;
        this.lastName = lastNameIn;
        this.dateOfBirth = dateOfBirthIn;
        this.address = addressIn;
        this.phoneNumber = phoneNumberIn;
        this.identificationNumber = identificationNumberIn;

    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstNameIn) {
        this.firstName = firstNameIn;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String lastNameIn) {
        this.lastName = lastNameIn;
    }

    public String getDateOfBirth() {
        return this.dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirthIn) {
        this.dateOfBirth = dateOfBirthIn;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String addressIn) {
        this.address = addressIn;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public void setPhoneNumber(String phoneNumberIn) {
        this.phoneNumber = phoneNumberIn;
    }

    public void setIdentificationNumber(int identificationNumberIn){
        int idNumber = identificationNumberIn;
        this.identificationNumber = idNumber;
    }

    public int getIdentificationNumber() {
        return this.identificationNumber;
    }
}
