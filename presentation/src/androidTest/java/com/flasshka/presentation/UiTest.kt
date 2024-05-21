package com.flasshka.presentation

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.flasshka.domain.entities.Habit
import com.flasshka.domain.usecases.getCurrentTime
import org.junit.Rule
import org.junit.Test
import java.time.LocalDate
import kotlin.random.Random

class UiTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun `to info screen and back`() {
        composeTestRule.onNodeWithTag(TestTags.OPEN_SIDE_BAR_BUTTON.toString())
            .assertExists()
            .performClick()

        composeTestRule.onNodeWithTag(TestTags.OPEN_INFO_BUTTON.toString())
            .assertExists()
            .performClick()

        composeTestRule.onNodeWithText("this is my app").assertExists()

        composeTestRule.onNodeWithTag(TestTags.OPEN_SIDE_BAR_BUTTON.toString())
            .assertExists()
            .performClick()

        composeTestRule.onNodeWithTag(TestTags.OPEN_LIST_BUTTON.toString())
            .assertExists()
            .performClick()
    }

    @Test
    fun `click to tabs`() {
        composeTestRule.onNodeWithTag(TestTags.GOOD_LIST.toString(), useUnmergedTree = true)
            .assertExists()
            .performClick()

        composeTestRule.onNodeWithTag(TestTags.BAD_LIST.toString(), useUnmergedTree = true)
            .assertExists()
            .performClick()
    }

    @Test
    fun `create habit`() {
        val rand = Random(getCurrentTime())
        val name = "habit_name_" + rand.nextInt()
        val description = "short_habit_description" + rand.nextInt()
        val periodicity = Habit.Periodicity(
            rand.nextInt(1, 10), rand.nextInt(1, 10)
        )

        // Переходим к созданию
        composeTestRule.onNodeWithTag(TestTags.CREATE_BUTTON.toString())
            .assertExists()
            .performClick()

        // Проверяем, что на странице есть заголовок
        composeTestRule.onNodeWithText("Создание/редактирование привычки")
            .assertExists()

        buttonIsNotEnabled()

        // Вводим данные и проверяем, что кнопка сохранения не работает, пока не введены все поля
        composeTestRule.onNodeWithTag(TestTags.NAME_FIELD.toString())
            .assertExists()
            .performTextInput(name)

        buttonIsNotEnabled()

        composeTestRule.onNodeWithTag(TestTags.DESCR_FIELD.toString())
            .assertExists()
            .performTextInput(description)

        buttonIsNotEnabled()

        composeTestRule.onNodeWithTag(TestTags.PERIOD_COUNT_FIELD.toString())
            .assertExists()
            .performTextInput(periodicity.count.toString())

        buttonIsNotEnabled()

        composeTestRule.onNodeWithTag(TestTags.PERIOD_DAYS_FIELD.toString())
            .assertExists()
            .performTextInput(periodicity.days.toString())

        // Все поля введены - кнопка сохранения должна сработать
        composeTestRule.onNodeWithTag(TestTags.SAVE_BUTTON.toString())
            .assertIsEnabled()
            .performClick()

        // В стартовом экране (плохие привычки) не должно быть нашей привычки
        composeTestRule.onNodeWithText(name).assertDoesNotExist()
        composeTestRule.onNodeWithText(description).assertDoesNotExist()

        // Проверяем в другом списке, в котором должна быть наша привычка
        composeTestRule.onNodeWithTag(TestTags.GOOD_LIST.toString(), useUnmergedTree = true)
            .performClick()

        composeTestRule.onAllNodesWithText(name)[0].assertExists()
        composeTestRule.onAllNodesWithText(description)[0].assertExists()
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun `delete habit`() {
        val name = createGoodHabit()

        // Переходим в список с хорошими привычками
        composeTestRule.onNodeWithTag(TestTags.GOOD_LIST.toString(), useUnmergedTree = true)
            .performClick()

        composeTestRule.onNodeWithText(name).assertExists()

        // Нажимаем на удаление привычки
        composeTestRule.onAllNodesWithTag(TestTags.DELETE_BUTTON.toString())[0]
            .assertExists()
            .performClick()

        // Проверяем, появилось ли диалоговое окно и подтверждаем удаление
        composeTestRule.onNodeWithTag(TestTags.DONE_DELETE_BUTTON.toString())
            .assertExists()
            .performClick()


        // Проверяем, что привычки больше нет
        composeTestRule.waitUntilDoesNotExist(hasText(name), 8000L)

        composeTestRule.onNodeWithText(name).assertDoesNotExist()
    }

    private fun createGoodHabit(): String {
        val rand = Random(getCurrentTime())
        val name = "habit_name_" + rand.nextInt()
        val description = "short_habit_description" + rand.nextInt()
        val periodicity = Habit.Periodicity(
            rand.nextInt(1, 10), rand.nextInt(1, 10)
        )

        composeTestRule.onNodeWithTag(TestTags.CREATE_BUTTON.toString()).performClick()
        composeTestRule.onNodeWithTag(TestTags.NAME_FIELD.toString()).performTextInput(name)
        composeTestRule.onNodeWithTag(TestTags.DESCR_FIELD.toString()).performTextInput(description)
        composeTestRule.onNodeWithTag(TestTags.PERIOD_COUNT_FIELD.toString())
            .performTextInput(periodicity.count.toString())
        composeTestRule.onNodeWithTag(TestTags.PERIOD_DAYS_FIELD.toString())
            .performTextInput(periodicity.days.toString())
        composeTestRule.onNodeWithTag(TestTags.SAVE_BUTTON.toString()).performClick()

        return name
    }

    private fun buttonIsNotEnabled() {
        // Проверяем, что кнопка сохранения не работает, тк все необходимые поля не введены
        composeTestRule.onNodeWithTag(TestTags.SAVE_BUTTON.toString())
            .assertExists()
            .assertIsNotEnabled()
    }
}