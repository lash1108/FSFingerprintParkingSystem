package com.fingerprint.parkingfpaaccessmanager.controller.Estacionamiento;

import com.fingerprint.parkingfpaaccessmanager.model.pojos.consume.ConsumeJsonGeneric;
import com.fingerprint.parkingfpaaccessmanager.model.pojos.response.ResponseJsonGeneric;
import com.fingerprint.parkingfpaaccessmanager.service.estacionamiento.EstacionamientoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = {"*"})
@RestController
@RequestMapping({"/est"})
public class EstacionamientoContoller {

    @Autowired
    private EstacionamientoService estacionamientoService;

    @PostMapping(value = {"/CreateOrUpdateEst"}, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseJsonGeneric> CreateOrUpdateEst(@RequestBody ConsumeJsonGeneric consume) {
        return ResponseEntity.ok(estacionamientoService.createOrUpdateEstacionamiento(consume));
    }

    @PostMapping(value = {"/findEstacionamientos"}, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseJsonGeneric> findEstacionamientos(@RequestBody ConsumeJsonGeneric consume) {
        return ResponseEntity.ok(estacionamientoService.chooseEstSearchProvider(consume));
    }
}
