import {type SubmitEventHandler, useState} from "react";
import {login} from "../hooks/useAccount.ts";
import {useNavigate} from "react-router-dom";

export function LoginPage() {
    const navigate = useNavigate();
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState<string | null>(null);

    const handleSubmit : SubmitEventHandler = async(e) => {
        e.preventDefault();
        setLoading(true);

        try {
            await login(email, password);
            setLoading(false);
            navigate("/")
        } catch (err) {
            setLoading(false);
            if (err instanceof Error) {
                setError(err.message);
            } else {
                setError("Connection error");
            }
        }
    };

    return (
        <div>
            <h1>LoginPage</h1>
            <form onSubmit={handleSubmit}>
                <label>Email</label>
                <input id="email" type="email" value={email} onChange={(e) => setEmail(e.target.value)} />
                <label>Password</label>
                <input id="password" type="password" value={password} onChange={(e) => setPassword(e.target.value)} />
                {error != null ? <p>{error}</p> : null}
                {loading ? <p>Connection...</p> : <input id="submit" type="submit" value="Login" />}
            </form>
        </div>
    )
}