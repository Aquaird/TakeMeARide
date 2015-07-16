package Data;

public class Data {
	
	public static Data myData = null; //单例的数据
	
	//个人信息
	private  String mName;
	private  String mPhone;
	private  String mAvatar;
	private  String mAlipay;
	public static Data getInstance(){
		if(myData == null) {
			myData = new Data();
		}
		return myData;
	}

	public  String getmAvatar() {
		return mAvatar;
	}

	public  void setmAvatar(String mAvatar) {
		Data.getInstance().mAvatar = mAvatar;
	}

	public  String getmName() {
		return mName;
	}

	public  void setmName(String mName) {
		Data.getInstance().mName = mName;
	}

	public  String getmPhone() {
		return mPhone;
	}

	public  void setmPhone(String mPhone) {
		Data.getInstance().mPhone = mPhone;
	}

	public  String getmAlipay() {
		return mAlipay;
	}

	public  void setmAlipay(String mAlipay) {
		Data.getInstance().mAlipay = mAlipay;
	}
}
