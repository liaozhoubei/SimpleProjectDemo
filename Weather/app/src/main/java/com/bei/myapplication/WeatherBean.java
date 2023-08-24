package com.bei.myapplication;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Bei on 2016/3/10.
 */
public class WeatherBean {

    /**
     * aqi : {"city":{"aqi":"28","co":"0","no2":"14","o3":"77","pm10":"16","pm25":"12","qlty":"优","so2":"4"}}
     * basic : {"city":"北京","cnty":"中国","id":"CN101010100","lat":"39.904000","lon":"116.391000","update":{"loc":"2016-03-11 14:50","utc":"2016-03-11 06:50"}}
     * daily_forecast : [{"astro":{"sr":"06:31","ss":"18:17"},"cond":{"code_d":"100","code_n":"101","txt_d":"晴","txt_n":"多云"},"date":"2016-03-09","hum":"9","pcpn":"0.0","pop":"0","pres":"1034","tmp":{"max":"8","min":"-2"},"vis":"10","wind":{"deg":"320","dir":"无持续风向","sc":"微风","spd":"5"}},{"astro":{"sr":"06:30","ss":"18:18"},"cond":{"code_d":"101","code_n":"100","txt_d":"多云","txt_n":"晴"},"date":"2016-03-10","hum":"14","pcpn":"0.0","pop":"0","pres":"1030","tmp":{"max":"9","min":"1"},"vis":"10","wind":{"deg":"309","dir":"无持续风向","sc":"微风","spd":"3"}},{"astro":{"sr":"06:28","ss":"18:19"},"cond":{"code_d":"100","code_n":"100","txt_d":"晴","txt_n":"晴"},"date":"2016-03-11","hum":"12","pcpn":"0.0","pop":"0","pres":"1023","tmp":{"max":"12","min":"1"},"vis":"10","wind":{"deg":"201","dir":"北风","sc":"3-4","spd":"12"}},{"astro":{"sr":"06:26","ss":"18:20"},"cond":{"code_d":"100","code_n":"101","txt_d":"晴","txt_n":"多云"},"date":"2016-03-12","hum":"20","pcpn":"0.0","pop":"0","pres":"1015","tmp":{"max":"15","min":"4"},"vis":"10","wind":{"deg":"101","dir":"无持续风向","sc":"微风","spd":"1"}},{"astro":{"sr":"06:25","ss":"18:21"},"cond":{"code_d":"101","code_n":"100","txt_d":"多云","txt_n":"晴"},"date":"2016-03-13","hum":"12","pcpn":"0.0","pop":"0","pres":"1023","tmp":{"max":"15","min":"4"},"vis":"10","wind":{"deg":"338","dir":"无持续风向","sc":"微风","spd":"8"}},{"astro":{"sr":"06:23","ss":"18:22"},"cond":{"code_d":"100","code_n":"101","txt_d":"晴","txt_n":"多云"},"date":"2016-03-14","hum":"11","pcpn":"0.0","pop":"0","pres":"1018","tmp":{"max":"17","min":"7"},"vis":"10","wind":{"deg":"197","dir":"无持续风向","sc":"微风","spd":"6"}},{"astro":{"sr":"06:22","ss":"18:23"},"cond":{"code_d":"101","code_n":"100","txt_d":"多云","txt_n":"晴"},"date":"2016-03-15","hum":"12","pcpn":"0.0","pop":"0","pres":"1016","tmp":{"max":"18","min":"6"},"vis":"10","wind":{"deg":"125","dir":"无持续风向","sc":"微风","spd":"3"}}]
     * hourly_forecast : [{"date":"2016-03-11 01:00","hum":"23","pop":"0","pres":"1029","tmp":"2","wind":{"deg":"333","dir":"西北风","sc":"微风","spd":"6"}},{"date":"2016-03-11 04:00","hum":"25","pop":"0","pres":"1028","tmp":"1","wind":{"deg":"350","dir":"北风","sc":"微风","spd":"7"}},{"date":"2016-03-11 07:00","hum":"22","pop":"0","pres":"1028","tmp":"2","wind":{"deg":"353","dir":"北风","sc":"微风","spd":"6"}},{"date":"2016-03-11 10:00","hum":"16","pop":"0","pres":"1027","tmp":"6","wind":{"deg":"288","dir":"西北风","sc":"微风","spd":"5"}},{"date":"2016-03-11 13:00","hum":"13","pop":"0","pres":"1024","tmp":"9","wind":{"deg":"219","dir":"西南风","sc":"微风","spd":"10"}},{"date":"2016-03-11 16:00","hum":"13","pop":"0","pres":"1022","tmp":"8","wind":{"deg":"192","dir":"西南风","sc":"微风","spd":"15"}},{"date":"2016-03-11 19:00","hum":"16","pop":"0","pres":"1021","tmp":"5","wind":{"deg":"195","dir":"西南风","sc":"微风","spd":"11"}},{"date":"2016-03-11 22:00","hum":"19","pop":"0","pres":"1022","tmp":"3","wind":{"deg":"186","dir":"南风","sc":"微风","spd":"9"}}]
     * now : {"cond":{"code":"100","txt":"晴"},"fl":"-8","hum":"13","pcpn":"0","pres":"1035","tmp":"8","vis":"10","wind":{"deg":"340","dir":"西风","sc":"5-6","spd":"31"}}
     * status : ok
     * suggestion : {"comf":{"brf":"较舒适","txt":"白天虽然天气晴好，但早晚会感觉偏凉，午后舒适、宜人。"},"cw":{"brf":"较适宜","txt":"较适宜洗车，未来一天无雨，风力较小，擦洗一新的汽车至少能保持一天。"},"drsg":{"brf":"较冷","txt":"建议着厚外套加毛衣等服装。年老体弱者宜着大衣、呢外套加羊毛衫。"},"flu":{"brf":"较易发","txt":"天气较凉，较易发生感冒，请适当增加衣服。体质较弱的朋友尤其应该注意防护。"},"sport":{"brf":"较不宜","txt":"天气较好，但考虑天气寒冷，推荐您进行室内运动，户外运动时请注意保暖并做好准备活动。"},"trav":{"brf":"适宜","txt":"天气较好，气温稍低，会感觉稍微有点凉，不过也是个好天气哦。适宜旅游，可不要错过机会呦！"},"uv":{"brf":"弱","txt":"紫外线强度较弱，建议出门前涂擦SPF在12-15之间、PA+的防晒护肤品。"}}
     */

