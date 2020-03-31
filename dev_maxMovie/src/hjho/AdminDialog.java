package hjho;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class AdminDialog extends JDialog implements ActionListener{
	AdminClient ac = null;
	//화면을 위한 그리기 코드
	JLabel jl_movieTitle = new JLabel("영화제목 : ");
	JLabel jl_scrName = new JLabel("상영관이름  : ");
	JLabel jl_date = new JLabel("날짜, 시간 : ");
	JLabel jl_yy = new JLabel(" 년");
	JLabel jl_mm = new JLabel(" 월");
	JLabel jl_dd = new JLabel(" 일");
	JLabel jl_hh24 = new JLabel(" 시");
	JLabel jl_mi = new JLabel(" 분");
	JComboBox jcb_movieTitle = null;
	JComboBox jcb_scrName = null;
	JTextField jtf_date = new JTextField(5); 
	JTextField jtf_yy   = new JTextField(5);
	JTextField jtf_mm   = new JTextField(5);
	JTextField jtf_dd   = new JTextField(5);
	JTextField jtf_hh24 = new JTextField(5);
	JTextField jtf_mi   = new JTextField(5);
	
	public AdminDialog() {}
	public AdminDialog(AdminClient ac) {
		this.ac = ac;
	}
	public void initDisplay() {
		this.setSize(500,750);
		this.setVisible(true);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		
		
	}
	public static void main(String[] args) {
		AdminDialog ad = new AdminDialog();
		ad.initDisplay();
	}

}
