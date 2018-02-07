package board;

// import한 것들.
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

//싱글톤, 이해는 정확히 안간다. 객체를 생성하지 않는것? 한 주소에만 계속 생성되게하는거?
public class BoardDBBean {
//                  클래스명        인스턴스변수   객체생성     클래스명
   private static BoardDBBean instance = new BoardDBBean();	
   // 이게 뭐지? 생성자인가?
   private BoardDBBean() {
      
   }
// 이해가 안간다. 메소드인가보다. 
//                 리턴타입           메소드 명
   public static BoardDBBean getInstance() {
      return instance;		//위의 인스턴스 변수를 반환하네?
   }

// connection. db에 연결하는 메소드 인가보다. con을 반환하기위한 메소드.
//             리턴타입?     메소드 명 
public static Connection getConnection(){
//   리턴타입      변수             
   Connection con = null;			//con변수를 선언하고  null을 넣는다.
// getConnection 메소드를 실행하면 밑의 코드들이 실행된다.
   try {
	  // db와의 접속을 위해 db정보를 입력한다. 자세한건 추후에 다시 공부하기. 책에 내용 있음.
      String jdbcUrl = "jdbc:oracle:thin:@localhost:1521:orcl";
      String dbUser = "scott";
      String dbPass = "tiger";
      Class.forName("oracle.jdbc.driver.OracleDriver");
      //con 에 값을 넣는다. 
      con = DriverManager.getConnection(jdbcUrl, dbUser, dbPass);
   //근데 try-catch가 뭐였지. try하고 안되면 catch?
   }catch(Exception e) {
      e.printStackTrace();
   }
   //con을 반환한다.
   return con;
   }

//close메소드. 파라미터로 con,rs,pstmt를 받는다. con은 위의 메소드에서 반환받고. rs,pstmt 받는다. null이 아닐때 닫아주는 메소드.
public void close(Connection con, ResultSet rs, PreparedStatement pstmt) {
   if(rs!=null)		//rs가 null이 아니면 rs를 닫고 null이면 ex(예외처리)를 한다. 
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

//insertArticle메소드. 매개변수로 article을 받는다. 근데 article이 뭐지?
public void insertArticle(BoardDataBean article) {
      //밑에 이해 안감. 일단 값을 다 null로 만드나 보다.
	  String sql="";
      Connection con = getConnection();
      PreparedStatement pstmt = null;
      ResultSet rs = null;
      int number=0;
      
      //시퀀스를 모르겠음. 다시 재 정의.
      try {                           //boardser 시퀀스 / 현재 시퀀스의 다음값 반환
         pstmt = con.prepareStatement("select boardser.nextval"+" from dual");
         rs = pstmt.executeQuery();		//?
         if(rs.next())					//?
            number = rs.getInt(1)+1;
            else number = 1;
      
         //insert하는 sql문이다.key,value를 다 넣는다. 
      sql = "insert into board(num,writer,email,subject,passwd,reg_date,";
      sql += "ref,re_step,re_level,content,ip,boardid) "
         + "values(?,?,?,?,?,sysdate,?,?,?,?,?, ?)";
      
      
      pstmt = con.prepareStatement(sql);
      pstmt.setInt(1, number);
      //헙 article이 get해서 변수값들을 다 갖고오네? key와 value를 같이 저장하려는건가 쿼리문 위에 써준거랑 밑에 코드랑 무슨관계일까.
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
      }finally {		//finally는 왜 해주는 걸까?
         close(con, rs, pstmt);
      }
   }
	//getArticleCount 메소드이다. 무슨 메소드인지 모르겠네 int를 반환하고 매개변수로는 boardid를 받는다.
   public int getArticleCount(String boardid) {
      int x=0;		//x변수선언.
      //sql이랑 String sql이랑 무슨차이지. 좀 더 문장같아 보이네. " " 전체가 묶어있어서 그런가. 그럼 위에도 String으로 하면 안되나?
      String sql="select nvl(count(*),0) from board where boardid = ?";
      //왜 자꾸 해주는 걸까?
      Connection con = getConnection();
      PreparedStatement pstmt = null;
      ResultSet rs = null;
      int number = 0;		//number은 어디에 쓰이나.
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

