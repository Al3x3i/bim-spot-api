package bim.spot.api;

import bim.spot.api.icu.AvailableSpecies.Species;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
public class SpeciesResponse {

    private String region;

    private CategorySpeciesResponse speciesByCategory = new CategorySpeciesResponse();
    private SpeciesByClassName speciesByClass = new SpeciesByClassName();

    @Data
    public static class CategorySpeciesResponse {

        private String category;

        private List<Species> species = new ArrayList<>();

        private List<SpeciesMeasureResponse> conservationMeasures = new ArrayList<>();
    }

    @Data
    @Builder
    public static class SpeciesMeasureResponse {

        private String id;

        private String titles;
    }

    @Data
    public static class SpeciesByClassName {

        private String className;

        List<Species> species = new ArrayList<>();
    }
}
