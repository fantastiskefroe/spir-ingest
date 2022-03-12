package dk.fantastiskefroe.spir.ingest.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
public class ApplicationProperties {

    private String hmacPrivateKey;

    public String getHmacPrivateKey() {
        return hmacPrivateKey;
    }

    public void setHmacPrivateKey(String hmacPrivateKey) {
        this.hmacPrivateKey = hmacPrivateKey;
    }
}
