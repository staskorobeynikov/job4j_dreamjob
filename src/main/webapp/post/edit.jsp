<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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

    <title>Работа мечты</title>
</head>
<script src="https://code.jquery.com/jquery-3.4.1.min.js" ></script>
<script>
    function validate() {
        let fields = [$("#name"), $("#description")];
        let result = true;
        let answer = '';
        for (let i = 0; i < fields.length; i++) {
            if (fields[i].val() === "") {
                answer += fields[i].attr("placeholder") + "\n";
                result = false;
            }
        }
        if (!result) {
            alert(answer);
        }
        return result;
    }
</script>
<body>
<div class="container pt-3">
    <jsp:include page="/headers.jsp"/>
    <div class="row">
        <div class="card" style="width: 100%">
            <div class="card-header">
                <c:choose>
                    <c:when test="${post.id == 0}">
                        Новая вакансия.
                    </c:when>
                    <c:otherwise>
                        Редактирование вакансии.
                    </c:otherwise>
                </c:choose>
            </div>
            <div class="card-body">
                <form action="<c:url value="/posts.do?id=${post.id}"/>" method="post">
                    <div class="form-group">
                        <label for="name">Имя</label>
                        <input type="text" class="form-control" id="name" name="name" value="<c:out value="${post.name}"/>" placeholder="Введите ваше имя...">
                    </div>
                    <div class="form-group">
                        <label for="description">Описание</label>
                        <input type="text" class="form-control" id="description" name="description" value="<c:out value="${post.description}"/>" placeholder="Введите описание...">
                    </div>
                    <button type="submit" class="btn btn-primary" onclick="validate()">Сохранить</button>
                </form>
            </div>
        </div>
    </div>
</div>
</body>
</html>