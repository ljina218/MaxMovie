package maxmovie;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Calendar;

import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

public class EventMapping implements ActionListener, ItemListener{
	
	//진아수정
	/* 뷰 패널들의 변수이름
	//로그인뷰
	JPanel 					jp_lv 			= new LoginView(this);
	//마이페이지뷰
	JPanel 					jp_mv 			= new MyPageView(this);
	//무비초이스뷰
	JPanel 					jp_mcv 			= new MovieChoiceView(this);
	//시트초이스뷰
	JPanel 					jp_sc 			= new SeatChoiceView(this);
	//리절트뷰
	JPanel 					jp_rv 			= new ResultView(this);
	*/
	
	//필요한 주소들 선언부
	MaxMovieView mmv = null;
	MovieController ctrl = new MovieController();
	JoinView jv = null;
	
	public EventMapping(MaxMovieView mmv) {
		this.mmv = mmv;
	}
	
	//서버스레드에게 말하기 위한 메소드
	private void send(String msg) {
		try {
			mmv.oos.writeObject(msg);
		} catch (Exception e) {
			System.out.println(e.toString());
			//e.printStackTrace();
		}
	}
	
	//생년월일 기준체크 메소드
	public void birth_checking() {
		if("년도".equals(jv.choiceYear)) {
			jv.jl_year_warning.setVisible(true);
			jv.jl_month_warning.setVisible(false);
			jv.jl_day_warning.setVisible(false);
		}else {
			if("월".equals(jv.choiceMonth)) {
				jv.jl_year_warning.setVisible(false);
				jv.jl_month_warning.setVisible(true);
				jv.jl_day_warning.setVisible(false);
			}else{
				if("일".equals(jv.choiceDay)) {
					jv.jl_year_warning.setVisible(false);
					jv.jl_month_warning.setVisible(false);
					jv.jl_day_warning.setVisible(true);
				}else {
					jv.jl_year_warning.setVisible(false);
					jv.jl_month_warning.setVisible(false);
					jv.jl_day_warning.setVisible(false);
				}
			}
		}
	}
	
	/**************************************************************************************************
	 * ActionListener(로그인, )
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();
		//로그인 ----------------------------------------------------------------------------------------
		if(obj==mmv.jp_lv.jbt_login) {
			String id = "";
			String pw = "";
			String msg = 100+"#"+id+"#"+pw;
			send(msg);
		}
		//회원가입 ----------------------------------------------------------------------------------------
		else if(obj==mmv.jp_lv.jbt_join) {
			System.out.println("회원가입");
			jv = new JoinView(this);
		}
		else if(obj==jv.jbt_joingo) {
			//경고문구를 고려하여 insert결정
		}
		else if(obj==jv.jbt_back) {
			jv.dispose();
		}
		//로그인 ----------------------------------------------------------------------------------------
		//else if() {
		//	
		//}
		
	}
	
	/**************************************************************************************************
	 * ItemListener(회원가입)
	 */
	@Override
	public void itemStateChanged(ItemEvent e) {
		Object ob = e.getSource();
		if(ob==jv.jcb_year) {//년도 콤보박스가 선택되었어요
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
		else if(ob==jv.jcb_month) {//월 콤보박스가 선택되었어요
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
				jv.jcb_day.setBounds(370, 428 ,80 ,32);//화면에 붙이기
				jv.jcb_day.setBorder(new TitledBorder(new LineBorder(new Color(0,80,255),3)));
				jv.add(jv.jcb_day);
				jv.revalidate();
				birth_checking();//기준체크 메소드
			}
		}
		else if(ob==jv.jcb_day) {//일 콤보박스가 선택되었어요
			if(e.getStateChange()==ItemEvent.SELECTED) {
				this.jv.choiceDay = jv.jcb_day.getSelectedItem().toString();//선택한 일 정보 일단 저장할게
				birth_checking();//기준체크 메소드
			}
		}
	}///ItemListener
	
	


}
