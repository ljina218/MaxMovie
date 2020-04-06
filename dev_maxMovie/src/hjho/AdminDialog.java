package hjho;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class AdminDialog extends JDialog implements ActionListener{
	AdminClient ac = null;
	//화면을 위한 그리기 코드
	JPanel jp = new JPanel();
	
	JLabel jl_movieTitle = new JLabel("영화제목 : ",JLabel.RIGHT);
	JLabel jl_scrName = new JLabel("상영관이름  : ",JLabel.RIGHT);
	JLabel jl_date = new JLabel("날짜 : ",JLabel.RIGHT);
	JLabel jl_time = new JLabel("시간 : ",JLabel.RIGHT);
	JLabel jl_yy = new JLabel(" 년",JLabel.RIGHT);
	JLabel jl_mm = new JLabel(" 월",JLabel.RIGHT);
	JLabel jl_dd = new JLabel(" 일",JLabel.RIGHT);
	JLabel jl_hh24 = new JLabel(" 시",JLabel.RIGHT);
	JLabel jl_mi = new JLabel(" 분",JLabel.RIGHT);
	JComboBox jcb_movieTitle = null;
	JComboBox jcb_scrName = null;
	JComboBox jcb_yy   = null;
	JComboBox jcb_mm   = null;
	JComboBox jcb_dd   = null;
	JComboBox jcb_hh24 = null;
	JComboBox jcb_mi   = null;
	
	Calendar cal = null;
	
	//JButton jbtn_sel = new JButton("영화정보 가져오기");	
	JButton jbtn_ins = new JButton("반영하기");	
	JButton jbtn_exit = new JButton("나가기");	
	
	Vector<String> movieTitle = null;
	Vector<String> scrName = null;
	String yy[] = new String[3]; 
	String mm[] = {"월","01","02","03","04","05","06","07","08","09","10","11","12"};
	String hh24[] = {"시","00","01","02","03","04","05","06","07","08","09","10","11"
			        ,"12","13","14","15","16","17","18","19","20","21","22","23"};
	String mi[] = {"분","00","05","10","15","20","25","30","35","40","45","50","55"};
/*******************************************************************************************
 * 생성자 : adminClient 주소값을 받아주고 Listener 추가~
 * 		그 후 JComboBox 함수 호출을 담당  
 */
	public AdminDialog(AdminClient ac) {
		this.ac = ac;
		//jbtn_sel.addActionListener(this);
		jbtn_ins.addActionListener(this);
		jbtn_exit.addActionListener(this);
		setJcomboBox();
	}
/**************************************************************************************
 * setJcomboBox() : 처음 Dialog를 띄울 때 JComboBox의 내용으 초기화 시켜주는 함수 
 */
	public void setJcomboBox() {
		//영화제목과 상영관을 담을 벡터
		movieTitle = new Vector<String>();
		scrName = new Vector<String>();
		//오라클에서 받은 현재상영하는 영화와
		//접속한 지점에 해당하는 상영관을 받기위한 for문 
		for (int i = 0; i < ac.first.size(); i++) {
			if(ac.first.get(i).toString().equals("영화제목")) {
				movieTitle.add(ac.second.get(i));
			}
			else {
				scrName.add(ac.second.get(i));
			}
		}
		cal = Calendar.getInstance();
		yy[0] = "연";
		yy[1] = Integer.toString(cal.get(Calendar.YEAR));
		yy[2] = Integer.toString(cal.get(Calendar.YEAR)+1);
		
		jcb_movieTitle = new JComboBox(movieTitle);
		jcb_scrName = new JComboBox(scrName);
		jcb_yy = new JComboBox(yy);
		jcb_mm = new JComboBox(mm);
		jcb_dd = new JComboBox();
		jcb_hh24 = new JComboBox(hh24);
		jcb_mi = new JComboBox(mi);
		
		jcb_movieTitle.addActionListener(this);
		jcb_scrName.addActionListener(this);
		jcb_yy.addActionListener(this);
		jcb_mm.addActionListener(this);
		jcb_hh24.addActionListener(this);
		jcb_mi.addActionListener(this);
	}
	
	
	
	public void initDisplay() {
		this.setLayout(null);
		//jl_movieTitle.setBounds(x, y, width, height);
		jl_movieTitle.setBounds(20, 20, 100, 20);
		jcb_movieTitle.setBounds(125, 20, 300, 20);
		jl_scrName.setBounds(20, 45, 100, 20);
		jcb_scrName.setBounds(125, 45, 100, 20);
		
		jl_date.setBounds(20, 70, 100, 20);
		jcb_yy.setBounds(125, 70, 60, 20);
		jl_yy.setBounds(160, 70, 40, 20);
		jcb_mm.setBounds(205, 70, 40, 20);
		jl_mm.setBounds(225, 70, 40, 20);
		jcb_dd.setBounds(270, 70, 40, 20);
		jl_dd.setBounds(290, 70, 40, 20);
		
		jl_time.setBounds(20, 95, 100, 20);
		jcb_hh24.setBounds(125, 95, 60, 20);
		jl_hh24.setBounds(160, 95, 40, 20);
		jcb_mi.setBounds(205, 95, 40, 20);
		jl_mi.setBounds(225, 95, 40, 20);
		
		//jbtn_sel.setBounds(20, 130, 150, 20);
		jbtn_ins.setBounds(150, 130, 100, 20);
		jbtn_exit.setBounds(260, 130, 100, 20);
		this.add(jl_movieTitle);
		this.add(jcb_movieTitle);
		this.add(jl_scrName);
		this.add(jcb_scrName);
		this.add(jl_date);
		this.add(jl_time);
		this.add(jl_yy);
		this.add(jl_mm);
		this.add(jl_dd);
		this.add(jl_hh24);
		this.add(jl_mi);
		this.add(jcb_yy);
		this.add(jcb_mm);
		this.add(jcb_dd);
		this.add(jcb_hh24);
		this.add(jcb_mi);
		//his.add(jbtn_sel);
		this.add(jbtn_ins);
		this.add(jbtn_exit);
		this.setTitle("상영시간표 작성");
		this.setSize(500,250);
		this.setVisible(true);
	}
//	JButton jbtn_sel = new JButton("영화정보 가져오기");	
//	JButton jbtn_ins = new JButton("반영하기");	
//	JButton jbtn_exit = new JButton("나가기");
	@Override
	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();
		//반영하기를 눌렀니?
		if(obj==jbtn_ins) {
			//예 : 0 , 아니오 : 1
			int confirm = JOptionPane.showConfirmDialog(
					this, "상영시간표에 추가하시겠습니까?"
					, "상영시간표에 추가", JOptionPane.YES_NO_OPTION
					, JOptionPane.INFORMATION_MESSAGE);
			//System.out.println(Confirm);
			if(confirm==0) {
				AdminShowtimeVO astVO = confirmShowtime();
				//System.out.println(astVO.getMsg());
				if(!(astVO.getMsg().equals("확인 완료"))) {
					JOptionPane.showMessageDialog(this
					, astVO.getMsg()
				    , "경고" , JOptionPane.ERROR_MESSAGE);
				}
				else {
					int reconfirm = JOptionPane.showConfirmDialog(this
							, "영화제목 : "+astVO.getMovieTitle()+" \n"
							 +"상영관명 : "+astVO.getScrName()+" \n" 
							 +"상영날짜 : "+astVO.getDate()+" \n" 
							 +"상영시간 : "+astVO.getTime()+" \n" 
							, "상영 정보"
							, JOptionPane.INFORMATION_MESSAGE);
					if(reconfirm==0) {
						try {
							ac.oos.writeObject(AdminProtocol._INS
									+"#"+ac.astVO.getId()
									+"#"+astVO.getMovieTitle()
									+"#"+astVO.getScrName()
									+"#"+astVO.getYy()
									+"#"+astVO.getMm()
									+"#"+astVO.getDd()
									+"#"+astVO.getHh24()
									+"#"+astVO.getMi()
									);
						} catch (Exception e2) {
							e2.printStackTrace();
						}
						
					}
				}
			}
		}
		//나가기를 눌렀니?
		else if(obj==jbtn_exit) {
			int confirm = JOptionPane.showConfirmDialog(
					this, "나가시겠습니까?"
					, "상영시간표 추가 화면 나가기", JOptionPane.YES_NO_OPTION
					, JOptionPane.INFORMATION_MESSAGE);
			if(confirm==0) {
				this.dispose();
				ac.ad = null;
			}
		}
		//콤보박스에서 월을 설정 했니?
		else if(obj==jcb_mm) {
			String mm = (String)(jcb_mm.getSelectedItem());
			//System.out.println(mm);//확인용
			String dd[] = getDayList(mm);
			this.remove(jcb_dd);
			jcb_dd = null;
			jcb_dd = new JComboBox(dd);
			jcb_dd.addActionListener(this);
			jcb_dd.setBounds(270, 70, 40, 20);
			this.add(jcb_dd);
			Container cont = this.getContentPane();
			cont.revalidate();
		}
	}
