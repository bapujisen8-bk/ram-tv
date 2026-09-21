package com.example

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import com.example.model.ChannelCategory
import com.example.model.RamChannel
import com.example.ui.components.RamChannelCard
import com.example.ui.theme.MyApplicationTheme
import com.github.takahirom.roborazzi.RobolectricDeviceQualifiers
import com.github.takahirom.roborazzi.captureRoboImage
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(qualifiers = RobolectricDeviceQualifiers.Pixel7)
class GreetingScreenshotTest {

  @get:Rule
  val composeTestRule = createComposeRule()

  @Test
  fun greeting_screenshot() {
    val sampleChannel = RamChannel(
      id = "dd_odia",
      name = "DD Odia (ଦୂରଦର୍ଶନ)",
      logoText = "DDO",
      category = ChannelCategory.ODIA,
      streamUrl = "https://stream.ecable.tv/ddodia/index.m3u8",
      viewersCount = "18.4K",
      resolution = "1080p FHD",
      currentShow = "Khabar Sambad & Sanskruti Live",
      currentShowCategory = "News & Culture",
      progressPercent = 0.65f,
      language = "Odia",
      isFeatured = true
    )

    composeTestRule.setContent {
      MyApplicationTheme {
        RamChannelCard(
          channel = sampleChannel,
          isActive = true,
          isFavorite = true,
          onSelect = {},
          onToggleFavorite = {}
        )
      }
    }

    composeTestRule.onRoot().captureRoboImage()
  }
}
