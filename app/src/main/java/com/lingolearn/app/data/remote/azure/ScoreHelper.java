package com.lingolearn.app.data.remote.azure;

import com.microsoft.cognitiveservices.speech.PronunciationAssessmentResult;
import com.microsoft.cognitiveservices.speech.WordLevelTimingResult;

public class ScoreHelper {
    public static float getPronunciationScore(PronunciationAssessmentResult result) {
        if (result == null) return 0f;
        try {
            Double score = result.getPronunciationScore();
            return score != null ? score.floatValue() : 0f;
        } catch (Exception e) {
            return 0f;
        }
    }

    public static float getAccuracyScore(PronunciationAssessmentResult result) {
        if (result == null) return 0f;
        try {
            Double score = result.getAccuracyScore();
            return score != null ? score.floatValue() : 0f;
        } catch (Exception e) {
            return 0f;
        }
    }

    public static float getFluencyScore(PronunciationAssessmentResult result) {
        if (result == null) return 0f;
        try {
            Double score = result.getFluencyScore();
            return score != null ? score.floatValue() : 0f;
        } catch (Exception e) {
            return 0f;
        }
    }

    public static float getProsodyScore(PronunciationAssessmentResult result) {
        if (result == null) return 0f;
        try {
            Double score = result.getProsodyScore();
            return score != null ? score.floatValue() : 0f;
        } catch (Exception e) {
            return 0f;
        }
    }

    public static float getCompletenessScore(PronunciationAssessmentResult result) {
        if (result == null) return 0f;
        try {
            Double score = result.getCompletenessScore();
            return score != null ? score.floatValue() : 0f;
        } catch (Exception e) {
            return 0f;
        }
    }

    public static float getWordAccuracyScore(WordLevelTimingResult result) {
        if (result == null) return 0f;
        try {
            Double score = result.getAccuracyScore();
            return score != null ? score.floatValue() : 0f;
        } catch (Exception e) {
            return 0f;
        }
    }
}
