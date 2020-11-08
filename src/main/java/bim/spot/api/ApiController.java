package bim.spot.api;

import bim.spot.api.icu.ICUService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ApiController {


    private ICUService icuService;

    @GetMapping
    public ResponseEntity preview() {

        icuService.getAllRegions();

        return ResponseEntity.ok().build();
    }
}
