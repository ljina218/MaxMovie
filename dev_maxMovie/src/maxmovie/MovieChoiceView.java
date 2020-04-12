package maxmovie;


import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

public class MovieChoiceView extends JPanel implements TableCellRenderer{
	Vector<String> arealist = null;//지역정보 저장
	Vector<String> loclist = null;//지점정보 저장

	JLabel				jl_movie				= new JLabel("영화");
	JLabel				jl_locThe				= new JLabel("극장");
	JLabel				jl_date					= new JLabel("날짜");
	JLabel				jl_time					= new JLabel("시간");
	JLabel				jl_timeLock				= new JLabel("영화 극장 날짜를 선택해 주세요.");

	ImageIcon			_19					= new ImageIcon("C:\\git_MaxMovie\\dev_maxMovie\\src\\maxmovie\\grade19.png");
	ImageIcon			_15					= new ImageIcon("C:\\git_MaxMovie\\dev_maxMovie\\src\\maxmovie\\grade15.png");
	ImageIcon			_12					= new ImageIcon("C:\\git_MaxMovie\\dev_maxMovie\\src\\maxmovie\\grade12.png");
	ImageIcon			_0					= new ImageIcon("C:\\git_MaxMovie\\dev_maxMovie\\src\\maxmovie\\grade0.png");

	
	String 				col_movie[] 			= {"이용등급", "영화"};
	Object 				data_movie[][] 			= new String[0][2];
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
        public boolean isCellEditable(int rowIndex, int mColindex) {
			return false;
		}
    };
	JTable 				jt_movie 				= new JTable(dtm_movie);
	JScrollPane 		jsp_movie 				= new JScrollPane(jt_movie);
	
	String 				col_local[] 			= {"지역"};
	String 				data_local[][] 			= {{"서울"}, {"전주"}, {"부산"}};
//	String 				data_local[][] 			= new String[0][1];

	DefaultTableModel 	dtm_local  				= new DefaultTableModel(data_local, col_local);

	JTable 				jt_local				= new JTable(dtm_local);
	JScrollPane 		jsp_local				= new JScrollPane(jt_local);
	
	String 				col_theater[] 			= {"지점"};
	String 				data_theater[][] 		= {{"강남"}, {"건대"}, {"영등포"}};
