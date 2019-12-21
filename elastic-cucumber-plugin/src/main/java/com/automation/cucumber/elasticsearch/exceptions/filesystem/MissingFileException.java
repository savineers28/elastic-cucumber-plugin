package com.automation.cucumber.elasticsearch.exceptions.filesystem;

import com.automation.cucumber.elasticsearch.exceptions.CucElasticPluginException;

 /*
 * Thrown when a file is not found.
 */
public class MissingFileException extends CucElasticPluginException {
    /*
     * Constructor.
     *
     * @param fileName The name of the missing file.
     */
    public MissingFileException(final String fileName) {
        super("File '" + fileName + "' could not be found.");
    }
}
