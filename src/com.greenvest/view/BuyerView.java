package com.greenvest.view;
import com.greenvest.controller.BuyerController;
import com.greenvest.model.Credit;
import com.greenvest.model.Receipt;
import com.greenvest.model.User;
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

            if (choice == 1) viewAvailableCredits(buyer);
            else if (choice == 2) handlePurchase(buyer);
            else if (choice == 3) showPortfolio(buyer);
            else if (choice == 4) showReceipt(buyer);
            else if (choice == 5) showAccountSummary(buyer);
            else if (choice <= 0 || choice > 6) System.out.println("Invalid choice, Try Again !");
            else { System.out.println("Logging out !!!.. "); return; }
        }
    }

  private List<Credit> lastMarketplace = new ArrayList<>();

    private void viewAvailableCredits(User buyer) {
        lastMarketplace = controller.viewMarketplace(buyer);

        if (lastMarketplace == null || lastMarketplace.isEmpty()) {
            System.out.println("No credits available.");
            showDashboard(buyer);

        }

        System.out.println("\n===== AVAILABLE CREDITS =====");
        for (int i = 0; i < lastMarketplace.size(); i++) {
            Credit c = lastMarketplace.get(i);
            System.out.println(
                    i + ". Seller: " + c.getSellerEmail() +
                            " | Qty: " + c.getQuantity() +
                            " | Price: " + c.getPrice()
            );
        }
        showDashboard(buyer);
    }

    private void handlePurchase(User buyer) {

        if (lastMarketplace == null || lastMarketplace.isEmpty()) {
            System.out.println("No credits loaded. View marketplace first.");
             showDashboard(buyer);

        } else {
            System.out.print("Select credit index: ");
            int index = sc.nextInt();
            sc.nextLine();

            if (index < 0 || index >= lastMarketplace.size()) {
                System.out.println("Invalid selection.");
            }

            Credit selected = lastMarketplace.get(index);

            System.out.print("Quantity: ");
            int qty = sc.nextInt();
            sc.nextLine();

            Receipt receipt = controller.purchase(buyer, selected, qty);

            if (receipt != null) {
                System.out.println(" Purchase successful!");
                System.out.println("Receipt ID: " + receipt.getId());
                System.out.println("Total Amount: " + receipt.getTotalCost());

            } else {
                System.out.println(" Purchase failed.");

            }
        }
    }

    private void showPortfolio(User buyer) {
        List<Credit> portfolio = controller.viewPortfolio(buyer);

        if (portfolio == null || portfolio.isEmpty()) {
            System.out.println("No credits in your portfolio.");
            showDashboard(buyer);
        }


        for (Credit c : portfolio) {
            c.updateState();  // State Pattern
            System.out.println(
                    c.getId() + " | Qty: " + c.getQuantity() +
                            " | Price: " + c.getPrice() +
                            " | Status: " + c.getState()
            );
        }
    }
    private void showReceipt(User buyer) {
        System.out.println("\n===== YOUR RECEIPTS =====");
        List<Receipt> receipts = controller.showReceipts(buyer);



    }
    private void showAccountSummary(User buyer) {

        Map<String, Object> summary = controller.getAccountSummary(buyer);

        if (summary == null) {
            System.out.println("Unable to load summary.");
        }

        System.out.println("\n===== ACCOUNT SUMMARY =====");
        System.out.println("Buyer: " + buyer.getName());
        System.out.println("Available Balance: €" + summary.get("balance"));
        System.out.println("Total Credits Owned: " + summary.get("totalCredits"));
        System.out.println("Active Credits: " + summary.get("activeCredits"));
        System.out.println("Expired Credits: " + summary.get("expiredCredits"));
    }
}

