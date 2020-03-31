package maxmovie;


import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class MovieChoiceView extends JPanel implements ActionListener, MouseListener{
	JLabel				jl_movie				= new JLabel("영화");
	JLabel				jl_locThe				= new JLabel("극장");
	JLabel				jl_date					= new JLabel("날짜");
	JLabel				jl_time					= new JLabel("시간");
	JLabel				jl_timeLock				= new JLabel("영화 극장 날짜를 선택해 주세요.");
	JLabel				jl_south				= new JLabel("　　　　　　　　　　　　　　　 　　　 　　  　　　　　>　　　    　   　          　　　　　 　　　>　 　　          　   　　　　　　　　　　>");
	JLabel				jl_south_movie			= new JLabel("영화선택");
	JLabel				jl_south_ctf			= new JLabel("");
	JLabel				jl_south_loc			= new JLabel("극장선택");
	JLabel				jl_south_theater		= new JLabel("");
	JLabel				jl_south_date			= new JLabel("날짜선택");
	JLabel				jl_south_screen			= new JLabel("시간선택");
	JLabel				jl_south_time			= new JLabel("");
	JButton				jbt_goSeatChoice 		= new JButton("좌석선택→");
	ImageIcon			grade19					= new ImageIcon("C:\\git_MaxMovie\\dev_maxMovie\\src\\maxmovie\\grade19.png");
	ImageIcon			grade15					= new ImageIcon("C:\\git_MaxMovie\\dev_maxMovie\\src\\maxmovie\\grade15.png");
	ImageIcon			grade12					= new ImageIcon("C:\\git_MaxMovie\\dev_maxMovie\\src\\maxmovie\\grade12.png");
	ImageIcon			grade0					= new ImageIcon("C:\\git_MaxMovie\\dev_maxMovie\\src\\maxmovie\\grade0.png");

	String 				col_movie[] 			= {"이용등급", "영화"};
	Object 				data_movie[][] 			= {{grade15, "이보다 더 좋을 순 없다."}, {grade12, "대디스홈2"}, {grade12, "정직한 후보"}, {grade15, "그녀"}, {grade15, "그랜드부다페스트호텔"}, {grade0, "작은아씨들"}};
	DefaultTableModel 	dtm_movie  				= new DefaultTableModel(data_movie, col_movie) {
        @Override
        public Class<?> getColumnClass(int column) {
            switch (column) {
            	case 0:
            	return ImageIcon.class;
                case 1:
                    return String.class;
                default:
                    return String.class;
            }
        }
    };
	JTable 				jt_movie 				= new JTable(dtm_movie);
	JScrollPane 		jsp_movie 				= new JScrollPane(jt_movie);
	
	String 				col_local[] 			= {"지역"};
	String 				data_local[][] 			= {{"서울"}, {"경기"}, {"인천"}, {"강원"}, {"대전/충청"}, {"부산/울산"}, {"경상"}, {"광주/전라"}, {"제주"}};
	DefaultTableModel 	dtm_local  				= new DefaultTableModel(data_local, col_local);
	JTable 				jt_local				= new JTable(dtm_local);
	JScrollPane 		jsp_local				= new JScrollPane(jt_local);
	
	String 				col_theater[] 			= {"지점"};
	String 				data_theater[][] 		= {{"건대입구"}, {"영등포"}, {"가산"}, {"홍대입구"}, {"구로"}, {"독산"}, {"마포"}, {"강남"}, {"서초"}, {"불광"}, {"신사"}, {"잠실"}, {"녹번"}, {"경복궁"}, {"안국"}, {"신림"} };
	DefaultTableModel 	dtm_theater  			= new DefaultTableModel(data_theater, col_theater);
	JTable 				jt_theater 			= new JTable(dtm_theater);
	JScrollPane 		jsp_theater 			= new JScrollPane(jt_theater);
	
	String 				col_date[] 				= {"날짜"};
	String 				data_date[][] 			= {{"2020년"}, {"3월"}, {"21일"}, {"22일"}, {"23일"}, {"24일"}, {"25일"}, {"26일"}, {"27일"}, {"28일"}, {"29일"}, {"30일"}, {"31일"},
												   {"4월"}, {"1일"}, {"2일"}, {"3일"}, {"4일"}, {"5일"}, {"6일"}, {"7일"}, {"8일"}, {"9일"} };
	DefaultTableModel 	dtm_date 				= new DefaultTableModel(data_date, col_date);
	JTable 				jt_date 				= new JTable(dtm_date);
	JScrollPane 		jsp_date 				= new JScrollPane(jt_date);
	
	String 				col_time[] 				= {"시간"};
	String 				data_time[][] 			= {{"1관"}, {"09:00"}, {"12:00"}, {"15:00"}, {"18:00"}, {"21:00"}, {"2관"}, {"09:00"}, {"12:00"}, {"15:00"}, {"18:00"}, {"21:00"}, {"3관"}, {"09:00"}, {"12:00"}, {"15:00"}, {"18:00"}, {"21:00"}};
	DefaultTableModel 	dtm_time  				= new DefaultTableModel(data_time, col_time);
	JTable 				jt_time 				= new JTable(dtm_time);
	JScrollPane 		jsp_time 				= new JScrollPane(jt_time);
	
	EventMapping 		em 						= null;
	
	int movieIndex = 0;
	String movieChoice = "";
	int localIndex = 0;
	String localChoice = "서울";
	int theaterIndex = 0;
	String theaterChoice = "건대/입구";
	int dateIndex = 0;
	String dateChoice = "";
	int timeIndex = 0;
	String timeChoice = "";
	
	public MovieChoiceView() {
		initDisplay();
	}

	public MovieChoiceView(EventMapping em) {
		this.em = em;
		initDisplay();
	}
	public void initDisplay() {
		this.setLayout(null);
		this.setBackground(Color.white);
		

		jl_movie.setBounds(200, 25, 295, 32);
		jl_locThe.setBounds(500, 25, 346, 32);
		jl_date.setBounds(850, 25, 146, 32);
		jl_time.setBounds(1000, 25, 345, 32);
		jsp_movie.setBounds(200, 60, 296, 600);
		jsp_local.setBounds(500, 60, 174, 600);
		jsp_theater.setBounds(673, 60, 174, 600);
		jsp_date.setBounds(850, 60, 146, 600);
		jsp_time.setBounds(1000, 60, 346, 600);
		jl_timeLock.setBounds(1000, 60, 346, 600);
		jl_south.setBounds(200, 664, 1144, 90);		
		jl_south_movie.setBounds(350, 698, 150, 20);		
		jl_south_ctf.setBounds(350, 710, 150, 20);	
		jl_south_loc.setBounds(580, 698, 150, 20);	
		jl_south_theater.setBounds(580, 710, 150, 20);	
		jl_south_date.setBounds(810, 698, 150, 20);			
		jl_south_screen.setBounds(1040, 698, 150, 20);	
		jl_south_time.setBounds(1040, 710, 150, 20);	
		jbt_goSeatChoice.setBounds(1220, 685, 100, 45);
		jl_timeLock.setVisible(true);
		jl_south_ctf.setVisible(false);
		jl_south_theater.setVisible(false);
		jl_south_time.setVisible(false);
		
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
	
	
		
		jt_movie.setBackground(Color.white);
		DefaultTableCellRenderer dtcr_movie = new DefaultTableCellRenderer();
		jt_movie.getColumn("영화").setCellRenderer(dtcr_movie);
		jt_movie.setRowHeight(28);
		jt_movie.setTableHeader(null);
		jt_movie.setShowVerticalLines(false);
		jt_movie.setShowHorizontalLines(false);
		jt_movie.getColumn("이용등급").setPreferredWidth(26);
		jt_movie.getColumn("영화").setPreferredWidth(268);

		
		jt_local.setBackground(Color.white);
		DefaultTableCellRenderer dtcr_local = new DefaultTableCellRenderer();
		dtcr_local.setHorizontalAlignment(JLabel.CENTER);
		jt_local.getColumn("지역").setCellRenderer(dtcr_local);
		jt_local.setRowHeight(35);
		jt_local.setTableHeader(null);
		jt_local.setShowVerticalLines(false);
		jt_local.setShowHorizontalLines(false);
		
		jt_theater.setBackground(Color.white);
		DefaultTableCellRenderer dtcr_theater = new DefaultTableCellRenderer();
		dtcr_theater.setHorizontalAlignment(JLabel.CENTER);
		jt_theater.getColumn("지점").setCellRenderer(dtcr_theater);
		jt_theater.setRowHeight(35);
		jt_theater.setTableHeader(null);
		jt_theater.setShowVerticalLines(false);
		jt_theater.setShowHorizontalLines(false);
		
		jt_date.setBackground(Color.white);
		DefaultTableCellRenderer dtcr_date = new DefaultTableCellRenderer();
		dtcr_date.setHorizontalAlignment(JLabel.CENTER);
		jt_date.getColumn("날짜").setCellRenderer(dtcr_date);
		jt_date.setRowHeight(35);
		jt_date.setTableHeader(null);
		jt_date.setShowVerticalLines(false);
		jt_date.setShowHorizontalLines(false);
		
		jt_time.setBackground(Color.white);
		DefaultTableCellRenderer dtcr_time = new DefaultTableCellRenderer();
		dtcr_time.setHorizontalAlignment(JLabel.CENTER);
		jt_time.getColumn("시간").setCellRenderer(dtcr_time);
		jt_time.setRowHeight(35);
		jt_time.setTableHeader(null);
		jt_time.setShowVerticalLines(false);
		jt_time.setShowHorizontalLines(false);
		
		jbt_goSeatChoice.setBackground(new Color(230, 230, 230));
		jbt_goSeatChoice.setForeground(Color.gray);
		jbt_goSeatChoice.setEnabled(false);
		
		jl_movie.setOpaque(true);	
		jl_locThe.setOpaque(true);
		jl_date.setOpaque(true);
		jl_time.setOpaque(true);
		jl_timeLock.setOpaque(true);
		jl_south.setOpaque(true);

		jsp_time.setVisible(false);
		
		jl_movie.setBackground(new Color(190, 190, 190));
		jl_locThe.setBackground(new Color(190, 190, 190));
		jl_date.setBackground(new Color(190, 190, 190));
		jl_time.setBackground(new Color(190, 190, 190));
		jl_timeLock.setBackground(new Color(80, 80, 80));
		jl_south.setBackground(new Color(190, 190, 190));
		jl_timeLock.setForeground(Color.black);
		jl_timeLock.setFont(new Font("굴림체", Font.BOLD, 13));

		jl_movie.setHorizontalAlignment(JLabel.CENTER);
		jl_locThe.setHorizontalAlignment(JLabel.CENTER);
		jl_date.setHorizontalAlignment(JLabel.CENTER);
		jl_time.setHorizontalAlignment(JLabel.CENTER);
		jl_timeLock.setHorizontalAlignment(JLabel.CENTER);
		
		jl_south_movie.setHorizontalAlignment(JLabel.CENTER);
		jl_south_ctf.setHorizontalAlignment(JLabel.CENTER);
		jl_south_loc.setHorizontalAlignment(JLabel.CENTER);
		jl_south_theater.setHorizontalAlignment(JLabel.CENTER);
		jl_south_date.setHorizontalAlignment(JLabel.CENTER);
		jl_south_screen.setHorizontalAlignment(JLabel.CENTER);
		jl_south_time.setHorizontalAlignment(JLabel.CENTER);
		
		jt_movie.addMouseListener(this);
		jt_local.addMouseListener(this);
		jt_theater.addMouseListener(this);
		jt_date.addMouseListener(this);
		jt_time.addMouseListener(this);
		jbt_goSeatChoice.addActionListener(this);
		
		this.add(jl_movie);
		this.add(jl_locThe);
		this.add(jl_date);
		this.add(jl_time);
		this.add(jsp_movie);
		this.add(jsp_local);
		this.add(jsp_theater);
		this.add(jsp_date);
		this.add(jsp_time);
		this.add(jl_timeLock);
		this.add(jl_south_movie);
		this.add(jl_south_ctf);
		this.add(jl_south_loc);
		this.add(jl_south_theater);
		this.add(jl_south_date);
		this.add(jl_south_screen);
		this.add(jl_south_time);
		this.add(jbt_goSeatChoice);
		this.add(jl_south);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();
		if(obj==jbt_goSeatChoice) {
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
		 *	jbt_goSeatChoice.setForeground(Color.white);
		 *	jbt_goSeatChoice.setBackground(new Color(52, 152, 219));
		 *	jbt_goSeatChoice.setEnabled(true);
		 * //하나라도 미선택시
		 *	jbt_goSeatChoice.setBackground(new Color(230, 230, 230));
		 *	jbt_goSeatChoice.setForeground(Color.gray);
		 *	jbt_goSeatChoice.setEnabled(false);
		 **********************************************************************************/
		}
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		Object obj = e.getSource();
		if(obj==jt_movie) {
			movieIndex = jt_movie.getSelectedRow();
			movieChoice = jt_movie.getValueAt(movieIndex, 0).toString();
			jt_movie.setSelectionBackground(Color.gray);
			jt_movie.setSelectionForeground(Color.white);
			jl_south_movie.setBounds(350, 690, 150, 20);
			//영화 가져오시는 걸로 바꾸어야됨 ↓
			jl_south_movie.setText("이보다 더 좋을 순 없다.");
			jl_south_ctf.setVisible(true);
			jl_south_ctf.setText("전체이용가");
			
			
			
			
			
		}
		if(obj==jt_local) {
			localIndex = jt_local.getSelectedRow();
			localChoice = jt_local.getValueAt(localIndex, 0).toString();
			jt_local.setSelectionBackground(Color.gray);
			jt_local.setSelectionForeground(Color.white);
			
			 
			
			
			
			
		}
		if(obj==jt_theater) {
			theaterIndex = jt_theater.getSelectedRow();
			theaterChoice = jt_theater.getValueAt(theaterIndex, 0).toString();
			jt_theater.setSelectionBackground(Color.gray);
			jt_theater.setSelectionForeground(Color.white);
			jl_south_loc.setBounds(580, 690, 150, 20);
			//지점과 극장 가져오시는 걸로 바꾸어야됨 ↓
			jl_south_loc.setText(localChoice);
			jl_south_theater.setVisible(true);
			jl_south_theater.setText(theaterChoice);
			
			
			
			
			
			
		}
		if(obj==jt_date) {
			dateIndex = jt_date.getSelectedRow();
			dateChoice = jt_date.getValueAt(dateIndex, 0).toString();
			jt_date.setSelectionBackground(Color.gray);
			jt_date.setSelectionForeground(Color.white);
			//데이터 가져오시는 걸로 바꾸어야됨 ↓
			jl_south_date.setText("2020년 03월 28일 (금)");
			jsp_time.setVisible(true);
			jl_timeLock.setVisible(false);
			
			
			
			
			
		}
		if(obj==jt_time) {
			timeIndex = jt_time.getSelectedRow();
			timeChoice = jt_time.getValueAt(timeIndex, 0).toString();
			jl_south_screen.setBounds(1040, 690, 150, 20);
			jl_south_screen.setText("1관");
			jl_south_time.setVisible(true);
			//데이터 가져오시는 걸로 바꾸어야됨 ↓
			jl_south_time.setText("오전 09:00:00");
			jt_time.setSelectionBackground(Color.gray);
			jt_time.setSelectionForeground(Color.white);
			
			
			
			
			
		}
	}
	@Override
	public void mouseEntered(MouseEvent e) {}
	@Override
	public void mouseExited(MouseEvent e) {}
	@Override
	public void mousePressed(MouseEvent e) {}
	@Override
	public void mouseReleased(MouseEvent e) {}

	
	public static void main(String[] args) {
		MaxMovieView mmv = new MaxMovieView();
		mmv.jl_logo_small.setVisible(true);
		mmv.jp_lv.setVisible(false);
		mmv.jp_mv.setVisible(false);
		mmv.jp_scv.setVisible(false);
		mmv.jp_rv.setVisible(false);
		mmv.jp_mcv.setVisible(true);
		mmv.getContentPane().revalidate();
		mmv.getContentPane().repaint();
		new MovieChoiceView(mmv.em);
	}
}