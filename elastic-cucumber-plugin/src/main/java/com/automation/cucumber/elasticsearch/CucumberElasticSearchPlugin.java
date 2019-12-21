package com.automation.cucumber.elasticsearch;

import java.nio.file.Path;
import java.util.List;

import javax.inject.Inject;

import com.automation.cucumber.elasticsearch.exceptions.CucElasticPluginException;
import com.automation.cucumber.elasticsearch.filesystem.FileIO;
import com.automation.cucumber.elasticsearch.filesystem.FileSystemManager;
import com.automation.cucumber.elasticsearch.json.JsonPojoConverter;
import com.automation.cucumber.elasticsearch.json.pojo.Report;
import com.automation.cucumber.elasticsearch.logging.CucElasticPluginLogger;
import com.automation.cucumber.elasticsearch.pojos.collections.AllScenariosCollection;
import com.automation.cucumber.elasticsearch.properties.PropertyManager;
import com.automation.cucumber.elasticsearch.utils.CucElasticPluginReportGenerator;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

@Mojo(name = "upload")
public class CucumberElasticSearchPlugin extends AbstractMojo {
    
    private final CucElasticPluginLogger logger;
    private final PropertyManager propertyManager;
    private final FileSystemManager fileSystemManager;
    private final FileIO fileIO;
    private final JsonPojoConverter jsonPojoConverter;
    private final CucElasticPluginReportGenerator reportGenerator;

    /**
     * The path to the Cucumber JSON files.
    */ 
    @Parameter(property = "load.sourceJsonReportDirectory", required = true)
    private String sourceJsonReportDirectory = "";

    /**
     * The host name of elastic search server.
    */ 
    @Parameter(property = "load.elasticSearchHostName", required = true)
    private String elasticSearchHostName = "";

    /**
     * The Cucumber Feature summary index name for elastic search.
    */ 
    @Parameter(property = "load.featureSummaryIndex", defaultValue="feature_summary_index", required = true)
    private String featureSummaryIndex = "";
    
    /**
     * The Cucumber Scenario summary index name for elastic search.
    */ 
    @Parameter(property = "load.scenarioSummaryIndex", defaultValue="scenario_summary_index", required = true)
    private String scenarioSummaryIndex = "";

    /**
     * The Cucumber Steps summary index name for elastic search.
    */ 
    @Parameter(property = "load.stepSummaryIndex", defaultValue="step_summary_index", required = true)
    private String stepSummaryIndex = "";

    /**
     * The Cucumber Tags summary index name for elastic search.
    */ 
    @Parameter(property = "load.tagSummaryIndex", defaultValue="tag_summary_index", required = true)
    private String tagSummaryIndex = "";
    
    /**
     * The Cucumber Feature summary document type name for elastic search.
    */ 
    @Parameter(property = "load.featureSummaryDocumentType", defaultValue="feature_summary_document_type", required = true)
    private String featureSummaryDocumentType = "";
    
    /**
     * The Cucumber Scenario summary document type name for elastic search.
    */ 
    @Parameter(property = "load.scenarioSummaryDocumentType", defaultValue="scenario_summary_document_type", required = true)
    private String scenarioSummaryDocumentType = "";
    
    /**
     * The Cucumber Steps summary document type name for elastic search.
    */ 
    @Parameter(property = "load.stepSummaryDocumentType", defaultValue="step_summary_document_type", required = true)
    private String stepSummaryDocumentType = "";

    /**
     * The Cucumber Tags summary document type name for elastic search.
    */ 
    @Parameter(property = "load.tagSummaryDocumentType", defaultValue="tag_summary_document_type", required = true)
    private String tagSummaryDocumentType = "";
    
    /**
     * The flag to control sending Feature summary documents to elastic search.
    */ 
    @Parameter(property = "load.sendFeatureSummaryToElasticSearch", defaultValue="false", required = true)
    private String sendFeatureSummaryToElasticSearch = "";

    /**
     * The flag to control sending Scenario summary documents to elastic search.
    */ 
    @Parameter(property = "load.sendScenarioSummaryToElasticSearch", defaultValue="false", required = true)
    private String sendScenarioSummaryToElasticSearch = "";

