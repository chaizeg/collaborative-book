<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
    <head>
        <title>Connexion</title>
        <link rel="icon" href="img/book.png" />
        <link rel="stylesheet" type="text/css" href="css/login.css" />        
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    </head>
    <body>
        <div class="navbar">
                <a href="">Connexion</a>
                <a href="controller?action=reading">Lecture</a>
                <a href="controller?action=homeScreen">Accueil</a>
         
        </div>

    
        <div class="wrapper fadeInDown">
                <div class="titre">
                        Livres dont VOUS êtes le héros
                    </div>
            <div id="formContent">
            <!-- Icon -->
            <div class="fadeIn first">
                <img src="img/book.png" id="icon" alt="User Icon"/>
            </div>
            <c:if test="${not empty loginError}">
                <script>
                    window.addEventListener("load", function(){alert("${loginError}");})
                </script>
            </c:if>
            <!-- Login Form -->
            <form action="controller" method="post" >
                <input type="text" id="login" class="fadeIn second" name="id" placeholder="Nom d'utilisateur">
                <input type="password" id="password" class="fadeIn third" name="pwd" placeholder="Mot de passe">
                <input type="hidden" name="action" value="doLogin"/>
                <button type="submit" name="submit" class="fadeIn fourth">S'authentifier</button>
            </form>
        
        
        </div>
      </div>
    </body>
</html>
