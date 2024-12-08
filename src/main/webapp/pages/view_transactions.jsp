<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:useBean id="now" class="java.util.Date"/>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>MoneyTracker - View Transactions</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f4f4;
            margin: 0;
            padding: 20px;
        }

        .container {
            background-color: white;
            padding: 2rem;
            border-radius: 8px;
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
            max-width: 1200px;
            margin: 0 auto;
        }

        h1 {
            text-align: center;
            color: #333;
            margin-bottom: 1.5rem;
        }

        .filters {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
            gap: 1rem;
            margin-bottom: 2rem;
        }

        .filter-group {
            display: flex;
            flex-direction: column;
        }

        label {
            margin-bottom: 0.5rem;
            color: #666;
        }

        input, select {
            margin-bottom: 1rem;
            padding: 0.5rem;
            border: 1px solid #ddd;
            border-radius: 4px;
            font-size: 1rem;
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

        .transactions-container {
            margin-top: 2rem;
            max-height: 500px;
            overflow-y: auto;
            border: 1px solid #ddd;
            border-radius: 4px;
        }

        table {
            width: 100%;
            border-collapse: collapse;
        }

        th, td {
            padding: 1rem;
            text-align: left;
            border-bottom: 1px solid #ddd;
        }

        th {
            background-color: #f8f8f8;
            position: sticky;
            top: 0;
        }

        tr:hover {
            background-color: #f5f5f5;
        }

        .no-transactions {
            text-align: center;
            padding: 2rem;
            color: #666;
        }

        .error-message {
            color: #ff7f7f;
            text-align: center;
            margin-bottom: 1rem;
        }

        @media (max-width: 768px) {
            .filters {
                grid-template-columns: 1fr;
            }

            th, td {
                padding: 0.5rem;
            }
        }

        .positive {
            color: #2ecc71;
        }
        
        .negative {
            color: #e74c3c;
        }

        .transfer {
            color: dimgray;
        }
        
        .transactions-container table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
        }
        
        .transactions-container th,
        .transactions-container td {
            padding: 12px;
            text-align: left;
            border-bottom: 1px solid #ddd;
        }
        
        .transactions-container th {
            background-color: #f8f9fa;
            font-weight: bold;
        }
        
        .transactions-container tr:hover {
            background-color: #f5f5f5;
        }
        
        .no-transactions {
            text-align: center;
            padding: 40px;
            color: #666;
            font-style: italic;
        }
    </style>
</head>
<body>
<div class="container">
    <h1>View Transactions</h1>

    <form action="${pageContext.request.contextPath}/controller" method="get">
        <input type="hidden" name="command" value="view_transactions">

        <div class="filters">
            <div class="filter-group">
                <label for="dateFrom">From Date:</label>
                <input type="date" id="dateFrom" name="dateFrom" value="<fmt:formatDate value="${now}" pattern="yyyy-MM-dd"/>">
            </div>

            <div class="filter-group">
                <label for="dateTo">To Date:</label>
                <input type="date" id="dateTo" name="dateTo" value="<fmt:formatDate value="${now}" pattern="yyyy-MM-dd"/>">
            </div>

<%--            <div class="filter-group">--%>
<%--                <label for="account">Account:</label>--%>
<%--                <select id="account" name="account">--%>
<%--                    <option value="">All Accounts</option>--%>
<%--                    <c:forEach items="${accounts}" var="account">--%>
<%--                        <option value="${account.id}" ${param.account eq account.id ? 'selected' : ''}>--%>
<%--                                ${account.name}--%>
<%--                        </option>--%>
<%--                    </c:forEach>--%>
<%--                </select>--%>
<%--            </div>--%>

            <div class="filter-group">
                <label for="type">Transaction Type:</label>
                <select id="type" name="type">
                    <option value="0">All Types</option>
                    <c:forEach items="${sessionScope.transactionTypes}" var="type">
                        <option value="${type.id}" ${param.type eq type.id ? 'selected' : ''}>${type.name}</option>
                    </c:forEach>
                </select>
            </div>

            <div class="filter-group">
                <label for="search">Search:</label>
                <input type="text" id="search" name="search" placeholder="Search description..." value="${param.search}">
            </div>
        </div>

        <button type="submit">Apply Filters</button>
    </form>

    <div class="error-message">${errorMessage}</div>

    <div class="transactions-container">
        <c:choose>
            <c:when test="${not empty transactions}">
                <table>
                    <thead>
                        <tr>
                            <th>Date</th>
                            <th>Type</th>
                            <th>From</th>
                            <th>To</th>
                            <th>Amount</th>
                            <th>Comment</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="transaction" items="${transactions}">
                            <tr>
                                <td>
                                    <fmt:formatDate value="${transaction.timestamp}" 
                                                  pattern="yyyy-MM-dd HH:mm:ss"/>
                                </td>
                                <td>${transaction.transactionType}</td>
                                <td>${transaction.sourceName}</td>
                                <td>${transaction.destinationName}</td>
                                <td>
                                    <c:choose>
                                        <c:when test="${transaction.transactionType == 'INCOME'}">
                                            <span class="positive">
                                                ${transaction.currencySymbol} 
                                                <fmt:formatNumber value="${transaction.amount}" 
                                                          pattern="#,##0.00"/>
                                            </span>
                                        </c:when>
                                        <c:when test="${transaction.transactionType == 'EXPENSE'}">
                                            <span class="negative">
                                                ${transaction.currencySymbol} 
                                                <fmt:formatNumber value="${transaction.amount}" 
                                                          pattern="-#,##0.00"/>
                                            </span>
                                        </c:when>
                                        <c:when test="${transaction.transactionType == 'TRANSFER'}">
                                            <span class="transfer">
                                                ${transaction.currencySymbol} 
                                                <fmt:formatNumber value="${transaction.amount}" 
                                                          pattern="#,##0.00"/>
                                            </span>
                                        </c:when>
                                        <c:otherwise>
                                            <span>
                                                ${transaction.currencySymbol} 
                                                <fmt:formatNumber value="${transaction.amount}" 
                                                          pattern="#,##0.00"/>
                                            </span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td>${transaction.comment}</td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </c:when>
            <c:otherwise>
                <div class="no-transactions">
                    <p>No transactions found for the selected period.</p>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
    <br/>
    <div class="navigation">
        <a href="${pageContext.request.contextPath}/controller?command=dashboard" class="back-button">
            Back to Dashboard
        </a>
    </div>
</div>
</body>
</html>