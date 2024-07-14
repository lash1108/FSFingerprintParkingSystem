package com.fingerprint.parkingfpaaccessmanager.controller.Registry;

import com.fingerprint.parkingfpaaccessmanager.model.pojos.consume.ConsumeJsonString;
import com.fingerprint.parkingfpaaccessmanager.model.pojos.consume.ConsumeJsonStringString;
import com.fingerprint.parkingfpaaccessmanager.model.pojos.response.ResponseJsonGeneric;
import com.fingerprint.parkingfpaaccessmanager.model.pojos.response.ResponseJsonStringString;
import com.fingerprint.parkingfpaaccessmanager.service.registro.RegistroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = {"*"})
@RestController
@RequestMapping({"/registry"})
public class RegistryController {

    @Autowired
    RegistroService registroService;

    @GetMapping(value = {"/findLastRegistryRecord"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseJsonStringString> findLastRegistryRecord() {
        return ResponseEntity.ok(registroService.findLastRegistryRecord());
    }

    @PostMapping(value = {"/setNewRegistryByToken"}, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseJsonGeneric> setNewRegistryByToken(@RequestBody ConsumeJsonString consume) {
        return ResponseEntity.ok(registroService.setNewRegistryByToken(consume));
    }

    @PostMapping(value = {"/findRegistryByToken"}, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseJsonGeneric> findRegistryByToken(@RequestBody ConsumeJsonStringString consume) {
        return ResponseEntity.ok(registroService.findRegistryByToken(consume));
    }

    @PostMapping(value = {"/unsetNewRegistryByToken"}, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseJsonGeneric> unsetNewRegistryByToken(@RequestBody ConsumeJsonString consume) {
        return ResponseEntity.ok(registroService.unsetRegistryByToken(consume));
    }

    @GetMapping(value = {"/findAllRegistries"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseJsonGeneric> findAllRegistries() {
        return ResponseEntity.ok(registroService.findAllRegistries());
    }


}
