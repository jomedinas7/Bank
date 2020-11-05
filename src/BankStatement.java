/**
 * A BankStatement class that will be used to keep record of the user's accounts
 * as well as contain all of the information when compiling a bank statement text file
 * @author Joseph Medina - Sandoval
 */
public class BankStatement {
    private Customer cust;
    private double[] startingBalances = new double[3];
    private String record = "";

    public BankStatement(){}

    /**
     * The account will have an account number and a balance, as well as a starting balance
     * @param customer the customer that the bank statement will be based on. Accessing the customer's attributes will
     * allow for the bank statement to list all of the user's attributes as well
     */
    public BankStatement(Customer customer){
        this.cust = customer;
        this.startingBalances = new double[3];
        this.startingBalances[0] = customer.getSavings().getStartingBalance();
        this.startingBalances[1] = customer.getCheckings().getStartingBalance();
        this.startingBalances[2] = customer.getCredit().getStartingBalance();
        this.record = "";

    }
    public Customer getCustomer() {
        return this.cust;
    }

    public void setCustomer(Customer cust) {
        this.cust = cust;
    }

    public double[] getStartingBalances() {
        return this.startingBalances;
    }

    public void setStartingBalances(double[] startingBalance) {
        this.startingBalances[0] = startingBalance[0];
        this.startingBalances[1] = startingBalance[1];
        this.startingBalances[2] = startingBalance[2];
    }

    public String getRecord() {
        return this.record;
    }


    public void setRecord(String s){
        this.record = s;
    }

}
