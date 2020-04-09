package maxmovie;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;


import com.sun.mail.iap.Protocol;


public class EventMapping implements ActionListener, ItemListener, KeyListener, MouseListener{

	
	/* 뷰 패널들의 변수이름
	//로그인뷰
	JPanel 					jp_lv 			= new LoginView(this);
	//마이페이지뷰
	JPanel 					jp_mv 			= new MyPageView(this);
	//무비초이스뷰
	JPanel 					jp_mcv 			= new MovieChoiceView(this);
	//시트초이스뷰
	JPanel 					jp_scv 			= new SeatChoiceView(this);
	//리절트뷰
	JPanel 					jp_rv 			= new ResultView(this);
	 */
	
	/* 마이페이지 뷰의 변수 이름
	 * MemInfoView miv = new MemInfoView();
		MemUpdateView muv = new MemUpdateView();
		TicketHistoryView thv = new TicketHistoryView();
	*/
	
	//회원 아이디 및 닉네임
	//String myid = null;
	//String mynickname = null;
	
	//영화선택 정보 저장
	//영화정보 셋팅할 변수
	TicketingVO tVO = new TicketingVO();
	
	//필요한 주소값 선언부
	MaxMovieView mmv = null;
	MovieController ctrl = new MovieController();
	JoinView jv = null;
	
	//회원가입 목록별 기준체크를 위한 선언부 (회원정보 수정 포함)
	int id = 0;
	int pw = 0;
	int name = 0;
	int nickName = 0;
	int gender = 0;
	int birth = 0;
	int email = 0;
	int email_r = 0;
	
	//인증메일을 위한 선언부
	SendMail sm = null;
	long start_millisecond = 0;//메일 보낸 시간
	long end_millisecond = 0;//인증번호 입력된 시간
	String beforeEmail = null;
	String afterEmail = null;
	
	//생성자
	public EventMapping(MaxMovieView mmv) {
		this.mmv = mmv;
	}
	
	//서버스레드에게 말하기 위한 메소드 
	public void send(String msg) {
		try {
			mmv.oos.writeObject(msg);
		} catch (Exception e) {
			System.out.println(e.toString());
			e.printStackTrace();
		}
	}
	
	/**************************************************************************************************
	 * 영화 선택을 위한 메소드들
	 */
	//영화 시간 조건 메소드
	public int checkTime (String time) {//스케줄에 있는 영화 시간 받아서
		int result = 0;
		StringTokenizer token = new StringTokenizer(time, ":");//시와 분으로 쪼개기
		char[] scheduledhour = token.nextToken().toCharArray();
		char[] scheduledmin = token.nextToken().toCharArray();
		String sethour = null;
		String setmin = null;
		if(scheduledhour[0]==0){//시 앞에 붙은 0 떼어내기
			sethour = scheduledhour[1]+"";
		}else {
			sethour = scheduledhour[0]+""+scheduledhour[1]; 
		}
		if(scheduledmin[0]==0){//분 앞에 붙은 0 떼어내기
			setmin = scheduledmin[1]+"";
		}else {
			setmin = scheduledmin[0]+""+scheduledmin[1]; 
		}
		int hour = Integer.parseInt(sethour);//시와 분을 string에서 int 형으로 바꾸기
		int min = Integer.parseInt(setmin);
		Calendar now = Calendar.getInstance();//현재 시간에서 
		now.add(Calendar.MINUTE, -30);//30분 빼기 => 30분 전까지만 예매가능
		int nowhour = now.get(Calendar.HOUR_OF_DAY);
		int nowmin = now.get(Calendar.MINUTE);
		if(hour>=nowhour) {//시가 아직 안지났고
			if(min>=nowmin) {//분이 아직 안지났다면,
				result=1;
			}
		}
		return result;
	}
	
	//영화 선택 메소드
	public List<Map<String, Object>> choiceMoive(TicketingVO tVO) {
		List<Map<String, Object>> user_list = new Vector<Map<String,Object>>();
		Map<String, Object> umap = null;
		for(int i=0; i<mmv.movieList.size(); i++) {
			String m_title = mmv.movieList.get(i).get("M_TITLE").toString();
			String m_age = mmv.movieList.get(i).get("M_CERTIF").toString();
			String m_loc = mmv.movieList.get(i).get("T_LOC").toString();
			String m_theater = mmv.movieList.get(i).get("T_NAME").toString();
			String m_date = mmv.movieList.get(i).get("S_DATE").toString();
			String m_time = mmv.movieList.get(i).get("S_TIME").toString();
			String m_screen = mmv.movieList.get(i).get("SC_NAME").toString();
			int result = checkTime(m_time);
			if(result==1) {//상영시간이 30분 전이고....
				if(tVO.getMovie_name()==null) {
					tVO.setMovie_name(m_title);
				}
				if(tVO.getMovie_age()==null) {
					tVO.setMovie_age(m_age);
				}
				if(tVO.getLoc()==null) {
					tVO.setLoc(m_loc);
				}
				if(tVO.getTheater()==null) {
					tVO.setTheater(m_theater);
				}
				if(tVO.getMovie_date()==null) {
					tVO.setMovie_date(m_date);
				}
				if(tVO.getMovie_time()==null) {
					tVO.setMovie_time(m_time);
				}
				if(tVO.getMovie_screen()==null) {
					tVO.setMovie_screen(m_screen);
				}
//				if(tVO.getMovie_name().equals(title)&&
//						tVO.getLoc().equals(loc)&&
//							tVO.getTheater().equals(theater)&&
//								tVO.getMovie_date().equals(date)&&
//									tVO.getMovie_time().equals(time)&&
//										tVO.getMovie_screen().equals(screen)){
//					String msg = MovieProtocol+"#"+tVO.getMovie_name()+"#"+tVO.getMovie_age()+"#"+
//							tVO.getLoc()+"#"+tVO.getTheater()+"#"+tVO.getMovie_date()+"#"+tVO.getMovie_time()+"#"+tVO.getMovie_screen();
//					this.send(msg);	
//
//				}
				umap = new HashMap<String, Object>();
				umap.put("M_TITLE", tVO.getMovie_name());
				umap.put("M_CERTIF", tVO.getMovie_age());
				umap.put("T_LOC", tVO.getLoc());
				umap.put("T_NAME",tVO.getTheater());
				umap.put("S_DATE", tVO.getMovie_date());
				umap.put("S_TIME", tVO.getMovie_time());
				umap.put("SC_NAME", tVO.getMovie_screen());
				user_list.add(umap);
			}
		}//for문 끝
		return user_list;
	}
	
