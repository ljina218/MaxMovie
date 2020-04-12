package maxmovie;

import java.awt.Color;
import java.awt.Font;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class SeatChoiceView extends JPanel{
	EventMapping				em						= null;
	
	JPanel						jp_center				= new JPanel();
	JLabel						jl_seatHeader			= new JLabel("좌석 선택");
	JLabel						jl_adult				= new JLabel("일반");
	JLabel						jl_teen					= new JLabel("청소년");
	JButton[]					jbts_adult				= new JButton[8];
	JButton[]					jbts_teen				= new JButton[8];
	JLabel[]					jl_seatLines			= new JLabel[10];					
	JButton[][]					jbts_seat				= null;
	
	JLabel						jl_remainSeat			= new JLabel("남은좌석       /");
	JLabel						jl_remainSeatCnt		= new JLabel();
	JLabel						jl_totalSeatCnt			= new JLabel();
	JLabel						jl_runningTime			= new JLabel();
	
	JLabel						jl_info1				= new JLabel();
	JLabel						jl_info2				= new JLabel();
	JLabel						jl_info3				= new JLabel("X");
	JLabel						jl_info1_1				= new JLabel("선택한 좌석");
	JLabel						jl_info2_1				= new JLabel("선택가능");
	JLabel						jl_info3_1				= new JLabel("예매불가");
	
	JLabel						jl_screen				= new JLabel("/////////////////////////////////////////////////SCREEN/////////////////////////////////////////////////");
	
	List<Map<String, Object>>	seatList				= new Vector<>();
	
	int							adultChoice				= 0;
	int							teenChoice				= 0;
	
	List<String> 				seatChoiceList			= new ArrayList<>();				
	
	
	public SeatChoiceView(EventMapping em) {
		//리스트 받는곳(매개변수추가?) public SeatChoiceView(EventMapping em, ArrayList<Map<String, Object>> seatList)
		//this.seatList = seatList;
		this.em = em;
//		seatSetting();
		initDisplay();
	}
	
	//0:빈자리  1:결제 진행중  2:결제완료

	public void seatSetting(List<Map<String, Object>> seatList) {
		
		this.seatList = seatList;
		for(Map<String, Object> rmap: this.seatList) {
			System.out.println("SeatChoiceView"+rmap.get("좌석").toString() +","+ rmap.get("현황").toString());
		}
		//[80]석 셋팅 
		if(seatList.size()==80) {
			jbts_seat = new JButton[10][8];
			int k=0;
			for(int i=0; i<10; i++) {
				char c_line = (char) (65+i);
				System.out.println(c_line);
				String line = String.valueOf(c_line);
				jl_seatLines[i] = new JLabel();
				jl_seatLines[i].setText(line);
				jl_seatLines[i].setBounds(578, 350+i*25, 20, 20);
				jl_seatLines[i].setForeground(Color.gray);
				jl_seatLines[i].setHorizontalAlignment(JLabel.CENTER);
				this.add(jl_seatLines[i]);
				for(int j=0; j<8; j++) {
					int check = Integer.parseInt(seatList.get(k++).get("현황").toString());
					int between = 0;
					System.out.println(check);
					jbts_seat[i][j] = new JButton();
					jbts_seat[i][j].addActionListener(em);
					jbts_seat[i][j].setOpaque(true);
					if(j<2) {
						jbts_seat[i][j].setBounds(600+j*25+between, 350+i*25, 20, 20);				
					} else if(j>=2&&j<6) {
						between = 70;
						jbts_seat[i][j].setBounds(600+j*25+between, 350+i*25, 20, 20);						
					} else if(j>=6&&j<8) {
						between = 140;
						jbts_seat[i][j].setBounds(600+j*25+between, 350+i*25, 20, 20);
					}
					jbts_seat[i][j].setMargin(new Insets(0, 0, 0, 0));
					jbts_seat[i][j].setHorizontalAlignment(JLabel.CENTER);
					jbts_seat[i][j].setFont(new Font("굴림체", Font.BOLD, 12));
					
					if(check==0) {
						jbts_seat[i][j].setBackground(Color.green);
						jbts_seat[i][j].setText(Integer.toString(j+1));
						jbts_seat[i][j].setForeground(Color.white);
						this.add(jbts_seat[i][j]);
					} else if(check==1) {
						jbts_seat[i][j].setBackground(Color.gray);
						jbts_seat[i][j].setText(Integer.toString(j+1));
						jbts_seat[i][j].setForeground(Color.white);
						this.add(jbts_seat[i][j]);
						
					} else if(check==2) {
						jbts_seat[i][j].setBackground(Color.white);
						jbts_seat[i][j].setText("X");
						jbts_seat[i][j].setForeground(Color.black);
						this.add(jbts_seat[i][j]);
					}
				}
			}
		}
		this.add(jp_center);
	}//end of seatSetting method
		//[120]석 셋팅 
//		if(seatList.size()==120) {
//			jbts_seat = new JButton[10][12];
//			int k=0;
//			for(int i=0; i<10; i++) {
//				char c_line = (char) (65+i);
////				System.out.println(c_line);
//				String line = String.valueOf(c_line);
//				jl_seatLines[i] = new JLabel();
//				jl_seatLines[i].setText(line);
//				jl_seatLines[i].setBounds(528, 350+i*25, 20, 20);
//				jl_seatLines[i].setForeground(Color.gray);
//				jl_seatLines[i].setHorizontalAlignment(JLabel.CENTER);
//				this.add(jl_seatLines[i]);
//				for(int j=0; j<12; j++) {
//					int check = (int)seatList.get(k++).get("현황");
//					int between = 0;
////					System.out.println(check);
//					jbts_seat[i][j] = new JButton();
//					jbts_seat[i][j].addActionListener(em);
//					jbts_seat[i][j].setOpaque(true);
//					if(j<3) {
//						jbts_seat[i][j].setBounds(550+j*25+between, 350+i*25, 20, 20);				
//					} else if(j>=3&&j<9) {
//						between = 70;
//						jbts_seat[i][j].setBounds(550+j*25+between, 350+i*25, 20, 20);						
//					} else if(j>=9&&j<12) {
//						between = 140;
//						jbts_seat[i][j].setBounds(550+j*25+between, 350+i*25, 20, 20);
//					}
//					jbts_seat[i][j].setMargin(new Insets(0, 0, 0, 0));
//					jbts_seat[i][j].setHorizontalAlignment(JLabel.CENTER);
//					jbts_seat[i][j].setFont(new Font("굴림체", Font.BOLD, 12));
//					
//					if(check==0) {
//						jbts_seat[i][j].setBackground(Color.green);
//						jbts_seat[i][j].setText(Integer.toString(j+1));
//						jbts_seat[i][j].setForeground(Color.white);
//						this.add(jbts_seat[i][j]);
//					} else if(check==1) {
//						jbts_seat[i][j].setBackground(Color.gray);
//						jbts_seat[i][j].setText(Integer.toString(j+1));
//						jbts_seat[i][j].setForeground(Color.white);
//						this.add(jbts_seat[i][j]);
//						
//					} else if(check==2) {
//						jbts_seat[i][j].setBackground(Color.white);
//						jbts_seat[i][j].setText("X");
//						jbts_seat[i][j].setForeground(Color.black);
//						this.add(jbts_seat[i][j]);
//					}
//				}
//			}
//		}
	
//		[140]석 셋팅 
//		if(seatList.size()==140) {
//			jbts_seat = new JButton[10][14];
//			int k=0;
//			for(int i=0; i<10; i++) {
//				char c_line = (char) (65+i);
//				System.out.println(c_line);
//				String line = String.valueOf(c_line);
//				jl_seatLines[i] = new JLabel();
//				jl_seatLines[i].setText(line);
//				jl_seatLines[i].setBounds(503, 350+i*25, 20, 20);
//				jl_seatLines[i].setForeground(Color.gray);
//				jl_seatLines[i].setHorizontalAlignment(JLabel.CENTER);
//				this.add(jl_seatLines[i]);
//				for(int j=0; j<14; j++) {
//					int check = (int)seatList.get(k++).get("현황");
//					int between = 0;
//					System.out.println(check);
//					jbts_seat[i][j] = new JButton();
//					jbts_seat[i][j].addActionListener(em);
//					jbts_seat[i][j].setOpaque(true);
//					if(j<3) {
//						jbts_seat[i][j].setBounds(525+j*25+between, 350+i*25, 20, 20);				
//					} else if(j>=3&&j<11) {
//						between = 70;
//						jbts_seat[i][j].setBounds(525+j*25+between, 350+i*25, 20, 20);						
//					} else if(j>=11&&j<14) {
//						between = 140;
//						jbts_seat[i][j].setBounds(525+j*25+between, 350+i*25, 20, 20);
//					}
//					jbts_seat[i][j].setMargin(new Insets(0, 0, 0, 0));
//					jbts_seat[i][j].setHorizontalAlignment(JLabel.CENTER);
//					jbts_seat[i][j].setFont(new Font("굴림체", Font.BOLD, 12));
//					
//					if(check==0) {
//						jbts_seat[i][j].setBackground(Color.green);
//						jbts_seat[i][j].setText(Integer.toString(j+1));
//						jbts_seat[i][j].setForeground(Color.white);
//						this.add(jbts_seat[i][j]);
//					} else if(check==1) {
//						jbts_seat[i][j].setBackground(Color.gray);
//						jbts_seat[i][j].setText(Integer.toString(j+1));
//						jbts_seat[i][j].setForeground(Color.white);
//						this.add(jbts_seat[i][j]);
//						
//					} else if(check==2) {
//						jbts_seat[i][j].setBackground(Color.white);
//						jbts_seat[i][j].setText("X");
//						jbts_seat[i][j].setForeground(Color.black);
//						this.add(jbts_seat[i][j]);
//					}
//				}
//			}
//		}
//	}
	
	
		//[160]석 셋팅 
//		if(seatList.size()==160) {
//			jbts_seat = new JButton[10][16];
//			int k=0;
//			for(int i=0; i<10; i++) { //0~9 a~j 
//				//A~J까지 라벨 박는 c_line
//				char c_line = (char) (65+i);
////				System.out.println(c_line);
//				String line = String.valueOf(c_line);
//				jl_seatLines[i] = new JLabel();
//				jl_seatLines[i].setText(line);
//				jl_seatLines[i].setBounds(478, 350+i*25, 20, 20);
//				jl_seatLines[i].setForeground(Color.gray);
//				jl_seatLines[i].setHorizontalAlignment(JLabel.CENTER);
//				this.add(jl_seatLines[i]);
//				//알파벳 박음
//				for(int j=0; j<16; j++) {
//					//리스트 순서대로 160석인경우 k는 0에서 ++ 된다.
//					int check = (int)seatList.get(k++).get("현황");
////					System.out.println((int)seatList.get(k++).get("현황"));
//					System.out.println("k : " + k);
//					System.out.println("check : " + check);
//					
//					int between = 0;
////					System.out.println(check);
//					jbts_seat[i][j] = new JButton();
//					jbts_seat[i][j].addActionListener(em);
//					jbts_seat[i][j].setOpaque(true);
//					if(j<3) {
//						jbts_seat[i][j].setBounds(500+j*25+between, 350+i*25, 20, 20);				
//					} else if(j>=3&&j<13) {
//						between = 70;
//						jbts_seat[i][j].setBounds(500+j*25+between, 350+i*25, 20, 20);						
//					} else if(j>=13&&j<16) {
//						between = 140;
//						jbts_seat[i][j].setBounds(500+j*25+between, 350+i*25, 20, 20);
//					}
//					jbts_seat[i][j].setMargin(new Insets(0, 0, 0, 0));
//					jbts_seat[i][j].setHorizontalAlignment(JLabel.CENTER);
//					jbts_seat[i][j].setFont(new Font("굴림체", Font.BOLD, 12));
//					if(check==0) { //좌석이 빈자리일때
//						jbts_seat[i][j].setBackground(Color.green);
//						jbts_seat[i][j].setText(Integer.toString(j+1));
//						jbts_seat[i][j].setForeground(Color.white);
//					} else if(check==1) { //좌석이 결제중일때
//						jbts_seat[i][j].setBackground(Color.gray);
//						jbts_seat[i][j].setText(Integer.toString(j+1));
//						jbts_seat[i][j].setForeground(Color.white);						
//					} else if(check==2) { //좌석이 결제완료일때
//						jbts_seat[i][j].setBackground(Color.white);
//						jbts_seat[i][j].setText("X");
//						jbts_seat[i][j].setForeground(Color.black);
//					}
//					this.add(jbts_seat[i][j]);
//				}
//			}
//		}
//	}
		
	
	public void initDisplay() {
		this.setLayout(null);
		this.setBackground(Color.white);
		
		/**********************************************************************************
		 * //영화 선택시
		 * jl_south_movie.setBounds(350, 690, 150, 20);
		 * jl_south_movie.setText("이보다 더 좋을 순 없다.");
		 * jl_south_ctf.setVisible(true);
		 * jl_south_ctf.setText("전체이용가");
		 * 
		 * //지역&&지점 선택시
		 * jl_south_loc.setBounds(580, 690, 150, 20);
		 * jl_south_loc.setText("전북/전주");
		 * jl_south_theater.setVisible(true);
		 * jl_south_theater.setText("건대입구");
		 * 
		 * //날짜 선택시
		 * jl_south_date.setText("2020년 03월 28일 (금)");
		 * 
		 * //시간 선택시
		 * jl_south_screen.setBounds(1040, 690, 150, 20);
		 * jl_south_screen.setText("1관");
		 * jl_south_time.setVisible(true);
		 * jl_south_time.setText("오전 09:00:00");
		 * 
		 * //영화 + 극장 + 날짜  모두 선택시
		 * jsp_time.setVisible(true);
		 * jl_timeLock.setVisible(false);
		 * 
		 * //모두 선택시
		 * jbt_goSeatChoice.setForeground(Color.white);
		 * jbt_goSeatChoice.setBackground(new Color(52, 152, 219));
		 * jbt_goSeatChoice.setEnabled(true);
		 **********************************************************************************/
		//티켓개수버튼
		for(int i=0; i<jbts_adult.length; i++) {
			jbts_adult[i] = new JButton(Integer.toString(i));
			jbts_teen[i] = new JButton(Integer.toString(i));
			jbts_adult[i].setMargin(new Insets(0, 0, 0, 0));
			jbts_teen[i].setMargin(new Insets(0, 0, 0, 0));
			jbts_adult[i].setOpaque(true);
			jbts_teen[i].setOpaque(true);
			jbts_adult[i].setFont(new Font("굴림체", Font.BOLD, 10));
			jbts_adult[i].setForeground(Color.black);
			jbts_teen[i].setFont(new Font("굴림체", Font.BOLD, 10));
			jbts_teen[i].setForeground(Color.black);
			jbts_adult[i].setBackground(new Color(230, 230, 230));
			jbts_adult[i].setForeground(Color.black);
			jbts_teen[i].setBackground(new Color(230, 230, 230));
			jbts_teen[i].setForeground(Color.black);
			jbts_adult[i].setBounds(370+i*17, 93, 15, 15);
			jbts_teen[i].setBounds(370+i*17, 134 ,15 ,15);
			jbts_adult[i].addActionListener(em);
			jbts_teen[i].addActionListener(em);
			this.add(jbts_adult[i]);
			this.add(jbts_teen[i]);
		}
		

		jbts_adult[adultChoice].setBackground(Color.yellow);
		jbts_teen[teenChoice].setBackground(Color.yellow);
		/********************************************************
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
		 * 인원수와 좌석개수가 일치할 시
		 * jbt_goPayChoice.setForeground(Color.white);
		 * jbt_goPayChoice.setBackground(new Color(52, 152, 219));
		 * jbt_goPayChoice.setEnabled(true);
		 * 
		 * jbt_goPayChoice.setForeground(Color.white);
		 * jbt_goPayChoice.setBackground(new Color(52, 152, 219));
		 * jbt_goPayChoice.setEnabled(true);
		 ********************************************************/
		jl_adult.setBounds(320, 80, 40, 40);
		jl_teen.setBounds(320, 122, 40, 40);
		jl_remainSeat.setBounds(650, 80, 250, 40);
		jl_remainSeatCnt.setBounds(735, 80, 40, 40);
		jl_totalSeatCnt.setBounds(775, 80, 40, 40);
		jl_runningTime.setBounds(650, 120, 440, 40);
		jl_info1.setBounds(1100, 90, 15, 15);
		jl_info2.setBounds(1100, 115, 15, 15);
		jl_info3.setBounds(1100, 140, 15, 15);
		jl_info1_1.setBounds(1125, 90, 70, 15);
		jl_info2_1.setBounds(1125, 115, 60, 15);
		jl_info3_1.setBounds(1125, 140, 60, 15);
		jl_screen.setBounds(300, 200, 946, 18);
		jl_seatHeader.setBounds(200, 25, 1146, 32);
		jp_center.setBounds(200, 60, 1146, 600);
		
		/* 
		 * jl_south_movie.setBounds(350, 698, 150, 20);
		 * jl_south_ctf.setBounds(350, 710, 150, 20);
		 * jl_south_loc.setBounds(580, 698, 150, 20);
		 * jl_south_theater.setBounds(580, 710, 150, 20);
		 * jl_south_date.setBounds(810, 698, 150, 20);
		 * jl_south_screen.setBounds(1040, 698, 150, 20);
		 * jl_south_time.setBounds(1040, 710, 150, 20);
		 */
		jl_remainSeatCnt.setText("120");
		jl_totalSeatCnt.setText("144");
		jl_runningTime.setText("2020.03.28 (토) 12:00 ~ 13:45  ");
		jl_seatHeader.setOpaque(true);
		jl_seatHeader.setBackground(new Color(190, 190, 190));
		jl_seatHeader.setHorizontalAlignment(JLabel.CENTER);
		jl_info1_1.setFont(new Font("굴림체", Font.BOLD, 10));
		jl_info1_1.setForeground(Color.gray);
		jl_info2_1.setFont(new Font("굴림체", Font.BOLD, 10));
		jl_info2_1.setForeground(Color.gray);
		jl_info3_1.setFont(new Font("굴림체", Font.BOLD, 10));
		jl_info3_1.setForeground(Color.gray);
		jl_info3.setHorizontalAlignment(JLabel.CENTER);
		jl_info1.setBackground(Color.green);
		jl_info2.setBackground(Color.white);
		jl_info3.setBackground(Color.white);
		jl_remainSeat.setFont(new Font("굴림체", Font.BOLD, 14));
		jl_remainSeat.setForeground(Color.black);
		jl_remainSeatCnt.setFont(new Font("굴림체", Font.BOLD, 14));
		jl_remainSeatCnt.setForeground(Color.red);
		jl_totalSeatCnt.setFont(new Font("굴림체", Font.BOLD, 14));
		jl_totalSeatCnt.setForeground(Color.black);
		jl_runningTime.setFont(new Font("굴림체", Font.BOLD, 24));
		jl_runningTime.setForeground(Color.black);
		jl_screen.setBackground(new Color(210, 210, 210));
		jl_screen.setFont(new Font("굴림체", Font.PLAIN, 18));
		jl_screen.setForeground(Color.gray);
		jl_screen.setHorizontalAlignment(JLabel.CENTER);
		jl_info1.setOpaque(true);
		jl_info2.setOpaque(true);
		jl_info3.setOpaque(true);
		jl_screen.setOpaque(true);
		jp_center.setOpaque(true);
		jp_center.setBackground(new Color(225, 225, 225));
		this.add(jl_seatHeader);
		this.add(jl_adult);
		this.add(jl_teen);
		this.add(jl_remainSeat);
		this.add(jl_remainSeatCnt);
		this.add(jl_totalSeatCnt);
		this.add(jl_runningTime);
		this.add(jl_info1);
		this.add(jl_info2);
		this.add(jl_info3);
		this.add(jl_info1_1);
		this.add(jl_info2_1);
		this.add(jl_info3_1);
		this.add(jl_screen);
//		this.add(jp_center);
	}

	
	
	
	public static void main(String[] args) {
		MaxMovieView mmv = new MaxMovieView();
		mmv.jp_lv.setVisible(false);
		mmv.jp_mrv.setVisible(true);
		mmv.jp_mrv.jp_mcv.setVisible(false);
		mmv.jp_mrv.jp_scv.setVisible(true);
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
		mmv.jp_mrv.jbt_backMovieChoice.setVisible(true);
		mmv.jp_mrv.jbt_goSeatChoice.setVisible(false);
		mmv.jp_mrv.jbt_goPayChoice.setVisible(true);
	}
}