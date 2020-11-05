/**
 * This is the Customer class that extends the Person class
 * @author Joseph Medina - Sandoval
 */

public class Customer extends Person{
    //Attributes
    private Checking checking;
    private Savings savings;
    private Credit credit;
    private boolean hasCredit;
    private boolean hasChecking;
    private BankStatement statement;



    //Constructors
    public Customer(){
        super();
        Checking checkingIn = new Checking();
        this.checking = checkingIn;

        Savings savingsIn = new Savings();
        this.savings = savingsIn;

        Credit creditIn = new Credit();
        this.credit = creditIn;

        this.hasCredit=false;
        this.hasChecking =false;

    }


    /**
     *
     * @param firstNameIn The first name of the user
     * @param lastNameIn The last name of the user
     * @param dateOfBirthIn The date of birth of the user
     * @param addressIn The address of the user
     * @param phoneNumberIn The phone number of the user
     * @param identificationNum The identification number of the user
     * @param checkingNumIn The user's checking account number
     * @param savingsNumIn The user's savings account number
     * @param creditNumIn The user's credit account number
     * @param checkingBalIn The user's checking account balance
     * @param savingBalIn The user's savings account balance
     * @param creditBalIn The user's credit account balance
     * @param creditLimitIn The user's credit balance limit
     */
    public Customer(String firstNameIn, String lastNameIn, String dateOfBirthIn, int identificationNum, String addressIn,
                    String phoneNumberIn, int checkingNumIn, int savingsNumIn,int creditNumIn,double checkingBalIn, double savingBalIn,
                    double creditBalIn,double creditLimitIn){
        super(firstNameIn,lastNameIn,dateOfBirthIn,addressIn,phoneNumberIn,identificationNum);

        Checking checkingIn = new Checking(checkingNumIn,checkingBalIn);
        this.checking = checkingIn;

        Savings savingsIn = new Savings(savingsNumIn,savingBalIn);
        this.savings = savingsIn;

        Credit creditIn = new Credit(creditNumIn,creditBalIn,creditLimitIn);
        this.credit = creditIn;

        this.hasChecking = true;
        this.hasCredit = true;

    }

    //Methods

    public Checking getCheckings(){
        return this.checking;
    }
    public Savings getSavings(){return this.savings;}
    public Credit getCredit(){return this.credit;}

    public boolean getHasCredit() {
        return this.hasCredit;
    }

    public void setHasCredit(boolean hasCreditIn) {
        this.hasCredit = hasCreditIn;
    }

    public boolean getHasChecking() {
        return this.hasChecking;
    }

    public void setHasChecking(boolean hasCheckingIn) {
        this.hasChecking = hasCheckingIn;
    }

    public BankStatement getStatement() {
        return this.statement;
    }

    public void setStatement(BankStatement statementIn) {
        this.statement = statementIn;
    }

}
