package com.maygood.xhw.net;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.AllowAllHostnameVerifier;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

public class HttpsUtils {

	private static final String LOG_TAG = "Xusr";   
	  
    private static final TrustManager[] TRUST_MANAGER = { new NaiveTrustManager() };   
  
    private static final AllowAllHostnameVerifier HOSTNAME_VERIFIER = new AllowAllHostnameVerifier();   
  
    private static final int SOCKET_TIMEOUT = 3000;   
  
    /**  
     * Send an HTTP(s) request with POST parameters.  
     *   
     * @param parameters  
     * @param url  
     * @throws UnsupportedEncodingException  
     * @throws IOException  
     * @throws KeyManagementException  
     * @throws NoSuchAlgorithmException  
     */  
    
    public static int getNetType(Object manager) {
		NetworkInfo info = null;
		info = ((ConnectivityManager) manager).getActiveNetworkInfo();
		if(info!=null) {
			if(info.getType() == ConnectivityManager.TYPE_WIFI){
				return 0;	//WIFI
			}
			else if(info.getType() == ConnectivityManager.TYPE_MOBILE) {
				return 1;	//gprs or edge
			}
		}
		return -1;
	}
    
    public static String doPost(Context context, Map<?, ?> parameters)   
            throws UnsupportedEncodingException, IOException, KeyManagementException, NoSuchAlgorithmException {   
  
    	String responseValue = "";
    	
    	Object imgfile = null;
    	for (Object key : parameters.keySet()) {
    		if (key.equals("pic")) {
    			imgfile = parameters.get(key);
    			parameters.remove(key);
    			break;
    		}
    	}
    	if (imgfile==null) {
    		responseValue = doPost(context, parameters, null);
    	}
    	else {
    		responseValue = doPost(context, parameters, (Uri) imgfile);
    	}
    	return responseValue;
    }   
    
    public static String doPost(Context context, Map<?, ?> parameters, Uri imgfile)   
            throws UnsupportedEncodingException, IOException, KeyManagementException, NoSuchAlgorithmException {
    	String responseValue = "";
    	String url = parameters.get("url").toString();
    	
    	HttpClient client = getConnection();
    	HttpUriRequest request = null;
    	ByteArrayOutputStream bos = null;
    	
    	//post
    	HttpPost post = new HttpPost(url);
    	byte[] data = null;
    	bos = new ByteArrayOutputStream();
    	
    	String boundary = "---------------------------37531613912423";
    	String content = "--"+boundary+"\r\nContent-Disposition: form-data; name=\"";
    	byte[] end_data = ("\r\n--" + boundary + "--\r\n").getBytes();
    	if (imgfile==null) {
    		post.setHeader("Content-Type", "application/x-www-form-urlencoded");
    		StringBuilder dataBfr = new StringBuilder();
        	for (Object key : parameters.keySet()) {
        		if (key.equals("url")) {
        			continue;
        		}
        		if (dataBfr.length() != 0) {
        			dataBfr.append('&');
        		}
        		Object value = parameters.get(key);
        		if (value == null) {
        			value = "";
        		}   
        		dataBfr.append(URLEncoder.encode(key.toString(), "UTF-8")).append('=').append(URLEncoder.encode(value.toString(), "UTF-8"));
        	}   
            bos.write(dataBfr.toString().getBytes());
    	}
    	else {
    		StringBuilder dataBfr = new StringBuilder();
        	for (Object key : parameters.keySet()) {
        		if (key.equals("url")) {
        			continue;
        		}
        		Object value = parameters.get(key);
        		if (value == null) {
        			value = "";
        		}
        		dataBfr.append("--"+boundary+"\r\nContent-Disposition: form-data; name=\"");
        		dataBfr.append(key.toString()+"\"\r\n\r\n"+value.toString()+"\r\n");
        	}
        	bos.write(dataBfr.toString().getBytes());
        	
        	post.setHeader("Content-Type", "multipart/form-data; boundary="+boundary);
        	BitmapFactory.Options opts = new BitmapFactory.Options();
        	opts.inSampleSize = 4;
        	//Bitmap b = BitmapFactory.decodeFile(imgfile, opts);
        	//Bitmap b = (Bitmap) imgfile;
        	ContentResolver cr = context.getContentResolver();
        	Bitmap b = MediaStore.Images.Media.getBitmap(cr, imgfile);
        	
        	StringBuffer picBfr = new StringBuffer();
        	picBfr.append("--"+boundary+"\r\nContent-Disposition: form-data; name=\"pic\"; filename=\"image.jpg\"\r\nContent-Type: image/jpg\r\n\r\n");
        	bos.write(picBfr.toString().getBytes());
        	
        	//BufferedInputStream bis = null;
        	b.compress(CompressFormat.JPEG, 100, bos);
        	bos.write(end_data);
        	b.recycle();
    	}
    	data = bos.toByteArray();
    	Log.d("length", data.length+"");
    	bos.close();
    	
    	ByteArrayEntity formEntity = new ByteArrayEntity(data);
    	post.setEntity(formEntity);
    	request = post;
    	
    	HttpResponse response = client.execute(request);
    	int statusCode = response.getStatusLine().getStatusCode();
    	//if (statusCode != 200) {
        //    return null;
        //}
    	responseValue = read(response);
        return responseValue;
    }
    
