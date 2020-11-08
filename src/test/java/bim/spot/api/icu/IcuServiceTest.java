package bim.spot.api.icu;

import bim.spot.api.icu.AvailableSpecies.Species;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.BDDAssertions.then;

@ExtendWith(MockitoExtension.class)
public class IcuServiceTest {

    @InjectMocks
    private ICUService icuService;


    @Test
    void should_filter_species() {
        // GIVEN
        AvailableSpecies availableSpecies = new AvailableSpecies();
        availableSpecies.setResult(new ArrayList<>());
        availableSpecies.getResult().add(Species.builder().category("CR").build());
        availableSpecies.getResult().add(Species.builder().category("LC").build());

        // WHEN
        List<Species> species = icuService.filterResultBySpeciesType(availableSpecies, "CR");

        // THEN
        then(species.size()).isEqualTo(1);
        then(species.get(0).getCategory()).isEqualTo("CR");
    }
}
