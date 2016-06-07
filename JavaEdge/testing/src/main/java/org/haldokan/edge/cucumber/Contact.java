package org.haldokan.edge.cucumber;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder(value = {"id", "firstName", "lastName", "externalId", "workEmail", "address1", "recordType",
        "location1", "country1", "city1", "state1", "postalCode1", "country2", "workPhone", "sms1", "personalPhone", "sms2"})
public class Contact {
    private String id;
    private String firstName;
    private String lastName;
    private String externalId;
    private String workEmail;
    private String personalEmail;
    private String address1;
    private String recordType;
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

    //First Name	Last Name	External ID	Email Address 1	Record Type	Location 1	Country	City 1	State/Province 1
    // Postal Code 1	Country 1	Phone 1	SMS 1	Phone 2	SMS 2
    //Paul	Gunning	p.gunning@iontrading.com	p.gunning@iontrading.com	Employee	London	GB	London		E1 8EU
    // GB	61481142842	61481142842	+61-423-450-734	+61-423-450-734

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

    public String getAddress1() {
        return address1;
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

    @Override
    public String toString() {
        return "Contact{" +
                "id='" + id + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", externalId='" + externalId + '\'' +
                ", workEmail='" + workEmail + '\'' +
                ", personalEmail='" + personalEmail + '\'' +
                ", address1='" + address1 + '\'' +
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

    public String getPersonalEmail() {
        return personalEmail;
    }

}