    public static String doGet(Map<?, ?> parameters)   
            throws UnsupportedEncodingException, IOException, KeyManagementException, NoSuchAlgorithmException {
    	
    	String responseValue = "";
    	HttpClient client = getConnection();
        HttpUriRequest request = null;
        
        StringBuilder dataBfr = new StringBuilder();   
    	for (Object key : parameters.keySet()) {   
    		if (key.equals("url")) {
    			continue;
    		}
    		if (dataBfr.length() != 0) {   
    			dataBfr.append('&');   
    		}   
    		Object value = parameters.get(key);   
    		if (value == null) {
    			value = "";
    		}   
    		dataBfr.append(URLEncoder.encode(key.toString(), "UTF-8")).append('=').append(URLEncoder.encode(value.toString(), "UTF-8"));   
    	}   
    	String uri = parameters.get("url").toString()+"?"+dataBfr.toString();
        HttpGet get = new HttpGet(uri);
        request = get;
        
        HttpResponse response = client.execute(request);
    	int statusCode = response.getStatusLine().getStatusCode();
    	//if (statusCode != 200) {
        //    return null;
        //}
    	responseValue = read(response);
        return responseValue;
    }   
  
  
    /**  
     * Open an URL connection. If HTTPS, accepts any certificate even if not  
     * valid, and connects to any host name.  
     *   
     * @param url  
     *            The destination URL, HTTP or HTTPS.  
     * @return The URLConnection.  
     * @throws IOException  
     * @throws NoSuchAlgorithmException  
     * @throws KeyManagementException  
     */  
    private static HttpClient getConnection() {
		try {
			KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
			trustStore.load(null, null);
			
			SSLSocketFactory sf = new MySSLSocketFactory(trustStore);
            sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            
            HttpParams params = new BasicHttpParams();
            
            HttpConnectionParams.setConnectionTimeout(params, 10000);
            HttpConnectionParams.setSoTimeout(params, 10000);
            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
            
            SchemeRegistry registry = new SchemeRegistry();
            registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
            registry.register(new Scheme("https", sf, 443));
            
            ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);
            HttpClient client = new DefaultHttpClient(ccm, params);
            
            return client;
		} catch (KeyStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CertificateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (KeyManagementException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnrecoverableKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return null;
    	
    }
    
    private static String read(HttpResponse response) {
    	String result = "";
    	try {
        HttpEntity entity = response.getEntity();
        InputStream inputStream;
			inputStream = entity.getContent();
        ByteArrayOutputStream content = new ByteArrayOutputStream();

        Header header = response.getFirstHeader("Content-Encoding");
        if (header != null && header.getValue().toLowerCase().indexOf("gzip") > -1) {
            inputStream = new GZIPInputStream(inputStream);
        }

        // Read response into a buffered stream
        int readBytes = 0;
        byte[] sBuffer = new byte[512];
        while ((readBytes = inputStream.read(sBuffer)) != -1) {
            content.write(sBuffer, 0, readBytes);
        }
        // Return result from buffered stream
        result = new String(content.toByteArray());
    	} catch (IllegalStateException e) {
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    	} catch (IOException e) {
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    	}
        return result;
    }
    
    public static class MySSLSocketFactory extends SSLSocketFactory {
        SSLContext sslContext = SSLContext.getInstance("TLS");

        public MySSLSocketFactory(KeyStore truststore) throws NoSuchAlgorithmException,
                KeyManagementException, KeyStoreException, UnrecoverableKeyException {
            super(truststore);

            TrustManager tm = new X509TrustManager() {
                public void checkClientTrusted(X509Certificate[] chain, String authType)
                        throws CertificateException {
                }

                public void checkServerTrusted(X509Certificate[] chain, String authType)
                        throws CertificateException {
                }

                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            };

            sslContext.init(null, new TrustManager[] { tm }, null);
        }

        @Override
        public Socket createSocket(Socket socket, String host, int port, boolean autoClose)
                throws IOException, UnknownHostException {
            return sslContext.getSocketFactory().createSocket(socket, host, port, autoClose);
        }

        @Override
        public Socket createSocket() throws IOException {
            return sslContext.getSocketFactory().createSocket();
        }
    }
    
}

class NaiveTrustManager implements X509TrustManager{

	@Override
	public void checkClientTrusted(X509Certificate[] chain, String authType)
			throws CertificateException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void checkServerTrusted(X509Certificate[] chain, String authType)
			throws CertificateException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public X509Certificate[] getAcceptedIssuers() {
		// TODO Auto-generated method stub
		return null;
	}
}
