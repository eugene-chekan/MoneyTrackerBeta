<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:useBean id="now" class="java.util.Date"/>
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
    input[type="date"] {
        width: 100%;
        padding: 8px;
        margin-bottom: 10px;
        border: 1px solid #ddd;
        border-radius: 4px;
    }

    input[type="date"]::-webkit-calendar-picker-indicator {
        cursor: pointer;
        padding: 5px;
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
    const transactionConfig = {
        types: {
            <c:forEach items="${transactionTypes}" var="type" varStatus="status">
                "${type.name}": {
                    id: "${type.id}",
                    name: "${type.name}"
                }${!status.last ? ',' : ''}
            </c:forEach>
        }
    };

    function updateSourceOptions() {
        const typeSelect = document.getElementById("type");
        const sourceContainer = document.getElementById("source");
        const selectedType = typeSelect.value;

        // Create a new select element
        let selectElement = document.createElement("select");
        selectElement.name = "source";
        selectElement.id = "source";
        selectElement.required = true;

        // Add options based on selected type
        if (selectedType === transactionConfig.types.INCOME.id) {
            <c:forEach items="${incomeSources}" var="account">
            let option = new Option("${account.name}", "${account.id}");
            selectElement.add(option);
            </c:forEach>
        } else if (selectedType === transactionConfig.types.EXPENSE.id) {
            <c:forEach items="${accounts}" var="account">
            let option = new Option("${account.name}", "${account.id}");
            selectElement.add(option);
            </c:forEach>
        } else if (selectedType === transactionConfig.types.TRANSFER.id) {
            <c:forEach items="${accounts}" var="account">
            let option = new Option("${account.name}", "${account.id}");
            selectElement.add(option);
            </c:forEach>
        }

        // Replace the old select with the new one
        sourceContainer.parentNode.replaceChild(selectElement, sourceContainer);
    }

    function updateDestinationOptions() {
        const typeSelect = document.getElementById("type");
        const destinationContainer = document.getElementById("destination");
        const selectedType = typeSelect.value;

        // Create a new select element
        let selectElement = document.createElement("select");
        selectElement.name = "destination";
        selectElement.id = "destination";
        selectElement.required = true;

        // Add options based on selected type
        if (selectedType === transactionConfig.types.INCOME.id) {
            <c:forEach items="${accounts}" var="account">
            let option = new Option("${account.name}", "${account.id}");
            selectElement.add(option);
            </c:forEach>
        } else if (selectedType === transactionConfig.types.EXPENSE.id) {
            <c:forEach items="${expenseSources}" var="account">
            let option = new Option("${account.name}", "${account.id}");
            selectElement.add(option);
            </c:forEach>
        } else if (selectedType === transactionConfig.types.TRANSFER.id) {
            <c:forEach items="${accounts}" var="account">
            let option = new Option("${account.name}", "${account.id}");
            selectElement.add(option);
            </c:forEach>
        }

        // Replace the old select with the new one
        destinationContainer.parentNode.replaceChild(selectElement, destinationContainer);
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
            <c:forEach items="${transactionTypes}" var="transactionType">
                <option value="${transactionType.id}">${transactionType.name}</option>
            </c:forEach>
        </select>

        <label for="source">Source Account:</label>
        <select name="source" id="source" required>
            <!-- Options will be populated dynamically -->
        </select>

        <label for="destination">Destination Account:</label>
        <select name="destination" id="destination" required>
            <!-- Options will be populated dynamically -->
        </select>

        <label for="amount">Amount:</label>
        <input type="number" name="amount" id="amount" step="0.01" required>

        <label for="transactionDate">Date:</label>
        <input type="date" name="transactionDate" id="transactionDate"
               value="<fmt:formatDate value="${now}" pattern="yyyy-MM-dd"/>"
               max="<fmt:formatDate value="${now}" pattern="yyyy-MM-dd"/>"
               required>

        <button type="submit" class="primary-button">Add Transaction</button>
        <button class="cancel-button" onclick="location.href='${pageContext.request.contextPath}/controller?command=dashboard'">Cancel</button>
    </form>
</div>
</body>
</html>