import {type SubmitEventHandler, useState} from "react";
import {register} from "../hooks/useAccount.ts";
import {useNavigate} from "react-router-dom";

export function NewAccountPage() {
    const navigate = useNavigate();
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [confirmedPassword, setConfirmedPassword] = useState('');
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState<string | null>(null);
    const [success, setSuccess] = useState(false);

    const handleSubmit: SubmitEventHandler = async(e) => {
        e.preventDefault();
        setLoading(true);

        if (password != confirmedPassword) {
            setError("passwords don't match");
            setLoading(false);
            setConfirmedPassword('');
            return;
        }

        try {
            await register(email, password);
            setLoading(false);
        } catch (err) {
            setLoading(false);

            if (err instanceof Error) {
                setError(err.message);
            } else {
                setError("Connection error");
            }
        }

        if (error != null) {
            setSuccess(true);
        }
    }

    return (
        <div>
            <h1>New Account</h1>
            <form onSubmit={handleSubmit}>
                <label>Email</label>
                <input type="text" name="email" id="email" value={email} onChange={(e) => setEmail(e.target.value)} />
                <label>Password</label>
                <input type="password" name="password" id="password" value={password} onChange={(e) => setPassword(e.target.value)} />
                <label>Confirm Password</label>
                <input type="password" name="confirmPassword" id="confirmPassword" value={confirmedPassword} onChange={(e) => setConfirmedPassword(e.target.value)} />
                {error != null ? <p>{error}</p> : null}
                {success ? <p>Account created</p> : null}
                {loading ? <p>Connection...</p> : <input id="submit" type="submit" value="Create Account" />}
            </form>
            <button onClick={() => navigate('/login')}>
                Connection
            </button>
        </div>
    )
}