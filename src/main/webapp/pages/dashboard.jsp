<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<jsp:useBean id="now" class="java.util.Date"/>
<c:set var="userCurrencyId" value="${sessionScope.user.defaultCurrency}"/>
<c:forEach items="${sessionScope.currencies}" var="currency">
    <c:if test="${currency.id == userCurrencyId}">
        <c:set var="userCurrencySymbol" value="${currency.symbol}" />
    </c:if>
</c:forEach>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>MoneyTracker - Dashboard</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f4f4;
            margin: 0;
            padding: 0;
        }
        .dashboard-container {
            background-color: white;
            padding: 2rem;
            border-radius: 8px;
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
            max-width: 600px;
            margin: 50px auto;
            text-align: center;
        }
        .assets-row {
            display: flex;
            max-width: 700px;
            justify-content: space-between; /* Spreads the assets evenly */
            margin-bottom: 1.5rem;
        }
        .assets-container {
            background-color: white;
            padding: 0.5rem;
            border-radius: 8px;
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
            flex: 1; /* Makes all rectangles the same size */
            max-width: 20%; /* Adjusts size to fit 4 rectangles in one row */
            margin: 1rem;
            text-align: center;
        }
        h1 {
            color: #333;
        }
        .balance {
            font-size: 1.5rem;
            font-weight: bold;
        }
        .positive {
            color: green;
        }
        .negative {
            color: red;
        }
        .report {
            margin: 1rem 0;
        }
        .date {
            font-size: 0.9rem;
            color: #666;
            margin-bottom: 1.5rem;
        }
        button {
            background-color: #4CAF50;
            color: white;
            padding: 0.75rem;
            border: none;
            border-radius: 4px;
            font-size: 1rem;
            cursor: pointer;
            transition: background-color 0.3s;
        }
        button:hover {
            background-color: #45a049;
        }
        a {
            color: #4c59af;
            text-decoration: none;
            margin-top: 1.5rem;
            display: inline-block;
        }
        a:hover {
            text-decoration: underline;
        }
    </style>
</head>
<body>
<div class="dashboard-container">
    <h1>Welcome, ${userName}!</h1>
    <div class="date">Today: <fmt:formatDate value="${now}" pattern="EEE, MMM dd yyyy"/></div>
    <div class="assets-row">
        <c:forEach var="asset" items="${accounts}">
            <div class="assets-container">
                <p>${asset.name}</p>
                <p>(<fmt:formatNumber value="${asset.balance}" type="currency" currencySymbol="$" minFractionDigits="2"/>)</p>
            </div>
        </c:forEach>
    </div>
    <div class="report">
        <p>This month income: ${userCurrencySymbol} ${income}</p>
        <p>This month expense: ${userCurrencySymbol} ${expense}</p>
    </div>

    <!-- Conditionally display balance in green or red -->
    <div class="balance">
        <c:choose>
            <c:when test="${balance >= 0}">
                <span class="positive">Current Balance: <fmt:formatNumber value="${balance}" type="currency" currencySymbol="${userCurrencySymbol} " minFractionDigits="2"/></span>
            </c:when>
            <c:otherwise>
                <span class="negative">Current Balance: <fmt:formatNumber value="${balance}" type="currency" currencySymbol="${userCurrencySymbol} " minFractionDigits="2"/></span>
            </c:otherwise>
        </c:choose>
    </div>

    <br/><br/>
    <p class="positive">${successfulTransactionMsg}</p>
    <button onclick="location.href='${pageContext.request.contextPath}/pages/new_transaction.jsp'">New Transaction</button><br/>
    <a href="${pageContext.request.contextPath}/pages/view_transactions.jsp">View Transactions</a>
    <br/><br/>
    <a class="logout" href="${pageContext.request.contextPath}/controller?command=logout">Logout</a>
</div>
</body>
</html>
