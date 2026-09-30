import { useState } from "react";
import Login from "./components/Login";
import Dashboard from "./components/Dashboard";
import AssetManagement from "./components/AssetManagement";

function App() {
    const [user, setUser] = useState(null);
    const [page, setPage] = useState("dashboard");

    function handleLogin(data) {
        setUser(data);

        if (data.role === "ROLE_LOGISTICS_OFFICER") {
            setPage("assets");
        } else {
            setPage("dashboard");
        }
    }

    function handleLogout() {
        setUser(null);
        setPage("dashboard");
    }

    if (!user) {
        return <Login onLogin={handleLogin} />;
    }

    const canViewDashboard =
        user.role === "ROLE_ADMIN" ||
        user.role === "ROLE_BASE_COMMANDER";

    return (
        <div>
            <nav className="navbar">
                <h2>Military Asset Management</h2>

                <div>
                    {canViewDashboard && (
                        <button onClick={() => setPage("dashboard")}>
                            Dashboard
                        </button>
                    )}

                    <button onClick={() => setPage("assets")}>
                        Asset Management
                    </button>

                    <button
                        className="logout-button"
                        onClick={handleLogout}
                    >
                        Logout
                    </button>
                </div>
            </nav>

            <div className="page">
                {page === "dashboard" && canViewDashboard && (
                    <Dashboard user={user} />
                )}

                {page === "assets" && (
                    <AssetManagement user={user} />
                )}
            </div>
        </div>
    );
}

export default App;