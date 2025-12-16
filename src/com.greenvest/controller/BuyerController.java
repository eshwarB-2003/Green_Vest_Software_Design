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

public class BuyerController {

    private InterceptorManager interceptorManager;
    private BuyerService buyerService;

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
    public Receipt purchase(User buyer, Credit credit, int qty) {
        if (!interceptorManager.execute(buyer.getEmail(), buyer.getRole()))
            return null;

        return buyerService.processPurchase(buyer, credit, qty);
    }

    public List<Receipt> showReceipts(User buyer) {
        List<Receipt> receipts = buyerService.getReceipts(buyer);

        if (receipts == null || receipts.isEmpty()) {
            System.out.println("No receipts found.");
        }


        // apply decorator here!!
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
    public List<Credit> viewMarketplace(User buyer) {
        if (!interceptorManager.execute(buyer.getEmail(), buyer.getRole()))
            return null;

        return buyerService.loadAvailableCredits();
    }
    public List<Credit> viewPortfolio(User buyer) {

        if (!interceptorManager.execute(buyer.getEmail(), buyer.getRole()))
            return null;

        return buyerService.getPortfolio(buyer);
    }
    public Map<String, Object> getAccountSummary(User buyer) {

        if (!interceptorManager.execute(buyer.getEmail(), buyer.getRole()))
            return null;

        return buyerService.getAccountSummary(buyer);
    }


}

