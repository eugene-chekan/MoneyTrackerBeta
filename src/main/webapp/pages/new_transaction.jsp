<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <%--    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">--%>
    <title>Add Transaction</title>
</head>
<style>
    body {
        font-family: Arial, sans-serif;
        background-color: #f4f4f4;
        margin: 0;
        padding: 0;
    }

    .container {
        background-color: white;
        padding: 2rem;
        border-radius: 8px;
        box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
        max-width: fit-content;
        margin: 50px auto;
        text-align: center;
    }

    h1 {
        color: #333;
        text-align: center;
    }

    form {
        margin-top: 20px;
    }

    label {
        display: block;
        margin-bottom: 5px;
    }

    input[type="text"],
    input[type="number"],
    select {
        width: 100%;
        padding: 8px;
        margin-bottom: 10px;
        border: 1px solid #ddd;
        border-radius: 4px;
    }

    button {

    }

    .primary-button {
        background-color: #4CAF50;
        color: white;
        padding: 0.75rem;
        border: none;
        border-radius: 4px;
        font-size: 1rem;
        cursor: pointer;
        transition: background-color 0.3s;
    }

    .primary-button:hover {
        background-color: #45a049;
    }

    .cancel-button {
        background-color: #DC3535E7;
        color: white;
        padding: 0.75rem;
        border: none;
        border-radius: 4px;
        font-size: 1rem;
        cursor: pointer;
        transition: background-color 0.3s;
    }

    .cancel-button:hover {
        background-color: #CD3131E7;
    }

    table {
        width: 100%;
        border-collapse: collapse;
        margin-top: 20px;
    }

    th, td {
        border: 1px solid #ddd;
        padding: 8px;
        text-align: left;
    }

    th {
        background-color: #f2f2f2;
    }
</style>
<script>
    function updateSourceOptions() {
        const typeSelect = document.getElementById("type");
        const sourceContainer = document.getElementById("source");

        const selectedType = typeSelect.value;
        sourceContainer.innerHTML = ""; // Reset options container

        // Dynamically generate options based on a transaction type
        if (selectedType === "income") {
            options = `<c:forEach items="${incomeSources}" var="account">
                              <option value="${account.name}">${account.name}</option>
                       </c:forEach>`;
        } else if (selectedType === "expense") {
            options = `<c:forEach items="${accounts}" var="account">
                              <option value="${account.name}">${account.name}</option>
                       </c:forEach>`;
        } else if (selectedType === "transfer") {
            options = `<c:forEach items="${accounts}" var="account">
                              <option value="${account.name}">${account.name}</option>
                       </c:forEach>`;
        }
        sourceContainer.innerHTML = options;
    }
    function updateDestinationOptions() {
        const typeSelect = document.getElementById("type");
        const destinationContainer = document.getElementById("destination");

        const selectedType = typeSelect.value;
        destinationContainer.innerHTML = ""; // Reset options container

        // Dynamically generate options based on a transaction type
        if (selectedType === "income") {
            options = `<c:forEach items="${accounts}" var="account">
                              <option value="${account.name}">${account.name}</option>
                       </c:forEach>`;
        } else if (selectedType === "expense") {
            options = `<c:forEach items="${expenseSources}" var="account">
                              <option value="${account.name}">${account.name}</option>
                       </c:forEach>`;
        } else if (selectedType === "transfer") {
            options = `<c:forEach items="${accounts}" var="account">
                              <option value="${account.name}">${account.name}</option>
                       </c:forEach>`;
        }
        destinationContainer.innerHTML = options;
    }
    function updateOptions() {
        updateSourceOptions();
        updateDestinationOptions();
    }

    document.addEventListener("DOMContentLoaded", () => {
        document.getElementById("type").addEventListener("change", updateOptions);
        updateOptions(); // Initialize options on a page load
    });
</script>
<body>
<div class="container">
    <h1>Add New Transaction</h1>
    <form name="add_transaction" action="${pageContext.request.contextPath}/controller" method="post">
        <input type="hidden" name="command" value="submit_transaction">

        <label for="type">Transaction Type:</label>
        <select name="type" id="type" required>
            <option value="income">Income</option>
            <option value="expense">Expense</option>
            <option value="transfer">Transfer</option>
        </select><br><br>

        <label for="source">Source Account:</label>
        <select name="source" id="source" required>
            <!-- Options will be populated dynamically -->
        </select>

        <label for="destination">Destination Account:</label>
        <select name="source" id="destination" required>
            <!-- Options will be populated dynamically -->
        </select>

        <label for="amount">Amount:</label>
        <input type="number" name="amount" id="amount" step="0.01" required><br><br>

        <button type="submit" class="primary-button">Add Transaction</button>
        <button class="cancel-button" onclick="location.href='${pageContext.request.contextPath}/controller?command=dashboard'">Cancel</button>
    </form>
</div>
</body>
</html>