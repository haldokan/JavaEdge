package org.haldokan.edge.validator;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Min;

public class Driver extends Person {
    VehicleType licensedFor;

    @Min(value = 18, message = "You have to be 18 to drive a car", groups = ValidationGroups.DriverChecks.class)
    public int age;

    @AssertTrue(message = "You first have to pass the driving test", groups = ValidationGroups.DriverChecks.class)
    public boolean hasDrivingLicense;

    public Driver(String name) {
	super(name);
    }

    public void passedDrivingTest(boolean b) {
	hasDrivingLicense = b;
    }

    public int getAge() {
	return age;
    }

    public void setAge(int age) {
	this.age = age;
    }

    /**
     * @return the licensedFor
     */
    public VehicleType getLicensedFor() {
	return licensedFor;
    }

    /**
     * @param licensedFor
     *            the licensedFor to set
     */
    public void setLicensedFor(VehicleType licensedFor) {
	this.licensedFor = licensedFor;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
	return "Driver [licensedFor=" + licensedFor + ", age=" + age + ", hasDrivingLicense=" + hasDrivingLicense + "]";
    }

}