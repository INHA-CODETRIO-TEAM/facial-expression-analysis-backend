package faceproject.faceanalysis.controller;

import faceproject.faceanalysis.service.EmotionTestService;
import faceproject.faceanalysis.service.ResultService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@CrossOrigin
@RequiredArgsConstructor
public class ResultController {

    private final ResultService resultService;
    private final EmotionTestService emotionTestService;

    @PostMapping(value = "/api/test/camera/{id}")
    public ResponseEntity<String> receivePhoto(
            @PathVariable Long id,
            @RequestParam("file1") MultipartFile file1,
            @RequestParam("file2") MultipartFile file2,
            @RequestParam("file3") MultipartFile file3,
            @RequestParam("file4") MultipartFile file4,
            @RequestParam("file5") MultipartFile file5) throws IOException {

        List<MultipartFile> files = new ArrayList<>(Arrays.asList(file1, file2, file3, file4, file5));

        // 사진 저장


        int result = resultService.savePhoto(id, files);
        switch (result) {
            case 0:
                break;
            case -1:
                return new ResponseEntity<>("Blocked: Wrong access", HttpStatus.FORBIDDEN);
            case -2:
                return new ResponseEntity<>("Error: Saving images failed", HttpStatus.INTERNAL_SERVER_ERROR);
            default:
                return new ResponseEntity<>("Error: Unknown error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // 사진 전송

        result = resultService.sendPhoto(id);

        switch (result) {
            case 0:
                break;
            case -1:
                return new ResponseEntity<>("Error: Analyzing image failed", HttpStatus.INTERNAL_SERVER_ERROR);
            default:
                return new ResponseEntity<>("Error: Unknown error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }


        // 사진 분석 후 대표사진 저장

        int sequence = resultService.selectCentralSequence(id);
        emotionTestService.updateFaceUrl(id, sequence);

        return new ResponseEntity<>("Success", HttpStatus.OK);
    }
}
