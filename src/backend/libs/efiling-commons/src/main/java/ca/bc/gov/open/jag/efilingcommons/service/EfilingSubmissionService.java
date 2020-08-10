package ca.bc.gov.open.jag.efilingcommons.service;

import ca.bc.gov.open.jag.efilingcommons.model.EfilingFilingPackage;
import ca.bc.gov.open.jag.efilingcommons.model.EfilingPackageAuthority;
import ca.bc.gov.open.jag.efilingcommons.model.EfilingService;

import java.math.BigDecimal;

public interface EfilingSubmissionService {
    BigDecimal submitFilingPackage(EfilingService service, EfilingFilingPackage filingPackage, EfilingPaymentService payment);

    EfilingService addService(EfilingService efilingService);

}
