package info.abdolahi.libraryapp.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import butterknife.ButterKnife;
import info.abdolahi.libraryapp.R;
import info.abdolahi.libraryapp.db.BookModel;

public class ShowBookDetailDialogFragment extends DialogFragment {


    faveMeDialogListener mCallback;
    Context mContext;
    List<String> realFontList;

    /**
     * interface for remote call
     */

    public interface faveMeDialogListener {
        void onFaveMeListener(int id, boolean isfaved);
    }

    public static ShowBookDetailDialogFragment getInstance(BookModel book) {

        /**
         * create dialog instance
         */
        ShowBookDetailDialogFragment dialog = new ShowBookDetailDialogFragment();

        Bundle b = new Bundle();
        b.putSerializable("data", book);
        dialog.setArguments(b);

        return dialog;
    }

    @Override
    public void onAttach(Activity activity) {

        super.onAttach(activity);
        mContext = activity;

        try {
            mCallback = (faveMeDialogListener) mContext;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement CityChooserDialogListener.");
        }

    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final BookModel mData = (BookModel) getArguments().get("data");

        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());

        dialog.setTitle(mData.getTitle());

        String saveToFave = (mData.getIs_faved() == 0) ? getString(R.string.save_tofaved) : getString(R.string.removed_fromfaved);

        dialog.setPositiveButton(getString(R.string.okay), new positiveButtonClickListener());
        dialog.setNegativeButton(saveToFave, new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                boolean sts = (mData.getIs_faved() == 0);
                mCallback.onFaveMeListener(mData.getId(), sts);
                dismiss();
            }
        });


        View dialoglayout = getActivity().getLayoutInflater().inflate(R.layout.dialog_bookdetail, null);
        dialog.setView(dialoglayout);

        TextView bookpublisher = ButterKnife.findById(dialoglayout, R.id.bookpublisher);
        TextView sarshenase = ButterKnife.findById(dialoglayout, R.id.sarshenase);
        TextView motarjem = ButterKnife.findById(dialoglayout, R.id.motarjem);
        TextView publish_year = ButterKnife.findById(dialoglayout, R.id.publish_year);

        bookpublisher.setText(mData.getPublisher());
        sarshenase.setText(mData.getSarsehnase());
        motarjem.setText(mData.getMotarjem());
        publish_year.setText(mData.getPublish_year());


        return dialog.create();
    }

    class positiveButtonClickListener implements
            OnClickListener {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
        }
    }


}
