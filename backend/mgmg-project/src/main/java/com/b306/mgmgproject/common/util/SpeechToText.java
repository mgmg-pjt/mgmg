package com.b306.mgmgproject.common.util;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SpeechToText {

    private CloseableHttpClient httpClient = HttpClients.createDefault();
    private Gson gson = new Gson();

    private static String SECRET;
    private static String INVOKE_URL;

    @Value("${Naver.STT-Secret}")
    private void setSECRET(String secretKey) {
        this.SECRET = secretKey;
    }

    @Value("${Naver.STT-INVOKEURL}")
    private void setInvokeUrl(String url) {
        this.INVOKE_URL = url;
    }

    private Header[] HEADERS = new Header[]{
            new BasicHeader("Accept", "application/json"),
            new BasicHeader("X-CLOVASPEECH-API-KEY", SECRET),
    };

    public static class Boosting {
        private String words;

        public String getWords() {
            return words;
        }

        public void setWords(String words) {
            this.words = words;
        }
    }

    public static class Diarization {
        private Boolean enable = Boolean.FALSE;
        private Integer speakerCountMin;
        private Integer speakerCountMax;

        public Boolean getEnable() {
            return enable;
        }

        public void setEnable(Boolean enable) {
            this.enable = enable;
        }

        public Integer getSpeakerCountMin() {
            return speakerCountMin;
        }

        public void setSpeakerCountMin(Integer speakerCountMin) {
            this.speakerCountMin = speakerCountMin;
        }

        public Integer getSpeakerCountMax() {
            return speakerCountMax;
        }

        public void setSpeakerCountMax(Integer speakerCountMax) {
            this.speakerCountMax = speakerCountMax;
        }
    }

    public static class NestRequestEntity {
        private String language = "ko-KR";
        private String completion = "sync";
        private String callback;
        private Map<String, Object> userdata;
        private Boolean wordAlignment = Boolean.TRUE;
        private Boolean fullText = Boolean.TRUE;
        private List<Boosting> boostings;
        private String forbiddens;
        private Diarization diarization;

        public String getLanguage() {
            return language;
        }

        public void setLanguage(String language) {
            this.language = language;
        }

        public String getCompletion() {
            return completion;
        }

        public void setCompletion(String completion) {
            this.completion = completion;
        }

        public String getCallback() {
            return callback;
        }

        public Boolean getWordAlignment() {
            return wordAlignment;
        }

        public void setWordAlignment(Boolean wordAlignment) {
            this.wordAlignment = wordAlignment;
        }

        public Boolean getFullText() {
            return fullText;
        }

        public void setFullText(Boolean fullText) {
            this.fullText = fullText;
        }

        public void setCallback(String callback) {
            this.callback = callback;
        }

        public Map<String, Object> getUserdata() {
            return userdata;
        }

        public void setUserdata(Map<String, Object> userdata) {
            this.userdata = userdata;
        }

        public String getForbiddens() {
            return forbiddens;
        }

        public void setForbiddens(String forbiddens) {
            this.forbiddens = forbiddens;
        }

        public List<Boosting> getBoostings() {
            return boostings;
        }

        public void setBoostings(List<Boosting> boostings) {
            this.boostings = boostings;
        }

        public Diarization getDiarization() {
            return diarization;
        }

        public void setDiarization(Diarization diarization) {
            this.diarization = diarization;
        }
    }

    public String upload(File file, NestRequestEntity nestRequestEntity) {
        HttpPost httpPost = new HttpPost(INVOKE_URL + "/recognizer/upload");
        httpPost.setHeaders(HEADERS);
        HttpEntity httpEntity = MultipartEntityBuilder.create()
                .addTextBody("params", gson.toJson(nestRequestEntity), ContentType.APPLICATION_JSON)
                .addBinaryBody("media", file, ContentType.MULTIPART_FORM_DATA, file.getName())
                .build();
        httpPost.setEntity(httpEntity);
        return execute(httpPost);
    }

    private String execute(HttpPost httpPost) {
        try (final CloseableHttpResponse httpResponse = httpClient.execute(httpPost)) {
            final HttpEntity entity = httpResponse.getEntity();
            return EntityUtils.toString(entity, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String play(File file) {
        final SpeechToText clovaSpeechClient = new SpeechToText();
        NestRequestEntity requestEntity = new NestRequestEntity();
        final String result =
                clovaSpeechClient.upload(file, requestEntity);
        JSONParser parser = new JSONParser();
        Object obj = null;
        try {
            obj = parser.parse(result);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        JSONObject jsonObj = (JSONObject) obj;
        String parseData = (String) jsonObj.get("text");
        return parseData;
    }
}
