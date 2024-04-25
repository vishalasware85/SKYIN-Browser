package com.skyinbrowser.DatabaseAndUnits;

public class NewsBookmarkRecord{
    private String title;
        public String getTitle() {
            return title;
        }
        public void setTitle(String title) {
            this.title = title;
        }

        private String url;
        public String getURL() {
            return url;
        }
        public void setURL(String url) {
            this.url = url;
        }

        private String source;
        public String getSource(){
            return source;
        }
        public void setSource(String source){
            this.source=source;
        }

        private String language;
        public String getLanguage(){
            return language;
        }
        public void setLanguage(String language){
            this.language=language;
        }

        private String image;
        public String getImage(){
        return image;
        }
        public void setImage(String image){
        this.image=image;
    }

        private String description;
        public String getDescription(){
            return description;
        }
        public void setDescription(String description){
        this.description=description;
    }

        private long time;
        public long getTime() {
            return time;
        }
        public void setTime(long time) {
            this.time = time;
        }

        public NewsBookmarkRecord() {
            this.title = null;
            this.url = null;
            this.source=null;
            this.language=null;
            this.image=null;
            this.description=null;
            this.time = 0l;
        }

        public NewsBookmarkRecord(String title, String url,String source,String language,String image,String description, long time) {
            this.title = title;
            this.url = url;
            this.source=source;
            this.language=language;
            this.image=image;
            this.description=description;
            this.time = time;
        }
}