    /**
     * The flag to control sending Step summary documents to elastic search.
    */ 
    @Parameter(property = "load.sendStepSummaryToElasticSearch", defaultValue="false", required = true)
    private String sendStepSummaryToElasticSearch = "";
    
    /**
     * The flag to control sending Tag summary documents to elastic search.
    */ 
    @Parameter(property = "load.sendTagSummaryToElasticSearch", defaultValue="false", required = true)
    private String sendTagSummaryToElasticSearch = "";

    /**
     * Skip Cucumber report generation.
    */ 
    @Parameter(defaultValue = "false", property = "load.skip")
    private boolean skip;

    @Inject
    public CucumberElasticSearchPlugin(
            final CucElasticPluginLogger logger,
            final PropertyManager propertyManager,
            final FileSystemManager fileSystemManager,
            final FileIO fileIO,
            final JsonPojoConverter jsonPojoConverter,
            final CucElasticPluginReportGenerator reportGenerator
    ) {
        this.propertyManager = propertyManager;
        this.fileSystemManager = fileSystemManager;
        this.fileIO = fileIO;
        this.jsonPojoConverter = jsonPojoConverter;
        this.logger = logger;
        this.reportGenerator = reportGenerator;
    }	
    
    /**
     * Cucumber Report start method.
     *
     * @throws CucElasticPluginException When thrown, the plugin execution is stopped.
    */ 
    public void execute() throws CucElasticPluginException {
        // Initialize logger to be available outside the AbstractMojo class
        logger.setMojoLogger(getLog());

        if (skip) {
            getLog().info("Cucumber report generation was skipped by a configuration flag.");
            return;
        }

        // Initialize and validate passed pom properties
        propertyManager.setSourceJsonReportDirectory(sourceJsonReportDirectory);
        propertyManager.setElasticSearchHostName(elasticSearchHostName);
        propertyManager.setFeatureSummaryIndex(featureSummaryIndex);
        propertyManager.setFeatureSummaryDocumentType(featureSummaryDocumentType);
        propertyManager.setScenarioSummaryIndex(scenarioSummaryIndex);
        propertyManager.setScenarioSummaryDocumentType(scenarioSummaryDocumentType);
        propertyManager.setStepSummaryIndex(stepSummaryIndex);
        propertyManager.setStepSummaryDocumentType(stepSummaryDocumentType);
        propertyManager.setTagSummaryIndex(tagSummaryIndex);
        propertyManager.setTagSummaryDocumentType(tagSummaryDocumentType);
        propertyManager.setSendFeatureSummaryToElasticSearch(sendFeatureSummaryToElasticSearch);
        propertyManager.setSendScenarioSummaryToElasticSearch(sendScenarioSummaryToElasticSearch);
        propertyManager.setSendStepSummaryToElasticSearch(sendStepSummaryToElasticSearch);
        propertyManager.setSendTagSummaryToElasticSearch(sendTagSummaryToElasticSearch);
        
        propertyManager.validateSettings();

        logger.info("-----------------------------------------------");
        logger.info(String.format(" Cucumber Report Maven Plugin, version %s", getClass().getPackage().getImplementationVersion()));
        logger.info("-----------------------------------------------");
        propertyManager.logProperties();

        AllScenariosCollection allScenariosPageCollection = new AllScenariosCollection();
        List<Path> jsonFilePaths = fileSystemManager.getJsonFilePaths(propertyManager.getSourceJsonReportDirectory());
        for (Path jsonFilePath : jsonFilePaths) {
            String jsonString = fileIO.readContentFromFile(jsonFilePath.toString());
            try {
                Report[] reports = jsonPojoConverter.convertJsonToReportPojos(jsonString);
                allScenariosPageCollection.addReports(reports);
            } catch (CucElasticPluginException e) {
                logger.error("Could not parse JSON in file '" + jsonFilePath.toString() + "': " + e.getMessage());
            }
        }
        
        reportGenerator.generateAndSendReportDocumentsForElasticSearch(allScenariosPageCollection);
    }
    
}
