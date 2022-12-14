import React from 'react'
import ReactDOM from 'react-dom/client'
import App from './App.jsx'
import Detail from './pages/Detail/Detail.jsx'
import './index.css'
import { createBrowserRouter, RouterProvider } from 'react-router-dom';

const router = createBrowserRouter([
    {
        path: "/",
        element: <App />,
    },
    {
        path: "/detail/:id",
        element: <Detail />,
    }
]);

ReactDOM.createRoot(document.getElementById('root')).render(
      <RouterProvider router={router} />
);