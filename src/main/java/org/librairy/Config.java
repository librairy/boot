package org.librairy;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

/**
 * Created by cbadenes on 14/03/16.
 */
@Configuration("librairy-boot")
@ComponentScan({"org.librairy.storage", "org.librairy.eventbus"})
@PropertySource({"classpath:storage.properties", "classpath:eventbus.properties"})
public class Config {

    public static final String EVENT_HOST = "MESSAGE_BROKER_HOST";


    /**
     * Resolve ${} in @Value references
     * @return
     */
    @Bean
    public static PropertySourcesPlaceholderConfigurer placeHolderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

}
