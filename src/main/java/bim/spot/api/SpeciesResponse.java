package bim.spot.api;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class SpeciesResponse {

    List<SpeciesMeasureResponse> species_measures = new ArrayList<>();

    @Data
    @AllArgsConstructor
    public static class SpeciesMeasureResponse {

        private String id;

        private String titles;
    }
}
