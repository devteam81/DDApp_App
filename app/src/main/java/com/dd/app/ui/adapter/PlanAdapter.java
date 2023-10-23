package com.dd.app.ui.adapter;

import android.app.Dialog;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Paint;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dd.app.R;
import com.dd.app.listener.OnLoadMoreVideosListener;
import com.dd.app.model.SubscriptionPlan;
import com.dd.app.network.APIClient;
import com.dd.app.network.APIInterface;
import com.dd.app.ui.activity.PlansActivity;
import com.dd.app.ui.fragment.bottomsheet.PaymentBottomSheet;
import com.dd.app.util.UiUtils;

import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.dd.app.ui.activity.PlansActivity.isCoinsUsed;
import static com.dd.app.ui.fragment.bottomsheet.PaymentBottomSheet.setPaymentsInterface;

public class PlanAdapter extends RecyclerView.Adapter<PlanAdapter.ViewHolder> {

    private final String TAG = PlanAdapter.class.getSimpleName();
    public static int position;

    private PlansActivity context;
    private ArrayList<SubscriptionPlan> planList;
    private LayoutInflater inflater;
    private OnLoadMoreVideosListener listener;
    APIInterface apiInterface;
    TextView amountToPay, dedectedAmt;
    public static int selectedposition = 0;

