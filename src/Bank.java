
import java.util.HashMap;
import java.io.*;
import java.util.Scanner;
import java.io.PrintWriter;

/**
 * This is the Bank class that holds the main to run this project
 * Course: Advanced Object Oriented Programming
 * Instructor : Dr. Mejia
 * Programming Assignment 3
 * Lab Description: To simulate a banking system with abstract classes and inheritance.
 * I confirm that the work of this assignment is completely my own. By turning in this assignment,
 * I declare that I did not receive unauthorized assistance. Moreover, all deliverables including,
 * but not limited to the source code, lab report and output files were written and produced by me alone.
 * @author Joseph Medina - Sandoval
 * @since October 15,2020
 */
public class Bank {
    public static void main(String[] args)throws Exception {
        //creates log file for transactions
        try {
            File transactions = new File("bankTransactions.txt");
            if (transactions.createNewFile()) {
                System.out.println("Log created: " + transactions.getName());
            }
        } catch (IOException e) {
            System.out.println("An error has occurred with the transactions log.");
            e.printStackTrace();
        }

        //create FileWriter to write to log
        FileWriter logWriter = new FileWriter("bankTransactions.txt");
        Scanner input = new Scanner(System.in);
        HashMap<Integer, Customer> map = null;

        int[] accNumsMax;
        System.out.println("Enter 'q' to exit or any other key to continue.");
        if (input.nextLine().equalsIgnoreCase("q")) {
            System.out.println("Thank you. Goodbye!");
        }
        else {

            String file;

            while (true) {
                System.out.println("Please input the file name for the bank database (w/o extension): ");
                Scanner getFile = new Scanner(System.in);
                file = getFile.nextLine();
                try {
                    map = fileToHash(file + ".csv");
                    break;
                } catch (FileNotFoundException e) {
                    System.out.println("This file was not found. Please try again.");
                    continue;
                }
            }
                accNumsMax = getHighestAccountNumbers(map);
            while (true) {
                    System.out.println("Hiya, welcome to the Bank of Disney, would you happen to be a Manager? (y/n) (Press Q to quit)");
                    Scanner ask = new Scanner(System.in);
                    String askManager = ask.nextLine();
                    if(askManager.equalsIgnoreCase("q")){
                        break;
                    }
                    if (askManager.equalsIgnoreCase("y")) {
                        System.out.println("Welcome Manager!");
                        managerMenu(map);
                    } else {
                        System.out.println("If you're an existing user, please enter 'z'. To create an account, enter 'e'. :");
                        String choice = ask.next();
                        if (choice.equalsIgnoreCase("e")) {
                            createCustomer(map, accNumsMax);
                        } else if (choice.equalsIgnoreCase("z")) {
                            BankStatement currentStatement = new BankStatement();
                            System.out.println("Please enter your identification number: ");
                            Customer currentCustomer = getUserFromKey(input, map);
                            currentStatement.setCustomer(currentCustomer);
                            currentCustomer.setStatement(currentStatement);
                            double[] bals = {currentCustomer.getSavings().getStartingBalance(),currentCustomer.getCheckings().getStartingBalance(),currentCustomer.getCredit().getStartingBalance()};
                            currentStatement.setStartingBalances(bals);
                            menu(map, currentCustomer, logWriter, accNumsMax, currentStatement);
                        } else {
                            System.out.println("This was not a valid input. Please try again.");
                        }
                    }
                }
            }
            logWriter.close();
            writeToCSV(map);
        }

