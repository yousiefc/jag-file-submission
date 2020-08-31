package ca.bc.gov.open.jag.efilingcommons.service;

import ca.bc.gov.open.jag.efilingcommons.model.AccountDetails;
import ca.bc.gov.open.jag.efilingcommons.model.EfilingFilingPackage;
import ca.bc.gov.open.jag.efilingcommons.model.FilingPackage;

import java.math.BigDecimal;

public interface EfilingSubmissionService {

    BigDecimal submitFilingPackage(
            AccountDetails accountDetails,
            FilingPackage efilingPackage,
            EfilingFilingPackage filingPackage,
            boolean isRushedProcessing,
            EfilingPaymentService payment);

}
