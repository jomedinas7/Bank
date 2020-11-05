import static org.junit.Assert.*;

/**
 * A series of test methods to test deposit, withdraw, and transfer functions
 */
public class BankTest {

    Checking check = new Checking(0001,205.0);
    Credit credit = new Credit(0002,-200.0,2000);

    @org.junit.Test
    public void deposit1() {
        assertTrue(Bank.deposit(check,200));
    }
    @org.junit.Test
    public void deposit2(){
        assertFalse(Bank.deposit(check, -200));
    }
    @org.junit.Test
    public void deposit3(){
        assertFalse(Bank.deposit(credit, 250));
    }

    @org.junit.Test
    public void withdraw() {
        assertTrue(Bank.withdraw(check,200));
    }
    @org.junit.Test
    public void withdraw2(){
        assertFalse(Bank.withdraw(credit, -200));
    }
    @org.junit.Test
    public void withdraw3(){
        assertFalse(Bank.withdraw(check, 5000));
    }

    @org.junit.Test
    public void transfer1() {
        assertFalse(Bank.transfer(check,credit,-200));
    }
    @org.junit.Test
    public void transfer2(){
        assertTrue(Bank.transfer(credit,check, 200));
    }
    @org.junit.Test
    public void transfer3(){
        assertTrue(Bank.transfer(check,credit,200));
    }
    @org.junit.Test
    public void transfer4(){
        assertFalse(Bank.transfer(check,credit,210));
    }
}