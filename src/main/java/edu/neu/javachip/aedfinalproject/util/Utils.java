package edu.neu.javachip.aedfinalproject.util;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import org.apache.commons.codec.digest.DigestUtils;

import javax.net.ssl.*;
import java.io.*;
import java.net.URL;
import java.net.URLEncoder;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Utils {
    private final static Logger LOGGER =
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    public static String getHash(String text) {
        return DigestUtils.sha256Hex(text);
    }

    public static String formatDate(LocalDate localDate) {
        return localDate.format(DateTimeFormatter.ofPattern("MM-dd-yyyy"));
    }

    public static String formatDateTime(LocalDateTime localDateTime) {
        return localDateTime.format(DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm"));
    }

    public static void sendSms(String message, long mobile) {
        Thread thread = new Thread(() -> {
            String apiUrl = "https://textbelt.com/text";
            URL url;
            HttpsURLConnection con;
            DataOutputStream wr = null;
            BufferedReader bin = null;
            StringBuffer response = null;
            InputStreamReader isr = null;
            try {
                url = new URL(apiUrl);
                Utils.trustAllHosts();
                con = (HttpsURLConnection) url.openConnection();
                con.setRequestMethod("POST");
                con.setDoOutput(true);
                wr = new DataOutputStream(con.getOutputStream());

                Map<String, String> requestParams = new HashMap<>();
                requestParams.put("phone", Long.toString(mobile));
                requestParams.put("message", message);
                requestParams.put("key", "textbelt");
                wr.writeBytes(getQuery(requestParams));
                wr.flush();


                isr = new InputStreamReader(con.getInputStream());
                bin = new BufferedReader(isr);
                String inputLine;
                response = new StringBuffer();

                int i = 0;
                while ((inputLine = bin.readLine()) != null) {
                    response.append(inputLine);
                }
                System.out.println(response);

            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        thread.start();
    }

    public static void sendEmail(String toString, String cc, String subject, String body) {
        Thread thread = new Thread(() -> {
            Email from = new Email("noreply@javachip.husky.edu");
            Email to = new Email(toString);
            Content content = new Content("text/plain", body + "\n\nThis is a system generated mail");
            Mail mail = new Mail(from, subject, to, content);
            if (cc != null) {
                mail.personalization.get(0).addCc(new Email(cc));
            }
//            SendGrid sg = new SendGrid("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");
            SendGrid sg = new SendGrid("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");
            Request request = new Request();
            try {
                request.setMethod(Method.POST);
                request.setEndpoint("mail/send");
                request.setBody(mail.build());
                Response response = sg.api(request);
                System.out.println(response.getStatusCode());
                System.out.println(response.getBody());
                System.out.println(response.getHeaders());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        thread.start();
    }


    public static void trustAllHosts() {
        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[]{};
            }

            public void checkClientTrusted(X509Certificate[] chain,
                                           String authType) throws CertificateException {
            }

            public void checkServerTrusted(X509Certificate[] chain,
                                           String authType) throws CertificateException {
            }
        }};

        // Install the all-trusting trust manager
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection
                    .setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
            //e.printStackTrace();
        }
    }

    final public static HostnameVerifier DO_NOT_VERIFY = (hostname, session) -> true;

    public static String getQuery(Map<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        Set<Map.Entry<String, String>> s = params.entrySet();
        Iterator itr = s.iterator();


        while (itr.hasNext()) {
            if (first)
                first = false;
            else
                result.append("&");
            Map.Entry me = (Map.Entry) itr.next();
            result.append(URLEncoder.encode(me.getKey().toString(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(me.getValue().toString(), "UTF-8"));
        }

        return result.toString();
    }

    public static Logger setupLogger(){
        try {
            AppLogger.setup();
            LOGGER.setLevel(Level.INFO);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Problems with creating the log files");
        }
        return LOGGER;
    }

    public static Logger getGlobalLogger(){
        return LOGGER;
    }
}
