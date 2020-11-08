package bim.spot.api.icu;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class IcuIntegrationServiceTest {

    @Autowired
    private ICUService icuService;


    @Test
    public void should_load_available_regions() {

        // GIVEN // WHEN
        icuService.getAllRegions();

        // THEN
    }
}
