package org.haldokan.edge.cucumber;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

// TODO: NEED work mobile number, and office address

@JsonPropertyOrder(value = {"id", "firstName", "lastName", "externalId", "workEmail", "recordType",
        "location1", "country1", "city1", "state1", "postalCode1", "country2", "workPhone", "sms1", "personalPhone", "sms2"})
public class Contact {
    private static final String DEFAULT_RECORD_TYPE = "employee";
    private static final String CSV_HEADER = "ID,First Name,Last Name,External ID,Email Address 1,Record Type,Location 1," +
            "Country,City 1,State/Province 1,Postal Code 1,Country 1,Phone 1,SMS 1,Phone 2,SMS 2\n";
    
    private String id;
    private String firstName;
    private String lastName;
    private String externalId;
    private String workEmail;
    private String personalEmail;
    private String recordType = DEFAULT_RECORD_TYPE;
    private String location1;
    private String country1;
    private String country2;
    private String city1;
    private String state1;
    private String postalCode1;
    private String workPhone;
    private String personalPhone;
    private String homePhone;
    private String sms2;
    private String sms1;

    public static String getCsvHeader() {
        return CSV_HEADER;
    }

    public String getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getExternalId() {
        return externalId;
    }

    public String getWorkEmail() {
        return workEmail;
    }

    public String getRecordType() {
        return recordType;
    }

    public String getLocation1() {
        return location1;
    }

    public String getCountry1() {
        return country1;
    }

    public String getCity1() {
        return city1;
    }

    public String getState1() {
        return state1;
    }

    public String getPostalCode1() {
        return postalCode1;
    }

    public String getCountry2() {
        return country2;
    }

    public String getWorkPhone() {
        return workPhone;
    }

    public String getSms1() {
        return sms1;
    }

    public String getPersonalPhone() {
        return personalPhone;
    }

    public String getSms2() {
        return sms2;
    }

    public String getHomePhone() {
        return homePhone;
    }

    public String getPersonalEmail() {
        return personalEmail;
    }

    @Override
    public String toString() {
        return "Contact{" +
                "id='" + id + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", externalId='" + externalId + '\'' +
                ", workEmail='" + workEmail + '\'' +
                ", personalEmail='" + personalEmail + '\'' +
                ", recordType='" + recordType + '\'' +
                ", location1='" + location1 + '\'' +
                ", country1='" + country1 + '\'' +
                ", country2='" + country2 + '\'' +
                ", city1='" + city1 + '\'' +
                ", state1='" + state1 + '\'' +
                ", postalCode1='" + postalCode1 + '\'' +
                ", workPhone='" + workPhone + '\'' +
                ", personalPhone='" + personalPhone + '\'' +
                ", homePhone='" + homePhone + '\'' +
                ", sms2='" + sms2 + '\'' +
                ", sms1='" + sms1 + '\'' +
                '}';
    }
}
