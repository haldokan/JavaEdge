package org.haldokan.edge.cucumber;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

/**
 * Created by haytham.aldokanji on 9/10/15.
 */
@RunWith(Cucumber.class)
// Tell Cuke to use the 'pretty' and 'html' formatter plugins
@CucumberOptions(plugin = {"pretty", "html:target/cucumber"})
// Name of class ends in Test so the Surefire Maven plugin can pick it up and execute it
public class RunCukesTest {
}
