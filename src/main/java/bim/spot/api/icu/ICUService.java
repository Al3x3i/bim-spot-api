package bim.spot.api.icu;

import bim.spot.api.IcuApiProperties;
import bim.spot.api.icu.AvailableSpecies.Species;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import static java.util.stream.Collectors.toList;

@Service
@Slf4j
public class ICUService {

    @Autowired
    private IcuApiProperties icuApiProperties;

    public final static String REGION_LIST_URL = "/region/list";
    public final static String REGION_SPECIES_URL = "/species/region/";
    public final static String REGIONAL_ASSESSMENTS_URL = "/measures/species/id/{id}/region/{region}";

    @Autowired
    private RestTemplate restTemplate;

    public AvailableRegions getAllRegions() {
        String urlWithToken = setTokenToUrl(REGION_LIST_URL);
        AvailableRegions result = restTemplate.getForObject(urlWithToken, AvailableRegions.class);
        log.info("Available '{}' regions", result.getCount());
        return result;
    }

    public AvailableSpecies getSpeciesByRegion(String region, int page) {
        String url = UriComponentsBuilder.fromUriString(REGION_SPECIES_URL).pathSegment(region, "page", String.valueOf(0)).toUriString();
        String urlWithToken = setTokenToUrl(url);

        AvailableSpecies result = restTemplate.getForObject(urlWithToken, AvailableSpecies.class);

        log.info("Available '{}' species for the region '{}' from the page '{}'", result.getCount(), region, page);
        return result;

    }

    public List<Species> preview(String region, int page, SpeciesCategoryEnum speciesCategoryFilter) {
        AvailableSpecies availableSpecies = getSpeciesByRegion(region, page);

        List<Species> filteredSpecies = filterResultBySpeciesType(availableSpecies, SpeciesCategoryEnum.CR.name());
        log.info("Filtered '{}' species by '{}'", filteredSpecies.size(), speciesCategoryFilter.name());

        for (Species filteredSpecy : filteredSpecies) {
            fetchConservationMeasures(filteredSpecy.getTaxonid(), region);
        }

        return filteredSpecies;
    }

    SpeciesMeasures fetchConservationMeasures(String id, String region) {
        Map<String, String> urlParams = new HashMap<>();
        urlParams.put("id", id);
        urlParams.put("region", region);

        String url = UriComponentsBuilder.fromUriString(REGIONAL_ASSESSMENTS_URL).buildAndExpand(urlParams).toUriString();

        String urlWithToken = setTokenToUrl(url);
        SpeciesMeasures result = restTemplate.getForObject(urlWithToken, SpeciesMeasures.class);
        return result;
    }

    List<Species> filterResultBySpeciesType(AvailableSpecies availableSpecies, String filter) {
        return availableSpecies.getResult().stream()
                .filter(species -> species.getCategory().equals(filter))
                .collect(toList());
    }

    public String setTokenToUrl(String urlPath) {
        return icuApiProperties.getApiUrl() + urlPath + "?token=" + icuApiProperties.getToken();
    }
}
