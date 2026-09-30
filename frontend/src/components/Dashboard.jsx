import { useEffect, useState } from "react";
import { bases, equipment } from "../data";
import { getDashboard } from "../services/api";

function Dashboard({ user }) {

    const isBaseCommander =
        user.role === "ROLE_BASE_COMMANDER";

    const visibleBases =
        isBaseCommander
            ? bases.filter(
                (base) =>
                    base.id === user.baseId
            )
            : bases;

    const equipmentTypes = [
        ...new Set(
            equipment.map(
                (item) => item.type
            )
        )
    ];

    const today = new Date()
        .toISOString()
        .split("T")[0];

    const [baseId, setBaseId] =
        useState(
            isBaseCommander
                ? String(user.baseId)
                : ""
        );

    const [equipmentType, setEquipmentType] =
        useState("");

    const [date, setDate] =
        useState(today);

    const [dashboard, setDashboard] =
        useState({
            "Opening Balance": 0,
            "Closing Balance": 0,
            "NetMovement": 0,
            "Assignment": 0,
            "Expenditure": 0
        });

    const [loading, setLoading] =
        useState(false);

    const [error, setError] =
        useState("");

    useEffect(() => {
        loadDashboard();
    }, []);

    async function loadDashboard() {

        setLoading(true);
        setError("");

        try {

            const data =
                await getDashboard(
                    baseId,
                    equipmentType,
                    date
                );

            setDashboard(data);

        } catch (err) {

            setError(err.message);

        } finally {

            setLoading(false);
        }
    }

    function handleClear() {

        const defaultBase =
            isBaseCommander
                ? String(user.baseId)
                : "";

        setBaseId(defaultBase);
        setEquipmentType("");
        setDate(today);

        setTimeout(() => {

            getDashboard(
                defaultBase,
                "",
                today
            )
                .then((data) =>
                    setDashboard(data)
                )
                .catch((err) =>
                    setError(err.message)
                );

        }, 0);
    }

    return (
        <div>

            <div className="page-heading">
                <div>
                    <h1>Dashboard</h1>

                    <p>
                        Overview of military asset movement
                    </p>
                </div>

                <div className="user-badge">
                    {user.role === "ROLE_ADMIN"
                        ? "Administrator"
                        : user.baseName}
                </div>
            </div>

            <div className="filter-panel">

                <div className="filter-title">
                    Filters
                </div>

                <div className="filter-grid">

                    <div className="form-group">

                        <label>
                            Base
                        </label>

                        <select
                            value={baseId}
                            onChange={(e) =>
                                setBaseId(
                                    e.target.value
                                )
                            }
                        >

                            {!isBaseCommander && (
                                <option value="">
                                    All Bases
                                </option>
                            )}

                            {visibleBases.map(
                                (base) => (
                                    <option
                                        key={base.id}
                                        value={base.id}
                                    >
                                        {base.name}
                                    </option>
                                )
                            )}

                        </select>

                    </div>

                    <div className="form-group">

                        <label>
                            Equipment Type
                        </label>

                        <select
                            value={equipmentType}
                            onChange={(e) =>
                                setEquipmentType(
                                    e.target.value
                                )
                            }
                        >

                            <option value="">
                                All Equipment
                            </option>

                            {equipmentTypes.map(
                                (type) => (
                                    <option
                                        key={type}
                                        value={type}
                                    >
                                        {type}
                                    </option>
                                )
                            )}

                        </select>

                    </div>

                    <div className="form-group">

                        <label>
                            Date
                        </label>

                        <input
                            type="date"
                            value={date}
                            onChange={(e) =>
                                setDate(
                                    e.target.value
                                )
                            }
                        />

                    </div>

                    <div className="filter-actions">

                        <button
                            onClick={loadDashboard}
                            className="primary-button"
                        >
                            {loading
                                ? "Loading..."
                                : "Apply Filters"}
                        </button>

                        <button
                            onClick={handleClear}
                            className="secondary-button"
                        >
                            Clear
                        </button>

                    </div>

                </div>

            </div>

            {error && (
                <div className="error-box page-message">
                    {error}
                </div>
            )}

            <div className="dashboard-grid">

                <div className="dashboard-card">
                    <div className="card-label">
                        Opening Balance
                    </div>

                    <div className="card-number">
                        {dashboard["Opening Balance"] || 0}
                    </div>

                    <div className="card-note">
                        Assets before selected date
                    </div>
                </div>

                <div className="dashboard-card">
                    <div className="card-label">
                        Net Movement
                    </div>

                    <div className="card-number">
                        {dashboard["NetMovement"] || 0}
                    </div>

                    <div className="card-note">
                        Purchases + In - Out
                    </div>
                </div>

                <div className="dashboard-card">
                    <div className="card-label">
                        Assigned
                    </div>

                    <div className="card-number">
                        {dashboard["Assignment"] || 0}
                    </div>

                    <div className="card-note">
                        Assets assigned
                    </div>
                </div>

                <div className="dashboard-card">
                    <div className="card-label">
                        Expended
                    </div>

                    <div className="card-number">
                        {dashboard["Expenditure"] || 0}
                    </div>

                    <div className="card-note">
                        Assets expended
                    </div>
                </div>

                <div className="dashboard-card highlight-card">
                    <div className="card-label">
                        Closing Balance
                    </div>

                    <div className="card-number">
                        {dashboard["Closing Balance"] || 0}
                    </div>

                    <div className="card-note">
                        Current balance
                    </div>
                </div>

            </div>

        </div>
    );
}

export default Dashboard;