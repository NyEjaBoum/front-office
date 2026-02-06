<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Liste des réservations</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css"/>
</head>
<body>
    <div class="container">
        <h1>Liste des réservations</h1>

        <form method="get" action="${pageContext.request.contextPath}/reservations" class="filtre-form">
            <label for="date">Filtrer par date d'arrivée :</label>
            <input type="date" id="date" name="date" value="${dateFiltre}" />
            <button type="submit">Filtrer</button>
            <c:if test="${not empty dateFiltre}">
                <a href="${pageContext.request.contextPath}/reservations" class="btn-reset">Réinitialiser</a>
            </c:if>
        </form>

        <c:choose>
            <c:when test="${empty reservations}">
                <p class="empty-message">Aucune réservation trouvée.</p>
            </c:when>
            <c:otherwise>
                <table>
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Client</th>
                            <th>Hôtel</th>
                            <th>Date d'arrivée</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="reservation" items="${reservations}">
                            <tr>
                                <td>${reservation.id}</td>
                                <td>${reservation.idClient}</td>
                                <td>${reservation.hotel.nom}</td>
                                <td>${reservation.dateArrivee}</td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </c:otherwise>
        </c:choose>
    </div>
</body>
</html>
