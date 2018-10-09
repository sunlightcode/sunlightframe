package SunlightFrame.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpClientParams;

public class HttpUtil {

	public static String getURLHTMLContent(String host, int port, String charset, String path) throws Exception {
		HostConfiguration hostConfig = new HostConfiguration();
		hostConfig.setHost(host, port);

		HttpClientParams clientParams = new HttpClientParams();
		clientParams.setContentCharset(charset);

		HttpClient client = new HttpClient();
		client.setHostConfiguration(hostConfig);
		client.setParams(clientParams);

		HttpMethod method = new GetMethod(path);
		method.setFollowRedirects(false);
		client.executeMethod(method);

		InputStream in = method.getResponseBodyAsStream();
		BufferedReader reader = new BufferedReader(new InputStreamReader(in, client.getParams().getContentCharset()));
		StringBuffer buf = new StringBuffer();
		String line = null;
		int lineCounter = 0;
		while ((line = reader.readLine()) != null) {
			if (lineCounter != 0) {
				buf.append("\n");
			}
			buf.append(line);
			lineCounter++;
		}
		reader.close();
		method.releaseConnection();
		return buf.toString();
	}

	public static void getURLFile(String host, int port, String path, String filePath) throws Exception {
		HostConfiguration hostConfig = new HostConfiguration();
		hostConfig.setHost(host, port);

		HttpClient client = new HttpClient();
		client.setHostConfiguration(hostConfig);

		HttpMethod method = new GetMethod(path);

		client.executeMethod(method);
		SimpleFileWriter writer = new SimpleFileWriter(filePath);
		writer.write(method.getResponseBodyAsStream());
		method.releaseConnection();
	}
}
