import type {Idea} from "../types/Idea.ts";

interface IdeaListResponse {
    ideas: Idea[];
}

export async function getRandomIdea() : Promise<Idea> {
    const response = await fetch(
        'api/ideas/get/random-idea'
    )

    if (!response.ok) {
        throw new Error(response.statusText);
    }

    const data : Idea = await response.json();
    data.duration = formatDuration(data.duration);
    return data;
}

export function formatDuration(isoDuration: string): string {
    const matches = isoDuration.match(/PT(?:(\d+)H)?(?:(\d+)M)?/);
    if (!matches) return isoDuration;

    const hours = matches[1] ? parseInt(matches[1], 10) : 0;
    const minutes = matches[2] ? parseInt(matches[2], 10) : 0;

    if (hours > 0 && minutes > 0) return `${hours}h ${minutes}min`;
    if (hours > 0) return `${hours}h`;
    return `${minutes}min`;
}

export async function getAllUsersIdeas(): Promise<Idea[]> {

    const userId = localStorage.getItem('userId');

    const response = await fetch(
        'api/ideas/get/all-users-ideas/' + userId
    )

    if (!response.ok) {
        throw new Error(response.statusText);
    }

    const data : IdeaListResponse = await response.json();

    return data.ideas.map(idea => ({
        ...idea,
        duration: formatDuration(idea.duration)
    }));
}

export async function createIdea(title : string, description : string, HDuration : number, MDuration : number) {
    const token = localStorage.getItem('token');

    const response = await fetch(
        '/api/ideas/new-idea',
        {
            method: 'POST',
            headers: {'Authorization': 'Bearer ' + token, 'Content-Type': 'application/json'},
            body: JSON.stringify({
                title: title,
                description: description,
                duration: toIsoDuration(HDuration, MDuration)
            })
        }
    )

    if (!response.ok) {
        throw new Error(response.statusText);
    }
}

function toIsoDuration(h: number, m: number): string {
    if (h > 0 && m > 0) return `PT${h}H${m}M`;
    if (h > 0) return `PT${h}H`;
    return `PT${m}M`;
}

export async function deleteIdea(ideaId: number) {
    const token = localStorage.getItem('token');

    const response = await fetch(
        '/api/ideas/' + ideaId,
        {
            method: 'DELETE',
            headers: {'Authorization': 'Bearer ' + token}
        }
    )

    if (!response.ok) {
        throw new Error(response.statusText);
    }
}