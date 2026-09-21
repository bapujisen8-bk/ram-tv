package com.example

import android.os.Bundle
import android.view.KeyEvent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.RamDatabase
import com.example.data.RamRepository
import com.example.ui.RamTvApp
import com.example.ui.RamTvViewModel
import com.example.ui.RamTvViewModelFactory
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {

    private lateinit var ramTvViewModel: RamTvViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val database = RamDatabase.getInstance(applicationContext)
        val repository = RamRepository(database)
        val viewModelFactory = RamTvViewModelFactory(repository)

        ramTvViewModel = androidx.lifecycle.ViewModelProvider(this, viewModelFactory)[RamTvViewModel::class.java]

        setContent {
            MyApplicationTheme {
                RamTvApp(viewModel = ramTvViewModel)
            }
        }
    }

    /**
     * Android TV Remote Control Key Event Interception:
     * - Number keys (0-9): Accumulates digits to directly jump to channel code (e.g. 101, 205...)
     * - Channel UP / DOWN: Zaps to next / previous live channel
     */
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (!::ramTvViewModel.isInitialized) {
            return super.onKeyDown(keyCode, event)
        }
        when (keyCode) {
            KeyEvent.KEYCODE_0 -> { ramTvViewModel.handleRemoteDigit('0'); return true }
            KeyEvent.KEYCODE_1 -> { ramTvViewModel.handleRemoteDigit('1'); return true }
            KeyEvent.KEYCODE_2 -> { ramTvViewModel.handleRemoteDigit('2'); return true }
            KeyEvent.KEYCODE_3 -> { ramTvViewModel.handleRemoteDigit('3'); return true }
            KeyEvent.KEYCODE_4 -> { ramTvViewModel.handleRemoteDigit('4'); return true }
            KeyEvent.KEYCODE_5 -> { ramTvViewModel.handleRemoteDigit('5'); return true }
            KeyEvent.KEYCODE_6 -> { ramTvViewModel.handleRemoteDigit('6'); return true }
            KeyEvent.KEYCODE_7 -> { ramTvViewModel.handleRemoteDigit('7'); return true }
            KeyEvent.KEYCODE_8 -> { ramTvViewModel.handleRemoteDigit('8'); return true }
            KeyEvent.KEYCODE_9 -> { ramTvViewModel.handleRemoteDigit('9'); return true }
            KeyEvent.KEYCODE_CHANNEL_UP -> { ramTvViewModel.zapNext(); return true }
            KeyEvent.KEYCODE_CHANNEL_DOWN -> { ramTvViewModel.zapPrevious(); return true }
        }
        return super.onKeyDown(keyCode, event)
    }
}
