package com.greenvest.controller;
// vaitheeshwar
import com.greenvest.model.Receipt;
import com.greenvest.patterns.decorator.*;
import com.greenvest.patterns.interceptor. *;
import com.greenvest.service.BuyerService;
import com.greenvest.model.Credit;
import com.greenvest.model.User;

import java.util.List;
import java.util.Map;

/* Enforces security checks using Interceptor Pattern
*  Delegates business logic to BuyerService */

public class BuyerController {
    // Interceptor manager handles cross-cutting concerns
    // such as authentication and role validation
    private InterceptorManager interceptorManager;
    // Business logic layer dependency
    private BuyerService buyerService;
    /*  Injects BuyerService
        Registers interceptors for security enforcement */
    public BuyerController(BuyerService buyerService) {
        this.buyerService = buyerService;
        interceptorManager = new InterceptorManager();
        interceptorManager.addInterceptor(new AuthenticationInterceptor());
        interceptorManager.addInterceptor(new RoleInterceptor("BUYER"));
    }

  /*  public List<Credit> getAvailableCredits(User buyer) {
        if (!interceptorManager.execute(buyer.getEmail(), buyer.getRole()))
            return null;

        return buyerService.loadAvailableCredits();
    }
*/
    /*
     1.  Handles credit purchase request from BuyerView

     * @param buyer logged-in buyer
     * @param credit selected credit
     * @param qty quantity to purchase
     * @return Receipt if purchase succeeds, null otherwise
     */
    public Receipt purchase(User buyer, Credit credit, int qty) {
        // Execute interceptor chain before processing
        if (!interceptorManager.execute(buyer.getEmail(), buyer.getRole()))
            return null;

        return buyerService.processPurchase(buyer, credit, qty);
    }

    public List<Receipt> showReceipts(User buyer) {
        List<Receipt> receipts = buyerService.getReceipts(buyer);

        if (receipts == null || receipts.isEmpty()) {
            System.out.println("No receipts found.");
        }


        // decorator pattern applied to receipt printing
        ReceiptPrinter printer =
                new FooterDecorator(
                        new HeaderDecorator(
                                new BasicReceiptPrinter()
                        )
                );
       // List<Credit> portfolio;
       // List<Receipt> receipts;
            for (Receipt r : receipts){
                printer.print(r);
                System.out.println("----------------------------------");
            }
       return buyerService.getReceipts(buyer);
        }
        // Returns marketplace credits available for purchase
    public List<Credit> viewMarketplace(User buyer) {
        if (!interceptorManager.execute(buyer.getEmail(), buyer.getRole()))
            return null;

        return buyerService.loadAvailableCredits();
    }
    //  Returns buyer's purchased credit portfolio
    public List<Credit> viewPortfolio(User buyer) {

        if (!interceptorManager.execute(buyer.getEmail(), buyer.getRole()))
            return null;

        return buyerService.getPortfolio(buyer);
    }
   // returns aggregated account summary for buyer
    public Map<String, Object> getAccountSummary(User buyer) {

        if (!interceptorManager.execute(buyer.getEmail(), buyer.getRole()))
            return null;

        return buyerService.getAccountSummary(buyer);
    }


}

