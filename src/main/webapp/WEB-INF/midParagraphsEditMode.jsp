<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
    <head>
        <title>Mes histoires</title>
        <link rel="icon" href="img/book.png" />
        <link rel="stylesheet" type="text/css" href="css/edit.css" />        
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
            Éditer : ${choiceTitle}
        </div>
        <br/>
        <br/>
        <div class="maintext">
            ${source.content}
        </div>
        <div class="modifier">
        <!--modify text // add choices //delete paragraph if the choice doesn't lead to another paragraph : droits d'accès-->
        <c:if test="${(locked == 0) && (userId == source.authorId)}">
        <a style ="text-align: center;" href="controller?action=editParagraph&storyId=${storyId}&paragraphId=${source.id}&choiceTitle=${choiceTitle}&choiceId=${choiceId}&authorId=${source.authorId}">Modifier ce paragraphe </a>
        </c:if>

        <!--CHECKING IS USER IS INVITED TO STORY-->
        <c:set var="contains" value="0" />
        <c:forEach var="inv" items="${invitees}">
        <c:if test="${inv eq userId}">
            <c:set var="contains" value="1" />
        </c:if>
        </c:forEach>

        <c:if test="${(isOpen == 1)||((locked == 0) && (contains==1 || userId == source.authorId))}">
        <a style ="text-align: center;" href="controller?action=addChoices&storyId=${storyId}&paragraphId=${source.id}&choiceTitle=${choiceTitle}&choiceId=${choiceId}&authorId=${source.authorId}">Ajouter des choix à ce paragraphe</a>
        </c:if>

        <c:if test="${((locked == 0) && (canBeDeleted == 1) && (userId == source.authorId))}">
            <a style ="text-align: center;" href="controller?action=deleteParagraph&storyId=${storyId}&paragraphId=${source.id}&choiceTitle=${choiceTitle}&choiceId=${choiceId}&authorId=${source.authorId}">Supprimer ce paragraphe</a>
        </c:if>
        </div>
        <br/>
        <br/>
        <table>
            <tr>
                <th>Choix</th>
            </tr>
            <tr>
                <c:forEach items="${choices}" var="choice">
                    <tr>
                        <td>
                            <c:choose>
                                <c:when test="${(choice.destinationId == -1) && (choice.locked == 0)}">
                                    <a href="controller?action=writeParagraph&storyId=${storyId}&source=${source.id}&choice=${choice.choiceId}&choiceTitle=${choice.title}">Rédiger : ${choice.title}</a>
                                </c:when>    
                                <c:when test="${(choice.destinationId == -1) && (choice.locked == 1)}">
                                    En cours de rédaction : ${choice.title}
                                </c:when>
                                <c:otherwise>
                                   Lire - Modifier :<a href="controller?action=readModify&storyId=${storyId}&paragraphId=${choice.destinationId}&source=${source.id}&choice=${choice.choiceId}&locked=${choice.locked}&choiceTitle=${choice.title}">${choice.title}</a>
                                </c:otherwise>
                            </c:choose>
                        </td>
                    </tr>
                </c:forEach>
            </tr>
            
        </table>
    </body>
</html>
