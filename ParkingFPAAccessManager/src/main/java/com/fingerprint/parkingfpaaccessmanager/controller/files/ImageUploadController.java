package com.fingerprint.parkingfpaaccessmanager.controller.files;

import com.fingerprint.parkingfpaaccessmanager.model.pojos.response.ResponseJsonGeneric;
import com.fingerprint.parkingfpaaccessmanager.model.pojos.util.ResponseJsonHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("files")
public class ImageUploadController {

    private static String UPLOADED_FOLDER = "/home/alextcw/Files/images/";

    @PostMapping("/upload/image")
    public ResponseEntity<String> uploadImage(@RequestParam("file")MultipartFile file){
        ResponseJsonHandler response = new ResponseJsonHandler();

        if (file.isEmpty()) {
            return new ResponseEntity<>("The file cannot be empty",HttpStatus.BAD_REQUEST);
        }

        try {
            String fileName = file.getOriginalFilename();
            Path path = Paths.get(UPLOADED_FOLDER + fileName);

            Files.write(path, file.getBytes());
            return new ResponseEntity<>("Image uploaded successfully: " + fileName, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            e.getCause();
            return new ResponseEntity<>("Error uploading image. " + e.getCause(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
