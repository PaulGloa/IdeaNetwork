import { IdeaComponent, type IdeaComponentProps } from "./IdeaComponent.tsx";
import { useState } from "react";
import { deleteIdea } from "../hooks/useIdea.ts";

interface ListedIdeaComponentProps extends IdeaComponentProps {
    onIdeaDeleted: () => void;
}

export function ListedIdeaComponent({ idea, onIdeaDeleted }: ListedIdeaComponentProps) {
    const [detailed, setDetailed] = useState(false);
    const [error, setError] = useState<Error | null>(null);
    const [loading, setLoading] = useState<boolean>(false);

    const handleDelete = async () => {
        setLoading(true);
        setError(null);
        try {
            await deleteIdea(idea.id);
            setLoading(false);
            onIdeaDeleted();
        } catch (err) {
            if (err instanceof Error) {
                setError(err);
            }
            setLoading(false);
        }
    };

    return (
        <div>
            {detailed ? <IdeaComponent idea={idea} /> : <p>{idea.title}</p>}
            <button onClick={() => setDetailed(!detailed)}>{detailed ? "hide" : "details"}</button>
            {loading ? <p>Loading ...</p> : <button onClick={handleDelete}>delete</button>}
            {error != null ? <p>{error.message}</p> : null}
        </div>
    );
}