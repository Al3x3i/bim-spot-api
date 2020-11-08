package bim.spot.api.icu;

import bim.spot.api.SpeciesResponse;
import bim.spot.api.SpeciesResponse.SpeciesMeasureResponse;
import bim.spot.api.icu.AvailableSpecies.Species;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static java.util.stream.Collectors.toList;

@Service
@Slf4j
public class ICUService {

    @Autowired
    private IcuClient icuClient;

    public AvailableRegions getAllRegions() {
        AvailableRegions result = icuClient.findAvailableRegions();
        log.info("Available '{}' regions", result.getCount());
        return result;
    }

    public AvailableSpecies getSpeciesByRegion(String region, int page) {
        AvailableSpecies result = icuClient.findSpecies(region, page);
        log.info("Available '{}' species for the region '{}' from the page '{}'", result.getCount(), region, page);
        return result;
    }

    public SpeciesResponse preview(String region, int page, SpeciesCategoryEnum speciesCategoryFilter, SpeciesClassNameEnum speciesClassNameEnum) {

        AvailableSpecies availableSpecies = getSpeciesByRegion(region, page);

        // 5
        List<Species> filteredSpeciesByCategory = filterSpeciesByCategory(availableSpecies.getResult(), speciesCategoryFilter);
        log.info("Filtered '{}' species by '{}' category", filteredSpeciesByCategory.size(), speciesCategoryFilter.name());

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
        log.info("Filtered '{}' species by '{}' class", filteredSpeciesByCategory.size(), speciesCategoryFilter.name());

        SpeciesResponse response = generateResponseModel(region, speciesCategoryFilter, filteredSpeciesByCategory,
                speciesClassNameEnum, filteredSpeciesByClass, speciesMeasures);

        return response;
    }

    SpeciesMeasure fetchConservationMeasures(String id, String region) {
        SpeciesMeasure result = icuClient.findSpeciesMeasure(id, region);
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
