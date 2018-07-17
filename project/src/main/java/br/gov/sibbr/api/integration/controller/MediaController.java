package br.gov.sibbr.api.integration.controller;

import br.gov.sibbr.api.core.http.RestResponse;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpHeaders;
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
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/media")
public class MediaController implements RestResponse {

    //Save the uploaded file to this folder
    private static String UPLOADED_FOLDER = "F://temp//";

    // 3.1.1 Single file upload
    @PostMapping("/api/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile uploadfile) {

        if (uploadfile.isEmpty())
            return ok("please select a file!");

        try {
            saveUploadedFiles(Arrays.asList(uploadfile));
        } catch (IOException e) {
            return badRequest();
        }

        return ok("Successfully uploaded - " + uploadfile.getOriginalFilename());

    }

    // 3.1.2 Multiple file upload
    @PostMapping("/api/upload/multi")
    public ResponseEntity<?> uploadFileMulti(@RequestParam("extraField") String extraField, @RequestParam("files") MultipartFile[] uploadfiles) {

        // Get file name
        String uploadedFileName = Arrays.stream(uploadfiles)
                .map(MultipartFile::getOriginalFilename)
                .filter(x -> !StringUtils.isEmpty(x))
                .collect(Collectors.joining(" , "));

        if (StringUtils.isEmpty(uploadedFileName))
            return notFound("please select a file!");

        try {
            saveUploadedFiles(Arrays.asList(uploadfiles));
        } catch (IOException e) {
            return badRequest();
        }

        return ok("Successfully uploaded - " + uploadedFileName);
    }

    //save file
    private void saveUploadedFiles(List<MultipartFile> files) throws IOException {

        for (MultipartFile file : files) {

            if (file.isEmpty()) {
                continue; //next pls
            }

            byte[] bytes = file.getBytes();
            Path path = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename());
            Files.write(path, bytes);

        }
    }
}
