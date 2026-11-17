import { useNavigate } from 'react-router-dom'

export function LoginButtons() {
    const navigate = useNavigate()
    return (
        <div id="login_buttons_div">
            <button onClick={() => {navigate('/login')}}>
                Login
            </button>
            <button onClick={() => navigate("/newAccount")}>
                Create account
            </button>
        </div>
    )
}