	//날짜 셋팅 메소드
	public void dateDtm() {
		mmv.jp_mrv.jp_mcv.dtm_date.setRowCount(0);
		Calendar today = Calendar.getInstance();
		//today.add(Calendar.MONTH, -3);
		//today.add(Calendar.DAY_OF_MONTH, -10);
		String before_year ="";
		String before_month = "";
		Vector<String> nowdate = null;
		for(int i=0; i<20; i++) {
			int year = today.get(Calendar.YEAR);
			String after_year = Integer.toString(year);
			if(!before_year.equals(after_year)) {
				nowdate = new Vector<String>();
				nowdate.add(year+"년");
				mmv.jp_mrv.jp_mcv.dtm_date.addRow(nowdate);
				before_year = after_year;
			}
			int month = today.get(Calendar.MONTH)+1;
			String after_month = Integer.toString(month);
			if(!before_month.equals(after_month)) {
				nowdate = new Vector<String>();
				nowdate.add(after_month+"월");
				mmv.jp_mrv.jp_mcv.dtm_date.addRow(nowdate);
				before_month = after_month;
			}
			String day = Integer.toString(today.get(Calendar.DAY_OF_MONTH));
			nowdate = new Vector<String>();
			nowdate.add(day+"일");
			today.add(Calendar.DAY_OF_MONTH, +1);
			mmv.jp_mrv.jp_mcv.dtm_date.addRow(nowdate);
		}
		//조건추가
	}
		
	/**************************************************************************************************
	 * 회원가입 기준체크를 위한 메소드들.....
	 */
	//기준 모두 초기화 메소드
	public void refreshCheck() {
		this.id = 0;
		this.pw = 0;
		this.name = 0;
		this.nickName = 0;
		this.gender = 0;
		this.birth = 0;
		this.email = 0;
		this.email_r = 0;
		this.start_millisecond = 0;
		this.end_millisecond = 0;
		this.beforeEmail = null;
		this.afterEmail = null;
	}
	
	//아이디 기준체크 메소드
	public int checkId (String inputId) {
		int sum = 0;
		if(7<=inputId.length()&&inputId.length()<=12) {//문자 길이가 7~12니?
			char[] ids = inputId.toCharArray();//스트링을 쪼개서
			int[] result = new int[inputId.length()];
			for(int i=0; i<inputId.length(); i++) {
				int cha = (int)ids[i];
				if(33<=cha&&cha<=126) {//영문, 숫자, 특수문자니?
					if((33<=cha&&cha<=47)||(58<=cha&&cha<=64)||
							(91<=cha&&cha<=96)||(123<=cha&&cha<=126)) {//특수문자니? 문제군
						result[i]=1;
					}else {//영문, 숫자니? 좋아
						result[i]=0;
					}
				}else {//공백, 한글이니? 문제야
					result[i]=1;
				}
			}
			for(int a:result) {
				sum+= a;
			}
		}else {//문자길이가 기준에 맞지 않군
			sum = inputId.length();
		}
		return sum;
	}
	
	//패스워드 스트링으로 만들어주는 메소드
	public String pwToString (char[] password) {
		StringBuilder sb = new StringBuilder();
		for(char cha: password) {
			Character.toString(cha);
			sb.append(cha);
			System.out.println(cha);
		}
		return sb.toString();
	}
	
	//이름, 닉네임 기준체크 메소드
	public int checkNames(String inputnames, int s, int e) {
		System.out.println(inputnames);
		int sum =0;
		if(s<=inputnames.length()&&inputnames.length()<=e) {//문자길이 맞니?
			char[] names = inputnames.toCharArray();//스트링을 쪼개서
			int[] result = new int[inputnames.length()];
			for(int i=0; i<inputnames.length(); i++) {
				int cha = (int)names[i];
				if((33<=cha&&cha<=47)||(58<=cha&&cha<=64)||
							(91<=cha&&cha<=96)||(123<=cha&&cha<=126)) {//특수문자니? 문제군
					result[i]=1;
				}else {
					result[i]=0;
				}
			}
			for(int a:result) {
				sum+= a;
				System.out.println(a);
			}
		}else {
			sum = inputnames.length();
		}
		System.out.println("sum: "+sum);
		return sum;
	}
	
	//비번 기준체크 메소드
	public int checkPw(String inputpw, int s, int e) {//비밀번호는 알아서 영문으로 입력됩니다
		int sum =0;
		if(s<=inputpw.length()&&inputpw.length()<=e) {//문자길이 맞니?
			char[] names = inputpw.toCharArray();//스트링을 쪼개서
			int[] result = new int[inputpw.length()];
			for(int i=0; i<inputpw.length(); i++) {
				int cha = (int)names[i];
				if(33<=cha&&cha<=126) {//영문, 숫자니?				
					result[i]=0;
				}else {//한글, 공백이니? 문제군
					result[i]=1;
				}
			}
			for(int a:result) {
				sum+= a;
			}
		}else {
			sum = inputpw.length();
		}
		return sum;
	}
	
	//생년월일 기준체크 메소드
	/*
	 * jl_year_warning.setText(" 생년을 선택하여주세요");
		jl_month_warning.setText(" 생월을 선택하여주세요");
		jl_day_warning.setText(" 생일을 선택하여주세요");
	 */
	public void birth_checking() {
		if("년도".equals(jv.choiceYear)) {
			jv.jl_year_warning.setVisible(true);
			jv.jl_month_warning.setVisible(false);
			jv.jl_day_warning.setVisible(false);
			birth = 0;
		}else {
			if("월".equals(jv.choiceMonth)) {
				jv.jl_year_warning.setVisible(false);
				jv.jl_month_warning.setVisible(true);
				jv.jl_day_warning.setVisible(false);
				birth = 0;
			}else{
				if("일".equals(jv.choiceDay)) {
					jv.jl_year_warning.setVisible(false);
					jv.jl_month_warning.setVisible(false);
					jv.jl_day_warning.setVisible(true);
					birth = 0;
				}else {
					jv.jl_year_warning.setVisible(false);
					jv.jl_month_warning.setVisible(false);
					jv.jl_day_warning.setVisible(false);
					birth = 1;
				}
			}
		}
	}
	
