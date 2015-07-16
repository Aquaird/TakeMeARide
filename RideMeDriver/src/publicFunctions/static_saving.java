package publicFunctions;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

public class static_saving {
	@SuppressWarnings("deprecation")
	public static HttpClient httpClient=new DefaultHttpClient();
	public static Boolean isFirst=true;
	public static Boolean shouldRefresh=true;
}
