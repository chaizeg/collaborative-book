package fr.ensimag.dao;

import java.awt.Container;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLType;
import java.sql.Types;
import java.util.*;

import javax.sql.DataSource;

import fr.ensimag.model.*;
import oracle.net.aso.s;

public class AppDAO {
    private final DataSource dataSource;

    public AppDAO(DataSource ds){
        dataSource = ds;
    }
    public Connection getConn() throws SQLException{
        return dataSource.getConnection();
    }

    public User doLogin(String username, String password){
        try{
            System.out.println(username + " : " + password );
            Connection conn = getConn();
            String loginQuery = "SELECT * FROM users WHERE uname=? AND pwd=?";
            PreparedStatement st = conn.prepareStatement(loginQuery);
            st.setString(1, username);
            st.setString(2, password);
            ResultSet rs = st.executeQuery();
            if(!rs.next()){
                System.out.println("login unsuccessful!");
                return new User(-1, "");
            }
            int id = rs.getInt(1);
            System.out.println("login successful!");
            conn.close();
            return new User(id, username);
        }catch(SQLException e){
            e.printStackTrace();
            throw new DAOException("Failed login attempt " + e.getMessage(), e);
        }
    }

    public List<Story> getUserPublishables(int userId){
        try{
            List<Story> allStories= new ArrayList<Story>();
            Connection conn = getConn();
            String getQuery = "SELECT * FROM "
                             +"stories s "
                             +"WHERE author=? AND isPublic=0 AND EXISTS("
                             +"SELECT NULL FROM paragraphs p "
                             +"WHERE p.storyId = s.id AND p.isConclusive=?)";
            PreparedStatement st = conn.prepareStatement(getQuery);
            st.setInt(1, userId);
            st.setInt(2, 1);
            ResultSet rs = st.executeQuery();
            while(rs.next()){
                int id = rs.getInt("id");
                String title = rs.getString("title");
                boolean isPublic = 1 == rs.getInt("isPublic");
                boolean isOpen = 1 == rs.getInt("isOpen");
                allStories.add(new Story(id, userId, isOpen, isPublic, title));
            }
            conn.close();
            return allStories;
        }catch(SQLException e){
            e.printStackTrace();
            throw new DAOException("Failed to parse publishable stories " + e.getMessage(), e);
        }
    }

    public List<Story> getUserUnpublishables(int userId) {
        try{
            List<Story> allStories= new ArrayList<Story>();
            Connection conn = getConn();
            String getQuery = "SELECT * FROM "
                             +"stories s "
                             +"WHERE author=? and isPublic=1 ";
            PreparedStatement st = conn.prepareStatement(getQuery);
            st.setInt(1, userId);
            ResultSet rs = st.executeQuery();
            while(rs.next()){
                int id = rs.getInt("id");
                String title = rs.getString("title");
                boolean isPublic = 1 == rs.getInt("isPublic");
                boolean isOpen = 1 == rs.getInt("isOpen");
                allStories.add(new Story(id, userId, isOpen, isPublic, title));
            }
            conn.close();
            return allStories;
        }catch(SQLException e){
            e.printStackTrace();
            throw new DAOException("Failed to parse publishable stories " + e.getMessage(), e);
        }
	}
    public void publishStory(int userId, int storyId){
        try (
	     Connection conn = getConn();
	     PreparedStatement st = conn.prepareStatement("UPDATE stories SET isPublic=? WHERE id=?");
	     ) {
            st.setInt(1, 1);
            st.setInt(2, storyId);
            st.executeUpdate();
            
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DAOException("Failed to publish story " + e.getMessage(), e);
        }
    }

    public void unpublishStory(int userId, int storyId) {
        try (
            Connection conn = getConn();
            PreparedStatement st = conn.prepareStatement("UPDATE stories SET isPublic=0 WHERE id=? and author=?");
            ) {
               st.setInt(1, storyId);
               st.setInt(2, userId);
               st.executeUpdate();
               System.out.println("unpublished it");
               conn.close();
           } catch (SQLException e) {
               e.printStackTrace();
               throw new DAOException("Failed to unpublish story " + e.getMessage(), e);
           }
	}
	public int saveStory(int userId, boolean isOpen, boolean isPublic, String title) {
        try {
            int storyId = -1;
            Connection conn = getConn();
            //phase 1 : get sequence number
            String getSequenceQuery = "SELECT story_seq.nextval FROM dual";
            PreparedStatement seqStatement = conn.prepareStatement(getSequenceQuery);
            ResultSet rs = seqStatement.executeQuery();
            if(rs.next()){
                storyId = rs.getInt(1);
            }else{
                throw new DAOException("Failed to update story sequence");
            }

            PreparedStatement statement = conn.prepareStatement("INSERT INTO stories VALUES (?, ?, ?, ?, ?, 0)");
            statement.setInt(1, storyId);
            statement.setInt(2, userId);
            statement.setInt(3, isOpen? 1 : 0);
            statement.setInt(4, isPublic? 1 : 0);
            statement.setString(5, title);
            statement.executeUpdate();
            
            conn.close();
            return storyId;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DAOException("Failed to save story " + e.getMessage(), e);
        }
    }
    
