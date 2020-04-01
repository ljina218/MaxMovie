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
	
	JButton jbtn_sel = new JButton("영화정보 가져오기");	
	JButton jbtn_ins = new JButton("반영하기");	
	JButton jbtn_exit = new JButton("나가기");	
	
	Vector<String> movieTitle = null;
	Vector<String> scrName = null;
	String yy[] = new String[3]; 
	String mm[] = {"월","01","02","03","04","05","06","07","08","09","10","11","12"};
	String hh24[] = {"시","00","01","02","03","04","05","06","07","08","09","10","11"
			        ,"12","13","14","15","16","17","18","19","20","21","22","23"};
	String mi[] = {"분","00","05","10","15","20","25","30","35","40","45","50","55"};
	public AdminDialog() {}
	public AdminDialog(AdminClient ac) {
		this.ac = ac;
		jbtn_sel.addActionListener(this);
		jbtn_ins.addActionListener(this);
		jbtn_exit.addActionListener(this);
		setJcomboBox();
	}
	
	public void setJcomboBox() {
		movieTitle = new Vector<String>();
		scrName = new Vector<String>();
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
		
		jbtn_sel.setBounds(20, 130, 150, 20);
		jbtn_ins.setBounds(180, 130, 100, 20);
		jbtn_exit.setBounds(290, 130, 100, 20);
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
		this.add(jbtn_sel);
		this.add(jbtn_ins);
		this.add(jbtn_exit);
		this.setSize(500,250);
		this.setVisible(true);
	}
//	JButton jbtn_sel = new JButton("영화정보 가져오기");	
//	JButton jbtn_ins = new JButton("반영하기");	
//	JButton jbtn_exit = new JButton("나가기");
	@Override
	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();
		//영화정보 가져오기를 눌렀니?
		if(obj==jbtn_sel) {
			
		}
		//반영하기를 눌렀니?
		else if(obj==jbtn_ins) {
			
		}
		//나가기를 눌렀니?
		else if(obj==jbtn_exit) {
			this.dispose();
			ac.ad = null;
		}	
		else if(obj==jcb_mm) {
			String mm = (String)(jcb_mm.getSelectedItem());
			//System.out.println(mm);
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
	private String[] getDayList(String mm) {
//		31일 : 1, 3, 5, 7, 8, 10, 12
//		30일 : 4, 6, 9, 11
//		그외  : 2
		//System.out.println("List안에서 :"+mm);
		String[][] month = {
				{"01","02","03","04","05","06","07","08","09","10","11","12"}
			   ,{"31","28","31","30","31","30","31","31","30","31","30","31"}
		};
		String[] rDD = null;
		int day = 0;
		for (int i = 0; i < month[0].length; i++) {
			if(mm.equals(month[0][i])) {
				if(i==1) {
					if(cal.get(Calendar.YEAR)%4==0) {
						day = 29;
						break;
					}
					else {
						day = Integer.parseInt(month[1][i]);
						break;
					}
				}
				else {
					day = Integer.parseInt(month[1][i]);
					break;
				}
			}
		}
		rDD = new String[day+1];
		for (int i = 0; i < day+1; i++) {
			if(i==0) {
				rDD[0] = "일";
			}
			else if(i<10) {
				rDD[i] = "0"+i;
			}
			else {
				rDD[i] = Integer.toString(i);
			}
			
		}
		return rDD;
	}
	public static void main(String[] args) {
		AdminDialog ad = new AdminDialog();
		ad.initDisplay();
	}

}
