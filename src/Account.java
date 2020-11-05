/**
 * The Account class is an abstract for the Checking, Savings, and Credit classes
 * @author Joseph Medina - Sandoval
 */
public abstract class Account {

    private int accountNumber;
    private double startingBalance;
    private double balance;

    public Account(){}

    /**
     * The account will have an account number and a balance, as well as a starting balance
     * @param accountNumberIn the account number for this account
     * @param balanceIn the balance this account holds
     */
    public Account(int accountNumberIn, double balanceIn){
        this.accountNumber = accountNumberIn;
        this.balance = balanceIn;
        this.startingBalance = balanceIn;
    }

    public int getAccountNumber() {
        return this.accountNumber;
    }

    public void setAccountNumber(int accountNumber) {
        this.accountNumber = accountNumber;
    }

    public double getBalance() {
        return this.balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public double getStartingBalance() {
        return this.startingBalance;
    }

    public void setStartingBalance(double balance) {
        this.startingBalance = balance;
    }
}
