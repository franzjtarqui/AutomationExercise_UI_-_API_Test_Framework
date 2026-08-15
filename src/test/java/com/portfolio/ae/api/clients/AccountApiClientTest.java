package com.portfolio.ae.api.clients;

import com.portfolio.ae.api.models.AccountRequest;
import com.portfolio.ae.api.models.ApiResponse;
import com.portfolio.ae.api.models.UserDetailResponse;
import com.portfolio.ae.utils.FakerUtil;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

/**
 * Full lifecycle of an account via API (create -> getDetail -> update -> getDetail ->
 * delete -> getDetail), the same chain that {@code account_lifecycle_api.feature} will cover
 * (PLAN.md section 4).
 */
public class AccountApiClientTest {

    private final AccountApiClient client = new AccountApiClient();

    @Test
    public void fullAccountLifecycle() {
        String email = FakerUtil.uniqueEmail();
        String password = "Passw0rd!123";
        boolean deleted = false;

        try {
            AccountRequest account = AccountRequest.anAccount()
                    .withName("Lifecycle User")
                    .withEmail(email)
                    .withPassword(password)
                    .withFirstName("Lifecycle")
                    .withLastName("User")
                    .withAddress1("123 Lifecycle St")
                    .withCity("Lifecycle City")
                    .withState("CA")
                    .withZipcode("90001")
                    .withMobileNumber("5551234567");

            ApiResponse createResponse = client.createAccount(account);
            assertEquals(createResponse.getResponseCode(), 201);

            UserDetailResponse afterCreate = client.getUserDetailByEmail(email);
            assertEquals(afterCreate.getResponseCode(), 200);
            assertEquals(afterCreate.getUser().getFirstName(), "Lifecycle");
            assertEquals(afterCreate.getUser().getCity(), "Lifecycle City");

            AccountRequest updatedAccount = AccountRequest.anAccount()
                    .withName("Lifecycle User Updated")
                    .withEmail(email)
                    .withPassword(password)
                    .withFirstName("LifecycleUpdated")
                    .withLastName("UserUpdated")
                    .withAddress1("456 Updated Ave")
                    .withCity("Updated City")
                    .withState("NY")
                    .withZipcode("10001")
                    .withMobileNumber("5559876543");

            ApiResponse updateResponse = client.updateAccount(updatedAccount);
            assertEquals(updateResponse.getResponseCode(), 200);

            UserDetailResponse afterUpdate = client.getUserDetailByEmail(email);
            assertEquals(afterUpdate.getUser().getFirstName(), "LifecycleUpdated");
            assertEquals(afterUpdate.getUser().getCity(), "Updated City");

            ApiResponse deleteResponse = client.deleteAccount(email, password);
            assertEquals(deleteResponse.getResponseCode(), 200);
            deleted = true;

            UserDetailResponse afterDelete = client.getUserDetailByEmail(email);
            assertEquals(afterDelete.getResponseCode(), 404);
        } finally {
            // Best-effort cleanup: if any assertion above failed before reaching the explicit
            // delete, this avoids leaving the account orphaned on the real site.
            if (!deleted) {
                client.deleteAccount(email, password);
            }
        }
    }
}
