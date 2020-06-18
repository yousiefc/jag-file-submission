package ca.bc.gov.open.jag.efilingaccountclient;

import java.util.ArrayList;
import java.util.List;

public class CsoAccountDetails {

    public CsoAccountDetails(String accountId, String clientId, String[] roles) {

        this.accountId = accountId;
        this.clientId = clientId;

        for (String role: roles) {
            String toAdd = role.toLowerCase();
            this.roles.add(toAdd);
        }
    }

    private String accountId;
    private String clientId;
    private List<String> roles = new ArrayList<>();

    public String getAccountId() {
        return accountId;
    }

    public String getClientId() {
        return clientId;
    }

    public boolean HasRole(String role) {
        String toFind = role.toLowerCase();
        return roles.stream().anyMatch(r -> r.equals(toFind));
    }
}
