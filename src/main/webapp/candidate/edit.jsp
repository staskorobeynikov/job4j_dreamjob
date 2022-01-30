<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="ru.job4j.dream.store.PsqlStore" %>
<%@ page import="ru.job4j.dream.model.Candidate" %>
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

<body>
<div class="container pt-3">
    <jsp:include page="/headers.jsp"/>
    <div class="row">
        <div class="card" style="width: 100%">
            <div class="card-header">
                <c:choose>
                    <c:when test="${candidate.id == 0}">
                        Новый кандидат.
                    </c:when>
                    <c:otherwise>
                        Редактирование кандидата.
                    </c:otherwise>
                </c:choose>
            </div>
            <div class="card-body">
                <form action="<c:url value="/candidates.do?id=${candidate.id}"/>" method="post">
                    <div class="form-group">
                        <label for="name">Имя</label>
                        <input type="text" class="form-control" id="name" name="name" value="<c:out value="${candidate.name}"/>" placeholder="Введите ваше имя...">
                    </div>
                    <div class="form-group">
                        <label for="city">Город</label>
                        <select class="custom-select" id="city" name="city">
                            <option value="<c:out value="${candidate.cityId}"/>" selected><c:out value="${candidate.city}"/></option>
                        </select>
                    </div>
                    <button type="submit" class="btn btn-primary" onclick="validate()">Сохранить</button>
                </form>
            </div>
        </div>
    </div>
</div>
<script>
    function validate() {
        let field = $("#name");
        let result = true;
        let answer = '';
        if (field.val() === "") {
            answer += field.attr("placeholder") + "\n";
            result = false;
        }
        if (!result) {
            alert(answer);
        }
        return result;
    }
    $(document).ready(function () {
        $.ajax({
            type: "GET",
            url: "http://localhost:8080/dreamjob/cities",
            dataType: "json",
            success: function (data) {
                let cities = "";
                for (let i = 0; i < data.length; i++) {
                    cities += "<option value=" + data[i]['id'] + ">" + data[i]['name'] + "</option>";
                }
                $('#city option:last').after(cities);
            }
        })
    })
</script>
</body>
</html>