    /**
     * @param fileName a string containing the name of the file to create a map from
     * @return  a Hashmap that contains Customer objects as values and their ID numbers as keys
     * @throws FileNotFoundException throws exception
     */
    public static HashMap fileToHash(String fileName) throws FileNotFoundException{
        HashMap<Integer,Customer> mappy = new HashMap<>();
        Scanner fileReader = new Scanner(new File(fileName));
        String[] fields= fileReader.nextLine().split(",");

        String[] customerData;
        while(fileReader.hasNextLine()) {
            customerData = fileReader.nextLine().split(",");
            Customer brokeBoi = new Customer();
            int index = 0;
            for (int i = 0; i < fields.length; i++) {
                switch (fields[i]){
                    case("Identification Number"):
                        brokeBoi.setIdentificationNumber(Integer.parseInt(customerData[index]));
                        index++;
                        break;
                    case("Savings Account Number"):
                        brokeBoi.getSavings().setAccountNumber(Integer.parseInt(customerData[index]));
                        index++;
                        break;
                    case("Last Name"):
                        brokeBoi.setLastName(customerData[index]);
                        index++;
                        break;
                    case("Date of Birth"):
                        brokeBoi.setDateOfBirth(customerData[index]);
                        index++;
                        break;
                    case("Checking Account Number"):
                        brokeBoi.getCheckings().setAccountNumber(Integer.parseInt(customerData[index]));
                        index++;
                        break;
                    case("Credit Account Number"):
                        brokeBoi.getCredit().setAccountNumber(Integer.parseInt(customerData[index]));
                        index++;
                        break;
                    case("Phone Number"):
                        brokeBoi.setPhoneNumber(customerData[index]);
                        index++;
                        break;
                    case("Checking Starting Balance"):
                        brokeBoi.getCheckings().setBalance(Double.parseDouble(customerData[index]));
                        brokeBoi.getCheckings().setStartingBalance(Double.parseDouble(customerData[index]));
                        index++;
                        break;
                    case("Savings Starting Balance"):
                        brokeBoi.getSavings().setBalance(Double.parseDouble(customerData[index]));
                        brokeBoi.getSavings().setStartingBalance(Double.parseDouble(customerData[index]));
                        index++;
                        break;
                    case("Credit Max"):
                        brokeBoi.getCredit().setCreditLimit(Double.parseDouble(customerData[index]));
                        index++;
                        break;
                    case("Credit Starting Balance"):
                        brokeBoi.getCredit().setBalance(Double.parseDouble(customerData[index]));
                        brokeBoi.getCredit().setStartingBalance(Double.parseDouble(customerData[index]));
                        index++;
                        break;
                    case("Address"):
                        brokeBoi.setAddress(customerData[index]+","+customerData[index+1]+","+customerData[index+2]);
                        index+=3;
                        break;
                    case("First Name"):
                        brokeBoi.setFirstName(customerData[index]);
                        index++;
                        break;
                    default:
                        System.out.println("This is not a known field.");
                        index++;
                        break;
                }
            }
            if(brokeBoi.getCredit().getAccountNumber() > 0){
                brokeBoi.setHasCredit(true);
            }
            if(brokeBoi.getCheckings().getAccountNumber() > 0){
                brokeBoi.setHasChecking(true);
            }
            BankStatement s= new BankStatement();
            brokeBoi.setStatement(s);
            mappy.put(brokeBoi.getIdentificationNumber(), brokeBoi);
        }
        return mappy;
    }

    /**
     * A method to take valid inputs for the required fields when creating a customer. For when the user cannot have an empty string in a field.
     * @return a String of whatever the user has input
     */
    public static String takeValidString(){
        Scanner scnr = new Scanner(System.in);
        while(true){
            String fN = scnr.nextLine();
            if(fN.equals("") || fN.equals(" ")){
                System.out.println("This is not a valid input. Please try again.");
            }
            else{
                return fN;
            }
        }
    }

    /**
     * A method for creating a Customer that was not included in the csv originally
     * @param map a Hashmap of Customers, to add the new customer to
     * @param maxAccNums an array of the largest account numbers so far, so that the user can have their account number as the increment of the previous largest
     */
    public static void createCustomer(HashMap<Integer,Customer> map, int[] maxAccNums){
        Scanner scnr = new Scanner(System.in);
        Customer newMF = new Customer();
        System.out.println("Hello there! What's your first name pal?");
        newMF.setFirstName(takeValidString());
        System.out.println("That's a neat first name! What about your last name?");
        newMF.setLastName(takeValidString());
        System.out.println("Nice to meet yah! When's your birthday? (Ex: 7-Jun-97)");
        newMF.setDateOfBirth(takeValidString());

        while(true){
            System.out.println("Okay, please enter your address (Ex: 123 Street St, New Vegas, WA 42069)");
            String[] addy = scnr.nextLine().split(",");
            if(addy.length == 3){
                newMF.setAddress(addy[0] +addy[1]+addy[2]);
                break;
            }
            else {
                System.out.println("This is not a valid address. Please make sure you followed the right format.");
            }
        }
        System.out.println("Sounds like a nice place to live! Now all we need is a phone number (Ex: (719)266-2837)");
        newMF.setPhoneNumber(takeValidString());

        //accNum[0] = Highest Checking Account Num
        //accNum[1] = Highest Savings Account Num
        //accNum[2] = Highest Credit Account Num
        //accNum[3] = Highest Identification Num
        newMF.getSavings().setAccountNumber(maxAccNums[1]+1);
        newMF.getSavings().setBalance(20.0);
        newMF.getSavings().setStartingBalance(20.0);
        maxAccNums[1]++;
        newMF.setIdentificationNumber(maxAccNums[3]+1);
        newMF.getCredit().setAccountNumber(-1);
        newMF.setHasCredit(false);
        newMF.getCheckings().setAccountNumber(-1);
        newMF.setHasChecking(false);
        maxAccNums[3]++;
        BankStatement s =new BankStatement();
        newMF.setStatement(s);
        map.put(newMF.getIdentificationNumber(),newMF);
        System.out.println("Great! You're now a member of DisneyBank! Your ID number is: " + newMF.getIdentificationNumber() + "\n We've started you out with a Savings account with $20.00.");

    }

