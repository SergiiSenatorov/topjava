<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%--<%@ page import="ru.javawebinar.topjava.util.TimeUtil" %>--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html lang="ru">
<head>
    <meta charset="utf-8">
    <title>Meals</title>
    <style>
        body {
            font-family: sans-serif;
            padding: 1em;
        }
        table {
            border-spacing: 0;
        }
        tr.exceeded {
            color: red;
        }
        tr.normal {
            color: green;
        }
        div.actions-block {
            padding: 0 0 1em 1em;
            min-width: 20em;
        }
    </style>
</head>
<body class="center">
    <jsp:useBean id="defaultLocale" scope="request" type="java.lang.String"/>
    <fmt:setLocale value="${defaultLocale}" />
    <h3><a href="index.html">Home</a></h3>
    <h2>Meals</h2>

    <div class="actions-block">
        <span><a href="meals?action=create">Add new meal</a></span>
    </div>
    <jsp:useBean id="isEditFormVisible" scope="request" type="java.lang.String"/>

    <c:catch var="curMealException">
        <jsp:useBean id="curMeal" scope="request" type="ru.javawebinar.topjava.model.Meal"/>
    </c:catch>

    <c:if test="${curMeal != null}">

        <div style="display: ${isEditFormVisible};">
            <form method="post" action="meals">
                <fieldset>
                    <legend>Edit meal</legend>

                    <div>
                        <c:if test="${empty curMeal.id}"><input type="hidden" name="id" value="${null}"></c:if>
                        <c:if test="${not empty curMeal.id}"><input type="hidden" name="id" value="${curMeal.id}"></c:if>

                        <dl>
                            <dt><label for="dateTime">Date&Time</label></dt>
                            <dd><input id="dateTime" type="datetime-local" name="dateTime" value="${curMeal.dateTime}"></dd>
                            <dt><label for="description">Description</label></dt>
                            <dd><input class="description" id="description" type="text" name="description" value="${curMeal.description}"></dd>
                            <dt><label for="calories">Calories</label></dt>
                            <dd><input id="calories" type="number" name="calories" value="${curMeal.calories}"></dd>
                        </dl>
                    </div>
                    <div>
                        <button type="submit">Save</button>
                        <button onclick="location.href='meals'" formmethod="get">Cancel</button>
                    </div>
                </fieldset>
            </form>
        </div>

    </c:if>

    <table class="meal-table">
        <thead>
        <tr>
            <th>Date</th>
            <th>Description</th>
            <th>Calories</th>
            <th></th>
            <th></th>
        </tr>
        </thead>
        <jsp:useBean id="mealList" scope="request" type="java.util.List"/>
        <c:forEach items="${mealList}" var="meal">
            <jsp:useBean id="meal" type="ru.javawebinar.topjava.model.MealWithExceed"/>
            <tr class="${meal.exceed ? "exceeded" : "normal"}">
                <fmt:parseDate value="${meal.dateTime}" pattern="y-M-dd'T'H:m" var="parsedDate"/>
                <fmt:formatDate value="${parsedDate}" pattern="yyyy-MM-dd HH:mm" var="formatedDate"/>
                <td>${formatedDate}</td>
                <td>${meal.description}</td>
                <td>${meal.calories}</td>
                <td><a  href="meals?action=edit&id=${meal.id}">Edit</a></td>
                <td><a  href="meals?action=delete&id=${meal.id}">Delete</a></td>
            </tr>
        </c:forEach>
    </table>
</body>
</html>