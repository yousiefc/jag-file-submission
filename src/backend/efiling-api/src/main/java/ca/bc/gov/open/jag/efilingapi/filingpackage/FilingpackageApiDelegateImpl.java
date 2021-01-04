package ca.bc.gov.open.jag.efilingapi.filingpackage;

import ca.bc.gov.open.jag.efilingapi.api.FilingpackageApiDelegate;
import ca.bc.gov.open.jag.efilingapi.api.model.FilingPackage;
import ca.bc.gov.open.jag.efilingapi.error.EfilingErrorBuilder;
import ca.bc.gov.open.jag.efilingapi.error.ErrorResponse;
import ca.bc.gov.open.jag.efilingapi.filingpackage.service.FilingPackageService;
import ca.bc.gov.open.jag.efilingapi.utils.SecurityUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.annotation.security.RolesAllowed;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Service
public class FilingpackageApiDelegateImpl implements FilingpackageApiDelegate {

    private final FilingPackageService filingPackageService;

    public FilingpackageApiDelegateImpl(FilingPackageService filingPackageService) {
        this.filingPackageService = filingPackageService;
    }

    @Override
    @RolesAllowed("efiling-user")
    public ResponseEntity<FilingPackage> getFilingPackage(BigDecimal packageIdentifier) {

        Optional<UUID> universalId = SecurityUtils.getUniversalIdFromContext();

        if(!universalId.isPresent()) return new ResponseEntity(
                EfilingErrorBuilder.builder().errorResponse(ErrorResponse.MISSING_UNIVERSAL_ID).create(), HttpStatus.FORBIDDEN);


        Optional<FilingPackage> result = filingPackageService.getCSOFilingPackage(universalId.get(), packageIdentifier);

        if (!result.isPresent()) return new ResponseEntity(
                EfilingErrorBuilder.builder().errorResponse(ErrorResponse.FILING_PACKAGE_NOT_FOUND).create(), HttpStatus.NOT_FOUND);

        return ResponseEntity.ok(result.get());
    }
}