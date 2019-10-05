<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
    <head>
        <title>Mes histoires</title>
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
            Vos histoires: 
        </div>
        <br/>
        <br/>
        <table>
            <tr>
                <th>Titre</th>
                <th>Auteurs</th>
            </tr>
            <tr>
                <c:forEach items="${openStories}" var="st">
                    <tr>
                        <td>
                            <a href="controller?action=readStory&storyId=${st.id}&start=1">${st.title}</a>                            
                        </td>
                        <td>
                            <c:forEach items="${st.authors}" var="aut">
                                ${aut} <c:out value=" "/>
                            </c:forEach>
                        </td>
                    </tr>
                </c:forEach>
                
            </tr>
            
        </table>
    </body>
</html>
