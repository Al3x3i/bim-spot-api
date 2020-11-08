package bim.spot.api.icu;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class IcuClient {

    private final static String REGION_LIST_URL = "/region/list";
    private final static String REGION_SPECIES_URL = "/species/region/";
    private final static String REGIONAL_ASSESSMENTS_URL = "/measures/species/id/{id}/region/{region}";

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    private IcuApiProperties icuApiProperties;


    private String setTokenToUrl(String urlPath) {
        return icuApiProperties.getApiUrl() + urlPath;
    }

    public AvailableRegions findAvailableRegions() {
        String urlWithToken = setTokenToUrl(REGION_LIST_URL);
        return restTemplate.getForObject(urlWithToken, AvailableRegions.class);
    }

    public AvailableSpecies findSpecies(String region, int page) {
        String url = UriComponentsBuilder.fromUriString(REGION_SPECIES_URL).pathSegment(region, "page", String.valueOf(page)).toUriString();
        String urlWithToken = setTokenToUrl(url);
        return restTemplate.getForObject(urlWithToken, AvailableSpecies.class);
    }

    @Async("icuThreadPoolTaskExecutor")
    public CompletableFuture<SpeciesMeasure> findSpeciesMeasure(String id, String region) {
        Map<String, String> urlParams = new HashMap<>();
        urlParams.put("id", id);
        urlParams.put("region", region);

        String url = UriComponentsBuilder.fromUriString(REGIONAL_ASSESSMENTS_URL).buildAndExpand(urlParams).toUriString();

        String urlWithToken = setTokenToUrl(url);
        SpeciesMeasure result = restTemplate.getForObject(urlWithToken, SpeciesMeasure.class);

        return CompletableFuture.completedFuture(result);
    }
}
