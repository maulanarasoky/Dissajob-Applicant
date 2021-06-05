package org.d3ifcool.dissajobapplicant.ui.home

import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import org.d3ifcool.dissajobapplicant.R
import org.d3ifcool.dissajobapplicant.utils.EspressoIdlingResource
import org.d3ifcool.dissajobapplicant.utils.dummy.ApplicantDummy
import org.junit.After
import org.junit.Before
import org.junit.Test

class HomeActivityTest {
    private val dummyApplicantData = ApplicantDummy.generateApplicantDetails()

    @Before
    fun setUp() {
        ActivityScenario.launch(HomeActivity::class.java)
        IdlingRegistry.getInstance().register(EspressoIdlingResource.espressoTestIdlingResource)
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.espressoTestIdlingResource)
    }

    @Test
    fun loadJobsTest() {
        onView(withId(R.id.header)).check(matches(isDisplayed()))
        onView(withId(R.id.rvJob))
            .check(matches(isDisplayed()))
        onView(withId(R.id.rvJob)).perform(
            RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(
                0
            )
        )
    }

    @Test
    fun loadApplicantProfileTest() {
        onView(withId(R.id.profile))
            .check(matches(isDisplayed()))
        onView(withId(R.id.profile)).perform(click())

        onView(withId(R.id.imgProfilePic)).check(matches(isDisplayed()))

        onView(withId(R.id.tvProfileName)).check(matches(isDisplayed()))
        onView(withId(R.id.tvProfileName)).check(matches(withText(dummyApplicantData.fullName)))

        onView(withId(R.id.tvEmail)).check(matches(isDisplayed()))
        onView(withId(R.id.tvEmail)).check(matches(withText(dummyApplicantData.email)))

        onView(withId(R.id.tvPhoneNumber)).check(matches(isDisplayed()))
        onView(withId(R.id.tvPhoneNumber)).check(matches(withText(dummyApplicantData.phoneNumber)))

        onView(withId(R.id.tvAboutMe)).check(matches(isDisplayed()))
        onView(withId(R.id.tvAboutMe)).check(matches(withText(dummyApplicantData.aboutMe)))

        onView(withId(R.id.rvWorkExperience)).check(matches(isDisplayed()))

        onView(withId(R.id.rvEducationalBackground)).check(matches(isDisplayed()))
    }
}