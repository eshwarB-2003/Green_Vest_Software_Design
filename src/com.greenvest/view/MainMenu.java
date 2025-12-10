package com.greenvest.view;

import java.util.Scanner;
import com.greenvest.service.AuthService;
import com.greenvest.controller.*;
import com.greenvest.model.User;
import com.greenvest.service.BuyerService;
import com.greenvest.rules.*;
import com.greenvest.view.BuyerView;

public class MainMenu {

    private AuthService auth = new AuthService();

    public void start() {
        Scanner sc = new Scanner(System.in);
   while (true) {
        System.out.println("Select Role:");
        System.out.println("1. Admin");
        System.out.println("2. Seller");
        System.out.println("3. Buyer");
        int roleOption = sc.nextInt(); sc.nextLine();

        String role = switch (roleOption) {
            case 1 -> "ADMIN";
            case 2 -> "SELLER";
            case 3 -> "BUYER";
            default -> throw new IllegalArgumentException("Invalid role");
        };

        System.out.println("1. Register");
        System.out.println("2. Login");
        int option = sc.nextInt(); sc.nextLine();

        if (option == 1) {
            System.out.print("Enter SME Name: ");
            String name = sc.nextLine();
            System.out.print("Enter Company Email: ");
            String email = sc.nextLine();
            System.out.print("Enter Password: ");
            String pass = sc.nextLine();

            boolean success = auth.register(name, email, pass, role);
            System.out.println(success ? "Registration Successful" : "User already exists!");
        }

        if (option == 2) {
            System.out.print("Email: ");
            String email = sc.nextLine();
            System.out.print("Password: ");
            String pass = sc.nextLine();

            User user = auth.login(email, pass);

            if (user == null) {
                System.out.println(" Invalid Login !!! ");
                return;
            }

            switch (user.getRole()) {
                case "ADMIN" -> new AdminController().openDashboard(user);
                case "SELLER" -> new SellerController().openDashboard(user);

                case "BUYER" -> {

                    RuleEngineService engine = new RuleEngineService();
                    engine.registerRule(new ComplianceRule());
                    // Add more rules if needed (expiry, depreciation)

                    // SERVICE + CONTROLLER + VIEW
                    BuyerService service = new BuyerService(engine);
                    BuyerController controller = new BuyerController(service);
                    BuyerView view = new BuyerView(controller);

                    view.showDashboard(user);
                   }
                }
            }
        }
    }

    public static void main(String[] args) {
        new MainMenu().start();
    }
}
