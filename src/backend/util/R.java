package backend.util;

/**
 * Created by cuong on 11/8/2015.
 */
public class R {

	//!!! BECAUSE OF PRIVACY, WE DON'T SUPPORT APIS HERE, YOU HAVE TO GET ONE FOR YOURSELF

    // ocr
    public final static String OCR_API_KEY = "<fill your own one>";
    public final static String OCR_URL_UPLOAD = "http://api.newocr.com/v1/upload";
    public final static String OCR_URL_PARSE = "http://api.newocr.com/v1/ocr";

    // stt
    public final static String RECORD_PATH = "./tmp/RecordAudio.wav";
    public final static String GOOGLE_STT_URL = "https://www.google.com/speech-api/v2/recognize?output=json&lang=%s&key=%s";
    public final static String GOOGLE_STT_API_KEY = "<fill your own one>";
    public final static String IBM_STT_USER = "<fill your own one>";
    public final static String IBM_STT_PASSWD = "<fill your own one>";

    // IPA Transcription
    public static final String IPA_URL = "http://lingorado.com/ipa/";
    public static final String IPA_PARAMS = "text_to_transcribe=%s" +
            "&submit=Show+transcription" +
            "&output_dialect=am" +
            "&output_style=only_tr" +
            "&preBracket=" +
            "&postBracket=";

    // translators, tts
    public static final String GOOGLE_TRANS_URL
            = "https://translate.googleapis.com/translate_a/single?client=gtx&sl=%s&tl=%s&dt=t&q=%s&ie=UTF-8&oe=UTF-8";
    public static final String TRANS_RESULT_PATH = "./tmp/f.txt";
    public final static String MICROSOFT_TRANS_TTS_CLIENT_ID = "<fill your own one>";
    public final static String MICROSOFT_TRANS_TTS_CLIENT_SECRET = "<fill your own one>";


}
