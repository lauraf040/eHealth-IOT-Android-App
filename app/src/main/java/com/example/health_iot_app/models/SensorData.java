package com.example.health_iot_app.models;

public class SensorData {
    private String bodyTemp;
    private String roomTemp;
    private String humidity;
    private String heartRate;
    private String bloodOxygen;

    public SensorData(String bodyTemp, String roomTemp, String humidity, String heartRate, String bloodOxygen) {
        this.bodyTemp = bodyTemp;
        this.roomTemp = roomTemp;
        this.humidity = humidity;
        this.heartRate = heartRate;
        this.bloodOxygen = bloodOxygen;
    }

    public String getHeartRate() {
        return heartRate;
    }

    public void setHeartRate(String heartRate) {
        this.heartRate = heartRate;
    }

    public String getBloodOxygen() {
        return bloodOxygen;
    }

    public void setBloodOxygen(String bloodOxygen) {
        this.bloodOxygen = bloodOxygen;
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

    public class SensorBodyTemperatureData {
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

    public class SensorRoomTemperatureData {
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

    public class SensorPulseData {
        private String heartRate;

        public SensorPulseData(String heartRate) {
            this.heartRate = heartRate;
        }

        public String getHeartRate() {
            return heartRate;
        }

        public void setHeartRate(String heartRate) {
            this.heartRate = heartRate;
        }
    }

    public class SensorBloodOxygenData {
        private String bloodOxygen;

        public SensorBloodOxygenData(String bloodOxygen) {
            this.bloodOxygen = bloodOxygen;
        }

        public String getBloodOxygen() {
            return bloodOxygen;
        }

        public void setBloodOxygen(String bloodOxygen) {
            this.bloodOxygen = bloodOxygen;
        }
    }
}
