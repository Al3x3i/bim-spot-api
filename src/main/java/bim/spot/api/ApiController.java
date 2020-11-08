package bim.spot.api;

import bim.spot.api.icu.IcuService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ApiController {


    private IcuService icuService;

    @GetMapping
    public ResponseEntity preview() {


        return ResponseEntity.ok().build();
    }
}
