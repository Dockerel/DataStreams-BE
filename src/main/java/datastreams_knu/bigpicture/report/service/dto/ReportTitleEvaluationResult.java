package datastreams_knu.bigpicture.report.service.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReportTitleEvaluationResult {
    private int headline_relevance;
    private int engagement_potential;
    private int stylistic_quality;
    private int factual_alignment;
    private int alignment_with_reader_profile;
    private int grammar_and_typos;

    public int getScore() {
        return headline_relevance
                + engagement_potential
                + stylistic_quality
                + factual_alignment
                + alignment_with_reader_profile
                + grammar_and_typos;
    }
}
