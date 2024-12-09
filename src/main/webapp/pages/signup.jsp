<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
    <title>MoneyTracker - Sign Up</title>
</head>
<body>
    <div class="login-container">
        <form name="SignUpAction" action="${pageContext.request.contextPath}/controller" method="post">
            <h1>Sign Up for MoneyTracker</h1>
            <input type="hidden" name="command" value="signup">
            <input type="text" name="username" placeholder="Username" required>
            <input type="text" name="firstName" placeholder="First name" required>
            <input type="text" name="lastName" placeholder="Last name" required>
            <input type="email" name="email" placeholder="Email" required>
            <input type="password" name="password" placeholder="Password" required>
            <input type="password" name="confirmPassword" placeholder="Confirm Password" required checked>
            <!-- Currency Dropdown -->
            <label for="default_curr">Choose your currency:</label>
            <select name="default_curr" id="default_curr" required>
                <c:forEach var="currency" items="${currencies}">
                    <option value="${currency.id}">
                        ${currency.symbol} - ${currency.name}
                    </option>
                </c:forEach>
            </select>
            <button type="submit">Sign Up</button><br/>
            <div class="error-message">
                ${errorUserNameTaken}${errorPasswordMismatch}${errorMessage}
            </div>
        </form>
        <div class="signup-link">
            <p>Already have an account? <a href="${pageContext.request.contextPath}/index.jsp">Login</a></p>
        </div>
    </div>
</body>
</html>
