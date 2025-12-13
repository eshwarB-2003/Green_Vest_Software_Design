package com.greenvest.config;


import java.io.*;

public class SystemConfig {
        private static double MIN_CREDIT_PRICE = 5.0;

        public static double getMinCreditPrice() {
            return MIN_CREDIT_PRICE;
        }

        public static void setMinCreditPrice(double price) {
            MIN_CREDIT_PRICE = price;
        }
    }