    /**
     * A method that will start the flow of creating a new Credit or Checking account since a new user will start with only a Saving account
     * @param crusty the new customer, ironically named
     * @param maxAccNums an array of the largest account numbers so far, so that the user can have their account number as the increment of the previous larest
     */
    public static void createAccount(Customer crusty, int[] maxAccNums){
        if(crusty.getHasCredit() && crusty.getHasChecking()){
            System.out.println("You already have Checking, Savings, and Credit accounts. Thank you!");
            return;
        }
        Scanner scnr = new Scanner(System.in);
        boolean makingAccounts = true;

        //accNum[0] = Highest Checking Account Num
        //accNum[1] = Highest Savings Account Num
        //accNum[2] = Highest Credit Account Num
        //accNum[3] = Highest Identification Num

        while(makingAccounts){
            System.out.println("1. Create a Checking Account");
            System.out.println("2. Create a Credit Account");
            System.out.println("3. Quit\n");

            int input = scnr.nextInt();
            switch(input){
                case(1):
                    if(crusty.getHasChecking() == true){
                        System.out.println("You already have a Checking account.");
                    }
                    else{
                        crusty.setHasChecking(true);
                        //SET THEIR CHECKING ACCOUNT NUMBER
                        crusty.getCheckings().setAccountNumber(maxAccNums[0]+1);
                        maxAccNums[0]++;
                        crusty.getCheckings().setBalance(0);
                        System.out.println("You now have a Checking account. Your account number is: " + crusty.getCheckings().getAccountNumber());
                    }
                    break;
                case(2):
                    if(crusty.getHasCredit() == true){
                        System.out.println("You already have Credit account.");
                    }
                    else{
                        crusty.setHasCredit(true);
                        //SET THEIR CHECKING ACCOUNT NUMBER
                        crusty.getCredit().setAccountNumber(maxAccNums[2]+1);
                        maxAccNums[2]++;
                        crusty.getCredit().setBalance(0);
                        System.out.println("You now have a Credit account. Your account number is: " + crusty.getCredit().getAccountNumber());
                    }
                    break;
                case(3):
                    System.out.println("Thank you, goodbye!");
                    makingAccounts = false;
                    break;
                default:
                    System.out.println("This is not a valid input. Please try again.");
            }
        }
    }

    /**
     * If a user entered 'y' when asked if they were a manager they will be given the manager menu.
     * The entire manager menu takes place in this method.
     * @param map the hashmap of Customer objects
     * @throws IOException throws Exception
     */
    public static void managerMenu(HashMap<Integer,Customer> map) throws IOException {
        boolean managing = true;
        while(true) {
            System.out.println("Please select an option:");
            System.out.println("A.Inquire account by name");
            System.out.println("B.Inquire account by type/number");
            System.out.println("C.Exit");
            System.out.println("D.Print info from all accounts");
            System.out.println("E.Run a Transactions File");
            System.out.println("F.Generate a bank statement");

            Scanner manInput = new Scanner(System.in);
            String choice = manInput.nextLine();
            if (choice.equalsIgnoreCase("a")) {
                boolean found = false;
                System.out.println("Who's account would you like to inquire about? (Ex: firstName lastName)");
                String userInq = manInput.nextLine();
                for (Customer cust : map.values()) {
                    if ((cust.getFirstName() + " " + cust.getLastName()).equals(userInq)) {
                        printUserInfo(cust);
                        found = true;
                        break;
                    }
                }
                if(!found){
                    System.out.println("This user was not found please try again.");
                }
            } else if (choice.equalsIgnoreCase("b")) {
                System.out.println("What account type?");
                System.out.println("A.Checking");
                System.out.println("B.Savings");
                System.out.println("C.Credit");
                String type = manInput.nextLine();
                System.out.println("What is the account number?");
                int accountNum = manInput.nextInt();
                if (type.equalsIgnoreCase("A")) {
                    boolean found = false;
                    for (Customer cust : map.values()) {
                        if (cust.getCheckings().getAccountNumber() == accountNum) {
                            printUserInfo(cust);
                            found = true;
                            break;
                        }
                    }
                    if(!found){
                        System.out.println("This user was not found please try again.");
                    }

                }
                if (type.equalsIgnoreCase("b")) {
                    boolean found = false;
                    for (Customer cust : map.values()) {
                        if (cust.getSavings().getAccountNumber() == accountNum) {
                            printUserInfo(cust);
                            found = true;
                            break;
                        }
                    }
                    if(!found){
                        System.out.println("This user was not found please try again.");
                    }

                }
                if (type.equalsIgnoreCase("c")) {
                    boolean found = false;
                    for (Customer cust : map.values()) {
                        if (cust.getCredit().getAccountNumber() == accountNum) {
                            printUserInfo(cust);
                            found = true;
                            break;
                        }
                    }
                    if(!found){
                        System.out.println("This user was not found please try again.");
                    }

                }
            }
            else if(choice.equalsIgnoreCase("c")){
                System.out.println("Thank you! Goodbye.");
                break;
            }
            else if(choice.equalsIgnoreCase("d")){
                for (Customer cust : map.values()) {
                    printUserInfo(cust);
                }
            }
            else if(choice.equalsIgnoreCase("e")){
                readTransactions(map);
            }
            else if(choice.equalsIgnoreCase("f")){
                System.out.println("Who's Bank Statement would you like to generate?");
                Scanner scnr = new Scanner(System.in);
                boolean found = false;
                System.out.println("Who's account would you like to inquire about? (Ex: firstName lastName)");
                String userInq = manInput.nextLine();
                for (Customer cust : map.values()) {
                    if ((cust.getFirstName() + " " + cust.getLastName()).equals(userInq)) {
                        generateBankStatementText(cust.getStatement());
                        found = true;
                        break;
                    }
                }
                if(!found){
                    System.out.println("This user was not found please try again.");
                }

            }
            else{
                System.out.println("This was not a valid input, please try again:");
            }
        }
    }

