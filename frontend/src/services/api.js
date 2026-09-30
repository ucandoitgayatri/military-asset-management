const BASE_URL = "http://localhost:8080";

async function getErrorMessage(response, defaultMessage) {
    try {
        const data = await response.json();

        return (
            data.message ||
            data.error ||
            defaultMessage
        );
    } catch {
        return defaultMessage;
    }
}

// ---------------- LOGIN ----------------

export async function loginUser(username, password) {

    const response = await fetch(
        `${BASE_URL}/api/auth/login`,
        {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            credentials: "include",
            body: JSON.stringify({
                username,
                password
            })
        }
    );

    if (!response.ok) {
        throw new Error(
            await getErrorMessage(
                response,
                "Login failed"
            )
        );
    }

    return response.json();
}

// ---------------- DASHBOARD ----------------

export async function getDashboard(
    baseId,
    equipmentType,
    date
) {

    const params = new URLSearchParams();

    if (baseId) {
        params.append("baseId", baseId);
    }

    if (equipmentType) {
        params.append(
            "equipmentType",
            equipmentType
        );
    }

    if (date) {
        params.append("date", date);
    }

    const url =
        `${BASE_URL}/api/dashboard` +
        (params.toString()
            ? `?${params.toString()}`
            : "");

    const response = await fetch(url, {
        method: "GET",
        credentials: "include"
    });

    if (!response.ok) {
        throw new Error(
            await getErrorMessage(
                response,
                "Failed to load dashboard"
            )
        );
    }

    return response.json();
}

// ---------------- PURCHASE ----------------

export async function addPurchase(data) {

    const response = await fetch(
        `${BASE_URL}/api/assetManagement/addPurchase`,
        {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            credentials: "include",
            body: JSON.stringify(data)
        }
    );

    if (!response.ok) {
        throw new Error(
            await getErrorMessage(
                response,
                "Failed to add purchase"
            )
        );
    }

    return response.json();
}

export async function getPurchases() {

    const response = await fetch(
        `${BASE_URL}/api/assetManagement/getAllPurchases`,
        {
            method: "GET",
            credentials: "include"
        }
    );

    if (!response.ok) {
        throw new Error(
            await getErrorMessage(
                response,
                "Failed to load purchase history"
            )
        );
    }

    return response.json();
}

// ---------------- TRANSFER ----------------

export async function addTransfer(data) {

    const response = await fetch(
        `${BASE_URL}/api/assetManagement/addTransfer`,
        {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            credentials: "include",
            body: JSON.stringify(data)
        }
    );

    if (!response.ok) {
        throw new Error(
            await getErrorMessage(
                response,
                "Failed to add transfer"
            )
        );
    }

    return response.json();
}

export async function getTransfers() {

    const response = await fetch(
        `${BASE_URL}/api/assetManagement/getAllTransfers`,
        {
            method: "GET",
            credentials: "include"
        }
    );

    if (!response.ok) {
        throw new Error(
            await getErrorMessage(
                response,
                "Failed to load transfer history"
            )
        );
    }

    return response.json();
}

// ---------------- ASSIGNMENT ----------------

export async function addAssignment(data) {

    const response = await fetch(
        `${BASE_URL}/api/assetManagement/addAssignment`,
        {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            credentials: "include",
            body: JSON.stringify(data)
        }
    );

    if (!response.ok) {
        throw new Error(
            await getErrorMessage(
                response,
                "Failed to add assignment"
            )
        );
    }

    return response.json();
}

export async function getAssignments() {

    const response = await fetch(
        `${BASE_URL}/api/assetManagement/getAllAssignments`,
        {
            method: "GET",
            credentials: "include"
        }
    );

    if (!response.ok) {
        throw new Error(
            await getErrorMessage(
                response,
                "Failed to load assignment history"
            )
        );
    }

    return response.json();
}

// ---------------- EXPENDITURE ----------------

export async function addExpenditure(data) {

    const response = await fetch(
        `${BASE_URL}/api/assetManagement/addExpenditure`,
        {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            credentials: "include",
            body: JSON.stringify(data)
        }
    );

    if (!response.ok) {
        throw new Error(
            await getErrorMessage(
                response,
                "Failed to add expenditure"
            )
        );
    }

    return response.json();
}

export async function getExpenditures() {

    const response = await fetch(
        `${BASE_URL}/api/assetManagement/getAllExpenditures`,
        {
            method: "GET",
            credentials: "include"
        }
    );

    if (!response.ok) {
        throw new Error(
            await getErrorMessage(
                response,
                "Failed to load expenditure history"
            )
        );
    }

    return response.json();
}