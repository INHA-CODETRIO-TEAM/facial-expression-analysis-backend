package faceproject.faceanalysis.controller;

import faceproject.faceanalysis.domain.EmotionTest;
import faceproject.faceanalysis.dto.EmotionSetDto;
import faceproject.faceanalysis.dto.EmotionTestDto;
import faceproject.faceanalysis.dto.IdList;
import faceproject.faceanalysis.dto.ResultDto;
import faceproject.faceanalysis.service.EmotionTestService;
import faceproject.faceanalysis.service.FeedbackService;
import faceproject.faceanalysis.service.ResultService;
import faceproject.faceanalysis.service.SampleService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


@RestController
@CrossOrigin
@RequiredArgsConstructor
public class EmotionTestController {

    private final EmotionTestService emotionTestService;
    private final SampleService sampleService;
    private final FeedbackService feedbackService;
    private final ResultService resultService;

    @GetMapping("api/test/start")
    public TestStartResponse testStart() throws Exception {
        List<EmotionTestDto> tests = new ArrayList<>();

        Long[] sampleNum = new Long[3];
        for(int i = 0; i < 3; i++) {
            EmotionTest emotionTest = new EmotionTest();
            while(true) {
                boolean isRedundant = false;
                sampleNum[i] = sampleService.randomSample();
                for(int j = 0; j < i; j++) {
                    if(sampleNum[i].equals(sampleNum[j])) {
                        isRedundant = true;
                    }
                }
                if(!isRedundant) break;
            }
            emotionTestService.setTestSample(emotionTest, sampleNum[i]);

            Long id = emotionTestService.generateTest(emotionTest);
            Long sampleId = emotionTest.getSample().getId();
            String sampleImgUrl = sampleService.getSampleImage(sampleId);
            byte[] sampleImg = emotionTestService.getImage(sampleImgUrl);
            String sampleComment = sampleService.getSampleComment(sampleId);

            tests.add(new EmotionTestDto(id, sampleImg, sampleComment));
        }
        return new TestStartResponse(tests);
    }

    @PostMapping("/api/test/result")
    public TestResultResponse testResult(@RequestBody IdList ids) throws Exception {
        List<ResultDto> results = new ArrayList<>();
        List<Long> id = ids.getIds();

        for(int i = 0; i < 3; i++) {
            Long tempId = id.get(i);
            EmotionTest emotionTest = emotionTestService.getTest(tempId);
            String sampleUrl = emotionTest.getSample().getImgUri();
            String faceUrl = emotionTest.getFaceUrl();
            byte[] faceImg = emotionTestService.getImage(faceUrl);
            byte[] sampleImg = emotionTestService.getImage(sampleUrl);


            EmotionSetDto result = resultService.getResult(tempId);
            EmotionSetDto feedback = feedbackService.getFeedback(tempId);
            BigDecimal error = emotionTestService.calculateError(tempId);

            results.add(new ResultDto(faceImg, sampleImg, result, feedback, error));

        }

        return new TestResultResponse(results);
    }



    @Data
    static class TestStartResponse {
        private List<EmotionTestDto> tests;

        public TestStartResponse(List<EmotionTestDto> tests) {
            this.tests = tests;
        }
    }

    @Data
    static class TestResultResponse {
        private List<ResultDto> results;

        public TestResultResponse(List<ResultDto> results) {
            this.results = results;
        }
    }
}
