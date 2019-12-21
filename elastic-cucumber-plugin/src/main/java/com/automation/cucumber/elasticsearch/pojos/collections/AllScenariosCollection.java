package com.automation.cucumber.elasticsearch.pojos.collections;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.automation.cucumber.elasticsearch.constants.Status;
import com.automation.cucumber.elasticsearch.json.pojo.Element;
import com.automation.cucumber.elasticsearch.json.pojo.Report;
import com.automation.cucumber.elasticsearch.json.pojo.Tag;
import com.automation.cucumber.elasticsearch.pojos.Feature;
import com.automation.cucumber.elasticsearch.utils.CucElasticPluginUtils;

public class AllScenariosCollection {
    private List<Report> reports = new ArrayList<>();
    private Tag tagFilter;
    private Feature featureFilter;

    public List<Report> getReports() {
        return reports;
    }

    public void clearReports() {
        reports = new ArrayList<>();
    }

    public void addReports(final Report[] reportList) {
        if (reportList == null) {
            return;
        }
        this.reports.addAll(Arrays.asList(reportList));
    }

    public int getTotalNumberOfScenarios() {
        return reports.stream().map(Report::getElements).
                mapToInt(elements -> (int) elements.stream().filter(Element::isScenario).count()).sum();
    }

    public boolean hasFailedScenarios() {
        return getTotalNumberOfFailedScenarios() > 0;
    }

    public boolean hasPassedScenarios() {
        return getTotalNumberOfPassedScenarios() > 0;
    }

    public boolean hasSkippedScenarios() {
        return getTotalNumberOfSkippedScenarios() > 0;
    }

    public int getTotalNumberOfPassedScenarios() {
        return getNumberOfScenariosWithStatus(Status.PASSED);
    }

    public int getTotalNumberOfFailedScenarios() {
        return getNumberOfScenariosWithStatus(Status.FAILED);
    }

    public int getTotalNumberOfSkippedScenarios() {
        return getNumberOfScenariosWithStatus(Status.SKIPPED);
    }

    private int getNumberOfScenariosWithStatus(final Status status) {
        return reports.stream().mapToInt(
                report -> (int) report.getElements().stream().filter(
                        element -> element.getStatus().equals(status)
                ).count()).sum();
    }

    public long getTotalDuration() {
        long totalDurationMicroseconds = 0;
        for (Report report : reports) {
            totalDurationMicroseconds += report.getTotalDuration();
        }
        return totalDurationMicroseconds;
    }

    public String getTotalDurationString() {
        return CucElasticPluginUtils.convertMicrosecondsToTimeString(getTotalDuration());
    }

    public Tag getTagFilter() {
        return tagFilter;
    }

    public void setTagFilter(final Tag tagFilter) {
        this.tagFilter = tagFilter;
    }

    public Feature getFeatureFilter() {
        return featureFilter;
    }

    public void setFeatureFilter(final Feature featureFilter) {
        this.featureFilter = featureFilter;
    }
}