/*******************************************************************************
 * getDayList() : 월을 입력하면 해당하는 월의 일을 반환 해 준다. 
 * 				31일 : 1, 3, 5, 7, 8, 10, 12 월
 * 				30일 : 4, 6, 9, 11 월 
 * 				그외  : 2 월 (28, 29)
 * @param mm : 선택한 월 
 * @return rDD : 해당하는 일 배열  
 */
	private String[] getDayList(String mm) {
//		31일 : 1, 3, 5, 7, 8, 10, 12
//		30일 : 4, 6, 9, 11
//		그외  : 2
		//System.out.println("List안에서 :"+mm);
		//1차배열을 달, 2차배열은 해당하는 달의 일자
		String[][] month = {
				{"01","02","03","04","05","06","07","08","09","10","11","12"}
			   ,{"31","28","31","30","31","30","31","31","30","31","30","31"}
		};
		String[] rDD = null; //일자 담을 배열 
		int day = 0;
		//1월 부터 12 월 중에 
		for (int i = 0; i < month[0].length; i++) {
			//선택한 달이 있니? 
			if(mm.equals(month[0][i])) {
				if(i==1) {//선택한 달이 2월이야?
					//그러면 현재 연도 를 4를 나눌 때 0인애를 알려줘 
					if(cal.get(Calendar.YEAR)%4==0) {
						day = 29;//0이라면  29일을 넣어줘
						break;
					}
					else {//아니면 28일을 넣어줘 
						day = Integer.parseInt(month[1][i]);
						break;
					}
				}
				else {//선택한 달이 2월이 아니면 해당하는 달의 일을 넣어줘 
					day = Integer.parseInt(month[1][i]);
					break;
				}
			}
		}
		rDD = new String[day+1]; //0번지의 "일"을 넣기 위한 +1
		for (int i = 0; i < day+1; i++) {
			if(i==0) {//0번지의 "일"
				rDD[0] = "일";
			}
			else if(i<10) {//1~9까지는 01,02,03,04,05,06,07,08,09
				rDD[i] = "0"+i;
			}
			else {//나머지는 그냥 넣는다 
				rDD[i] = Integer.toString(i);
			}
		}
		return rDD; //해당하는 일자 반환 
	}
	/*
	public static void main(String[] args) {
		AdminDialog ad = new AdminDialog();
		ad.initDisplay();
	}
	*/
	
	public AdminShowtimeVO confirmShowtime() {
		//사용자가 입력한 정보를 가져온다.
		AdminShowtimeVO astVO = new AdminShowtimeVO();
		astVO.setMovieTitle((String)jcb_movieTitle.getSelectedItem());
		astVO.setScrName((String)jcb_scrName.getSelectedItem());
		String yy = (String)(jcb_yy.getSelectedItem());
		String mm = (String)(jcb_mm.getSelectedItem());
		String dd = (String)(jcb_dd.getSelectedItem());
		String hh24 = (String)(jcb_hh24.getSelectedItem());
		String mi = (String)(jcb_mi.getSelectedItem());
		//날짜 및 시간을 입력 안했으면 
		if(yy.equals("연")||mm.equals("월")||dd.equals("일")||dd==null
		 ||hh24.equals("시")||mi.equals("분"))
		 { 	
		 	astVO.setMsg("날짜와 시간을 확인해주세요");
		}else { //날짜 및 시간을 입력 했다면 
			String date = yy+mm+dd;  //선택한 날짜 조합
			String time = hh24+mi;   //선택한 시간 조합
			String c_Date = ac.setYMD(); //현재날짜
			String c_Time = ac.setHMS(); //현재시간
			//선택한 날짜가 이미 지났니? 
			if(Integer.parseInt(c_Date)
					> Integer.parseInt(date)) {
				astVO.setMsg("이미 지난 날짜입니다.");
			}
			//선택한 날짜가 오늘 날짜야?
			else if(c_Date.equals(date)){
				//그러면 시간은 이미 지났니?
				if(Integer.parseInt(c_Time)
						> Integer.parseInt(time)) {
					astVO.setMsg("이미 지난 시간입니다.");
				}
				else { //시간 안지났으면
					//날짜 저장 및 시간형식으로 저장
					astVO.setMsg("확인 완료");
					astVO.setDate(date);
					astVO.setYy(yy);
					astVO.setMm(mm);
					astVO.setDd(dd);
					astVO.setTime(hh24+":"+mi);
					astVO.setHh24(hh24);
					astVO.setMi(mi);
				}
			}
			//선택한 날짜가 현재날짜보다 미래라면 
			else { 
				astVO.setMsg("확인 완료");
				astVO.setDate(date);
				astVO.setYy(yy);
				astVO.setMm(mm);
				astVO.setDd(dd);
				astVO.setTime(hh24+":"+mi);
				astVO.setHh24(hh24);
				astVO.setMi(mi);
			}
		}//날짜 및 시간 적합성 검사
		return astVO;
	}//end of confirmShowtime
}


















