package hjho;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class AdminClient extends JFrame {
	AdminLoginView alv = null;
	
	public AdminClient(AdminLoginView alv) {
		this.alv = alv;
		JOptionPane.showMessageDialog(this, alv.nickName);
	}
}
