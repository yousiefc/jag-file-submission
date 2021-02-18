package ca.bc.gov.open.jag.efiling.page;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.springframework.beans.factory.annotation.Value;

public class KeycloakAuthenticationPageImpl extends BasePage implements AuthenticationPage {

    private Logger logger = LogManager.getLogger(KeycloakAuthenticationPageImpl.class);

    @Value("${USERNAME_BCEID:bobross}")
    private String username;

    @Value("${PASSWORD_BCEID:changeme}")
    private String password;

    //Page Objects:
    @FindBy(id = "username")
    private WebElement usernameField;

    @FindBy(id = "password")
    private WebElement passwordField;

    @FindBy(id = "kc-login")
    private WebElement signIn;

    @Override
    public String name() {
        return "keycloak";
    }

    @Override
    public void signIn() {

        logger.info("Waiting for the page to load...");
        wait.until(ExpectedConditions.titleIs("Sign in to Efiling Hub"));

        usernameField.sendKeys(username);
        passwordField.sendKeys(password);
        signIn.click();

    }

}
