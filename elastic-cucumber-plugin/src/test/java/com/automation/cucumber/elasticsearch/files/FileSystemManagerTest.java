package com.automation.cucumber.elasticsearch.files;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.automation.cucumber.elasticsearch.exceptions.CucElasticPluginException;
import com.automation.cucumber.elasticsearch.filesystem.FileSystemManager;

public class FileSystemManagerTest {
    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();
    private FileSystemManager fileSystemManager;

    @Before
    public void setup() {
        fileSystemManager = new FileSystemManager();
    }

    @Test(expected = CucElasticPluginException.class)
    public void invalidSourceFeaturesTest() throws Exception {
        fileSystemManager.getJsonFilePaths("nonexistentpath");
    }

    @Test
    public void validSourceFeaturesTest() throws Exception {
        String jsonPath = testFolder.getRoot().getPath();
        fileSystemManager.getJsonFilePaths(jsonPath);
    }
}
