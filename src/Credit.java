/**
 * The Credit class will extend Account and will be an attribute for a Customer
 */

public class Credit extends Account {

    //adding credit limit
    private double creditLimit;

    public Credit() {
        super();
        creditLimit = 0.0;
    }
    /**
     * The credit account will have an account number and a balance
     * @param accountNumberIn the account number for this account
     * @param balanceIn the balance this account holds
     * @param creditLimitIn the maximum amount of credit a user can use
     */

    public Credit(int accountNumberIn, double balanceIn,double creditLimitIn) {
        super(accountNumberIn, balanceIn);
        this.creditLimit = creditLimitIn;

    }

    public double getCreditLimit(){
        return this.creditLimit;
    }

    public void setCreditLimit(double limit){
        this.creditLimit = limit;
    }

}