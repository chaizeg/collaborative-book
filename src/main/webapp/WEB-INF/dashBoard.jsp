<%@ page contentType="text/html; charset=utf-8"%>
<html>
    <head>
        <title>Tableau de bord</title>
        <link rel="icon" href="img/book.png" />
        <link rel="stylesheet" type="text/css" href="css/dashboard.css" />        
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
        <div class="tableau">
            Tableau de bord
        </div>
        <div class="wrapper">
            <div class="tasks">
                <br/>
                <br/>
                <a href="controller?action=add">Créer une histoire</a>
                <br/>
                <br/>
                <a href="controller?action=showStories">Éditer mes histoires</a>
                <br/>
                <br/>
                <a href="controller?action=publish">Publier une histoire</a>
                <br/>
                <br/>
                <a href="controller?action=unpublish">Dépublier une histoire</a>
                <br/>
                <br/>
                <a href="controller?action=history">Historique</a>
                <br/>
                <br/>
            </div>
        </div>
    </body>
</html>
