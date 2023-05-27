package model;

public class contact {
    private int contactId;
    private String contactName;
    private String email;

    public contact(int contactId, String contactName, String email) {
        this.contactId = contactId;
        this.contactName = contactName;
        this.email = email;
    }

    public int getContactId() {
        return this.contactId;
    }

    public String getContactName() {
        return this.contactName;
    }

    public String getEmail() {
        return this.email;
    }

    public String toString() {
        return this.contactId + ": " + this.contactName;
    }
}
