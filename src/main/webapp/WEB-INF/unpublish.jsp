<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
    <head>
        <title>Histoires à dépublier</title>
        <link rel="icon" href="img/book.png" />
        <link rel="stylesheet" type="text/css" href="css/publish.css" />        
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    </head>
    <body>
        <div class="navbar">
            <a href="controller?action=logout">Déconnexion</a>
            <a href="controller?action=reading">Lecture</a>
            <a href="controller">Accueil</a>
        </div>

        <div class="titre">
            Livres dont VOUS êtes le héros
        </div>
        <br/>
        <br/>
        <br/>

        <div class="history">
            Vos histoires: 
        </div>
        <br/>
        <br/>
        <form action="controller" method="post" accept-charset="UTF-8">
            <select name="selected">
                <c:forEach items="${storiesU}" var="st">
                    <option value="${st.id}">${st.title}</option>
                </c:forEach>
            </select>
            <br/>
            <br/>
            <br/>

            <input type="submit" value="Dépublier" />
            <input type="hidden" name="action" value="doUnpublish"/>
        </form>
        <%-- <table>
            <tr>
                <th>Titre de l'histoire</th>
                <th>Actions possibles</th>
            </tr>
            <c:forEach items="${stories}" var="st">
                <tr>
                    <td>"${st.title}"</td>
                    <td>
                        <form action="controller" method="post" accept-charset="UTF-8" >
                            <input type="hidden" name="id" value="${st.id}">
                            <input type="submit" value="Publier" />
                            <input type="hidden" name="action" value="doPublish"/>
                        </form>
                    </td>
                </tr>
            </c:forEach>
        </table> --%>
    </body>
</html>
