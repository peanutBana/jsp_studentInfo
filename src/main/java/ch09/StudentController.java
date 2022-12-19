package ch09;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;

@WebServlet("/studentControl")
public class StudentController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	StudentDAO dao;

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);		//서블릿 초기화
		dao = new StudentDAO();		//초기화 시 한번만 생성
	}
	

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		//request : 뷰에서 넘어온 데이터의 정보 저장되어있음
		String action = request.getParameter("action");		//
		String view="";
		
		if(action == null) {
			//리퀘스트를 또 한다.
			getServletContext().getRequestDispatcher("/studentControl?action=list").forward(request, response);
		}
		else {
			switch(action) {
				case "list": view = list(request, response); break;
				case "insert": view = insert(request, response);  break;	//request와 response객체를 매개변수로 넘겨준다. ==>
			}
			//getRequestDispatcher: 이동할 페이지의 경로 지정
			//forward: 페이지를 이동시킴. 내부에서 이동이 되므로 주소가 변하지 않는다.
			getServletContext().getRequestDispatcher("/ch09/" + view).forward(request, response);
		}
	}
	
	public String list(HttpServletRequest request, HttpServletResponse response) {
		//request.setAttribute(key, value)
		request.setAttribute("students", dao.getAll());		//request, response 할 때 값을 넘겨준다.
		return "studentInfo.jsp";
		
	}
	
	
	//request데이터 받아옴 -> DAO에 있는 insert 실행(DB에 insert됨) -> 페이지명(studentInfo.jsp)에 리턴 
	public String insert(HttpServletRequest request, HttpServletResponse response) {		
		Student s = new Student();
		
		try {
			BeanUtils.populate(s,request.getParameterMap());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		dao.insert(s);		//컨트롤러는 DAO에 있는 메소드를 사용한다. / DAO한테 요청해야한다.
		
		return "studentInfo.jsp";
	}

}
