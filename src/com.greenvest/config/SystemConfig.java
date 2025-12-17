// store global system configuration values
// these values are shared and used across

package com.greenvest.config;


import java.io.*;

public class SystemConfig {
    // default min price
        private static double MIN_CREDIT_PRICE = 5.0;

        // return current min price
        public static double getMinCreditPrice() {
            return MIN_CREDIT_PRICE;
        }

        // updates min price set 
        public static void setMinCreditPrice(double price) {
            MIN_CREDIT_PRICE = price;
        }
    }