	public void saveParagraphFirst(int userId, int storyId, String content, boolean isConclusive) {
        try {
            Connection conn = getConn();
            PreparedStatement statement;
            statement = conn.prepareStatement("INSERT INTO paragraphs VALUES (?, ?, ?, ?, ?)");
            statement.setInt(1, storyId);
            statement.setInt(2, 1);
            statement.setString(3, content);
            statement.setInt(4, userId);
            statement.setInt(5, isConclusive? 1: 0);
            statement.executeUpdate();
            
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DAOException("Failed to save first paragraph " + e.getMessage(), e);
        }
    }
    
	public int saveParagraph(int userId, int storyId, String content, boolean isConclusive) {
        try {
            Connection conn = getConn();
            int idNewPar = -1;
            PreparedStatement statement = conn.prepareStatement("SELECT max(id) from paragraphs where storyId = ?");
            statement.setInt(1, storyId);
            ResultSet rs = statement.executeQuery();
            rs.next();
            idNewPar = rs.getInt("max(id)")+1;
            statement = conn.prepareStatement("INSERT INTO paragraphs VALUES (?, ?, ?, ?, ?)");
            statement.setInt(1, storyId);
            statement.setInt(2, idNewPar);
            statement.setString(3, content);
            statement.setInt(4, userId);
            statement.setInt(5, isConclusive? 1: 0);
            statement.executeUpdate();
            
            conn.close();
            return idNewPar;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DAOException("Failed to save paragraph " + e.getMessage(), e);
        }
    }
	public void addChoiceNoLead(int storyId, int sourceId, String choice, int choiceId) {
        try {
            Connection conn = getConn();
            PreparedStatement statement = conn.prepareStatement("INSERT INTO choices(storyId, sourceId, choice, choiceId) VALUES (?, ?, ?, ?)");
            statement.setInt(1, storyId);
            statement.setInt(2, sourceId);
            statement.setString(3, choice);
            statement.setInt(4, choiceId);
            statement.executeUpdate();
            
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DAOException("Failed to save choice " + e.getMessage(), e);
        }
    }
    
	public void addInviteByUname(String invitee, int storyId) {
        try {
            int userId = -1;
            Connection conn = getConn();
            PreparedStatement getUserIdStatement = conn.prepareStatement("SELECT id FROM users WHERE uname=?");
            getUserIdStatement.setString(1, invitee);
            ResultSet rs = getUserIdStatement.executeQuery();
            if(rs.next()){
                userId= rs.getInt(1);
            }else{
                throw new DAOException("Failed to get user id from username");
            }
            PreparedStatement statement = conn.prepareStatement("INSERT INTO invites VALUES (?, ?)");
            statement.setInt(1, storyId);
            statement.setInt(2, userId);
            statement.executeUpdate();
            
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DAOException("Failed to save choice " + e.getMessage(), e);
        }
	}
	public List<Story> getUserStories(int userId) {
		try{
            //TO DO:  adding story container
            List<Story> allStories= new ArrayList<Story>();
            Connection conn = getConn();
            //user's stories
            String getQuery = "SELECT * FROM stories s WHERE author=?";
            PreparedStatement st = conn.prepareStatement(getQuery);
            st.setInt(1, userId);
            ResultSet rs = st.executeQuery();
            while(rs.next()){
                int id = rs.getInt("id");
                String title = rs.getString("title");
                boolean isPublic = 1 == rs.getInt("isPublic");
                boolean isOpen = 1 == rs.getInt("isOpen");
                allStories.add(new Story(id, userId, isOpen, isPublic, title));
            }
            //open stories
            getQuery = "SELECT * FROM stories s WHERE author!=? AND isOpen=1";
            st = conn.prepareStatement(getQuery);
            st.setInt(1, userId);
            rs = st.executeQuery();
            while(rs.next()){
                int id = rs.getInt("id");
                String title = rs.getString("title");
                boolean isPublic = 1 == rs.getInt("isPublic");
                boolean isOpen = 1 == rs.getInt("isOpen");
                allStories.add(new Story(id, userId, isOpen, isPublic, title));
            }
            //invited 
            getQuery = "SELECT * FROM stories s, invites i WHERE author!=? AND isOpen=0 AND s.id=i.storyId AND i.userId=?";
            st = conn.prepareStatement(getQuery);
            st.setInt(1, userId);
            st.setInt(2, userId);
            rs = st.executeQuery();
            while(rs.next()){
                int id = rs.getInt("id");
                String title = rs.getString("title");
                boolean isPublic = 1 == rs.getInt("isPublic");
                boolean isOpen = 1 == rs.getInt("isOpen");
                allStories.add(new Story(id, userId, isOpen, isPublic, title));
            }
            conn.close();
            return allStories;
        }catch(SQLException e){
            e.printStackTrace();
            throw new DAOException("Failed to parse stories " + e.getMessage(), e);
        }
    }

