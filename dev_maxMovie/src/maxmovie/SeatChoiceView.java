package maxmovie;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class SeatChoiceView extends JPanel{
	String[][]					seatModel				= null;
	
	JPanel						jp_center				= new JPanel();
	JLabel						jl_seatHeader			= new JLabel("좌석");
	JLabel						jl_adult				= new JLabel("일반");
	JLabel						jl_teen					= new JLabel("청소년");
	JButton[]					jbts_adult				= new JButton[8];
	JButton[]					jbts_teen				= new JButton[8];
	JLabel						jl_remainSeat			= new JLabel("");
	
	JLabel						jl_south				= new JLabel("　　　　　　　　　　　　　　　 　　　 　　  　　　　　>　　　    　   　          　　　　　 　　　>　 　　          　   　　　　　　　　　　>");
	JButton						jbt_goMovieChoice 		= new JButton("←영화선택");
	JLabel						jl_south_movie			= new JLabel("영화선택");
	JLabel						jl_south_ctf			= new JLabel("");
	JLabel						jl_south_loc			= new JLabel("극장선택");
	JLabel						jl_south_theater		= new JLabel("");
	JLabel						jl_south_date			= new JLabel("날짜선택");
	JLabel						jl_south_screen			= new JLabel("시간선택");
	JLabel						jl_south_time			= new JLabel("");
	JButton						jbt_goPayChoice 		= new JButton("결제선택→");
	EventMapping 				em 						= null;
	
	
	List<Map<String, Object>>	seatList				= new ArrayList<>();		
	
	public SeatChoiceView() {
		initDisplay();
	}

	public SeatChoiceView(EventMapping em) {
		this.em = em;
		initDisplay();
		
	}
	//0빈자리  1결제 진행중  2결제완료
	public void seatSetting() {
		
	}
	public void initDisplay() {
		this.setLayout(null);
		this.setBackground(Color.white);
		
		/**********************************************************************************
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
		 * jl_south_date.setText("2020년 03월 28일 (금)");
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
		 * //하나라도 미선택시
		 * jbt_goSeatChoice.setBackground(new Color(230, 230, 230));
		 * jbt_goSeatChoice.setForeground(Color.gray);
		 * jbt_goSeatChoice.setEnabled(false);
		 **********************************************************************************/
//		for(int i=0; i<jbts_adult.length; i++) {
//			jbts_adult[i] = new JButton();
//			jbts_teen[i] = new JButton();
//			
//			String num = Integer.toString(i);
//			
//			jbts_adult[i].setText(Integer.toString(i));
//			jbts_teen[i].setText(Integer.toString(i));
//			
//			jbts_adult[i].setBounds(240+i*18, 80, 18, 18);
//			jbts_teen[i].setBounds(240+i*18, 92 ,18 ,18);
//			
//			
//			this.add(jbts_adult[i]);
//			this.add(jbts_teen[i]);
//		}
		
		for(int i=0; i<80; i++) {
			Map<String, Object> map = new HashMap<>();
			map.put("좌석", "A1");
			map.put("현황", "1");
			seatList.add(map);	
			System.out.println(i +" : " + seatList.get(i));
		}
		
		jl_seatHeader.setBounds(200, 25, 1146, 32);
		jp_center.setBounds(200, 60, 1146, 600);
		
		jl_south.setBounds(200, 664, 1144, 90);		
		jbt_goMovieChoice.setBounds(220, 685, 100, 45);	
		jbt_goPayChoice.setBounds(1220, 685, 100, 45);
		jl_south_movie.setBounds(350, 698, 150, 20);	
		jl_south_ctf.setBounds(350, 710, 150, 20);	
		jl_south_loc.setBounds(580, 698, 150, 20);	
		jl_south_theater.setBounds(580, 710, 150, 20);	
		jl_south_date.setBounds(810, 698, 150, 20);			
		jl_south_screen.setBounds(1040, 698, 150, 20);	
		jl_south_time.setBounds(1040, 710, 150, 20);
		
		jl_south_ctf.setVisible(false);
		jl_south_theater.setVisible(false);
		jl_south_time.setVisible(false);

		jl_seatHeader.setOpaque(true);
		jl_seatHeader.setBackground(new Color(190, 190, 190));
		
		jbt_goMovieChoice.setBackground(new Color(230, 230, 230));
		jbt_goMovieChoice.setForeground(Color.gray);
		jbt_goMovieChoice.setEnabled(false);
		
		jbt_goPayChoice.setBackground(new Color(230, 230, 230));
		jbt_goPayChoice.setForeground(Color.gray);
		jbt_goPayChoice.setEnabled(false);
		
		jl_south.setOpaque(true);
		jl_south.setBackground(new Color(190, 190, 190));

		jl_seatHeader.setHorizontalAlignment(JLabel.CENTER);
		jl_south_ctf.setHorizontalAlignment(JLabel.CENTER);
		jl_south_loc.setHorizontalAlignment(JLabel.CENTER);
		jl_south_theater.setHorizontalAlignment(JLabel.CENTER);
		jl_south_date.setHorizontalAlignment(JLabel.CENTER);
		jl_south_screen.setHorizontalAlignment(JLabel.CENTER);
		jl_south_time.setHorizontalAlignment(JLabel.CENTER);
		
		jbt_goMovieChoice.addActionListener(em);
		jbt_goPayChoice.addActionListener(em);
		
		this.add(jl_seatHeader);
		this.add(jp_center);
		this.add(jl_south_ctf);
		this.add(jl_south_loc);
		this.add(jl_south_theater);
		this.add(jl_south_date);
		this.add(jl_south_screen);
		this.add(jl_south_time);
		this.add(jbt_goMovieChoice);
		this.add(jbt_goPayChoice);
		this.add(jl_south);
		
	}
	
	

	public static void main(String[] args) {
		MaxMovieView mmv = new MaxMovieView();
		mmv.jl_logo_small.setVisible(true);
		mmv.jp_lv.setVisible(false);
		mmv.jp_mv.setVisible(false);
		mmv.jp_mcv.setVisible(false);
		mmv.jp_scv.setVisible(true);
		mmv.jp_rv.setVisible(false);
		mmv.getContentPane().revalidate();
		mmv.getContentPane().repaint();
		new SeatChoiceView(mmv.em);
	}
}