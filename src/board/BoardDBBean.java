package board;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BoardDBBean {

	private static BoardDBBean instance = new BoardDBBean(); //按眉 积己

	private BoardDBBean() {		//积己磊 private 急攫: main俊辑 按眉 积己 给窍霸 窃.
		
	}
	public static BoardDBBean getInstance() {		
		return instance;
	}
	
	public static Connection getConnection() {
			Connection con = null;
			try {
				String jdbcUrl = "jdbc:oracle:thin:@localhost:1521:orcl";
				String dbId = "scott";
				String dbPass="tiger";
				Class.forName("oracle.jdbc.driver.OracleDriver");
				con = DriverManager.getConnection(jdbcUrl, dbId, dbPass);
			}catch(Exception e) {
				e.printStackTrace();
			}
			return con;
	}
	public void close(Connection con,ResultSet rs,
			PreparedStatement pstmt) {
		if(rs!=null)
			try {
				rs.close();
			}catch(SQLException ex) {}
		if(pstmt != null) try {pstmt.close();
	} catch(SQLException ex) {	}
		if(con != null)
			try {
				con.close();
			}catch(SQLException ex) {}

	}
	public void insertArticle(BoardDataBean article) {
		String sql="";
		Connection con = getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int number = 0;
		try {
			pstmt
			= con.prepareStatement("select boardser.nextval " + " from dual");
			rs= pstmt.executeQuery();
			if(rs.next()) number = rs.getInt(1) + 1;
			else number = 1;
			
		sql = "insert into board(num,writer,email,subject,passwd,reg_date,";
		sql+= "ref,re_step,re_level,content,ip,boardid) "
				+ "values(?,?,?,?,?,sysdate,?,?,?,?,?, ?)";
		pstmt = con.prepareStatement(sql);
		pstmt.setInt(1, number);
		pstmt.setString(2, article.getWriter());
		pstmt.setString(3, article.getEmail());
		pstmt.setString(4, article.getSubject());
		pstmt.setString(5, article.getPasswd());
		pstmt.setInt(6, number);
		pstmt.setInt(7, 0);
		pstmt.setInt(8, 0);
		pstmt.setString(9, article.getContent());
		pstmt.setString(10, article.getIp());
		pstmt.setString(11,article.getBoardid());
		pstmt.executeUpdate();
		
		}catch(SQLException e1) {
			e1.printStackTrace();
		}finally {
			close(con,rs,pstmt);
}
}
}