    /**
     * If the user is not a manager, they will enter this menu method after logging in with their credentials
     * Here they will choose which account to use for the accountMenu method
     * @param bankCustomers the Hashmap of Customer objects
     * @param user the Customer that was entered in the main method
     * @param fileWriter the FileWriter that we will bring over to help write out log
     * @param maxNums the array of max id and bank account numbers
     * @param state the bank statement for that user
     * @throws IOException required for file exception
     */
    //METHOD FOR MENU AND TRAVERSAL
    public static void menu(HashMap<Integer, Customer> bankCustomers,Customer user, FileWriter fileWriter,int[]maxNums,BankStatement state) throws IOException {
        boolean loggedIn = true;
        Scanner input = new Scanner(System.in);


        while (loggedIn) {

            boolean working = false;
            while (!working) {
                System.out.println("Hiya " + user.getFirstName() + "! Which of your accounts would you like to use?");
                System.out.println("1. Checking");
                System.out.println("2. Savings");
                System.out.println("3. Credit");
                System.out.println("4. Open a Checking or Savings account");
                System.out.println("5. None (exit)");
                if (input.hasNextInt()) {
                    working = true;
                } else {
                    input.next();
                    System.out.println("This is not a number. Please try again.");
                }
            }
            int accountChoice = input.nextInt();
            switch (accountChoice) {
                case 1:
                    if(!user.getHasChecking()){
                       System.out.println("You do not have a checking account. Please press 4 to open one.");
                       break;
                    }
                    System.out.println("You have chosen to use your checking account.");
                    accountMenu(bankCustomers,user,user.getCheckings(),fileWriter,state);
                    break;
                case 2:
                    System.out.println("You have chosen to use your savings account.");
                    accountMenu(bankCustomers,user,user.getSavings(),fileWriter,state);
                    break;
                case 3:
                    if(!user.getHasCredit()){
                        System.out.println("You do not have a credit account. Please press 4 to open one.");
                        break;
                    }
                    System.out.println("You have chosen to use your credit account.");
                    accountMenu(bankCustomers,user,user.getCredit(),fileWriter,state);
                    break;
                case 4:
                    System.out.println("Proceeding to create account.");
                    createAccount(user,maxNums);
                    break;
                case 5:
                    System.out.println("Thank you, goodbye!");
                    loggedIn = false;
                    break;
                default:
                    System.out.println("This was not a valid choice, please try again.");
            }
        }
    }

