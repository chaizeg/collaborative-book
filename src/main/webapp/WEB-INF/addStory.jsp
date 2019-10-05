<%@page contentType="text/html" pageEncoding="UTF-8"%>
<html>
    <head>
        <title>Ajout d'histoire</title>
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
                <label for="title">Titre</label>
                <br/>
                <input type="text" style="display: inline-block;"id="title" name="title" placeholder="Titre..">
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
                </br>
                </br>

                <label for="openPublic">L'histoire est:</label>
                </br>
                <select id="public" name="public">
                    <option value="yes">Ouverte</option>
                    <option value="no">Sur Invitation</option>
                </select>

                </br>
                </br>

                <div class="people">
                    <div id="newlinkP">
                        <div>
                            <label for="person">Collaborateur: </label>
                            </br>
                            <input type="text" name="linkurlP[]" placeholder="nom d'utilisateur"> 
                        
                        </div>
                    </div>
                    <p id="addnewP">
                        <a href="javascript:new_linkP()">Ajouter un nouveau collaborateur</a>
                    </p>                 
                    <div id="newlinktplP" style="display:none">
                        <div>
                        <label for="person">Collaborateur: </label>
                        </br>
                        <input type="text" name="linkurlP[]" placeholder="nom d'utilisateur"> 
                        </div>
                    </div>
                </div>



                <label for="conclusion">S'agit-il d'un paragraphe de conclusion?</label>
                </br>
                <select id="conc" name="conc">
                    <option value="yes">Oui</option>
                    <option value="no">Non</option>
                </select>

                </br>
                </br>

                <div class="options">
                    <div id="newlink">
                        <div>
                            <label for="choice">Choix: </label>
                            </br>
                            <input type="text" name="linkurl[]" placeholder="Choix.."> 
                        
                        </div>
                    </div>
                    <p id="addnew">
                        <a href="javascript:new_link()">Ajouter un nouveau choix</a>
                    </p>                 
                    <div id="newlinktpl" style="display:none">
                        <div>
                        <label for="choice">Choix: </label>
                        </br>
                        <input type="text" name="linkurl[]" placeholder="Choix.."> 
                        </div>
                    </div>
                </div>
                <br/>
                <div id="buttons">
                    <input type="submit" value="Soumettre">
                    <input type="hidden" name="action" value="doAdd">
                    <a href="controller?action=unlockUser" id="cancel">Annuler</a>
                </div>
            </form>
        </div>
        <script type="text/javascript" src="js/adding.js"></script>
    </body>
</html>
