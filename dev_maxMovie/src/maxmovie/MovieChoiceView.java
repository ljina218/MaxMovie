package maxmovie;


import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
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

import javafx.scene.shape.Line;



	

public class MovieChoiceView extends JPanel implements TableCellRenderer{
	Vector<String> arealist = null;//지역정보 저장
	Vector<String> loclist = null;//지점정보 저장

	int					result					= 0;
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
	String 				data_local[][] 			= new String[0][1];

	DefaultTableModel 	dtm_local  				= new DefaultTableModel(data_local, col_local);

	JTable 				jt_local				= new JTable(dtm_local);
	JScrollPane 		jsp_local				= new JScrollPane(jt_local);
	
	String 				col_theater[] 			= {"지점"};
	String 				data_theater[][] 		= new String[0][1];

	DefaultTableModel 	dtm_theater  			= new DefaultTableModel(data_theater, col_theater);
<<<<<<< HEAD

=======
>>>>>>> refs/remotes/origin/master
	JTable 				jt_theater 				= new JTable(dtm_theater);
	JScrollPane 		jsp_theater 			= new JScrollPane(jt_theater);
	
	String 				col_date[] 				= {"날짜"};
	String 				data_date[][] 			= new String[0][1];

	DefaultTableModel 	dtm_date 				= new DefaultTableModel(data_date, col_date);
	JTable 				jt_date 				= new JTable(dtm_date);
	JScrollPane 		jsp_date 				= new JScrollPane(jt_date);
	
<<<<<<< HEAD
	String 				col_time[] 				= {"상영관","시간"};
	String 				data_time[][] 			= new String[0][2];
=======
	String 				col_time[] 				= {"상영관", "시간"};
	String 				data_time[][] 			= {{"1관", "09:00"}, {"","12:00"}, {"", "15:00"},{"", "18:00"}, {"", "21:00"}, {"2관", "09:00"}, {"","12:00"}, {"", "15:00"},{"", "18:00"}, {"", "21:00"}};
>>>>>>> refs/remotes/origin/master
	DefaultTableModel 	dtm_time  				= new DefaultTableModel(data_time, col_time);
	JTable 				jt_time 				= new JTable(dtm_time);
	JScrollPane 		jsp_time 				= new JScrollPane(jt_time);
	
	EventMapping 		em 						= null;
	int movieIndex = 0;
	String movieChoice = "";
	int localIndex = 0;
	String localChoice = "";
	int theaterIndex = 0;
	String theaterChoice = "건대/입구";
	int dateIndex = 0;
	String dateChoice = "";
	int timeIndex = 0;
	String timeChoice = "";
	
<<<<<<< HEAD
	DefaultTableCellRenderer dtcr_local = null;
	
//	DefaultTableCellRenderer dtcr_local = new DefaultTableCellRenderer() {
//		@Override
//		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
//			Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
//			if (!isSelected) {
//				String localName = (String)value;
//				System.out.println("value : " + value);
//				System.out.println("localName : " + localName);
//				StringTokenizer st = new StringTokenizer(localName, "(");
//				localName = (String)st.nextToken();
//				System.out.println(localName);
//				if (column == 0) {
//					cell.setBackground(Color.lightGray);
//				if(result == 0) {
//					if (localName.equals("서울")) {
//						cell.setBackground(Color.white);
//						result = 1;
//					} 
//				} else {
//					if (localName.equals("서울")) {
//						cell.setBackground(Color.lightGray);
//					}
//					if (localName.equals("강남")) {
//						cell.setBackground(Color.lightGray);
//					}
//					if (localName.equals("부산")) {
//						cell.setBackground(Color.lightGray);
//					}
//				}
//			} else {
//				cell.setBackground(Color.lightGray);
//			}
//			}
//			return this;  
//		}
//	};
=======
	int result = 0;
	boolean goOneRow = false;
>>>>>>> refs/remotes/origin/master
	
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
		dtcr_local = new DefaultTableCellRenderer() {
			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
				Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
				String localName = (String)value;
<<<<<<< HEAD
				//System.out.println("value : " + value);
				//System.out.println("localName : " + localName);
				StringTokenizer st = new StringTokenizer(localName, "(");
				localName = (String)st.nextToken();
				System.out.println(localName);
				//
				System.out.println("result1 : " + result);
				if(column == 0) {
					cell.setBackground(Color.lightGray);
					((JComponent)cell).setBorder(new LineBorder(null,0));
				}
				//
				if(result==0) {
					if(localName.equals("서울")) {
						((JComponent)cell).setBorder(new LineBorder(Color.black,3));
						cell.setBackground(Color.white);
						result = 2;
					}
				}else if(result==2) {
					if (!isSelected) {
						cell.setBackground(Color.lightGray);
						((JComponent)cell).setBorder(new LineBorder(null,0));
						System.out.println("result21 : " + result);
					}
					else {
						((JComponent)cell).setBorder(new LineBorder(Color.black,3));
						cell.setBackground(Color.white);
						System.out.println("result22 : " + result);
					}
=======
				StringTokenizer st = new StringTokenizer(localName, "(");
				localName = (String)st.nextToken();
				((JComponent)cell).setBorder(new LineBorder(Color.black,0));
				cell.setBackground(Color.lightGray);
				if (!isSelected) {
					System.out.println("row : " + row);
					if(result==0) {
						if (row==0) {
							localChoice = localName;
							((JComponent)cell).setBorder(new LineBorder(Color.black,3));
							cell.setBackground(Color.white);
							result = 1;
						} else {
							localChoice = localName;
							((JComponent)cell).setBorder(new LineBorder(Color.black,0));
							cell.setBackground(Color.lightGray);
						}
					}      	
				} else {
					((JComponent)cell).setBorder(new LineBorder(Color.black,3));
					cell.setBackground(Color.white);
>>>>>>> refs/remotes/origin/master
				}
<<<<<<< HEAD
				System.out.println("result3 : " + result);
				return this;  
		}
=======
				return this;
			}
>>>>>>> refs/remotes/origin/master
		};
