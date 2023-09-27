package org.imixs.application.ui.view;

import java.io.Serializable;
import java.util.logging.Logger;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

/**
 * The BackupController is used to monitor the backup status of the
 * {@link BackupService}. The controller provides a processing log and shows the
 * current configuration. This controller does not hold any state.
 *
 * @author rsoika
 *
 */
@Named("apiController")
@RequestScoped
public class APIController implements Serializable {

    private static final long serialVersionUID = 7027147503119012594L;

    @Inject
    @ConfigProperty(name = "health.endpoint", defaultValue = "http://localhost:9990/health")
    String healthEndpoint;

    @Inject
    @ConfigProperty(name = "metrics.endpoint", defaultValue = "http://localhost:9990/metrics")
    String metricsEndpoint;

    @SuppressWarnings("unused")
    private static Logger logger = Logger.getLogger(APIController.class.getName());

  
    public String getHealthEndpoint() {
        return healthEndpoint;
    }

    public String getMetricsEndpoint() {
        return metricsEndpoint;
    }

  
}