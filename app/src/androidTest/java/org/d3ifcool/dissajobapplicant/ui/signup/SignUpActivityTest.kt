package org.d3ifcool.dissajobapplicant.ui.signup

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import org.d3ifcool.dissajobapplicant.R
import org.d3ifcool.dissajobapplicant.ui.signin.SignInActivity
import org.d3ifcool.dissajobapplicant.utils.EspressoIdlingResource
import org.d3ifcool.dissajobapplicant.utils.dummy.ApplicantDummy
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.util.regex.Pattern

class SignUpActivityTest {
    private val dummyFirstName = "Maulana"
    private val dummyLastName = "Rasoky Nasution"
    private val dummyEmail = "example@example.com"
    private val dummyInvalidEmail = "example@@"
    private val dummyPhoneNumber = "0987654321"
    private val dummyInvalidPhoneNumber = "123"
    private val dummyPassword = "123456"
    private val dummyConfirmPassword = "654321"

    @Before
    fun setUp() {
        ActivityScenario.launch(SignUpActivity::class.java)
        IdlingRegistry.getInstance().register(EspressoIdlingResource.espressoTestIdlingResource)
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.espressoTestIdlingResource)
    }

    @Test
    fun showFirstNameEditTextErrorTest() {
        onView(withId(R.id.header)).check(matches(isDisplayed()))
        onView(withId(R.id.footer)).check(matches(isDisplayed()))

        onView(withId(R.id.btnSignUp)).perform(click())

        onView(withId(R.id.etFirstName)).check(matches(hasErrorText("Nama depan tidak boleh kosong")))
    }

    
}