    public Paragraph getParagraphById(int storyId, int paragraphId){
        try{
            Connection conn = getConn();
            String getQuery = "SELECT * FROM paragraphs WHERE storyId=? AND id=?";
            PreparedStatement st = conn.prepareStatement(getQuery);
            st.setInt(1, storyId);
            st.setInt(2, paragraphId);
            ResultSet rs = st.executeQuery();
            if(rs.next()){
                String content = rs.getString("paragraphText");
                int authorId = rs.getInt("author");
                boolean isConclusive = rs.getInt("isConclusive") == 1;
                conn.close();
                return new Paragraph(paragraphId, storyId, authorId, content, isConclusive);
            }
            throw new DAOException("Failed to get paragraph from ids");
        }catch(SQLException e){
            e.printStackTrace();
            throw new DAOException("Failed to parse paragraph" + e.getMessage(), e);
        }
    }

	public List<Choice> getChildren(int storyId, int paragraphId) {
		try{
            List<Choice> ret= new ArrayList<Choice>();
            Connection conn = getConn();
            String getQuery = "SELECT * FROM choices WHERE storyId=? AND sourceId=?";
            PreparedStatement st = conn.prepareStatement(getQuery);
            st.setInt(1, storyId);
            st.setInt(2, paragraphId);
            ResultSet rs = st.executeQuery();
            while(rs.next()){
                String title = rs.getString("choice");
                int choiceId = rs.getInt("choiceId");
                int locked = rs.getInt("locked");
                int destinationId = rs.getInt("destinationId");
                if(rs.wasNull()){
                    Choice ch = new Choice(title, storyId, paragraphId, choiceId);
                    ch.setLocked(locked);
                    ret.add(ch);
                }else{
                    Choice ch = new Choice(title, storyId, paragraphId, choiceId, destinationId);
                    ch.setLocked(locked); 
                    ret.add(ch);
                }
            }
            conn.close();
            return ret;
        }catch(SQLException e){
            e.printStackTrace();
            throw new DAOException("Failed to parse paragraphs" + e.getMessage(), e);
        }
	}
	public Story getStoryById(int storyId) {
		try{
            Connection conn = getConn();
            String getQuery = "SELECT * FROM stories WHERE id=?";
            PreparedStatement st = conn.prepareStatement(getQuery);
            st.setInt(1, storyId);
            ResultSet rs = st.executeQuery();
            if(rs.next()){
                String title = rs.getString("title");
                int authorId = rs.getInt("author");
                boolean isPublic = rs.getInt("isPublic") == 1;
                boolean isOpen = rs.getInt("isOpen") == 1;
                conn.close();
                return new Story(storyId, authorId, isOpen, isPublic, title);
            }
            throw new DAOException("Failed to get story from id");
        }catch(SQLException e){
            e.printStackTrace();
            throw new DAOException("Failed to parse story " + e.getMessage(), e);
        }
    }
    

	public void lockChoice(int storyId, int choiceId) {
        try{
            Connection conn = getConn();
            String getQuery = "UPDATE choices SET locked = 1 WHERE storyId = ? AND choiceId = ?";
            PreparedStatement st = conn.prepareStatement(getQuery);
            st.setInt(1, storyId);
            st.setInt(2, choiceId);
            ResultSet rs = st.executeQuery();
            rs.next();
            conn.close();
            
        }catch(SQLException e)
        {
            throw new DAOException("Failed to lock choice "+e.getMessage(),e);
        }
	}
    
    public void lockUser(int userId){
        try{
            Connection conn = getConn();
            String getQuery = "UPDATE users SET locking = 1 WHERE id = ?";
            PreparedStatement st = conn.prepareStatement(getQuery);
            st.setInt(1, userId);
            ResultSet rs = st.executeQuery();
            rs.next();
            conn.close();
            
        }catch(SQLException e)
        {
            throw new DAOException("Failed to lock user "+e.getMessage(),e);
        }
    }


