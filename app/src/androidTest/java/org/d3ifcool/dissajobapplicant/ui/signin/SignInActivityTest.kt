package org.d3ifcool.dissajobapplicant.ui.signin

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.*
import org.d3ifcool.dissajobapplicant.R
import org.d3ifcool.dissajobapplicant.ui.home.HomeActivity
import org.d3ifcool.dissajobapplicant.ui.signup.SignUpActivity
import org.d3ifcool.dissajobapplicant.utils.EspressoIdlingResource
import org.d3ifcool.dissajobapplicant.utils.dummy.ApplicantDummy
import org.junit.After
import org.junit.Before
import org.junit.Test

class SignInActivityTest {
    private val dummyApplicantData = ApplicantDummy.generateApplicantDetails()
    private val dummyUnverifiedEmail = "example@example.com"
    private val dummyAccountPassword = "123456"
    private val dummyWrongAccountPassword = "cobacoba"

    @Before
    fun setUp() {
        ActivityScenario.launch(SignInActivity::class.java)
        IdlingRegistry.getInstance().register(EspressoIdlingResource.espressoTestIdlingResource)
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.espressoTestIdlingResource)
    }

    @Test
    fun showEmailEditTextErrorTest() {
        onView(withId(R.id.header)).check(matches(isDisplayed()))
        onView(withId(R.id.footer)).check(matches(isDisplayed()))

        onView(withId(R.id.etPassword)).perform(typeText(dummyAccountPassword))

        onView(withId(R.id.btnSignIn)).perform(click())

        onView(withId(R.id.etEmail)).check(matches(hasErrorText("Email tidak boleh kosong")))
    }

    @Test
    fun showPasswordEditTextErrorTest() {
        onView(withId(R.id.header)).check(matches(isDisplayed()))
        onView(withId(R.id.footer)).check(matches(isDisplayed()))

        onView(withId(R.id.etEmail)).perform(typeText(dummyApplicantData.email))

        onView(withId(R.id.btnSignIn)).perform(click())

        onView(withId(R.id.etPassword)).check(matches(hasErrorText("Password tidak boleh kosong")))
    }

    @Test
    fun clickSignInButtonTest() {
        onView(withId(R.id.header)).check(matches(isDisplayed()))
        onView(withId(R.id.footer)).check(matches(isDisplayed()))

        onView(withId(R.id.etEmail)).perform(typeText(dummyApplicantData.email))
        onView(withId(R.id.etPassword)).perform(typeText(dummyAccountPassword))

        onView(withId(R.id.btnSignIn)).perform(click())

        Intents.init()
        Intents.intended(hasComponent(HomeActivity::class.simpleName))
    }

    @Test
    fun showUnverifiedEmailAlertTest() {
        onView(withId(R.id.header)).check(matches(isDisplayed()))
        onView(withId(R.id.footer)).check(matches(isDisplayed()))

        onView(withId(R.id.etEmail)).perform(typeText(dummyUnverifiedEmail))
        onView(withId(R.id.etPassword)).perform(typeText(dummyAccountPassword))

        onView(withId(R.id.btnSignIn)).perform(click())

        onView(withText(R.string.email_is_not_verified)).check(matches(isDisplayed()))
    }

    @Test
    fun unCorrectEmailOrPasswordTest() {
        onView(withId(R.id.header)).check(matches(isDisplayed()))
        onView(withId(R.id.footer)).check(matches(isDisplayed()))

        onView(withId(R.id.etEmail)).perform(typeText(dummyApplicantData.email))
        onView(withId(R.id.etPassword)).perform(typeText(dummyWrongAccountPassword))

        onView(withId(R.id.btnSignIn)).perform(click())

        onView(withText(R.string.error_login)).check(matches(isDisplayed()))
    }

    @Test
    fun clickSignUpButtonTest() {
        onView(withId(R.id.header)).check(matches(isDisplayed()))
        onView(withId(R.id.footer)).check(matches(isDisplayed()))

        onView(withId(R.id.btnSignUp)).perform(click())

        Intents.init()
        Intents.intended(hasComponent(SignUpActivity::class.simpleName))
    }
}