    /**
     *
     * @param bankCustomers the hashmap of Customer objects
     * @param user the user that was signed in through the main method
     * @param account the account that they have chosen from the menu method
     * @param logWriter the FileWriter that we use to write our transaction log
     * @param statement the bank statement for the customer
     * @throws IOException required for file exception
     */
    public static void accountMenu(HashMap<Integer,Customer> bankCustomers,Customer user, Account account,FileWriter logWriter, BankStatement statement) throws IOException {
        Scanner input = new Scanner(System.in);
        boolean loggedIn = true;
        while (loggedIn) {
            boolean working = false;
            while (!working) {
                System.out.println("Okay " + user.getFirstName() + "! Which mouska-tool do you want to use?");
                System.out.println("1. Check Balance");
                System.out.println("2. Deposit Money");
                System.out.println("3. Withdraw Money");
                System.out.println("4. Transfer Money (between your accounts)");
                System.out.println("5. Pay someone (another user)");
                System.out.println("6. Log out");
                if (input.hasNextInt()) {
                    working = true;
                } else {
                    input.next();
                    System.out.println("This is not a number. Please try again.");
                }
            }
            int choice = input.nextInt();
            switch (choice) {
                case 1:
                    System.out.println("Your balance is: $" + account.getBalance());
                    break;
                case 2:
                    System.out.println("How much would you like to deposit? Enter below:");
                    try {
                        double dep = Double.parseDouble(input.next());
                        if (dep <= 0) {
                            System.out.println("Please enter a valid amount. (Only positive amounts please)");
                        } else {
                            if(deposit(account, dep)){
                                System.out.println("Successful deposit of $" +dep);
                                statement.setRecord(statement.getRecord() +"\n" + user.getFirstName() + " deposited $" + dep + " to " + account.getClass().getName() +" .\n");
                                logWriter.write(user.getFirstName() + " deposited $" + dep + " to " + account.getClass().getName() +" .\n");
                                System.out.println("Your balance is now: $" + account.getBalance());
                            }
                            else{
                                System.out.println("Failed to deposit");
                            }
                        }
                    } catch (Exception e) {
                        System.out.println("This is not a number.");
                    }
                    break;
                case 3:
                    System.out.println("How much would you like to withdraw? Enter below:");
                    try {
                        double with = Double.parseDouble(input.next());
                        if(account instanceof Credit) {
                            if (with <= 0) {
                                System.out.println("Please enter a valid amount. (Only positive amounts)");
                            } else {
                                if (withdraw(account, with)) {
                                    System.out.println("Successful withdrawal of $" + with);
                                    statement.setRecord(statement.getRecord() +"\n" +user.getFirstName() + " withdrew $" + with + " from " + account.getClass().getName() + ".\n");
                                    logWriter.write(user.getFirstName() + " withdrew $" + with + " from " + account.getClass().getName() + ".\n");
                                    System.out.println("Your balance is: $" + account.getBalance());
                                } else {
                                    System.out.println("Failed to withdraw");

                                }
                            }
                        }
                        else if (with <= 0 || with > account.getBalance()) {
                            System.out.println("Please enter a valid amount. (Only positive amounts, no greater than your balance please)");
                        } else {
                            if(withdraw(account, with)){
                                System.out.println("Successful withdrawal of $" + with);
                                statement.setRecord(statement.getRecord() +"\n" +user.getFirstName() + " withdrew $" + with + " from " + account.getClass().getName() +".\n");
                                logWriter.write(user.getFirstName() + " withdrew $" + with + " from " + account.getClass().getName() +".\n");
                                System.out.println("Your balance is: $" + account.getBalance());
                            }
                            else{
                                System.out.println("Failed to withdraw");
                            }
                        }
                    } catch (Exception e) {
                        System.out.println("This is not a number.");
                    }
                    break;
                case 4:
                    System.out.println("Which of your accounts would you like to transfer to?");
                    Account accountSecond = null;
                    int secChoice;
                    if(account instanceof Checking){
                        System.out.println("1. Savings");
                        System.out.println("2. Credit");
                        secChoice = input.nextInt();
                        if(secChoice == 1){
                            accountSecond = user.getSavings();
                        }
                        if(secChoice == 2){
                            if(user.getHasCredit()) {
                                accountSecond = user.getCredit();
                            }
                            else{
                                System.out.println("You do not have a credit account.");
                                break;
                            }
                        }
                    }
                    if(account instanceof Savings){
                        if(!user.getHasChecking() && !user.getHasCredit()){
                            System.out.println("You do not have any accounts to transfer to. Please open either a Checking or Credit account.");
                            break;
                        }
                        System.out.println("1. Checking");
                        System.out.println("2. Credit");
                        secChoice = input.nextInt();
                        if(secChoice == 1){
                            if(!user.getHasChecking()) {
                                System.out.println("Sorry, you do not have a checking account.");
                                break;
                            }
                            accountSecond = user.getCheckings();
                        }
                        if(secChoice == 2){
                            if(!user.getHasCredit()) {
                                System.out.println("Sorry, you do not have a credit account.");
                                break;
                            }

                            accountSecond = user.getCredit();
                        }

                    }
                    if(account instanceof  Credit) {
                        System.out.println("1. Checking");
                        System.out.println("2. Savings");
                        secChoice = input.nextInt();
                        if (secChoice == 1) {
                            if(!user.getHasChecking()){
                                System.out.println("Sorry, you do not have a checking account.");
                                break;
                            }
                            accountSecond = user.getCheckings();
                        }
                        if (secChoice == 2) {
                            accountSecond = user.getSavings();
                        }
                    }
                        System.out.println("How much would you like to transfer?");
                        Double amount = input.nextDouble();

                        if(transfer(account,accountSecond,amount)) {
                            System.out.println("Successful transfer of $" + amount);
                            statement.setRecord(statement.getRecord() +"\n" +user.getFirstName() + " deposited $" + amount + " to "+ accountSecond.getClass().getName() +" from "+ account.getClass().getName() +".\n");
                            logWriter.write(user.getFirstName() + " deposited $" + amount + " to "+ accountSecond.getClass().getName() +" from "+ account.getClass().getName() +".\n");
                            System.out.println(accountSecond.getClass().getName() + "now has a balance of $" + accountSecond.getBalance());
                        }
                        else{
                            System.out.println("Failed to transfer");
                        }
                    break;
                case 5:
                    System.out.println("Enter an account number to transfer to:");
                    Customer user2 = getUserFromKey(input, bankCustomers);
                    Checking account2 = user2.getCheckings();
                    System.out.println("How much would you like to transfer to "+ user2.getFirstName() +"? Enter below:");
                    try {
                        double transf = Double.parseDouble(input.next());
                        if(transfer(account, account2, transf)){
                            System.out.println("Successful transfer of $" + transf);
                            statement.setRecord(statement.getRecord() +"\n" +user.getFirstName() + " transferred $" + transf + " from " + account.getClass().getName() +" to " + user2.getFirstName() + ".\n");
                            logWriter.write(user.getFirstName() + " transferred $" + transf + " from " + account.getClass().getName() +" to " + user2.getFirstName() + ".\n");
                            System.out.println("Your balance is: $" + account.getBalance());
                        }
                        else{
                            System.out.println("Failed to transfer.");
                        }
                    } catch (Exception e) {
                        System.out.println("This is not a number.");
                    }
                    break;
                case 6:
                    System.out.println("Thank you. Goodbye!");
                    loggedIn = false;
                    break;
                default:
                    System.out.println("This is not a valid input. Please try again.");
            }
        }
    }
    //END OF MENU METHOD


