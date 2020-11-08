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

    private AvailableSpecies availableSpecies;

    @Test
    void should_filter_species_by_category() {
        // GIVEN
        givenAvailableSpecies();

        // WHEN
        List<Species> species = icuService.filterSpeciesByCategory(availableSpecies.getResult(), SpeciesCategoryEnum.CR);

        // THEN
        then(species.size()).isEqualTo(1);
        then(species.get(0).getCategory()).isEqualTo(SpeciesCategoryEnum.CR.name());
    }

    @Test
    void should_filter_species_by_class_name() {
        // GIVEN
        AvailableSpecies availableSpecies = givenAvailableSpecies();

        // WHEN
        List<Species> species = icuService.filterSpeciesByClassName(availableSpecies.getResult(), SpeciesClassNameEnum.MAMMALIA);

        // THEN
        then(species.size()).isEqualTo(1);
        then(species.get(0).getClass_name()).isEqualTo(SpeciesClassNameEnum.MAMMALIA.name());
    }

    private AvailableSpecies givenAvailableSpecies() {
        availableSpecies = new AvailableSpecies();
        availableSpecies.setResult(new ArrayList<>());
        availableSpecies.getResult().add(Species.builder()
                .category(SpeciesCategoryEnum.CR.name())
                .class_name(SpeciesClassNameEnum.MAMMALIA.name())
                .build());
        availableSpecies.getResult().add(Species.builder()
                .category(SpeciesCategoryEnum.VU.name())
                .class_name(SpeciesClassNameEnum.REPTILIA.name())
                .build());
        return availableSpecies;
    }
}
