package fr.paul_glnc.ennui_api.idea

import fr.paul_glnc.ennui_api.user.User
import jakarta.persistence.*
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.type.SqlTypes
import java.time.Duration

@Entity
@Table(name = "ideas")
class Idea(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val user: User,

    @Column(nullable = false, length = 255)
    var title: String,

    @Column(columnDefinition = "TEXT")
    var description: String = "",

    @Column(name = "duration", columnDefinition = "interval", nullable = false)
    @JdbcTypeCode(SqlTypes.INTERVAL_SECOND)
    var duration: Duration = Duration.ZERO
)