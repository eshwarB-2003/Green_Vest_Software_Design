package com.greenvest.model;

/*
 * SustainabilityAction represents an action
 * submitted by a seller for approval.
 * It supports both credit-based and custom actions.
 */
public class SustainabilityAction {

    // Unique action ID
    private String id;

    // Email of the seller who submitted the action
    private String sellerEmail;

    // Type of action (TREE, SOLAR, RECYCLE, OTHER)
    private String type;

    // Numeric value used for credit calculation
    private double metricValue;

    // Description used only for OTHER actions
    private String description;

    // Current status of the action
    private String status;

    /*
     * Constructor for credit-based actions
     * like TREE, SOLAR and RECYCLE.
     */
    public SustainabilityAction(String id, String sellerEmail,
                                String type, double metricValue) {
        this.id = id;
        this.sellerEmail = sellerEmail;
        this.type = type.trim().toUpperCase();
        this.metricValue = metricValue;
        this.status = "Pending";
    }

    /*
     * Constructor for OTHER type actions
     * where description is required instead of metrics.
     */
    public SustainabilityAction(String id, String sellerEmail,
                                String type, String description) {
        this.id = id;
        this.sellerEmail = sellerEmail;
        this.type = type.trim().toUpperCase();
        this.description = description;
        this.metricValue = 0;
        this.status = "Pending";
    }

    // Getter methods
    public String getId() { return id; }
    public String getSellerEmail() { return sellerEmail; }
    public String getType() { return type; }
    public double getMetricValue() { return metricValue; }
    public String getDescription() { return description; }
    public String getStatus() { return status; }

    // Checks if the action is still waiting for approval
    public boolean isPending() {
        return status.equals("Pending");
    }

    // Checks if the action is of type OTHER
    public boolean isOther() {
        return "OTHER".equals(type);
    }

    // Marks the action as approved
    public void approve() {
        status = "Approved";
    }

    // Marks the action as rejected
    public void reject() {
        status = "Rejected";
    }
}
