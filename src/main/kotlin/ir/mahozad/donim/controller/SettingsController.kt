package ir.mahozad.donim.controller

import ir.mahozad.donim.PersistentSettings
import ir.mahozad.donim.exception.SettingNotFoundException
import ir.mahozad.donim.model.DEFAULT_BREAK_DURATION
import ir.mahozad.donim.model.DEFAULT_FOCUS_DURATION
import ir.mahozad.donim.util.DecorationUtil
import javafx.fxml.FXML
import javafx.scene.control.TextField
import javafx.scene.control.TextFormatter
import javafx.scene.control.ToggleButton

class SettingsController : BaseController() {

    @FXML private lateinit var focusDuration: TextField
    @FXML private lateinit var breakDuration: TextField
    @FXML private lateinit var toggleTheme: ToggleButton

    private val durationRegex = Regex("\\d{0,2}")

    override fun initialize() {
        super.initialize()
        setupFormatters()
        focusDuration.setOnMouseClicked { focusDuration.selectAll() }
        breakDuration.setOnMouseClicked { breakDuration.selectAll() }
        setInitialSettingValues()
        setupListeners()
    }

    private fun setupFormatters() {
        focusDuration.textFormatter = TextFormatter<String> {
           return@TextFormatter if (it.controlNewText.matches(durationRegex)) it else null
        }
        breakDuration.textFormatter = TextFormatter<String> {
            return@TextFormatter if (it.controlNewText.matches(durationRegex)) it else null
        }
    }

    private fun setInitialSettingValues() {
        focusDuration.promptText = DEFAULT_FOCUS_DURATION.toMinutes().toInt().toString()
        breakDuration.promptText = DEFAULT_BREAK_DURATION.toMinutes().toInt().toString()
        try {
            focusDuration.text = PersistentSettings.get("focus-duration")
            breakDuration.text = PersistentSettings.get("break-duration")
        } catch (e: SettingNotFoundException) {
            // That's fine, do nothing
        }
        try {
            if (PersistentSettings.get("theme") == DecorationUtil.Theme.DARK.name) {
                toggleTheme.isSelected = true
                toggleTheme.text = "Switch to light theme"
            } else {
                toggleTheme.isSelected = false
                toggleTheme.text = "Switch to dark theme"
            }
        } catch (e: SettingNotFoundException) {
            // That's fine, do nothing
        }
    }

    private fun setupListeners() {
        toggleTheme.selectedProperty().addListener { observable, oldValue, newValue ->
            if (newValue == true) {
                toggleTheme.text = "Switch to light theme"
            } else {
                toggleTheme.text = "Switch to dark theme"
            }
        }
        focusDuration.textProperty().addListener { _, _, newValue ->
            PersistentSettings.set("focus-duration", newValue)
        }
        breakDuration.textProperty().addListener { _, _, newValue ->
            PersistentSettings.set("break-duration", newValue)
        }
    }

    fun close() = closeWindow(false)

    fun toggleTheme() = DecorationUtil.toggleTheme()
}