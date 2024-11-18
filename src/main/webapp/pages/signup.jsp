<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="lt.ehu.student.moneytrackerbeta.model.CurrencyEnum" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>MoneyTracker - Sign Up</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f4f4;
            margin: 0;
            padding: 0;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
        }
        .login-container {
            background-color: white;
            padding: 2rem;
            border-radius: 8px;
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
            width: 300px;
        }
        h1 {
            text-align: center;
            color: #333;
            margin-bottom: 1.5rem;
        }
        form {
            display: flex;
            flex-direction: column;
        }
        input, select {
            margin-bottom: 1rem;
            padding: 0.5rem;
            border: 1px solid #ddd;
            border-radius: 4px;
            font-size: 1rem;
            width: 100%;
            box-sizing: border-box;
        }
        select {
            background-color: #fff;
            color: #333;
            appearance: none;
            -webkit-appearance: none;
            -moz-appearance: none;
            background-image: url('data:image/svg+xml;charset=US-ASCII,<svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="gray"><polygon points="0,0 16,0 8,10" /></svg>');
            background-repeat: no-repeat;
            background-position: right 10px top 50%;
            padding-right: 1.5rem;
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
        .signup-link {
            text-align: center;
            margin-top: 1rem;
        }
        .signup-link a {
            color: #4CAF50;
            text-decoration: none;
        }
        .signup-link a:hover {
            text-decoration: underline;
        }
        .error-message {
            color: #ff7f7f;
        }
    </style>
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
                <c:forEach var="currency" items="<%= CurrencyEnum.values() %>">
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
