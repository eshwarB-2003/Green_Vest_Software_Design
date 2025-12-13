package com.greenvest.model;

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
}
