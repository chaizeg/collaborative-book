package fr.ensimag.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import fr.ensimag.dao.*;
import fr.ensimag.model.*;

@WebServlet(name = "Controller", urlPatterns = {"/controller"})
public class Controller extends HttpServlet {

    private static final long serialVersionUID = -6370806142849782550L;

    @Resource(name = "jdbc/stories")
    private DataSource ds;

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        req.setCharacterEncoding("UTF-8");
        AppDAO dao = new AppDAO(ds);
        String action = req.getParameter("action");
        try{
            if(action == null){
                showHomeScreen(req, resp);
                return;
            }
            switch(action){
                case "login":
                    showLoginScreen(req, resp);
                    break;
                case "showStories":
                    showStoriesEditMode(req, resp, dao);
                    break;
                case "editStory":
                    showStoryParagraphs(req, resp, dao);
                    break;
                case "editFirstParagraph":
                    showEditFirstParagraph(req, resp, dao);
                    break;
                case "writeParagraph":
                    showWriteParagraphScreen(req, resp, dao);
                    break;
                case "readModify":
                     showMidEditScreen(req, resp, dao);
                     break;
                case "editParagraph":
                    showModifyText(req, resp, dao);
                    break;
                // case "invites":
                //     showInvites(req, resp, dao);
                //     break;
                case "add":
                    ShowAddStoryScreen(req ,resp, dao);
                    break;
                case "publish":
                    showPublishScreen(req, resp, dao);
                    break;
                    case "unpublish":
                    showUnpublishScreen(req, resp, dao);
                    break;
                case "reading":
                    showStoriesList(req, resp, dao);
                    break;
                case "readStory":
                    showStoryReadMode(req, resp, dao);
                    break;
                // case "getStory":
                //     showStory(req, resp, dao);
                //     break;
                case "addChoices":
                    showAddChoicesScreen(req, resp, dao);
                    break;
                case "getNode":
                    break;
                case "unlock":
                    unlockParagraph(req, resp, dao);
                    break;
                case "showHistory":
                    showHistory(req, resp, dao);
                    break;
                case "history":
                    showStoriesHistory(req, resp, dao);
                    break;
                case "unlockS":
                    HttpSession session = req.getSession();
                    int userId = (int) session.getAttribute("userId");
                    int storyId = Integer.valueOf(req.getParameter("storyId"));
                    dao.unlockStory(userId, storyId);
                    req.getRequestDispatcher("/WEB-INF/nochange.jsp").forward(req, resp); 
                    break;
                case "logout":
                    doLogout(req, resp);
                    break;
                case "deleteParagraph":
                    deleteParagraph(req, resp, dao);
                    break;
                case "unlockUser":
                    unlockUser(req, resp, dao);
                    break;
                default:
                    showHomeScreen(req, resp);
                    break;
            }
        }catch(DAOException e){
            databaseError(req, resp, e);
        }
    }





    private void showStoriesHistory(HttpServletRequest req, HttpServletResponse resp, AppDAO dao) throws ServletException, IOException {
        HttpSession session = req.getSession();
        int userId = (int) session.getAttribute("userId");
        List<Story> stories = dao.getStoriesForHistory(userId);
        req.setAttribute("stories", stories);
        req.getRequestDispatcher("/WEB-INF/showStoriesHistory.jsp").forward(req, resp);

    }

    private void showHistory(HttpServletRequest req, HttpServletResponse resp, AppDAO dao) throws ServletException, IOException {
        HttpSession session = req.getSession();
        int userId = (int) session.getAttribute("userId");
        int storyId = Integer.valueOf(req.getParameter("storyId"));
        List<Choice> choices = dao.getChoicesForHistory(storyId, userId);
        req.setAttribute("title", dao.getStoryById(storyId).getTitle());
        req.setAttribute("choices", choices);
        req.getRequestDispatcher("/WEB-INF/viewHistory.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String action = req.getParameter("action");
        if(action==null){
            invalidParameters(req, resp);
            return;
        }
        AppDAO dao = new AppDAO(ds);
        try{
            switch(action){
                case "doLogin":
                    doLogin(req, resp, dao);
                    break;
                case "doPushHistory":
                    doPushHistory(req, resp, dao);
                    break;
                case "doAdd":
                    doAddStory(req, resp, dao);
                    break;
                case "doModifyText":
                    doModifyText(req, resp, dao);
                    break;
                case "doModifyStory":
                    doModifyStory(req, resp, dao);
                    break;
                case "doAddParagraph":
                    doAddParagraph(req, resp, dao);
                    break;
                case "doPublish":
                    doPublish(req, resp, dao);
                    break;
                case "doUnpublish":
                    doUnpublish(req, resp, dao);
                    break;
                case "doAddChoices":
                    doAddChoices(req, resp, dao);
                    break;
                default:
                    invalidParameters(req, resp);
                    return;
            }
        }catch(DAOException e){
            databaseError(req, resp, e);
        }
    }



    private void doPushHistory(HttpServletRequest req, HttpServletResponse resp, AppDAO dao) throws ServletException, IOException {
        HttpSession session = req.getSession();
        int userId = (int) session.getAttribute("userId");
        int storyId = Integer.valueOf(req.getParameter("id"));
        List<Integer> history = new ArrayList<Integer>();
        int count = 0;
        Map<String, String[]> parameterMap = req.getParameterMap();
        while(parameterMap.containsKey("p"+count)){
            history.add(Integer.valueOf(req.getParameter("p"+count)));
            count++;
        }
        System.out.println(history);
        dao.pushHistory(history, storyId, userId);
        req.getRequestDispatcher("/WEB-INF/dashBoard.jsp").forward(req, resp);
    }

    private void deleteParagraph(HttpServletRequest req, HttpServletResponse resp, AppDAO dao)
            throws ServletException, IOException {
        checkLoggedIn(req, resp);
        int storyId = Integer.valueOf(req.getParameter("storyId"));
        int paragraphId = Integer.valueOf(req.getParameter("paragraphId"));
        int choiceId = Integer.valueOf(req.getParameter("choiceId"));
        //checking for access rights
        if(req.getParameter("authorId") == null){
            req.getRequestDispatcher("/WEB-INF/accessError.jsp").forward(req, resp);
        }
        int aut = Integer.valueOf(req.getParameter("authorId"));
        HttpSession session = req.getSession();
        int userId = (int) session.getAttribute("userId");
        if(aut != userId){
            req.getRequestDispatcher("/WEB-INF/accessError.jsp").forward(req, resp);
        }
        //can delete paragraph
        dao.deleteParagraph(storyId, paragraphId, choiceId);
        req.getRequestDispatcher("/WEB-INF/success.jsp").forward(req, resp);
    }

    private void showHomeScreen(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        String isUserLoggedIn = (String) session.getAttribute("isUserLoggedIn");
        if(isUserLoggedIn == null){
            request.getRequestDispatcher("/WEB-INF/homeScreen.jsp").forward(request, response);
        }else{
            //if isUserLoggedIn is true, all other info are part of the session
            request.getRequestDispatcher("/WEB-INF/dashBoard.jsp").forward(request, response);
        }
    }

    private void showLoginScreen(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/loginScreen.jsp").forward(request, response);
    }
    void doLogin(HttpServletRequest req, HttpServletResponse resp, AppDAO dao) throws ServletException, IOException {
        int userId;
        // here start a session
        String userName = req.getParameter("id");
        String passWord = req.getParameter("pwd");
        User usr = dao.doLogin(userName, passWord);
        userId = usr.getId();
        if(userId == -1){
            req.setAttribute("loginError", "Incorrect username/password");
            showLoginScreen(req, resp);
            return;
        }

        HttpSession session = req.getSession();
        session.setAttribute("isUserLoggedIn", "true");
        session.setAttribute("userId", userId);

        req.getRequestDispatcher("/WEB-INF/dashBoard.jsp").forward(req, resp);
    }
    
    private void showPublishScreen(HttpServletRequest req, HttpServletResponse resp, AppDAO dao) throws ServletException, IOException {
        checkLoggedIn(req, resp);                
        HttpSession session = req.getSession();
        int id = (int) session.getAttribute("userId");
        List<Story> publishables = dao.getUserPublishables(id);
        req.setAttribute("stories", publishables);
        req.getRequestDispatcher("/WEB-INF/publish.jsp").forward(req, resp); 
        //add another case scenario where user has no stories 
    }

    private void showUnpublishScreen(HttpServletRequest req, HttpServletResponse resp, AppDAO dao) throws ServletException, IOException {
        checkLoggedIn(req, resp);                
        HttpSession session = req.getSession();
        int id = (int) session.getAttribute("userId");
        List<Story> unpublishables = dao.getUserUnpublishables(id);
        req.setAttribute("storiesU", unpublishables);
        req.getRequestDispatcher("/WEB-INF/unpublish.jsp").forward(req, resp); 
        //add another case scenario where user has no stories 
    }



    private void doPublish(HttpServletRequest req, HttpServletResponse resp, AppDAO dao)
            throws ServletException, IOException {
        checkLoggedIn(req, resp);                
        HttpSession session = req.getSession();
        int userId = (int) session.getAttribute("userId");
        int storyId = Integer.valueOf(req.getParameter("selected"));
        dao.publishStory(userId, storyId);
        req.getRequestDispatcher("/WEB-INF/success.jsp").forward(req, resp);
        
    }

    private void doUnpublish(HttpServletRequest req, HttpServletResponse resp, AppDAO dao)
    throws ServletException, IOException {
        checkLoggedIn(req, resp);        
        HttpSession session = req.getSession();
        int userId = (int) session.getAttribute("userId");
        int storyId = Integer.valueOf(req.getParameter("selected"));
        dao.unpublishStory(userId, storyId);
        req.getRequestDispatcher("/WEB-INF/success.jsp").forward(req, resp);

}

    private void showModifyText(HttpServletRequest req, HttpServletResponse resp, AppDAO dao)
            throws ServletException, IOException {
        checkLoggedIn(req, resp);        
        int storyId = Integer.valueOf(req.getParameter("storyId"));
        int paragraphId = Integer.valueOf(req.getParameter("paragraphId"));
        int choiceId = Integer.valueOf(req.getParameter("choiceId"));
        String choiceTitle = req.getParameter("choiceTitle");
        req.setAttribute("storyId", storyId);
        req.setAttribute("paragraphId", paragraphId);
        req.setAttribute("choiceTitle", choiceTitle);
        req.setAttribute("choiceId", choiceId);
        //checking for access rights
        if(req.getParameter("authorId") == null){
            req.getRequestDispatcher("/WEB-INF/accessError.jsp").forward(req, resp);
        }
        int aut = Integer.valueOf(req.getParameter("authorId"));
        HttpSession session = req.getSession();
        int userId = (int) session.getAttribute("userId");
        if(aut != userId){
            req.getRequestDispatcher("/WEB-INF/accessError.jsp").forward(req, resp);
        }
        dao.lockChoice(storyId, choiceId);
        req.getRequestDispatcher("/WEB-INF/modifyParagraph.jsp").forward(req, resp);
        
    }


    private void ShowAddStoryScreen(HttpServletRequest req, HttpServletResponse resp, AppDAO dao) throws ServletException, IOException {
        checkLoggedIn(req, resp);  
        HttpSession session = req.getSession();
        int userId = (int) session.getAttribute("userId");
        int locking = dao.checkIfLocking(userId);
        if(locking == 1){
            req.getRequestDispatcher("/WEB-INF/noAddError.jsp").forward(req, resp);
        }
        dao.lockUser(userId);
        req.getRequestDispatcher("/WEB-INF/addStory.jsp").forward(req, resp);
    }

    /****
     * shows adding choices screen -accessible to invitees and main author
     * @param req
     * @param resp
     * @param dao
     * @throws ServletException
     * @throws IOException
     */
    private void showAddChoicesScreen(HttpServletRequest req, HttpServletResponse resp, AppDAO dao)
            throws ServletException, IOException {
        checkLoggedIn(req, resp);                
        int storyId = Integer.valueOf(req.getParameter("storyId"));
        int paragraphId = Integer.valueOf(req.getParameter("paragraphId"));
        int choiceId = Integer.valueOf(req.getParameter("choiceId"));
        String choiceTitle = req.getParameter("choiceTitle");
        //checking for access rights
        List<Integer> invitees = dao.getInvitees(storyId);
        HttpSession session = req.getSession();
        int userId = (int) session.getAttribute("userId");       
        int open = dao.isOpen(storyId);
        if(open == 0){
            boolean canAdd = false;
            for(Integer inv : invitees){
                if(inv == userId){
                    canAdd = true;
                }
            }  
            if(!canAdd){
                req.getRequestDispatcher("/WEB-INF/accessError.jsp").forward(req, resp);
            } 
        }
        //can add choices -> moving to addChoices page   
        req.setAttribute("storyId", storyId);
        req.setAttribute("paragraphId", paragraphId);
        req.setAttribute("choiceTitle", choiceTitle);
        req.setAttribute("choiceId", choiceId);
        if(choiceId != 0)  
            dao.lockChoice(storyId, choiceId);
        List<Choice> paragraphsCh = dao.getParagraphsAsChoices(storyId, -1);
        req.setAttribute("listParagraphs", paragraphsCh);
        //retrieve paragraphs that can be sequels
        req.getRequestDispatcher("/WEB-INF/addChoices.jsp").forward(req, resp);
        
    }

    private void doAddChoices(HttpServletRequest req, HttpServletResponse resp, AppDAO dao)
            throws ServletException, IOException {
        checkLoggedIn(req, resp);                
        String[] choices = req.getParameterValues("linkurl[]");
        int storyId = Integer.valueOf(req.getParameter("storyId"));
        int paragraphId = Integer.valueOf(req.getParameter("paragraphId"));
        //updating choice corresponding to this paragraph
        //adding choices corresponding to the new paragraph
        int choiceId = dao.getLengthChoices(storyId, paragraphId)+1;
        System.out.println("length is : "+choiceId);
        for (String choice : choices) {
            if(choice != null && !"".equals(choice)){
                System.out.println("j'ajoute : "+storyId + " "+paragraphId+ " "+choice +" "+choiceId);
                dao.addChoiceNoLead(storyId, paragraphId, choice, choiceId);
                choiceId++;
            }
        }
        int i = 0;
        while(req.getParameter("dropdown"+i) != null && !req.getParameter("dropdown"+i).equals("NADA")) {
            System.out.println("print it : "+req.getParameter("dropdown"+i));
            dao.addExistingChoice(storyId, paragraphId, Integer.valueOf(req.getParameter("dropdown"+i)), choiceId);
            choiceId++;
            i++;
        }
        if(Integer.valueOf(req.getParameter("choiceId")) != 0)
            dao.unlockChoice(storyId, Integer.valueOf(req.getParameter("choiceId")));
        req.getRequestDispatcher("WEB-INF/success.jsp").forward(req, resp);
    }
    
    private void doModifyText(HttpServletRequest req, HttpServletResponse resp, AppDAO dao)
            throws ServletException, IOException {
        checkLoggedIn(req, resp);                
        int storyId = Integer.valueOf(req.getParameter("storyId"));
        int paragraphId = Integer.valueOf(req.getParameter("paragraphId"));
        int choixId = Integer.valueOf(req.getParameter("choiceId"));
        HttpSession session = req.getSession();
        int userId = (int) session.getAttribute("userId");
        int destinationId = -1;
        //1st paragraph things
        String content = req.getParameter("content");
        dao.modifyText(content, storyId, paragraphId, userId);
        //unlocking choice
        dao.unlockChoice(storyId, choixId);
        //successful operation
        req.getRequestDispatcher("WEB-INF/success.jsp").forward(req, resp);

        
    }

    private void doModifyStory(HttpServletRequest req, HttpServletResponse resp, AppDAO dao)
    throws ServletException, IOException {
        checkLoggedIn(req, resp);                
        int storyId = Integer.valueOf(req.getParameter("storyId"));
        int paragraphId = Integer.valueOf(req.getParameter("paragraphId"));
        HttpSession session = req.getSession();
        int userId = (int) session.getAttribute("userId");
        //1st paragraph things
        String content = req.getParameter("content");
        dao.modifyText(content, storyId, paragraphId, userId);
        dao.unlockStory(userId, storyId);
        //successful operation
        req.getRequestDispatcher("WEB-INF/success.jsp").forward(req, resp);
    }


    private void doAddStory(HttpServletRequest req, HttpServletResponse resp, AppDAO dao)throws ServletException, IOException {
        checkLoggedIn(req, resp);                        
        //story things
        String title = req.getParameter("title");
        boolean isOpen = "yes".equals(req.getParameter("public"));
        boolean isPublic = false;
        HttpSession session = req.getSession();
        int userId = (int) session.getAttribute("userId");
        int storyId = dao.saveStory(userId, isOpen, isPublic, title);

        //1st paragraph things
        String content = req.getParameter("content");
        boolean isConclusive = "yes".equals(req.getParameter("conc"));
        dao.saveParagraphFirst(userId, storyId, content, isConclusive);
        //choice things
        String[] choices = req.getParameterValues("linkurl[]");
        int choiceId=1;
        for (String choice : choices) {
            if(choice != null && !"".equals(choice)){
                dao.addChoiceNoLead(storyId, 1, choice, choiceId);
                choiceId++;
            }
        }
        //invite things
        String[] invitees = req.getParameterValues("linkurlP[]");
        for (String invitee : invitees) {
            if(invitee != null && !"".equals(invitee)){
                dao.addInviteByUname(invitee, storyId);
            }
        }
        dao.unlockUser(userId);
        req.getRequestDispatcher("WEB-INF/success.jsp").forward(req, resp);
    }


    private void doAddParagraph(HttpServletRequest req, HttpServletResponse resp, AppDAO dao)
            throws ServletException, IOException {
        checkLoggedIn(req, resp);                        
        String title = req.getParameter("currentChoice");
        int storyId = Integer.valueOf(req.getParameter("storyId"));
        int sourceId = Integer.valueOf(req.getParameter("sourceId"));
        int choixId = Integer.valueOf(req.getParameter("choiceId"));
        HttpSession session = req.getSession();
        int userId = (int) session.getAttribute("userId");
        int destinationId = -1;
        //1st paragraph things
        String content = req.getParameter("content");
        int noCondition = Integer.valueOf(req.getParameter("noConditions")); //is 1 if there were no paragraphs to set constraints
        boolean isConclusive = "yes".equals(req.getParameter("conc"));
        //a paragraph if not conclusive and conditions actually exist must necessarily have a condition
        if(noCondition == 0 && (!isConclusive && req.getParameter("dropdown0").equals("NADA") && req.getParameter("dropdow0").equals("NADA"))){
            dao.unlockChoice(storyId, choixId);
            dao.unlockUser(userId);
            req.getRequestDispatcher("/WEB-INF/conditionsError.jsp").forward(req, resp);
            return;
        }
        //one condition at least exists - all clear
        destinationId = dao.saveParagraph(userId, storyId, content, isConclusive);
        //choice things
        String[] choices = req.getParameterValues("linkurl[]");
        //updating choice corresponding to this paragraph
        dao.updateChoice(storyId, sourceId, destinationId, choixId);
        //adding choices corresponding to the new paragraph
        int choiceId=1;
        for (String choice : choices) {
            if(choice != null && !"".equals(choice)){
                dao.addChoiceNoLead(storyId, destinationId, choice, choiceId);
                choiceId++;
            }
        }
        int i = 0;
        //we shouldnt have conditions for conclusions
        if(!isConclusive){
            while(req.getParameter("dropdown"+i) != null && !req.getParameter("dropdown"+i).equals("NADA")) {
                dao.addConditionVisited(storyId, destinationId, Integer.valueOf(req.getParameter("dropdown"+i)));
                choiceId++;
                i++;
            }
            i = 0;
            while(req.getParameter("dropdow"+i) != null && !req.getParameter("dropdow"+i).equals("NADA")) {
                dao.addConditionUnvisited(storyId, destinationId, Integer.valueOf(req.getParameter("dropdow"+i)));
                choiceId++;
                i++;
            }
        }
        //unlocking choice
        choiceId = Integer.valueOf(req.getParameter("choiceId"));
        dao.unlockChoice(storyId, choiceId);
        dao.unlockUser(userId);
        //successful operation
        req.getRequestDispatcher("WEB-INF/success.jsp").forward(req, resp);

    }


    void doLogout(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        session.invalidate();
        showHomeScreen(req, resp);
    }
    
    void showStoriesEditMode(HttpServletRequest req, HttpServletResponse resp, AppDAO dao)throws ServletException, IOException {
        checkLoggedIn(req, resp);                        
        HttpSession session = req.getSession();
        int id = (int) session.getAttribute("userId");
        List<Story> stories = dao.getUserStories(id);
        req.setAttribute("stories", stories);
        req.getRequestDispatcher("/WEB-INF/storiesEditMode.jsp").forward(req, resp);
    }



    /****
     * shows editing screen : accessible to invitees and main author
     *
     */
    void showStoryParagraphs(HttpServletRequest req, HttpServletResponse resp, AppDAO dao)throws ServletException, IOException {
        checkLoggedIn(req, resp);                        
        int storyId = Integer.valueOf(req.getParameter("storyId"));
        List<Integer> invitees = dao.getInvitees(storyId);        
        HttpSession session = req.getSession();
        int userId = (int) session.getAttribute("userId");   
        String open = (String) (req.getParameter("isOpen"));    
        if(open.equals("false")){   
            boolean canAdd = false;
            for(Integer inv : invitees){
                if(inv == userId){
                    canAdd = true;
                }
            }  
            if(!canAdd){
                req.getRequestDispatcher("/WEB-INF/accessError.jsp").forward(req, resp);
            }  
        }
        int paragraphId = Integer.valueOf(req.getParameter("paragraphId"));
        List<Choice> choices = dao.getChildren(storyId, paragraphId);
        Paragraph source = dao.getParagraphById(storyId, paragraphId);
        Story story = dao.getStoryById(storyId);
        int aut = Integer.valueOf(req.getParameter("authorId"));
        req.setAttribute("authorId", aut);
        req.setAttribute("source", source);
        req.setAttribute("choices", choices);
        req.setAttribute("story", story);
        req.setAttribute("invitees", invitees);
        int dt = dao.canBeDeleted(1, storyId);
        req.setAttribute("canBeDeleted", dt);
        int locked = dao.checkIfLocked(storyId);
        req.setAttribute("locked", locked);
        int op = open.equals("true")? 1:0;
        req.setAttribute("isOpen", op);
        req.getRequestDispatcher("/WEB-INF/paragraphsEditMode.jsp").forward(req, resp);
    }

    private void showEditFirstParagraph(HttpServletRequest req, HttpServletResponse resp, AppDAO dao)
            throws ServletException, IOException {
        checkLoggedIn(req, resp);   
        int storyId = Integer.valueOf(req.getParameter("storyId"));
        int paragraphId = Integer.valueOf(req.getParameter("paragraphId"));
        HttpSession session = req.getSession();
        int userId = (int) session.getAttribute("userId");
        req.setAttribute("storyId", storyId);
        req.setAttribute("paragraphId", paragraphId);
        //checking for access rights
        if(req.getParameter("authorId") == null){
            req.getRequestDispatcher("/WEB-INF/accessError.jsp").forward(req, resp);
        }
        int aut = Integer.valueOf(req.getParameter("authorId"));
        if(aut != userId){
            req.getRequestDispatcher("/WEB-INF/accessError.jsp").forward(req, resp);
        }
        dao.lockStory(userId, storyId); 
        req.getRequestDispatcher("/WEB-INF/modifyFirstParagraph.jsp").forward(req, resp);
        
    }

    /****
     * shows editing screen : accessible to invitees and main author
     *
     */
    private void showMidEditScreen(HttpServletRequest req, HttpServletResponse resp, AppDAO dao) throws ServletException, IOException {
        checkLoggedIn(req, resp);                        
        int storyId = Integer.valueOf(req.getParameter("storyId"));
        int paragraphId = Integer.valueOf(req.getParameter("paragraphId"));
        List<Choice> choices = dao.getChildren(storyId, paragraphId);
        Paragraph source = dao.getParagraphById(storyId, paragraphId);
        int locked = Integer.valueOf(req.getParameter("locked"));
        String choiceTitle = req.getParameter("choiceTitle");
        int choiceId = Integer.valueOf(req.getParameter("choice"));
        List<Integer> invitees = dao.getInvitees(storyId);
        HttpSession session = req.getSession();
        int userId = (int) session.getAttribute("userId");  
        int open = dao.isOpen(storyId);
        if(open == 0){
            boolean canAdd = false;
            for(Integer inv : invitees){
                if(inv == userId){
                    canAdd = true;
                }
            }  
            if(!canAdd){
                req.getRequestDispatcher("/WEB-INF/accessError.jsp").forward(req, resp);
            }  
        }
        req.setAttribute("invitees", invitees);
        req.setAttribute("choiceId", choiceId);
        req.setAttribute("source", source);
        req.setAttribute("choices", choices);
        req.setAttribute("storyId", storyId);
        req.setAttribute("locked", locked);
        req.setAttribute("choiceTitle", choiceTitle);
        req.setAttribute("isOpen", open);
        int dt = dao.canBeDeleted(paragraphId, storyId);
        req.setAttribute("canBeDeleted", dt);
        req.getRequestDispatcher("/WEB-INF/midParagraphsEditMode.jsp").forward(req, resp);
    }


    /****
     * shows adding paragraph screen : only accessible to invitees
     * @param req
     * @param resp
     * @param dao
     * @throws ServletException
     * @throws IOException
     */
    void showWriteParagraphScreen(HttpServletRequest req, HttpServletResponse resp, AppDAO dao) throws ServletException, IOException {
        checkLoggedIn(req, resp);   
        HttpSession session = req.getSession();
        int userId = (int) session.getAttribute("userId"); 
        int locking = dao.checkIfLocking(userId);
        if(locking == 1){
            req.getRequestDispatcher("/WEB-INF/noAddError.jsp").forward(req, resp);
        }               
        int storyId = Integer.valueOf(req.getParameter("storyId"));
        int sourceId = Integer.valueOf(req.getParameter("source"));
        int choiceId = Integer.valueOf(req.getParameter("choice"));
        req.setAttribute("storyId", storyId );
        req.setAttribute("choiceId", choiceId);
        req.setAttribute("sourceId", sourceId);        
        req.setAttribute("currentChoice", req.getParameter("choiceTitle"));
        List<Integer> invitees = dao.getInvitees(storyId);    
        int open = dao.isOpen(storyId);
        if(open == 0){
            boolean canAdd = false;
            for(Integer inv : invitees){
                if(inv == userId){
                    canAdd = true;
                }
            }  
            if(!canAdd){
                req.getRequestDispatcher("/WEB-INF/accessError.jsp").forward(req, resp);
            } 
        } 
        //lock the choice and the user
        dao.lockUser(userId);
        dao.lockChoice(storyId, choiceId);
        List<Choice> paragraphsCh = dao.getConditions(storyId);
        req.setAttribute("listParagraphs", paragraphsCh);
        int noConds = paragraphsCh.size() == 0? 1:0;
        System.out.println("conditions "+noConds);
        req.setAttribute("noConditions", noConds);
        //adding paragraph page
        req.getRequestDispatcher("/WEB-INF/addParagraph.jsp").forward(req, resp);
    }

    void showStoriesList(HttpServletRequest req, HttpServletResponse resp, AppDAO dao)throws ServletException, IOException{
        HttpSession session = req.getSession();
        int id;
        String isUserLoggedIn = (String) session.getAttribute("isUserLoggedIn");
        if(isUserLoggedIn != null){
            id = (int) session.getAttribute("userId");
        }    
        else{
            id = 0;
        }
        StoriesContainer container = dao.getUsReadingContainer(id);
        req.setAttribute("openStories", container.getOpenStories());
        req.setAttribute("loggedIn", id);
        req.getRequestDispatcher("/WEB-INF/storiesReadingMode.jsp").forward(req, resp);
    }

    void showStoryReadMode(HttpServletRequest req, HttpServletResponse resp, AppDAO dao)throws ServletException, IOException{
        int storyId = Integer.valueOf(req.getParameter("storyId"));
        int start = Integer.valueOf(req.getParameter("start"));
        Map<Integer, ParagraphContainer> container = dao.getStoryParagraphsRead(storyId);
        String title = dao.getStoryById(storyId).getTitle();
        req.setAttribute("container", container);
        req.setAttribute("title", title);
        req.setAttribute("storyId", storyId);
        req.setAttribute("start", start);
        req.getRequestDispatcher("/WEB-INF/readStory.jsp").forward(req, resp);
    }

    void showStoryReadModeFromHistory(HttpServletRequest req, HttpServletResponse resp, AppDAO dao)throws ServletException, IOException{

    }

    void invalidParameters(HttpServletRequest req, HttpServletResponse resp) {

    }

    void databaseError(HttpServletRequest req, HttpServletResponse resp, DAOException e) throws ServletException, IOException {
        e.printStackTrace();
        req.setAttribute("errorMessage", e.getMessage());
        req.getRequestDispatcher("/WEB-INF/dbError.jsp").forward(req, resp);
    }

    private void unlockUser(HttpServletRequest req, HttpServletResponse resp, AppDAO dao)
    throws ServletException, IOException {
        HttpSession session = req.getSession();
        int userId = (int) session.getAttribute("userId"); 
        dao.unlockUser(userId);
        req.getRequestDispatcher("/WEB-INF/nochange.jsp").forward(req, resp); 

        }

    private void checkLoggedIn(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException {
        HttpSession session = req.getSession();
        String isUserLoggedIn = (String) session.getAttribute("isUserLoggedIn");
        if(isUserLoggedIn == null){
            req.getRequestDispatcher("/WEB-INF/accessError.jsp").forward(req, resp); 
        }
    }



    private void unlockParagraph(HttpServletRequest req, HttpServletResponse resp, AppDAO dao)
        throws ServletException, IOException {
        int storyId = Integer.valueOf(req.getParameter("storyId"));
        int choixId = Integer.valueOf(req.getParameter("choiceId"));
        dao.unlockChoice(storyId, choixId);   
        HttpSession session = req.getSession();
        int userId = (int) session.getAttribute("userId"); 
        dao.unlockUser(userId);
        req.getRequestDispatcher("/WEB-INF/nochange.jsp").forward(req, resp); 

    }

}