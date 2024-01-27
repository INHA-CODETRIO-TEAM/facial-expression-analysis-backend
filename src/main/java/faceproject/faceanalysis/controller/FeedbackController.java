package faceproject.faceanalysis.controller;


import faceproject.faceanalysis.dto.FeedbackDto;
import faceproject.faceanalysis.service.FeedbackService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin

@RequiredArgsConstructor
public class FeedbackController {
    private final FeedbackService feedbackService;

    @PostMapping("api/test/feedback")
    public ResponseEntity<String> saveFeedback
            (@RequestParam Long id,
             @RequestBody FeedbackRequest request) {
        FeedbackDto feedbackForm = new FeedbackDto(
                id,
                request.getAngry(),
                request.getFear(),
                request.getHappy(),
                request.getSad(),
                request.getSurprise()
        );

        // feedbackService.convertToDecimal();
        Long saveStatus = feedbackService.saveFeedback(feedbackForm);

        if(saveStatus < 0) {
            return new ResponseEntity<>("Save Failed", HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            return new ResponseEntity<>("Success", HttpStatus.OK);
        }


    }


    @Data
    static class FeedbackRequest {
        private int angry;
        private int fear;
        private int happy;
        private int sad;
        private int surprise;
    }

    @Data
    static class FeedbackResponse {
            private String message;

        public FeedbackResponse(String message) {
            this.message = message;
        }
    }
}
