package bim.spot.api.icu;

import bim.spot.api.IcuApiProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@Slf4j
public class ICUService {

    @Autowired
    private IcuApiProperties icuApiProperties;

    public final static String REGION_LIST_URL = "/region/list";
    public final static String REGION_SPECIES_URL = "/species/region/";

    @Autowired
    private RestTemplate restTemplate;

    public AvailableRegions getAllRegions() {
        String urlWithToken = setTokenToUrl(REGION_LIST_URL);
        AvailableRegions result = restTemplate.getForObject(urlWithToken, AvailableRegions.class);
        log.info("Available '{}' regions", result.getCount());
        return result;
    }

    public void getSpeciesByRegion(String region, int page) {
        String url = UriComponentsBuilder.fromUriString(REGION_SPECIES_URL)
                .pathSegment(region, "page", String.valueOf(page))
                .toUriString();
        String urlWithToken = setTokenToUrl(url);

        String result = restTemplate.getForObject(urlWithToken, String.class);
    }

    public String setTokenToUrl(String urlPath) {
        return icuApiProperties.getApiUrl() + urlPath + "?token=" + icuApiProperties.getToken();
    }
}
