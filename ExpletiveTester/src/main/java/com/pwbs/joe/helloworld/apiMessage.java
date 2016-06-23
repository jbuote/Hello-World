package com.pwbs.joe.helloworld;

/**
 * Created by Joe on 6/2/2016.
 */
public class apiMessage {
    private String CensoredMessage;
    private int Err;
    private String ErrorMessage;
    private String Key;
    private String OriginalMessage;
    private String Rating;

    public apiMessage() {
        CensoredMessage = "";
        Err = 0;
        ErrorMessage = "";
        Key = "A";
        OriginalMessage = "";
        Rating = "";
    }

    public String getCensoredMessage() {
        return CensoredMessage;
    }

    public void SetCensoredMessage(String text) {
        CensoredMessage = text;
    }

    public int GetErr() {
        return Err;
    }

    public void SetErr(int err) {
        Err = err;
    }

    public String GetErrorMessage() {
        return ErrorMessage;
    }

    public void SetErrorMessage(String err) {
        ErrorMessage = err;
    }

    public String GetKey() {
        return Key;
    }

    public void SetKey(String key) {
        Key = key;
    }

    public String GetOriginalMessage() {
        return OriginalMessage;
    }

    public void SetOriginalMessage(String msg) {
        OriginalMessage = msg;
    }

    public String GetRating() {
        return Rating;
    }

    public void SetRating(String rating) {
        Rating = rating;
    }

    public String ToJsonStringContent() {
        String temp = "";
        //'{"OriginalMessage":"sample fucking string 1",
        //'    "Rating":"G",
        //'    "Key":"A",
        //'    "CensoredMessage":"sample ****ing string 1"
        //'    ,"Err":0,
        //'    "ErrorMessage":""}
        temp = "{\"OriginalMessage\": \"" + OriginalMessage;
        temp = temp + "\",\"Rating\": \"" + Rating;
        temp = temp + "\",\"Key\": \"" + Key;
        temp = temp + "\",\"CensoredMessage\": \"\",\"Err\": 0,\"ErrorMessage\": \"\"}";
        // String result = temp;

        return temp;

    }


    public apiMessage FromJsonStringContent(String JsonString) {
        apiMessage result = new apiMessage();
        String[] stringArray = JsonString.split(",");
        String[] sTemp;
        char[] trimChars = {'\"', '{', '}'};

        String key = "";
        String value = "";
        //String element;
        for (String element : stringArray) {
            sTemp = element.split(":");
            key = sTemp[0].replaceAll("^\\+","");
            sTemp[0] = sTemp[0].replaceAll("^\\{+", "");
            sTemp[0] = sTemp[0].replaceAll("^\\+", "");
            sTemp[0] = sTemp[0].replaceAll("^\"+", "");
            sTemp[0] = sTemp[0].replaceAll("\\{+$", "");
            sTemp[0] = sTemp[0].replaceAll("\"+$", "");
            sTemp[1] = sTemp[1].replaceAll("^\\{+", "");
            sTemp[1] = sTemp[1].replaceAll("^\\+", "");
            sTemp[1] = sTemp[1].replaceAll("^\"+", "");
            sTemp[1] = sTemp[1].replaceAll("\\{+$", "");
            sTemp[1] = sTemp[1].replaceAll("\"+$", "");

            switch (sTemp[0]) {
                case "OriginalMessage":
                    result.OriginalMessage = sTemp[1];
                    break;
                case "Rating":
                    result.Rating = sTemp[1];
                    break;
                case "Key":
                    result.Key = sTemp[1];
                    break;
                case "CensoredMessage":
                    result.CensoredMessage = sTemp[1];
                    break;
                case "Err":
                    result.Err = Integer.parseInt(sTemp[1]);
                    break;
                case "ErrorMessage":
                    result.ErrorMessage = sTemp[1];
                    break;
            }
        }
        return result;
    }
}
