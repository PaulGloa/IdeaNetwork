export type UserRole = 'ROLE_USER' | 'ROLE_ADMIN';

interface JwtPayload {
    sub: string;
    role: UserRole;
    iat: number;
    exp: number;
}

export async function login(email: string, password: string) {
    const response = await fetch(
        'api/users/login',
        {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify({email, password})
        }
    )

    if (!response.ok) {
        throw new Error(response.statusText);
    }

    const data : {token: string} = await response.json()

    try {
        const jwtPayload = decodePayload(data.token)

        localStorage.setItem('token', data.token)
        localStorage.setItem('userRole', jwtPayload.role)
        localStorage.setItem('userId', jwtPayload.sub)
    } catch {
        throw new Error("Unable to log in");
    }
}

export function logout() {
    localStorage.removeItem('token')

}

function decodePayload(token: string): JwtPayload {
    try {
        const base64Url = token.split('.')[1];
        if (!base64Url) throw new Error('Unable to log in');

        const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
        const jsonPayload = decodeURIComponent(
            atob(base64)
                .split('')
                .map((c) => '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2))
                .join('')
        );

        return JSON.parse(jsonPayload) as JwtPayload;
    } catch {
        throw new Error("bad token")
    }
}

export async function register(email: string, password: string) {
    const response = await fetch(
        'api/users/register',
        {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify({email, password})
        }
    )

    if (!response.ok) {
        const errorData = await response.json().catch(() => null);

        const message = errorData?.error || response.statusText;

        throw new Error(message);
    }
}

export async function deleteAccount(targetId: string | null) {
    const token = localStorage.getItem('token');

    if (token == null || targetId == null) {
        throw new Error('Unable to delete user');
    }

    const response = await fetch(
        'api/users/' + targetId,
        {
            method:'DELETE',
            headers: {'Authorization': `Bearer ${token}`},
        }
    )

    if (!response.ok) {
        const errorData = await response.json().catch(() => null);

        const message = errorData?.error || response.statusText;

        throw new Error(message);
    }
}