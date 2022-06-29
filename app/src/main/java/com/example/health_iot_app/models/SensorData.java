package com.example.health_iot_app.models;

public class SensorData {
    private String bodyTemp;
    private String roomTemp;
    private String humidity;

    public SensorData(String bodyTemp, String roomTemp, String humidity) {
        this.bodyTemp = bodyTemp;
        this.roomTemp = roomTemp;
        this.humidity = humidity;
    }

    public String getBodyTemp() {
        return bodyTemp;
    }

    public void setBodyTemp(String bodyTemp) {
        this.bodyTemp = bodyTemp;
    }

    public String getRoomTemp() {
        return roomTemp;
    }

    public void setRoomTemp(String roomTemp) {
        this.roomTemp = roomTemp;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public class SensorHumidityData {
        private String humidity;

        public SensorHumidityData(String humidityData) {
            this.humidity = humidityData;
        }

        public String getHumidity() {
            return humidity;
        }

        public void setHumidity(String humidity) {
            this.humidity = humidity;
        }
    }
    public class SensorBodyTemperatureData{
        private String bodyTemp;

        public SensorBodyTemperatureData(String bodyTemp) {
            this.bodyTemp = bodyTemp;
        }

        public String getBodyTemp() {
            return bodyTemp;
        }

        public void setBodyTemp(String bodyTemp) {
            this.bodyTemp = bodyTemp;
        }
    }
    public class SensorRoomTemperatureData{
        private String roomTemp;

        public SensorRoomTemperatureData(String roomTemp) {
            this.roomTemp = roomTemp;
        }

        public String getRoomTemp() {
            return roomTemp;
        }

        public void setRoomTemp(String roomTemp) {
            this.roomTemp = roomTemp;
        }
    }

}
