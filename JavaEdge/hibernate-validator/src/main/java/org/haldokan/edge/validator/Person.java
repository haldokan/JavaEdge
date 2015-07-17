package org.haldokan.edge.validator;

import javax.validation.constraints.NotNull;

public class Person {

    @NotNull
    private String name;

    public Person(String name) {
	this.name = name;
    }

    /**
     * @return the name
     */
    public String getName() {
	return name;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(String name) {
	this.name = name;
    }

}
