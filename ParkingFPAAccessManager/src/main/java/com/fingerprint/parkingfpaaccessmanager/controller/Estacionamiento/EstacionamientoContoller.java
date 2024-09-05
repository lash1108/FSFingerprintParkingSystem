package com.fingerprint.parkingfpaaccessmanager.controller.Estacionamiento;

import com.fingerprint.parkingfpaaccessmanager.model.pojos.consume.ConsumeJsonGeneric;
import com.fingerprint.parkingfpaaccessmanager.model.pojos.response.ResponseJsonGeneric;
import com.fingerprint.parkingfpaaccessmanager.model.pojos.util.ResponseJsonHandler;
import com.fingerprint.parkingfpaaccessmanager.service.Searchers.SearchService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = {"*"})
@RestController
@RequestMapping({"/est"})
public class EstacionamientoContoller {

    private final SearchService searchService;

    public EstacionamientoContoller(SearchService searchService) {
        this.searchService = searchService;
    }

    @PostMapping(value = {"/searchEstByParam"}, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseJsonGeneric> searchEstByParam(@RequestBody ConsumeJsonGeneric consume) {
        ResponseJsonGeneric response;
        try {
            response = searchService.SearchEstByRangeKeyOrType(consume);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ResponseJsonHandler errorResponse = new ResponseJsonHandler();
            response = errorResponse.serverErrorResponse("An error occurred: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

}
