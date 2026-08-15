package com.portfolio.ae.data;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class ExpectedMessagesTest {

    @Test
    public void readsAllMessagesFromTheFixture() {
        assertEquals(ExpectedMessages.accountCreated(), "Account Created!");
        assertEquals(ExpectedMessages.accountDeleted(), "Account Deleted!");
        assertEquals(ExpectedMessages.loginError(), "Your email or password is incorrect!");
        assertEquals(ExpectedMessages.signupEmailExists(), "Email Address already exist!");
        assertEquals(ExpectedMessages.orderPlaced(), "Order Placed!");
        assertEquals(ExpectedMessages.cartItemAdded(), "Added!");
    }
}
