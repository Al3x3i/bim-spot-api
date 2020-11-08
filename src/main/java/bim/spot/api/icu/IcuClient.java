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

    public AvailableRegions findAvailableRegions() {
        return restTemplate.getForObject(REGION_LIST_URL, AvailableRegions.class);
    }

    public AvailableSpecies findSpecies(String region, int page) {
        String url = UriComponentsBuilder.fromUriString(REGION_SPECIES_URL).pathSegment(region, "page", String.valueOf(page)).toUriString();
        return restTemplate.getForObject(url, AvailableSpecies.class);
    }

    @Async("icuThreadPoolTaskExecutor")
    public CompletableFuture<SpeciesMeasure> findSpeciesMeasure(String id, String region) {
        Map<String, String> urlParams = new HashMap<>();
        urlParams.put("id", id);
        urlParams.put("region", region);

        String url = UriComponentsBuilder.fromUriString(REGIONAL_ASSESSMENTS_URL).buildAndExpand(urlParams).toUriString();

        SpeciesMeasure result = restTemplate.getForObject(url, SpeciesMeasure.class);

        return CompletableFuture.completedFuture(result);
    }
}
