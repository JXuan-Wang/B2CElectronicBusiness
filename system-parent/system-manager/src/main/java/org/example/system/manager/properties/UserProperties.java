package org.example.system.manager.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Data
@ConfigurationProperties(prefix = "system.auth")
public class UserProperties {
    private List<String> noAuthUrls;
}