    public void unlockUser(int userId){
        try{
            Connection conn = getConn();
            String getQuery = "UPDATE users SET locking = 0 WHERE id = ?";
            PreparedStatement st = conn.prepareStatement(getQuery);
            st.setInt(1, userId);
            ResultSet rs = st.executeQuery();
            rs.next();
            conn.close();
            
        }catch(SQLException e)
        {
            throw new DAOException("Failed to lock user "+e.getMessage(),e);
        }
    }

    public void unlockChoice(int storyId, int choiceId) {
        try{
            Connection conn = getConn();
            String getQuery = "UPDATE choices SET locked = 0 WHERE storyId = ? AND choiceId = ?";
            PreparedStatement st = conn.prepareStatement(getQuery);
            st.setInt(1, storyId);
            st.setInt(2, choiceId);
            ResultSet rs = st.executeQuery();
            rs.next();
            conn.close();
            
        }catch(SQLException e)
        {
            throw new DAOException("Failed to unlock choice "+e.getMessage(),e);
        }
    }
    
	public void updateChoice(int storyId, int sourceId, int destinationId, int choixId) {
        try{
            Connection conn = getConn();
            String sql = "UPDATE choices SET destinationId=? WHERE storyId=? and sourceId=? and choiceId=?";
            PreparedStatement st = conn.prepareStatement(sql);
            st.setInt(1, destinationId);
            st.setInt(2, storyId);
            st.setInt(3, sourceId);
            st.setInt(4, choixId);
            st.executeUpdate();
            
            conn.close();
        }catch(SQLException e){
            throw new DAOException("Failed to update destination id of current choice "+e.getMessage(), e);
        }
	}
	public void modifyText(String content, int storyId, int paragraphId, int userId) {
        try{
            Connection conn = getConn();
            String sql = "UPDATE paragraphs SET paragraphText =? WHERE storyId=? and id=? and author=?";
            PreparedStatement st = conn.prepareStatement(sql);
            st.setString(1, content);
            st.setInt(2, storyId);
            st.setInt(3, paragraphId);
            st.setInt(4, userId);
            st.executeUpdate();
            
            conn.close();
        }catch(SQLException e){
            throw new DAOException("Failed to modify text of current paragraph "+ e.getMessage(), e);
        }
    
    }
	public int getLengthChoices(int storyId, int paragraphId) {
        try{
            Connection conn = getConn();
            PreparedStatement statement = conn.prepareStatement("SELECT max(choiceId) from choices where storyId = ? and sourceId=?");
            statement.setInt(1, storyId);
            statement.setInt(2, paragraphId);
            ResultSet rs = statement.executeQuery();
            int idNewPar;
            rs.next();
            idNewPar = rs.getInt("max(choiceId)");
            conn.close();
            return idNewPar;
        }catch(SQLException e){
            throw new DAOException(" Failed to get length of choices list "+e.getMessage(), e);
        }
    }


	public int canBeDeleted(int paragraphId, int storyId) {
        try{
            Connection conn = getConn();
            String getQuery = "SELECT * FROM choices WHERE storyId=? AND sourceId=? AND  destinationId != -1";
            PreparedStatement st = conn.prepareStatement(getQuery);
            st.setInt(1, storyId);
            st.setInt(2, paragraphId);
            ResultSet rs = st.executeQuery();
            if(rs.next()){
                conn.close();
                return 0;
            }else{
                conn.close();
                return 1;
            }
        }catch(SQLException e){
            throw new DAOException("Failed to retrieve data from database (if paragraph can be deleted", e);
        }

	}
	public void deleteParagraph(int storyId, int paragraphId, int choiceId) {
        try{
            Connection conn = getConn();
            PreparedStatement st;
            String sql;
            sql = "DELETE from conditions where subjectId = ? and storyId=?";
            st = conn.prepareStatement(sql);
            st.setInt(1, paragraphId);
            st.setInt(2 , storyId);
            st.executeUpdate();
            sql = "DELETE from conditions where objectId = ? and storyId=?";
            st = conn.prepareStatement(sql);
            st.setInt(1, paragraphId);
            st.setInt(2 , storyId);
            st.executeUpdate();
            sql = "DELETE from choices where sourceId = ? and storyId=?";
            st = conn.prepareStatement(sql);
            st.setInt(1, paragraphId);
            st.setInt(2 , storyId);
            st.executeUpdate();
            sql = "DELETE from choices where destinationId = ? and storyId=?";
            st = conn.prepareStatement(sql);
            st.setInt(1, paragraphId);
            st.setInt(2 , storyId);
            st.executeUpdate();
            st = conn.prepareStatement("update choices set destinationId = NULL where storyId = ? and destinationId = ?");
            st.setInt(1, storyId);
            st.setInt(2, paragraphId);
            st.executeUpdate();
            st = conn.prepareStatement("delete from paragraphs where storyId=? and id=?");
            st.setInt(1, storyId);
            st.setInt(2, paragraphId);
            st.executeUpdate();
            if(paragraphId ==1)//deleting story altogether
            {
                sql = "DELETE from invites where storyId=?";
                st = conn.prepareStatement(sql);
                st.setInt(1 , storyId);
                st.executeUpdate();
                sql = "DELETE from stories where id=?";
                st = conn.prepareStatement(sql);
                st.setInt(1 , storyId);
                st.executeUpdate();
            }
            conn.close();

        }catch(SQLException e)
        {
            throw new DAOException("failed to delete paragraph "+e.getMessage(), e);
        }
    }
    

