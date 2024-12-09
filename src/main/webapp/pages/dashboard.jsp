<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
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
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
    <title>MoneyTracker - Dashboard</title>
</head>
<body>
<div class="container">
    <h1>Welcome, ${userName}!</h1>
    <div class="date" style="text-align: center;">Today: <fmt:formatDate value="${now}" pattern="EEE, MMM dd yyyy"/></div>
    <div class="assets-row">
        <c:forEach var="asset" items="${accounts}">
            <div class="assets-container">
                <p>${asset.name}</p>
                <p>(<fmt:formatNumber value="${asset.balance}" type="currency" currencySymbol="$" minFractionDigits="2"/>)</p>
            </div>
        </c:forEach>
    </div>
    <div class="report">
        <c:set var="userCurrency" value="${sessionScope.userDefaultCurrency}"/>
        <p>This month income: ${income} ${userCurrency.symbol}</p>
        <p>This month expense: ${expense} ${userCurrency.symbol}</p>
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
    <button class="primary-button" onclick="location.href='${pageContext.request.contextPath}/pages/new_transaction.jsp'">New Transaction</button>
    <button class="primary-button" onclick="location.href='${pageContext.request.contextPath}/pages/view_transactions.jsp'">View Transactions</button>
    <br/><br/>
    <a class="logout" href="${pageContext.request.contextPath}/controller?command=logout">Logout</a>
</div>
</body>
</html>
