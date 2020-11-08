package bim.spot.api.icu;


import bim.spot.api.icu.AvailableSpecies.Species;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.BDDAssertions.then;

@SpringBootTest
public class IcuIntegrationServiceTest {

    @Autowired
    private ICUService icuService;

    @Test
    public void should_preview() {

        // GIVEN // WHEN
        List<Species> filteredSpecies = icuService.preview("europe", 0, SpeciesCategoryEnum.CR);

        // THEN
        then(filteredSpecies.size()).isNotZero();
    }

    @Test
    public void should_load_available_regions() {

        // GIVEN // WHEN
        AvailableRegions availableRegions = icuService.getAllRegions();

        // THEN
        then(availableRegions.getCount()).isNotNull();
        then(availableRegions.getResults().size()).isGreaterThan(0);
    }

    @Test
    void should_load_species_by_regions() {

        // GIVEN // WHEN
        AvailableSpecies availableSpecies = icuService.getSpeciesByRegion("europe", 0);

        // THEN
        then(availableSpecies.getCount()).isNotNull();
        then(availableSpecies.getResult().size()).isGreaterThan(0);
    }

}
