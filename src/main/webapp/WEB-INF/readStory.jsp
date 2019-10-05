<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
    <head>
        <title>Lecture</title>
        <link rel="icon" href="img/book.png" />
        <link rel="stylesheet" type="text/css" href="css/reading.css" />        
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <script>
            var list = [];
            function append(pid) {
                var input = document.createElement("input");
                input.setAttribute("type", "hidden");
                input.setAttribute("name", "p" + list.length);
                input.setAttribute("value", pid);
                document.getElementById("pushForm").appendChild(input);
                list.push(pid);
                
                var container = document.getElementById("para"+pid);
                var newPara = container.children[0].innerHTML;
                var buttons = container.children[1];

                var pageContent = document.getElementById("content");
                var currentPara = pageContent.children[0].innerHTML;
                pageContent.children[0].innerHTML = currentPara + "<br/>" + newPara;
                var newButtons = buttons.cloneNode(true);
                pageContent.replaceChild(newButtons, pageContent.children[1]);
                
            }

            function submitPush(){
                var form = document.getElementById("pushForm");
                form.submit();
            }
        </script>
    </head>
    <body onload="append(${start});">
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

        <div class="title">
            Titre de l'histoire : ${title}
            <br/>
        </div>

        <div class ="paragraph" id="content">
            <div></div>
            <div></div>
        </div>
        <br/>
        <br/>
        <br/>

        <form id="pushForm" action="controller" method="post">
            <input type="hidden" name="id" value="${storyId}">
            <input type="hidden" name="action" value="doPushHistory">
            <button type="submit" name="submit">Valider</button>
        </form>

        <c:forEach items="${container}" var="p">
                    <div class="hidden" id="para${p.key}">
                        <div>
                            ${p.value.content}
                        </div>
                        <div>
                            <c:forEach items="${p.value.childrenIds}" var="c">
                                <button onclick="append(${c.key});">${c.value}</button>
                            </c:forEach>
                        </div>
                    </div> <!--on click show paragraph TODO : redirect to controller-->
        </c:forEach>



    
    </body>
</html>
