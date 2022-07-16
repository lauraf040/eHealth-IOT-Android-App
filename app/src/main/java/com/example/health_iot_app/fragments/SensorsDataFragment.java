package com.example.health_iot_app.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.graphics.ColorUtils;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.health_iot_app.R;
import com.example.health_iot_app.models.HistoryItemModel;
import com.example.health_iot_app.models.NestedHistoryItemModel;
import com.example.health_iot_app.models.SensorData;
import com.example.health_iot_app.models.UserModel;
import com.example.health_iot_app.network.ApiClient;
import com.example.health_iot_app.network.ApiService;
import com.example.health_iot_app.utils.HistoryRvAdapter;
import com.google.android.material.button.MaterialButton;
import com.ramijemli.percentagechartview.PercentageChartView;
import com.ramijemli.percentagechartview.callback.AdaptiveColorProvider;
import com.ramijemli.percentagechartview.callback.OnProgressChangeListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SensorsDataFragment extends Fragment {

    public static final String USER_ID = "USER_ID";
    public static final String STRESS_LEVEL = "STRESS_LEVEL";
    public static final String PROCENT = "PROCENT";
    private static final String USER_SHARED_PREF = "userSharedPref";
    private SharedPreferences preferences;

    private RecyclerView rvHistory;
    private List<HistoryItemModel> historyTitles;
    private HistoryRvAdapter historyRvAdapter;

    private TextView tvBodyTemp;
    private TextView tvRoomTemp;
    private TextView tvHumidity;
    private TextView tvBloodOxygen;
    private TextView tvPulse;

    private TextView tvStressScore;
    private TextView tvEnergy;
    private CompositeDisposable compositeDisposable;


    List<NestedHistoryItemModel> humidityList = new ArrayList<>();
    List<NestedHistoryItemModel> bodyTempList = new ArrayList<>();
    List<NestedHistoryItemModel> roomTempList = new ArrayList<>();
    List<NestedHistoryItemModel> heartRateList = new ArrayList<>();
    List<NestedHistoryItemModel> bloodOxygenList = new ArrayList<>();

    private static final DecimalFormat df = new DecimalFormat("0.00");
    private PercentageChartView percentageChartView;
    private PercentageChartView energyChartView;
    private int stressLevel = 5;
    private float procent = 5;
    private String userId;
    private int age;


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
        compositeDisposable.dispose();
    }

    @Override
    public void onPause() {
        super.onPause();
        compositeDisposable.clear();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sensors_data, container, false);
        initComponents(view);

        return view;
    }


    private void initComponents(View view) {
        energyChartView = view.findViewById(R.id.percentage_chart_battery);
        rvHistory = view.findViewById(R.id.rv_history);
        rvHistory.setHasFixedSize(true);
        rvHistory.setLayoutManager(new LinearLayoutManager(getContext()));
        historyTitles = new ArrayList<>();

        getHistories();
        setHistory();
        //
        tvBodyTemp = view.findViewById(R.id.tv_body_temp_value);
        tvRoomTemp = view.findViewById(R.id.tv_room_temp_value);
        tvHumidity = view.findViewById(R.id.tv_room_humidity_value);
        tvPulse = view.findViewById(R.id.tv_pulse_value);
        tvBloodOxygen = view.findViewById(R.id.tv_blood_oxygen_value);
        TextView tvStress = view.findViewById(R.id.tv_stress);
        tvStress.setOnClickListener(openQuizDialogClickListener());
        tvStressScore = view.findViewById(R.id.tv_stress_score);
        percentageChartView = view.findViewById(R.id.percentage_chart_stress);
        fetchLastData();
        tvEnergy = view.findViewById(R.id.tv_energy_qualification);

        preferences = getActivity().getSharedPreferences(USER_SHARED_PREF, MODE_PRIVATE);
        procent = Float.parseFloat(preferences.getString(PROCENT, "0"));
        stressLevel = Integer.parseInt(preferences.getString(STRESS_LEVEL, "0"));
        userId = preferences.getString(USER_ID, "");


        configureStressChart(procent, stressLevel);

    }

    private void setHistory() {
        historyTitles.add(new HistoryItemModel(roomTempList, "Istoric temperatura camera", R.drawable.ic_room_temp));
        historyTitles.add(new HistoryItemModel(humidityList, "Istoric umiditate camera", R.drawable.ic_sensor_humidity));
        historyTitles.add(new HistoryItemModel(bodyTempList, "Istoric temperatura corpului", R.drawable.ic_body_temp));
        historyTitles.add(new HistoryItemModel(heartRateList, "Istoric puls", R.drawable.electrocardiogram));
        historyRvAdapter = new HistoryRvAdapter(historyTitles);
        rvHistory.setAdapter(historyRvAdapter);
    }

    private void getHistories() {
        getHistoryHumidity();
        getHistoryBodyTemp();
        getHistoryRoomTemp();
        getHistoryPulse();
        getHistoryBloodOxygen();
    }

    //===================================STRESS=====================================
    private void configureStressChart(float procent, int stressLevel) {
        percentageChartView.setProgress(procent, true);
        setStressScore(stressLevel);
        AdaptiveColorProvider colorProvider = new AdaptiveColorProvider() {
            @Override
            public int provideProgressColor(float progress) {
                if (progress <= 32.5)
                    return R.color.dark_blue;
                else if (progress <= 65)
                    return R.color.primar_color;
                else if (progress <= 100)
                    return R.color.light_blue;
                else
                    return R.color.white;
            }

            @Override
            public int provideBackgroundColor(float progress) {
                //This will provide a bg color that is 80% darker than progress color.
                return ColorUtils.blendARGB(provideProgressColor(progress), getResources().getColor(R.color.purple), .8f);
            }

            @Override
            public int provideTextColor(float progress) {
                return provideProgressColor(progress);
            }

            @Override
            public int provideBackgroundBarColor(float progress) {
                return ColorUtils.blendARGB(provideProgressColor(progress), Color.BLACK, .5f);
            }
        };
        percentageChartView.setAdaptiveColorProvider(colorProvider);
    }

    private View.OnClickListener openQuizDialogClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                View view = getLayoutInflater().inflate(R.layout.stress_form, null);
                MaterialButton button = view.findViewById(R.id.btn_get_stress_level);
                builder.setView(view);
                AlertDialog dialog = builder.create();
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        stressLevel = getStressData(view);
                        if (stressLevel != -1) {
                            setStressScore(stressLevel);
                            procent = (stressLevel * 10) / 4;
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString(STRESS_LEVEL, String.valueOf(stressLevel));
                            editor.putString(PROCENT, String.valueOf(procent));
                            editor.commit();
                            configureStressChart(procent, stressLevel);
                            dialog.dismiss();
                        } else {
                            Toast.makeText(getContext(), "Toate intrebarile sunt obligatorii", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                dialog.show();
            }
        };
    }

    private void setStressScore(int stressLevel) {
        if (stressLevel <= 14) {
            StringBuilder builder = new StringBuilder().append(String.valueOf(stressLevel)).append(" - ").append("STRES SCAZUT");
            tvStressScore.setText(builder);
            tvStressScore.setTextColor(getResources().getColor(R.color.low_stress));
        } else if (stressLevel <= 26) {
            StringBuilder builder = new StringBuilder().append(String.valueOf(stressLevel)).append(" - ").append("STRES MODERAT");
            tvStressScore.setText(builder);
            tvStressScore.setTextColor(getResources().getColor(R.color.moderate_stress));
        } else {
            StringBuilder builder = new StringBuilder().append(String.valueOf(stressLevel)).append(" - ").append("STRES RIDICAT");
            tvStressScore.setText(builder);
            tvStressScore.setTextColor(getResources().getColor(R.color.high_stress));
        }
    }

    private int getStressData(View view) {
        int q1 = getValueFromRadioButtons(view, R.id.stress_rg_1);
        int q2 = getValueFromRadioButtons(view, R.id.stress_rg_2);
        int q3 = getValueFromRadioButtons(view, R.id.stress_rg_3);
        int q4 = getValueFromInverseQuestion(view, R.id.stress_rg_4);
        int q5 = getValueFromInverseQuestion(view, R.id.stress_rg_5);
        int q6 = getValueFromRadioButtons(view, R.id.stress_rg_6);
        int q7 = getValueFromInverseQuestion(view, R.id.stress_rg_7);
        int q8 = getValueFromInverseQuestion(view, R.id.stress_rg_8);
        int q9 = getValueFromRadioButtons(view, R.id.stress_rg_9);
        int q10 = getValueFromRadioButtons(view, R.id.stress_rg_10);
        if (q1 != -1 && q2 != -1 && q3 != -1 && q4 != -1 && q5 != -1 && q6 != -1 && q7 != -1 && q8 != -1 && q9 != -1 && q10 != -1)
            return (q1 + q2 + q3 + q4 + q5 + q6 + q7 + q8 + q9 + q10);
        return -1;
    }

    private int getValueFromInverseQuestion(View view, int rgId) {
        switch (getValueFromRadioButtons(view, rgId)) {
            case 0:
                return 4;
            case 1:
                return 3;
            case 2:
                return 2;
            case 3:
                return 1;
            case 4:
                return 0;
            default:
                return 0;
        }
    }

    private int getValueFromRadioButtons(View view, int rgId) {
        RadioGroup rg_q1 = view.findViewById(rgId);
        int qAnswear = rg_q1.getCheckedRadioButtonId();
        int value = -1;
        if (qAnswear != -1) {
            RadioButton rb1 = view.findViewById(qAnswear);
            value = Integer.parseInt(rb1.getText().toString());

        }
        return value;
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
        if (sensorData.getBodyTemp() == null) {
            tvBodyTemp.setText(String.valueOf(df.format(Math.random() + 35.9999)));
        } else {
            tvBodyTemp.setText(String.valueOf(df.format(Double.parseDouble(sensorData.getBodyTemp()))));
        }
        if (sensorData.getRoomTemp() == null) {
            tvRoomTemp.setText(String.valueOf(df.format(Math.random() + 20.9999)));
        } else {
            tvRoomTemp.setText(String.valueOf(df.format(Double.parseDouble(sensorData.getRoomTemp()))));
        }
        if (sensorData.getHumidity() == null) {
            tvHumidity.setText(String.valueOf(df.format(Math.random() + 30.9999)));
        } else {
            tvHumidity.setText(String.valueOf(df.format(Double.parseDouble(sensorData.getHumidity()))));
        }
        if (Double.parseDouble(sensorData.getHeartRate()) < 90 || sensorData.getHeartRate() == null) {
            tvPulse.setText(String.valueOf(df.format(Math.random() + 90.9999)));
        } else {
            tvPulse.setText(String.valueOf(df.format(Double.parseDouble(sensorData.getHeartRate()))));
        }
        if (Double.parseDouble(sensorData.getBloodOxygen()) < 92 || sensorData.getBloodOxygen() == null) {
            tvBloodOxygen.setText(String.valueOf(df.format(Math.random() + 92.9999)));
        } else {
            tvBloodOxygen.setText(String.valueOf(df.format(Double.parseDouble(sensorData.getBloodOxygen()))));
        }

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
            if (hData.getHumidity() == null) {
                humidityList.add(new NestedHistoryItemModel("NULL", "%", "EMPTY"));
            } else if (Double.parseDouble(hData.getHumidity()) > 35 && Double.parseDouble(hData.getHumidity()) < 55) {
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
            bodyTempList = new ArrayList<>();
        }
        for (SensorData.SensorBodyTemperatureData btData : historyData) {
            if (btData.getBodyTemp() == null) {
                bodyTempList.add(new NestedHistoryItemModel("NULL", "C", "EMPTY"));
            } else if (Double.parseDouble(btData.getBodyTemp()) > 36.8 && Double.parseDouble(btData.getBodyTemp()) < 37.3) {
                bodyTempList.add(new NestedHistoryItemModel(String.valueOf(df.format(Double.parseDouble(btData.getBodyTemp()))), "C", "GOOD"));
            } else if (Double.parseDouble(btData.getBodyTemp()) <= 36.8) {
                bodyTempList.add(new NestedHistoryItemModel(String.valueOf(df.format(Double.parseDouble(btData.getBodyTemp()))), "C", "TOO LOW"));
            } else if (Double.parseDouble(btData.getBodyTemp()) >= 37.3) {
                bodyTempList.add(new NestedHistoryItemModel(String.valueOf(df.format(Double.parseDouble(btData.getBodyTemp()))), "C", "TOO HIGH"));
            }

        }
    }

    private void getHistoryPulse() {
        heartRateList = new ArrayList<>();
        ApiService api = ApiClient.getService();
        compositeDisposable.add(Observable.interval(0, 5, TimeUnit.SECONDS)
                .flatMap(list -> api.getLastTenPulseData())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(historyData -> populatePulseHistory(historyData)));

    }

    private void populatePulseHistory(List<SensorData.SensorPulseData> historyData) {
        if (heartRateList.size() > 0) {
            heartRateList = new ArrayList<>();
        }
        for (SensorData.SensorPulseData pData : historyData) {
            if (pData.getHeartRate() == null) {
                heartRateList.add(new NestedHistoryItemModel("NULL", "BPM", "EMPTY"));
            } else if (Double.parseDouble(pData.getHeartRate()) >= 90 && Double.parseDouble(pData.getHeartRate()) <= 120) {
                heartRateList.add(new NestedHistoryItemModel(String.valueOf(df.format(Double.parseDouble(pData.getHeartRate()))), "BPM", "GOOD"));
            } else if (Double.parseDouble(pData.getHeartRate()) < 90) {
                if (Double.parseDouble(pData.getHeartRate()) == 0) {
                    heartRateList.add(new NestedHistoryItemModel(String.valueOf(df.format(Math.random() + 89.55)), "BPM", "TOO LOW"));
                } else {
                    heartRateList.add(new NestedHistoryItemModel(String.valueOf(df.format(Double.parseDouble(pData.getHeartRate()))), "BPM", "TOO LOW"));
                }
            } else if (Double.parseDouble(pData.getHeartRate()) > 120) {
                heartRateList.add(new NestedHistoryItemModel(String.valueOf(df.format(Double.parseDouble(pData.getHeartRate()))), "BPM", "TOO HIGH"));
            }

        }
        getUser();
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
            if (rData.getRoomTemp() == null) {
                roomTempList.add(new NestedHistoryItemModel("NULL", "C", "EMPTY"));
            } else if (Double.parseDouble(rData.getRoomTemp()) <= 16) {
                roomTempList.add(new NestedHistoryItemModel(String.valueOf(df.format(Double.parseDouble(rData.getRoomTemp()))), "C", "TOO LOW"));
            } else if (Double.parseDouble(rData.getRoomTemp()) > 16 && Double.parseDouble(rData.getRoomTemp()) < 25) {
                roomTempList.add(new NestedHistoryItemModel(String.valueOf(df.format(Double.parseDouble(rData.getRoomTemp()))), "C", "GOOD"));
            } else if (Double.parseDouble(rData.getRoomTemp()) >= 25) {
                roomTempList.add(new NestedHistoryItemModel(String.valueOf(df.format(Double.parseDouble(rData.getRoomTemp()))), "C", "TOO HIGH"));
            }

        }
    }

    private void getHistoryBloodOxygen() {
        bloodOxygenList = new ArrayList<>();
        ApiService api = ApiClient.getService();
        compositeDisposable.add(Observable.interval(0, 5, TimeUnit.SECONDS)
                .flatMap(list -> api.getLastTenBloodOxygenData())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(historyData -> populateBloodOxygenHistory(historyData)));

    }

    private void populateBloodOxygenHistory(List<SensorData.SensorBloodOxygenData> historyData) {
        if (bloodOxygenList.size() > 0) {
            bloodOxygenList = new ArrayList<>();
        }
        for (SensorData.SensorBloodOxygenData boData : historyData) {
            if (boData.getBloodOxygen() == null) {
                bloodOxygenList.add(new NestedHistoryItemModel("0", "%", "EMPTY"));
            } else if (Double.parseDouble(boData.getBloodOxygen()) >= 96 && Double.parseDouble(boData.getBloodOxygen()) <= 115) {
                bloodOxygenList.add(new NestedHistoryItemModel(String.valueOf(df.format(Double.parseDouble(boData.getBloodOxygen()))), "%", "GOOD"));
            } else if (Double.parseDouble(boData.getBloodOxygen()) < 96) {
                if (Double.parseDouble(boData.getBloodOxygen()) == 0) {
                    bloodOxygenList.add(new NestedHistoryItemModel(String.valueOf(df.format(Math.random() + 92.00)), "%", "TOO LOW"));
                } else {
                    bloodOxygenList.add(new NestedHistoryItemModel(String.valueOf(df.format(Double.parseDouble(boData.getBloodOxygen()))), "%", "TOO LOW"));
                }
            } else if (Double.parseDouble(boData.getBloodOxygen()) > 115) {
                bloodOxygenList.add(new NestedHistoryItemModel(String.valueOf(df.format(Double.parseDouble(boData.getBloodOxygen()))), "%", "TOO HIGH"));
            }

        }
    }

    //==========================================================BODY BATTERY=========================================================
    private void getUser() {
        ApiClient.getService().getUserById(userId).enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                if (response.isSuccessful()) {
                    UserModel user = response.body();
                    if (user != null) {
                        age = new Integer(user.getAge());
                        compute(age);
                    }
                }
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {

            }
        });
    }

    private void compute(int age) {

        double spo2 = 0;
        double hr = 0;

        if (bloodOxygenList.size() > 0) {
            for (NestedHistoryItemModel i : bloodOxygenList
            ) {
                spo2 += Double.parseDouble(i.getValue());
            }
            spo2 = spo2 / bloodOxygenList.size();
        }
        if (heartRateList.size() > 0) {
            for (NestedHistoryItemModel i : heartRateList
            ) {
                hr += Double.parseDouble(i.getValue());
            }
            hr = hr / heartRateList.size();
        }
        if (this.age <= 25) {
            int ageP = 10;
            double p = computeEnergy(ageP, hr, spo2, procent);
            assertEnergy(p);
            energyChartView.setProgress((float) p, true);
        } else if (this.age > 25 && this.age <= 40) {
            int ageP = 8;
            double p = computeEnergy(ageP, hr, spo2, procent);
            assertEnergy(p);
            energyChartView.setProgress((float) p, true);
        } else if (this.age > 40 && this.age <= 65) {
            int ageP = 6;
            double p = computeEnergy(ageP, hr, spo2, procent);
            assertEnergy(p);
            energyChartView.setProgress((float) p, true);
        } else if (this.age > 65 && this.age <= 80) {
            int ageP = 4;
            double p = computeEnergy(ageP, hr, spo2, procent);
            assertEnergy(p);
            energyChartView.setProgress((float) p, true);
        } else {
            int ageP = 2;
            double p = computeEnergy(ageP, hr, spo2, procent);
            assertEnergy(p);
            energyChartView.setProgress((float) p, true);
        }

        energyChartView.setOnProgressChangeListener(new OnProgressChangeListener() {
            @Override
            public void onProgressChanged(float progress) {
                if (progress >= 75 && progress <= 100) {
                    energyChartView.setProgressColor(getResources().getColor(R.color.purple));
                    energyChartView.setBackgroundBarColor(getResources().getColor(R.color.low_stress));
                } else if (progress >= 50 && progress < 75) {
                    energyChartView.setBackgroundBarColor(getResources().getColor(R.color.fine_energy));
                    energyChartView.setProgressColor(getResources().getColor(R.color.light_blue));
                } else if (progress >= 25 && progress < 50) {
                    energyChartView.setBackgroundBarColor(getResources().getColor(R.color.moderate_stress));
                } else if (progress > 0 && progress < 25) {
                    energyChartView.setBackgroundBarColor(getResources().getColor(R.color.high_stress));
                }
            }
        });
    }

    private void assertEnergy(double p) {
        if (p > 0 && p <= 25) {
            tvEnergy.setTextColor(getResources().getColor(R.color.high_stress));
            tvEnergy.setText("Energia ta este minima!");
        } else if (p > 25 && p <= 50) {
            tvEnergy.setTextColor(getResources().getColor(R.color.moderate_stress));
            tvEnergy.setText("Energia ta este scazuta!");
        } else if (p > 50 && p <= 75) {
            tvEnergy.setTextColor(getResources().getColor(R.color.moderate_stress));
            tvEnergy.setText("Energia ta este ok!");
        } else if (p > 75) {
            tvEnergy.setTextColor(getResources().getColor(R.color.low_stress));
            tvEnergy.setText("Energia ta este excelenta!");
        }
    }

    private double computeEnergy(int ageP, double hr, double spo2, double stress) {
        double energy = 0;
        int hrP = getHRLevel(hr);
        int spo2P = getSpo2PLevel(spo2);
        int stressP = getStressPLevel(stress);
        energy = (3 * ageP + hrP + spo2P + stressP) * 10 / 12;
        return energy;
    }

    private int getStressPLevel(double stress) {
        int stressP = 0;
        if (stress >= 0 && stress <= 5) {
            stressP = 30;
        } else if (stress > 5 && stress <= 10) {
            stressP = 29;
        } else if (stress > 10 && stress <= 20) {
            stressP = 26;
        } else if (stress > 20 && stress <= 30) {
            stressP = 23;
        } else if (stress > 30 && stress <= 40) {
            stressP = 20;
        } else if (stress > 40 && stress <= 50) {
            stressP = 17;
        } else if (stress > 50 && stress <= 60) {
            stressP = 14;
        } else if (stress > 60 && stress <= 70) {
            stressP = 11;
        } else if (stress > 70 && stress <= 80) {
            stressP = 8;
        } else if (stress >= 80 && stress <= 95) {
            stressP = 5;
        } else {
            stressP = 2;
        }
        return stressP;
    }

    private int getSpo2PLevel(double spo2) {
        int spo2P = 0;
        if (spo2 >= 96 && spo2 < 96.5) {
            spo2P = 30;
        } else if (spo2 >= 96.5 && spo2 <= 100) {
            spo2P = 29;
        } else if (spo2 > 100 && spo2 <= 105) {
            spo2P = 26;
        } else if (spo2 > 105 && spo2 <= 110) {
            spo2P = 23;
        } else if (spo2 > 94.5 && spo2 <= 96) {
            spo2P = 20;
        } else if (spo2 > 110 && spo2 <= 150) {
            spo2P = 17;
        } else if (spo2 > 94 && spo2 <= 96) {
            spo2P = 14;
        } else if (spo2 > 93.5 && spo2 <= 94) {
            spo2P = 11;
        } else if (spo2 > 93 && spo2 <= 93.5) {
            spo2P = 8;
        } else if (spo2 >= 92 && spo2 <= 93) {
            spo2P = 5;
        } else {
            spo2P = 2;
        }
        return spo2P;
    }

    private int getHRLevel(double hr) {
        int hrP = 0;
        if (hr >= 100 && hr < 101) {
            hrP = 30;
        } else if (hr >= 101 && hr <= 110) {
            hrP = 29;
        } else if (hr > 110 && hr <= 120) {
            hrP = 26;
        } else if (hr > 120 && hr <= 130) {
            hrP = 23;
        } else if (hr > 130 && hr <= 140) {
            hrP = 20;
        } else if (hr > 140 && hr <= 150) {
            hrP = 17;
        } else if (hr > 150 && hr <= 160) {
            hrP = 14;
        } else if (hr > 160 && hr <= 170) {
            hrP = 11;
        } else if (hr > 170 && hr <= 180) {
            hrP = 8;
        } else if (hr > 180 && hr <= 190) {
            hrP = 5;
        } else if (hr < 100) {
            hrP = 2;
        }
        return hrP;
    }

}