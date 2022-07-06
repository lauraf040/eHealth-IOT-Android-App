package com.example.health_iot_app.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;
import com.example.health_iot_app.R;
import com.example.health_iot_app.models.HistoryItemModel;
import com.example.health_iot_app.models.NestedHistoryItemModel;
import com.example.health_iot_app.models.SensorData;
import com.example.health_iot_app.network.ApiClient;
import com.example.health_iot_app.network.ApiService;
import com.example.health_iot_app.utils.HistoryRvAdapter;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class SensorsDataFragment extends Fragment {

    private RecyclerView rvHistory;
    private List<HistoryItemModel> historyTitles;
    private HistoryRvAdapter historyRvAdapter;
    private TextView tvBodyTemp;
    private TextView tvRoomTemp;
    private TextView tvHumidity;
    private CompositeDisposable compositeDisposable;
    private SensorData lastSensorData;
    List<NestedHistoryItemModel> humidityList = new ArrayList<>();
    List<NestedHistoryItemModel> bodyTempList = new ArrayList<>();
    List<NestedHistoryItemModel> roomTempList = new ArrayList<>();
    List<NestedHistoryItemModel> pulseList = new ArrayList<>();
    private static final DecimalFormat df = new DecimalFormat("0.00");

    public SensorsDataFragment() {
        // Required empty public constructor
    }


    public static SensorsDataFragment newInstance(String param1, String param2) {
        SensorsDataFragment fragment = new SensorsDataFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sensors_data, container, false);
        initComponents(view);
        initPython();
        return view;
    }

    private void initPython() {
        if (!Python.isStarted()) {
            Python.start(new AndroidPlatform(getContext()));
        }
        Python py = Python.getInstance();
        //Python pyObj = py.getModule("");
    }

    private void initComponents(View view) {
        rvHistory = view.findViewById(R.id.rv_history);
        rvHistory.setHasFixedSize(true);
        rvHistory.setLayoutManager(new LinearLayoutManager(getContext()));
        historyTitles = new ArrayList<>();
        getHistoryHumidity();
        getHistoryBodyTemp();
        getHistoryRoomTemp();
        getHistoryPulse();

//        List<NestedHistoryItemModel> bodyTempList = new ArrayList<>();
//        bodyTempList.add(new NestedHistoryItemModel("37.5", "C", "GOOD"));
//        bodyTempList.add(new NestedHistoryItemModel("39.5", "C", "HIGH"));
//        bodyTempList.add(new NestedHistoryItemModel("35.5", "C", "LOW"));
//        bodyTempList.add(new NestedHistoryItemModel("37.3", "C", "GOOD"));


        pulseList.add(new NestedHistoryItemModel("60", "BPM", "GOOD"));
        pulseList.add(new NestedHistoryItemModel("100", "BPM", "HIGH"));
        pulseList.add(new NestedHistoryItemModel("70", "BPM", "GOOD"));
        pulseList.add(new NestedHistoryItemModel("45", "BPM", "LOW"));

        historyTitles.add(new HistoryItemModel(roomTempList, "Istoric temperatura camera", R.drawable.ic_room_temp));
        historyTitles.add(new HistoryItemModel(humidityList, "Istoric umiditate camera", R.drawable.ic_sensor_humidity));
        historyTitles.add(new HistoryItemModel(bodyTempList, "Istoric temperatura corpului", R.drawable.ic_body_temp));
        historyTitles.add(new HistoryItemModel(pulseList, "Istoric puls", R.drawable.electrocardiogram));
        historyRvAdapter = new HistoryRvAdapter(historyTitles);
        rvHistory.setAdapter(historyRvAdapter);


        //
        tvBodyTemp = view.findViewById(R.id.tv_body_temp_value);
        tvRoomTemp = view.findViewById(R.id.tv_room_temp_value);
        tvHumidity = view.findViewById(R.id.tv_room_humidity_value);

        fetchLastData();
    }

    //=====================================================REAL TIME DATA===================================================
    private void fetchLastData() {
        ApiService api = ApiClient.getService();
        compositeDisposable.add(Observable.interval(0, 5, TimeUnit.SECONDS)
                .flatMap(i -> api.getLastData())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(sensorData -> displayLastData(sensorData)));
    }

    private void displayLastData(SensorData sensorData) {
        tvBodyTemp.setText(String.valueOf(df.format(Double.parseDouble(sensorData.getBodyTemp()))));
        tvRoomTemp.setText(String.valueOf(df.format(Double.parseDouble(sensorData.getRoomTemp()))));
        tvHumidity.setText(String.valueOf(df.format(Double.parseDouble(sensorData.getHumidity()))));
    }

    //=============================================================HISTORY DATA=================================================
    private void getHistoryHumidity() {
        humidityList = new ArrayList<>();
        ApiService api = ApiClient.getService();
        compositeDisposable.add(Observable.interval(0, 5, TimeUnit.SECONDS)
                .flatMap(list -> api.getLastTenHumidityData())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(historyData -> populateHumidityHistory(historyData)));
    }

    private void populateHumidityHistory(List<SensorData.SensorHumidityData> humidityHistoryData) {
        if (humidityList.size() > 0) {
            humidityList = new ArrayList<>();
        }
        for (SensorData.SensorHumidityData hData : humidityHistoryData) {
            if (Double.parseDouble(hData.getHumidity()) > 35 && Double.parseDouble(hData.getHumidity()) < 55) {
                humidityList.add(new NestedHistoryItemModel(String.valueOf(df.format(Double.parseDouble(hData.getHumidity()))), "%", "GOOD"));
            } else if (Double.parseDouble(hData.getHumidity()) <= 35) {
                humidityList.add(new NestedHistoryItemModel(String.valueOf(df.format(Double.parseDouble(hData.getHumidity()))), "%", "TOO LOW"));
            } else if (Double.parseDouble(hData.getHumidity()) >= 55) {
                humidityList.add(new NestedHistoryItemModel(String.valueOf(df.format(Double.parseDouble(hData.getHumidity()))), "%", "TOO HIGH"));
            }

        }
    }

    private void getHistoryBodyTemp() {
        bodyTempList = new ArrayList<>();
        ApiService api = ApiClient.getService();
        compositeDisposable.add(Observable.interval(0, 5, TimeUnit.SECONDS)
                .flatMap(list -> api.getLastTenBodyTempData())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(historyData -> populateBodyTempHistory(historyData)));
    }

    private void populateBodyTempHistory(List<SensorData.SensorBodyTemperatureData> historyData) {
        if (bodyTempList.size() > 0) {
            humidityList = new ArrayList<>();
        }
        for (SensorData.SensorBodyTemperatureData btData : historyData) {
            if (Double.parseDouble(btData.getBodyTemp()) > 36.8 && Double.parseDouble(btData.getBodyTemp()) < 37.3) {
                bodyTempList.add(new NestedHistoryItemModel(String.valueOf(df.format(Double.parseDouble(btData.getBodyTemp()))), "C", "GOOD"));
            } else if (Double.parseDouble(btData.getBodyTemp()) <= 36.8) {
                bodyTempList.add(new NestedHistoryItemModel(String.valueOf(df.format(Double.parseDouble(btData.getBodyTemp()))), "C", "TOO LOW"));
            } else if (Double.parseDouble(btData.getBodyTemp()) >= 37.3) {
                bodyTempList.add(new NestedHistoryItemModel(String.valueOf(df.format(Double.parseDouble(btData.getBodyTemp()))), "C", "TOO HIGH"));
            }

        }
    }

    private void getHistoryPulse() {

    }

    private void getHistoryRoomTemp() {
        roomTempList = new ArrayList<>();
        ApiService api = ApiClient.getService();
        compositeDisposable.add(Observable.interval(0, 5, TimeUnit.SECONDS)
                .flatMap(list -> api.getLastTenRoomTempData())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(historyData -> populateRoomTempHistory(historyData)));
    }

    private void populateRoomTempHistory(List<SensorData.SensorRoomTemperatureData> historyData) {
        if (roomTempList.size() > 0) {
            roomTempList = new ArrayList<>();
        }
        for (SensorData.SensorRoomTemperatureData rData : historyData) {
            if (Double.parseDouble(rData.getRoomTemp()) > 16 && Double.parseDouble(rData.getRoomTemp()) < 25) {
                roomTempList.add(new NestedHistoryItemModel(String.valueOf(df.format(Double.parseDouble(rData.getRoomTemp()))), "C", "GOOD"));
            } else if (Double.parseDouble(rData.getRoomTemp()) <= 16) {
                roomTempList.add(new NestedHistoryItemModel(String.valueOf(df.format(Double.parseDouble(rData.getRoomTemp()))), "C", "TOO LOW"));
            } else if (Double.parseDouble(rData.getRoomTemp()) >= 25) {
                roomTempList.add(new NestedHistoryItemModel(String.valueOf(df.format(Double.parseDouble(rData.getRoomTemp()))), "C", "TOO HIGH"));
            }

        }
    }

}