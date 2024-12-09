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
    <title>MoneyTracker - View Transactions</title>
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

        <button class="primary-button" type="submit">Apply Filters</button>
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