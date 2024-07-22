package com.fingerprint.parkingfpaaccessmanager.controller.Usuario;

import com.fingerprint.parkingfpaaccessmanager.model.pojos.consume.ConsumeJsonGeneric;
import com.fingerprint.parkingfpaaccessmanager.model.pojos.consume.ConsumeJsonLong;
import com.fingerprint.parkingfpaaccessmanager.model.pojos.consume.ConsumeJsonString;
import com.fingerprint.parkingfpaaccessmanager.model.pojos.consume.ConsumeJsonStringString;
import com.fingerprint.parkingfpaaccessmanager.model.pojos.response.ResponseJsonBoolean;
import com.fingerprint.parkingfpaaccessmanager.model.pojos.response.ResponseJsonGeneric;
import com.fingerprint.parkingfpaaccessmanager.model.pojos.response.ResponseJsonLongString;
import com.fingerprint.parkingfpaaccessmanager.service.usuario.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static java.util.Objects.nonNull;


@CrossOrigin(origins = {"*"})
@RestController
@RequestMapping({"/usr"})
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping(value = {"/createOrUpdateUsr"}, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseJsonGeneric> createOrUpdateUsr(@RequestBody ConsumeJsonGeneric consume) {
        ResponseJsonGeneric response = usuarioService.createOrUpdateUser(consume);
        if (nonNull(response)) {
            return ResponseEntity.ok(response);
        }
        return null;
    }

    @PostMapping(value = {"/deleteUsrByCveUsr"}, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseJsonGeneric> deleteUsrByCveUsr(@RequestBody ConsumeJsonLong consume) {
        return ResponseEntity.ok(usuarioService.deleteUsrByCveusr(consume));
    }

    @PostMapping(value = {"/findAllUsers"}, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseJsonGeneric> findAllUsers(@RequestBody ConsumeJsonString consume) {
        return ResponseEntity.ok(usuarioService.findAllUsersByTypeusr(consume.getName()));
    }

    @PostMapping(value = {"/findUserByCveusr"}, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseJsonGeneric> findUserByCveusr(@RequestBody ConsumeJsonLong consume) {
        return ResponseEntity.ok(usuarioService.findUserByCveusr(consume));
    }

    @PostMapping(value = {"/validEmail"}, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseJsonLongString> validEmail(@RequestBody ConsumeJsonString consume) {
        ResponseJsonLongString response = new ResponseJsonLongString();
        ResponseJsonBoolean validEmail = usuarioService.existUsrByParam(consume.getName(), 1);
        if (validEmail.isSuccess()) {
            response.setLabel("Email Not Available");
            response.setValue(HttpStatus.BAD_REQUEST.value());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } else {
            response.setLabel("Email Available");
            response.setValue(HttpStatus.OK.value());
            return ResponseEntity.ok(response);
        }
    }

    @PostMapping(value = {"/validLogin"}, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseJsonLongString> validLogin(@RequestBody ConsumeJsonString consume) {
        ResponseJsonLongString response = new ResponseJsonLongString();
        ResponseJsonBoolean validEmail = usuarioService.existUsrByParam(consume.getName(), 2);
        if (validEmail.isSuccess()) {
            response.setLabel("Login Not Available");
            response.setValue(HttpStatus.BAD_REQUEST.value());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } else {
            response.setLabel("Login Available");
            response.setValue(HttpStatus.OK.value());
            return ResponseEntity.ok(response);
        }
    }

    @PostMapping(value = {"/findUsrByEmailAndPassword"}, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseJsonGeneric> findUsrByParamAndPassword(@RequestBody ConsumeJsonStringString consume) {
        return ResponseEntity.ok(usuarioService.findUsrByParamAndPassword(
                consume.getValue1(), consume.getValue2(), 1));
    }

    @PostMapping(value = {"/findUserByLoginAndPassword"}, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseJsonGeneric> findUserByLoginAndPassword(@RequestBody ConsumeJsonStringString consume) {
        return ResponseEntity.ok(usuarioService.findUsrByParamAndPassword(
                consume.getValue1(), consume.getValue2(), 2));
    }

    @PostMapping(value = {"/findUserByToken"}, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseJsonGeneric> findUserByToken(@RequestBody ConsumeJsonString consume) {
        return ResponseEntity.ok(usuarioService.findUsrByParamAndPassword(
                consume.getName(), null, 3));
    }

}
