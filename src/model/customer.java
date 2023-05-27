package model;

import java.time.LocalDateTime;

public class customer {
    private int customerId;
    private String name;
    private String address;
    private String postalCode;
    private String phoneNumber;
    private LocalDateTime createTime;
    private String createdBy;
    private LocalDateTime lastUpdate;
    private String lastUpdatedBy;
    private int divisionId;

    public customer(int customerId, String name, String address, String postalCode, String phoneNumber, LocalDateTime createTime, String createdBy, LocalDateTime lastUpdate, String lastUpdatedBy, int divisionId) {
        this.customerId = customerId;
        this.name = name;
        this.address = address;
        this.postalCode = postalCode;
        this.phoneNumber = phoneNumber;
        this.createTime = createTime;
        this.createdBy = createdBy;
        this.lastUpdate = lastUpdate;
        this.lastUpdatedBy = lastUpdatedBy;
        this.divisionId = divisionId;
    }

    public int getCustomerId() {
        return this.customerId;
    }

    public String getName() {
        return this.name;
    }

    public String getAddress() {
        return this.address;
    }

    public String getPostalCode() {
        return this.postalCode;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public LocalDateTime getCreateTime() {
        return this.createTime;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public LocalDateTime getLastUpdate() {
        return this.lastUpdate;
    }

    public String getLastUpdatedBy() {
        return this.lastUpdatedBy;
    }

    public int getDivisionId() {
        return this.divisionId;
    }
}

