
package com.greenvest.view;
import java.util.Scanner;
 import com.greenvest.util.InputValidator;
import com.greenvest.repo.*;
import com.greenvest.service.AdminService;
import com.greenvest.service.AuthService;
import com.greenvest.controller.*;
import com.greenvest.model.User;
import com.greenvest.service.BuyerService;
import com.greenvest.rules.*;
import com.greenvest.service.SellerService;

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

            String role = null;
            switch (roleOption) {
                case 1 -> role = "ADMIN";
                case 2 -> role = "SELLER";
                case 3 -> role = "BUYER";
                default -> {
                    System.out.println("Invalid role selection,Try again.");
                    continue;
                }
            };

            System.out.println("1. Register");
            System.out.println("2. Login");
            int option = sc.nextInt(); sc.nextLine();

            if (option == 1) {
                System.out.print("Enter SME Name: ");
                String name = sc.nextLine();
                System.out.print("Enter Company Email: ");
                String email = sc.nextLine();
                    if (!InputValidator.isValidEmail(email)) {
                        System.out.println("Invalid email format.");
                    }

                    System.out.print("Enter Password: ");
                    String pass = sc.nextLine();
                    if (!InputValidator.isValidPassword(pass)) {
                        System.out.println(
                                "Password must be at least 6 characters and contain letters and numbers."
                        );
                        continue;
                    }


                boolean success = auth.register(name, email, pass, role);
                System.out.println(success ? "Registration Successful" : "User already exists!");
            }

            if (option == 2) {
                System.out.print("Email: ");
                String email = sc.nextLine();
                if (!InputValidator.isValidEmail(email)) {
                    System.out.println("Invalid email format.");
                    continue;
                }

                System.out.print("Password: ");
                String pass = sc.nextLine();
                if (!InputValidator.isValidPassword(pass)) {
                    System.out.println(
                            "Password must be at least 6 characters and contain letters and numbers."
                    );
                    continue;
                }

                User user = auth.login(email, pass);


                if (user == null) {
                    System.out.println(" Invalid Login !!! ");
                    continue;
                }

                switch (user.getRole()) {
                    case "ADMIN" -> {

                        ActionRepository actionRepo = ActionRepositoryJSON.getInstance();
                        CreditRepository creditRepo = CreditRepositoryJSON.getInstance();

                        AdminService adminService =
                                new AdminService(actionRepo, creditRepo);

                        AdminController adminController =
                                new AdminController(adminService);

                        AdminView adminView =
                                new AdminView(adminController);

                        adminView.showDashboard(user);
                    }
                    case "SELLER" -> {

                        ActionRepository actionRepo = ActionRepositoryJSON.getInstance();
                        CreditRepository creditRepo = CreditRepositoryJSON.getInstance();


                        RuleEngineService ruleEngine = new RuleEngineService();
                        ruleEngine.registerRule(new ListingRule());
                        ruleEngine.registerRule(new MinPriceRule());


                        // Service
                        SellerService sellerService =
                                new SellerService(actionRepo, creditRepo, ruleEngine);

                        //  Controller
                        SellerController sellerController =
                                new SellerController(sellerService);

                        //  View
                        SellerView sellerView =
                                new SellerView(sellerController);

                        sellerView.showDashboard(user);
                    }

                    case "BUYER" -> {

                        // Rule Engine
                        RuleEngineService engine = new RuleEngineService();
                        engine.registerRule(new ComplianceRule());


// Repositories (SINGLETONS)
                        CreditRepository creditRepo = CreditRepositoryJSON.getInstance();
                        ReceiptRepository receiptRepo = ReceiptRepositoryJSON.getInstance();
                        PortfolioRepository portfolioRepo = PortfolioRepositoryJSON.getInstance();

// Service
                        BuyerService service =
                                new BuyerService(creditRepo, receiptRepo, portfolioRepo, engine);

// Controller + View
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
