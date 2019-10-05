<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
    <head>
        <title>Invitations</title>
        <link rel="icon" href="img/book.png" />
        <link rel="stylesheet" type="text/css" href="css/history.css" />        
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
            Vos invitations: 
        </div>
        <br/>
        <br/>
        <table>
            <tr>
                <th>Titre de l'histoire</th>
                <th>Actions possibles</th>
            </tr>
            <c:forEach items="${invites}" var="invite">
                <tr>
                    <td>"${invite.title}"</td>
                    <!--TODO: change this section-->
                    <td><a href="controleur?action=getNode&id=${invite.id}">Lire</a>, <a href="controleur?action=getNode&id=1">Modifier</a></td>
                </tr>
            </c:forEach>
        </table>
    </body>
</html>
