package fr.paul_glnc.ennui_api.idea

import fr.paul_glnc.ennui_api.idea.dto.CreateIdeaRequest
import fr.paul_glnc.ennui_api.idea.dto.IdeaListResponse
import fr.paul_glnc.ennui_api.idea.dto.IdeaResponse
import org.springframework.http.HttpStatus
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/ideas")
class IdeaController(
    private val ideaService: IdeaService
) {

    @GetMapping("/get/random-idea")
    @ResponseStatus(HttpStatus.OK)
    fun getRandomIdea() : IdeaResponse{
        return ideaService.getRandomIdea()
    }
    //ajouter getRandomIdeaByDuration

    @GetMapping("/get/random-idea/{userId}")
    @ResponseStatus(HttpStatus.OK)
    fun getRandomIdeaFromUser(@PathVariable userId: Long) : IdeaResponse {
        return ideaService.getRandomIdeaFromUser(userId)
    }

    @GetMapping("/get/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun getIdea(@PathVariable id: Long) : IdeaResponse {
        return ideaService.getIdea(id)
    }

    @GetMapping("/get/all-users-ideas/{userId}")
    @ResponseStatus(HttpStatus.OK)
    fun getAllIdeasFromUser(@PathVariable userId: Long) : IdeaListResponse {
        return ideaService.getAllUsersIdeas(userId)
    }

    // à tester
    @PostMapping("/new-idea")
    @ResponseStatus(HttpStatus.CREATED)
    fun newIdea(
        @RequestBody idea: CreateIdeaRequest,
        @AuthenticationPrincipal currentUserId : String) : IdeaResponse {
        return ideaService.createIdea(currentUserId, idea)
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteIdea(
        @PathVariable id: Long,
        @AuthenticationPrincipal currentUserId : String
        ) {
        ideaService.deleteIdea(currentUserId, id)
    }

}