    public List<Choice> getParagraphsAsChoices(int storyId, int paragraphId){
        try{
            Connection conn = getConn();
            PreparedStatement st, stmt;
            ResultSet rs, rsIn;
            List<Choice> choices = new ArrayList<Choice>();
            // st = conn.prepareStatement("SELECT * FROM paragraphs p, conditions c, choices ch where p.storyId = c.storyId and p.id=ch.destinationId and ch.storyId=p.storyId and p.storyId=?");
            st = conn.prepareStatement("SELECT distinct * FROM paragraphs p, choices ch where p.id=ch.destinationId and ch.storyId=p.storyId and p.storyId=? and p.id !=?");
            st.setInt(1, storyId);
            st.setInt(2, paragraphId);
            rs = st.executeQuery();
            int i =0;
            while(rs.next()){
                Choice choix =  new Choice(rs.getString("choice"), rs.getInt("storyId"), rs.getInt("sourceId"), rs.getInt("choiceId"), rs.getInt("destinationId"),rs.getInt("locked"));
                //checking if conditions are verified 
                stmt = conn.prepareStatement("SELECT * from conditions where subjectId=? and storyId=?");
                stmt.setInt(1, choix.getDestinationId());
                stmt.setInt(2, choix.getStoryId());
                rsIn = stmt.executeQuery();
                int visited = 0, objet = 0;
                boolean allClear = true;
                while(rsIn.next() && allClear==true){
                    visited = rsIn.getInt("visited");
                    objet = rsIn.getInt("objectId");
                    if(visited == 0){
                        List<Integer> sources = checkIfVisited(paragraphId, storyId, objet, conn);
                        allClear = ascendVisited(paragraphId, storyId, objet, sources, conn);
                    }
                    else{
                        List<Integer> sources = checkIfVisited(paragraphId, storyId, objet, conn);
                        allClear = !(ascendVisited(paragraphId, storyId, objet, sources, conn));
                    }
                    int cl = allClear? 1:0;
                    System.out.println(" itération nm : "+i+"visited : "+visited +" for : "+objet+ "and result ? "+cl) ;

                }
                if(allClear == true){
                    choices.add(choix);
                    System.out.println(" itération nm : "+i);
                    System.out.println("titre : "+choices.get(i).getTitle());
                    System.out.println("id du par : "+choices.get(i).getDestinationId());
                    i++;
                    
                }

                
            }
            conn.close();
            
            return choices;
        }catch(SQLException e){
            throw new DAOException("Failed to get paragraphs "+e.getMessage());
        }
    }

    /****
     * returns null if objectId was visited
     */
    public List<Integer> checkIfVisited(int idPar, int storyId, int objet, Connection conn) throws SQLException {
        PreparedStatement stmt;
        ResultSet rsIn;
        stmt = conn.prepareStatement("SELECT * from choices where destinationId=? and storyId=?");
        stmt.setInt(1, idPar);
        stmt.setInt(2, storyId);
        rsIn = stmt.executeQuery();
        List<Integer> source = new ArrayList<Integer>();
        while(rsIn.next()){
            int curr = rsIn.getInt("sourceId");
            if(curr == objet){
                stmt.close();
                rsIn.close();
                return null;
            }
            else{
                source.add(curr);
            }
        }
        stmt.close();
        rsIn.close();
        return source;    
    }

