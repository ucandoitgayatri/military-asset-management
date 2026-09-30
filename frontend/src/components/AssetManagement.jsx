import { useEffect, useState } from "react";
import { bases, equipment } from "../data";

import {
    addPurchase,
    getPurchases,
    addTransfer,
    getTransfers,
    addAssignment,
    getAssignments,
    addExpenditure,
    getExpenditures
} from "../services/api";

function AssetManagement({ user }) {

    const role = user.role;

    const isBaseCommander =
        role === "ROLE_BASE_COMMANDER";

    const canManageAssignments =
        role === "ROLE_ADMIN" ||
        role === "ROLE_BASE_COMMANDER";

    const visibleBases = isBaseCommander? bases.filter((base) =>base.id === user.baseId ): bases;

    const [activeTab, setActiveTab] =useState("purchase");

    const [message, setMessage] =useState("");

    const [error, setError] =useState("");

    const [purchases, setPurchases] = useState([]);

    const [transfers, setTransfers] =useState([]);

    const [assignments, setAssignments] = useState([]);

    const [expenditures, setExpenditures] =useState([]);

    const [purchaseForm, setPurchaseForm] =useState({baseId: "",equipmentId: "",quantity: "",date: ""});

    const [transferForm, setTransferForm] =
        useState({sourceBaseId: "",destinationBaseId: "",equipmentId: "",quantity: ""});

    const [assignmentForm, setAssignmentForm] =
        useState({baseId: "",equipmentId: "",personName: "",quantity: "",date: ""});

    const [expenditureForm, setExpenditureForm] =
        useState({baseId: "",equipmentId: "",quantity: "",date: ""});

    useEffect(() => {
        loadHistory();
    }, []);

    async function loadHistory() {

        try {
            setError("");

            const purchaseData =
            await getPurchases();

            const transferData =
             await getTransfers();

            setPurchases(purchaseData);
            setTransfers(transferData);

            if (canManageAssignments) {

                const assignmentData =
                    await getAssignments();

                const expenditureData =
                    await getExpenditures();

                setAssignments(assignmentData);

                setExpenditures(expenditureData);
            }

        } catch (err) {
            setError(err.message);
        }
    }

    function clearMessages() {
        setMessage("");
        setError("");
    }


    async function handlePurchase(e) {

        e.preventDefault();
        clearMessages();

        try {

            const data = {
                base: {
                    id: Number(
                        purchaseForm.baseId
                    )
                },

                equipment: {
                    id: Number(
                        purchaseForm.equipmentId
                    )
                },

                quantity: Number(
                    purchaseForm.quantity
                ),

                date: purchaseForm.date
            };

            await addPurchase(data);

            setMessage(
                "Purchase added successfully."
            );

            setPurchaseForm({
                baseId: "",
                equipmentId: "",
                quantity: "",
                date: ""
            });

            await loadHistory();

        } catch (err) {

            setError(err.message);
        }
    }



    async function handleTransfer(e) {

        e.preventDefault();
        clearMessages();

        try {

            const data = {sourceBase: {id: Number(transferForm.sourceBaseId)},

                destinationBase: {
                    id: Number(transferForm.destinationBaseId)
                },

                equipment: {
                    id: Number(transferForm.equipmentId)
                },

                quantity: Number(transferForm.quantity)
            };

            await addTransfer(data);

            setMessage(
                "Transfer added successfully."
            );

            setTransferForm({
                sourceBaseId: "",
                destinationBaseId: "",
                equipmentId: "",
                quantity: ""
            });

            await loadHistory();

        } catch (err) {

            setError(err.message);
        }
    }


    async function handleAssignment(e) {

        e.preventDefault();
        clearMessages();

        try {

            const data = {
                base: {
                    id: Number(assignmentForm.baseId)
                },

                equipment: {
                    id: Number(assignmentForm.equipmentId)
                },

                personName:
                    assignmentForm.personName,

                quantity: Number(
                    assignmentForm.quantity
                ),

                date:
                    assignmentForm.date
            };

            await addAssignment(data);

            setMessage(
                "Asset assigned successfully."
            );

            setAssignmentForm({
                baseId: "",
                equipmentId: "",
                personName: "",
                quantity: "",
                date: ""
            });

            await loadHistory();

        } catch (err) {

            setError(err.message);
        }
    }

    async function handleExpenditure(e) {

        e.preventDefault();
        clearMessages();

        try {

            const data = {
                base: {
                    id: Number(expenditureForm.baseId)
                },

                equipment: {
                    id: Number(expenditureForm.equipmentId)
                },

                quantity: Number(expenditureForm.quantity),

                date:
                    expenditureForm.date
            };

            await addExpenditure(data);

            setMessage(
                "Expenditure added successfully."
            );

            setExpenditureForm({
                baseId: "",
                equipmentId: "",
                quantity: "",
                date: ""
            });

            await loadHistory();

        } catch (err) {

            setError(err.message);
        }
    }

    return (
        <div>

            <div className="page-heading">

                <div>
                    <h1>Asset Management</h1>
                    <p>
                        Manage purchases, transfers and asset records
                    </p>
                </div>

                <div className="user-badge">
                    {role === "ROLE_ADMIN"? "Administrator": role === "ROLE_BASE_COMMANDER"? user.baseName
                            : "Logistics Officer"}
                </div>

            </div>

            <div className="tabs">

                <button
                    className={
                        activeTab === "purchase"  ? "tab active-tab": "tab"
                    }
                    onClick={() =>
                        setActiveTab("purchase")
                    }
                >
                    Purchases
                </button>

                <button
                    className={
                        activeTab === "transfer"? "tab active-tab"  : "tab"
                    }
                    onClick={() =>
                        setActiveTab("transfer")
                    }
                >
                    Transfers
                </button>

                {canManageAssignments && (
                    <button
                        className={
                            activeTab === "assignment"? "tab active-tab" : "tab"
                        }
                        onClick={() =>
                            setActiveTab("assignment")
                        }
                    >
                        Assignments
                    </button>
                )}

                {canManageAssignments && (
                    <button
                        className={
                            activeTab === "expenditure" ? "tab active-tab" : "tab"
                        }
                        onClick={() =>
                            setActiveTab("expenditure")
                        }
                    >
                        Expenditures
                    </button>
                )}

            </div>

            {message && (
                <div className="success-box">
                    {message}
                </div>
            )}

            {error && (
                <div className="error-box">
                    {error}
                </div>
            )}

            {/* ---------------- PURCHASE ---------------- */}

            {activeTab === "purchase" && (
                <div className="content-section">

                    <div className="form-card">

                        <div className="section-header">
                            <h2>Add Purchase</h2>
                            <p>
                                Record new assets purchased for a base
                            </p>
                        </div>

                        <form
                            onSubmit={handlePurchase}
                            className="form-grid"
                        >

                            <div className="form-group">

                                <label>Base</label>

                                <select
                                    value={
                                        purchaseForm.baseId
                                    }
                                    onChange={(e) =>
                                        setPurchaseForm({...purchaseForm,baseId:e.target.value
                                        })
                                    }
                                    required
                                >
                                    <option value="">
                                        Select Base
                                    </option>

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
                                    Equipment
                                </label>

                                <select
                                    value={purchaseForm.equipmentId}
                                    onChange={(e) =>
                                        setPurchaseForm({...purchaseForm,equipmentId:e.target.value
                                        })
                                    }
                                    required
                                >
                                    <option value="">
                                        Select Equipment
                                    </option>

                                    {equipment.map(
                                        (item) => (
                                            <option key={item.id
                                                }
                                                value={ item.id
                                                }
                                            >
                                                {item.name}
                                            </option>
                                        )
                                    )}

                                </select>

                            </div>

                            <div className="form-group">

                                <label>Quantity</label>

                                <input type="number" min="1" placeholder="Enter quantity"  value={purchaseForm.quantity}
                                    onChange={(e) =>
                                        setPurchaseForm({...purchaseForm,quantity:e.target.value
                                        })
                                    }
                                    required
                                />

                            </div>

                            <div className="form-group">

                                <label>Date</label>

                                <input type="date"
                                    value={purchaseForm.date}
                                    onChange={(e) =>
                                        setPurchaseForm({...purchaseForm,date:e.target.value
                                        })
                                    }
                                    required
                                />

                            </div>

                            <div className="form-submit">

                                <button
                                    type="submit"
                                    className="primary-button"
                                >
                                    Add Purchase
                                </button>

                            </div>

                        </form>

                    </div>

                    <div className="history-card">

                        <div className="section-header">
                            <h2>
                                Purchase History
                            </h2>

                            <span className="record-count">
                                {purchases.length} records
                            </span>
                        </div>

                        <div className="table-container">

                            <table>

                                <thead>
                                    <tr>
                                        <th>Base</th>
                                        <th>Equipment</th>
                                        <th>Quantity</th>
                                        <th>Date</th>
                                    </tr>
                                </thead>

                                <tbody>

                                    {purchases.length === 0 ? (
                                        <tr>
                                            <td
                                                colSpan="4"
                                                className="empty-row"
                                            >
                                                No purchase records found.
                                            </td>
                                        </tr>
                                    ) : (
                                        purchases.map(
                                            (item) => (
                                                <tr
                                                    key={
                                                        item.id
                                                    }
                                                >
                                                    <td>
                                                        {item.base?.name}
                                                    </td>

                                                    <td>
                                                        {item.equipment?.name}
                                                    </td>

                                                    <td>
                                                        {item.quantity}
                                                    </td>

                                                    <td>
                                                        {item.date}
                                                    </td>
                                                </tr>
                                            )
                                        )
                                    )}

                                </tbody>

                            </table>

                        </div>

                    </div>

                </div>
            )}



            {activeTab === "transfer" && (
                <div className="content-section">

                    <div className="form-card">

                        <div className="section-header">
                            <h2>Add Transfer</h2>
                            <p>
                                Move assets from one base to another
                            </p>
                        </div>

                        <form
                            onSubmit={handleTransfer}
                            className="form-grid"
                        >

                            <div className="form-group">

                                <label>
                                    Source Base
                                </label>

                                <select
                                    value={
                                        transferForm.sourceBaseId
                                    }
                                    onChange={(e) =>
                                        setTransferForm({...transferForm,sourceBaseId:e.target.value
                                        })
                                    }
                                    required
                                >
                                    <option value="">
                                        Select Source Base
                                    </option>

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
                                    Destination Base
                                </label>

                                <select
                                    value={
                                        transferForm.destinationBaseId
                                    }
                                    onChange={(e) =>
                                        setTransferForm({...transferForm,destinationBaseId:e.target.value
                                        })
                                    }
                                    required
                                >
                                    <option value="">
                                        Select Destination Base
                                    </option>

                                    {bases
                                        .filter(
                                            (base) =>
                                                !isBaseCommander ||
                                                base.id !==
                                                    user.baseId
                                        )
                                        .map(
                                            (base) => (
                                                <option
                                                    key={
                                                        base.id
                                                    }
                                                    value={
                                                        base.id
                                                    }
                                                >
                                                    {base.name}
                                                </option>
                                            )
                                        )}

                                </select>

                            </div>

                            <div className="form-group">

                                <label>
                                    Equipment
                                </label>

                                <select
                                    value={
                                        transferForm.equipmentId
                                    }
                                    onChange={(e) =>
                                        setTransferForm({...transferForm,equipmentId:e.target.value
                                        })
                                    }
                                    required
                                >
                                    <option value="">
                                        Select Equipment
                                    </option>

                                    {equipment.map(
                                        (item) => (
                                            <option
                                                key={
                                                    item.id
                                                }
                                                value={
                                                    item.id
                                                }
                                            >
                                                {item.name}
                                            </option>
                                        )
                                    )}

                                </select>

                            </div>

                            <div className="form-group">

                                <label>
                                    Quantity
                                </label>

                                <input type="number"  min="1"   placeholder="Enter quantity"  value={
                                        transferForm.quantity
                                    }
                                    onChange={(e) =>setTransferForm({...transferForm,quantity:e.target.value
                                        })
                                    }
                                    required
                                />

                            </div>

                            <div className="form-submit">

                                <button
                                    type="submit"
                                    className="primary-button"
                                >
                                    Add Transfer
                                </button>

                            </div>

                        </form>

                    </div>

                    <div className="history-card">

                        <div className="section-header">
                            <h2>
                                Transfer History
                            </h2>

                            <span className="record-count">
                                {transfers.length} records
                            </span>
                        </div>

                        <div className="table-container">

                            <table>

                                <thead>
                                    <tr>
                                        <th>Source</th>
                                        <th>Destination</th>
                                        <th>Equipment</th>
                                        <th>Quantity</th>
                                        <th>Timestamp</th>
                                    </tr>
                                </thead>

                                <tbody>

                                    {transfers.length === 0 ? (
                                        <tr>
                                            <td
                                                colSpan="5"
                                                className="empty-row"
                                            >
                                                No transfer records found.
                                            </td>
                                        </tr>
                                    ) : (
                                        transfers.map(
                                            (item) => (
                                                <tr
                                                    key={
                                                        item.id
                                                    }
                                                >
                                                    <td>
                                                        {item.sourceBase?.name}
                                                    </td>

                                                    <td>
                                                        {item.destinationBase?.name}
                                                    </td>

                                                    <td>
                                                        {item.equipment?.name}
                                                    </td>

                                                    <td>
                                                        {item.quantity}
                                                    </td>

                                                    <td>
                                                        {item.date}
                                                    </td>
                                                </tr>
                                            )
                                        )
                                    )}

                                </tbody>

                            </table>

                        </div>

                    </div>

                </div>
            )}

            {activeTab === "assignment" &&
                canManageAssignments && (
                    <div className="content-section">

                        <div className="form-card">

                            <div className="section-header">
                                <h2>
                                    Assign Asset
                                </h2>

                                <p>
                                    Assign assets to personnel
                                </p>
                            </div>

                            <form
                                onSubmit={
                                    handleAssignment
                                }
                                className="form-grid"
                            >

                                <div className="form-group">

                                    <label>
                                        Base
                                    </label>

                                    <select
                                        value={
                                            assignmentForm.baseId
                                        }
                                        onChange={(e) =>
                                            setAssignmentForm({...assignmentForm,baseId:e.target.value})
                                        }
                                        required
                                    >
                                        <option value="">
                                            Select Base
                                        </option>

                                        {visibleBases.map(
                                            (base) => (
                                                <option
                                                    key={
                                                        base.id
                                                    }
                                                    value={
                                                        base.id
                                                    }
                                                >
                                                    {base.name}
                                                </option>
                                            )
                                        )}

                                    </select>

                                </div>

                                <div className="form-group">

                                    <label>
                                        Equipment
                                    </label>

                                    <select
                                        value={
                                            assignmentForm.equipmentId
                                        }
                                        onChange={(e) =>setAssignmentForm({...assignmentForm,equipmentId:
                                                    e.target.value
                                            })
                                        }
                                        required
                                    >
                                        <option value="">
                                            Select Equipment
                                        </option>

                                        {equipment.map(
                                            (item) => (
                                                <option
                                                    key={
                                                        item.id
                                                    }
                                                    value={
                                                        item.id
                                                    }
                                                >
                                                    {item.name}
                                                </option>
                                            )
                                        )}

                                    </select>

                                </div>

                                <div className="form-group">

                                    <label>
                                        Person Name
                                    </label>

                                    <input type="text" placeholder="Enter personnel name"
                                        value={assignmentForm.personName
                                        }
                                        onChange={(e) =>
                                            setAssignmentForm({...assignmentForm,personName:e.target.value
                                            })
                                        }
                                        required
                                    />

                                </div>

                                <div className="form-group">

                                    <label>
                                        Quantity
                                    </label>

                                    <input type="number" min="1" placeholder="Enter quantity" value={
                                            assignmentForm.quantity
                                        }
                                        onChange={(e) =>
                                            setAssignmentForm({...assignmentForm,quantity:e.target.value
                                            })
                                        }
                                        required
                                    />

                                </div>

                                <div className="form-group">

                                    <label>
                                        Date
                                    </label>

                                    <input type="date"
                                        value={
                                            assignmentForm.date
                                        }
                                        onChange={(e) =>
                                            setAssignmentForm({...assignmentForm,date:e.target.value
                                            })
                                        }
                                        required
                                    />

                                </div>

                                <div className="form-submit">

                                    <button
                                        type="submit"
                                        className="primary-button"
                                    >
                                        Assign Asset
                                    </button>

                                </div>

                            </form>

                        </div>

                        <div className="history-card">

                            <div className="section-header">

                                <h2>
                                    Assignment History
                                </h2>

                                <span className="record-count">
                                    {assignments.length} records
                                </span>

                            </div>

                            <div className="table-container">

                                <table>

                                    <thead>
                                        <tr>
                                            <th>Base</th>
                                            <th>Equipment</th>
                                            <th>Person</th>
                                            <th>Quantity</th>
                                            <th>Date</th>
                                        </tr>
                                    </thead>

                                    <tbody>

                                        {assignments.length === 0 ? (
                                            <tr>
                                                <td
                                                    colSpan="5"
                                                    className="empty-row"
                                                >
                                                    No assignment records found.
                                                </td>
                                            </tr>
                                        ) : (
                                            assignments.map(
                                                (item) => (
                                                    <tr
                                                        key={
                                                            item.id
                                                        }
                                                    >
                                                        <td>
                                                            {item.base?.name}
                                                        </td>

                                                        <td>
                                                            {item.equipment?.name}
                                                        </td>

                                                        <td>
                                                            {item.personName}
                                                        </td>

                                                        <td>
                                                            {item.quantity}
                                                        </td>

                                                        <td>
                                                            {item.date}
                                                        </td>
                                                    </tr>
                                                )
                                            )
                                        )}

                                    </tbody>

                                </table>

                            </div>

                        </div>

                    </div>
                )}



            {activeTab === "expenditure" &&
                canManageAssignments && (
                    <div className="content-section">

                        <div className="form-card">

                            <div className="section-header">
                                <h2>
                                    Record Expenditure
                                </h2>

                                <p>
                                    Record assets that have been expended
                                </p>
                            </div>

                            <form
                                onSubmit={
                                    handleExpenditure
                                }
                                className="form-grid"
                            >

                                <div className="form-group">

                                    <label>
                                        Base
                                    </label>

                                    <select
                                        value={
                                            expenditureForm.baseId
                                        }
                                        onChange={(e) =>
                                            setExpenditureForm({...expenditureForm,baseId:e.target.value
                                            })
                                        }
                                        required
                                    >
                                        <option value="">
                                            Select Base
                                        </option>

                                        {visibleBases.map(
                                            (base) => (
                                                <option
                                                    key={
                                                        base.id
                                                    }
                                                    value={
                                                        base.id
                                                    }
                                                >
                                                    {base.name}
                                                </option>
                                            )
                                        )}

                                    </select>

                                </div>

                                <div className="form-group">

                                    <label>
                                        Equipment
                                    </label>

                                    <select
                                        value={
                                            expenditureForm.equipmentId
                                        }
                                        onChange={(e) =>
                                            setExpenditureForm({
                                                ...expenditureForm,
                                                equipmentId:
                                                    e.target.value
                                            })
                                        }
                                        required
                                    >
                                        <option value="">
                                            Select Equipment
                                        </option>

                                        {equipment.map(
                                            (item) => (
                                                <option
                                                    key={
                                                        item.id
                                                    }
                                                    value={
                                                        item.id
                                                    }
                                                >
                                                    {item.name}
                                                </option>
                                            )
                                        )}

                                    </select>

                                </div>

                                <div className="form-group">

                                    <label>
                                        Quantity
                                    </label>

                                    <input type="number" min="1"   placeholder="Enter quantity"
                                        value={
                                            expenditureForm.quantity
                                        }
                                        onChange={(e) =>
                                            setExpenditureForm({...expenditureForm,quantity:
                                                    e.target.value
                                            })
                                        }
                                        required
                                    />

                                </div>

                                <div className="form-group">

                                    <label>
                                        Date
                                    </label>

                                    <input
                                        type="date"
                                        value={
                                            expenditureForm.date
                                        }
                                        onChange={(e) =>
                                            setExpenditureForm({
                                                ...expenditureForm,
                                                date:
                                                    e.target.value
                                            })
                                        }
                                        required
                                    />

                                </div>

                                <div className="form-submit">

                                    <button
                                        type="submit"
                                        className="primary-button"
                                    >
                                        Add Expenditure
                                    </button>

                                </div>

                            </form>

                        </div>

                        <div className="history-card">

                            <div className="section-header">

                                <h2>
                                    Expenditure History
                                </h2>

                                <span className="record-count">
                                    {expenditures.length} records
                                </span>

                            </div>

                            <div className="table-container">

                                <table>

                                    <thead>
                                        <tr>
                                            <th>Base</th>
                                            <th>Equipment</th>
                                            <th>Quantity</th>
                                            <th>Date</th>
                                        </tr>
                                    </thead>

                                    <tbody>

                                        {expenditures.length === 0 ? (
                                            <tr>
                                                <td
                                                    colSpan="4"
                                                    className="empty-row"
                                                >
                                                    No expenditure records found.
                                                </td>
                                            </tr>
                                        ) : (
                                            expenditures.map(
                                                (item) => (
                                                    <tr
                                                        key={
                                                            item.id
                                                        }
                                                    >
                                                        <td>
                                                            {item.base?.name}
                                                        </td>

                                                        <td>
                                                            {item.equipment?.name}
                                                        </td>

                                                        <td>
                                                            {item.quantity}
                                                        </td>

                                                        <td>
                                                            {item.date}
                                                        </td>
                                                    </tr>
                                                )
                                            )
                                        )}

                                    </tbody>

                                </table>

                            </div>

                        </div>

                    </div>
                )}

        </div>
    );
}

export default AssetManagement;