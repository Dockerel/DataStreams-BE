package datastreams_knu.bigpicture.report.service.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReportEvaluationResult {
    private int issue_identification;
    private int factual_accuracy;
    private int relevance_to_interest;
    private int structural_coherence;
    private int clarity_of_expression;
    private int alignment_with_investor_profile;
    private int grammar_and_typos;

    public int getScore() {
        return issue_identification
                + factual_accuracy
                + relevance_to_interest
                + structural_coherence
                + clarity_of_expression
                + alignment_with_investor_profile
                + grammar_and_typos;
    }
}
