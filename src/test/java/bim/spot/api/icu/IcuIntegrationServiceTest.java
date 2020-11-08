package bim.spot.api.icu;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.BDDAssertions.then;

@SpringBootTest
public class IcuIntegrationServiceTest {

    @Autowired
    private ICUService icuService;


    @Test
    public void should_load_available_regions() {

        // GIVEN // WHEN
        AvailableRegions availableRegions = icuService.getAllRegions();

        // THEN
        then(availableRegions.getCount()).isNotNull();
        then(availableRegions.getResults().size()).isGreaterThan(0);
    }
}