	//이메일 기준 체크 메소드
	/*
	 * 로컬부분:대문자A~Z,소문자a~z,숫자0~9,특수문자!#$%&'*+-/=?^_{|}~,(.은사용가능하나첫번째, 마지막아니어야됨) 
	 도메인부분 : 대문자 A~Z, 소문자 a~z, 숫자 0~9, 하이픈-(첫번째 또는 마지막 아니어야됨)
	 정의) 로컬부분 + @ + 도메인부분 (상세하게 하면 매우다르지만 일반적으로 이정도) 
	 */
	public static int checkEmail(String inputemail) {
		String local = null;
		String domain = null;
		StringTokenizer st = null;
		int sum = 0;
		int sum2 = 0;
		if(inputemail.length()==0) {//이메일을 적지 않았다면,
			sum = 1;
			sum2 = 1;
		}else {//이메일을 적었다면,
			//먼저 @ 갯수를 확인할게
			char[] emails = inputemail.toCharArray();
			int [] check = new int[inputemail.length()];
			int checking = 0;
			for(int i =0; i<inputemail.length(); i++) {
				int email = emails[i];
				if(email==64) {
					checking+= 1;
				}
			}
			if(checking>1) {//@가 여러개니?
				sum = 1;
				sum2 = 1;
			}else {//@가 하나니?
				st = new StringTokenizer(inputemail,"@");//그럼 이제 로컬과 도메인 검사 할게
				local = st.nextToken();
				if(st.hasMoreTokens()) {//@가 있다면,
					domain = st.nextToken();
					if(domain!=null) {
						//로컬 검사
						char[] locals = local.toCharArray();
						int [] result = new int[local.length()];
						if(locals[0]==46||locals[local.length()-1]==46) {//.이 첫번째 혹은 마지막이니?
							sum = local.length();
						}else {//아니라면 다음 기준 간다
							for(int i=0; i<local.length(); i++) {
								int loc = (int)locals[i];
								if(33<=loc&&loc<=126){//영문, 숫자, 특수문자 니?
									if(loc==34||loc==40||loc==41||
											loc==44||loc==46||loc==58||loc==59||
											loc==60||loc==62||loc==64||(91<=loc&&loc<=96)) {
										result[i] = 1;//문제군
									}else {//특수문자 !#$%&'*+-/=?^_{|}~. 니?
										result[i] = 0;//좋아
									}
								}else {//아니니?
									result[i] = 1;//문제군
								}
							}
							for(int a:result) {
								sum+= a;
							}
						}
						//도메인 검사
						char[] domains = domain.toCharArray();
						int [] result2 = new int[domain.length()];
						if(domains[0]==45||domains[domain.length()-1]==45) {//-이 첫번째 혹은 마지막이니?
							sum2 = domain.length();
						}else {//아니라면 다음 기준 간다
							for(int i=0; i<domain.length(); i++) {
								int dom = (int)domains[i];
								if((48<=dom&&dom<=57)||(65<=dom&&dom<=90)||(97<=dom&&dom<=122)||dom==45||dom==46) {//영문, 숫자, -니?
									result2[i] = 0;//좋아
								}else {//아니니?
									result2[i] = 1;//문제군
								}
							}
							for(int a:result2) {
								sum2+= a;
							}
						}
					}else {//domain==null 이라면,
						sum = local.length();
						sum2 = 1;
					}
				}else {//@이 하나도 없니?
					sum = local.length();
					sum2 = 1;
				}
			}
		}
		return sum+sum2;
	}
	
	/**************************************************************************************************
	 * ActionListener(로그인,회원가입,회원가입-이메일,마이페이지,화면전환)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();
		
		//로그인 -----------------------------------------------------------------------------------------
		if(obj==mmv.jp_lv.jbt_login) {//로그인이 하고 싶어요
			System.out.println("클릭");
			mmv.mem_id = mmv.jp_lv.jtf_id.getText(); //아이디 전역변수 저장
			String login_id = mmv.jp_lv.jtf_id.getText();
			String login_pw = pwToString(mmv.jp_lv.jpf_pw.getPassword());
			String login_msg = MovieProtocol.LOGIN+"#"+login_id+"#"+login_pw;
			send(login_msg);//아이디,비번 검사해주세요
		}
		
		//회원가입 ----------------------------------------------------------------------------------------
		else if(obj==mmv.jp_lv.jbt_join) {//회원가입하고 싶니?
			jv = new JoinView(this);//화면띄워줄게
		}
		else if(jv!=null&&obj==jv.jbt_joingo) {//드디어 회원가입 버튼을 눌렀군
			//회원가입을 위한 기준들 마지막 체크 
			afterEmail = jv.jtf_email.getText();
			if(afterEmail!=null) {
				if(afterEmail.equals(beforeEmail)) {
					email_r = 1;								
				}
			}
			int sum = id + pw + name + nickName + birth + email + email_r + gender;
			if(sum==8) {//모든 기준 통과시, 모든 값들 가져와서
				String name = jv.jtf_name.getText();
				String id = jv.jtf_id.getText();
				String pw = pwToString(jv.jpf_pw.getPassword());
				String email = jv.jtf_email.getText();
				String nickName = jv.jtf_nick.getText();
				if(jv.choiceMonth.length()==1) {//생년월일 - "월,일" 1자리면 앞에 0 붙여주기
					System.out.println("월 "+jv.choiceMonth);
					jv.choiceMonth = "0"+jv.choiceMonth;
				}
				if(jv.choiceDay.length()==1) {
					System.out.println("일"+jv.choiceDay);
					jv.choiceDay = "0"+jv.choiceDay;
				}
				String birth = jv.choiceYear+""+jv.choiceMonth+""+jv.choiceDay;//[형식]19960218
				String gender = jv.jcb_genderChoice;
				//서버스레드로 메세지 전송
				String join_msg = MovieProtocol.JOIN+"#"+name+"#"+id+"#"+pw+"#"+email+"#"+nickName+"#"+birth+"#"+gender;
				send(join_msg);//db에 넣어주세요
				System.out.println(join_msg);
			}else {
				JOptionPane.showMessageDialog(jv, "입력한 정보가 부적합합니다. 다시 확인해주세요.");
			}
		}
		else if(jv!=null&&obj==jv.jbt_back) {//회원가입 화면을 나가고 싶니?
			jv.dispose();//okay bye...
		}
		//아이디 -----------------------------------------------------------------------------------------
		/*
		 * jl_id_warning.setText(" 동일한 아이디가 존재합니다.");
		jl_id_success.setText(" 사용 가능한 아이디입니다.");
		jl_id_warning2.setText(" 7~12자이어야하고 특수문자는 입력할 수 없습니다.");
		 */
		else if(jv!=null&&obj==jv.jbt_id_check) {//아이디 중복 체크하고 싶니?
			jv.jl_id_warning.setVisible(false);
			jv.jl_id_warning2.setVisible(false);
			jv.jl_id_success.setVisible(false);
			//일단 기준 체크좀 할게
			String inputId = jv.jtf_id.getText();
			int result = checkId(inputId);
			if(result>0) {//기준이 안맞네
				jv.jl_id_warning2.setVisible(true);
				id = 0;
			}else {//기준통과했다면, 이제 중복 체크 해줄게
				String chektid_msg = MovieProtocol.CHECK_ID+"#"+inputId;
				send(chektid_msg);//중복체크해주세요
			}
		}
		
