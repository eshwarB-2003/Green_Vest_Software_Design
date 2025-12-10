package com.greenvest.view;
import com.greenvest.controller.BuyerController;
import com.greenvest.model.Credit;
import com.greenvest.model.Receipt;
import com.greenvest.model.User;
import com.greenvest.patterns.decorator.*;
import java.util.*;

import java.util.List;
import java.util.Scanner;

public class BuyerView {

    private BuyerController controller;
    private Scanner sc = new Scanner(System.in);

    public BuyerView(BuyerController controller) {
        this.controller = controller;
    }

    public void showDashboard(User buyer) {

        while (true) {

            System.out.println("\n===== BUYER DASHBOARD =====");
            System.out.println("Welcome, " + buyer.getName());
            System.out.println("1. View Available Credits");
            System.out.println("2. Purchase Credits");
            System.out.println("3. View Portfolio");
            System.out.println("4. View Receipts");
            System.out.println("5. View Account Summary");

            System.out.println("6. Logout");
            System.out.print("Enter choice: ");

            int choice = sc.nextInt();
            sc.nextLine();

            if (choice == 1) showAvailable(buyer);
            else if (choice == 2) handlePurchase(buyer);
            else if(choice == 3) showPortfolio(buyer);
            else if(choice == 4) showReceipt(buyer);
            else if(choice == 5 ) showAccountSummary(buyer);
            else return;
        }
    }

    private void showAvailable(User buyer) {
        List<Credit> list = controller.getAvailableCredits(buyer);
        if (list == null || list.isEmpty()) {
            System.out.println("No credits available.");
            return;
        }

        for (Credit c : list) {
            System.out.println(c.getId() + " | Qty: " + c.getQuantity() +
                    " | Price: " + c.getPrice() +
                    " | Expiry: " + c.getExpiry());
        }
    }

    private void handlePurchase(User buyer) {
        System.out.print("Enter Credit ID: ");
        String id = sc.nextLine();

        System.out.print("Quantity: ");
        int qty = sc.nextInt();
        sc.nextLine();

        Credit selected = controller.getAvailableCredits(buyer)
                .stream().filter(c -> c.getId().equals(id)).findFirst().orElse(null);

        if (selected == null) {
            System.out.println("Invalid Credit ID");
            return;
        }

        boolean ok = controller.purchase(buyer, selected, qty);

        System.out.println(ok ? "Purchase successful!" : "Purchase failed.");
    }

    private void showPortfolio(User buyer) {
        List<Credit> portfolio = controller.getPortfolio(buyer);

        if (portfolio == null || portfolio.isEmpty()) {
            System.out.println("No credits in your portfolio.");
            return;
        }

        for (Credit c : portfolio) {
            c.updateState();  // State Pattern
            System.out.println(
                    c.getId() + " | Qty: " + c.getQuantity() +
                            " | Price: " + c.getPrice() +
                            " | Status: " + c.getState().getStateName()
            );
        }
    }
    private void showReceipt(User buyer) {
        List<Receipt> receipts = controller.showReceipts(buyer);

        System.out.println("\n===== YOUR RECEIPTS =====");

    }
    private void showAccountSummary(User buyer) {

        Map<String, Object> summary = controller.getAccountSummary(buyer);

        if (summary == null) {
            System.out.println("Unable to load summary.");
            return;
        }

        System.out.println("\n===== ACCOUNT SUMMARY =====");
        System.out.println("Buyer: " + buyer.getName());
        System.out.println("Available Balance: €" + summary.get("balance"));
        System.out.println("Total Credits Owned: " + summary.get("totalCredits"));
        System.out.println("Active Credits: " + summary.get("activeCredits"));
        System.out.println("Expired Credits: " + summary.get("expiredCredits"));
    }



}

