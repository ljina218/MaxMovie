package maxmovie;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

public class MovieReserationView extends JPanel{
	EventMapping				em						= null;
	MovieChoiceView				jp_mcv					= null;
	SeatChoiceView				jp_scv					= null;
	PayView						jp_pv					= null;
	
	JLabel						jl_south				= new JLabel("　　　　　　　　　　　　　         　  　 　　　　>　　　　　　　 　　　>　　　　　　　 　　　>　　　　　　　 　　　>　　　　　　　 　　　>");
	JLabel						jl_south_movie			= new JLabel("영화선택");
	JLabel						jl_south_ctf			= new JLabel("");
	JLabel						jl_south_loc			= new JLabel("극장선택");
	JLabel						jl_south_theater		= new JLabel(""); //서울/강남점
	JLabel						jl_south_date			= new JLabel("날짜선택"); 
	JLabel						jl_south_screen			= new JLabel("시간선택");
	JLabel						jl_south_time			= new JLabel("");
	JLabel						jl_south_seat			= new JLabel("좌석");
	JTextArea					jta_south_Allseat		= new JTextArea();
	JLabel						jl_south_pay			= new JLabel("결제금액");
	JLabel						jl_south_totalPay		= new JLabel();
		
	JButton						jbt_backMovieChoice 	= new JButton("←영화선택");
	JButton						jbt_backSeatChoice 		= new JButton("←좌석선택");
	JButton						jbt_goSeatChoice 		= new JButton("좌석선택→");
	JButton						jbt_goPayChoice			= new JButton("결제선택→");
	JButton						jbt_goPayInfo			= new JButton("결제하기→");
	
