package com.example.health_iot_app.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.health_iot_app.R;
import com.example.health_iot_app.models.HistoryItemModel;
import com.example.health_iot_app.models.NestedHistoryItemModel;
import com.example.health_iot_app.utils.HistoryRvAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SensorsDataFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SensorsDataFragment extends Fragment {

    private RecyclerView rvHistory;
    private List<HistoryItemModel> historyTitles;
    private HistoryRvAdapter historyRvAdapter;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    public SensorsDataFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static SensorsDataFragment newInstance(String param1, String param2) {
        SensorsDataFragment fragment = new SensorsDataFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sensors_data, container, false);
        initComponents(view);
        return view;
    }

    private void initComponents(View view) {
        rvHistory = view.findViewById(R.id.rv_history);
        rvHistory.setHasFixedSize(true);
        rvHistory.setLayoutManager(new LinearLayoutManager(getContext()));
        historyTitles = new ArrayList<>();

        List<NestedHistoryItemModel> roomTempList = new ArrayList<>();
        roomTempList.add(new NestedHistoryItemModel("22.7", "C", "GOOD"));
        roomTempList.add(new NestedHistoryItemModel("29.7", "C", "HIGH"));
        roomTempList.add(new NestedHistoryItemModel("15.7", "C", "LOW"));
        roomTempList.add(new NestedHistoryItemModel("22.7", "C", "GOOD"));

        List<NestedHistoryItemModel> humidityList = new ArrayList<>();
        humidityList.add(new NestedHistoryItemModel("47.7", "%", "GOOD"));
        humidityList.add(new NestedHistoryItemModel("50", "%", "GOOD"));
        humidityList.add(new NestedHistoryItemModel("70.2", "%", "HIGH"));
        humidityList.add(new NestedHistoryItemModel("12.7", "%", "LOW"));

        List<NestedHistoryItemModel> bodyTempList = new ArrayList<>();
        bodyTempList.add(new NestedHistoryItemModel("37.5", "C", "GOOD"));
        bodyTempList.add(new NestedHistoryItemModel("39.5", "C", "HIGH"));
        bodyTempList.add(new NestedHistoryItemModel("35.5", "C", "LOW"));
        bodyTempList.add(new NestedHistoryItemModel("37.3", "C", "GOOD"));

        List<NestedHistoryItemModel> pulseList = new ArrayList<>();
        pulseList.add(new NestedHistoryItemModel("60", "BPM", "GOOD"));
        pulseList.add(new NestedHistoryItemModel("100", "BPM", "HIGH"));
        pulseList.add(new NestedHistoryItemModel("70", "BPM", "GOOD"));
        pulseList.add(new NestedHistoryItemModel("45", "BPM", "LOW"));

        historyTitles.add(new HistoryItemModel(roomTempList, "Istoric temperatura camera", R.drawable.room_temp));
        historyTitles.add(new HistoryItemModel(humidityList, "Istoric umiditate camera", R.drawable.humidity));
        historyTitles.add(new HistoryItemModel(bodyTempList, "Istoric temperatura corpului", R.drawable.thermometer));
        historyTitles.add(new HistoryItemModel(pulseList, "Istoric puls", R.drawable.electrocardiogram));
        historyRvAdapter = new HistoryRvAdapter(historyTitles);
        rvHistory.setAdapter(historyRvAdapter);

    }


}