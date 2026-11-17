import {type SubmitEventHandler, useState} from "react";
import {useNavigate} from "react-router-dom";
import {createIdea} from "../hooks/useIdea.ts";

export function NewIdeaPage() {
    const navigate = useNavigate();
    const [loading, setLoading] = useState<boolean>(false);
    const [error, setError] = useState<string | null>(null);
    const [title, setTitle] = useState<string>("");
    const [description, setDescription] = useState<string>("");
    const [hoursDuration, setHoursDuration] = useState<number>(0);
    const [minutesDuration, setMinutesDuration] = useState<number>(0);

    const handleSubmit : SubmitEventHandler = async (e) => {
        e.preventDefault();
        setLoading(true);

        try {
            await createIdea(title, description, hoursDuration, minutesDuration);
            setLoading(false);
            navigate('/account');
        } catch (err) {
            if (err instanceof Error) {
                setError(err.message);
            }
            setLoading(false);
        }
    }

    return (
        <div>
            <form onSubmit={handleSubmit}>
                <label>Title : </label>
                <input id="title" type="text" value={title} onChange={(e) => setTitle(e.target.value)} />
                <label>Description : </label>
                <input id="description" type="text" value={description} onChange={(e) => setDescription(e.target.value)} />
                <label>Hours : </label>
                <input id="hours" type="number" value={hoursDuration} onChange={(e) => setHoursDuration(e.target.valueAsNumber || 0)}/>
                <label>Minutes : </label>
                <input id="minutes" type="number" value={minutesDuration} onChange={(e) => setMinutesDuration(e.target.valueAsNumber || 0)}/>
                {error != null ? <p>{error}</p> : null}
                {loading ? <p>Loading ...</p> : <input type="submit" value="Create Idea"/>}
            </form>
        </div>
    )
}