//	String 				data_theater[][] 		= new String[0][1];

	DefaultTableModel 	dtm_theater  			= new DefaultTableModel(data_theater, col_theater);
	JTable 				jt_theater 				= new JTable(dtm_theater);
	JScrollPane 		jsp_theater 			= new JScrollPane(jt_theater);
	
	String 				col_date[] 				= {"날짜"};
	String 				data_date[][] 			= new String[0][1];

	DefaultTableModel 	dtm_date 				= new DefaultTableModel(data_date, col_date);
	JTable 				jt_date 				= new JTable(dtm_date);
	JScrollPane 		jsp_date 				= new JScrollPane(jt_date);
	
	String 				col_time[] 				= {"상영관","시간"};
	String 				data_time[][] 			= new String[0][2];
	DefaultTableModel 	dtm_time  				= new DefaultTableModel(data_time, col_time);
	JTable 				jt_time 				= new JTable(dtm_time);
	JScrollPane 		jsp_time 				= new JScrollPane(jt_time);
	
	EventMapping 		em 						= null;


	int					movieResult				= 0;
	int					locResult				= 0;
	int					theaterResult			= 0;
	int					dateResult				= 0;
	int					timeResult				= 0;
	
	
	int movieIndex = 0;
	String movieChoice = "";
	int localIndex = 0;
	String localChoice = "";
	int theaterIndex = 0;
	String theaterChoice = "";
	int dateIndex = 0;
	String dateChoice = "";
	int timeIndex = 0;
	String timeChoice = "";
	
	List<String> movieList = new ArrayList<>(); 
	List<String> localList = new ArrayList<>(); 
	List<String> teatherList = new ArrayList<>(); 
	List<String> dateList = new ArrayList<>(); 
	List<String> timeList = new ArrayList<>();
	
	public MovieChoiceView(EventMapping em) {
		this.em = em;
		initDisplay();
		eventMapping();
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
	
		jl_timeLock.setVisible(false);///////////////////////////////////////
		
		jsp_movie.getViewport().setBackground(Color.white);
		jt_movie.setBackground(Color.white);
		DefaultTableCellRenderer dtcr_movie = new DefaultTableCellRenderer();
		jt_movie.getColumn("영화").setCellRenderer(dtcr_movie);
		jt_movie.setRowHeight(28);
		jt_movie.setTableHeader(null);
		jt_movie.setShowVerticalLines(false);
		jt_movie.setShowHorizontalLines(false);
		jt_movie.getColumn("이용등급").setPreferredWidth(26);
		jt_movie.getColumn("영화").setPreferredWidth(268);
		jt_local.setBackground(Color.lightGray);
		DefaultTableCellRenderer dtcr_local = new DefaultTableCellRenderer() {
		    @Override
		    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column){
		        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		        ((JComponent)c).setBorder(new LineBorder(Color.black,0));
                c.setBackground(Color.lightGray);
		        if (!isSelected) {
		        	if(locResult==0) {
		        		//맨처음 맨위에 선택
			            if(row==0) {
			            	((JComponent)c).setBorder(new LineBorder(Color.black,3));
			                c.setBackground(Color.white);
			                localChoice = (String)value;
			                System.out.println("선택한 (String)value : " + (String)value);
			            }
			            //마지막커서면 result = 1로 변경
			            if(dtm_local.getRowCount() == (row+1)) {
			            	locResult=1;
			            }
		        	}
		        } else {
		        	if(locResult==2) {
		        				        		
		        		((JComponent)c).setBorder(new LineBorder(Color.black,3));
		        		c.setBackground(Color.white);
		                localChoice = (String)value;
		        		System.out.println("선택한 row : " + row);
		                System.out.println("선택한 (String)value : " + (String)value);
		                System.out.println("선택한 column : " + column);
		                if(localChoice == "부산") {
		                	  for(int i=0; i<dtm_theater.getRowCount(); i++) {
				                	dtm_theater.removeRow(0);
				                }
		                	/*************************************
		                	 * 1.localChoice의 정보(아마도 ticketingVo.setLocal?한정보를 get?)와
		                	 * 현재 선택되어진 정보들 (movieChoice, movieLocal, movieTheater, movieDate, movieTime)을 가지고
		                	 * ""&null인지 체크하고 !=null 이면 그정보를 가져와서 DB에 들려 새로운 목록들을 List 또는 vector로 뽑는다.(뭘로 뽑는지 몰름)
		                	 * 2.리스트형이면 벡터형으로 바꿔넣는다. OR 벡터형이라면 다이렉트로 로우를 박는다.
		                	 * //로우로 박는놈들이 있고, 로우로 안박고 순서만 다시 정렬하여 색상으로 분류만 해놓는 두가지경우 모두 사용해야한다.
		                	 * 영화 : 색상분류, 지역 : 색상분류(선택하였을때만), 지점 : 색상  
		                	 * 3.박은뒤 로우색 변경한다.
		                	 *밑은 리스트형인경우 뽑아서 넣기
		                	 ****************************************/
		                	List<String> movieList = new ArrayList<>(); 
		                	List<String> localList = new ArrayList<>(); 
		                	List<String> teatherList = new ArrayList<>(); 
		                	List<String> dateList = new ArrayList<>(); 
		                	List<String> timeList = new ArrayList<>();
		                	
		                	//리스트형태로 올것이다 1번
		                	
		                	
		                	teatherList.add("해운대");
		                	teatherList.add("부산");
		                	teatherList.add("서면");
//			                	for(int i=0; i<list.size();i++) {
//			                		Vector<String> v = new Vector<>();
//					                v.add(list.get(i));
//					                dtm_theater.addRow(v);	
//			                	}
		                	
		                	//벡터로 바로받을것이다 2번
		                	//샘플 시작
	                		Vector<String> v = new Vector<>();
	                		
	                		//샘플 끝
//			                	for(int i=0; i<list.size();i++) {
//			                	}
		                	
		                	
		                }
		                
		                
		                
		                
		                
		                
		                
		                
		                
			                
			                
			                
		        	}
		        	else if(locResult==1) {
		        		//모든로우의 색상을 초기화한다.
		        		jt_local.setRowSelectionInterval(0,dtm_local.getRowCount()-1);
		        		jt_local.setRowSelectionInterval(row,row);
		        		((JComponent)c).setBorder(new LineBorder(Color.black,0));
		                c.setBackground(Color.lightGray);
		                locResult=2;
		        	} 
		        }
		        return c;
		    }
		};
		
		dtcr_local.setHorizontalAlignment(JLabel.CENTER);
		jt_local.getColumn("지역").setCellRenderer(dtcr_local);
		jt_local.setRowHeight(35);
		jt_local.setTableHeader(null);
		jt_local.setShowVerticalLines(false);
		jt_local.setShowHorizontalLines(false);
		
		jt_theater.setBackground(Color.white);
		DefaultTableCellRenderer dtcr_theater = new DefaultTableCellRenderer(){
			   @Override
			    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column){
			        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			        ((JComponent)c).setBorder(new LineBorder(Color.black,0));
	                c.setBackground(Color.lightGray);
	                /******************************
	                 * 지우지마시오
	                 * table.isRowSelected(row)
	                 * isSelected
	                 ******************************/
			        if (!isSelected) {
			        	if(theaterResult==0) {
			        		//맨처음 맨위에 선택
			                c.setBackground(Color.white);
			                theaterChoice = (String)value;
			                System.out.println("선택한 (String)value : " + (String)value);
				            //마지막커서면 result = 1로 변경
				            if(dtm_theater.getRowCount() == (row+1)) {
				            	theaterResult=1;
				            }
			        	}
			        } else {
			        	 if(theaterResult==2) {
				        		((JComponent)c).setBorder(new LineBorder(Color.black,3));
				        		c.setBackground(Color.white);
				                theaterChoice = (String)value;
				        		System.out.println("선택한 theater row : " + row);
				                System.out.println("선택한 theater (String)value : " + (String)value);
				                System.out.println("선택한 theater column : " + column);
			        	 } else if(theaterResult==1) {
				        		//모든로우의 색상을 초기화한다.
			        		 	jt_theater.setRowSelectionInterval(0,dtm_theater.getRowCount()-1);
			        		 	jt_theater.setRowSelectionInterval(row, row);
				        		((JComponent)c).setBorder(new LineBorder(Color.black,0));
				                c.setBackground(Color.lightGray);
				                theaterResult=2;
			        	}
			        }
			        return c;
			    }
		};
		dtcr_theater.setHorizontalAlignment(JLabel.CENTER);
		jt_theater.getColumn("지점").setCellRenderer(dtcr_theater);
		jt_theater.setRowHeight(35);
		jt_theater.setTableHeader(null);
		jt_theater.setShowVerticalLines(false);
		jt_theater.setShowHorizontalLines(false);
		
		jsp_date.getViewport().setBackground(Color.white);
		jt_date.setBackground(Color.white);
		DefaultTableCellRenderer dtcr_date = new DefaultTableCellRenderer();
		dtcr_date.setHorizontalAlignment(JLabel.CENTER);
		jt_date.getColumn("날짜").setCellRenderer(dtcr_date);
		jt_date.setRowHeight(35);
		jt_date.setTableHeader(null);
		jt_date.setShowVerticalLines(false);
		jt_date.setShowHorizontalLines(false);
		
		jsp_time.getViewport().setBackground(Color.white);
		jt_time.setBackground(Color.white);
		DefaultTableCellRenderer dtcr_time = new DefaultTableCellRenderer();
	
		dtcr_time.setHorizontalAlignment(JLabel.CENTER);
		jt_time.getColumn("상영관").setCellRenderer(dtcr_time);
		jt_time.getColumn("시간").setCellRenderer(dtcr_time);
		
		jt_time.setRowHeight(35);
		jt_time.setTableHeader(null);
		jt_time.setShowVerticalLines(false);
		jt_time.setShowHorizontalLines(false);
		
		jl_movie.setOpaque(true);	
		jl_locThe.setOpaque(true);
		jl_date.setOpaque(true);
		jl_time.setOpaque(true);
		
		//jl_timeLock.setOpaque(true);
		//jsp_time.setVisible(false);//
		
		//테스트용
		jl_timeLock.setOpaque(false);
		jsp_time.setVisible(true);//
		jl_timeLock.setOpaque(true);

		jsp_time.setVisible(true);////////////////////////////////////////
		
		jl_movie.setBackground(new Color(190, 190, 190));
		jl_locThe.setBackground(new Color(190, 190, 190));
		jl_date.setBackground(new Color(190, 190, 190));
		jl_time.setBackground(new Color(190, 190, 190));
		jl_timeLock.setBackground(new Color(80, 80, 80));
		jl_timeLock.setForeground(Color.black);
		jl_timeLock.setFont(new Font("굴림체", Font.BOLD, 13));
		
		jl_movie.setHorizontalAlignment(JLabel.CENTER);
		jl_locThe.setHorizontalAlignment(JLabel.CENTER);
		jl_date.setHorizontalAlignment(JLabel.CENTER);
		jl_time.setHorizontalAlignment(JLabel.CENTER);
		jl_timeLock.setHorizontalAlignment(JLabel.CENTER);
		
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
	}

	public void eventMapping() {
//		jt_movie.addMouseListener(em);
//		jt_local.addMouseListener(this);
//		jt_theater.addMouseListener(em);
//		jt_date.addMouseListener(em);
//		jt_time.addMouseListener(em);	
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
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		// TODO Auto-generated method stub
		return null;
	}
}