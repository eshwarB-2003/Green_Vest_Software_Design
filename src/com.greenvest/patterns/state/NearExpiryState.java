package com.greenvest.patterns.state;

public class NearExpiryState implements CreditState {
   public String getStateName(){
       return "NEAR_EXPIRY";
   }

}
