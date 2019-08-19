package com.jagadeesh.moneytransfer.config;

import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.ApplicationPath;

@ApplicationPath("resources")
class JerseyApplication extends ResourceConfig {

    JerseyApplication() {
        packages("com.jagadeesh.moneytransfer");
        register(JacksonFeature.class);
    }
}
