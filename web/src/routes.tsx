import { createBrowserRouter } from 'react-router-dom';
import {HomePage} from "./pages/HomePage.tsx";
import {LoginPage} from "./pages/LoginPage.tsx";
import {NewAccountPage} from "./pages/NewAccountPage.tsx";
import {AccountPage} from "./pages/AccountPage.tsx";
import {NewIdeaPage} from "./pages/NewIdeaPage.tsx";

export const router = createBrowserRouter([
    {path: '/', element: <HomePage />},
    {path: '/newAccount', element: <NewAccountPage />},
    {path: '/account', element: <AccountPage />},
    {path: '/login', element: <LoginPage />},
    {path: '/newIdea', element: <NewIdeaPage />},
    {path: '*', element: <div>Page Not Found</div>}
])