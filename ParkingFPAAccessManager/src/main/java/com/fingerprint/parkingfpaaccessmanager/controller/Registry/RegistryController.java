package com.fingerprint.parkingfpaaccessmanager.controller.Registry;

import com.fingerprint.parkingfpaaccessmanager.model.pojos.consume.ConsumeJsonGeneric;
import com.fingerprint.parkingfpaaccessmanager.model.pojos.response.ResponseJsonGeneric;
import com.fingerprint.parkingfpaaccessmanager.model.pojos.util.ResponseJsonHandler;
import com.fingerprint.parkingfpaaccessmanager.service.registro.RegistroService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = {"*"})
@RestController
@RequestMapping({"/registry"})
public class RegistryController {

    final
    RegistroService registroService;

    public RegistryController(RegistroService registroService) {
        this.registroService = registroService;
    }

    @PostMapping(value = {"/createOrUpdateRegistry"}, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseJsonGeneric> setNewRegistryByToken(@RequestBody ConsumeJsonGeneric consume) {
        ResponseJsonGeneric response;
        try {
            response = registroService.createOrUpdateRegistryByToken(consume);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ResponseJsonHandler errorResponse = new ResponseJsonHandler();
            response = errorResponse.serverErrorResponse("An error occurred: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping(value = {"/findRegistryByToken"}, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseJsonGeneric> findRegistryByToken(@RequestBody ConsumeJsonGeneric consume) {
        ResponseJsonGeneric response;
        try {
            response = registroService.findRegistryByToken(consume);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ResponseJsonHandler errorResponse = new ResponseJsonHandler();
            response = errorResponse.serverErrorResponse("An error occurred: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping(value = {"/findAllRegistries"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseJsonGeneric> findAllRegistries() {
        return ResponseEntity.ok(registroService.findAllRegistries());
    }


}
