package org.example;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class FileHandler {

    private static String transactionPath = "src/main/resources/transactions.csv";

    public FileHandler() {
        this.transactionPath = transactionPath;
    }

    public static List<Transactions> loadTransactions() {
        try{
            List<String> lines = Files.readAllLines(Path.of(transactionPath)); //Grabs all transactions from transactions.csv
            List<Transactions> list = new ArrayList<>(); //

            for(String line : lines){
                String[] parts = line.split("\\|");
                list.add(new Transactions(
                        parts[0],
                        parts[1],
                        parts[2],
                        parts[3],
                        Double.parseDouble(parts[4])));
            }
            return list;
        }
        catch (FileNotFoundException ex) {
            System.out.println("File not found!");
        }
        catch (IOException e) {
            System.out.println("IO Exception!");
        }
        return null;
    }

    public static List<Transactions> loadDeposits() {
        return loadTransactions().stream()
                .filter(t -> t.getAmount() >0)
                .toList();
    }

    public static List<Transactions> loadPayments() {
        return loadTransactions().stream()
                .filter(t -> t.getAmount() <0)
                .toList();
    }

    public static List<Transactions> reportVendor(String vendor){
        return loadTransactions().stream()
                .filter(t -> t.getVendor().toLowerCase().contains(vendor.toLowerCase()))
                .toList();
    }

    public static List<Transactions> reportMonthToDay() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate today = LocalDate.now();
        LocalDate startDate = today.withDayOfMonth(1);

        List<Transactions> grabTransaction = loadTransactions().stream()
                .filter(t -> {
                    LocalDate transactionDate = LocalDate.parse(t.getDate(), formatter);
                    return (!transactionDate.isBefore(startDate) && !transactionDate.isAfter(today));
                })
                .toList();
        return grabTransaction;
    }

    public static List<Transactions> reportLastMonth() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate today = LocalDate.now();
        LocalDate startDate = today.minusMonths(1).withDayOfMonth(1);
        LocalDate endDate = today.withDayOfMonth(1).minusDays(1);


        List<Transactions> grabTransaction = loadTransactions().stream()
                .filter(t -> {
                    LocalDate transactionDate = LocalDate.parse(t.getDate(), formatter);
                    return (!transactionDate.isBefore(startDate) && !transactionDate.isAfter(endDate));
                })
                .toList();
        return grabTransaction;
    }

    public static List<Transactions> reportYear() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate today = LocalDate.now();
        LocalDate startDate = today.withDayOfYear(1);


        List<Transactions> grabTransaction = loadTransactions().stream()
                .filter(t -> {
                    LocalDate transactionDate = LocalDate.parse(t.getDate(), formatter);
                    return (!transactionDate.isBefore(startDate) && !transactionDate.isAfter(today));
                })
                .toList();
        return grabTransaction;
    }

    public static List<Transactions> reportLastYear() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate today = LocalDate.now();
        LocalDate startDate = today.minusYears(1).withDayOfYear(1);
        LocalDate endDate = today.withDayOfYear(1).minusDays(1);


        List<Transactions> grabTransaction = loadTransactions().stream()
                .filter(t -> {
                    LocalDate transactionDate = LocalDate.parse(t.getDate(), formatter);
                    return (!transactionDate.isBefore(startDate) && !transactionDate.isAfter(endDate));
                })
                .toList();
        return grabTransaction;
    }

    public void writeTransaction(String output) {
        output += System.lineSeparator();

        try(FileWriter fw = new FileWriter(transactionPath, true)){
            fw.write(output);
        }
        catch (FileNotFoundException ex) {
                System.out.println("File not found!");
            }
        catch (IOException e) {
                System.out.println("IO Exception!");
            }
    }
}
