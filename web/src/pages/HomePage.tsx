import {LoginButtons} from "../components/LoginButtons.tsx";
import {LogoutButton} from "../components/LogoutButton.tsx";
import {useState} from "react";
import type {Idea} from "../types/Idea.ts";
import {getRandomIdea} from "../hooks/useIdea.ts";
import {IdeaComponent} from "../components/IdeaComponent.tsx";

export function HomePage() {
    const [token, setToken] = useState<string | null>(localStorage.getItem("token"));
    const [idea, setIdea] = useState<Idea | null>(null);
    const [error, setError] = useState<string | null>(null);
    const [ideaLoading, setIdeaLoading] = useState<boolean>(false);

    const handleGetRandomIdea = async() => {
        setError(null)
        setIdeaLoading(true);
        try {
            const idea = await getRandomIdea();
            setIdea(idea);
            setIdeaLoading(false)
        } catch (err) {
            if (err instanceof Error) {
                setError(err.message)
            } else {
               setError("error while getting an idea");
            }
            setIdeaLoading(false);
        }
    }

    return (
        <div>
            <h1>HomePage</h1>
            {token != null ? <LogoutButton token={token} setToken={setToken}/> : <LoginButtons />}
            <br/>
            <br/>
            {idea != null ? <IdeaComponent idea={idea}/> : <br/>}
            {error != null ? <p>{error}</p> : null}
            {ideaLoading ? <p>Getting an idea...</p> : <button onClick={() => handleGetRandomIdea()}>Get a random idea</button>}
        </div>
    )
}