    //METHODS FOR ACCOUNT ACTIONS

    /**
     * a method called when a deposit is made to adjust the account balance
     * @param account the account that will be deposited into
     * @param amount the amount that the user inputs to deposit
     * @return a boolean that will be used to indicate if the deposit was successful
     */
    public static boolean deposit(Account account, double amount){
        if(account instanceof Credit){
            if(amount > Math.abs(account.getBalance())){
                return false;
            }
        }
        if(amount < 0){
            return false;
        }
        account.setBalance(account.getBalance() + amount);
        return true;
    }

    /**
     * used to adjust account when a user withdraws money
     * @param account the account that will be withdrawn from
     * @param amount the amount the user has specified
     * @return a boolean to indicate if the transaction was successful
     */
    public static boolean withdraw(Account account, double amount){
        if(account instanceof Credit){
            if(amount < 0){
                return false;
            }
            account.setBalance(account.getBalance() - amount);
            return true;
        }
        if(amount < 0){
            return false;
        }
        if(amount > account.getBalance()){
            return false;
        }
        account.setBalance(account.getBalance() - amount);

        return true;

    }

    /**
     * A method to transfer between two accounts, could be the user to another account or user to another user
     * @param account the account that the user has chosen to transfer from
     * @param account2 the account that the user will tranfer to
     * @param amount the amount the user has specified
     * @return a boolean to indicate if the transaction was successful
     */
    public static boolean transfer(Account account, Account account2, double amount){
        if(account instanceof Credit){
            if(amount <0){
                return false;
            }
            account2.setBalance(account2.getBalance() + amount);
            account.setBalance(account.getBalance() - amount);
            return true;
        }
        if(amount < 0 || amount > account.getBalance()){
            System.out.println("You cannot transfer a negative amount or more than the balance.");
            return false;
        }
        if(account2 instanceof Credit) {
            if (amount > Math.abs(account.getBalance()) || amount < 0) {
                return false;
            }
        }
        account2.setBalance(account2.getBalance() + amount);
        account.setBalance(account.getBalance() - amount);
        return true;
    }

    /**
     * will get a Customer from the key entered by the user. The method will ask the user to input a key until a customer is found.
     * @param input a Scanner object to have the user enter the key
     * @param bankCustomers the hashmap of Customer objects
     * @return a Customer that was retrieved from the hashmap using the key
     */
    public static Customer getUserFromKey(Scanner input,HashMap<Integer, Customer> bankCustomers){
        boolean accountFound = false;
        int key = 0;
        while (!accountFound) {
            if(input.hasNextInt()){
                key = input.nextInt();
            }
            else{
                input.next();
                System.out.println("This is not a number. Please try again:");
                continue;
            }
            if (bankCustomers.get(key) != null) {
                accountFound = true;
            } else {
                System.out.println("This account number does not exist, please try another:");
            }
        }
        Customer user = bankCustomers.get(key);
        return user;
    }

