package com.fingerprint.parkingfpaaccessmanager.controller.util;

import com.fingerprint.parkingfpaaccessmanager.model.pojos.response.ResponseJsonStringString;
import com.fingerprint.parkingfpaaccessmanager.service.util.UtilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@CrossOrigin(origins = {"*"})
@RestController
@RequestMapping({"/util"})
public class UtilController {

    @Autowired
    UtilService utilService;

    @GetMapping(value = {"/getFormattedCurrentDateTime"}, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<ResponseJsonStringString> getFormattedCurrentDateTime() {
        return ResponseEntity.ok(utilService.getDateAndHourFormated(LocalDateTime.now()));
    }
}