		//이메일 -----------------------------------------------------------------------------------------
		/*
		 * jl_email_warning.setText(" 이메일주소 형식에 맞지 않습니다.");
		jl_email_warning2.setText(" 인증번호 입력시간은 2분입니다.");
		jl_email_r_success.setText(" 인증성공");
		jl_email_r_warning.setText(" 인증번호가 일치하지 않습니다.");
		jl_email_r_warning2.setText(" 입력시간이 초과했습니다.");
		 */
		else if(jv!=null&&obj==jv.jbt_email) {//이메일 입력버튼 눌렀니?
			String inputemail = jv.jtf_email.getText();
			if(inputemail!=null) {
				beforeEmail = inputemail;
				jv.jl_email_warning.setVisible(false);
				jv.jl_email_r_warning.setVisible(false);
				jv.jl_email_r_warning2.setVisible(false);
				jv.jl_email_r_success.setVisible(false);
				//기준 검사를 먼저 할게
				int result = checkEmail(inputemail);
				if(result>0) {//이메일 형식에 안맞다면
					jv.jl_email_warning.setVisible(true);
					email = 0;
				}else {//이메일형식이 맞다면
					jv.jl_email_warning.setVisible(false);
					email = 1;
					try {//메일 발송
						sm = new SendMail(inputemail);
						jv.jl_email_warning2.setVisible(true);//입력시간 2분이라는 알림
						start_millisecond = System.currentTimeMillis();//메일 보낸시간 저장
					} catch (UnsupportedEncodingException ee) {
						System.out.println(ee.toString());
						//e.printStackTrace();
					}
				}
			}else {
				jv.jl_email_warning.setVisible(true);
				email = 0;
			}
		}
		else if(jv!=null&&obj==jv.jbt_email_r) {//인증번호 버튼을 눌렀니?
			
			end_millisecond = System.currentTimeMillis();//인증번호 입력시간 저장
			long term = end_millisecond - start_millisecond;//전송~입력 시간 계산
			if(term<120000) {//2분안에 입력한다면
				String inputNum = jv.jtf_email_r.getText();
				if(sm.rnum!=null) {
					if(sm.rnum.equals(inputNum)) {//인증번호가 일치시
						jv.jl_email_r_success.setVisible(true);
						jv.jl_email_r_warning.setVisible(false);
						jv.jl_email_warning2.setVisible(false);
						
					}
					else {//인증번호가 일치하지 않다면
						jv.jl_email_r_warning.setVisible(true);
						jv.jtf_email_r.setText("");
						email_r = 0;
					}
				}
			}
			else {//입력시간이 초과했다면
				jv.jl_email_r_warning2.setVisible(true);
				jv.jtf_email_r.setText("");
				email_r = 0;
				sm.rnum = null;
			}
		}
		/************************************************************************************************
		 * 마이페이지뷰
		 */
		else if(obj==mmv.jp_mv.jp_miv.jbt_modified) {//회원정보 수정버튼 => 일단 회원정보 조회가 됨
			String pw = pwToString(mmv.jp_mv.jp_miv.jpf_pw.getPassword());//비번을 입력했어요
			if(pw.length()>0) {
				String info_msg = MovieProtocol.MY_INFO+"#"+pw; //DAO 구현필요
				this.send(info_msg);
			}else {
				JOptionPane.showMessageDialog(mmv.jp_mv.jp_miv, "비밀번호를 입력해주세요.");
			}
		}
		else if(obj==mmv.jp_mv.jp_muv.jbt_back) {//다시 뒤로! miv로
			String mypageView_msg = MovieProtocol.MY_MOVIE+"#"+mmv.mem_id;
			this.send(mypageView_msg);
		}
		else if(obj==mmv.jp_mv.jp_muv.jbt_email) {//마이페이지 회원정보 수정- 이메일 입력버튼 눌렀니?
			String inputemail = mmv.jp_mv.jp_muv.jtf_email.getText();
			if(inputemail!=null) {
				beforeEmail = inputemail;
				mmv.jp_mv.jp_muv.jl_email_warning.setVisible(false);
				mmv.jp_mv.jp_muv.jl_email_r_warning.setVisible(false);
				mmv.jp_mv.jp_muv.jl_email_r_warning2.setVisible(false);
				mmv.jp_mv.jp_muv.jl_email_r_success.setVisible(false);
				//기준 검사를 먼저 할게
				int result = checkEmail(inputemail);
				if(result>0) {//이메일 형식에 안맞다면
					mmv.jp_mv.jp_muv.jl_email_warning.setVisible(true);
					email = 0;
				}else {//이메일형식이 맞다면
					mmv.jp_mv.jp_muv.jl_email_warning.setVisible(false);
					email = 1;
					try {//메일 발송
						sm = new SendMail(inputemail);
						mmv.jp_mv.jp_muv.jl_email_warning2.setVisible(true);//입력시간 2분이라는 알림
						start_millisecond = System.currentTimeMillis();//메일 보낸시간 저장
					} catch (UnsupportedEncodingException ee) {
						System.out.println(ee.toString());
						//e.printStackTrace();
					}
				}
			}else {
				mmv.jp_mv.jp_muv.jl_email_warning.setVisible(true);
				email = 0;
			}
		}
		else if(obj==mmv.jp_mv.jp_muv.jbt_email_r) {//마이페이지 회원정보 수정- 인증번호 버튼을 눌렀니?
			end_millisecond = System.currentTimeMillis();//인증번호 입력시간 저장
			long term = end_millisecond - start_millisecond;//전송~입력 시간 계산
			if(term<120000) {//2분안에 입력한다면
				String inputNum =mmv.jp_mv.jp_muv.jtf_email_r.getText();
				if(sm.rnum!=null) {
					if(sm.rnum.equals(inputNum)) {//인증번호가 일치시
						mmv.jp_mv.jp_muv.jl_email_r_success.setVisible(true);
						mmv.jp_mv.jp_muv.jl_email_r_warning.setVisible(false);
						mmv.jp_mv.jp_muv.jl_email_warning2.setVisible(false);
					}
					else {//인증번호가 일치하지 않다면
						mmv.jp_mv.jp_muv.jl_email_r_warning.setVisible(true);
						mmv.jp_mv.jp_muv.jtf_email_r.setText("");
						email_r = 0;
					}
				}
			}
			else {//입력시간이 초과했다면
				mmv.jp_mv.jp_muv.jl_email_r_warning2.setVisible(true);
				mmv.jp_mv.jp_muv.jtf_email_r.setText("");
				email_r = 0;
				sm.rnum = null;
			}
		}
		else if(obj==mmv.jp_mv.jp_muv.jbt_modifiedGo) {//정보수정버튼 클릭
			//회원정보 수정을 위한 기준들 마지막 체크 
			afterEmail = mmv.jp_mv.jp_muv.jtf_email.getText();
			if(afterEmail!=null) {
				if(afterEmail.equals(beforeEmail)) {
					email_r = 1;								
				}
			}
			int sum = pw + nickName + email + email_r;
			System.out.println("pw "+pw);
			System.out.println("nickName "+nickName);
			System.out.println("email"+email);
			System.out.println("email_r "+email_r);
			System.out.println(sum);
			if(sum==4) {//모든 기준 통과시, 모든 값들 가져와서
				String id = mmv.mem_id;
				String pw = pwToString(mmv.jp_mv.jp_muv.jpf_pw.getPassword());
				String email = mmv.jp_mv.jp_muv.jtf_email.getText();
				String nickName = mmv.jp_mv.jp_muv.jtf_nick.getText();
				//서버스레드로 메세지 전송
				String update_msg = MovieProtocol.INFO_UPDATE+"#"+id+"#"+pw+"#"+email+"#"+nickName;
				this.send(update_msg);//db에 넣어주세요
				//refreshCheck();//기준모두초기화@@@@@@@
				//jv.dispose();//가입화면 닫기
			}else {
				JOptionPane.showMessageDialog(mmv.jp_mv.jp_muv, "입력한 정보가 부적합합니다. 다시 확인해주세요.");
			}
		}
		/************************************************************************************************
		 * 화면전환
		 */
		else if(obj==mmv.jbt_logout) {//로그아웃 하려고?
			System.out.println("로그아웃 호출");
			String logout_msg = MovieProtocol.LOGOUT+"#"+mmv.mem_id;
			this.send(logout_msg);
		}
		else if(obj==mmv.jbt_myPage||obj==mmv.jp_mv.jbt_thv) {//마이페이지로 가보자 => 영화예매내역
			if (mmv.jp_mv.jp_thv.dtm_history.getRowCount() > 0) {
			    for (int i = mmv.jp_mv.jp_thv.dtm_history.getRowCount() - 1; i > -1; i--) {
			    	mmv.jp_mv.jp_thv.dtm_history.removeRow(i);
			    }
			}
			String mypageView_msg = MovieProtocol.MY_MOVIE+"#"+mmv.mem_id;
			this.send(mypageView_msg);
		}
		else if(obj==mmv.jbt_ticketing) {//예매를 하고 싶구나
			mmv.jp_lv.setVisible(false);//로그인
			mmv.jp_mv.setVisible(false);//마이페이지-틀뷰
			mmv.jp_mv.jp_miv.setVisible(false);//마이페이지-비밀번호입력뷰
			mmv.jp_mv.jp_muv.setVisible(false);//마이페이지-회원정보수정뷰
			mmv.jp_mv.jp_thv.setVisible(false);//마이페이지-영화내역뷰
			mmv.jp_mrv.setVisible(true);//영화예매-틀뷰
			mmv.jp_mrv.jp_mcv.setVisible(true);//영화예매-영화선택뷰
			mmv.jp_mrv.jp_scv.setVisible(false);//영화예매-좌석선택뷰
			mmv.jp_mrv.jp_pv.setVisible(false);//영화예매-결제뷰
			mmv.jp_rv.setVisible(false);//결과화면
			//dtm 설정 메소드 호출@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
			//mmv.em.choiceMoive(title, age, loc, theater, date, time, screen);
		}
		else if(obj==mmv.jp_mv.jbt_miv) {//마이페이지에서 회원정보조회버튼
			System.out.println("회원정보버튼 클릭");
			mmv.jp_mv.jp_miv.jl_mem_id.setText(mmv.mem_id);
			mmv.jp_lv.setVisible(false);
			mmv.jp_mv.setVisible(true);//마이페이지-틀뷰
			mmv.jp_mv.jp_miv.setVisible(true);//마이페이지-비밀번호입력뷰
			mmv.jp_mv.jp_muv.setVisible(false);
			mmv.jp_mv.jp_thv.setVisible(false);
			mmv.jp_mrv.setVisible(false);
			mmv.jp_mrv.jp_mcv.setVisible(false);
			mmv.jp_mrv.jp_pv.setVisible(false);
			mmv.jp_mrv.jp_scv.setVisible(false);
		}
		
