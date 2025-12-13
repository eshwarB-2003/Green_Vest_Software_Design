package com.greenvest.view;
import com.greenvest.config.SystemConfig;
import com.greenvest.controller.AdminController;
import com.greenvest.model.Activity;
import com.greenvest.model.SustainabilityAction;
import com.greenvest.model.User;
import com.greenvest.service.*;

import java.util.List;
import java.util.Scanner;
public class AdminView {

    private AdminController controller;
    private Scanner sc = new Scanner(System.in);

    public AdminView(AdminController controller) {
        this.controller = controller;
    }

    public void showDashboard(User admin) {

        while (true) {

            System.out.println("\n===== ADMIN DASHBOARD =====");
            System.out.println("1. View Pending Seller Actions");
            System.out.println("2. View Approved Actions");
            System.out.println("3. View Rejected Actions");
            System.out.println("4. Set Minimum Price");
            System.out.println("5. View System Activities");
            System.out.println("6. Logout");

            System.out.print("Select option: ");
            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1 -> viewPendingActions(admin);
                case 2 -> viewApprovedActions(admin);
                case 3 -> viewRejectedActions(admin);
                case 4 -> setMinimumPrice(admin);
                case 5 -> viewActivities();
                case 6 -> {
                    System.out.println("Logging out...");
                }
                default -> System.out.println("Invalid option.");
            }
        }
    }

    private void viewApprovedActions(User admin) {
        List<SustainabilityAction> list = controller.viewApproved(admin);

        if (list == null || list.isEmpty()) {
            System.out.println("No approved actions.");
        }

        System.out.println("===== APPROVED ACTIONS =====");
        list.forEach(a ->
                System.out.println(
                        "Seller: " + a.getSellerEmail() +
                                " | Type: " + a.getType() +
                                " | Metric: " + a.getMetricValue()
                )
        );
    }





    private void viewRejectedActions(User admin) {
        List<SustainabilityAction> list = controller.viewRejected(admin);

        if (list == null || list.isEmpty()) {
            System.out.println("No rejected actions.");

        }

        System.out.println("===== REJECTED ACTIONS =====");
        list.forEach(a ->
                System.out.println(
                        "Seller: " + a.getSellerEmail() +
                                " | Type: " + a.getType() +
                                " | Metric: " + a.getMetricValue()
                )
        );
    }

    private void viewActivities() {

        ActivityService service = new ActivityService();
        List<Activity> list = service.getAllActivities();

        if (list.isEmpty()) {
            System.out.println("No activities found.");
            return;
        }

        System.out.println("\n===== SYSTEM ACTIVITY LOG =====");
        for (Activity a : list) {
            System.out.println(
                    a.getTimestamp() + " | " +
                            a.getPerformedBy() + " | " +
                            a.getMessage()
            );
        }
    }

    private void setMinimumPrice(User admin) {

        System.out.print("Enter minimum credit price: ");
        double price = sc.nextDouble();
        sc.nextLine();

        if (price <= 0) {
            System.out.println("Invalid price.");
        }

        controller.setMinPrice(admin, price);

        System.out.println("Minimum credit price set to: " + price);
    }



    private void viewPendingActions(User admin) {

        List<SustainabilityAction> actions =
                controller.viewPending(admin);

        if (actions == null || actions.isEmpty()) {
            System.out.println("No pending seller actions.");
        }

        System.out.println("\n===== PENDING SELLER ACTIONS =====");

        for (int i = 0; i < actions.size(); i++) {
            SustainabilityAction a = actions.get(i);
            System.out.println(
                    i + ". Seller: " + a.getSellerEmail()
                            + " | Type: " + a.getType()
                            + " | Metric: " + a.getMetricValue()
            );
        }

        System.out.print("\nSelect action index (-1 to cancel): ");
        int index = sc.nextInt();
        sc.nextLine();

        if (index == -1) {
            System.out.println("Operation cancelled.");
            return;
        }

        SustainabilityAction selected = actions.get(index);

        System.out.println("\n1. Approve");
        System.out.println("2. Reject");
        System.out.println("0. Cancel");
        System.out.print("Enter choice: ");

        int choice = sc.nextInt();
        sc.nextLine();

        switch (choice) {
            case 1 -> {
                controller.approve(admin, selected);
                System.out.println(" Action approved and credits generated.");
            }
            case 2 -> {
                controller.reject(admin, selected);
                System.out.println("  Action rejected.");
            }
            case 0 -> System.out.println("Operation cancelled.");
            default -> System.out.println("Invalid option.");
        }
    }

}