package maxmovie;

public class ClientThread extends Thread{
	
	//진아수정
	MaxMovieView iv = null;
	
	boolean stop = false;

	public ClientThread(MaxMovieView iv) {
		this.iv = iv;
	}
	
	//서버로부터 메세지를 듣고 뷰에 변화를 주는 메소드
	@Override
	public void run() {
		try {
			while(!stop) {
				//mmv.ois.readObject();
			}
		} catch (Exception e) {
			System.out.println(e.toString());
			//e.printStackTrace();
		}
	}
	
}