		/**************************************************************************************************
		 * 좌석선택 버튼 클릭했을때
		 */
		else if(obj==mmv.jp_mrv.jbt_goSeatChoice) {
			/*********단위테스트하기위해 잠시 주석처리*************/
//			String temptheater 		= mmv.jp_mrv.jl_south_theater.getText();
//			StringTokenizer st = new StringTokenizer(temptheater, "/");
//			st.nextToken();
//			String theater = st.nextToken();
//			String screen 		= mmv.jp_mrv.jl_south_screen.getText();
//			String tempdate 		= mmv.jp_mrv.jl_south_date.getText();
//			System.out.println("tempdate"+ tempdate);
//			String date = (tempdate.substring(0, 4)+tempdate.substring(6, 8)+tempdate.substring(10, 12));
//			String time = mmv.jp_mrv.jl_south_time.getText();
//			String seatstatus_msg 	= MovieProtocol.GET_SEATSTATUS+"#"+theater+"#"+screen+"#"+date+"#"+time;
			//단위테스트용
			String seatstatus_msg = MovieProtocol.GET_SEATSTATUS+"#"+"해운대점"+"#"+"2관"+"#"+"20200411"+"#"+"19:40";
			this.send(seatstatus_msg);
		}
		else if(jv!=null&&obj==jv.jbt_email_r) {//인증번호 버튼을 눌렀니?
			
			end_millisecond = System.currentTimeMillis();//인증번호 입력시간 저장
			long term = end_millisecond - start_millisecond;//전송~입력 시간 계산
			if(term<120000) {//2분안에 입력한다면
				String inputNum = jv.jtf_email_r.getText();
				if(sm.rnum!=null) {
					if(sm.rnum.equals(inputNum)) {//인증번호가 일치시
						jv.jl_email_r_success.setVisible(true);
						jv.jl_email_r_warning.setVisible(false);
						jv.jl_email_warning2.setVisible(false);
						
					}
					else {//인증번호가 일치하지 않다면
						jv.jl_email_r_warning.setVisible(true);
						jv.jtf_email_r.setText("");
						email_r = 0;
					}
				}
			}
			else {//입력시간이 초과했다면
				jv.jl_email_r_warning2.setVisible(true);
				jv.jtf_email_r.setText("");
				email_r = 0;
				sm.rnum = null;
			}
		}
		/*************************************************************************************************
		 * 좌석 선택이 끝나고 결제 버튼을 눌렀을때 
		 */
		else if(obj==mmv.jp_mrv.jbt_goPayChoice) {
			System.out.println("결제버튼 클릭 : "+mmv.jp_mrv.jp_scv.seatChoiceList.size());
			for(String seatCode : mmv.jp_mrv.jp_scv.seatChoiceList) {
				String mem_id 		= mmv.mem_id;
				String movieName 	= mmv.jp_mrv.jl_south_movie.getText();
				String temptheater 		= mmv.jp_mrv.jl_south_theater.getText();
				StringTokenizer st = new StringTokenizer(temptheater, "/");
				st.nextToken();
				String theater = st.nextToken();
				String screen 		= mmv.jp_mrv.jl_south_screen.getText();
				String seat 		= seatCode;
				String tempdate 		= mmv.jp_mrv.jl_south_date.getText();
				System.out.println("tempdate"+ tempdate);
				//년월일 제외하여 20200408 형식 맞추기
//			jl_south_date.setText("2020년 03월 28일") -> 20200328
				String date = (tempdate.substring(0, 4)+tempdate.substring(6, 8)+tempdate.substring(10, 12));
				//시간 형식 24:00 라면 그냥 집어넣어
				String time 		= mmv.jp_mrv.jl_south_time.getText();
				String pay_msg 	= MovieProtocol.PAY+"#"+mem_id+"#"+movieName+"#"+theater+"#"
						+screen+"#"+seat+"#"+date+"#"+time;
				this.send(pay_msg);
			}
		}//end of obj==mmv.jp_mrv.jbt_goPayChoice
//		//선택한 인원수에 맞게 좌석수를 선택한 경우에 결제버튼이 활성화됨
		else if(mmv.jp_mrv.jp_scv.teenChoice!=0 && mmv.jp_mrv.jp_scv.teenChoice==mmv.jp_mrv.jp_scv.seatChoiceList.size()) {
			mmv.jp_mrv.jbt_goPayChoice.setForeground(Color.white);
			mmv.jp_mrv.jbt_goPayChoice.setBackground(new Color(52, 152, 219));
			mmv.jp_mrv.jbt_goPayChoice.setEnabled(true);
		} 
		else if(mmv.jp_mrv.jp_scv.adultChoice!=0 && mmv.jp_mrv.jp_scv.adultChoice==mmv.jp_mrv.jp_scv.seatChoiceList.size()) {
			mmv.jp_mrv.jbt_goPayChoice.setForeground(Color.white);
			mmv.jp_mrv.jbt_goPayChoice.setBackground(new Color(52, 152, 219));
			mmv.jp_mrv.jbt_goPayChoice.setEnabled(true);
		}
		/**************************************************************************************************
		 * 좌석 선택화면에서 adult, teen 인원 선택 - 선택한 인원만큼 좌석 선택	
		 */
		else {
			for(int i=0; i<mmv.jp_mrv.jp_scv.jbts_adult.length; i++) {
				if(obj==mmv.jp_mrv.jp_scv.jbts_adult[i]) {
					mmv.jp_mrv.jp_scv.jbts_adult[mmv.jp_mrv.jp_scv.adultChoice].setBackground(new Color(230, 230, 230));
					mmv.jp_mrv.jp_scv.adultChoice = i;
					System.out.println("mmv.jp_mrv.jp_scv.adultChoice 선택 인원수 : "+mmv.jp_mrv.jp_scv.adultChoice);
					mmv.jp_mrv.jp_scv.jbts_adult[i].setBackground(Color.yellow);
					mmv.jp_mrv.jp_scv.jbts_adult[i].setForeground(Color.black);
					break;
				}	
			}
			for(int i=0; i<mmv.jp_mrv.jp_scv.jbts_teen.length; i++) {
				if(obj==mmv.jp_mrv.jp_scv.jbts_teen[i]) {
					mmv.jp_mrv.jp_scv.jbts_teen[mmv.jp_mrv.jp_scv.teenChoice].setBackground(new Color(230, 230, 230));
					mmv.jp_mrv.jp_scv.teenChoice = i;
					System.out.println("mmv.jp_mrv.jp_scv.teenChoice 선택 인원수 : "+mmv.jp_mrv.jp_scv.teenChoice);
					mmv.jp_mrv.jp_scv.jbts_teen[i].setBackground(Color.yellow);
					mmv.jp_mrv.jp_scv.jbts_teen[i].setForeground(Color.black);
					break;
				}					
			}
			
			for(int i=0; i<mmv.jp_mrv.jp_scv.jbts_seat.length; i++) {
				for(int j=0; j<mmv.jp_mrv.jp_scv.jbts_seat[i].length;  j++) {
					if(obj==mmv.jp_mrv.jp_scv.jbts_seat[i][j]) {
//						for(int k =0; k<mmv.jp_mrv.jp_scv.seatChoiceList.size(); k++) {
//							System.out.println(seatChoice);
//							if(seatChoice.equals(mmv.jp_mrv.jp_scv.seatChoiceList.get(k))) {
//								mmv.jp_mrv.jp_scv.seatChoiceList.remove(k);
//							}
//						}
						if(mmv.jp_mrv.jp_scv.seatChoiceList.size() < mmv.jp_mrv.jp_scv.teenChoice + mmv.jp_mrv.jp_scv.adultChoice) {
							String seatChoice = (char)(i+65) + Integer.toString(j+1);
							//(행) : (char)(i+65)   (열) : Integer.toString(j+1) 행과열 : (char)(i+65) + Integer.toString(j+1)					
							mmv.jp_mrv.jp_scv.jbts_seat[i][j].setBackground(Color.white);
							mmv.jp_mrv.jp_scv.jbts_seat[i][j].setForeground(Color.black);
							System.out.println("seatChoice : " + seatChoice);
							mmv.jp_mrv.jp_scv.seatChoiceList.add(seatChoice);
							
						} else {
							return;
						}
					}
				}
			}

			
		

		} 
		
	}
	/**************************************************************************************************
	 * ItemListener(회원가입-생년월일, 회원가입-성별)
	 */
	@Override
	public void itemStateChanged(ItemEvent e) {
		Object obj = e.getSource();
		//생년월일 ----------------------------------------------------------------------------------------
		if(obj==jv.jcb_year) {//년도 콤보박스가 선택되었어요
			if(e.getStateChange()==ItemEvent.SELECTED) {
				this.jv.choiceYear = jv.jcb_year.getSelectedItem().toString();//선택한 년도 정보 일단 저장할게
				String []months = null;//얘는 월정보 저장할 배열이야
				jv.remove(jv.jcb_month);
				jv.remove(jv.jcb_day);
				if(!"년도".equals(jv.choiceYear)) {//숫자년도를 선택했다면		
					if(jv.jcb_year.getSelectedItem().toString().equals(Integer.toString(jv.year))) {//선택한 년도가 올해와 같니?
						months = new String[jv.month+1];//그럼 올해의 월을 고려할게
						months[0] = "월";
						for(int i=1; i<months.length; i++) {
							months[i]= Integer.toString(i);
						}
					}
					else {//선택한 년도가 올해가 아니니?
						months = new String[13];//그럼 기본 12개월을 기준으로할게
						months[0] = "월";
						for(int i=1; i<months.length; i++) {
							months[i]= Integer.toString(i);
						}
					}
					jv.jcb_month = new JComboBox<String>(months);//월,일 콤보박스 생성
					jv.jcb_day = new JComboBox<String>();
					jv.jcb_month.addItemListener(this);
					jv.jcb_day.addItemListener(this);
					jv.jcb_day.addItem("일");
				}
				else {//설마 선택한게 문자 년도니?
					jv.jcb_month = new JComboBox<String>();//월,일 콤보박스 생성
					jv.jcb_month.addItem("월");
					jv.jcb_day = new JComboBox<String>();
					jv.jcb_day.addItem("일");
				}
				jv.choiceMonth ="월";
				jv.choiceDay ="일";
				jv.jcb_month.setBounds(288, 428 ,80 ,32);//화면에 붙이기
				jv.jcb_day.setBounds(370, 428 ,80 ,32);
				jv.jcb_month.setBorder(new TitledBorder(new LineBorder(new Color(0,80,255),3)));
				jv.jcb_day.setBorder(new TitledBorder(new LineBorder(new Color(0,80,255),3)));
				jv.add(jv.jcb_month);
				jv.add(jv.jcb_day);
				jv.revalidate();
				birth_checking();//기준체크 메소드
			}
		}
		else if(obj==jv.jcb_month) {//월 콤보박스가 선택되었어요
			if(e.getStateChange()==ItemEvent.SELECTED) {
				this.jv.choiceMonth = jv.jcb_month.getSelectedItem().toString();//선택한 월 정보 일단 저장할게
				String []days = null;//얘는 일정보 저장할 배열이야
				jv.remove(jv.jcb_day);
				if(!"월".equals(jv.choiceMonth)) {//숫자 월을 선택했다면
					if(jv.jcb_year.getSelectedItem().toString().equals(Integer.toString(jv.year))&&
							jv.jcb_month.getSelectedItem().toString().equals(Integer.toString(jv.month))) {//올해 월을 선택했니?
						days = new String[jv.day+1];//이번 달을 기준으로 할게
					}
					else {//다른 년도의 월을 선택했니? (31일/28일/30일)을 기준으로 할게
						if("1".equals(jv.choiceMonth)||"3".equals(jv.choiceMonth)||"5".equals(jv.choiceMonth)||
								"7".equals(jv.choiceMonth)||"8".equals(jv.choiceMonth)||"10".equals(jv.choiceMonth)||
										"12".equals(jv.choiceMonth)) {
							days = new String[32];
						}
						else if("2".equals(jv.choiceMonth)) {
							days = new String[29];
						}else {
							days = new String[31];
						}
					}
					days[0] = "일";
					for(int i=1; i<days.length; i++) {//일 콤보박스 생성
						days[i]= Integer.toString(i);
					}
					jv.jcb_day = new JComboBox<String>(days);
					jv.jcb_day.addItemListener(this);
				}
				else {//설마 선택한게 문자 월이니?
					jv.jcb_day = new JComboBox<String>();//일 콤보박스 생성
					jv.jcb_day.addItem("일");
				}
				jv.choiceDay ="일";
				jv.jcb_day.setBounds(370, 428 ,80 ,32);//화면에 붙이기
				jv.jcb_day.setBorder(new TitledBorder(new LineBorder(new Color(0,80,255),3)));
				jv.add(jv.jcb_day);
				jv.revalidate();
				birth_checking();//기준체크 메소드
			}
		}
		else if(obj==jv.jcb_day) {//일 콤보박스가 선택되었어요
			if(e.getStateChange()==ItemEvent.SELECTED) {
				this.jv.choiceDay = jv.jcb_day.getSelectedItem().toString();//선택한 일 정보 일단 저장할게
				birth_checking();//기준체크 메소드
			}
		}
		//성별 ------------------------------------------------------------------------------------------
		/*
		 * jl_gender_warning.setText(" 성별을 선택해주세요.");
		 */
		else if(obj==jv.jcb_gender) {
			if(e.getStateChange()==ItemEvent.SELECTED) {
				jv.jcb_genderChoice = jv.jcb_gender.getSelectedItem().toString();
				if("선택".equals(jv.jcb_genderChoice)) {
					jv.jl_gender_warning.setVisible(true);
					gender = 0;
				}else {
					jv.jl_gender_warning.setVisible(false);
					gender = 1;
				}
			}
		}
	}
	/**************************************************************************************************
	 * KeyListener(회원가입-이름, 회원가입-pw, 회원가입-닉네임)
	 */
	@Override
	public void keyReleased(KeyEvent e) {
		Object obj = e.getSource();
		//회원가입 ---------------------------------------------------------------------------------------
		/*
		 * jl_pw_warning.setText(" 7~12자이어야 하고 공백은 불가합니다.");
		jl_name_warning.setText(" 2~8자이어야 하고 특수문자는 사용할 수 없습니다");
		jl_nick_warning.setText(" 2~8자이이어야 하고 특수문자는 사용할 수 없습니다.")
		 */
		if(jv!=null&&obj==jv.jtf_name) {//이름 검사
			String inputname = jv.jtf_name.getText();
			int result = checkNames(inputname, 2, 8);
			if(result>0) {//기준미통과
				jv.jl_name_warning.setVisible(true);
				name = 0;
			}else {//기준통과
				jv.jl_name_warning.setVisible(false);
				name = 1;
			}
		}
		else if(jv!=null&&obj==jv.jtf_nick) {//닉네임 검사
			String inputnick = jv.jtf_nick.getText();
			int result = checkNames(inputnick, 2, 8);
			if(result>0) {//기준미통과
				jv.jl_nick_warning.setVisible(true);
				nickName = 0;
			}else {//기준통과
				jv.jl_nick_warning.setVisible(false);
				nickName = 1;
			}
		}
		else if(jv!=null&&obj==jv.jpf_pw) {//비밀번호 검사
			String inputpw = pwToString(jv.jpf_pw.getPassword());
			int result = checkPw(inputpw, 7 ,12);
			if(result>0) {//기준미통과
				jv.jl_pw_warning.setVisible(true);
				pw = 0;
			}else {//기준통과
				jv.jl_pw_warning.setVisible(false);
				pw = 1;
			}
		}
		//마이페이지 --------------------------------------------------------------------------------------
		/*jl_pw_warning  	   		= new JLabel(" 7~12자이어야 하고 공백은 불가합니다.");
		jl_nick_warning	    	= new JLabel(" 2~8자이이어야 하고 특수문자는 사용할 수 없습니다.");
		 */
		else if(obj==mmv.jp_mv.jp_muv.jpf_pw) {//비밀번호 검사
			System.out.println("비번검사");
			String inputpw = pwToString(mmv.jp_mv.jp_muv.jpf_pw.getPassword());
			int result = checkPw(inputpw, 7 ,12);
			if(result>0) {//기준미통과
				mmv.jp_mv.jp_muv.jl_pw_warning.setVisible(true);
				pw = 0;
			}else {//기준통과
				mmv.jp_mv.jp_muv.jl_pw_warning.setVisible(false);
				pw = 1;
			}
		}
		else if(obj==mmv.jp_mv.jp_muv.jtf_nick) {//닉네임 검사
			System.out.println("닉검사");
			String inputnick = mmv.jp_mv.jp_muv.jtf_nick.getText();
			int result = checkNames(inputnick, 2, 8);
			if(result>0) {//기준미통과
				mmv.jp_mv.jp_muv.jl_nick_warning.setVisible(true);
				nickName = 0;
			}else {//기준통과
				mmv.jp_mv.jp_muv.jl_nick_warning.setVisible(false);
				nickName = 1;
			}
		}
	}
	@Override
	public void keyTyped(KeyEvent e) {}
	@Override
	public void mouseClicked(MouseEvent e) {}
	@Override
	public void mouseEntered(MouseEvent e) {}
	@Override
	public void mouseExited(MouseEvent e) {}
	@Override
	public void mousePressed(MouseEvent e) {}
	@Override
	public void mouseReleased(MouseEvent e) {}

	@Override
	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
