package bim.spot.api.icu;

import java.util.List;
import lombok.Data;

@Data
public class SpeciesMeasure {
    private String id;
    private String region_identifier;
    private List<Measure> result;

    @Data
    public static class Measure {
        private String code;
        private String title;
    }
}
