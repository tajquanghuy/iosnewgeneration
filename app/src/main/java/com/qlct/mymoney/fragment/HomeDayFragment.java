package com.qlct.mymoney.fragment;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.qlct.mymoney.R;
import com.qlct.mymoney.adapter.ExpenseAdapter;
import com.qlct.mymoney.adapter.IncomeAdapter;
import com.qlct.mymoney.model.DataBaseIntalizerIncome;
import com.qlct.mymoney.model.DatabaseIntalizer;
import com.qlct.mymoney.model.Expenditures;
import com.qlct.mymoney.model.ExpendituresDB;
import com.qlct.mymoney.model.IncomeDitures;
import com.qlct.mymoney.model.IncomeDituresDB;
import com.qlct.mymoney.viewmodel.AddExpendituresViewModel;
import com.qlct.mymoney.viewmodel.AddIncomeDituresViewModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener;

public class HomeDayFragment extends Fragment {
    private TextView txtCostExpense;
    private TextView txtCostIncome;
    private TextView totalCost;

    private HorizontalCalendar horizontalCalendar;
    private RecyclerView rcExpend;
    private RecyclerView rcIncome;
    private List<Expenditures> expenditures = new ArrayList<>();
    private List<IncomeDitures> incomeDitures = new ArrayList<>();
    ExpenseAdapter expenseAdapter = new ExpenseAdapter(expenditures);


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_home_day, container, false);

        txtCostExpense = rootView.findViewById(R.id.txtCostExpense);
        txtCostIncome = rootView.findViewById(R.id.txtCostIncoming);

        DatabaseIntalizer.populateAsync(ExpendituresDB.getExpendituresDB(getContext()));
        DataBaseIntalizerIncome.populateAsync(IncomeDituresDB.getIncomeDituresBD(getContext()));

        /* start before 1 month from now */
        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.MONTH, -2);

        /* end after 1 month from now */
        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.MONTH, 2);

        horizontalCalendar = new HorizontalCalendar.Builder(rootView, R.id.calendarView)
                .range(startDate, endDate)
                .datesNumberOnScreen(5)
                .configure()
                .formatTopText("MMM")
                .formatMiddleText("dd")
                .formatBottomText("EEE")
                .textSize(14f, 24f, 14f)
                .showTopText(true)
                .showBottomText(true)
                .textColor(Color.LTGRAY, Color.WHITE)
                .end()
                .build();

        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
            @Override
            public void onDateSelected(Calendar date, int position) {
                Toast.makeText(getContext(), DateFormat.format("EEE, MMM d, yyyy", date) + " is selected!", Toast.LENGTH_SHORT).show();
                int day = Integer.valueOf(DateFormat.format("d", date).toString().trim());
                rcExpend = (RecyclerView) rootView.findViewById(R.id.recyclerViewDay);
                rcExpend.setLayoutManager(new LinearLayoutManager(getActivity()));
                rcExpend.setAdapter(expenseAdapter);

                AddExpendituresViewModel viewModel = ViewModelProviders.of(HomeDayFragment.this).get(AddExpendituresViewModel.class);
                viewModel.getExpanddituresDay(day).observe(getActivity(), expenseAdapter::setExpendituresList);
               /* viewModel.getExpanddituresDay(day).observe(getActivity(), new Observer<List<Expenditures>>() {
                    @Override
                    public void onChanged(List<Expenditures> expendituresList) {
                        if (expendituresList != null) {
                            for (int i = 0; i < expendituresList.size(); i++) {
                                Expenditures expenditures = expendituresList.get(i);
                                txtCostExpense.setText(expenditures.getMoney());
                            }
                        }
                    }

                });*/

            }


        });


        rcIncome = rootView.findViewById(R.id.recyclerViewDay_1);


        IncomeAdapter incomeAdapter = new IncomeAdapter(incomeDitures);

        rcIncome.setLayoutManager(new LinearLayoutManager(getActivity()));
        rcIncome.setAdapter(incomeAdapter);


        AddIncomeDituresViewModel viewModel2 = ViewModelProviders.of(this).get(AddIncomeDituresViewModel.class);
        viewModel2.getIncome().observe(getActivity(), incomeAdapter::setIncomeDituresList);
        /*viewModel2.getIncome().observe(getActivity(), new Observer<List<IncomeDitures>>() {
            @Override
            public void onChanged(List<IncomeDitures> incomeDituresList) {
                if(incomeDituresList!=null)
                {
                    for (int i=0;i<incomeDituresList.size();i++)
                    {
                        IncomeDitures incomeDitures = incomeDituresList.get(i);
                        incomeDitures.getDay();
                    }
                }
            }
        });*/

        return rootView;
    }

}