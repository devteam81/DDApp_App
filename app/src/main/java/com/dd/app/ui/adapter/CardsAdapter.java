package com.dd.app.ui.adapter;

import android.content.Context;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import com.dd.app.R;
import com.dd.app.model.Card;
import com.dd.app.network.APIClient;
import com.dd.app.network.APIConstants;
import com.dd.app.network.APIInterface;
import com.dd.app.ui.activity.PaymentsActivity;
import com.dd.app.util.NetworkUtils;
import com.dd.app.util.UiUtils;
import com.dd.app.util.sharedpref.PrefKeys;
import com.dd.app.util.sharedpref.PrefUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.dd.app.network.APIConstants.Constants;
import static com.dd.app.network.APIConstants.Params;

public class CardsAdapter extends RecyclerView.Adapter<CardsAdapter.CardViewHolder> {

    private final String TAG = CardsAdapter.class.getSimpleName();

    private APIInterface apiInterface;
    private LayoutInflater inflate;
    private Context context;
    private ArrayList<Card> cardList;
    private RadioButton lastChecked = null;
    private CardListener cardListener;
    private boolean checkedChangeDisableFlag = false;
    private boolean isMarkDefaultClicked = false;
    private boolean isDeleteClicked = false;


    public CardsAdapter(Context activity, ArrayList<Card> cards, CardListener listener) {
        this.context = activity;
        this.cardList = cards;
        cardListener = listener;
        inflate = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        apiInterface = APIClient.getClient().create(APIInterface.class);
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = inflate.inflate(R.layout.item_card, viewGroup, false);
        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder viewHolder, int position) {
        Card card = cardList.get(position);
        viewHolder.cardNumb.setText(String.format("%s%s", context.getString(R.string.card_hider_text), card.getLast4()));
        viewHolder.expiryDate.setText(String.format("%s/%s", card.getMonth(), card.getYear()));
//        viewHolder.deleteCard.setVisibility(card.isDefault() ? View.GONE : View.VISIBLE);
        viewHolder.isDefault.setChecked(card.isDefault());
        viewHolder.isDefault.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked && !checkedChangeDisableFlag)
                makeDefaultCardInBackend(card, (RadioButton) buttonView);
        });
        viewHolder.deleteCard.setOnClickListener(v -> new AlertDialog.Builder(context)
                .setIcon(R.mipmap.ic_launcher)
                .setTitle(context.getString(R.string.confirmation))
                .setMessage(String.format("Are you sure to delete card ending with %s?", card.getLast4()))
                .setPositiveButton(context.getString(R.string.yes), (dialog, which) -> deleteCard(card, viewHolder.getAdapterPosition()))
                .setNegativeButton(context.getString(R.string.no), null)
                .create().show());
    }

    @Override
    public int getItemCount() {
        return cardList.size();
    }

    private void deleteCard(Card card, int position) {

        //Fix for Multiple click together
        if(isDeleteClicked)
            return;
        isDeleteClicked = true;
        new Handler().postDelayed(() -> isDeleteClicked = false, 50);

        UiUtils.showLoadingDialog(context);
        PrefUtils preferences = PrefUtils.getInstance(context);
        Map<String, Object> params = new HashMap<>();
        params.put(Params.ID, preferences.getIntValue(PrefKeys.USER_ID, -1));
        params.put(Params.TOKEN, preferences.getStringValue(PrefKeys.SESSION_TOKEN, ""));
        params.put(Params.SUB_PROFILE_ID, preferences.getIntValue(PrefKeys.ACTIVE_SUB_PROFILE, -1));
        params.put(Params.USER_CARD_ID, card.getId());
        params.put(Params.POSITION, position);
        params.put(Params.LANGUAGE, preferences.getStringValue(PrefKeys.APP_LANGUAGE_STRING, ""));

        Call<String> call = apiInterface.deleteCard(APIConstants.APIs.DELETE_CARD, params);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                UiUtils.hideLoadingDialog();
                JSONObject deleteCardResponse = null;
                try {
                    deleteCardResponse = new JSONObject(response.body());
                } catch (Exception e) {
                    UiUtils.log(TAG, Log.getStackTraceString(e));
                }
                if (deleteCardResponse != null)
                    if (deleteCardResponse.optString(Params.SUCCESS).equals(Constants.TRUE)) {
                        UiUtils.showShortToast(context, deleteCardResponse.optString(Params.MESSAGE));
                        for (Card cardItem : cardList) {
                            if (cardItem.getId() == card.getId()) {
                                cardList.remove(cardItem);
                                notifyItemRemoved(position);
                                if (cardListener != null)
                                    cardListener.onCardDeleted(cardList.isEmpty(), position);
                                break;
                            }
                        }
                    } else {
                        UiUtils.showShortToast(context, deleteCardResponse.optString(Params.ERROR_MESSAGE));
                    }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                UiUtils.hideLoadingDialog();
                NetworkUtils.onApiError(context);
            }
        });
    }

    private void makeDefaultCardInBackend(Card card, final RadioButton checkedButton) {

        //Fix for Multiple click together
        if (isMarkDefaultClicked)
            return;
        isMarkDefaultClicked = true;
        new Handler().postDelayed(() -> isMarkDefaultClicked = false, 50);


        UiUtils.showLoadingDialog(context);
        PrefUtils preferences = PrefUtils.getInstance(context);
        Map<String, Object> params = new HashMap<>();
        params.put(Params.ID, preferences.getIntValue(PrefKeys.USER_ID, -1));
        params.put(Params.TOKEN, preferences.getStringValue(PrefKeys.SESSION_TOKEN, ""));
        params.put(Params.SUB_PROFILE_ID, preferences.getIntValue(PrefKeys.ACTIVE_SUB_PROFILE, -1));
        params.put(Params.USER_CARD_ID, card.getId());
        params.put(Params.LANGUAGE, preferences.getStringValue(PrefKeys.APP_LANGUAGE_STRING, ""));

        Call<String> call = apiInterface.makeCardDefault(APIConstants.APIs.MAKE_DEFAULT_CARD, params);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                UiUtils.hideLoadingDialog();
                JSONObject makeDefaultCardResponse = null;
                try {
                    makeDefaultCardResponse = new JSONObject(response.body());
                } catch (Exception e) {
                    UiUtils.log(TAG, Log.getStackTraceString(e));
                }
                if (makeDefaultCardResponse != null)
                    if (makeDefaultCardResponse.optString(Params.SUCCESS).equals(Constants.TRUE)) {
                        UiUtils.showShortToast(context, makeDefaultCardResponse.optString(Params.MESSAGE));
                        if (lastChecked != null) {
                            lastChecked.setChecked(false);
                        }
                        lastChecked = checkedButton;
                        for (Card cardItem : cardList) {
                            cardItem.setDefault(cardItem.getId() == card.getId());
                        }

                        checkedChangeDisableFlag = true;
                        notifyDataSetChanged();
                        new Handler().postDelayed(() -> checkedChangeDisableFlag = false, 100);
                    } else {
                        UiUtils.showShortToast(context, makeDefaultCardResponse.optString(Params.ERROR_MESSAGE));
                        rollBackToPreviousDefaultCard(checkedButton);
                    }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                UiUtils.hideLoadingDialog();
                NetworkUtils.onApiError(context);
                rollBackToPreviousDefaultCard(checkedButton);
            }
        });
    }

    private void rollBackToPreviousDefaultCard(RadioButton checkedButton) {
        checkedButton.setChecked(false);
    }

    public interface CardListener {
        void onCardDeleted(boolean isEmpty, int pos);
    }

    class CardViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.cardNumb)
        TextView cardNumb;
        @BindView(R.id.expiryDate)
        TextView expiryDate;
        @BindView(R.id.isDefault)
        RadioButton isDefault;
        @BindView(R.id.deleteCard)
        View deleteCard;

        CardViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
