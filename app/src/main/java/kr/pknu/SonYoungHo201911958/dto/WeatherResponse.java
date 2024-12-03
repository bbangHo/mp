package kr.pknu.SonYoungHo201911958.dto;

import java.util.List;

public class WeatherResponse {

    public static class Result {
        private String city;
        private String street;
        private String currentSkyType;
        private Integer currentTmp;
        private List<HourlyWeatherData> hourlyWeatherData;

        public Result(String city, String street, String currentSkyType, Integer currentTmp) {
            this.city = city;
            this.street = street;
            this.currentSkyType = currentSkyType;
            this.currentTmp = currentTmp;
        }

        public String getCity() {
            return city;
        }

        public String getStreet() {
            return street;
        }

        public String getCurrentSkyType() {
            return currentSkyType;
        }

        public Integer getCurrentTmp() {
            return currentTmp;
        }

        public List<HourlyWeatherData> getHourlyWeatherData() {
            return hourlyWeatherData;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public void setStreet(String street) {
            this.street = street;
        }

        public void setCurrentSkyType(String currentSkyType) {
            this.currentSkyType = currentSkyType;
        }

        public void setCurrentTmp(Integer currentTmp) {
            this.currentTmp = currentTmp;
        }

        public void setHourlyWeatherData(List<HourlyWeatherData> hourlyWeatherDataList) {
            this.hourlyWeatherData = hourlyWeatherDataList;
        }
    }

    public static class HourlyWeatherData {
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

        public void setHour(String hour) {
            this.hour = hour;
        }

        public void setSkyType(String skyType) {
            this.skyType = skyType;
        }

        public void setRainAdverb(String rainAdverb) {
            this.rainAdverb = rainAdverb;
        }

        public void setRainText(String rainText) {
            this.rainText = rainText;
        }

        public void setRain(int rain) {
            this.rain = rain;
        }

        public void setTmpAdverb(String tmpAdverb) {
            this.tmpAdverb = tmpAdverb;
        }

        public void setTmpText(String tmpText) {
            this.tmpText = tmpText;
        }

        public void setTmp(int tmp) {
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
}


