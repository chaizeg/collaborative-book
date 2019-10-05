<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 

<html>
    <head>
        <title>Ajout de paragraphe</title>
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
                <label for="title" style="color : #281e61; font-size: 4àpx;">Titre du choix : ${currentChoice}</label>
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

  

                <label for="conclusion">S'agit-il d'un paragraphe de conclusion?</label>
                </br>
                <select id="conc" name="conc">
                    <option value="yes">Oui</option>
                    <option value="no">Non</option>
                </select>

                </br>
                </br>

                <div id="newlinkList">

                        <div>
                            <label for="choice">L'histoire doit nécessairement passer par le paragraphe: </label>
                            </br>
                            <select name = "dropdown0" id="dropdown0">
                                <option value="NADA" selected></option>
                                <c:forEach items="${listParagraphs}" var="choice">
                                    <option value="${choice.destinationId}"> ${choice.title}</option>
                                </c:forEach>
                            </select>
                        </div>
                    <p id="addnewDropdown">
                        <a href="javascript:new_linkDropdown()">Ajouter un nouveau choix</a>
                    </p>                 
                    <div id="newlinktplDr" style="display:none">
                            <br/>
                            <br/>
                            <br/>
                            <c:set var="increm" value="1" scope="request" />
                            <select name = "dropdown${increm}" id="dropdown${increm}">
                                <option value="NADA" selected></option>
                                <c:forEach items="${listParagraphs}" var="para">
                                    <option value="${para.destinationId}"> ${para.title}</option>
                                </c:forEach>
                            </select>
                            <c:set var="increm" value="${increm+1}" scope="request" />
                    </div>
                </div>
                <div id="newlinkList">

                        <div>
                            <label for="choice">L'histoire ne doit pas passer par le paragraphe: </label>
                            </br>
                            <select name = "dropdow0" id="dropdow0">
                                <option value="NADA" selected></option>
                                <c:forEach items="${listParagraphs}" var="choice">
                                    <option value="${choice.destinationId}"> ${choice.title}</option>
                                </c:forEach>
                            </select>
                        </div>
                    <p id="addnewDropdown">
                        <a href="javascript:new_linkDropdown()">Ajouter un nouveau choix</a>
                    </p>                 
                    <div id="newlinktplDr" style="display:none">
                            <br/>
                            <br/>
                            <br/>
                            <c:set var="increm" value="1" scope="request" />
                            <select name = "dropdow${increm}" id="dropdow${increm}">
                                <option value="NADA" selected></option>
                                <c:forEach items="${listParagraphs}" var="para">
                                    <option value="${para.destinationId}"> ${para.title}</option>
                                </c:forEach>
                            </select>
                            <c:set var="increm" value="${increm+1}" scope="request" />
                    </div>
                </div>

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
                    <input type="hidden" name="action" value="doAddParagraph">
                    <input type="hidden" name="currentChoice" value="${currentChoice}">
                    <input type="hidden" name="storyId" value="${storyId}">
                    <input type="hidden" name="choiceId" value="${choiceId}">
                    <input type="hidden" name="sourceId" value="${sourceId}">
                    <input type="hidden" name="noConditions" value="${noConditions}">
                    <a href="controller?action=unlock&storyId=${storyId}&choiceId=${choiceId}&noConditions=${noConditions}" id="cancel">Annuler</a>
                </div>
            </form>
        </div>
        <script type="text/javascript" src="js/adding.js"></script>
    </body>
</html>