    /****
     * recursive method checking if objectid was not visited
     */
    public boolean ascendVisited(int idPar, int storyId, int objet, List<Integer> sourc, Connection conn)
            throws SQLException {
        if(sourc.size() == 0){
            return true;
        }
        else if(sourc == null){
            return false;
        }
        else{
            List<Integer> src = checkIfVisited(idPar, storyId, objet, conn);
            if(src != null && src.size() > 0){
                for(Integer s:src){
                    List<Integer> sources = checkIfVisited(s, storyId, objet, conn);
                    ascendVisited(s, storyId, objet, sources, conn);
                }
            }
            return true;
        }
 
    }

    
	public void addExistingChoice(int storyId, int paragraphId, int destId, int choiceId) {
        try{
            Connection conn = getConn();
            PreparedStatement st = conn.prepareStatement("SELECT choice, locked FROM choices where destinationId=?");
            st.setInt(1, destId);
            ResultSet rs = st.executeQuery();
            String choix = "";
            int verr = 0;
            if(rs.next()){
                choix = rs.getString("choice");
                verr = rs.getInt("locked");
            }
            st = conn.prepareStatement("INSERT INTO choices VALUES (?,?,?,?,?,?)");
            st.setInt(1, storyId);
            st.setInt(2, paragraphId);
            st.setInt(3, destId);
            st.setInt(5, choiceId);
            st.setString(4, choix);
            st.setInt(6, verr);
            st.executeUpdate();
            conn.close();

        }catch(SQLException e){
            throw new DAOException("Failed to add existing choice "+e.getMessage(), e);
        }
    }
    public Map<Integer, ParagraphContainer> getStoryParagraphsRead(int storyId){
        try{
            Map<Integer, ParagraphContainer> ret = new HashMap<Integer, ParagraphContainer>();
            Connection conn = getConn();
            String getParagraphsQuery = "SELECT id, paragraphText FROM paragraphs where storyId=?";
            PreparedStatement st = conn.prepareStatement(getParagraphsQuery);
            st.setInt(1, storyId);
            ResultSet rs = st.executeQuery();
            while(rs.next()){
                ParagraphContainer container = new ParagraphContainer();
                int id = rs.getInt("id");
                String content = rs.getString("paragraphText");
                container.id = id;
                container.content = content;
                container.childrenIds = new HashMap<Integer, String>();
                ret.put(id, container);
            }
            String getChoicesQuery = "SELECT * FROM choices where storyId=? AND destinationId IS NOT NULL";
            st = conn.prepareStatement(getChoicesQuery);
            st.setInt(1, storyId);
            rs = st.executeQuery();
            while(rs.next()){
                int source = rs.getInt("sourceId");
                int dest = rs.getInt("destinationId");
                String choice = rs.getString("choice");
                ret.get(source).childrenIds.put(dest, choice);
            }
            conn.close();

            return ret;
        }catch(SQLException e){
            throw new DAOException("Failed to get story for reading "+e.getMessage(), e);
        }
    }




    public StoriesContainer getUsReadingContainer(int userId){
        try{
            StoriesContainer container = new StoriesContainer();
            Connection conn = getConn();
            String sql;
            ResultSet rsOpen, rs;
            PreparedStatement stmt, st;
            String openStoriesQuery = "SELECT * FROM stories s where s.isPublic=1"
                                          +"AND EXISTS( "
                                          +"SELECT NULL FROM paragraphs p "
                                          +"WHERE p.storyId=s.id AND p.isConclusive=1)";
            st = conn.prepareStatement(openStoriesQuery);
            rs=st.executeQuery();
            while(rs.next()){
                int authorId = rs.getInt("author");
                int storyId = rs.getInt("id");
                String title = rs.getString("title");
                boolean isPublic = rs.getInt("isPublic") == 1;
                boolean isOpen = rs.getInt("isOpen") == 1;
                Story hist = new Story(storyId, authorId, isOpen, isPublic, title);
                sql = "SELECT uname from paragraphs p, users u where u.id=p.author and p.storyId=?";
                stmt = conn.prepareStatement(sql);
                stmt.setInt(1, storyId);
                rsOpen = stmt.executeQuery();
                while(rsOpen.next()){
                    hist.addAuthor(rsOpen.getString("uname"));
                }
                container.appendOpenStory(hist);


            }
            conn.close();

            return container;
        }catch(SQLException e){
            throw new DAOException("Failed to eet stories for reading "+e.getMessage(), e);
        }
    }


