package org.d3ifcool.dissajobapplicant.ui.job

import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import org.d3ifcool.dissajobapplicant.R
import org.d3ifcool.dissajobapplicant.ui.home.HomeActivity
import org.d3ifcool.dissajobapplicant.utils.EspressoIdlingResource
import org.d3ifcool.dissajobapplicant.utils.dummy.JobDummy
import org.d3ifcool.dissajobapplicant.utils.dummy.RecruiterDummy
import org.junit.After
import org.junit.Before
import org.junit.Test

class JobDetailsActivityTest {
    private val dummyDetailsJob = JobDummy.generateJobDetails()
    private val dummyRecruiterData = RecruiterDummy.generateRecruiterDetails()

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
    fun loadJobDetailsTest() {
        onView(withId(R.id.rvJob)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0,
                click()
            )
        )

        onView(withId(R.id.tvJobTitle)).check(matches(isDisplayed()))
        onView(withId(R.id.tvJobTitle)).check(matches(withText(dummyDetailsJob.title)))

        onView(withId(R.id.tvJobRecruiterName)).check(matches(isDisplayed()))
        onView(withId(R.id.tvJobRecruiterName)).check(matches(withText(dummyRecruiterData.fullName)))

        onView(withId(R.id.tvJobAddress)).check(matches(isDisplayed()))
        onView(withId(R.id.tvJobAddress)).check(matches(withText(dummyDetailsJob.address)))

        onView(withId(R.id.tvJobType)).check(matches(isDisplayed()))
        onView(withId(R.id.tvJobType)).check(matches(withText("${dummyDetailsJob.employment} \u2022 ${dummyDetailsJob.type}")))

        onView(withId(R.id.imgRecruiterPicture)).check(matches(isDisplayed()))

        onView(withId(R.id.tvJobPostedDate)).check(matches(isDisplayed()))
        onView(withId(R.id.tvJobPostedDate)).check(matches(withText(dummyDetailsJob.postedDate)))

        onView(withId(R.id.tvJobDescription)).check(matches(isDisplayed()))
        onView(withId(R.id.tvJobDescription)).check(matches(withText(dummyDetailsJob.description)))

        onView(withId(R.id.tvJobQualification)).check(matches(isDisplayed()))
        onView(withId(R.id.tvJobQualification)).check(matches(withText(dummyDetailsJob.qualification)))

        onView(withId(R.id.tvJobIndustry)).check(matches(isDisplayed()))
        onView(withId(R.id.tvJobIndustry)).check(matches(withText(dummyDetailsJob.industry)))

        onView(withId(R.id.tvJobSalary)).check(matches(isDisplayed()))
        onView(withId(R.id.tvJobSalary)).check(matches(withText(dummyDetailsJob.salary)))
    }
}