//이 java파일이 무슨 역할인지 정확히 모르겠다. 

package board;		//board 패키지 안에 들어있음

import java.util.Date;	//Date를 import함.

public class BoardDataBean {		
// board DB테이블 컬럼들을 private으로 선언함.
   private int num;
   private String boardid;
   private String writer;
   private String email;
   private String subject;
   private String passwd;
   private Date reg_date;
   private int readcount;
   private int ref;
   private int re_step;
   private int re_level;
   private String content;
   private String ip;
   private String filename;
   private int filesize;
   
   //getter setter을 사용하여 값을 가져오고(get) 설정함(set).
   public int getNum() {
      return num;
   }
   public void setNum(int num) {
      this.num = num;
   }
   public String getBoardid() {
      return boardid;
   }
   public void setBoardid(String boardid) {
      this.boardid = boardid;
   }
   public String getWriter() {
      return writer;
   }
   public void setWriter(String writer) {
      this.writer = writer;
   }
   public String getEmail() {
      return email;
   }
   public void setEmail(String email) {
      this.email = email;
   }
   public String getSubject() {
      return subject;
   }
   public void setSubject(String subject) {
      this.subject = subject;
   }
   public String getPasswd() {
      return passwd;
   }
   public void setPasswd(String passwd) {
      this.passwd = passwd;
   }
   public Date getReg_date() {
      return reg_date;
   }
   public void setReg_date(Date reg_date) {
      this.reg_date = reg_date;
   }
   public int getReadcount() {
      return readcount;
   }
   public void setReadcount(int readcount) {
      this.readcount = readcount;
   }
   public int getRef() {
      return ref;
   }
   public void setRef(int ref) {
      this.ref = ref;
   }
   public int getRe_step() {
      return re_step;
   }
   public void setRe_step(int re_step) {
      this.re_step = re_step;
   }
   public int getRe_level() {
      return re_level;
   }
   public void setRe_level(int re_level) {
      this.re_level = re_level;
   }
   public String getContent() {
      return content;
   }
   public void setContent(String content) {
      this.content = content;
   }
   public String getIp() {
      return ip;
   }
   public void setIp(String ip) {
      this.ip = ip;
   }
   public String getFilename() {
      return filename;
   }
   public void setFilename(String filename) {
      this.filename = filename;
   }
   public int getFilesize() {
      return filesize;
   }
   public void setFilesize(int filesize) {
      this.filesize = filesize;
   }
@Override //오버라이드를 어떻게 하는거지? 어디서 이 핑크 오버라이드가 나온거지? 
//toString => 값을 string으로 바꿈. 콘솔에 찍기위해서 만든 메소드. 근데 잘 모르겠다. 확실히는 더 찾아볼것.
public String toString() {
	return "BoardDataBean [num=" + num + ", boardid=" + boardid + ", writer=" + writer + ", email=" + email
			+ ", subject=" + subject + ", passwd=" + passwd + ", reg_date=" + reg_date + ", readcount=" + readcount
			+ ", ref=" + ref + ", re_step=" + re_step + ", re_level=" + re_level + ", content=" + content + ", ip=" + ip
			+ ", filename=" + filename + ", filesize=" + filesize + "]";
}
   
   
}