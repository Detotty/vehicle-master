package pk.roadpartner.view;

import android.content.Context;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardGridArrayMultiChoiceAdapter;
import it.gmariotti.cardslib.library.internal.multichoice.OptionMultiChoice;
import it.gmariotti.cardslib.library.view.CardView;

/**
 * Created by Gazi Rimon on 4/12/2016.
 */
public class MyCardArrayMultiChoiceAdapter extends CardGridArrayMultiChoiceAdapter {

    public MyCardArrayMultiChoiceAdapter(Context context, List<Card> cards) {
        super(context, cards);
    }

    public MyCardArrayMultiChoiceAdapter(Context context, List<Card> cards, OptionMultiChoice options) {
        super(context, cards, options);
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        return false;
    }

    @Override
    public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked, CardView cardView, Card card) {

    }
}
