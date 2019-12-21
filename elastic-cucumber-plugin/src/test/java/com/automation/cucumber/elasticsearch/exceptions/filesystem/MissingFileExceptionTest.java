
 
 

package com.automation.cucumber.elasticsearch.exceptions.filesystem;

import org.junit.Test;

import com.automation.cucumber.elasticsearch.exceptions.filesystem.MissingFileException;

import static org.assertj.core.api.Assertions.*;

public class MissingFileExceptionTest {

    @Test
    public void testErrorMessage() {
        MissingFileException exception = new MissingFileException("Filename");
        assertThat("File 'Filename' could not be found.").isEqualTo(exception.getMessage());
    }
}
