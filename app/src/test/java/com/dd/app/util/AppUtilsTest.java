package com.dd.app.util;

import static org.junit.Assert.*;

import android.content.Context;

import com.dd.app.ui.fragment.LoginFragment;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)//Mockitorunner
public class AppUtilsTest {

    //add mockito
    @Test
    public void readStringFromContext_LocalizedString() {

        LoginFragment myObjectUnderTest = new LoginFragment();
        assertTrue(myObjectUnderTest.validateFields("8108693693","123456"));
    }
}