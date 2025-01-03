<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<jsp:useBean id="now" class="java.util.Date"/>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
    <title>Add Transaction</title>
</head>
<body>
    <div class="container">
        <h1>Add New Transaction</h1>
        <form name="add_transaction" action="${pageContext.request.contextPath}/controller" method="post">
            <input type="hidden" name="command" value="submit_transaction">

            <label for="type">Transaction Type:</label>
            <select name="type" id="type" required>
                <c:forEach items="${applicationScope.availableTransactionTypes}" var="transactionType">
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
    <script>
        const transactionConfig = {
            types: {
                <c:forEach items="${applicationScope.availableTransactionTypes}" var="type" varStatus="status">
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
            if (selectedType === transactionConfig.types.Income.id) {
                <c:forEach items="${incomeSources}" var="account">
                let option = new Option("${account.name}", "${account.id}");
                selectElement.add(option);
                </c:forEach>
            } else if (selectedType === transactionConfig.types.Expense.id) {
                <c:forEach items="${accounts}" var="account">
                let option = new Option("${account.name}", "${account.id}");
                selectElement.add(option);
                </c:forEach>
            } else if (selectedType === transactionConfig.types.Transfer.id) {
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
            if (selectedType === transactionConfig.types.Income.id) {
                <c:forEach items="${accounts}" var="account">
                let option = new Option("${account.name}", "${account.id}");
                selectElement.add(option);
                </c:forEach>
            } else if (selectedType === transactionConfig.types.Expense.id) {
                <c:forEach items="${expenseSources}" var="account">
                let option = new Option("${account.name}", "${account.id}");
                selectElement.add(option);
                </c:forEach>
            } else if (selectedType === transactionConfig.types.Transfer.id) {
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
</body>
</html>