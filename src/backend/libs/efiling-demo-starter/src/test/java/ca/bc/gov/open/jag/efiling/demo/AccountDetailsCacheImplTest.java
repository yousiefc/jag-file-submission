package ca.bc.gov.open.jag.efiling.demo;

import ca.bc.gov.open.jag.efilingcommons.model.AccountDetails;
import org.junit.jupiter.api.*;

import java.math.BigDecimal;
import java.util.UUID;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AccountDetailsCacheImplTest {

    private AccountDetailsCacheImpl sut;

    @Test
    @DisplayName("Cache test are useles")
    public void testCache() {
        sut = new AccountDetailsCacheImpl();
        Assertions.assertDoesNotThrow(() -> sut.get(UUID.randomUUID()));
        Assertions.assertDoesNotThrow(() -> sut.put(AccountDetails.builder().clientId(BigDecimal.TEN).create()));

    }


}
