package maxmovie;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class ResultView extends JPanel{
	JLabel					jl_pageInfoLeft 		= new JLabel("");
	JLabel					jl_pageInfoRight		= new JLabel("님 예매(결제)가 완료되었습니다.");
	
	String 					col_history[] 			= {"영화이름", "지점/지역", "상영날짜/시간", "상영관/좌석", "예매번호"};
	String 					data_history[][] 		= new String[0][5]; 
	DefaultTableModel 		dtm_history				= new DefaultTableModel(data_history, col_history);
	JTable 					jt_history				= new JTable(dtm_history);
	JScrollPane 			jsp_history				= new JScrollPane(jt_history);

	
	public ResultView() {
		initDisplay();
	}
	public void initDisplay() {
		this.setLayout(null);
		this.setBackground(Color.white);
		//jp_rv.setBounds(310, 150, 920, 440);
		jl_pageInfoLeft.setBounds(310, 120, 1305, 150);
		jl_pageInfoRight.setBounds(700, 120, 1320, 150);
		jsp_history.setBounds(350, 230, 840, 320);
		jt_history.setBackground(new Color(200, 200, 200));
		
		DefaultTableCellRenderer dtcr_local = new DefaultTableCellRenderer();
		jsp_history.getViewport().setBackground(Color.white);
		dtcr_local.setHorizontalAlignment(JLabel.CENTER);
		jt_history.getTableHeader().setBackground(Color.white);
		jt_history.getColumn("영화이름").setCellRenderer(dtcr_local);
		jt_history.getColumn("지점/지역").setCellRenderer(dtcr_local);
		jt_history.getColumn("상영날짜/시간").setCellRenderer(dtcr_local);
		jt_history.getColumn("상영관/좌석").setCellRenderer(dtcr_local);
		jt_history.getColumn("예매번호").setCellRenderer(dtcr_local);
		jt_history.setRowHeight(35);
		
		jl_pageInfoLeft.setHorizontalAlignment(JLabel.RIGHT);
		jl_pageInfoLeft.setFont(new Font("굴림체", Font.BOLD, 24));
		jl_pageInfoLeft.setForeground(Color.black);
		jl_pageInfoRight.setFont(new Font("굴림체", Font.BOLD, 24));
		jl_pageInfoRight.setForeground(Color.black);
		
		this.add(jl_pageInfoLeft);
		this.add(jl_pageInfoRight);
		this.add(jsp_history);
	}

	public static void main(String[] args) {
		MovieServer ms = new MovieServer();
		ms.display();
		Thread th = new Thread(ms);
		th.start();
		MaxMovieView mmv = new MaxMovieView();
		mmv.jp_lv.setVisible(false);
		mmv.jp_mrv.setVisible(false);
		mmv.jp_mrv.jp_mcv.setVisible(false);
		mmv.jp_mrv.jp_scv.setVisible(false);
		mmv.jp_mrv.jp_pv.setVisible(false);
		mmv.jp_mv.setVisible(false);
		mmv.jp_mv.jp_thv.setVisible(false);
		mmv.jp_mv.jp_miv.setVisible(false);
		mmv.jp_mv.jp_muv.setVisible(false);
		mmv.jp_rv.setVisible(true);
		
		mmv.jl_logo_small.setVisible(true);
		mmv.jl_nickInfo.setVisible(true);
		mmv.jl_nickInfoEnd.setVisible(true);
		mmv.jbt_logout.setVisible(true);
		mmv.jbt_myPage.setVisible(true);
		mmv.jbt_ticketing.setVisible(true);
	}
}
