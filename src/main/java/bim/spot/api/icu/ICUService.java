package bim.spot.api.icu;

import bim.spot.api.IcuApiProperties;
import bim.spot.api.SpeciesResponse;
import bim.spot.api.SpeciesResponse.SpeciesMeasureResponse;
import bim.spot.api.icu.AvailableSpecies.Species;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
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

    public SpeciesResponse preview(String region, int page, SpeciesCategoryEnum speciesCategoryFilter, SpeciesClassNameEnum speciesClassNameEnum) {

        AvailableSpecies availableSpecies = getSpeciesByRegion(region, page);

        // 5
        List<Species> filteredSpeciesByCategory = filterSpeciesByCategory(availableSpecies.getResult(), speciesCategoryFilter);
        log.info("Filtered '{}' species by '{}'", filteredSpeciesByCategory.size(), speciesCategoryFilter.name());

        List<SpeciesMeasure> speciesMeasures = new ArrayList<>();


        // 5.1
        for (Species species : filteredSpeciesByCategory) {
            SpeciesMeasure speciesMeasure = fetchConservationMeasures(species.getTaxonid(), region);
            speciesMeasures.add(speciesMeasure);

            // TODO Resolve 502 Bad Gateway
            if (speciesMeasures.size() > 1) {
                break;
            }
        }

        // 6
        List<Species> filteredSpeciesByClass = filterSpeciesByClassName(availableSpecies.getResult(), speciesClassNameEnum);

        SpeciesResponse response = generateResponseModel(region, speciesCategoryFilter, filteredSpeciesByCategory,
                speciesClassNameEnum, filteredSpeciesByClass, speciesMeasures);

        return response;
    }

    public String setTokenToUrl(String urlPath) {
        return icuApiProperties.getApiUrl() + urlPath + "?token=" + icuApiProperties.getToken();
    }

    SpeciesMeasure fetchConservationMeasures(String id, String region) {
        Map<String, String> urlParams = new HashMap<>();
        urlParams.put("id", id);
        urlParams.put("region", region);

        String url = UriComponentsBuilder.fromUriString(REGIONAL_ASSESSMENTS_URL).buildAndExpand(urlParams).toUriString();

        String urlWithToken = setTokenToUrl(url);
        SpeciesMeasure result = restTemplate.getForObject(urlWithToken, SpeciesMeasure.class);
        return result;
    }

    List<Species> filterSpeciesByCategory(List<Species> species, SpeciesCategoryEnum filter) {
        String filterName = filter.name();
        return species.stream()
                .filter(item -> item.getCategory().equals(filterName))
                .collect(toList());
    }

    List<Species> filterSpeciesByClassName(List<Species> species, SpeciesClassNameEnum filter) {
        String filterName = filter.name();
        return species.stream()
                .filter(item -> item.getClass_name().equals(filterName))
                .collect(toList());
    }

    String concatenateSpeciesMeasureTiles(SpeciesMeasure speciesMeasure) {
        return speciesMeasure.getResult().stream().map(x -> x.getTitle()).collect(Collectors.joining(","));
    }

    private SpeciesResponse generateResponseModel(String region,
                                                  SpeciesCategoryEnum speciesCategoryFilter,
                                                  List<Species> filteredSpeciesByCategory,
                                                  SpeciesClassNameEnum speciesClassNameEnum,
                                                  List<Species> filteredSpeciesByClass,
                                                  List<SpeciesMeasure> speciesMeasures) {

        SpeciesResponse speciesResponse = new SpeciesResponse();
        // Species Region
        speciesResponse.setRegion(region);

        // Species filtered species by category + metrics
        speciesResponse.getSpeciesByCategory().setCategory(speciesCategoryFilter.name());
        speciesResponse.getSpeciesByCategory().setSpecies(filteredSpeciesByCategory);

        for (SpeciesMeasure speciesMeasure : speciesMeasures) {
            String concatenatedTitles = concatenateSpeciesMeasureTiles(speciesMeasure);
            speciesResponse
                    .getSpeciesByCategory()
                    .getConservationMeasures()
                    .add(SpeciesMeasureResponse.builder()
                            .id(speciesMeasure.getId())
                            .titles(concatenatedTitles)
                            .build());
        }

        // Species filtered species by category + metrics
        speciesResponse.getSpeciesByClass().setClassName(speciesClassNameEnum.name());
        speciesResponse.getSpeciesByClass().setSpecies(filteredSpeciesByClass);

        return speciesResponse;
    }
}
