
package ca.bc.gov.open.jag.efiling.stepDefinitions;

import ca.bc.gov.open.jag.efiling.error.EfilingTestException;
import ca.bc.gov.open.jag.efiling.page.AuthenticationPage;
import ca.bc.gov.open.jag.efiling.page.PackageConfirmationPage;
import ca.bc.gov.open.jag.efiling.services.GenerateUrlService;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.util.List;

public class AuthenticateAndRedirectToEfilingHubSD {

    private final AuthenticationPage authenticationPage;
    private final PackageConfirmationPage packageConfirmationPage;
    private final GenerateUrlService generateUrlService;

    private static final String EFILE_SUBMISSION_PAGE_TITLE = "E-File submission";
    private static final String INITIAL_DOCUMENT_NAME = "test-document.pdf";

    @Value("${test:bceid}")
    private String provider;

    Logger log = LogManager.getLogger(AuthenticateAndRedirectToEfilingHubSD.class);

    public AuthenticateAndRedirectToEfilingHubSD(List<AuthenticationPage> authenticationPages, PackageConfirmationPage packageConfirmationPage, GenerateUrlService generateUrlService) {
        this.authenticationPage = authenticationPages.stream().filter(x -> StringUtils.equals(provider, x.name())).findFirst().get();
        this.packageConfirmationPage = packageConfirmationPage;
        this.generateUrlService = generateUrlService;
    }

    @Given("user is on the eFiling submission page")
    public void userIsOnTheEfilingSubmissionPage() throws IOException {

        String actualGeneratedRedirectUrl = generateUrlService.getGeneratedUrl();

        if(actualGeneratedRedirectUrl == null) throw new EfilingTestException("Redirect url is not generated.");

        this.authenticationPage.goTo(actualGeneratedRedirectUrl);
        this.authenticationPage.signIn();
        log.info("user is authenticated with keycloak");
    }

    @Then("Package information is displayed")
    public void verifyPackageInformation() {
        Assert.assertEquals(EFILE_SUBMISSION_PAGE_TITLE, this.packageConfirmationPage.verifyPageTitle());
        log.info("Efiling submission page title is verified");

        Assert.assertEquals(INITIAL_DOCUMENT_NAME, this.packageConfirmationPage.getInitialDocumentName());
        log.info("Actual document name matches the uploaded document name");

    }

    @And("continue to payment button is enabled")
    public void verifyContinueToPaymentButtonIsEnabled() {
        Assert.assertTrue(this.packageConfirmationPage.verifyContinuePaymentBtnIsEnabled());
        log.info("Continue payment button is enabled");
    }
}

