package faceproject.faceanalysis.service;

import faceproject.faceanalysis.dto.EmotionSetDto;
import faceproject.faceanalysis.dto.FeedbackDto;
import faceproject.faceanalysis.domain.Feedback;
import faceproject.faceanalysis.repository.EmotionTestRepository;
import faceproject.faceanalysis.repository.FeedbackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final EmotionTestRepository emotionTestRepository;

    @Transactional
    public Long saveFeedback(FeedbackDto form) {
        Feedback feedback = new Feedback();

        try {
            convertToDecimal(feedback, form);
            feedbackRepository.save(feedback);
        } catch (Exception e) {
            return -1L;
        }

        return feedback.getId();
    }

    public EmotionSetDto getFeedback(Long id) {
        Feedback feedback = feedbackRepository.findByTestId(id).get(0);

        return new EmotionSetDto(
                feedback.getAngry(), feedback.getFear(),
                feedback.getHappy(), feedback.getSad(),
                feedback.getSurprise(), feedback.getNeutral());
    }


    public void convertToDecimal(Feedback feedback, FeedbackDto form) {
        BigDecimal angry = new BigDecimal(Integer.toString(form.getAngry()));
        BigDecimal fear = new BigDecimal(Integer.toString(form.getFear()));
        BigDecimal happy = new BigDecimal(Integer.toString(form.getHappy()));
        BigDecimal sad = new BigDecimal(Integer.toString(form.getSad()));
        BigDecimal surprise = new BigDecimal(Integer.toString(form.getSurprise()));
        BigDecimal neutral;

        BigDecimal highest = angry.max(fear.max(happy.max(sad.max(surprise))));
        BigDecimal sum = angry.add(fear.add(happy.add(sad.add(surprise))));
        BigDecimal rest = highest.divide(new BigDecimal("5"), 3, RoundingMode.HALF_EVEN);

        if(highest.equals(BigDecimal.ZERO)) {
            neutral = BigDecimal.ONE;
        } else {
            neutral = BigDecimal.ONE.subtract(rest);
            angry = angry.multiply(rest.divide(sum,3,RoundingMode.HALF_EVEN));
            fear = fear.multiply(rest.divide(sum,3,RoundingMode.HALF_EVEN));
            happy = happy.multiply(rest.divide(sum,3,RoundingMode.HALF_EVEN));
            sad = sad.multiply(rest.divide(sum,3,RoundingMode.HALF_EVEN));
            surprise = surprise.multiply(rest.divide(sum,3,RoundingMode.HALF_EVEN));
        }

        feedback.setFeedback(emotionTestRepository.findOne(form.getId()),
                 angry, fear, happy, sad, surprise, neutral);
    }
}
