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

        <div class="history">
            Votre historique: 
        </div>
        <br/>
        <br/>
        <table>
            <tr>
                <th>Titre de l'histoire</th>
                <th>Titre du paragraphe</th>
            </tr>
                <tr>
                    <td>Titre</td>
                    <td><a href="controleur?action=getNode&id=1">Paragraphe de l'histoire intitulée : </a></td>
                </tr>
            
        </table>
    </body>
</html>
