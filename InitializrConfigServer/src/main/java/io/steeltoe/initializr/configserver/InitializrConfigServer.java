// Licensed to the .NET Foundation under one or more agreements.
// The .NET Foundation licenses this file to you under the Apache 2.0 License.
// See the LICENSE file in the project root for more information.

package io.steeltoe.initializr.configserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.info.BuildProperties;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.env.Environment;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@EnableConfigServer
@SpringBootApplication
public class InitializrConfigServer implements ApplicationContextAware {

    private static final Logger logger = LoggerFactory.getLogger(InitializrConfigServer.class);

    @Autowired
    private Environment env;

    public static void main(String[] args) {
        SpringApplication.run(InitializrConfigServer.class, args);
    }

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        BuildProperties buildProperties = context.getBean(BuildProperties.class);
        InputStream pin = getClass().getClassLoader().getResourceAsStream("git.properties");
        Properties gitProperties = new Properties();
        if (pin != null) {
            try {
                gitProperties.load(pin);
                pin.close();
            } catch (IOException e) {
                logger.warn("IO error processing Git properties: {}", e.getLocalizedMessage());
            }
        }
        logger.info("{}, version {} [{}]", buildProperties.getName(), buildProperties.getVersion(), gitProperties.getOrDefault("git.commit.id", "unknown"));
        Package springPkg = EnableConfigServer.class.getPackage();
        logger.info("{}, version {}", springPkg.getImplementationTitle(), springPkg.getImplementationVersion());
        logger.info("{}: {}", "spring.cloud.config.server.git.uri", env.getProperty("spring.cloud.config.server.git.uri"));
        logger.info("{}: {}", "spring.profiles.active", env.getProperty("spring.profiles.active"));
        logger.info("{}: {}", "spring.cloud.config.server.native.searchLocations", env.getProperty("spring.cloud.config.server.native.searchLocations"));
    }
}
