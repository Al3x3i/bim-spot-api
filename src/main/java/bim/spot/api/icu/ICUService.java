package bim.spot.api.icu;

import bim.spot.api.IcuApiProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ICUService {

    @Autowired
    private IcuApiProperties icuApiProperties;

    public final static String REGION_LIST_URL = "/region/list";

    @Autowired
    private RestTemplate restTemplate;

    public void getAllRegions() {
        String urlWithToken = setTokenToUrl(REGION_LIST_URL);
        String result = restTemplate.getForObject(urlWithToken, String.class);
    }

    public String setTokenToUrl(String urlPath) {
        return icuApiProperties.apiUrl + urlPath + "?token=" + icuApiProperties.token;
    }
}