    /***
     * returns all people invited to the story + main author
     * @param storyId
     * @return
     */
	public List<Integer> getInvitees(int storyId) {
        try{
            Connection conn = getConn();
            PreparedStatement st;
            ResultSet rs;
            List<Integer> invitees = new ArrayList<Integer>();
            // st = conn.prepareStatement("SELECT * FROM paragraphs p, conditions c, choices ch where p.storyId = c.storyId and p.id=ch.destinationId and ch.storyId=p.storyId and p.storyId=?");
            st = conn.prepareStatement("SELECT userId FROM invites where storyId=?");
            st.setInt(1, storyId);
            rs = st.executeQuery();
            int i =0;
            while(rs.next()){
                invitees.add(rs.getInt("userId"));
            }
            st = conn.prepareStatement("SELECT author from stories where id=?");
            st.setInt(1, storyId);
            rs = st.executeQuery();
            if(rs.next()){
                invitees.add(rs.getInt("author"));
            }
            conn.close();

            return invitees;
        }catch(SQLException e){
            throw new DAOException("Failed to get invitees "+e.getMessage());
        }
	}

    /***
     * returns value of locking field for user
     * @param userId
     * @return
     */
	public int checkIfLocking(int userId) {
        try{
            Connection conn = getConn();
            PreparedStatement st;
            ResultSet rs;
            st = conn.prepareStatement("SELECT locking FROM users where id=?");
            st.setInt(1, userId);
            rs = st.executeQuery();
            int i =0;
            if(rs.next()){
                i = rs.getInt("locking");
            }
            conn.close();

            return i;
        }catch(SQLException e){
            throw new DAOException("Failed to check if user is locking a paragraph "+e.getMessage());
        }
    }
    
    /***
     * returns value of locked field for 1st paragraph of story
     * @param userId
     * @return
     */
	public int checkIfLocked(int id) {
        try{
            Connection conn = getConn();
            PreparedStatement st;
            ResultSet rs;
            st = conn.prepareStatement("SELECT locked FROM stories where id=?");
            st.setInt(1, id);
            rs = st.executeQuery();
            int i =0;
            if(rs.next()){
                i = rs.getInt("locked");
            }
            conn.close();

            return i;
        }catch(SQLException e){
            throw new DAOException("Failed to check if 1st paragraph is locked "+e.getMessage());
        }
	}

    public void lockStory(int userId, int storyId){
        try (
	     Connection conn = getConn();
	     PreparedStatement st = conn.prepareStatement("UPDATE stories SET locked=1 WHERE author=? AND id=?");
	     ) {
            st.setInt(1, userId); 
            st.setInt(2, storyId);
            st.executeUpdate();
            
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DAOException("Failed to publish story " + e.getMessage(), e);
        }
    }

    public void unlockStory(int userId, int storyId){
        try (
	     Connection conn = getConn();
	     PreparedStatement st = conn.prepareStatement("UPDATE stories SET locked=0 WHERE author=? AND id=?");
	     ) {
            st.setInt(1, userId); 
            st.setInt(2, storyId);
            st.executeUpdate();
            
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DAOException("Failed to publish story " + e.getMessage(), e);
        }
    }

    public int isOpen(int storyId){
        try (
	     Connection conn = getConn();
	     PreparedStatement st = conn.prepareStatement("SELECT isOpen FROM stories WHERE id=?");
	     ) {
            st.setInt(1, storyId);
            ResultSet rs = st.executeQuery();
            int op = 0;
            if(rs.next()){
                op = rs.getInt("isOpen");
            }
            conn.close();
            return op;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DAOException("Failed to publish story " + e.getMessage(), e);
        }
    }

