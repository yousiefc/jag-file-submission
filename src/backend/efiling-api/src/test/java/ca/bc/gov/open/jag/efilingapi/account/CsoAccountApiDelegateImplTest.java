package ca.bc.gov.open.jag.efilingapi.account;

import ca.bc.gov.open.jag.efilingapi.Keys;
import ca.bc.gov.open.jag.efilingapi.TestHelpers;
import ca.bc.gov.open.jag.efilingapi.account.mappers.CsoAccountMapper;
import ca.bc.gov.open.jag.efilingapi.account.mappers.CsoAccountMapperImpl;
import ca.bc.gov.open.jag.efilingapi.account.service.AccountService;
import ca.bc.gov.open.jag.efilingapi.api.model.CreateCsoAccountRequest;
import ca.bc.gov.open.jag.efilingapi.api.model.CsoAccount;
import ca.bc.gov.open.jag.efilingapi.api.model.EfilingError;
import ca.bc.gov.open.jag.efilingcommons.exceptions.EfilingAccountServiceException;
import ca.bc.gov.open.jag.efilingcommons.model.AccountDetails;
import org.junit.jupiter.api.*;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.representations.AccessToken;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("CsoAccountApiDelegateImpl Test Suite")
public class CsoAccountApiDelegateImplTest {

    private static final String LAST_NAME = "lastName";
    private static final String MIDDLE_NAME = "middleName";
    private static final String EMAIL = "email@email.com";
    private static final String FIRST_NAME = "firstName";
    private static final String INTERNAL_CLIENT_NUMBER = "123456";

    private CsoAccountApiDelegateImpl sut;

    @Mock
    private AccountService accountServiceMock;

    @Mock
    private SecurityContext securityContextMock;

    @Mock
    private Authentication authenticationMock;

    @Mock
    private KeycloakPrincipal keycloakPrincipalMock;

    @Mock
    private KeycloakSecurityContext keycloakSecurityContextMock;

    @Mock
    private AccessToken tokenMock;

    @BeforeEach
    public void setUp() {

        MockitoAnnotations.initMocks(this);

        AccountDetails accountDetails = AccountDetails.builder()
                .fileRolePresent(true)
                .accountId(BigDecimal.ONE)
                .clientId(BigDecimal.TEN)
                .cardRegistered(true)
                .universalId(UUID.randomUUID())
                .internalClientNumber(INTERNAL_CLIENT_NUMBER)
                .universalId(TestHelpers.CASE_1).create();

        Mockito
                .doReturn(accountDetails)
                .when(accountServiceMock)
                .createAccount(Mockito.eq(TestHelpers.CASE_1), Mockito.any());

        Mockito
                .doReturn(accountDetails)
                .when(accountServiceMock)
                .getCsoAccountDetails(Mockito.eq(TestHelpers.CASE_1));

        Mockito
                .doReturn(null)
                .when(accountServiceMock)
                .getCsoAccountDetails(Mockito.eq(TestHelpers.CASE_2));

        Mockito
                .doThrow(new EfilingAccountServiceException("random"))
                .when(accountServiceMock)
                .createAccount(Mockito.eq(TestHelpers.CASE_2), Mockito.any());

        Mockito.when(securityContextMock.getAuthentication()).thenReturn(authenticationMock);
        Mockito.when(authenticationMock.getPrincipal()).thenReturn(keycloakPrincipalMock);
        Mockito.when(keycloakPrincipalMock.getKeycloakSecurityContext()).thenReturn(keycloakSecurityContextMock);
        Mockito.when(keycloakSecurityContextMock.getToken()).thenReturn(tokenMock);

        SecurityContextHolder.setContext(securityContextMock);

        // Testing mapper in this test
        CsoAccountMapper csoAccountMapper = new CsoAccountMapperImpl();

        sut = new CsoAccountApiDelegateImpl(accountServiceMock, csoAccountMapper);
    }


