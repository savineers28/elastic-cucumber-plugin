
 
 

package com.automation.cucumber.elasticsearch.exceptions.properties;

import org.junit.Test;

import com.automation.cucumber.elasticsearch.exceptions.properties.WrongOrMissingPropertyException;

import static org.assertj.core.api.Assertions.*;

public class WrongOrMissingPropertyExceptionTest {

    @Test
    public void testErrorMessage() {
        WrongOrMissingPropertyException exception = 
    		new WrongOrMissingPropertyException("PropertyName");
        assertThat("Property 'PropertyName' is not specified"
    		+ " in the configuration section of your pom file"
    		+ " or contains an invalid value.")
        	.isEqualTo(exception.getMessage());
    }
}
