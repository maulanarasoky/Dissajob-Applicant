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
import org.d3ifcool.dissajobapplicant.utils.DataDummy
import org.d3ifcool.dissajobapplicant.utils.EspressoIdlingResource
import org.junit.After
import org.junit.Before
import org.junit.Test

class HomeActivityTest {
    private val dummyDetailsJob = DataDummy.generateJobDetails()
    private val dummyRecruiterData = DataDummy.generateRecruiterDetails()
    private val dummyApplicantData = DataDummy.generateApplicantDetails()

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
    fun loadJobs() {
        onView(withId(R.id.rvJob))
            .check(matches(isDisplayed()))
        onView(withId(R.id.rvJob)).perform(
            RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(
                5
            )
        )
    }
}