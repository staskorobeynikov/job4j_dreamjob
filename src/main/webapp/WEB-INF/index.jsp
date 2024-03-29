<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!doctype html>
<html lang="en">
<head>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css"
          integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh" crossorigin="anonymous">
    <script src="https://code.jquery.com/jquery-3.4.1.slim.min.js"
            integrity="sha384-J6qa4849blE2+poT4WnyKhv5vZF5SrPo0iEjwBvKU7imGFAV0wwj1yYfoRSJoZ+n" crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.0/dist/umd/popper.min.js"
            integrity="sha384-Q6E9RHvbIyZFJoft+2mJbHaEWldlvI9IOYy5n3zV9zzTtmI3UksdQRVvoxMfooAo" crossorigin="anonymous"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/js/bootstrap.min.js"
            integrity="sha384-wfSDF2E50Y2D1uUdj0O3uMBJnjuUD4Ih7YwaYd1iqfktj0Uod8GCExl3Og8ifwB6" crossorigin="anonymous"></script>
    <title>Работа мечты!</title>
</head>
<body>
<div class="container">
    <jsp:include page="../headers.jsp"/>
    <div class="row">
        <div class="card" style="width: 100%">
            <div class="card-header">
                Сегодняшние вакансии.
            </div>
            <div class="card-body">
                <c:choose>
                    <c:when test="${posts == 0}">
                        Новых вакансий за сегодня нет
                    </c:when>
                    <c:when test="${posts != 0}">
                        <table class="table">
                            <thead>
                            <tr>
                                <th scope="col">Названия</th>
                                <th scope="col">Описание</th>
                                <th scope="col">Дата создания</th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:forEach items="${postLastDay}" var="post">
                                <tr>
                                    <td>
                                        <c:out value="${post.name}"/>
                                    </td>
                                    <td>
                                        <c:out value="${post.description}"/>
                                    </td>
                                    <td>
                                        <c:out value="${post.created}"/>
                                    </td>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                    </c:when>
                </c:choose>
            </div>
        </div>
    </div>
    <div class="row pt-3">
        <div class="card" style="width: 100%">
            <div class="card-header">
                Сегодняшние кандидаты.
            </div>
            <div class="card-body">
                <c:choose>
                    <c:when test="${candidates == 0}">
                        Новых вакансий за сегодня нет
                    </c:when>
                    <c:when test="${candidates != 0}">
                        <table class="table">
                            <thead>
                            <tr>
                                <th scope="col">Названия</th>
                                <th scope="col">Город</th>
                                <th scope="col">Дата создания</th>
                                <th scope="col">Фото</th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:forEach items="${candidateLastDay}" var="candidate">
                                <tr>
                                    <td>
                                        <c:out value="${candidate.name}"/>
                                    </td>
                                    <td>
                                        <c:out value="${candidate.city}"/>
                                    </td>
                                    <td>
                                        <fmt:formatDate value="${candidate.created}" type="both" pattern='dd MMMM, EEEE, yyyy г.'/>
                                    </td>
                                    <td>
                                        <img src="<c:url value='/download?photoId=${candidate.photoId}'/>" width="100px" height="100px"/>
                                    </td>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                    </c:when>
                </c:choose>
            </div>
        </div>
    </div>
</div>
</body>
</html>
