package ca.bc.gov.open.jag.efilingcsostarter.mappers;

import ca.bc.gov.ag.csows.services.Service;
import ca.bc.gov.ag.csows.services.ServiceSession;
import ca.bc.gov.open.jag.efilingcommons.model.AccountDetails;
import ca.bc.gov.open.jag.efilingcommons.model.FilingPackage;
import ca.bc.gov.open.jag.efilingcsostarter.Keys;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = { DateMapper.class })
public interface ServiceMapper {

    @Mapping(target = "accountId", source = "accountDetails.accountId")
    @Mapping(target = "clientId", source = "accountDetails.clientId")
    @Mapping(target = "entDtm", qualifiedByName= {"DateMapper", "toCurrentDateTime"})
    @Mapping(target = "courtFileNo", source = "filingPackage.court.fileNumber")
    @Mapping(target = "entUserId", source = "accountDetails.clientId")
    @Mapping(target = "serviceReceivedDtm", qualifiedByName= {"DateMapper", "toCurrentDateTime"})
    @Mapping(target = "serviceSubtypeCd", constant = Keys.SERVICE_SUBTYPE_CD)
    @Mapping(target = "serviceTypeCd", constant = Keys.SERVICE_TYPE_CD)
    @Mapping(target = "feePaidYn", constant = "false")
    @Mapping(target = "userSessionId", source = "serviceSession.userSessionId")
    @Mapping(target = "serviceSessionId", source = "serviceSession.serviceSessionId")
    Service  toCreateService(FilingPackage filingPackage, AccountDetails accountDetails, ServiceSession serviceSession);

}