    @Test
    @DisplayName("201: should return an account with cso")
    public void whenAccountCreatedShouldReturn201() {

        Map<String, Object> otherClaims = new HashMap<>();
        otherClaims.put(Keys.UNIVERSAL_ID_CLAIM_KEY, TestHelpers.CASE_1);
        Mockito.when(tokenMock.getOtherClaims()).thenReturn(otherClaims);

        CreateCsoAccountRequest request = new CreateCsoAccountRequest();
        request.setLastName(LAST_NAME);
        request.setMiddleName(MIDDLE_NAME);
        request.setEmail(EMAIL);
        request.setFirstName(FIRST_NAME);
        ResponseEntity<CsoAccount> actual = sut.createAccount(TestHelpers.CASE_1, request);

        Assertions.assertEquals(HttpStatus.CREATED, actual.getStatusCode());
        Assertions.assertEquals("1", actual.getBody().getAccountId());
        Assertions.assertEquals("10", actual.getBody().getClientId());
        Assertions.assertEquals(true, actual.getBody().getFileRolePresent());
        Assertions.assertEquals(INTERNAL_CLIENT_NUMBER, actual.getBody().getInternalClientNumber());

    }

    @Test
    @DisplayName("403: when universal id is missing should return 403")
    public void withUserNotHavingUniversalIdShouldReturn403() {

        Map<String, Object> otherClaims = new HashMap<>();
        Mockito.when(tokenMock.getOtherClaims()).thenReturn(otherClaims);

        CreateCsoAccountRequest request = new CreateCsoAccountRequest();
        request.setLastName(LAST_NAME);
        request.setMiddleName(MIDDLE_NAME);
        request.setEmail(EMAIL);
        request.setFirstName(FIRST_NAME);
        ResponseEntity actual = sut.createAccount(TestHelpers.CASE_1, request);

        assertEquals(HttpStatus.FORBIDDEN, actual.getStatusCode());
        assertEquals("MISSING_UNIVERSAL_ID", ((EfilingError)actual.getBody()).getError());
        assertEquals("universal-id claim missing in jwt token.", ((EfilingError)actual.getBody()).getMessage());

    }


    @Test
    @DisplayName("500: when exception should return 500")
    public void whenEfilingAccountServiceExceptionShouldReturn500() {

        Map<String, Object> otherClaims = new HashMap<>();
        otherClaims.put(Keys.UNIVERSAL_ID_CLAIM_KEY, TestHelpers.CASE_2);
        Mockito.when(tokenMock.getOtherClaims()).thenReturn(otherClaims);

        CreateCsoAccountRequest request = new CreateCsoAccountRequest();
        request.setLastName(LAST_NAME);
        request.setMiddleName(MIDDLE_NAME);
        request.setEmail(EMAIL);
        request.setFirstName(FIRST_NAME);
        ResponseEntity actual = sut.createAccount(TestHelpers.CASE_2, request);

        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, actual.getStatusCode());

    }

    @Test
    @DisplayName("200: should return a cso account")
    public void withExistingAccountShouldReturnAccount() {

        Map<String, Object> otherClaims = new HashMap<>();
        otherClaims.put(Keys.UNIVERSAL_ID_CLAIM_KEY, TestHelpers.CASE_1);
        Mockito.when(tokenMock.getOtherClaims()).thenReturn(otherClaims);

        ResponseEntity<CsoAccount> actual = sut.getCsoAccount(TestHelpers.CASE_1);

        Assertions.assertEquals(HttpStatus.OK, actual.getStatusCode());

    }

    @Test
    @DisplayName("404: with account not found should return not found")
    public void withNoAccountShouldReturnNotFound() {

        Map<String, Object> otherClaims = new HashMap<>();
        otherClaims.put(Keys.UNIVERSAL_ID_CLAIM_KEY, TestHelpers.CASE_2);
        Mockito.when(tokenMock.getOtherClaims()).thenReturn(otherClaims);

        ResponseEntity<CsoAccount> actual = sut.getCsoAccount(TestHelpers.CASE_1);

        Assertions.assertEquals(HttpStatus.NOT_FOUND, actual.getStatusCode());

    }

    @Test
    @DisplayName("403: when getting account universal id is missing should return 403")
    public void getAccountWithUserNotHavingUniversalIdShouldReturn403() {

        ResponseEntity<CsoAccount> actual = sut.getCsoAccount(TestHelpers.CASE_3);

        Assertions.assertEquals(HttpStatus.FORBIDDEN, actual.getStatusCode());

    }


}