	StringBuilder 				sb_seatChoiceList 		= new StringBuilder();
	public MovieReserationView(EventMapping em) {
		this.em = em;
		jp_mcv = new MovieChoiceView(em);
		jp_scv = new SeatChoiceView(em);
		jp_pv = new PayView(em);
		eventMapping();
		initDisplay();
	}
	public void initDisplay() {
		this.setLayout(null);
		this.setBackground(Color.white);

		jp_mcv.setBounds(0, 0, 1360, 660);
		jp_scv.setBounds(0, 0, 1360, 660);
		jp_pv.setBounds(0, 0, 1360, 660);
		
		jl_south.setBounds(200, 664, 1144, 90);		
		jl_south_movie.setBounds(340, 698, 150, 20);		
		jl_south_ctf.setBounds(340, 710, 150, 20);	
		jl_south_loc.setBounds(480, 698, 150, 20);	
		jl_south_theater.setBounds(480, 710, 150, 20);	
		jl_south_date.setBounds(620, 698, 150, 20);			
		jl_south_screen.setBounds(760, 698, 150, 20);	
		jl_south_time.setBounds(760, 710, 150, 20);
		jl_south_seat.setBounds(900, 698, 150, 20);
		jta_south_Allseat.setBounds(922, 690, 110, 50);
		jl_south_pay.setBounds(1040, 698, 150, 20);	
		jl_south_totalPay.setBounds(1040, 710, 150, 20);
		jbt_backMovieChoice.setBounds(220, 685, 100, 45);	
		jbt_goSeatChoice.setBounds(1220, 685, 100, 45);
		jbt_goPayChoice.setBounds(1220, 685, 100, 45);
		
		jbt_backSeatChoice.setBounds(220, 685, 100, 45);	
		jbt_goPayInfo.setBounds(1220, 685, 100, 45);
		
		jp_scv.setVisible(false);
		jp_pv.setVisible(false);
		jp_mcv.setVisible(false);
		jl_south.setOpaque(true);
		jta_south_Allseat.setOpaque(true);
		jta_south_Allseat.setLineWrap(true);
		jta_south_Allseat.setEditable(false);
		
		jl_south.setBackground(new Color(190, 190, 190));

//		jbt_goMovieChoice.setBackground(new Color(52, 152, 219));
//		jbt_goMovieChoice.setForeground(Color.white);		
//		jbt_goMovieChoice.setVisible(true);
		jbt_goSeatChoice.setBackground(new Color(230, 230, 230));
		jbt_goSeatChoice.setForeground(Color.gray);
		jbt_goSeatChoice.setEnabled(false);
		jbt_backMovieChoice.setBackground(new Color(52, 152, 219));
		jbt_backMovieChoice.setForeground(Color.white);		
		jbt_backMovieChoice.setVisible(false);
		jbt_backSeatChoice.setBackground(new Color(52, 152, 219));
		jbt_backSeatChoice.setForeground(Color.white);	
		jbt_backSeatChoice.setVisible(false);
		jbt_goSeatChoice.setBackground(new Color(230, 230, 230));
		jbt_goSeatChoice.setForeground(Color.gray);
		jbt_goSeatChoice.setEnabled(false);
		//jbt_goSeatChoice.setBackground(new Color(230, 230, 230));
		//jbt_goSeatChoice.setForeground(Color.gray);
		//jbt_goSeatChoice.setEnabled(false);
		jbt_goPayChoice.setBackground(new Color(230, 230, 230));
		jbt_goPayChoice.setForeground(Color.gray);
		jbt_goPayChoice.setEnabled(false);
		jta_south_Allseat.setBackground(new Color(190, 190, 190));
		jbt_goPayInfo.setBackground(new Color(230, 230, 230));
		jbt_goPayInfo.setForeground(Color.gray);
		jbt_goPayInfo.setEnabled(false);
		//
		
		//
		
		jl_south_movie.setHorizontalAlignment(JLabel.CENTER);
		jl_south_ctf.setHorizontalAlignment(JLabel.CENTER);
		jl_south_loc.setHorizontalAlignment(JLabel.CENTER);
		jl_south_theater.setHorizontalAlignment(JLabel.CENTER);
		jl_south_date.setHorizontalAlignment(JLabel.CENTER);
		jl_south_screen.setHorizontalAlignment(JLabel.CENTER);
		jl_south_time.setHorizontalAlignment(JLabel.CENTER);
		jl_south_seat.setHorizontalAlignment(JLabel.CENTER);
		jl_south_pay.setHorizontalAlignment(JLabel.CENTER);
		jl_south_totalPay.setHorizontalAlignment(JLabel.CENTER);
		
		/**********************************************************************************
		 * MOVIE CHOICE VIEW 에서 일어나는 일들
		 * //영화 선택시
		 * jl_south_movie.setBounds(350, 690, 150, 20);
		 * jl_south_movie.setText("이보다 더 좋을 순 없다.");
		 * jl_south_ctf.setVisible(true);
		 * jl_south_ctf.setText("전체이용가");
		 * //영화 미선택시
		 * jl_south_movie.setBounds(350, 698, 150, 20);
		 * jl_south_movie.setText("영화선택");
		 * jl_south_ctf.setVisible(false);
		 * jl_south_ctf.setText("");
		 * 
		 * //지역&&지점 선택시
		 * jl_south_loc.setBounds(580, 690, 150, 20);
		 * jl_south_loc.setText("전북/전주");
		 * jl_south_theater.setVisible(true);
		 * jl_south_theater.setText("건대입구");
		 * //지역&&지점 미선택시
		 * jl_south_loc.setBounds(580, 698, 150, 20);
		 * jl_south_loc.setText("극장선택");
		 * jl_south_theater.setVisible(false);
		 * jl_south_theater.setText("");
		 * 
		 * //날짜 선택시
		 * jl_south_date.setText("2020년 03월 28일");
		 * //날짜미 미선택시
		 * jl_south_date.setText("날짜선택");
		 * 
		 * //시간 선택시
		 * jl_south_screen.setBounds(1040, 690, 150, 20);
		 * jl_south_screen.setText("1관");
		 * jl_south_time.setVisible(true);
		 * jl_south_time.setText("오전 09:00:00");
		 * //시간 미선택시
		 * jl_south_screen.setBounds(1040, 698, 150, 20);
		 * jl_south_screen.setText("시간선택");
		 * jl_south_time.setVisible(false);
		 * jl_south_time.setText("");
		 * 
		 * //영화 + 극장 + 날짜  모두 선택시
		 * jsp_time.setVisible(true);
		 * jl_timeLock.setVisible(false);
		 * //영화 + 극장 + 날짜 중 하나라도 미 선택시
		 * jsp_time.setVisible(false);
		 * jl_timeLock.setVisible(true);
		 * 
		 * //모두 선택시
		 * jbt_goSeatChoice.setForeground(Color.white);
		 * jbt_goSeatChoice.setBackground(new Color(52, 152, 219));
		 * jbt_goSeatChoice.setEnabled(true);
		 * 
		 * //하나라도 미선택시
		 * jbt_goSeatChoice.setBackground(new Color(230, 230, 230));
		 * jbt_goSeatChoice.setForeground(Color.gray);
		 * jbt_goSeatChoice.setEnabled(false);
		 * 
		 * //모두 선택시 좌석 버튼 활성화
		 * jbt_goSeatChoice.setForeground(Color.white);
		 * jbt_goSeatChoice.setBackground(new Color(52, 152, 219));
		 * jbt_goSeatChoice.setEnabled(true);
		 * 
		 * //시트초이스뷰로 넘어가는 경우(시트초이스뷰인경우)
		 * jbt_backMovieChoice.setVisible(true);
		 * jbt_goSeatChoice.setVisible(false);
		 * jbt_goPayChoice.setVisible(true);
		 **********************************************************************************/
		
		/**********************************************************************************
		 * SEAT CHOICE VIEW 에서 일어나는 일들
		 * 
		 * jbt_BACK
		 * 인원수 클릭시
		 * jbts_adult[?].setBackground(new Color(0, 0, 0));
		 * jbts_adult[?].setForeground(Color.white);
		 * jbts_teen[?].setBackground(new Color(0, 0, 0));
		 * jbts_teen[?].setForeground(Color.white);
		 * 
		 * 해제시
		 * jbts_adult[i].setBackground(new Color(230, 230, 230));
		 * jbts_adult[i].setForeground(Color.black);
		 * jbts_teen[i].setBackground(new Color(230, 230, 230));
		 * jbts_teen[i].setForeground(Color.black);
		 * 
		 * 인원수와 좌석이 아직 맞지 않은경우
		 * jbt_goPayChoice.setBackground(new Color(230, 230, 230));
		 * jbt_goPayChoice.setForeground(Color.gray);
		 * jbt_goPayChoice.setEnabled(false);
		 * 
		 * 좌석이 하나라도 선택된경우
		 * jl_south_seat.setBounds(900, 690, 150, 20);	
		 * jl_south_pay.setBounds(1040, 690, 150, 20);
		 * jta_south_Allseat.setText(????);
		 * jl_south_totalPay.setText(????);
		 * 예시)
		 * jta_south_Allseat.setText("A2, A3, A4");  <- 아마 4개 이후에는 \n하는게 좋을꺼같습니다람쥐
		 * jl_south_totalPay.setText("총 금액 20,000원");
		 * 
		 * 좌석선택이 하나라도 안된경우
		 * jl_south_seat.setBounds(900, 698, 150, 20);	
		 * jl_south_pay.setBounds(1040, 698, 150, 20);
		 * jta_south_Allseat.setText("");
		 * jl_south_totalPay.setText("");
		 * 
		 * 인원수와 동일하게 좌석을 선택한 경우
		 * jbt_goPayChoice.setBackground(new Color(52, 152, 219));
		 * jbt_goPayChoice.setForeground(Color.white);
		 * 
		 * 페이뷰로 페이지 전환한 경우(페이뷰인경우)
		 * jbt_backMovieChoice.setVisible(false);
		 * jbt_goPayChoice.setVisible(false);
		 * jbt_backSeatChoice.setVisible(true);
		 * jbt_goPayInfo.setVisible(true);
		 **********************************************************************************/
		
		/**********************************************************************************
		 * PAY VIEW 에서 일어나는 일들
		 * 라디오버튼 하나라도 클릭한다면
		 * jbt_backSeatChoice.setBackground(new Color(52, 152, 219));
		 * jbt_backSeatChoice.setForeground(Color.white);
		 * jbt_backSeatChoice.setEnabled(true);
		 * jbt_backSeatChoice.setVisible(true);
		 *  
		 * jbt_goPayInfo.setBackground(new Color(52, 152, 219));
		 * jbt_goPayInfo.setForeground(Color.white);
		 * jbt_goPayInfo.setVisible(true);
		 * jbt_goPayInfo.setEnabled(true);
		 * 
		 * 라디오버튼 클릭 안되어있다면
		 * jbt_backSeatChoice.setBackground(new Color(52, 152, 219));
		 * jbt_backSeatChoice.setForeground(Color.white);	
		 * jbt_backSeatChoice.setEnabled(true);
		 * jbt_backSeatChoice.setVisible(true);
		 * 
		 * jbt_goPayInfo.setBackground(new Color(230, 230, 230));
		 * jbt_goPayInfo.setForeground(Color.gray);
		 * jbt_goPayInfo.setVisible(true);
		 * jbt_goPayInfo.setEnabled(false);
		**********************************************************************************/
		
		/**********************************************************************************
		 * PAY INFO VIEW 에서 일어나는일들
		 * jl_choice_movie.setText(????);		기입 예)"이보다 더 좋을 순 없다."
		 * jl_choice_locThe.setText(????); 		기입 예)"전북/전주/강남"
		 * jl_choice_screen .setText(????);		기입 예)"1관"
		 * jl_choice_seat.setText(????);		기입 예)"A2, B1"
		 * jl_choice_date.setText(????);		기입 예)"2020년 3월 28일"
		 * jl_choice_time.setText(????);		기입 예)"12:00 ~ 13:45"
		 * jl_choice_payWay.setText(????);		기입 예)"카카오페이"
		 * jl_choice_totalPay.setText(????);	기입 예)"18,000원"
		**********************************************************************************/
		
		jbt_backMovieChoice.setVisible(false);
		jbt_backSeatChoice.setVisible(false);
		jbt_goSeatChoice.setVisible(true);
		jbt_goPayChoice.setVisible(false);
		jbt_goPayInfo.setVisible(false);
		this.add(jp_scv);
		this.add(jp_pv);
		this.add(jp_mcv);
		this.add(jl_south_movie);
		this.add(jl_south_ctf);
		this.add(jl_south_loc);
		this.add(jl_south_theater);
		this.add(jl_south_date);
		this.add(jl_south_screen);
		this.add(jl_south_time);
		this.add(jl_south_seat);
		this.add(jta_south_Allseat);
		this.add(jl_south_pay);
		this.add(jl_south_totalPay);
		this.add(jbt_backMovieChoice);
		this.add(jbt_backSeatChoice);
		this.add(jbt_goSeatChoice);
		this.add(jbt_goPayChoice);
		this.add(jbt_goPayInfo);
		this.add(jl_south);
	}
	
	public void eventMapping() {
		jbt_backMovieChoice.addActionListener(em);
		jbt_goSeatChoice.addActionListener(em);
		jbt_goPayChoice.addActionListener(em);
	}
	
	public static void main(String[] args) {
		MaxMovieView mmv = new MaxMovieView();
		mmv.jp_lv.setVisible(false);
		mmv.jp_mrv.setVisible(true);
		mmv.jp_mrv.jp_mcv.setVisible(true);
		mmv.jp_mrv.jp_scv.setVisible(false);
		mmv.jp_mrv.jp_pv.setVisible(false);
		mmv.jp_mv.setVisible(false);
		mmv.jp_mv.jp_thv.setVisible(false);
		mmv.jp_mv.jp_miv.setVisible(false);
		mmv.jp_mv.jp_muv.setVisible(false);
		mmv.jp_rv.setVisible(false);
		

		mmv.jl_logo_small.setVisible(true);
		mmv.jl_nickInfo.setVisible(true);
		mmv.jl_nickInfoEnd.setVisible(true);
		mmv.jbt_logout.setVisible(true);
		mmv.jbt_myPage.setVisible(true);
		mmv.jbt_ticketing.setVisible(true);
		
	}
}
