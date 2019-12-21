package com.automation.cucumber.elasticsearch.pojos.collections;

import com.automation.cucumber.elasticsearch.json.pojo.Element;

public class ScenarioDetailsCollection {
    private final Element element;

    public ScenarioDetailsCollection(final Element element) {
        this.element = element;
    }

    public Element getElement() {
        return element;
    }
}
