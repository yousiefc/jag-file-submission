package ca.bc.gov.open.jag.efilingapi.courts;

import ca.bc.gov.open.jag.efilingapi.api.model.CourtLocations;
import org.junit.jupiter.api.*;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.math.BigDecimal;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("CourtsApiDelegateImpl test suite")
public class GetCourtLocationsTest {
    private final String COURTLEVEL = "COURTLEVEL";
    CourtsApiDelegateImpl sut;


    @BeforeAll
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        sut = new CourtsApiDelegateImpl();
    }

    @Test
    @DisplayName("200")
    public void withValidCourtNameReturnCourtLocations() {

        ResponseEntity<CourtLocations> actual = sut.getCourtLocations(COURTLEVEL);

        Assertions.assertEquals(HttpStatus.OK, actual.getStatusCode());
        Assertions.assertEquals(2, actual.getBody().getCourts().size());
        Assertions.assertEquals(BigDecimal.valueOf(1031), actual.getBody().getCourts().get(0).getId());
        Assertions.assertEquals("Campbell River",actual.getBody().getCourts().get(0).getName());
        Assertions.assertEquals("MockCode",actual.getBody().getCourts().get(0).getCode());
        Assertions.assertEquals(true,actual.getBody().getCourts().get(0).getIsSupremeCourt());
        Assertions.assertEquals("500 - 13th Avenue",actual.getBody().getCourts().get(0).getAddress().getAddressLine1());
        Assertions.assertEquals(null,actual.getBody().getCourts().get(0).getAddress().getAddressLine2());
        Assertions.assertEquals(null,actual.getBody().getCourts().get(0).getAddress().getAddressLine3());
        Assertions.assertEquals("V9W 6P1",actual.getBody().getCourts().get(0).getAddress().getPostalCode());
        Assertions.assertEquals("Campbell River",actual.getBody().getCourts().get(0).getAddress().getCityName());
        Assertions.assertEquals("British Columbia",actual.getBody().getCourts().get(0).getAddress().getProvinceName());
        Assertions.assertEquals("Canada",actual.getBody().getCourts().get(0).getAddress().getCountryName());

        Assertions.assertEquals(BigDecimal.valueOf(3521), actual.getBody().getCourts().get(1).getId());
        Assertions.assertEquals("Chilliwack",actual.getBody().getCourts().get(1).getName());
        Assertions.assertEquals("MockCode",actual.getBody().getCourts().get(1).getCode());
        Assertions.assertEquals(true,actual.getBody().getCourts().get(1).getIsSupremeCourt());
        Assertions.assertEquals("46085 Yale Road",actual.getBody().getCourts().get(1).getAddress().getAddressLine1());
        Assertions.assertEquals("  ",actual.getBody().getCourts().get(1).getAddress().getAddressLine2());
        Assertions.assertEquals("  ",actual.getBody().getCourts().get(1).getAddress().getAddressLine3());
        Assertions.assertEquals("V2P 2L8",actual.getBody().getCourts().get(1).getAddress().getPostalCode());
        Assertions.assertEquals("Chilliwack",actual.getBody().getCourts().get(1).getAddress().getCityName());
        Assertions.assertEquals("British Columbia",actual.getBody().getCourts().get(1).getAddress().getProvinceName());
        Assertions.assertEquals("Canada",actual.getBody().getCourts().get(1).getAddress().getCountryName());

    }
}