<<<<<<< HEAD
		
		//DefaultTableCellRenderer dtcr_local = new DefaultTableCellRenderer();
=======
>>>>>>> refs/remotes/origin/master
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
<<<<<<< HEAD
		jl_timeLock.setOpaque(true);

		jsp_time.setVisible(true);////////////////////////////////////////
=======
		//jl_timeLock.setOpaque(true);
		//jsp_time.setVisible(false);//
		
		//테스트용
		jl_timeLock.setOpaque(false);
		jsp_time.setVisible(true);//
>>>>>>> refs/remotes/origin/master
		
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
		jt_movie.addMouseListener(em);
		jt_local.addMouseListener(em);
		jt_theater.addMouseListener(em);
		jt_date.addMouseListener(em);
		jt_time.addMouseListener(em);		
		
	}
	/**********************************************************************************
	 * 참고용
	 * @Override public void mouseClicked(MouseEvent e) { Object obj =
	 * e.getSource(); if(obj==jt_movie) { movieIndex = jt_movie.getSelectedRow();
	 * movieChoice = jt_movie.getValueAt(movieIndex, 0).toString();
	 * jt_movie.setSelectionBackground(Color.gray);
	 * jt_movie.setSelectionForeground(Color.white); //영화 가져오시는 걸로 바꾸어야됨 ↓
	 * 
	 * } if(obj==jt_local) { localIndex = jt_local.getSelectedRow(); localChoice =
	 * jt_local.getValueAt(localIndex, 0).toString();
	 * jt_local.setSelectionBackground(Color.gray);
	 * jt_local.setSelectionForeground(Color.white);
	 * 
	 * } if(obj==jt_theater) { theaterIndex = jt_theater.getSelectedRow();
	 * theaterChoice = jt_theater.getValueAt(theaterIndex, 0).toString();
	 * jt_theater.setSelectionBackground(Color.gray);
	 * jt_theater.setSelectionForeground(Color.white); //지점과 극장 가져오시는 걸로 바꾸어야됨 ↓
	 * 
	 * } if(obj==jt_date) { dateIndex = jt_date.getSelectedRow(); dateChoice =
	 * jt_date.getValueAt(dateIndex, 0).toString();
	 * jt_date.setSelectionBackground(Color.gray);
	 * jt_date.setSelectionForeground(Color.white); //데이터 가져오시는 걸로 바꾸어야됨 ↓
	 * jsp_time.setVisible(true); jl_timeLock.setVisible(false);
	 * 
	 * } if(obj==jt_time) {
	 * timeIndex = jt_time.getSelectedRow();
	 * timeChoice = jt_time.getValueAt(timeIndex, 0).toString(); //데이터 가져오시는 걸로 바꾸어야됨 ↓
	 * jt_time.setSelectionBackground(Color.white);
	 * 

	 * } }

	 */
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