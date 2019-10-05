<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
    <head>
        <title>Tableau de bord</title>
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

        <div class="published">
            Histoires publiées :
        </div>
        <br/>
        <br/>
        <table>
            <tr>
                <th>Titre de l'histoire</th>
                <th>Auteur principal</th>
                <th>Auteurs</th>
                <th>Action</th>
            </tr>
                <tr>
                    <c:forEach items="${histoires}" var="ouvrage">
                        <tr>
                            <td>${ouvrage.titre}</td>
                            <td>${ouvrage.auteurPrincipal}</td>
                            <%
                                   String authors = "";
                            %>
                            <c:forEach items="${ouvrage.auteurs}" var="auteur">
                                <%
                                    authors +=" ";
                                    authors += "${auteur}";
                                %>   
                            </c:forEach>
                            <td><%= authors %> </td>
                            <td><a href="controleur?action=getStory&id=${ouvrage.id_fil}&title=${ouvrage.titre}&idstory=${ouvrage.id}">lire cette histoire</a></td>
                        </tr>
                     </c:forEach>
               </tr>
            
        </table>
    </body>
</html>
