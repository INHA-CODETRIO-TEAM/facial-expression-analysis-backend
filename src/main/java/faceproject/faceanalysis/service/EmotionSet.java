package faceproject.faceanalysis.service;

import faceproject.faceanalysis.domain.Feedback;
import faceproject.faceanalysis.domain.Result;
import lombok.Getter;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.List;

@Getter
public class EmotionSet {

    private BigDecimal angry;
    private BigDecimal fear;
    private BigDecimal happy;
    private BigDecimal sad;
    private BigDecimal surprise;
    private BigDecimal neutral;

    public EmotionSet() {
        angry = new BigDecimal("0");
        fear = new BigDecimal("0");
        happy = new BigDecimal("0");
        sad = new BigDecimal("0");
        surprise = new BigDecimal("0");
        neutral = new BigDecimal("0");
    }

    public void calculateAverage(List<Result> results) {
        BigDecimal listSize = new BigDecimal(Integer.toString(results.size()));

        for(Result r : results) {
            angry = angry.add(r.getAngry());
            fear = fear.add(r.getFear());
            happy = happy.add(r.getHappy());
            sad = sad.add(r.getSad());
            surprise = surprise.add(r.getSurprise());
            neutral = neutral.add(r.getNeutral());
        }

        angry = angry.divide(listSize, 3, RoundingMode.HALF_EVEN);
        fear = fear.divide(listSize, 3, RoundingMode.HALF_EVEN);
        happy = happy.divide(listSize, 3, RoundingMode.HALF_EVEN);
        sad = sad.divide(listSize, 3, RoundingMode.HALF_EVEN);
        surprise = surprise.divide(listSize, 3, RoundingMode.HALF_EVEN);
        neutral = neutral.divide(listSize, 3, RoundingMode.HALF_EVEN);
    }

    public BigDecimal calculateDispersion(Result result) {
        BigDecimal dispersion = new BigDecimal("0");

        BigDecimal tempAngry = result.getAngry();
        BigDecimal tempFear = result.getFear();
        BigDecimal tempHappy = result.getHappy();
        BigDecimal tempSad = result.getSad();
        BigDecimal tempSurprise = result.getSurprise();
        BigDecimal tempNeutral = result.getNeutral();

        dispersion = dispersion.add(square(tempAngry.subtract(angry)));
        dispersion = dispersion.add(square(tempFear.subtract(fear)));
        dispersion = dispersion.add(square(tempHappy.subtract(happy)));
        dispersion = dispersion.add(square(tempSad.subtract(sad)));
        dispersion = dispersion.add(square(tempSurprise.subtract(surprise)));
        dispersion = dispersion.add(square(tempNeutral.subtract(neutral)));

        return dispersion;
    }

    public BigDecimal calculateError(Feedback feedback) {
        BigDecimal dispersion = new BigDecimal("0");

        BigDecimal tempAngry = feedback.getAngry();
        BigDecimal tempFear = feedback.getFear();
        BigDecimal tempHappy = feedback.getHappy();
        BigDecimal tempSad = feedback.getSad();
        BigDecimal tempSurprise = feedback.getSurprise();
        BigDecimal tempNeutral = feedback.getNeutral();

        dispersion = dispersion.add(square(tempAngry.subtract(angry)));
        dispersion = dispersion.add(square(tempFear.subtract(fear)));
        dispersion = dispersion.add(square(tempHappy.subtract(happy)));
        dispersion = dispersion.add(square(tempSad.subtract(sad)));
        dispersion = dispersion.add(square(tempSurprise.subtract(surprise)));
        dispersion = dispersion.add(square(tempNeutral.subtract(neutral)));

        dispersion = dispersion.divide(new BigDecimal("6"),3,RoundingMode.HALF_EVEN);
        return dispersion.sqrt(new MathContext(4)).setScale(3,RoundingMode.HALF_EVEN);
    }

    public BigDecimal square(BigDecimal bigDecimal) {
        return bigDecimal.multiply(bigDecimal);
    }
}