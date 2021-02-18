package ca.bc.gov.open.jag.efiling.page;

public interface AuthenticationPage {

    void goTo(String url);

    String name();

    void signIn();
}