	public void pushHistory(List<Integer> history, int storyId, int userId) {
        try {
            Connection conn = getConn();
            PreparedStatement st;
            ResultSet rs;
            String query;
            int previous = -1;
            for (Integer para : history) {
                query = "SELECT history_seq.nextval FROM dual";
                st = conn.prepareStatement(query);
                rs = st.executeQuery();
                if(!rs.next()){
                    //error here
                }
                int id = rs.getInt(1);
                query = "INSERT INTO history (id, previousPar, userId, storyId, paragraphId) VALUES (?, ?, ?, ?, ?)";
                st = conn.prepareStatement(query);
                st.setInt(1, id);
                if(previous == -1){
                    st.setNull(2, Types.INTEGER);
                }else{
                    st.setInt(2, previous);
                }
                st.setInt(3, userId);
                st.setInt(4, storyId);
                st.setInt(5, para);
                st.executeQuery();
                if(previous != -1){
                    query = "UPDATE history SET nextPar=? WHERE paragraphId=? AND userId=?";
                    st = conn.prepareStatement(query);
                    st.setInt(1, para);
                    st.setInt(2, previous);
                    st.setInt(3, userId);
                    st.executeQuery();
                }
                previous = para;
            }
            conn.close();

        }
        catch (SQLException e) {
            e.printStackTrace();
            throw new DAOException("Failed to publish story " + e.getMessage(), e);
        }
	}
	public List<Choice> getConditions(int storyId) {
        try{
            Connection conn = getConn();
            PreparedStatement st = conn.prepareStatement("SELECT * FROM choices where storyId=? and destinationId != -1");
            st.setInt(1, storyId);
            ResultSet rs = st.executeQuery();
            List<Choice> ch = new ArrayList<Choice>();
            while(rs.next()){
                Choice choix =  new Choice(rs.getString("choice"), rs.getInt("storyId"), rs.getInt("sourceId"), rs.getInt("choiceId"), rs.getInt("destinationId"),rs.getInt("locked"));
                ch.add(choix);
            }
            return ch;
        }catch(SQLException e){
            throw new DAOException("Failed to get paragraphs as choices "+e.getMessage());
        }
    }
	public void addConditionVisited(int storyId, int destinationId, int objectId) {
        try{
            Connection conn = getConn();
            PreparedStatement st = conn.prepareStatement("SELECT * FROM conditions where subjectId=? and storyId=? and visited=1 and objectId=?");
            st.setInt(1, destinationId);
            st.setInt(2, storyId);
            st.setInt(3, objectId);
            ResultSet rs = st.executeQuery();
            if(!rs.next()){
                st.close();
                rs.close();
                st = conn.prepareStatement("INSERT INTO conditions VALUES (?,1,?,?)");
                st.setInt(1, storyId);
                st.setInt(2, destinationId);
                st.setInt(3, objectId);
                st.executeUpdate();
            }
        }catch(SQLException e){
            throw new DAOException("Failed to add condition of visited paragraph"+e.getMessage());
        }    
    }
    
    public void addConditionUnvisited(int storyId, int destinationId, int objectId) {
        try{
            Connection conn = getConn();
            PreparedStatement st = conn.prepareStatement("SELECT * FROM conditions where subjectId=? and storyId=? and visited=0 and objectId=?");
            st.setInt(1, destinationId);
            st.setInt(2, storyId);
            st.setInt(3, objectId);
            ResultSet rs = st.executeQuery();
            if(!rs.next()){
                st.close();
                rs.close();
                st = conn.prepareStatement("INSERT INTO conditions VALUES (?,0,?,?)");
                st.setInt(1, storyId);
                st.setInt(2, destinationId);
                st.setInt(3, objectId);
                st.executeUpdate();
            }
        }catch(SQLException e){
            throw new DAOException("Failed to add condition of unvisited paragraph"+e.getMessage());
        }   
    }
	public List<Story> getStoriesForHistory(int userId) {
		try{
            List<Story> stories = new ArrayList<Story>();
            Connection conn = getConn();
            String query = "select distinct storyId from history where userId=?";
            PreparedStatement st = conn.prepareStatement(query);
            st.setInt(1, userId);
            ResultSet rs = st.executeQuery();
            while(rs.next()){
                Story str = getStoryById(rs.getInt(1));
                stories.add(str);
            }
            return stories;
        }catch(SQLException e){
            throw new DAOException("Failed to get stories for history "+e.getMessage());
        }
	}
	public List<Choice> getChoicesForHistory(int storyId, int userId) {
		try{
            List<Choice> choices = new ArrayList<Choice>();
            Connection conn = getConn();
            String query = "select max(id) from history where userId=? and storyId=? and previousPar is null";
            PreparedStatement st = conn.prepareStatement(query);
            st.setInt(1, userId);
            st.setInt(2, storyId);
            ResultSet rs = st.executeQuery();
            if(!rs.next()){
                //error here
            }
            int currentEntry = rs.getInt(1);
            int currentParagraph = 1;
            choices.add(new Choice("Début", storyId, 0, 0, currentParagraph));
            while(currentEntry != 1){
                //moving to next choice in history
                query = "select id, paragraphId from history where previousPar=?";
                st = conn.prepareStatement(query);
                st.setInt(1, currentEntry);
                rs = st.executeQuery();
                if(!rs.next()){
                    //error here
                    break;
                }
                
                currentEntry = rs.getInt(1);
                System.out.println(currentEntry+ "currEntry1");
                
                st.close();
                rs.close();

                query = "select * from choices where storyId=? and destinationId=?";
                st = conn.prepareStatement(query);
                st.setInt(1, storyId);
                st.setInt(2, currentParagraph);
                if(!rs.next())
                    break;
                
                currentParagraph=rs.getInt("paragraphId");
                System.out.println(currentParagraph+ "para");
    
                String title = rs.getString("choice");
                choices.add(new Choice(title, storyId, 0, 0, currentParagraph));
                

                st.close();
                rs.close();
            }
            
            
            return choices;
        }catch(SQLException e){
            throw new DAOException("Failed to get history "+e.getMessage());
        }
	}
}