    public PlanAdapter(PlansActivity activity, OnLoadMoreVideosListener listener, ArrayList<SubscriptionPlan> subList) {
        this.context = activity;
        this.listener = listener;
        this.planList = subList;
        apiInterface = APIClient.getClient().create(APIInterface.class);
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        UiUtils.log(TAG ,"Position: "+ viewType);
        View view = inflater.inflate(R.layout.item_plan_3_subscribe_1, viewGroup, false);

        /*switch (viewType) {
            case 0: view = inflater.inflate(R.layout.item_plan_3_subscribe_1, viewGroup, false);
                break;
            case 2: view = inflater.inflate(R.layout.item_plan_3_subscribe_2, viewGroup, false);
                break;
        }*/

        return new ViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        // Just as an example, return 0 or 2 depending on position
        // Note that unlike in ListView adapters, types don't have to be contiguous
        return position % 2 * 2;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        viewHolder.title.setText(planList.get(i).getTitle());
        //viewHolder.tvMonths.setText(String.valueOf(planList.get(i).getMonths()));
        String[] plan = planList.get(i).getMonthFormatted().split("\\s");
        viewHolder.tvMonths.setText(plan[0]);
        viewHolder.tv_months.setText(plan[1]);

        viewHolder.tvFullPriceCurrency.setText(planList.get(i).getSymbol());
        viewHolder.tvPriceCurrency.setText(planList.get(i).getSymbol());

        if(planList.get(i).getSymbol().equalsIgnoreCase(""))
        {
            viewHolder.tvFullPriceCurrency.setText(context.getResources().getString(R.string.currency));
            viewHolder.tvPriceCurrency.setText(context.getResources().getString(R.string.currency));
        }

        if(planList.get(i).getAmount()!= 0) {
            viewHolder.ll_Amount.setVisibility(View.VISIBLE);
            viewHolder.tvFullPriceCurrency.setPaintFlags(viewHolder.tvFullPriceCurrency.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            viewHolder.tvFullPrice.setPaintFlags(viewHolder.tvFullPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            //viewHolder.tvPrice.setText(String.valueOf((int)planList.get(i).getListedPrice()));
            int coinReduced = 0;
            if(planList.get(i).getTotalCoins()>=planList.get(i).getMaxCoins()) {
                coinReduced = planList.get(i).getMaxCoins() / planList.get(i).getPricePerCoins();
                viewHolder.txt_coins.setText(String.valueOf(planList.get(i).getMaxCoins()));
            } else {
                coinReduced = planList.get(i).getTotalCoins() / planList.get(i).getPricePerCoins();
                viewHolder.txt_coins.setText(String.valueOf(planList.get(i).getTotalCoins()));
            }
            if (isCoinsUsed) {
                viewHolder.ll_coins.setVisibility(View.VISIBLE);
                viewHolder.tvPrice.setText(String.valueOf((int)planList.get(i).getListedPrice() - coinReduced));
            } else {
                viewHolder.ll_coins.setVisibility(View.GONE);
                viewHolder.tvPrice.setText(String.valueOf((int)planList.get(i).getListedPrice()));
            }
            viewHolder.tvFullPrice.setText(String.valueOf((int)planList.get(i).getAmount()));
            viewHolder.plan.setText(planList.get(i).getMonthFormatted());
        }else
        {
            viewHolder.ll_Amount.setVisibility(View.GONE);
        }


      /*  viewHolder.noOfAcc.setText(MessageFormat.format(context.getString(R.string.no_of_accounts) + " " +"{0}", planList.get(i).getNoOfAccounts()));
        viewHolder.amount.setText(planList.get(i).getAmountWithCurrency());
        viewHolder.date.setVisibility(View.GONE);
        viewHolder.viewDetails.setOnClickListener(view -> viewFullSubscription(planList.get(i)));
        viewHolder.backAmt.setText(planList.get(i).getAmountWithCurrency());
        viewHolder.perMonth.setText(MessageFormat.format("/{0}", planList.get(i).getMonths()));*/

        /*if (selectedposition == i) {
            viewHolder.root.setBackgroundColor(ContextCompat.getColor(context,R.color.select_theme_green));
        }else {
            viewHolder.root.setBackgroundColor(ContextCompat.getColor(context,R.color.transparent));
        }*/

        viewHolder.root.setOnClickListener(v -> {
            selectedposition = i;
            PlansActivity.plan = planList.get(i);
            notifyDataSetChanged();

            //if(context.prefUtils.getStringValue(PrefKeys.USER_AGE, "").equalsIgnoreCase(""))
                context.showAgePicker();
            /*else {
                setPaymentsInterface(context, PlansActivity.plan, null);
                context.showPaymentPicker(null);
            }*/

        });
    }

    @Override
    public int getItemCount() {
        return planList.size();
    }

    //Dialog to View Full description
    private void viewFullSubscription(SubscriptionPlan plan) {
        Dialog dialog = new Dialog(context, R.style.AppTheme_NoActionBar);
        dialog.setContentView(R.layout.view_plan_dialog);

        Toolbar toolbar = dialog.findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> dialog.cancel());

        TextView noOfAcc = dialog.findViewById(R.id.noOfAcc);
        TextView planName = dialog.findViewById(R.id.planName);
        TextView amount = dialog.findViewById(R.id.amount);
        TextView desc = dialog.findViewById(R.id.desc);
        TextView select = dialog.findViewById(R.id.select);


        planName.setText(plan.getTitle());
        noOfAcc.setText(String.format(Locale.ENGLISH,"%s %d", context.getString(R.string.no_of_profile), plan.getNoOfAccounts()));
        amount.setText(plan.getAmountWithCurrency());
        desc.setText(Html.fromHtml(plan.getDescription()));
        select.setText(plan.getAmount() == 0 ? context.getString(R.string.pay) : context.getString(R.string.select));

        select.setOnClickListener(view -> {
            dialog.dismiss();
            Intent paymentsIntent = new Intent(context, PaymentBottomSheet.class);
            context.startActivity(paymentsIntent);
            setPaymentsInterface(context, plan, null);
        });

        dialog.show();
    }

    public void showLoading() {
        if(listener!=null)
            listener.onLoadMore(planList.size());
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.tvMonths)
        TextView tvMonths;
        @BindView(R.id.tv_months)
        TextView tv_months;
        @BindView(R.id.ll_Amount)
        LinearLayout ll_Amount;
        @BindView(R.id.tvPrice)
        TextView tvPrice;
        @BindView(R.id.tvPriceCurrency)
        TextView tvPriceCurrency;
        @BindView(R.id.tvFullPrice)
        TextView tvFullPrice;
        @BindView(R.id.tvFullPriceCurrency)
        TextView tvFullPriceCurrency;
        @BindView(R.id.txt_plan)
        TextView plan;
        @BindView(R.id.ll_coins)
        LinearLayout ll_coins;
        @BindView(R.id.txt_coins)
        TextView txt_coins;
        @BindView(R.id.root)
        RelativeLayout root;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
