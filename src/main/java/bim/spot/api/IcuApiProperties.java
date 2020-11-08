package bim.spot.api;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "api.icu")
public class IcuApiProperties {

    public String apiUrl = "http://apiv3.iucnredlist.org/api/v3";

    public String token = "9bb4facb6d23f48efbf424bb05c0c1ef1cf6f468393bc745d42179ac4aca5fee";
}
