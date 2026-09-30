import { useState } from "react";
import { loginUser } from "../services/api";

function Login({ onLogin }) {

    const [username, setUsername] =useState("");

    const [password, setPassword] =useState("");

    const [error, setError] = useState("");

    const [loading, setLoading] = useState(false);

    async function handleSubmit(e) {

        e.preventDefault();

        setError("");
        setLoading(true);

        try {
            const data =
                await loginUser(username,password
                );

            onLogin(data);
        } catch (err) {
            setError(err.message);
        } finally {
            setLoading(false);
        }
    }

    return (
        <div className="login-page">
            <div className="login-card">
                <div className="login-header">
                    <div className="login-icon">
                        M
                    </div>
                    <h1>
                        Military Asset Management
                    </h1>
                    <p>
                        Login to continue
                    </p>
                </div>

                <form onSubmit={handleSubmit} className="login-form" >

                    <div className="form-group">
                        <label>
                            Username
                        </label>

                        <input type="text" placeholder="Enter username" value={username}  onChange={(e) =>
                                setUsername(e.target.value)
                            }
                            required
                        />
                    </div>

                    <div className="form-group">
                        <label>
                            Password
                        </label>

                        <input type="password" placeholder="Enter password" value={password} onChange={(e) =>
                                setPassword(e.target.value)
                            }
                            required
                        />
                    </div>

                    {error && (
                        <div className="error-box">
                            {error}
                        </div>
                    )}

                    <button
                        type="submit"
                        className="login-button"
                        disabled={loading}
                    >
                        {loading? "Logging in...": "Login"}
                    </button>

                </form>

            </div>

        </div>
    );
}

export default Login;