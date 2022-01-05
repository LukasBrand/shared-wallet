package com.lukasbrand.sharedwallet

import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import de.mannodermaus.junit5.ActivityScenarioExtension
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension

class MyActivityTest {

    @JvmField
    @RegisterExtension
    val scenarioExtension = ActivityScenarioExtension.launch<MainActivity>()

    @Test
    fun test1() {
        val scenario = scenarioExtension.scenario
        scenario.moveToState(Lifecycle.State.RESUMED)
        assertEquals(scenario.state, Lifecycle.State.RESUMED)
    }

    @Test
    fun test2(scenario: ActivityScenario<MainActivity>) {
        scenario.moveToState(Lifecycle.State.RESUMED)
        assertEquals(scenario.state, Lifecycle.State.RESUMED)
    }
}