    @SerializedName("HeWeather data service 3.0")
    private List<HeWeather_data> HeWeather_data;

    public void setHeWeather_data(List<HeWeather_data> HeWeather_data) {
        this.HeWeather_data = HeWeather_data;
    }

    public List<HeWeather_data> getHeWeather_data() {
        return HeWeather_data;
    }

    public static class HeWeather_data {
        /**
         * city : 北京
         * cnty : 中国
         * id : CN101010100
         * lat : 39.904000
         * lon : 116.391000
         * update : {"loc":"2016-03-11 14:50","utc":"2016-03-11 06:50"}
         */

        private BasicEntity basic;
        /**
         * cond : {"code":"100","txt":"晴"}
         * fl : -8
         * hum : 13
         * pcpn : 0
         * pres : 1035
         * tmp : 8
         * vis : 10
         * wind : {"deg":"340","dir":"西风","sc":"5-6","spd":"31"}
         */

        private NowEntity now;
        private String status;

        public void setBasic(BasicEntity basic) {
            this.basic = basic;
        }

        public void setNow(NowEntity now) {
            this.now = now;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public BasicEntity getBasic() {
            return basic;
        }

        public NowEntity getNow() {
            return now;
        }

        public String getStatus() {
            return status;
        }

        public static class BasicEntity {
            private String city;
            private String cnty;
            private String lat;
            /**
             * loc : 2016-03-11 14:50
             * utc : 2016-03-11 06:50
             */

            private UpdateEntity update;

            public void setCity(String city) {
                this.city = city;
            }

            public void setCnty(String cnty) {
                this.cnty = cnty;
            }

            public void setLat(String lat) {
                this.lat = lat;
            }

            public void setUpdate(UpdateEntity update) {
                this.update = update;
            }

            public String getCity() {
                return city;
            }

            public String getCnty() {
                return cnty;
            }

            public String getLat() {
                return lat;
            }

            public UpdateEntity getUpdate() {
                return update;
            }

            public static class UpdateEntity {
                private String loc;

                public void setLoc(String loc) {
                    this.loc = loc;
                }

                public String getLoc() {
                    return loc;
                }
            }
        }

        public static class NowEntity {
            /**
             * code : 100
             * txt : 晴
             */

            private CondEntity cond;
            private String hum;
            private String tmp;
            /**
             * deg : 340
             * dir : 西风
             * sc : 5-6
             * spd : 31
             */

            private WindEntity wind;

            public void setCond(CondEntity cond) {
                this.cond = cond;
            }

            public void setHum(String hum) {
                this.hum = hum;
            }

            public void setTmp(String tmp) {
                this.tmp = tmp;
            }

            public void setWind(WindEntity wind) {
                this.wind = wind;
            }

            public CondEntity getCond() {
                return cond;
            }

            public String getHum() {
                return hum;
            }

            public String getTmp() {
                return tmp;
            }

            public WindEntity getWind() {
                return wind;
            }

            public static class CondEntity {
                private String txt;

                public void setTxt(String txt) {
                    this.txt = txt;
                }

                public String getTxt() {
                    return txt;
                }
            }

            public static class WindEntity {
                private String dir;
                private String sc;

                public void setDir(String dir) {
                    this.dir = dir;
                }

                public void setSc(String sc) {
                    this.sc = sc;
                }

                public String getDir() {
                    return dir;
                }

                public String getSc() {
                    return sc;
                }
            }
        }
    }
}
