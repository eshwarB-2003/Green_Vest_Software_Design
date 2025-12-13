package com.greenvest.view;

import com.greenvest.controller.SellerController;
import com.greenvest.model.Credit;
import com.greenvest.model.SustainabilityAction;
import com.greenvest.model.User;

import java.util.List;
import java.util.Scanner;
import java.util.UUID;

public class SellerView {

    private SellerController controller;
    private Scanner sc = new Scanner(System.in);

    public SellerView(SellerController controller) {
        this.controller = controller;
    }

    public void showDashboard(User seller) {

        while (true) {
            System.out.println("\n===== SELLER DASHBOARD =====");
            System.out.println("1. Submit Sustainability Action");
            System.out.println("2. View My Credits");
            System.out.println("3. List My Credits");
            System.out.println("4. Logout");

            int ch = sc.nextInt();
            sc.nextLine();

            if (ch == 1) submitAction(seller);
            else if (ch == 2) viewMyCredits(seller);
            else if (ch == 3)  listCredits(seller);
            else return;
        }
    }

    private void submitAction(User seller) {
        System.out.print("Action Type (TREE/SOLAR): ");
        String type = sc.nextLine();

        System.out.print("Metric Value: ");
        double metric = sc.nextDouble();
        sc.nextLine();

        SustainabilityAction action =
                new SustainabilityAction(
                        UUID.randomUUID().toString(),
                        seller.getEmail(),
                        type,
                        metric
                );

        controller.submitAction(seller, action);
        System.out.println("Action submitted. Pending admin approval.");
    }

    private void viewMyCredits(User seller) {
        List<Credit> credits = controller.getMyCredits(seller);
        //  List<Credit> credits = controller.viewCredits(seller);

        if (credits == null || credits.isEmpty()) {
            System.out.println("No credits found.");
            return;
        }

        System.out.println("\n===== MY GENERATED CREDITS =====");

        for (int i = 0; i < credits.size(); i++) {
            Credit c = credits.get(i);

            System.out.println(
                    i + ". ID: " + c.getId() +
                            " | Qty: " + c.getQuantity() +
                            " | Price: " + c.getPrice() +
                            " | Expiry: " + c.getExpiry() +
                            " | State: " + c.getState()
            );
        }
    }
    private void generateCredits(User seller, SustainabilityAction action) {

        Credit credit = controller.generateCredits(seller, action);

        if (credit != null) {
            System.out.println("Credits generated successfully!");
            System.out.println("Quantity: " + credit.getQuantity());
            System.out.println("Status: " + credit.getState());
        }
    }
    private void listCredits(User seller) {

            //  Get seller’s generated credits
            List<Credit> credits = controller.getMyCredits(seller);

            if (credits == null || credits.isEmpty()) {
                System.out.println("No credits available to list.");
                return;
            }

            //  Show credits
            for (int i = 0; i < credits.size(); i++) {
                Credit c = credits.get(i);
                System.out.println(
                        i + ". ID: " + c.getId() +
                                " | Qty: " + c.getQuantity() +
                                " | Price: " + c.getPrice() +
                                " | State: " + c.getState()
                );
            }

            //  Select credit to list
            System.out.print("Select credit index to list: ");
            int index = sc.nextInt();
            sc.nextLine();

            Credit selectedCredit = credits.get(index);


            boolean listed = controller.listCredit(seller, selectedCredit);

            if (listed) {
                System.out.println(" Credit listed successfully");
            } else {
                System.out.println(" Credit listing failed");
            }
        }

    }

