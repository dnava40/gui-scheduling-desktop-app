package model;

import java.time.LocalDateTime;

public class appointment {
    private int appointmentId;
    private String title;
    private String description;
    private String location;
    private String type;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private LocalDateTime createTime;
    private String createdBy;
    private LocalDateTime lastUpdate;
    private String lastUpdatedBy;
    private int customerId;
    private int userId;
    private int contactId;

    public appointment(int appointmentId, String title, String description, String location, String type, LocalDateTime startTime, LocalDateTime endTime, LocalDateTime createTime, String createdBy, LocalDateTime lastUpdate,
                       String lastUpdatedBy, int customerId, int userId, int contactId) {
        this.appointmentId = appointmentId;
        this.title = title;
        this.description = description;
        this.location = location;
        this.type = type;
        this.startTime = startTime;
        this.endTime = endTime;
        this.createTime = createTime;
        this.createdBy = createdBy;
        this.lastUpdate = lastUpdate;
        this.lastUpdatedBy = lastUpdatedBy;
        this.customerId = customerId;
        this.userId = userId;
        this.contactId = contactId;

    }

    public int getAppointmentId() {
        return this.appointmentId;
    }

    public String getTitle() {
        return this.title;
    }

    public String getDescription() {
        return this.description;
    }

    public String getLocation() {
        return this.location;
    }

    public String getType() {
        return this.type;
    }

    public LocalDateTime getStartTime() {
        return this.startTime;
    }

    public LocalDateTime getEndTime() {
        return this.endTime;
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

    public int getCustomerId() {
        return this.customerId;
    }

    public int getUserId() {
        return this.userId;
    }

    public int getContactId() {
        return this.contactId;
    }
}
