<%@page contentType="text/html" pageEncoding="UTF-8"%>
<html>
    <head>
        <title>Modifier paragraphe</title>
        <link rel="icon" href="img/book.png" />
        <link rel="stylesheet" type="text/css" href="css/adding.css" />        
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
        <div class="ajout">
            <form id="adding" action="controller" method="post">
                <label for="title" style="color : #281e61; font-size: 4àpx;">Modifier le texte du paragraphe intitulé : ${choiceTitle}</label>
                <br/>
                <br/>
                <br/>
                <br/>
                <br/>

                <label for="paragraphe">Texte du paragraphe</label>
                <br/>
                <br/>
                <textarea name="content">
                </textarea>
                <br/>
                <br/>
                <br/>

                <div id="buttons">
                    <input type="submit" value="Soumettre">
                    <input type="hidden" name="action" value="doModifyText">
                    <input type="hidden" name="choiceTitle" value="${choiceTitle}">
                    <input type="hidden" name="storyId" value="${storyId}">
                    <input type="hidden" name="choiceId" value="${choiceId}">
                    <input type="hidden" name="paragraphId" value="${paragraphId}">
                    <a href="controller?action=unlock" id="cancel">Annuler</a>
                </div>
            </form>
        </div>
        <script type="text/javascript" src="js/adding.js"></script>
    </body>
</html>
