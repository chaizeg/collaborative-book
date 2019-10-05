<%@ page contentType="text/html; charset=utf-8"%>
<html>
    <head>
        <title>Accueil</title>
        <link rel="icon" href="WEB-INF/img/book.png" />
        <link rel="stylesheet" type="text/css" href="css/homeScreen.css" />        
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    </head>
    <body>
        <div class="navbar">
            <a href="controller?action=login">Connexion</a>
            <a href="controller?action=reading">Lecture</a>
            <a href="">Accueil</a>
        </div>
        <div class="titre">
            Livres dont VOUS êtes le héros
        </div>
        <div class="description">
                Le but de l’application est de permettre l’écriture de « livres dont VOUS êtes le héros »
                participatifs.
                <br/>
                Le principe du livre dont VOUS êtes le héros est le suivant : il contient une histoire divisée
                en paragraphes numérotés. À la fin de chaque paragraphe, il vous est demandé de prendre
                une décision sur la suite des événements, chaque choix possible correspondant à un certain
                numéro de paragraphe à lire ensuite.
                <br/>
                <br/>
                <div style="text-align: left;">Par exemple :
                <ol >
                    <li>Votre tram arrive et ses portes s’ouvrent. Vous vous apprêtez à monter lorsque vous
                            entendez un bruit derrière le coin de l’immeuble voisin. Si vous décidez de monter dans
                            le tram, allez en 2 ; si vous décidez d’aller voir ce qui se passe, allez en 3.</li>
                            <li>Dans le tram, vous rencontrez..</li>
                            <li>En tournant le coin, vous découvrez..</li>
                </ol>
            </div>
        </div>
    </body>
</html>
