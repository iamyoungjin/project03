package test.web.project03;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import com.sun.org.apache.xpath.internal.operations.Equals;

public class MemberDAO {
	private Connection conn = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;
	// 주로 사용하는 커넥트, 프리페어스테이트먼트, 리설트셋 설정
	
	// DB연결 싱글톤
	MemberDAO(){}
	private static MemberDAO instance = new MemberDAO();
	public static MemberDAO getInstance() {
		return instance;
	}
	
	// 연결 메서드
	private Connection getConnection() throws Exception{
		Context ctx = new InitialContext();
		Context env = (Context)ctx.lookup("java:comp/env");
		DataSource ds = (DataSource)env.lookup("jdbc/xe");
		return ds.getConnection();
	}
	
	//  회원가입 메서드, DB에 insert 
	public boolean insert(MemberDTO dto) {
		boolean chk = false;
		try {
			conn = getConnection();
			pstmt = conn.prepareStatement("insert into member_table values(member_table_seq.nextval,?,?,?,?,?,?,2,sysdate)");
			pstmt.setString(1, dto.getId());
			pstmt.setString(2, dto.getEmail());
			pstmt.setString(3, dto.getPw());
			pstmt.setString(4, dto.getName());
			pstmt.setString(5, dto.getPhonenum());
			pstmt.setString(6, dto.getBirthdate());
			pstmt.executeUpdate();
			chk = true;
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			if(pstmt!=null) {try{pstmt.close();}catch(SQLException s) {s.printStackTrace();}}
			if(conn!=null) {try{conn.close();}catch(SQLException s) {s.printStackTrace();}}
		}
		return chk;
	}
		
	
	// 아이디 중복 검사 메서드, DB에서 아이디를 조회후 값이 중복일 경우 true를 리턴
	public boolean confirmId(String id) {
		boolean chk = false;
		try {
			conn = getConnection();
			pstmt = conn.prepareStatement("select id from member_table");
			rs = pstmt.executeQuery();
			while(rs.next()) {
				if(rs.getString("id").equals(id)) {
					chk = true;
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
		if(rs!=null) {try{rs.close();}catch(SQLException s) {s.printStackTrace();}}
		if(pstmt!=null) {try{pstmt.close();}catch(SQLException s) {s.printStackTrace();}}
		if(conn!=null) {try{conn.close();}catch(SQLException s) {s.printStackTrace();}}
	}
		return chk;
	}
	
	// 로그인 메서드
	public boolean login(String id,String pw) {
		boolean chk = false;
		try {
			conn = getConnection();
			// 아이디와 비밀번호가 일치하면 chk값을 true로 설정한다
			pstmt = conn.prepareStatement("select * from member_table where id=? and pw=?");
			pstmt.setString(1, id);
			pstmt.setString(2, pw);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				chk = true;
				// 만약, 탈퇴유저일 경우 가입할수 없도록 한다.
				if(rs.getInt("user_type") == 3) {
					chk = false;
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			if(rs!=null) {try{rs.close();}catch(SQLException s) {s.printStackTrace();}}
			if(pstmt!=null) {try{pstmt.close();}catch(SQLException s) {s.printStackTrace();}}
			if(conn!=null) {try{conn.close();}catch(SQLException s) {s.printStackTrace();}}
		}
		return chk;
	}
	// id를 매개변수로 이름을 가져오는 메서드
	public String searchName(String id) {
		String name=null;
		try {
			conn = getConnection();
			pstmt = conn.prepareStatement("select name from member_table where id=?");
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				name = rs.getString("name");
			}
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			if(rs!=null) {try{rs.close();}catch(SQLException s) {s.printStackTrace();}}
			if(pstmt!=null) {try{pstmt.close();}catch(SQLException s) {s.printStackTrace();}}
			if(conn!=null) {try{conn.close();}catch(SQLException s) {s.printStackTrace();}}
		}
		return name;
	}
	
	// 회원 정보 수정에서 기존 정보 읽어오는 메서드
	public MemberDTO getMember(String id) {
		MemberDTO dto = null;
		try {
			conn = getConnection();
			pstmt = conn.prepareStatement("select * from member_table where id=?");
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				dto = new MemberDTO();
				dto.setId(rs.getString("id"));
				dto.setName(rs.getString("name"));
				dto.setEmail(rs.getString("email"));
				dto.setPhonenum(rs.getString("phonenum"));
				dto.setPw(rs.getString("pw"));
				dto.setBirthdate(rs.getString("birthdate"));
				dto.setUser_type(rs.getInt("user_type")); 
			}
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			if(rs!=null) {try{rs.close();}catch(SQLException s) {s.printStackTrace();}}
			if(pstmt!=null) {try{pstmt.close();}catch(SQLException s) {s.printStackTrace();}}
			if(conn!=null) {try{conn.close();}catch(SQLException s) {s.printStackTrace();}}
		}
		
		return dto;
	}
	
	// 회원 정보 수정하는 메서드
	public boolean modifyMember(String id, MemberDTO dto) {
		boolean chk = false;
		try {
			conn = getConnection();
			String sql = "update member_table set email=?, pw=?, name=?, phonenum=?, birthdate=? where id=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, dto.getEmail());
			pstmt.setString(2, dto.getPw());
			pstmt.setString(3, dto.getName());
			pstmt.setString(4, dto.getPhonenum());
			pstmt.setString(5, dto.getBirthdate());
			pstmt.setString(6, id);
			pstmt.executeUpdate();
			chk = true;
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			if(pstmt!=null) {try{pstmt.close();}catch(SQLException s) {s.printStackTrace();}}
			if(conn!=null) {try{conn.close();}catch(SQLException s) {s.printStackTrace();}}
		}
		return chk;
	}
	
	// 회원 탈퇴 메서드
	public int deleteMember(String id, String pw) {
		int res = 0;
		// res 기본값 0, try가 실행 되지 않을시 발생
		try {
			conn = getConnection();
			pstmt = conn.prepareStatement("select * from member_table where id=? and pw=?");
			pstmt.setString(1, id);
			pstmt.setString(2, pw);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				pstmt = conn.prepareStatement("update member_table set user_type=3 where id=? and pw=?");
				pstmt.setString(1, id);
				pstmt.setString(2, pw);
				pstmt.executeUpdate();
				res = 1;
			}else{
				res = 2;
				// rs.next가 없다. 아이디와 비밀번호가 틀릴때.
			}
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			if(rs!=null) {try{rs.close();}catch(SQLException s) {s.printStackTrace();}}
			if(pstmt!=null) {try{pstmt.close();}catch(SQLException s) {s.printStackTrace();}}
			if(conn!=null) {try{conn.close();}catch(SQLException s) {s.printStackTrace();}}
		}
		return res;
	}
	// checkClientData에서 사람 검색을 하기 위한 메서드
	public List showMember(String search, String keyword) {
		List list = null;
		try {
			conn = getConnection();
			// search(셀렉트박스)와 keyword값을 통해 해당되는 데이터를 추출한다 
			if(search.equals("0")) {
				pstmt = conn.prepareStatement("select * from member_table where num!=1 order by num");
			}else if(search.equals("1")) {
				pstmt = conn.prepareStatement("select * from member_table where num!=1 and id like ? order by num");
				pstmt.setString(1, "%"+keyword+"%");
			}else if(search.equals("2")) {
				pstmt = conn.prepareStatement("select * from member_table where num!=1 and name like ? order by num");
				pstmt.setString(1, "%"+keyword+"%");
			}
			rs = pstmt.executeQuery();
			// 이를 list에 담는다 
			list = new ArrayList();
			while(rs.next()) {
				MemberDTO dto = new MemberDTO();
				dto.setNum(rs.getInt("num"));
				dto.setId(rs.getString("id"));
				dto.setEmail(rs.getString("email"));
				dto.setName(rs.getString("name"));
				dto.setPhonenum(rs.getString("phonenum"));
				dto.setBirthdate(rs.getString("birthdate"));
				dto.setReg(rs.getTimestamp("reg_date"));
				dto.setUser_type(rs.getInt("user_type"));
				dto.setPw(rs.getString("pw"));
				list.add(dto);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			if(rs!=null) {try{rs.close();}catch(SQLException s) {s.printStackTrace();}}
			if(pstmt!=null) {try{pstmt.close();}catch(SQLException s) {s.printStackTrace();}}
			if(conn!=null) {try{conn.close();}catch(SQLException s) {s.printStackTrace();}}
		}
		return list;
	}
	
	// sns 회원가입을 위한 메서드
	// sns 인증으로 oauth에서 값을 가져오고, 이를 통해 자동가입 시킨다
	public boolean insertNaver(String id, String email, String name) {
		boolean chk= false;
		try {
			conn = getConnection();
			pstmt = conn.prepareStatement("select * from member_table where id=?");
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				chk = false;
			}else {
				pstmt = conn.prepareStatement("insert into member_table values(member_table_seq.nextval,?,?,?,?,'null','null',4,sysdate)");
				pstmt.setString(1, id);
				pstmt.setString(2, email);
				pstmt.setString(3, id);
				pstmt.setString(4, name);
				pstmt.executeUpdate();
				chk = true;
			}
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			if(rs!=null) {try{rs.close();}catch(SQLException s) {s.printStackTrace();}}
			if(pstmt!=null) {try{pstmt.close();}catch(SQLException s) {s.printStackTrace();}}
			if(conn!=null) {try{conn.close();}catch(SQLException s) {s.printStackTrace();}}
		}
		return chk;
	}
	// sns 로그인 유저는 자신의 아이디/비밀번호를 알 수 없기 때문에 sns인증으로 로그인시킨다
	// sns 인증후 oauth를 통해 가져오는 값을 사용해 검사한다.
	
