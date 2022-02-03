package com.kamys.projects

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test


class FirstTests: BaseIntegrationTest() {

    @Test
    fun `first test`() {
        (1 + 1).shouldBe(2)
    }
}