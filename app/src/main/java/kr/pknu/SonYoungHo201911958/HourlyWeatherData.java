package kr.pknu.SonYoungHo201911958;

public class HourlyWeatherData {
    private String hour;
    private String skyType;
    private String rainAdverb;
    private String rainText;
    private int rain;
    private String tmpAdverb;
    private String tmpText;
    private int tmp;

    public HourlyWeatherData(String hour, String skyType, String rainAdverb, String rainText, int rain, String tmpAdverb, String tmpText, int tmp) {
        this.hour = hour;
        this.skyType = skyType;
        this.rainAdverb = rainAdverb;
        this.rainText = rainText;
        this.rain = rain;
        this.tmpAdverb = tmpAdverb;
        this.tmpText = tmpText;
        this.tmp = tmp;
    }

    public String getHour() {
        return hour;
    }

    public String getSkyType() {
        return skyType;
    }

    public String getRainAdverb() {
        return rainAdverb;
    }

    public String getRainText() {
        return rainText;
    }

    public int getRain() {
        return rain;
    }

    public String getTmpAdverb() {
        return tmpAdverb;
    }

    public String getTmpText() {
        return tmpText;
    }

    public int getTmp() {
        return tmp;
    }
}

