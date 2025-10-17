package org.example;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Controller {
    private final LocalDate localDate;
    private final LocalTime localTime;
    private final Scanner scanner;

    private final FileHandler fileHandler;

    public Controller() {
        this.localDate = LocalDate.now();
        this.localTime = LocalTime.now();
        this.scanner = new Scanner(System.in);

        this.fileHandler = new FileHandler();

    }

    public void Start(){
        String userInput;

        System.out.println("*** Welcome to Acledge ***");

        while(true){
            System.out.println(); //Break
            System.out.println("---------------------------------------------------------------------");
            System.out.println("                          ~~~ MAIN MENU ~~~                          ");
            System.out.println("---------------------------------------------------------------------");
            System.out.println(); //Break

            System.out.println("Would you like to:");            System.out.println("D) Add Deposit");
            System.out.println("P) Make Payment (Debit)");
            System.out.println("L) Ledger");
            System.out.println("X) Exit Application");
            userInput = scanner.nextLine().toLowerCase().trim();

            switch (userInput){
                case "d":
                    addDeposit();
                    break;
                case "p":
                    makePayment();
                    break;
                case "l":
                    showLedger();
                    break;
                case "x":
                    System.out.println();
                    System.out.println("*** Goodbye! ***");
                    System.exit(0);
                default:
                    System.out.println("Please enter 'D', 'P', 'L', or 'X'.");
                    break;
            }
        }
    }

    private void addDeposit() {
        String userInput;
        String vendor;
        String description;
        String amount = "";

        System.out.println(); //Break
        System.out.println("---------------------------------------------------------------------");
        System.out.println("                         ~~~ ADD DEPOSIT ~~~                         ");
        System.out.println("---------------------------------------------------------------------");
        System.out.println(); //Break

        while(true){
            System.out.println(); //Break

            System.out.println("Who is the recipient? Enter 'X' to go back");
            userInput = scanner.nextLine().trim();
            if(!userInput.equalsIgnoreCase("x"))
            {
                vendor = userInput;
            }
            else {
                break;
            }
            System.out.println("What is it for? Enter 'X' to go back");
            userInput = scanner.nextLine().trim();
            if(!userInput.equalsIgnoreCase("x"))
            {
                description = userInput;
            }
            else {
                break;
            }

            while(true){
                try{
                    System.out.println("What is the amount that you're depositing? Enter 'X' to go back");
                    userInput = scanner.nextLine().trim().replace("$","");
                    if(!userInput.equalsIgnoreCase("x"))
                    {
                        amount = String.format("%.2f", Double.parseDouble(userInput));
                    }
                    break;
                }
                catch (NumberFormatException ex) {
                    System.out.println("Please input a correct number");
                }
            }
            if(userInput.equalsIgnoreCase("x"))
                break;
            else{
                writeTransaction(vendor, description, "-" + amount);
            }
            break;
        }
    }

    private void makePayment() {
        String userInput;
        String vendor;
        String description;
        String amount = "";

        System.out.println(); //Break
        System.out.println("---------------------------------------------------------------------");
        System.out.println("                        ~~~ MAKE PAYMENT ~~~                         ");
        System.out.println("---------------------------------------------------------------------");
        System.out.println(); //Break

        while(true){
            System.out.println(); //Break

            System.out.println("Who is the recipient? Enter 'X' to go back");
            userInput = scanner.nextLine().trim();
            if(!userInput.equalsIgnoreCase("x"))
            {
                vendor = userInput;
            }
            else {
                break;
            }
            System.out.println("What is it for? Enter 'X' to go back");
            userInput = scanner.nextLine().trim();
            if(!userInput.equalsIgnoreCase("x"))
            {
                description = userInput;
            }
            else {
                break;
            }

            while(true){
                try{
                    System.out.println("What is the amount that you're paying? Enter 'X' to go back");
                    userInput = scanner.nextLine().trim().replace("$","");
                    if(!userInput.equalsIgnoreCase("x"))
                    {
                        amount = String.format("%.2f", Double.parseDouble(userInput));
                    }
                    break;
                }
                catch (NumberFormatException ex) {
                    System.out.println("Please input a correct number");
                }
            }
            if(userInput.equalsIgnoreCase("x"))
                break;
            else{
                writeTransaction(vendor, description, "-" + amount);
            }
            break;
        }
    }

    private void writeTransaction(String vendor, String description, String amount) {
        String userInput;
        System.out.println("Does this look correct (Y/N)?");
        System.out.println(vendor + " | " + description + " | $" + amount);
        userInput = scanner.nextLine().toLowerCase().trim();
        switch (userInput){
            case "y":
                System.out.println("Finalizing Transaction!");
                String output = localDate.now().toString() + "|" +
                        localTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")).toString() + "|" +
                        description + "|" +
                        vendor + "|" +
                        amount;

                fileHandler.writeTransaction(output);
                break;
            case "n":
                System.out.println("Cancelling Transaction.");
                break;
        }
    }

    private void showLedger() {
        String userInput;

        boolean loopCheck = true;
        while(loopCheck){
            System.out.println(); //Break
            System.out.println("---------------------------------------------------------------------");
            System.out.println("                           ~~~ LEDGER ~~~                            ");
            System.out.println("---------------------------------------------------------------------");
            System.out.println(); //Break
            System.out.println("Would you like to:");
            System.out.println("A) All");
            System.out.println("D) Deposits");
            System.out.println("P) Payments");
            System.out.println("R) Reports");
            System.out.println("X) Go Back");

            userInput = scanner.nextLine().toLowerCase().trim();


            switch(userInput){
                case "a":
                    printTransaction(fileHandler.loadTransactions(), "Transactions");
                    break;
                case "d":
                    printTransaction(fileHandler.loadDeposits(), "Deposits");
                    break;
                case "p":
                    printTransaction(fileHandler.loadPayments(), "Payments");
                    break;
                case "r":
                    showReports();
                    break;
                case "x":
                    loopCheck = false;
                    break;
                default:
                    System.out.println("Please enter 'A', 'D', 'P', 'R', or 'X'.");
                    break;
            }
        }
    }

    private void showReports() {
        String userInput;

        boolean loopCheck = true;
        while(loopCheck){
            System.out.println("---------------------------------------------------------------------");
            System.out.println("                          ~~~ REPORTS ~~~                            ");
            System.out.println("---------------------------------------------------------------------");
            System.out.println("Would you like to:");
            System.out.println("1) Month To Date");
            System.out.println("2) Previous Month");
            System.out.println("3) Year To Date");
            System.out.println("4) Previous Year");
            System.out.println("5) Search by Vendor");
            System.out.println("X) Go Back");

            userInput = scanner.nextLine().toLowerCase().trim();


            switch(userInput){
                case "1":
                    printReport(fileHandler.reportMonthToDay(), "Month To Date");
                    break;
                case "2":
                    printReport(fileHandler.reportLastMonth(), "Previous Month");
                    break;
                case "3":
                    printReport(fileHandler.reportYear(), "Year To Date");
                    break;
                case "4":
                    printReport(fileHandler.reportLastYear(), "Previous Year");
                    break;
                case "5":
                    System.out.println("Please enter the vendor");
                    userInput = scanner.nextLine().toLowerCase().trim();
                    printReport(fileHandler.reportVendor(userInput), "Filter Vendor");
                    break;
                case "x":
                    loopCheck = false;
                    break;
                default:
                    System.out.println("Please enter '1', '2', '3', '4', '5', or 'X'.");
                    break;
            }
        }
    }

    private void printTransaction(List<Transactions> list, String title) {
        System.out.println("---------------------------------------------------------------------");
        System.out.println("~~~ " + title + "~~~");
        System.out.println("---------------------------------------------------------------------");
        System.out.println("Date         Time     Description          Vendor             Amount");
        System.out.println("---------------------------------------------------------------------");

        for(Transactions t : list){
            System.out.println(t);
        }
        System.out.println("---------------------------------------------------------------------");

        System.out.println(); //Break
        System.out.println("* Press Enter to continue");
        scanner.nextLine();
    }

    private void printReport(List<Transactions> list, String title) {
        System.out.println("---------------------------------------------------------------------");
        System.out.println("~~~ " + title + "~~~");
        System.out.println("---------------------------------------------------------------------");
        System.out.println("Date         Time     Description          Vendor             Amount");
        System.out.println("---------------------------------------------------------------------");
        double sum = 0;

        for(Transactions t : list){
            System.out.println(t);
            sum += t.getAmount();

        }
        System.out.println("---------------------------------------------------------------------");
        System.out.printf("Total: $%.2f%n", sum);

        System.out.println(); //Break
        System.out.println("* Press Enter to continue");
        scanner.nextLine();
    }



}
