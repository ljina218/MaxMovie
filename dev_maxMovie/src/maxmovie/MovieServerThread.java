package maxmovie;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Vector;

public class MovieServerThread extends Thread{
	
	//id와 nickname 저장
	String id = null;
	String nickname = null;
	
	//영화정보 셋팅할 변수
	TicketingVO tVO = null;
	
	//예매 결제할때 
	List<TicketingVO> pList = null;
	
	//컨트롤러 생성
	MovieController ctrl = new MovieController();
	
	//스레드 선언부
	MovieServer ms = null;
	ObjectOutputStream oos = null;
	ObjectInputStream ois = null;
	boolean stop = false;
	
	//로그파일 생성을 위한 선언부
	String logpath = "src\\thread\\chat\\";
	String fileName = null;

	//생성자
	public MovieServerThread(MovieServer ms) {
		this.ms = ms;
		ms.globalist.add(this);
		ms.jta_log.append(ms.globalist.size()+"\n");
		ms.movieList = ctrl.dao.refreshMovieAll(ms.movieList);//서버에 3일치 영화정보 저장
		System.out.println("사이즈"+ms.movieList.size());
		try {
			oos = new ObjectOutputStream(ms.socket.getOutputStream());
			ois = new ObjectInputStream(ms.socket.getInputStream());
			String msg = (String) ois.readObject();
			StringTokenizer st = null;
			int protocol = 0;
			if(msg!=null){
				st = new StringTokenizer(msg,"#");
				protocol = Integer.parseInt(st.nextToken());
			}
			if(protocol==MovieProtocol.SELECT) {//화면을 켰으니 영화정보를 주세여!
				synchronized (this) { //00시에 무비리스트가 초기화 되어 에러가 날까봐......
					for(int i=0; i<ms.movieList.size(); i++) {
						this.send(MovieProtocol.SELECT+"#"+ms.movieList.get(i).get("M_CERTIF")
								+"#"+ms.movieList.get(i).get("M_TITLE")
								+"#"+ms.movieList.get(i).get("T_LOC")
								+"#"+ms.movieList.get(i).get("T_NAME")
								+"#"+ms.movieList.get(i).get("S_DATE")
								+"#"+ms.movieList.get(i).get("S_TIME")
								+"#"+ms.movieList.get(i).get("SC_NAME"));							
					}
				}
			}
		} catch (Exception e) {
			System.out.println(e.toString());
			//e.printStackTrace();
		}
	}
	
