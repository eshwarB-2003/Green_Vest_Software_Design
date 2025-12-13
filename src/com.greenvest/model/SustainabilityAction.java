/*package com.greenvest.model;

public class SustainabilityAction {

    private String id;
    private String sellerEmail;
    private String type;        // TREE, SOLAR, RECYCLE
    private double metricValue; // trees count, kWh, etc
    private String status;

    public SustainabilityAction(String id, String sellerEmail,
                                String type, double metricValue) {
        this.id = id;
        this.sellerEmail = sellerEmail;
        this.type = type.trim().toUpperCase().replace(" ", "_");
        this.metricValue = metricValue;
      //  this.approved = false;
        this.status = "Pending";
    }


    public String getId() { return id; }
    public String getType() { return type; }
    public String getSellerEmail(){ return sellerEmail; }
    public double getMetricValue() { return metricValue; }
    public void  approve() { this.status = "Approved"; }
    public void  reject() { this.status = "Rejected"; }
    public boolean isPending() { return status.equals("Pending"); }
    public String getStatus() {
        return this.status;
    }

    // public boolean isApproved() { return approved; }
}*/
package com.greenvest.model;

public class SustainabilityAction {

    private String id;
    private String sellerEmail;
    private String type;        // TREE, SOLAR, RECYCLE, OTHER
    private double metricValue; // used only for credit actions
    private String description; // used only for OTHER
    private String status;

    // Constructor for TREE / SOLAR / RECYCLE
    public SustainabilityAction(String id, String sellerEmail,
                                String type, double metricValue) {
        this.id = id;
        this.sellerEmail = sellerEmail;
        this.type = type.trim().toUpperCase();
        this.metricValue = metricValue;
        this.status = "Pending";
    }

    // Constructor for OTHER
    public SustainabilityAction(String id, String sellerEmail,
                                String type, String description) {
        this.id = id;
        this.sellerEmail = sellerEmail;
        this.type = type.trim().toUpperCase();
        this.description = description;
        this.metricValue = 0;
        this.status = "Pending";
    }

    public String getId() { return id; }
    public String getSellerEmail() { return sellerEmail; }
    public String getType() { return type; }
    public double getMetricValue() { return metricValue; }
    public String getDescription() { return description; }
    public String getStatus() { return status; }




    public boolean isPending() {
        return status.equals("Pending");
    }

    public boolean isOther() {
        return "OTHER".equals(type);
    }

    public void approve() { status = "Approved"; }
    public void reject() { status = "Rejected"; }
}

