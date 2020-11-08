package bim.spot.api.icu;


import bim.spot.api.SpeciesResponse;
import bim.spot.api.icu.AvailableRegions.Region;
import java.util.concurrent.CompletableFuture;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.BDDAssertions.then;

@SpringBootTest
public class IcuIntegrationServiceTest {

    @Autowired
    private IcuService icuService;

    @Test
    public void should_preview() {

        // GIVEN // WHEN
        SpeciesResponse speciesResponse = icuService.preview(0, SpeciesCategoryEnum.CR, SpeciesClassNameEnum.MAMMALIA);

        // THEN
        then(speciesResponse.getRegion()).isNotNull();
        then(speciesResponse.getSpeciesByCategory().getCategory()).isNotNull();
        then(speciesResponse.getSpeciesByCategory().getConservationMeasures().size()).isNotZero();
        then(speciesResponse.getSpeciesByCategory().getConservationMeasures().get(0).getId()).isNotNull();
        then(speciesResponse.getSpeciesByCategory().getConservationMeasures().get(0).getTitles()).isNotNull();
        then(speciesResponse.getSpeciesByCategory().getSpecies().size()).isNotZero();
        then(speciesResponse.getSpeciesByClass().getClassName()).isNotNull();
        then(speciesResponse.getSpeciesByClass().getSpecies().size()).isNotZero();
    }

    @Test
    public void should_select_random_region() {

        // GIVEN // WHEN
        Region availableRegions = icuService.getRandomRegion();

        // THEN
        then(availableRegions).isNotNull();
        then(availableRegions.getName()).isNotNull();
        then(availableRegions.getIdentifier()).isNotNull();
    }

    @Test
    void should_load_species_by_regions() {

        // GIVEN // WHEN
        AvailableSpecies availableSpecies = icuService.getSpeciesByRegion("europe", 0);

        // THEN
        then(availableSpecies.getCount()).isNotNull();
        then(availableSpecies.getResult().size()).isGreaterThan(0);
    }

    @Test
    @SneakyThrows
    void should_load_conservation_measures() {

        // GIVEN // WHEN
        CompletableFuture<SpeciesMeasure> completableFutureResult = icuService.fetchConservationMeasures("22823", "europe");
        SpeciesMeasure speciesMeasure = completableFutureResult.get();
        // THEN
        then(speciesMeasure.getResult()).isNotNull();
        then(speciesMeasure.getRegion_identifier()).isNotNull();
        then(speciesMeasure.getResult().size()).isGreaterThan(0);
    }
}
