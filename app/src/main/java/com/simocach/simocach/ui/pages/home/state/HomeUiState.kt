package com.simocach.simocach.ui.pages.home.state

import com.simocach.simocach.ui.pages.home.model.ModelHomeSensor

data class HomeUiState(var currentSensor: ModelHomeSensor? = null, var activeSensorCounts: Int = 1)