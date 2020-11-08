package bim.spot.api.icu;

import java.util.List;
import lombok.Data;

@Data
public class AvailableSpecies {

    private String count;

    private String regionIdentifier;

    private String page;

    private List<Species> result;

    @Data
    public static class Species {

        private String taxonid;

        private String kingdom_name;

        private String phylum_name;

        private String class_name;

        private String order_name;

        private String family_name;

        private String genus_name;

        private String scientific_name;

        private Object infra_rank;

        private Object infra_name;

        private Object population;

        private String category;
    }
}
