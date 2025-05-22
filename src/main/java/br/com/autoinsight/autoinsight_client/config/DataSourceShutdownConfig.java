package br.com.autoinsight.autoinsight_client.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.EventListener;

import com.zaxxer.hikari.HikariDataSource;

@Configuration
public class DataSourceShutdownConfig {

    @Autowired
    private DataSource dataSource;

    @EventListener
    public void handleContextClosed(ContextClosedEvent event) {
        if (dataSource instanceof HikariDataSource) {
            HikariDataSource hikariDataSource = (HikariDataSource) dataSource;
            if (!hikariDataSource.isClosed()) {
                hikariDataSource.close();
                System.out.println("HikariCP DataSource fechado adequadamente");
            }
        }
    }
} 