	public boolean loginNaver(String id) {
		boolean chk = false;
		try {
			conn = getConnection();
			pstmt = conn.prepareStatement("select * from member_table where id=? and pw=?");
			pstmt.setString(1, id);
			pstmt.setString(2, id);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				chk = true;
				if(rs.getInt("user_type") == 3) {
					chk = false;
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			if(rs!=null) {try{rs.close();}catch(SQLException s) {s.printStackTrace();}}
			if(pstmt!=null) {try{pstmt.close();}catch(SQLException s) {s.printStackTrace();}}
			if(conn!=null) {try{conn.close();}catch(SQLException s) {s.printStackTrace();}}
		}
		return chk;
	}
	// sns 회원가입을 위한 메서드
	// sns 인증으로 oauth에서 값을 가져오고, 이를 통해 자동가입 시킨다
	public boolean insertKakao(String id) {
		boolean chk= false;
		try {
			conn = getConnection();
			pstmt = conn.prepareStatement("select * from member_table where id=?");
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				chk = false;
			}else {
				pstmt = conn.prepareStatement("insert into member_table values(member_table_seq.nextval,?,'null',?,'null','null','null',4,sysdate)");
				pstmt.setString(1, id);
				pstmt.setString(2, id);
				pstmt.executeUpdate();
				chk = true;
			}
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			if(rs!=null) {try{rs.close();}catch(SQLException s) {s.printStackTrace();}}
			if(pstmt!=null) {try{pstmt.close();}catch(SQLException s) {s.printStackTrace();}}
			if(conn!=null) {try{conn.close();}catch(SQLException s) {s.printStackTrace();}}
		}
		return chk;
	}
	// sns 로그인 유저는 자신의 아이디/비밀번호를 알 수 없기 때문에 sns인증으로 로그인시킨다
	// sns 인증후 oauth를 통해 가져오는 값을 사용해 검사한다.
	public boolean loginKakao(String id) {
		boolean chk = false;
		try {
			conn = getConnection();
			pstmt = conn.prepareStatement("select * from member_table where id=? and pw=?");
			pstmt.setString(1, id);
			pstmt.setString(2, id);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				chk = true;
				if(rs.getInt("user_type") == 3) {
					chk = false;
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			if(rs!=null) {try{rs.close();}catch(SQLException s) {s.printStackTrace();}}
			if(pstmt!=null) {try{pstmt.close();}catch(SQLException s) {s.printStackTrace();}}
			if(conn!=null) {try{conn.close();}catch(SQLException s) {s.printStackTrace();}}
		}
		return chk;
	}
	// sns 회원가입을 위한 메서드
	// sns 인증으로 oauth에서 값을 가져오고, 이를 통해 자동가입 시킨다
	public boolean insertGoogle(String id, String email) {
		boolean chk = false;
		try {
			conn = getConnection();
			pstmt = conn.prepareStatement("select * from member_table where id=?");
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				chk = false;
			}else {
				pstmt = conn.prepareStatement("insert into member_table values(member_table_seq.nextval,?,?,?,'null','null','null',4,sysdate)");
				pstmt.setString(1, id);
				pstmt.setString(2, email);
				pstmt.setString(3, id);
				pstmt.executeUpdate();
				chk = true;
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			if(rs!=null) {try{rs.close();}catch(SQLException s) {s.printStackTrace();}}
			if(pstmt!=null) {try{pstmt.close();}catch(SQLException s) {s.printStackTrace();}}
			if(conn!=null) {try{conn.close();}catch(SQLException s) {s.printStackTrace();}}
		}
		
		return chk;
	}
	// sns 로그인 유저는 자신의 아이디/비밀번호를 알 수 없기 때문에 sns인증으로 로그인시킨다
	// sns 인증후 oauth를 통해 가져오는 값을 사용해 검사한다.
	public boolean loginGoogle(String id) {
		boolean chk = false;
		try {
			conn = getConnection();
			pstmt = conn.prepareStatement("select * from member_table where id=? and pw=?");
			pstmt.setString(1, id);
			pstmt.setString(2, id);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				chk = true;
				if(rs.getInt("user_type") == 3) {
					chk = false;
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			if(rs!=null) {try{rs.close();}catch(SQLException s) {s.printStackTrace();}}
			if(pstmt!=null) {try{pstmt.close();}catch(SQLException s) {s.printStackTrace();}}
			if(conn!=null) {try{conn.close();}catch(SQLException s) {s.printStackTrace();}}
		}
		return chk;
	}
	
	// 만일, sns 로그인 유저일시 oauth를 통해 넘어오는 정보의 양이 한정될 수 있다.
	// 그렇기 때문에 남은 개인정보를 입력하기 위해서 sns를 통한 회원가입인지 확인한다. 이후 chk가 true일시 modifyForm으로 보내기 위해 사용한다
	public boolean chkInfoSnsLogin(String sId) {
		boolean chk = false;
		try {
			conn = getConnection();
			pstmt = conn.prepareStatement("select * from member_table where id=?");
			pstmt.setString(1, sId);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {	// 검색 결과값이 존재하는가?
				if(rs.getInt("user_type") == 4) {	// 유저타입이 4 = 소셜 로그인 인가?
					if(rs.getString("email").equals("null") || rs.getString("name").equals("null") ||
							rs.getString("phonenum").equals("null") || rs.getString("birthdate").equals("null")) {
						// 개인정보의 이메일, 이름, 전화번호, 생년월일 중 하나라도 null인가?
						chk = true;
					}
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			if(rs!=null) {try{rs.close();}catch(SQLException s) {s.printStackTrace();}}
			if(pstmt!=null) {try{pstmt.close();}catch(SQLException s) {s.printStackTrace();}}
			if(conn!=null) {try{conn.close();}catch(SQLException s) {s.printStackTrace();}}
		}
		
		return chk;
	}
	
	
}