	//oos 하는 메소드
	public void send(String msg) {
		try {
			oos.writeObject(msg);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	public int checkTime (String date, String time) {//스케줄에 있는 영화 시간 받아서
		Calendar cal = Calendar.getInstance();
		String year = date.substring(0, 4);
		String month = date.substring(4, 6);
		String day = date.substring(6, 8);
		if("0".equals(month.substring(0, 1))) {
			month = month.substring(1, 2);
		}
		if("0".equals(day.substring(0, 1))) {
			day = day.substring(1, 2);
		}
		StringTokenizer st = new StringTokenizer(time, ":");
		String hour = st.nextToken();
		String min =st.nextToken();
		if("0".equals(hour.substring(0, 1))) {
			hour = hour.substring(1, 2);
		}
		if("0".equals(min.substring(0, 1))) {
			min = min.substring(1, 2);
		}
		cal.set(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day), Integer.parseInt(hour),  Integer.parseInt(min));
		//
		Calendar now = Calendar.getInstance();//3월24일에서
		now.set(2020, 3, 24);
		now.add(Calendar.MINUTE, -30);//30분 빼기 => 30분 전까지만 예매가능
		
		int result = cal.compareTo(now);
		//1,0=> 예약가능
		return result;
	}
	
//	//영화 선택 리스트 전달 메소드
//	public void choiceMoive(int MovieProtocol) {
//		for(int i=0; i<ms.movieList.size(); i++) {
//			String title = ms.movieList.get(i).get("M_TITLE").toString();
//			String age = ms.movieList.get(i).get("M_CERTIF").toString();
//			String loc = ms.movieList.get(i).get("T_LOC").toString();
//			String theater = ms.movieList.get(i).get("T_NAME").toString();
//			String date = ms.movieList.get(i).get("S_DATE").toString();
//			String time = ms.movieList.get(i).get("S_TIME").toString();
//			String screen = ms.movieList.get(i).get("SC_NAME").toString();
//			int result = checkTime(time);
//			if(result==1) {//상영시간이 30분 전이고....
//				
//				if(tVO.getMovie_name()==null) {
//					tVO.setMovie_name(title);
//				}
//				if(tVO.getMovie_age()==null) {
//					tVO.setMovie_age(age);
//				}
//				if(tVO.getLoc()==null) {
//					tVO.setLoc(loc);
//				}
//				if(tVO.getTheater()==null) {
//					tVO.setTheater(theater);
//				}
//				if(tVO.getMovie_date()==null) {
//					tVO.setMovie_date(date);
//				}
//				if(tVO.getMovie_time()==null) {
//					tVO.setMovie_time(time);
//				}
//				if(tVO.getMovie_screen()==null) {
//					tVO.setMovie_screen(screen);
//				}
//				if(tVO.getMovie_name().equals(title)&&
//						tVO.getLoc().equals(loc)&&
//							tVO.getTheater().equals(theater)&&
//								tVO.getMovie_date().equals(date)&&
//									tVO.getMovie_time().equals(time)&&
//										tVO.getMovie_screen().equals(screen)){
//					String msg = MovieProtocol+"#"+tVO.getMovie_name()+"#"+tVO.getMovie_age()+"#"+
//							tVO.getLoc()+"#"+tVO.getTheater()+"#"+tVO.getMovie_date()+"#"+tVO.getMovie_time()+"#"+tVO.getMovie_screen();
//					this.send(msg);	
//				}
//			}
//		}
//	}
		
	//이벤트로부터 넘어온 메세지를 듣고 말하는 메소드
	@Override
	public void run() {
		int num = 0;
		while(!stop) {
			/****************************************************************************
			 * 리프레시 되는 곳
			 */
			if("00:00:00".equals(ms.setTimer(null,"시간"))&&num==0) {//정시가되면
				//[1]로그파일저장
				String fileName = "_log_"+ms.setTimer(null,"날짜")+".txt";
				try {
					File f = new File(logpath+fileName);
					PrintWriter pw 
						= new PrintWriter(
								new BufferedWriter(
										new FileWriter(f.getAbsolutePath())));
					pw.write(ms.jta_log.getText());
					pw.close();
				} catch (Exception e) {
					System.out.println(e.toString());
					e.printStackTrace();
				}
//				//[2]어제 영화정보 지우기
//				Calendar newDay = Calendar.getInstance();//새로운 오늘 날짜정보에서
//				newDay.add(Calendar.DAY_OF_MONTH, -1);//어제날짜 뽑기[형식:20200325]
//				String yesterday = ms.setTimer(newDay, "날짜");
//				for(int i=0; i<ms.movieList.size(); i++) {
//					if(yesterday.equals(ms.movieList.get(i).get("S_DATE"))) {//영화정보 리스트에서 제거
//						ms.movieList.remove(i);
//					}			
//				}
//				//[3]새로운 영화정보 추가하기
//				List<Map<String, Object>> newMovie = ms.md.refreshMovieAll(ms.movieList);
//				for(int i=0; i<newMovie.size(); i++) {
//					ms.movieList.add(newMovie.get(i));
//				}
//				num = 1;//리프레시 조건식을 false로 만들기 위해
			}
			if("00:00:01".equals(ms.setTimer(null,"시간"))&&num==1) {//1초가 되면 
				num=0;//num을 0으로 다시 초기화 ==> while문을 안멈추면서 계속 "00:00:00" 조건을 검사할 수 있도록
			}
			/****************************************************************************
			 * 메세지 듣고 말하는 곳
			 */
			try {
				String msg = (String) ois.readObject();
				ms.jta_log.append(msg+"\n");
				int protocol = 0;
				StringTokenizer st = null;
				if(msg!=null) {
					st = new StringTokenizer(msg,"#");
					protocol = Integer.parseInt(st.nextToken());
				}
				switch(protocol) {
				//******************************************************************************
				case MovieProtocol.LOGIN:{//로그인
					String login_id = st.nextToken();
					String login_pw = st.nextToken();
					System.out.println(login_id+", "+login_pw);
					MemberVO pVO = new MemberVO();
					pVO.setMem_id(login_id);
					pVO.setMem_pw(login_pw);
					pVO.setCommand(ctrl.SELECT_LOGIN);
					MemberVO rVO = ctrl.control(pVO);
					String login_result = rVO.getResult();
					if(login_result==null){ //로그인 성공
						System.out.println();
						this.id = rVO.getMem_id();
						this.nickname = rVO.getMem_nickname();
						String login_msg = MovieProtocol.LOGIN+"#"+id+"#"+nickname;
						this.send(login_msg);
					}else {//로그인 실패시 "-1" or "2"
						String fail_msg = MovieProtocol.LOGIN+"#"+login_result;
						this.send(fail_msg);
					}
				}break;
				case MovieProtocol.LOGOUT:{
					this.send(msg);
				}break;
				//******************************************************************************
				case MovieProtocol.JOIN:{//회원가입 
				//MovieProtocol.JOIN+"#"+name+"#"+id+"#"+pw+"#"+email+"#"+nickName+"#"+birth+"#"+gender;
					MemberVO pVO = new MemberVO();
					pVO.setMem_name(st.nextToken());
					pVO.setMem_id(st.nextToken());
					pVO.setMem_pw(st.nextToken());
					pVO.setMem_email(st.nextToken());
					pVO.setMem_nickname(st.nextToken());
					pVO.setMem_birth(st.nextToken());
					pVO.setMem_gender(st.nextToken());
					pVO.setCommand(ctrl.INSERT_JOIN);
					MemberVO rVO = ctrl.control(pVO);
					String join_result = rVO.getResult();
					String join_msg = MovieProtocol.JOIN+"#"+join_result;
					this.send(join_msg);
				}break;
				//******************************************************************************
				case MovieProtocol.CHECK_ID:{//회원가입-아이디 중복확인
					MemberVO pVO = new MemberVO();
					pVO.setMem_id(st.nextToken());
					pVO.setCommand(ctrl.CHECK_ID);
					MemberVO rVO = ctrl.control(pVO);
					String checkId_result = rVO.getResult();
					String checkId_msg = MovieProtocol.CHECK_ID+"#"+checkId_result;
					this.send(checkId_msg);
					System.out.println(checkId_msg);
				}break;
				//******************************************************************************
				case MovieProtocol.MY_MOVIE:{//회원 예매내역 조회
					//영화이름, 지역, 지점, 상영날짜 시간, 상영관, 좌석, 예매번호
					TicketingVO pVO = new TicketingVO();
					pVO.setMem_id(st.nextToken());
					pVO.setCommand(ctrl.SELECT_TICKET);
					List<TicketingVO> ticket_list = ctrl.control(pVO);
					for(int i=0; i<ticket_list.size(); i++) {
						String movieName = ticket_list.get(i).getMovie_name();
						String loc = ticket_list.get(i).getLoc();
						String theater = ticket_list.get(i).getTheater();
						String date = ticket_list.get(i).getMovie_date();
						String time = ticket_list.get(i).getMovie_time();
						String screen = ticket_list.get(i).getMovie_screen();
						String seat = ticket_list.get(i).getScreen_seat();
						String ticketCode = ticket_list.get(i).getTicketing_code();
						String ticket_msg = MovieProtocol.MY_MOVIE+"#"+movieName+"#"+loc+"#"+theater+"#"+
												date+"#"+time+"#"+screen+"#"+seat+"#"+ticketCode;
						this.send(ticket_msg);
						System.out.println("서버에서 보내주는 예매내역: "+ticket_msg);
					}
				}break;
				//******************************************************************************
				case MovieProtocol.MY_INFO:{//회원 정보조회
					//아이디, 비번, 이름, 닉네임, 생일, 성별, 이메일
					String temppw = st.nextToken();
					MemberVO pVO = new MemberVO();
					pVO.setMem_id(this.id);
					pVO.setMem_pw(temppw);
					pVO.setCommand(ctrl.SELECT_MY);
					MemberVO tempVO = ctrl.control(pVO);
					String result = null;
					result = tempVO.getResult();
					if("2".equals(result)) { //비밀번호가 틀리면
						String myinfo_msg = MovieProtocol.MY_INFO+"#"+result;
						this.send(myinfo_msg);
					} else { //비밀번호가 맞으면 회원정보 조회
						pVO.setCommand(ctrl.SELECT_MY);
						MemberVO rVO = ctrl.control(pVO);
						String id = rVO.getMem_id();
						String pw = rVO.getMem_pw();
						String name = rVO.getMem_name();
						String nickname = rVO.getMem_nickname();
						String birth = rVO.getMem_birth();
						String gender = rVO.getMem_gender();
						String email = rVO.getMem_email();
						String myinfo_msg = MovieProtocol.MY_INFO+"#"+id+"#"+pw+"#"+name+
								"#"+nickname+"#"+birth+"#"+gender+"#"+email;
						this.send(myinfo_msg);
					}
				}break;
				//******************************************************************************
				case MovieProtocol.INFO_UPDATE:{//회원 정보수정
//					넘어오는 토큰
//					String update_msg = MovieProtocol.INFO_UPDATE+"#"+id+"#"+pw+"#"+email+"#"+nickName;
//					this.send(update_msg);//db에 넣어주세요
					MemberVO pVO = new MemberVO();
					pVO.setMem_id(st.nextToken());
					pVO.setMem_pw(st.nextToken());
					pVO.setMem_email(st.nextToken());
					pVO.setMem_nickname(st.nextToken());
					pVO.setCommand(ctrl.UPDATE);
					MemberVO rVO = ctrl.control(pVO);
					String update_result = rVO.getResult();
					String update_msg = MovieProtocol.INFO_UPDATE+"#"+update_result;
					this.send(update_msg);
				}break;
				//******************************************************************************
				case MovieProtocol.SELECT:{//영화정보 받고 싶군여
					//@@@@@이 아이는... 처음 화면을 켰을 때만 호출이 됩니다. 
					//오늘의 영화정보 list 선언부
					/* List<Map<String, Object>> movieList = null;
					 * showMap.put("M_TITLE", rs.getString("M_TITLE"));
						showMap.put("T_LOC", rs.getString("T_LOC"));
						showMap.put("T_NAME", rs.getString("T_NAME"));
						showMap.put("S_DATE", rs.getString("S_DATE"));
						showMap.put("S_TIME", rs.getString("S_TIME"));
						showMap.put("SC_NAME", rs.getString("SC_NAME"));
					 */
					for(int i=0; i<ms.movieList.size(); i++) {
						String age = ms.movieList.get(i).get("M_CERTIF").toString();
						String title = ms.movieList.get(i).get("M_TITLE").toString();
						String loc = ms.movieList.get(i).get("T_LOC").toString();
						String theater = ms.movieList.get(i).get("T_NAME").toString();
						String date = ms.movieList.get(i).get("S_DATE").toString();
						String time = ms.movieList.get(i).get("S_TIME").toString();
						String screen = ms.movieList.get(i).get("SC_NAME").toString();
						System.out.println("상영시간 멀었음, 예약 가능");
						String movielist_msg = MovieProtocol.SELECT+"#"+age+"#"+title+"#"+
								loc+"#"+theater+"#"+date+"#"+time+"#"+screen;
						this.send(movielist_msg);	
					}
				}break;
				//******************************************************************************
//				case MovieProtocol.SELECT_MOVIE:{//영화선택
//					String choiceMovie = st.nextToken();
//					if(choiceMovie!=null){
//						tVO.setMovie_name(choiceMovie);
//					}
//					choiceMoive(MovieProtocol.SELECT_MOVIE);
//				}break;
//				case MovieProtocol.SELECT_LOCAL:{//지역선택
//					String choiceLoc = st.nextToken();
//					if(choiceLoc!=null){
//						tVO.setLoc(choiceLoc);
//					}
//					choiceMoive(MovieProtocol.SELECT_LOCAL);
//				}break;
//				case MovieProtocol.SELECT_SCR:{//지점명선택
//					String choiceTheater = st.nextToken();
//					if(choiceTheater!=null){
//						tVO.setTheater(choiceTheater);
//					}
//					choiceMoive( MovieProtocol.SELECT_SCR);
//				}break;
//				case MovieProtocol.SELECT_DATE:{//관, 시간선택
//					String choiceScr = st.nextToken();
//					String choiceTime = st.nextToken();
//					if(choiceTime!=null&&choiceScr!=null){
//						tVO.setMovie_time(choiceTime);
//						tVO.setMovie_screen(choiceScr);
//					}
//					choiceMoive(MovieProtocol.SELECT_DATE);
//				}break;
				//******************************************************************************
				case MovieProtocol.GET_SEATSTATUS:{//좌석선택
					TicketingVO pVO = new TicketingVO();
					pVO.setTheater(st.nextToken());
					pVO.setMovie_screen(st.nextToken());
					pVO.setMovie_date(st.nextToken());
					pVO.setMovie_time(st.nextToken());
					pVO.setCommand(ctrl.GET_SEATSTATUS);
					List<Map<String, Object>> seatList = ctrl.controlSeat(pVO);
					System.out.println("ServerThread seatList size : "+seatList.size());
					for(Map<String, Object> rmap : seatList) {
						String seatcode = rmap.get("좌석").toString();
						String status = rmap.get("현황").toString();
						String seat_msg = MovieProtocol.GET_SEATSTATUS+"#"+seatcode+"#"+status;
						this.send(seat_msg);
					}
				}break;
				//******************************************************************************
				case MovieProtocol.PAY:{//결제하기
					//아이디, 영화이름, 지역, 지점, 상영관, 날짜 , 시간, 좌석
					pList = new Vector<TicketingVO>();
					TicketingVO pVO = new TicketingVO();
					pVO.setMem_id(st.nextToken());
					pVO.setMovie_name(st.nextToken());
					pVO.setTheater(st.nextToken());
					pVO.setMovie_screen(st.nextToken());
					pVO.setScreen_seat(st.nextToken());
					pVO.setMovie_date(st.nextToken());
					pVO.setMovie_time(st.nextToken());
					pVO.setCommand(ctrl.PAY);
					pList.add(pVO);
					//보냈던 리스트 그대로 반환 result값 따로 없음
					List<TicketingVO> rList = ctrl.control(pList);
					for(TicketingVO tvo : rList) {
						String mem_id 		= tvo.getMem_id();
						String movieName 	= tvo.getMovie_name();
						String theater 		= tvo.getTheater();
						String screen 		= tvo.getMovie_screen();
						String seat 		= tvo.getScreen_seat();
						String date 		= tvo.getMovie_date();
						String time 		= tvo.getMovie_time();
						String pay_msg 		= MovieProtocol.PAY+"#"+mem_id+"#"+movieName+"#"+theater+"#"
											+screen+"#"+seat+"#"+date+"#"+time;
						System.out.println("ServerThread pay_msg : "+pay_msg);
						this.send(pay_msg);
					}
				}break;
				}//end of switch
			
			} catch (ClassNotFoundException ce) {
				System.out.println(ce.toString());
				//e.printStackTrace();
			} catch (IOException e) {
				System.out.println(e.toString());
				//e.printStackTrace();
			}
			
		}
	
	
	}
}