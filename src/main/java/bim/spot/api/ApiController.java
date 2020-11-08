package bim.spot.api;

import bim.spot.api.icu.IcuService;
import bim.spot.api.icu.SpeciesCategoryEnum;
import bim.spot.api.icu.SpeciesClassNameEnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@Api(tags = "BIM SPOT API")
public class ApiController {

    @Autowired
    private IcuService icuService;

    @GetMapping(value = "preview", produces = {APPLICATION_JSON_VALUE})
    @ApiOperation("Returns list of Species filtered by `Critically Endangered`")
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
