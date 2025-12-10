# GreenVest – Carbon Credit Trading Platform  
A lightweight MVC-based prototype implementing Buyer, Seller, and Admin workflows with JSON storage and core software design patterns (Observer, Factory, Decorator, State, Interceptor, Singleton).  
This project was developed for CS6451 / CS5721 Software Design.

---

## 📌 Project Overview

GreenVest is a carbon credit trading prototype where:

- **Sellers** submit and list verified carbon credits for sale  
- **Buyers** purchase credits, manage portfolios, view receipts, and track balances  
- **Admins** verify sustainability actions and approve credits  

The system uses **file-based JSON storage**, **MVC architecture**, and **multiple design patterns** to replicate real marketplace behaviour.

---

## 🎯 Key Features

### ✔ Buyer Module  
- Register / Login  
- View available credits  
- Purchase credits (with rule validation)  
- Receive receipts (Factory Pattern)  
- View decorated receipts (Decorator Pattern)  
- View credit portfolio (State + Observer Pattern)  
- View account summary (balance + credit statistics)

### ✔ Seller Module  
- Register / Login  
- List credits for sale  
- Update credit prices  
- Track their own credit inventory  

### ✔ Admin Module  
- Login  
- Verify seller credit submissions  
- Approve or reject credits  
- Manage minimum pricing rules  

---

## 🧩 Architecture Summary (MVC + Microkernel)


### ✔ **View Layer**  
Handles ONLY input/output (console).  
Examples:  
- `BuyerView.java`  
- `SellerView.java`  
- `AdminView.java`  

### ✔ **Controller Layer**  
Middleware enforcing role access & linking view → service.  
Examples:  
- `BuyerController.java`  
- `SellerController.java`

### ✔ **Service Layer**  
Business logic goes here.  
Examples:  
- `BuyerService.java`  
- `SellerService.java`  
- `AuthService.java`

### ✔ **Repository Layer**  
Reads/writes JSON files.  
Examples:  
- `UsersRepositoryJSON.java`  
- `ReceiptRepositoryJSON.java`  
- `PortfolioRepositoryJSON.java`

### ✔ **Microkernel Rule Engine**  
Applies rules during purchase:  
- Compliance Rule  
- Expiry Rule (optional extension)  

### ✔ **Design Patterns Used**

| Pattern | Where Used | Purpose |
|--------|-------------|---------|
| **MVC** | Entire project | Separation of concerns |
| **Singleton** | JSON repositories | One instance for consistency |
| **Factory** | Receipt creation | Auto-generate receipt objects |
| **Decorator** | Receipt printing | Add header/footer dynamically |
| **Observer** | Expiry alerts | Notify buyers of expired credits |
| **State** | Credit lifecycle | Active vs Expired credits |
| **Interceptor** | Authentication | Block unauthorized access |

---



