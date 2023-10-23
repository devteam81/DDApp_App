package com.dd.app;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.dd.app.util.UiUtils;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

public class RegistrationTest {

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        System.out.println("before class");
    }
    @Before
    public void setUp() throws Exception {
        System.out.println("before");
    }

    @Test
    public void emailValidator_CorrectEmailSimple_ReturnsTrue() {
        assertFalse(!UiUtils.checkString("name@email.com"));
    }

    @Test
    public void emailValidator_CorrectEmailSubDomain_ReturnsTrue() {
        assertTrue(UiUtils.checkString(""));
    }

    @Test
    public void emailValidator_InvalidEmailNoTld_ReturnsFalse() {
        assertTrue(UiUtils.checkString("null"));
    }

    @Test
    public void emailValidator_InvalidEmailDoubleDot_ReturnsFalse() {
        assertFalse(UiUtils.checkString(null));
    }

    /*@Test
    public void emailValidator_InvalidEmailNoUsername_ReturnsFalse() {
        assertFalse(AppUtils.isValidEmail("@email.com"));
    }

    @Test
    public void emailValidator_EmptyString_ReturnsFalse() {
        assertFalse(AppUtils.isValidEmail(""));
    }

    @Test
    public void emailValidator_NullEmail_ReturnsFalse() {
        assertFalse(AppUtils.isValidEmail(null));
    }*/

    @After
    public void tearDown() throws Exception {
        System.out.println("after");
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        System.out.println("after class");
    }

}
