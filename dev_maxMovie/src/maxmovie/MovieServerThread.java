package maxmovie;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.StringTokenizer;

public class MovieServerThread extends Thread{
	
	//id와 nickname 저장
	String id = null;
	String nickname = null;
	
	//영화정보 셋팅할 변수
	TicketingVO tVO = null;
	
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
		//ms.movieList = ctrl.sendAll(ms.movieList);//서버에 3일치 영화정보 저장
		try {
			oos = new ObjectOutputStream(ms.socket.getOutputStream());
			ois = new ObjectInputStream(ms.socket.getInputStream());
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
	
	//영화 시간 조건 메소드
	public int checkTime (String time) {
		int result = 0;
		//[1]스케줄에 있는 영화 시간
		StringTokenizer token = new StringTokenizer(time, ":");//시와 분으로 쪼개기
		char[] scheduledhour = token.nextToken().toCharArray();
		char[] scheduledmin = token.nextToken().toCharArray();
		String sethour = null;
		if(scheduledhour[0]==0){//시 앞에 붙은 0 떼어내기
			sethour = scheduledhour[1]+"";
		}else {
			sethour = scheduledhour[0]+""+scheduledhour[1]; 
		}
		String setmin = null;
		if(scheduledmin[0]==0){//분 앞에 붙은 0 떼어내기
			setmin = scheduledmin[1]+"";
		}else {
			setmin = scheduledmin[0]+""+scheduledmin[1]; 
		}
		int hour = Integer.parseInt(sethour);//시와 분을 string에서 int 형으로 바꾸기
		int min = Integer.parseInt(setmin);
		//[2]현재 시간에서 30분 빼기
		Calendar now = Calendar.getInstance();
		now.add(Calendar.MINUTE, -30);//30분 전까지만 예매가능
		int nowhour = now.get(Calendar.HOUR_OF_DAY);
		int nowmin = now.get(Calendar.MINUTE);
		//[3]비교
		if(hour>=nowhour) {//시가 아직 안지났고
			if(min>=nowmin) {//분이 아직 안지났다면,
				result=1;
			}
		}
		return result;
	}
	
	//영화 선택 리스트 전달 메소드
	public void choiceMoive(int MovieProtocol) {
		for(int i=0; i<ms.movieList.size(); i++) {
			String title = ms.movieList.get(i).get("M_TITLE").toString();
			String loc = ms.movieList.get(i).get("T_LOC").toString();
			String theater = ms.movieList.get(i).get("T_NAME").toString();
			String date = ms.movieList.get(i).get("S_DATE").toString();
			String time = ms.movieList.get(i).get("S_TIME").toString();
			String screen = ms.movieList.get(i).get("SC_NAME").toString();
			int result = checkTime(time);
			if(result==1) {//상영시간이 30분 전이고....
				if(tVO.getMovie_name().equals(title)||
						tVO.getLoc().equals(loc)||
							tVO.getTheater().equals(theater)||
								tVO.getMovie_date().equals(date)||
									tVO.getMovie_time().equals(time)||
										tVO.getMovie_screen().equals(screen)){
					String msg = MovieProtocol+"#"+title+"#"+
							loc+"#"+theater+"#"+date+"#"+time+"#"+screen;
					this.send(msg);	
				}
			}
		}
	}
		
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
					//e.printStackTrace();
				}
				//[2]영화정보 리프레쉬
				Calendar newDay = Calendar.getInstance();//새로운 오늘 날짜정보에서
				//어제날짜 뽑기[형식:20200325]
				newDay.add(Calendar.DAY_OF_MONTH, -1);
				String yesterday = ms.setTimer(newDay, "날짜");
				for(int i=0; i<ms.movieList.size(); i++) {
					/*
					if(yesterday.equals(ms.movieList.get(i).날짜)) {
						영화정보 리스트에서 제거
						ms.movieList.remove(i);
					}			
					//db에 오늘 날짜 보내서 새로운 영화정보 가져오기
					*/			
				}
				num = 1;//리프레시 조건식을 false로 만들기 위해
			}
			if("00:00:01".equals(ms.setTimer(null,"시간"))&&num==1) {//1초가 되면 
				num=0;//num을 0으로 다시 초기화 ==> while문을 안멈추면서 계속 "00:00:00" 조건을 검사할 수 있도록
			}
			/****************************************************************************
			 * 메세지 듣고 말하는 곳
			 */
			try {
				String msg = (String) ois.readObject();
				int protocol = 0;
				StringTokenizer st = null;
				if(msg!=null) {
					st = new StringTokenizer("","#");
					protocol = Integer.parseInt(st.nextToken());
				}
				switch(protocol) {
				case MovieProtocol.LOGIN:{//로그인
					String login_id = st.nextToken();
					String login_pw = st.nextToken();
					MemberVO pVO = new MemberVO();
					pVO.setMem_id(login_id);
					pVO.setMem_pw(login_pw);
					pVO.setCommand(ctrl.SELECT_LOGIN);
					/*
					MemberVO rVO = ctrl.메소드;
					String login_result = rVO.getResult();
					if("".equals(login_result)){//로그인 성공시
						this.id = login_id;
						this.nickName = rVO.getMem_nickname();
						String login_msg = MovieProtocol.LOGIN+"#"+login_result+"#"+nickName;
						this.send(login_msg);
					}else {//로그인 실패시
						String fail_msg = MovieProtocol.LOGIN+"#"+login_result;
						this.send(fail_msg);
					}
					*/
					
				}
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
					/*
					MemberVO rVO = ctrl.메소드;
					String join_result = rVO.getResult();
					String join_msg = MovieProtocol.JOIN+"#"+join_result;
					this.send(msg);
					*/
				}
				case MovieProtocol.CHECK_ID:{//회원가입-아이디 중복확인
					MemberVO pVO = new MemberVO();
					pVO.setMem_id(st.nextToken());
					pVO.setCommand(ctrl.CHECK_ID);
					/*
					MemberVO rVO = ctrl.메소드;
					String checkId_result = rVO.getResult();
					String checkId_msg = MovieProtocol.CHECK_ID+"#"+checkId_result;
					*/		
				}
				case MovieProtocol.MY_MOVIE:{//회원 예매내역 조회
					//영화이름, 지역, 지점, 상영날짜 시간, 상영관, 좌석, 예매번호
					TicketingVO pVO = new TicketingVO();
					pVO.setMem_id(st.nextToken());
					pVO.setCommand(ctrl.SELECT_TICKET);
					/*
					List<TicketingVO> ticket_list = ctrl.메소드;
					for(int i=0; i<ticket_list.size(); i++) {
						String movieName = ticket_list.get(i).getMovie_name();
						String loc = ticket_list.get(i).getLoc();
						String theater = ticket_list.get(i).getTheater();
						String date = ticket_list.get(i).getMovie_date();
						String time = ticket_list.get(i).getMovie_time();
						String screen = ticket_list.get(i).getMovie_screen();
						String seat = ticket_list.get(i).getScreen_seat();
						String ticketCode = ticket_list.get(i).getTicketting_code();
						String ticket_msg = MovieProtocol.MY_MOVIE+"#"+movieName+"#"+loc+"#"+theater+"#"+
												date+"#"+time+"#"+screen+"#"+seat+"#"+ticketCode+"#"+ticketCode;
						this.send(ticket_msg);
					}
					*/
				}
				case MovieProtocol.MY_INFO:{//회원 정보조회
					//아이디, 비번, 이름, 닉네임, 생일, 성별, 이메일
					MemberVO pVO = new MemberVO();
					pVO.setMem_id(this.id);
					pVO.setMem_pw(st.nextToken());
					pVO.setCommand(ctrl.SELECT_MY);
					/*
					MemberVO rVO = ctrl.메소드;
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
					*/
				}
				case MovieProtocol.INFO_UPDATE:{//회원 정보수정
					//아이디, 비번, 이름, 닉네임, 생일, 성별, 이메일
					MemberVO pVO = new MemberVO();
					pVO.setMem_id(st.nextToken());
					pVO.setMem_pw(st.nextToken());
					pVO.setMem_name(st.nextToken());
					pVO.setMem_nickname(st.nextToken());
					pVO.setMem_birth(st.nextToken());
					pVO.setMem_gender(st.nextToken());
					pVO.setMem_email(st.nextToken());
					pVO.setCommand(ctrl.UPDATE);
					/*
					MemberVO rVO = ctrl.메소드;
					String update_result = rVO.getResult();
					String update_msg = MovieProtocol.MY_INFO+"#"+update_result;
					this.send(update_msg);
					*/
				}
				case MovieProtocol.SELECT:{//MovieChoiceView 호출
					//오늘의 영화정보 list 선언부
					/* List<Map<String, Object>> movieList = null;
					 * showMap.put("M_TITLE", rs.getString("M_TITLE"));
						showMap.put("T_LOC", rs.getString("T_LOC"));
						showMap.put("T_NAME", rs.getString("T_NAME"));
						showMap.put("S_DATE", rs.getString("S_DATE"));
						showMap.put("S_TIME", rs.getString("S_TIME"));
						showMap.put("SC_NAME", rs.getString("SC_NAME"));
					 */
					tVO = null;
					tVO = new TicketingVO();
					for(int i=0; i<ms.movieList.size(); i++) {
						String age = ms.movieList.get(i).get("M_AGE").toString();
						String title = ms.movieList.get(i).get("M_TITLE").toString();
						String loc = ms.movieList.get(i).get("T_LOC").toString();
						String theater = ms.movieList.get(i).get("T_NAME").toString();
						String date = ms.movieList.get(i).get("S_DATE").toString();
						String time = ms.movieList.get(i).get("S_TIME").toString();
						String screen = ms.movieList.get(i).get("SC_NAME").toString();
						int result = checkTime(time);//상영 시간 30분 전까지만 예약이 가능하도록 하는 조건.....
						if(result==1) {
							System.out.println("상영시간 멀었음, 예약 가능");
							String movielist_msg = MovieProtocol.SELECT+"#"+age+"#"+title+"#"+
									loc+"#"+theater+"#"+date+"#"+time+"#"+screen;
							this.send(movielist_msg);	
						}
					}
				}
				case MovieProtocol.SELECT_MOVIE:{//영화선택
					String choiceMovie = st.nextToken();
					if(choiceMovie!=null){
						tVO.setMovie_name(choiceMovie);
					}
					choiceMoive(MovieProtocol.SELECT_MOVIE);
				}
				case MovieProtocol.SELECT_LOCAL:{//지역선택
					String choiceLoc = st.nextToken();
					if(choiceLoc!=null){
						tVO.setLoc(choiceLoc);
					}
					choiceMoive(MovieProtocol.SELECT_LOCAL);
				}
				case MovieProtocol.SELECT_SCR:{//지점명선택
					String choiceTheater = st.nextToken();
					if(choiceTheater!=null){
						tVO.setTheater(choiceTheater);
					}
					choiceMoive( MovieProtocol.SELECT_SCR);
				}
				case MovieProtocol.SELECT_DATE:{//관, 시간선택
					String choiceScr = st.nextToken();
					String choiceTime = st.nextToken();
					if(choiceTime!=null&&choiceScr!=null){
						tVO.setMovie_time(choiceTime);
						tVO.setMovie_screen(choiceScr);
					}
					choiceMoive(MovieProtocol.SELECT_DATE);
				}
				case MovieProtocol.SELECT_SEAT:{//좌석선택
					TicketingVO pVO = tVO;
					pVO.setCommand(ctrl.SELECT_SEAT);
					/*
					List<Map<String, Object>> seatlist = ctrl.메소드();
					for(int i=0; i<seatlist.size(); i++) {
						String seatNum = seatlist.get(i).get("좌석번호");
						String seatState = seatlist.get(i).get("예약번호");
						String seat_msg = MovieProtocol.SELECT_SEAT+"#"+seatNum+"#"+seatState;
						this.send(seat_msg);
					}
					*/
				}
				case MovieProtocol.PAY:{//결제하기
					//아이디, 영화이름, 지역, 지점, 상영관, 날짜 , 시간, 좌석
					TicketingVO pVO = new TicketingVO();
					pVO.setMem_id(st.nextToken());
					pVO.setMovie_name(st.nextToken());
					pVO.setLoc(st.nextToken());
					pVO.setTheater(st.nextToken());
					pVO.setMovie_screen(st.nextToken());
					pVO.setMovie_date(st.nextToken());
					pVO.setMovie_time(st.nextToken());
					pVO.setScreen_seat(st.nextToken());
					/*
					pVO.setCommand(ctrl.결제);
					TicketingVO rVO = ctrl.메소드();
					String result = rVO.getResult();
					*/
				}
				}
			} catch (ClassNotFoundException e) {
				System.out.println(e.toString());
				//e.printStackTrace();
			} catch (IOException e) {
				System.out.println(e.toString());
				//e.printStackTrace();
			}
		}	
	}
	
	
}