    /**
     * Will be used by the manager to print the information from the user they inquire about
     * @param user the Customer the manager has inquired about
     */
    public static void printUserInfo(Customer user){
        System.out.println("Name:" + user.getFirstName() + " " + user.getLastName());
        System.out.println("DOB: " +user.getDateOfBirth());
        System.out.println("ID Number: 000-00-000" + String.valueOf(user.getIdentificationNumber()));
        System.out.println("Address: " +user.getAddress());
        System.out.println("Phone Number: " +user.getPhoneNumber());
        System.out.println("Checking Account Number:" +user.getCheckings().getAccountNumber());
        System.out.println("Checking Account Balance:" +user.getCheckings().getBalance());
        System.out.println("Savings Account Number: " +user.getSavings().getAccountNumber());
        System.out.println("Savings Account Balance: " +user.getSavings().getBalance());
        System.out.println("Credit Account Number: " + user.getCredit().getAccountNumber());
        System.out.println("Credit Account Balance: " +user.getCredit().getBalance());

    }

    /**
     * A method that will read the hashmap to find the largest account numbers for Checking, Savings, Credit, and the ID number
     * @param map the Hashmap of Customers
     * @return an integer array containing the highest account numbers
     */
    public static int[] getHighestAccountNumbers(HashMap<Integer,Customer> map){
        int[] accNum = {0,0,0,0};
        //accNum[0] = Highest Checking Account Num
        //accNum[1] = Highest Savings Account Num
        //accNum[2] = Highest Credit Account Num
        //accNum[3] = Highest Identification Num
        for (Customer cust : map.values()) {
            if(cust.getCredit().getAccountNumber() > accNum[2]){
                accNum[2] = cust.getCredit().getAccountNumber();
            }
            if(cust.getCheckings().getAccountNumber() > accNum[0]){
                accNum[0] = cust.getCheckings().getAccountNumber();
            }
            if(cust.getSavings().getAccountNumber() > accNum[1]){
                accNum[1] = cust.getSavings().getAccountNumber();
            }
            if(cust.getIdentificationNumber() > accNum[3]){
                accNum[3] = cust.getIdentificationNumber();
            }
        }
        return accNum;
    }

