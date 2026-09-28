package com.sostudio.apptemplate

import com.sostudio.apptemplate.data.prefs.TextSizeStage
import org.junit.Assert.assertEquals
import org.junit.Test

class TextSizeStageTest {
    @Test
    fun `shiftedSp moves up the size ladder by the given number of steps`() {
        assertEquals(16f, TextSizeStage.shiftedSp(12f, steps = 1), 0f)
    }

    @Test
    fun `shiftedSp clamps at the top of the ladder`() {
        assertEquals(44f, TextSizeStage.shiftedSp(44f, steps = 5), 0f)
    }

    @Test
    fun `shiftedSp returns the base size unchanged for an unlisted size`() {
        assertEquals(15f, TextSizeStage.shiftedSp(15f, steps = 2), 0f)
    }
}
