package br.com.autoinsight.autoinsight_client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component
public class EnvValidator {

  @Value("${SPRING_DATASOURCE_URL:}")
  private String datasourceUrl;

  @Value("${SPRING_DATASOURCE_USERNAME:}")
  private String datasourceUsername;

  @Value("${SPRING_DATASOURCE_PASSWORD:}")
  private String datasourcePassword;

  @Value("${SPRING_DATASOURCE_DRIVERCLASSNAME:}")
  private String datasourceDriverClassName;

  @PostConstruct
  public void validate() {
    if (datasourceUrl.isEmpty() || datasourceUsername.isEmpty() ||
        datasourcePassword.isEmpty() || datasourceDriverClassName.isEmpty()) {
      throw new IllegalStateException("Some required environment variables are not set!");
    }
  }
}