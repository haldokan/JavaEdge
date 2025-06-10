package org.haldokan.edge.cucumber;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.api.java8.En;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by haytham.aldokanji on 9/10/15.
 */
public class StoreStepDefs implements En {
    private ProvisionsStore store = new ProvisionsStore();
    private Integer itemProvisions;

    // TODO there is a problem with Cucumber on Java 1.8.0_60 (Mac) which results in lambdas not working.
    // A ticket is beginning Sep 2015

    /*
    public StoreStepDefs() {
        Given("^store has amount (\\d+) of item ([a-zA-Z])$", (Integer amount, String item) -> {
            store = new ProvisionsStore();
            store.updateProvisionsFor(item, amount);
        });

        When("^I get item ([a-zA-Z]) from the store$", (String item) -> {
            this.itemProvisions = store.provisionsFor(item);
        });

        Then("^item amount (\\d+) is returned from the store$", (Integer amount) -> {
            assertThat(itemProvisions, is(amount));
        });
    }
*/
    @Given("^store has amount (\\d+) of item ([a-zA-Z]+)$")
    public void storeHasItem(Integer amount, String item) throws Throwable {
        store = new ProvisionsStore();
        store.updateProvisionsFor(item, amount);
    }

    @When("^I get item ([a-zA-z]+) from the store$")
    public void gettingItem(String item) throws Throwable {
        this.itemProvisions = store.provisionsFor(item);
    }

    @Then("^item amount (\\d+) is returned$")
    public void itemAmountIsReturned(Integer amount) throws Throwable {
        assertThat(itemProvisions, is(amount));
    }

    @Given("^store has not item ([a-zA-Z]+)$")
    public void storeHasNotItem(String item) throws Throwable {
        store = new ProvisionsStore();
    }
}
