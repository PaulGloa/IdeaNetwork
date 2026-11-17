package fr.paul_glnc.ennui_api.idea

import fr.paul_glnc.ennui_api.idea.dto.CreateIdeaRequest
import fr.paul_glnc.ennui_api.idea.dto.IdeaListResponse
import fr.paul_glnc.ennui_api.idea.dto.IdeaResponse
import fr.paul_glnc.ennui_api.security.JwtService
import fr.paul_glnc.ennui_api.user.Role
import fr.paul_glnc.ennui_api.user.UserRepository
import org.springframework.stereotype.Service

@Service
class IdeaService(
    private val userRepository: UserRepository,
    private val ideaRepository: IdeaRepository,
    private val jwtService: JwtService
) {

    fun getIdea(id : Long) : IdeaResponse {
        val idea = ideaRepository.findById(id).orElseThrow { NoSuchElementException("No idea by id $id") }

        return IdeaResponse(
            id = id,
            title = idea.title,
            description = idea.description,
            duration = idea.duration
        )
    }

    fun getRandomIdea() : IdeaResponse {
        val idea = ideaRepository.findAll().random()

        return IdeaResponse(
            id = idea.id!!,
            title = idea.title,
            description = idea.description,
            duration = idea.duration
        )
    }

    fun getRandomIdeaFromUser(userId : Long) : IdeaResponse {
        val idea = ideaRepository.findByUserId(userId).random()

        return IdeaResponse(
            id = idea.id!!,
            title = idea.title,
            description = idea.description,
            duration = idea.duration
        )
    }

    fun getAllUsersIdeas(userId : Long) : IdeaListResponse {
        val ideas = ideaRepository.findByUserId(userId)

        val ideaResponses: List<IdeaResponse> = ideas.map { idea ->
            IdeaResponse(
                id = idea.id!!,
                title = idea.title,
                description = idea.description,
                duration = idea.duration
            )
        }

        return IdeaListResponse(
            ideas = ideaResponses,
        )
    }

    fun createIdea(currentUserIdString : String, createIdeaRequest: CreateIdeaRequest): IdeaResponse {
        val currentUserId = currentUserIdString.toLong()
        val user = userRepository.findById(currentUserId)
            .orElseThrow { NoSuchElementException("No user with id $currentUserId") }

        val idea = Idea(
            user = user,
            title = createIdeaRequest.title,
            description = createIdeaRequest.description,
            duration = createIdeaRequest.duration
        )

        val savedIdea = ideaRepository.save(idea)

        return IdeaResponse(
            id = savedIdea.id!!,
            title = savedIdea.title,
            description = savedIdea.description,
            duration = savedIdea.duration
        )
    }

    fun deleteIdea(currentUserIdString : String, ideaId : Long) {
        val currentUserId = currentUserIdString.toLong()
        val user = userRepository.findById(currentUserId)
            .orElseThrow { NoSuchElementException("No user with id $currentUserId") }

        val idea = ideaRepository.findById(ideaId)
            .orElseThrow { NoSuchElementException("No idea with id $currentUserId") }

        if (user.id != idea.user.id && user.role != Role.ROLE_ADMIN) {
            throw IllegalArgumentException("Deletion of this idea not permitted for this user")
        }

        ideaRepository.delete(idea)
    }
}