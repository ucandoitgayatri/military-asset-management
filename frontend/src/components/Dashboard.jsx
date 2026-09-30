
import { useEffect, useState } from "react";
import { bases } from "../data";
import { getDashboard } from "../services/api";

function Dashboard({ user }) {
    const isBaseCommander = user.role === "ROLE_BASE_COMMANDER";

    const visibleBases = isBaseCommander ? bases.filter((base) => base.id === user.baseId): bases;


    const [baseId, setBaseId] = useState(isBaseCommander ? user.baseId : "");
    const [equipmentType, setEquipmentType] = useState("");
    const [date, setDate] = useState(new Date().toISOString().split("T")[0]);


    const [appliedBaseId, setAppliedBaseId] = useState(isBaseCommander ? user.baseId : "");
    const [appliedEquipmentType, setAppliedEquipmentType] = useState("");
    const [appliedDate, setAppliedDate] = useState(new Date().toISOString().split("T")[0]);

    const [dashboard, setDashboard] = useState({
        "Opening Balance": 0,
        "Closing Balance": 0,
        "NetMovement": 0,
        "Assignment": 0,
        "Expenditure": 0
    });

    const [error, setError] = useState("");

    useEffect(() => {loadDashboard();}, [appliedBaseId, appliedEquipmentType, appliedDate]);

    async function loadDashboard() {
        try {
            setError("");
            const data = await getDashboard(appliedBaseId,appliedEquipmentType,appliedDate);
             setDashboard(data);
        } catch (err) {
            setError(err.message);
        }
    }

    function handleApplyFilter() {
        setAppliedBaseId(baseId);
        setAppliedEquipmentType(equipmentType);
        setAppliedDate(date);
    }

    function handleClear() {
        if (isBaseCommander) {
            setBaseId(user.baseId);
            setAppliedBaseId(user.baseId);
        } else {
            setBaseId("");
            setAppliedBaseId("");
        }

        setEquipmentType("");
        setAppliedEquipmentType("");

        const today = new Date().toISOString().split("T")[0];
        setDate(today);
        setAppliedDate(today);
    }

    return (
        <div>
            <div className="page-heading">
                <div>
                    <h1>Dashboard</h1>
                    <p>Military asset summary</p>
                </div>

                <div className="user-badge">
                    {user.role === "ROLE_ADMIN"
                        ? "Administrator"
                        : user.baseName}
                </div>
            </div>

            <div className="filter-panel">
                <div className="form-group">
                    <label>Base</label>

                    <select
                        value={baseId}
                        onChange={(e) => setBaseId(e.target.value)}
                        disabled={isBaseCommander}
                    >
                        {!isBaseCommander && (
                            <option value="">All Bases</option>
                        )}

                        {visibleBases.map((base) => (
                            <option key={base.id} value={base.id}>
                                {base.name}
                            </option>
                        ))}
                    </select>
                </div>

                <div className="form-group">
                    <label>Equipment Type</label>

                    <select
                        value={equipmentType}
                        onChange={(e) => setEquipmentType(e.target.value)}
                    >
                        <option value="">All Types</option>
                        <option value="Vehicle">Vehicle</option>
                        <option value="Weapon">Weapon</option>
                        <option value="Ammunition">Ammunition</option>
                    </select>
                </div>

                <div className="form-group">
                    <label>Date</label>

                    <input
                        type="date"
                        value={date}
                        onChange={(e) => setDate(e.target.value)}
                    />
                </div>

                <div className="filter-buttons">
                    <button
                        className="apply-button"
                        onClick={handleApplyFilter}
                    >
                        Apply Filter
                    </button>

                    <button
                        className="clear-button"
                        onClick={handleClear}
                    >
                        Clear
                    </button>
                </div>
            </div>

            {error && <div className="error-box">{error}</div>}

            <div className="dashboard-grid">
                <div className="dashboard-card">
                    <h3>Opening Balance</h3>
                    <p>{dashboard["Opening Balance"]}</p>
                </div>

                <div className="dashboard-card">
                    <h3>Closing Balance</h3>
                    <p>{dashboard["Closing Balance"]}</p>
                </div>

                <div className="dashboard-card">
                    <h3>Net Movement</h3>
                    <p>{dashboard["NetMovement"]}</p>
                </div>

                <div className="dashboard-card">
                    <h3>Assigned</h3>
                    <p>{dashboard["Assignment"]}</p>
                </div>

                <div className="dashboard-card">
                    <h3>Expended</h3>
                    <p>{dashboard["Expenditure"]}</p>
                </div>
            </div>
        </div>
    );
}

export default Dashboard;
