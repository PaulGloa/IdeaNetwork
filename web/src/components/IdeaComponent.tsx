import type {Idea} from "../types/Idea.ts";

export interface IdeaComponentProps {
    idea: Idea;
}

export function IdeaComponent({ idea }: IdeaComponentProps) {
    return (
        <div className={"idea"}>
            <h3>{idea.title}</h3>
            <p><span>description : </span>{idea.description}</p>
            <p><span>duration : </span>{idea.duration}</p>
        </div>
    )
}