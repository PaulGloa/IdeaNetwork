package fr.paul_glnc.ennui_api.idea

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import kotlin.time.Duration

@Repository
interface IdeaRepository : JpaRepository<Idea, Long> {
    fun findByDuration(duration: Duration) : List<Idea>
    fun findByUserId(userId: Long): List<Idea>
}