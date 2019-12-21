package com.automation.cucumber.elasticsearch.logging;

import org.apache.maven.plugin.logging.Log;

import javax.inject.Singleton;

@Singleton
public class CucElasticPluginLogger {

    private Log mojoLogger;

    public void setMojoLogger(final Log mojoLogger) {
        this.mojoLogger = mojoLogger;
    }

    public void info(final CharSequence charSequence) {
        mojoLogger.info(charSequence);
    }

    public void error(final CharSequence charSequence) {
        mojoLogger.error(charSequence);
    }
}
