package bim.spot.api.icu;

import bim.spot.api.IcuApiProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class ICUService {

    @Autowired
    private IcuApiProperties icuApiProperties;

    public final static String REGION_LIST_URL = "/region/list";

    @Autowired
    private RestTemplate restTemplate;

    public AvailableRegions getAllRegions() {
        String urlWithToken = setTokenToUrl(REGION_LIST_URL);
        AvailableRegions result = restTemplate.getForObject(urlWithToken, AvailableRegions.class);
        log.info("Available '{}' regions", result.getCount());
        return result;
    }

    public String setTokenToUrl(String urlPath) {
        return icuApiProperties.apiUrl + urlPath + "?token=" + icuApiProperties.token;
    }
}
