package board;

// import�� �͵�.
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

//�̱���, ���ش� ��Ȯ�� �Ȱ���. ��ü�� �������� �ʴ°�? �� �ּҿ��� ��� �����ǰ��ϴ°�?
public class BoardDBBean {
//                  Ŭ������        �ν��Ͻ�����   ��ü����     Ŭ������
   private static BoardDBBean instance = new BoardDBBean();	
   // �̰� ����? �������ΰ�?
   private BoardDBBean() {
      
   }
// ���ذ� �Ȱ���. �޼ҵ��ΰ�����. 
//                 ����Ÿ��           �޼ҵ� ��
   public static BoardDBBean getInstance() {
      return instance;		//���� �ν��Ͻ� ������ ��ȯ�ϳ�?
   }

// connection. db�� �����ϴ� �޼ҵ� �ΰ�����. con�� ��ȯ�ϱ����� �޼ҵ�.
//             ����Ÿ��?     �޼ҵ� �� 
public static Connection getConnection(){
//   ����Ÿ��      ����             
   Connection con = null;			//con������ �����ϰ�  null�� �ִ´�.
// getConnection �޼ҵ带 �����ϸ� ���� �ڵ���� ����ȴ�.
   try {
	  // db���� ������ ���� db������ �Է��Ѵ�. �ڼ��Ѱ� ���Ŀ� �ٽ� �����ϱ�. å�� ���� ����.
      String jdbcUrl = "jdbc:oracle:thin:@localhost:1521:orcl";
      String dbUser = "scott";
      String dbPass = "tiger";
      Class.forName("oracle.jdbc.driver.OracleDriver");
      //con �� ���� �ִ´�. 
      con = DriverManager.getConnection(jdbcUrl, dbUser, dbPass);
   //�ٵ� try-catch�� ������. try�ϰ� �ȵǸ� catch?
   }catch(Exception e) {
      e.printStackTrace();
   }
   //con�� ��ȯ�Ѵ�.
   return con;
   }

//close�޼ҵ�. �Ķ���ͷ� con,rs,pstmt�� �޴´�. con�� ���� �޼ҵ忡�� ��ȯ�ް�. rs,pstmt �޴´�. null�� �ƴҶ� �ݾ��ִ� �޼ҵ�.
public void close(Connection con, ResultSet rs, PreparedStatement pstmt) {
   if(rs!=null)		//rs�� null�� �ƴϸ� rs�� �ݰ� null�̸� ex(����ó��)�� �Ѵ�. 
      try {
         rs.close();
      }catch(SQLException ex) {}
   if(pstmt!=null)
      try {
         pstmt.close();
      }catch(SQLException ex) {}
   if(con!=null)
      try {
         con.close();
      }catch(SQLException ex) {}
   }

//insertArticle�޼ҵ�. �Ű������� article�� �޴´�. �ٵ� article�� ����?
public void insertArticle(BoardDataBean article) {
      //�ؿ� ���� �Ȱ�. �ϴ� ���� �� null�� ���峪 ����.
	  String sql="";
      Connection con = getConnection();
      PreparedStatement pstmt = null;
      ResultSet rs = null;
      int number=0;
      
      //�������� �𸣰���. �ٽ� �� ����.
      try {                           //boardser ������ / ���� �������� ������ ��ȯ
         pstmt = con.prepareStatement("select boardser.nextval"+" from dual");
         rs = pstmt.executeQuery();		//?
         if(rs.next())					//?
            number = rs.getInt(1)+1;
            else number = 1;
      
         //insert�ϴ� sql���̴�.key,value�� �� �ִ´�. 
      sql = "insert into board(num,writer,email,subject,passwd,reg_date,";
      sql += "ref,re_step,re_level,content,ip,boardid) "
         + "values(?,?,?,?,?,sysdate,?,?,?,?,?, ?)";
      
      
      pstmt = con.prepareStatement(sql);
      pstmt.setInt(1, number);
      //�� article�� get�ؼ� ���������� �� �������? key�� value�� ���� �����Ϸ��°ǰ� ������ ���� ���ذŶ� �ؿ� �ڵ�� ���������ϱ�.
      pstmt.setString(2, article.getWriter());
      pstmt.setString(3, article.getEmail());
      pstmt.setString(4, article.getSubject());
      pstmt.setString(5, article.getPasswd());
      pstmt.setInt(6, number);
      pstmt.setInt(7, 0);
      pstmt.setInt(8, 0);
      pstmt.setString(9, article.getContent());
      pstmt.setString(10, article.getIp());
      pstmt.setString(11, article.getBoardid());
      pstmt.executeQuery();
      
      }catch(SQLException ex){
         ex.printStackTrace();
      }finally {		//finally�� �� ���ִ� �ɱ�?
         close(con, rs, pstmt);
      }
   }
	//getArticleCount �޼ҵ��̴�. ���� �޼ҵ����� �𸣰ڳ� int�� ��ȯ�ϰ� �Ű������δ� boardid�� �޴´�.
   public int getArticleCount(String boardid) {
      int x=0;		//x��������.
      //sql�̶� String sql�̶� ����������. �� �� ���尰�� ���̳�. " " ��ü�� �����־ �׷���. �׷� ������ String���� �ϸ� �ȵǳ�?
      String sql="select nvl(count(*),0) from board where boardid = ?";
      //�� �ڲ� ���ִ� �ɱ�?
      Connection con = getConnection();
      PreparedStatement pstmt = null;
      ResultSet rs = null;
      int number = 0;		//number�� ��� ���̳�.
      try {
      pstmt=con.prepareStatement(sql);
      pstmt.setString(1, boardid);
      
      rs=pstmt.executeQuery();
      if(rs.next()) { x=rs.getInt(1); }
      }
      catch(Exception e) {
         e.printStackTrace();
      }finally {
         close(con, rs, pstmt);
      }
      return x;
   }
   
