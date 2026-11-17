import { logout } from "../hooks/useAccount.ts";
import {useNavigate} from "react-router-dom";

interface LogoutButtonProps {
    token: string;
    setToken: (token: string | null) => void;
}

export function LogoutButton({ token, setToken }: LogoutButtonProps) {

    const navigate = useNavigate();

    const handleLogout = () => {
        logout();
        setToken(null);
    };

    return (
        <>
            <p>token : {token}</p>
            <button onClick={handleLogout}>
                Logout
            </button>
            <button onClick={() => navigate('/account')}>
                My Account
            </button>
        </>
    );
}