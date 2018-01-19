package group16.mad.com.fragmentpractice;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 */
public class FragmentExample extends android.support.v4.app.Fragment {


    public FragmentExample() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this FragmentExample
        return inflater.inflate(R.layout.fragment_fragment, container, false);
    }

}
