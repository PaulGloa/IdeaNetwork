import {useEffect, useState} from "react";
import {deleteAccount} from "../hooks/useAccount.ts";
import {useNavigate} from "react-router-dom";
import type {Idea} from "../types/Idea.ts";
import {getAllUsersIdeas} from "../hooks/useIdea.ts";
import {ListedIdeaComponent} from "../components/ListedIdeaCoponent.tsx";

export function AccountPage() {

    const navigate = useNavigate();
    const [deleteLoading, setDeleteLoading] = useState<boolean>(false);
    const [deleteError, setDeleteError] = useState<string | null>(null);
    const [ideas, setIdeas] = useState<Idea[]>([]);
    const [refreshCount, setRefreshCount] = useState<number>(0);

    useEffect(() => {

        const fetchUserIdeas = async () => {
            const userId = localStorage.getItem('userId');
            if (!userId) return;

            try {
                const data = await getAllUsersIdeas();
                setIdeas(data);
            } catch (err) {
                console.error(err);
            }
        };

        fetchUserIdeas();
    }, [refreshCount]);

    const handleDeleteAccount = async () => {
        setDeleteLoading(true);
        setDeleteError(null);

        try {
            const userId = localStorage.getItem('userId');
            if (!userId) throw new Error("Utilisateur non identifié");

            await deleteAccount(userId);

            localStorage.removeItem('userId');
            localStorage.removeItem('token');
            localStorage.removeItem('userRole');

            setDeleteLoading(false);
            navigate('/');
        } catch (err) {
            setDeleteLoading(false);
            if (err instanceof Error) {
                setDeleteError(err.message);
            } else {
                setDeleteError("Erreur lors de la suppression du compte");
            }
        }
    };

    return (
        <div>
            <h1>Account Page</h1>
            {localStorage.getItem("userRole") == 'ROLE_ADMIN' ? <p>admin</p> : null}

            <h3>My Ideas</h3>
            {
                ideas.map((idea: Idea) => {
                    return <ListedIdeaComponent idea={idea} onIdeaDeleted={() => setRefreshCount((c) => c + 1)}/>
                })
            }

            <button onClick={() => navigate('/newIdea')}>New Idea</button>

            <h3>Account Settings</h3>
            {deleteLoading ? <p>Loading ...</p> : <button onClick={handleDeleteAccount}>Delete the account</button>}
            {deleteError != null ? <p>{deleteError}</p> : null}
        </div>
    )
}