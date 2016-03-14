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
@ComponentScan({"org.librairy.storage","org.librairy.eventbus"})
@PropertySource({"classpath:storage.properties","classpath:eventbus.properties"})
public class Config {

    //To resolve ${} in @Value
    @Bean
    public static PropertySourcesPlaceholderConfigurer placeHolderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

}
