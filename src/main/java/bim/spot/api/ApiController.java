package bim.spot.api;

import bim.spot.api.icu.IcuService;
import bim.spot.api.icu.SpeciesCategoryEnum;
import bim.spot.api.icu.SpeciesClassNameEnum;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ApiController {

    @Autowired
    private IcuService icuService;

    @GetMapping("preview")
    public ResponseEntity preview(@RequestParam(value = "region", required = false) String region) {
        SpeciesResponse response;
        if (Strings.isNotBlank(region)) {
            response = icuService.preview(region, 0, SpeciesCategoryEnum.CR, SpeciesClassNameEnum.MAMMALIA);
        } else {
            response = icuService.preview(0, SpeciesCategoryEnum.CR, SpeciesClassNameEnum.MAMMALIA);
        }

        return ResponseEntity.ok(response);
    }

}