   public List getArticles(int startRow, int endRow, String boardid) {
      Connection conn = null;
      PreparedStatement pstmt = null;
      ResultSet rs = null;
      List articleList = null;
      String sql = "";
      try {
         conn = getConnection();
         sql = " select * from" + "( select rownum rnum ,a.* "
                  + " from (select num,writer,email,subject,passwd,"
                  + "reg_date,readcount,ref,re_step,re_level,content,"
                  + "ip from board where boardid = ? order by ref desc , re_step) "
                  + " a ) where rnum between ? and ? ";
         pstmt = conn.prepareStatement(sql);
         pstmt.setString(1, boardid);
         pstmt.setInt(2, startRow);
         pstmt.setInt(3, endRow);
         rs = pstmt.executeQuery();
         
         if(rs.next()) {
            articleList = new ArrayList();
            do {
               BoardDataBean article = new BoardDataBean();
               article.setNum(rs.getInt("num"));
               article.setWriter(rs.getString("writer"));
               article.setEmail(rs.getString("email"));
               article.setSubject(rs.getString("subject"));
               article.setPasswd(rs.getString("passwd"));
               article.setReg_date(rs.getTimestamp("reg_date"));
               article.setReadcount(rs.getInt("readcount"));
               article.setRef(rs.getInt("ref"));
               article.setRe_step(rs.getInt("re_step"));
               article.setRe_level(rs.getInt("re_level"));
               article.setContent(rs.getString("content"));
               article.setIp(rs.getString("ip"));
               articleList.add(article);
               
            }while(rs.next());
         }}catch(Exception ex) {
            ex.printStackTrace();
         }finally {close(conn, rs, pstmt);}
            return articleList;
      }
   
   public BoardDataBean getArticle(int num, String boardid, String chk) {
      Connection conn = null;
      PreparedStatement pstmt = null;
      ResultSet rs = null;
      BoardDataBean article = null;
      String sql="";
      try {
         conn = getConnection();
         
         if(chk.equals("content")) {
         sql = "update board set readcount = readcount+1 " + "where num = ? and boardid = ?";
         pstmt = conn.prepareStatement(sql);
         pstmt.setInt(1, num);
         pstmt.setString(2, boardid);
         pstmt.executeUpdate();
         }
         sql = "select * from board where num = ? and boardid = ?";
         pstmt = conn.prepareStatement(sql);
         pstmt.setInt(1, num);
         pstmt.setString(2, boardid);
         rs=pstmt.executeQuery();
         
         if(rs.next()) {
            article = new BoardDataBean();
            article.setNum(rs.getInt("num"));
            article.setWriter(rs.getString("writer"));
            article.setEmail(rs.getString("email"));
            article.setSubject(rs.getString("subject"));
            article.setPasswd(rs.getString("passwd"));
            article.setReg_date(rs.getTimestamp("reg_date"));
            article.setReadcount(rs.getInt("readcount"));
            article.setRef(rs.getInt("ref"));
            article.setRe_step(rs.getInt("re_step"));
            article.setRe_level(rs.getInt("re_level"));
            article.setContent(rs.getString("content"));
            article.setIp(rs.getString("ip"));
         }
         
      }catch(Exception e) {
         e.printStackTrace();
   }finally {close(conn, rs, pstmt);}
   return article;
   }
   public int updateArticle(BoardDataBean article) {
	   Connection conn = null;
	   PreparedStatement pstmt = null;
	   int chk = 0;
	   try {
		   conn = getConnection();
	   String sql = "update board set writer=?,email=?,subject=?,content=? where num=? and passwd = ?";
	   pstmt =  conn.prepareStatement(sql);
	   pstmt.setString(1, article.getWriter());
	   pstmt.setString(2, article.getEmail());
	   pstmt.setString(3, article.getSubject());
	   pstmt.setString(4, article.getContent());
	   pstmt.setInt(5, article.getNum());
	   pstmt.setString(6, article.getPasswd());
	   chk = pstmt.executeUpdate();
	   }catch(Exception e) {e.printStackTrace();
	   }finally {close(conn,null,pstmt);}
	   return chk;}
}

