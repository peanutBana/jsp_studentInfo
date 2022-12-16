package ch09;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class StudentDAO {
	Connection conn = null;		//db와 연결에 사용
	PreparedStatement pstmt;	//쿼리문 실행에 사용(sql문은 미리 실행되어 pstmt에 저장)
	
	//jdbc: 자바와 db를 연결해주는 api => ojdbc6.jar
	final String JDBC_DRIVER = "oracle.jdbc.driver.OracleDriver";
	final String JDBC_URL= "jdbc:oracle:thin:@localhost:1521:xe";
	
	//db연결 메소드
	public void open() {
		try {
			Class.forName(JDBC_DRIVER);		//드라이버 로드
			conn = DriverManager.getConnection(JDBC_URL, "test","test1234");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//db연결 해제 메소드
	public void close() {
		//pstmt, conn은 리소스(데이터를 읽고 쓰는 객체)이므로 사용 후 반드시 닫아줘야 한다.
		try {
			pstmt.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	//학생 정보를 다 불러온다.(select * from tableName)
	public ArrayList<Student> getAll(){
		open();		//db오픈
		ArrayList<Student> students = new ArrayList<Student>();		//Student객체를 담을 ArrayList 생성
		
		try {
			pstmt = conn.prepareStatement("select * from student");		//실행할 쿼리문 작성
			ResultSet rs = pstmt.executeQuery();		//저장한 쿼리문 실행(select문 사용시)  / ResultSet객체: 쿼리문으로 받아온 데이터를 저장
			
			 while(rs.next()) {	//한 행씩 값이 있는지 없는지 판단 boolean
				 Student s = new Student();	
				 //rs.get~으로 db에 각 컬럼에 저장된 값을 Student객체에 set
				 s.setId(rs.getInt("id"));
				 s.setUsername(rs.getString("username"));
				 s.setUniv(rs.getString("univ"));
				 s.setBirth(rs.getDate("birth"));
				 s.setEmail(rs.getString("email"));
				 
				 students.add(s);
			 }
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}

		return students;
	}
	
	//학생 정보를 입력
	public void insert(Student s) {
		open();
		//?: 어떤 데이터가 들어올지 모름
		String sql = "insert into student values(id_seq.nextval, ?, ?, ?, ?)";
		
		try {
			pstmt = conn.prepareStatement(sql);
			//오라클 데이터 타입으로 변환해준다
			pstmt.setString(1, s.getUsername());	//pstmt.setString(값을 넣어줄 위치, 넣을 데이터)
			pstmt.setString(2, s.getUniv());
			pstmt.setDate(3, s.getBirth());
			pstmt.setString(4, s.getEmail());
			
			pstmt.executeUpdate();		//insert, delete, update시 사용
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			close();
		}
	}
	
	
	
	
}
