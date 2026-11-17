package fr.paul_glnc.ennui_api.idea.dto

import java.time.Duration

data class CreateIdeaRequest(
    val title: String,
    val description: String,
    val duration: Duration,
)