    /**
     * A method to write the csv file based on the information currently in the hashmap of customers
     * @param customerHashMap the hashmap of customers
     */
    public static void writeToCSV(HashMap<Integer,Customer> customerHashMap){
        try (PrintWriter writer = new PrintWriter("UpdatedBank.csv")) {

            StringBuilder sb = new StringBuilder();
            sb.append("First Name");
            sb.append(',');
            sb.append("Last Name");
            sb.append(',');
            sb.append("Date of Birth");
            sb.append(',');
            sb.append("Identification Number");
            sb.append(',');
            sb.append("Address");
            sb.append(',');
            sb.append("Phone Number");
            sb.append(',');
            sb.append("Checking Account Number");
            sb.append(',');
            sb.append("Savings Account Number");
            sb.append(',');
            sb.append("Credit Account Number");
            sb.append(',');
            sb.append("Checking Starting Balance");
            sb.append(',');
            sb.append("Savings Starting Balance");
            sb.append(',');
            sb.append("Credit Starting Balance");
            sb.append(",");
            sb.append("Credit Max");
            sb.append('\n');

            for(Customer customer: customerHashMap.values()){
                sb.append(customer.getFirstName());
                sb.append(",");
                sb.append(customer.getLastName());
                sb.append(",");
                sb.append(customer.getDateOfBirth());
                sb.append(",");
                sb.append(customer.getIdentificationNumber());
                sb.append(",");
                sb.append(customer.getAddress());
                sb.append(",");
                sb.append(customer.getPhoneNumber());
                sb.append(",");
                sb.append(customer.getCheckings().getAccountNumber());
                sb.append(",");
                sb.append(customer.getSavings().getAccountNumber());
                sb.append(",");
                sb.append(customer.getCredit().getAccountNumber());
                sb.append(",");
                sb.append(customer.getCheckings().getBalance());
                sb.append(",");
                sb.append(customer.getSavings().getBalance());
                sb.append(",");
                sb.append(customer.getCredit().getBalance());
                sb.append(",");
                sb.append(customer.getCredit().getCreditLimit());
                sb.append('\n');
            }

            writer.write(sb.toString());

            System.out.println("done!");

        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * A method to read a file with transactions and execute them
      * @param mappy  the hashmap of Customers
     * @throws FileNotFoundException throws Exception
     */
    public static void readTransactions(HashMap<Integer,Customer>mappy) throws FileNotFoundException {
        System.out.println("Please input the name of the Transactions file:");
        Scanner scnr = new Scanner(System.in);
        Scanner fileReader = new Scanner(new File(scnr.nextLine()));
        fileReader.nextLine();


        while(fileReader.hasNextLine()){
            String[] transaction = fileReader.nextLine().split(",");
            System.out.println(transaction[3]);
            //Account to be used by user2
            Account account2 = null;
            Customer user1 = customerByName(mappy,transaction[0],transaction[1]);
            Customer user2 = null;
            if(transaction.length >4) {
                user2 = customerByName(mappy, transaction[4], transaction[5]);
                switch(transaction[6]){
                    case("Checking"):
                        account2 = user2.getCheckings();
                        break;
                    case("Savings"):
                        account2 = user2.getSavings();
                        break;
                    case("Credit"):
                        account2 = user2.getCredit();
                        break;
                    default:
                        break;
                }
            }

            Account account1 = null;
            //Account to be used by user1
            switch(transaction[2]){
                case("Checking"):
                    account1 = user1.getCheckings();
                    break;
                case("Savings"):
                    account1 = user1.getSavings();
                    break;
                default:
                    break;
            }

            boolean worked;
            switch (transaction[3]){
                case("pays"):
                    worked = transfer(account1,account2,Double.parseDouble(transaction[7]));
                    if(!worked){
                        System.out.println(user1.getFirstName() +" could not pay " + user2.getFirstName());
                    }
                    break;
                case("transfers"):
                    worked = transfer(account1,account2,Double.parseDouble(transaction[7]));
                    if(!worked){
                        System.out.println(user1.getFirstName()+" "+ account1.toString() +" could not transfer to " + user2.getFirstName() + account2.toString() );
                    }

                    break;
                case("deposits"):
                    deposit(account2,Double.parseDouble(transaction[7]));
                    break;
                case("inquires"):
                    System.out.println(user1.getFirstName() + " " + user1.getLastName() + " inquires their balance of $" + account1.getBalance());
                    break;
                case("withdraws"):
                    worked = withdraw(account1,Double.parseDouble(transaction[7]));
                    if(!worked){
                        System.out.println(user1.getFirstName()+" could not withdraw $" + transaction[7] );
                    }

                    break;
            }
        }
        System.out.println("Actions read successfully. Thank you!");
    }

    /**
     * A method to find a customer by name in a hashmap of customers
     * @param map   the hashmap of customers
     * @param firstName the first name of the customer being searched for
     * @param lastName the last name of the customer being searched for
     * @return a Customer object with the name being looked for
     */
    public static Customer customerByName(HashMap<Integer,Customer> map, String firstName, String lastName){
        Customer crust = new Customer();
        for (Customer cust : map.values()) {
            if((cust.getFirstName() + cust.getLastName()).equals(firstName+lastName)){
                return cust;
            }
        }
        return crust;
    }

    /**
     * A method to generate a bank statement for a user based on the bankStatement class
     * @param state the bankStatement in question
     * @throws IOException throws exception
     */
    public static void generateBankStatementText(BankStatement state) throws IOException {
        File file = new File(state.getCustomer().getFirstName() + state.getCustomer().getLastName()+"BankStatement.txt");
        FileWriter writer = new FileWriter(file);
        writer.write("                        DisneyBank                     \n");
        writer.write(String.valueOf(java.time.LocalDateTime.now()).substring(0,10));
        writer.write("\nName: " + state.getCustomer().getFirstName() + " " + state.getCustomer().getLastName());
        writer.write("\nAddress: " + state.getCustomer().getAddress());
        writer.write("\nPhone Number: " + state.getCustomer().getPhoneNumber());
        writer.write("\nDOB: " + state.getCustomer().getDateOfBirth());
        writer.write("\nID Number: " + state.getCustomer().getIdentificationNumber());
        writer.write("\n");
        writer.write("\n");
        writer.write("Starting Balances: \n");

        writer.write("Savings: $" + state.getCustomer().getSavings().getStartingBalance() +"\n");
        writer.write("Checking: $" + state.getCustomer().getCheckings().getStartingBalance() + "\n");
        writer.write("Credit: $" + state.getCustomer().getCredit().getStartingBalance() + "\n");
        writer.write("\n");

        writer.write("Ending Balances:\n");
        writer.write("Savings: $" + state.getCustomer().getSavings().getBalance() +"\n");
        writer.write("Checking: $" + state.getCustomer().getCheckings().getBalance() + "\n");
        writer.write("Credit: $" + state.getCustomer().getCredit().getBalance() + "\n");

        writer.write(state.getRecord());
        writer.close();

        System.out.println("done generating!");


    }

}
