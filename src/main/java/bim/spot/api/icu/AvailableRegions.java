package bim.spot.api.icu;

import java.util.List;
import lombok.Data;

@Data
public class AvailableRegions {

    private String count;

    private List<Region> results;

    @Data
    public static class Region {

        private String name;

        private String identifier;
    }

}
