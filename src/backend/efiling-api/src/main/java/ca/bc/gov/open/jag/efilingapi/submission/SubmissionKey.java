package ca.bc.gov.open.jag.efilingapi.submission;

import java.util.UUID;

public class SubmissionKey {

    private String universalId;
    private UUID transactionId;
    private UUID submissionId;

    public SubmissionKey(String universalId, UUID transactionId, UUID submissionId) {
        this.universalId = universalId;
        this.transactionId = transactionId;
        this.submissionId = submissionId;
    }

    public String getUniversalId() {
        return universalId;
    }

    public UUID getTransactionId() {
        return transactionId;
    }

    public UUID getSubmissionId() {
        return submissionId;
    }
}
